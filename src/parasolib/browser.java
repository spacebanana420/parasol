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

  private static String formString(String parent, String[] paths, boolean checkFiles, int baseI) {
    String s = (checkFiles) ? "===Files===\n" : "===Directories===\n";
    for (int i = 0; i < paths.length; i++) {
      File f = new File(parent + "/" + paths[i]);
      String num = addNumberStr(i+baseI);
      if ((checkFiles && f.isFile()) || (!checkFiles && f.isDirectory())) {
        s = s + num + paths[i] + "\n";
      }
    }
    return s + "\n";
  }

  private static String[][] getPaths(String parent) {
    String[] paths = new File(parent).list();
    if (paths == null || paths.length == 0) {
      return new String[][]{};
    }
    String[][] result = filterPaths(parent, paths);
    misc.selectionSort(result[0]);
    misc.selectionSort(result[1]);
    return result;
  }

  public static void runBrowser(String parent) {
    String[][] subpaths = getPaths(parent);
    String dir_txt = formString(parent, subpaths[0], false, 2);
    String file_txt = formString(parent, subpaths[1], true, 2+subpaths[0].length);

    base.clear();
    String screen = parent + "\n\n" + addNumberStr(0) + "Exit\t\t" + addNumberStr(1) + "Go back\n\n" + dir_txt + file_txt;
    String answer = userinput.readUserInput(screen).strip();

    if (answer.equals("0")) {return;}
    if (answer.equals("1")) {
      String newparent = new File(parent).getParent();
      if (newparent != null) {runBrowser(newparent);}
      else {runBrowser(parent);}
      return;
    }

    if (!numops.isUint(answer)) {
      commands.runCommand(answer); runBrowser(parent); return;
    }
    int answer_i = userinput.answerToNumber(answer)-2;

    if (answer_i >= subpaths[0].length && answer_i - subpaths[0].length < subpaths[1].length) {
      runner.openFile(parent, subpaths[1][answer_i-subpaths[0].length]);
      runBrowser(parent);
    }
    else if (answer_i < subpaths[0].length) {
      runBrowser(parent + System.getProperty("file.separator") + subpaths[0][answer_i]);
    }
    else {runBrowser(parent);}
  }
}

class runner { //finish
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

  private static void execute(String[] command) {
    try {
      ProcessBuilder pbuilder = new ProcessBuilder(command);
      pbuilder.redirectOutput(Redirect.DISCARD); //java 9 and later
      pbuilder.redirectError(Redirect.DISCARD);
      pbuilder.start();
    }
    catch(Exception e) {e.printStackTrace();}
  }
}