import parasolib.*;
import java.io.File;

import bananatui.base;
import bananatui.extra;

public class main {
  public static void main(String[] args) {
    if (System.getProperty("os.name").contains("Windows")) {extra.windows_enableASCII();}
    if (
      checkForHelp(args)
      || checkForVersion(args)
      || checkForSize(args))
      {return;}

    globalvariables.SHELL_SILENT = checkForArgument(args, "--silent-shell");
    globalvariables.SHOW_HIDDEN_FILES = showHiddenFiles(args);
    globalvariables.DISPLAY_VERTICALLY_ONLY = displayVertically(args);
    
    browser.browser_directory = getPath(args);
    config.initialize();
    if (!checkShell(args)) {browser.runBrowser();}
  }

  private static boolean displayVertically(String[] args) {
    for (String a : args) {
      if (a.equals("-V") || a.equals("--vertical")) {return true;} 
    }
    return false; 
  }

  private static boolean checkForSize(String[] args) {
    for (int i = 0; i < args.length; i++) {
      if ((args[i].equals("-s") || args[i].equals("--size")) && i < args.length-1) {
        var f = new File(args[i+1]);
        if (!f.exists() || !f.canRead()) {continue;}
        if (f.isFile()) {commands.printSize_file(args[i+1]);}
        else {commands.printSize_dir(args[i+1]);}
        return true;
      }
    }
    return false;
  }

  private static boolean checkForVersion(String[] args) {
    boolean result = checkForArgument(args, new String[]{"-v", "--version"});
    if (result) {
      base.println("Parasol version " + globalvariables.PARASOL_VERSION);
    }
    return result;
  }

  private static boolean checkForHelp(String[] args) {
    boolean result = checkForArgument(args, new String[]{"-h", "--help"});
    if (result) {
      String help = globalvariables.getHelpMessage();
      base.println(help);
    }
    return result;
  }

  private static boolean checkShell(String[] args) {
    boolean result = checkForArgument(args, new String[]{"-S", "--shell"});
    if (result) {shell.runShell();}
    return result;
  }

  private static boolean checkForArgument(String[] args, String[] keywords) {
    for (String a : args) {
      for (String k : keywords) {if (a.equals(k)) {return true;}}
    }
    return false;
  }

  private static boolean checkForArgument(String[] args, String keyword) {
    for (String a : args) {
      if (a.equals(keyword)) {return true;}
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
