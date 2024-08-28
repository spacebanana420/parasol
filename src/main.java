import parasolib.*;
import java.io.File;

import bananatui.base;
import bananatui.extra;

public class main {
  public static void main(String[] args) {
    if (System.getProperty("os.name").contains("Windows")) {extra.windows_enableASCII();}
    if (checkForHelp(args) || checkForVersion(args) || checkForSize(args)) {return;}
    globalvariables.SHOW_HIDDEN_FILES = showHiddenFiles(args);
    globalvariables.DISPLAY_VERTICALLY_ONLY = displayVertically(args);
    
    String parent = getPath(args);
    browser.runBrowser(parent);
  }

  private static boolean displayVertically(String[] args) {
    for (String a : args) {
      if (a.equals("-V") || a.equals("--vertical")) {return true;} 
    }
    return false; 
  }

  private static boolean checkForSize(String[] args) {
    for (int i = 0; i < args.length; i++) {
      if (
        (args[i].equals("-s") || args[i].equals("--size"))
        && i < args.length-1
        && new File(args[i+1]).isFile() && new File(args[i+1]).canRead() 
      ) {
        commands.printSize(args[i+1]); return true;
      }
    }
    return false;
  }

  private static boolean checkForVersion(String[] args) {
    for (String a : args) {
      if (a.equals("-v") || a.equals("--version")) {
        base.println("Parasol version " + globalvariables.PARASOL_VERSION);
        return true;
      } 
    }
    return false;
  }

  private static boolean checkForHelp(String[] args) {
    for (String a : args) {
      if (a.equals("-h") || a.equals("--help")) {
        String help = globalvariables.getHelpMessage();
        base.println(help);
        return true;
      }
    }
    return false;
  }

  private static String getPath(String[] args) {
    String absoluteParent = new File("").getAbsolutePath();
    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("-home") || args[i].equals("--home")) {
        return System.getProperty("user.home");
      }
      if (new File(args[i]).isDirectory()) {return new File(args[i]).getAbsolutePath();}
      if (args[i].equals("")) {return absoluteParent;}
    }
    return absoluteParent;
  }

  private static boolean showHiddenFiles(String[] args) {
    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("-H") || args[i].equals("--hidden")) {
        //finish
        return true;
      } 
    }
    return false; 
  }
}
