package parasol.browser;

import bananatui.*;
import parasol.global;
import parasol.misc.misc;
import parasol.misc.numops;
import parasol.config.*;
import parasol.commands.commands;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class browser {
  public static String BROWSER_DIRECTORY = "";
  
  public static void runBrowser() {
    while (true) {
      String[][] subpaths = getPaths(BROWSER_DIRECTORY);
      boolean forceVertical = global.DETECT_FOREIGN_CHARACTERS && (hasForeignCharacters(subpaths[0]) || hasForeignCharacters(subpaths[1]));
      
      String screen = browsertui.buildScreen(BROWSER_DIRECTORY, subpaths[0], subpaths[1], forceVertical);
      String answer = userinput.spawnAndRead(screen).strip();

      if (answer.length() == 0) {continue;}
      if (answer.equals("0")) {return;}
      if (answer.equals("1"))
      {
        String newparent = new File(BROWSER_DIRECTORY).getParent();
        if (newparent != null) {BROWSER_DIRECTORY = newparent;}
        continue;
      }
      if (!numops.isUint(answer)) {commands.runCommand(answer, BROWSER_DIRECTORY, subpaths); continue;}
      
      int answer_i = answerToIndex(answer);
      if (indexLeadsToFile(answer_i, subpaths)) {
        platform.openFile(BROWSER_DIRECTORY, returnFile(answer_i, subpaths));
      }
      else if (indexLeadsToDir(answer_i, subpaths)){
        BROWSER_DIRECTORY = BROWSER_DIRECTORY + global.FILE_SEPARATOR + returnDir(answer_i, subpaths);
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
        if (f.isFile() && !global.EXCLUDED_EXTENSIONS.isExcluded(paths[i])) {files.add(paths[i]);}
        else if (f.isDirectory()) {dirs.add(paths[i]);}
      }
    }
    return new String[][]{dirs.toArray(new String[0]), files.toArray(new String[0])};
  }
  
  public static void openConfig() {platform.openFile("", confio.getConfigFile());}
  
  private static boolean hasForeignCharacters(String[] paths) {
    for (String p : paths)
    {
      for (int i = 0; i < p.length(); i++) {
        char c = p.charAt(i);
        if ((short)c != (byte)c) {return true;}
      }
    }
    return false;
  }
}
