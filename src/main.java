import parasolib.*;
import java.io.File;

import bananatui.base;
import bananatui.extra;

public class main {
  public static void main(String[] args) {
    if (System.getProperty("os.name").contains("Windows")) {extra.windows_enableASCII();}
    if (checkForHelp(args)) {return;}
    globalvariables.SHOW_HIDDEN_FILES = showHiddenFiles(args);

    browser.runBrowser(getPath(args));
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
      if (new File(args[i]).isDirectory()) {
        return new File(args[i]).getAbsolutePath();
      }
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
