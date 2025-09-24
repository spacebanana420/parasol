package parasol.cli;

import parasol.commands.cmd_filesize;
import java.io.File;

public class cli {
  public static boolean checkForSize(String[] args) {
    for (int i = 0; i < args.length; i++) {
      if ((args[i].equals("-s") || args[i].equals("--size")) && i < args.length-1) {
        var f = new File(args[i+1]);
        if (!f.exists() || !f.canRead()) {continue;}
        if (f.isFile()) {cmd_filesize.printSize_file(args[i+1], true);}
        else {cmd_filesize.printSize_dir(args[i+1], true);}
        return true;
      }
    }
    return false;
  }

  public static boolean checkForVersion(String[] args) {return checkForArgument(args, "-v", "--version");}
  public static boolean checkForHelp(String[] args) {return checkForArgument(args, "-h", "--help");}
  public static boolean checkShell(String[] args) {return checkForArgument(args, "-S", "--shell");}
  public static boolean silentShell(String[] args) {return checkForArgument(args, "--silent-shell");}
  public static boolean checkOpenConfig(String[] args) {return checkForArgument(args, "-c", "--config");}
  public static boolean displayVertically(String[] args) {return checkForArgument(args, "-V", "--vertical");}
  public static boolean checkForeignChars(String[] args) {return checkForArgument(args, "--check-foreign-characters");}
  public static boolean displayFullPaths(String[] args) {return checkForArgument(args, "-Vf", "--full-names");}

  public static String getPath(String[] args) {
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

  public static boolean showHiddenFiles(String[] args) {return checkForArgument(args, "-H", "--hidden");}
  
  private static boolean checkForArgument(String[] args, String keyword) {
    for (String a : args) {if (a.equals(keyword)) {return true;}}
    return false;
  }

  private static boolean checkForArgument(String[] args, String... keywords) {
    for (String a : args) {
      for (String k : keywords) {if (a.equals(k)) {return true;}}
    }
    return false;
  }
}
