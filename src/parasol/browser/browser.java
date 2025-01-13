package parasol.browser;

import bananatui.*;
import parasol.global;
import parasol.misc.misc;
import parasol.misc.numops;
import parasol.config.FileRunner;
import parasol.config.config;

import java.io.IOException;
import java.io.File;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;

public class browser {
  public static String BROWSER_DIRECTORY = "";
  
  public static void runBrowser() {
    while (true) {
      String parent = BROWSER_DIRECTORY;
      String[][] subpaths = getPaths(parent);
      String dir_txt = browsertui.formString(parent, subpaths[0], false, 2);
      String file_txt = browsertui.formString(parent, subpaths[1], true, 2+subpaths[0].length);

      String screen = browsertui.buildScreen(parent, dir_txt, file_txt);
      base.clear();
      String answer = userinput.readUserInput(screen).strip();

      if (answer.equals("0")) {return;}
      if (answer.equals("1"))
      {
        String newparent = new File(parent).getParent();
        if (newparent != null) {BROWSER_DIRECTORY = newparent;}
        continue;
      }

      if (!numops.isUint(answer)) 
      {
        commands.runCommand(answer, parent, subpaths);
        continue;
      }
      int answer_i = answerToIndex(answer);

      if (indexLeadsToFile(answer_i, subpaths))
      {
        runner.openFile(parent, returnFile(answer_i, subpaths));
      }
      else if (indexLeadsToDir(answer_i, subpaths))
      {
        BROWSER_DIRECTORY = parent + System.getProperty("file.separator") + returnDir(answer_i, subpaths);
      }
    }
  }
  
  public static String[][] getPaths(String parent) {
    String[] paths = new File(parent).list();
    if (paths == null || paths.length == 0) {return new String[][]{new String[0], new String[0]};}
    
    String[][] result = filterPaths(parent, paths);
    misc.selectionSort(result[0]);
    misc.selectionSort(result[1]);
    return result;
  }

  //ill also use this in command functions
  public static boolean indexLeadsToFile(int i, String[][] paths) {
    return i >= paths[0].length && i - paths[0].length < paths[1].length;
  }
  public static boolean indexLeadsToDir(int i, String[][] paths) {
    return i < paths[0].length && i >= 0;
  }
  public static int answerToIndex(String a) {return userinput.answerToNumber(a)-2;}

  public static String returnDir(int i, String[][] paths) {return paths[0][i];}
  public static String returnFile(int i, String[][] paths) {return paths[1][i-paths[0].length];}
  
  private static String[][] filterPaths(String parent, String[] paths) {
    ArrayList<String> files = new ArrayList<String>();
    ArrayList<String> dirs = new ArrayList<String>();

    for (int i = 0; i < paths.length; i++)
    {
      File f = new File(parent + "/" + paths[i]);
      if (global.SHOW_HIDDEN_FILES || !f.isHidden())
      {
        if (f.isFile()) {files.add(paths[i]);}
        else if (f.isDirectory()) {dirs.add(paths[i]);}
      }
    }
    return new String[][]{dirs.toArray(new String[0]), files.toArray(new String[0])};
  }
}

class runner {
  private static final FileRunner[] FILE_RUNNERS = config.getFileRunners();
  
  public static void openFile(String parent, String file) {
    String full_path = parent + "/" + file;
    for (FileRunner fr : FILE_RUNNERS) 
    {
      if (fr.hasValidExtension(file))
      {
        String[] cmd = fr.buildCommand(full_path);
        execute(cmd);
        return;
      }
    }
    String[] cmd = getRunnerCMD(full_path);
    execute(cmd);
  }

  private static String[] getRunnerCMD(String path) {
    String os = System.getProperty("os.name");
    if (os.contains("Windows"))
      {return new String[]{"explorer.exe", path};}
    else if (os.contains("Mac") || os.equals("Haiku"))
      {return new String[]{"open", path};}
    else
      {return new String[]{"xdg-open", path};}
  }

  public static void execute(String[] command) {
    try
    {
      ProcessBuilder pbuilder = new ProcessBuilder(command);
      pbuilder.redirectOutput(Redirect.DISCARD); //java 9 and later support
      pbuilder.redirectError(Redirect.DISCARD);
      pbuilder.start();
    }
    catch(IOException e) {
      e.printStackTrace();
      String txt = "===Command arguments===\n";
      for (String arg : command) {txt += arg + " ";}
      userinput.pressToContinue(txt+"\n============\n\nFailed to open file!");
    }
  }
}

class browserdata {
  public static String[] file_clipboard = new String[]{"", ""}; //path, name
  public static boolean clipboard_cut = false;

  public static ArrayList<String> browser_tabs = new ArrayList<String>();

  public static void addTab(String path) {browser_tabs.add(path);}
  public static String getTab(int i) {return browser_tabs.get(i);}
  public static void setTab(String path, int i) {browser_tabs.set(i, path);}
  public static void removeTab(int i) {browser_tabs.remove(i);}
  public static void clearTabs() {browser_tabs.clear();}
  public static int tabSize() {return browser_tabs.size();}

  public static String getTabList() {
    if (browser_tabs.isEmpty()) {return "There are currently no saved tabs!";}
    String txt = "Currently saved tabs:\n\n";
    String green = base.foreground("green"); String default_color = base.foreground("default");
    for (int i = 0; i < browser_tabs.size(); i++) {
      String tab = browser_tabs.get(i);
      String num = green + i + ": " + default_color;
      txt = txt + num + tab + "\n";
    }
    return txt;
  }
}
