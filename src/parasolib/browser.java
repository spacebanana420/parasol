package parasolib;

import bananatui.*;
import java.io.File;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;

public class browser {
  private static String color_green = base.foreground("green");
  private static String color_default = base.foreground("default");
  private static String addNumberStr(int n) {return color_green + n + ": " + color_default;}

  private static String[][] filterPaths(String parent, String[] paths) {
    ArrayList<String> files = new ArrayList<String>();
    ArrayList<String> dirs = new ArrayList<String>();

    for (int i = 0; i < paths.length; i++) {
      File f = new File(parent + "/" + paths[i]);
      if (globalvariables.SHOW_HIDDEN_FILES || !f.isHidden()) {
        if (f.isFile()) {files.add(paths[i]);}
        else if (f.isDirectory()) {dirs.add(paths[i]);}
      }
    }
    return new String[][]{dirs.toArray(new String[0]), files.toArray(new String[0])};
  }

  public static String formString(String parent, String[] paths, boolean checkFiles, int baseI) {
    String s = (checkFiles) ? "===Files===\n" : "===Directories===\n";
    int column_size = 0;
    for (int i = 0; i < paths.length; i++) {
      File f = new File(parent + "/" + paths[i]);
      String num = addNumberStr(i+baseI);
      if ((checkFiles && f.isFile()) || (!checkFiles && f.isDirectory())) {
        String path_element = num + paths[i]; path_element = shortenName(path_element);
        String separator = (column_size >= 1 || globalvariables.DISPLAY_VERTICALLY_ONLY) ? "\n" : mkEmptySpace(path_element.length());

        s = s + path_element + separator;
        if (column_size < 1) {column_size += 1;} else {column_size = 0;}
      }
    }
    if (!globalvariables.DISPLAY_VERTICALLY_ONLY && column_size != 0) {
      return s + "\n\n";
    }
    else {return s + "\n";}
  }

  public static String mkEmptySpace(int len) {
    String empty_space = "";
    for (int i = 0; i < 65-len; i++) {
      empty_space += " ";
    }
    return empty_space;
  }

  private static String shortenName(String name) {
    int max_length = (globalvariables.DISPLAY_VERTICALLY_ONLY) ? 90 : 55;
    if (name.length() < max_length) {return name;}
    String buf = "";
    for (int i = 0; i < max_length-1; i++) {
      buf += name.charAt(i);
    }
    return buf + "[...]";
  }

  public static String[][] getPaths(String parent) {
    String[] paths = new File(parent).list();
    if (paths == null || paths.length == 0) {
      return new String[][]{};
    }
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

  public static String browser_directory = "";
  public static void runBrowser() {
    while (true) {
      String parent = browser_directory;
      String[][] subpaths = getPaths(parent);
      String dir_txt = formString(parent, subpaths[0], false, 2);
      String file_txt = formString(parent, subpaths[1], true, 2+subpaths[0].length);

      base.clear();
      String screen = parent + "\n\n" + addNumberStr(0) + "Exit\t\t" + addNumberStr(1) + "Go back\n\n" + dir_txt + file_txt;
      String answer = userinput.readUserInput(screen).strip();

      if (answer.equals("0")) {return;}
      if (answer.equals("1")) {
        String newparent = new File(parent).getParent();
        if (newparent != null) {browser_directory = newparent;}
        continue;
      }

      if (!numops.isUint(answer)) {
        commands.runCommand(answer, parent, subpaths);
        continue;
      }
      int answer_i = answerToIndex(answer);

      if (indexLeadsToFile(answer_i, subpaths)) {
        runner.openFile(parent, returnFile(answer_i, subpaths));
      }
      else if (indexLeadsToDir(answer_i, subpaths)) {
        browser_directory = parent + System.getProperty("file.separator") + returnDir(answer_i, subpaths);
      }
    }
  }
}

class runner {
  public static void openFile(String parent, String file) {
    String full_path = parent + "/" + file;
    String[] cmd = getRunnerCMD(full_path);
    execute(cmd);
  }

  private static String[] getRunnerCMD(String path) {
    String os = System.getProperty("os.name");
    if (os.contains("Windows")) {return new String[]{"explorer.exe", path};}
    else if (os.contains("Mac")) {return new String[]{"open", path};}
    else {return new String[]{"xdg-open", path};}
  }

  public static void execute(String[] command) {
    try {
      ProcessBuilder pbuilder = new ProcessBuilder(command);
      pbuilder.redirectOutput(Redirect.DISCARD); //java 9 and later
      pbuilder.redirectError(Redirect.DISCARD);
      pbuilder.start();
    }
    catch(Exception e) {e.printStackTrace();}
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