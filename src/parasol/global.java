package parasol;

import parasol.browser.browsertui;
import parasol.config.config;
import parasol.config.FileRunner;
import parasol.config.ConfLine;
import parasol.config.ExcludedFiles;
import parasol.cli.cli;

import java.util.ArrayList;

public class global {
  public static final String PARASOL_VERSION = browsertui.COLOR_GREEN+"1.2"+browsertui.COLOR_DEFAULT;
  public static final char FILE_SEPARATOR = System.getProperty("file.separator").charAt(0);
  
  public static boolean SHOW_HIDDEN_FILES = false;
  public static boolean DISPLAY_VERTICALLY_ONLY = false;
  public static boolean SHELL_SILENT = false;
  public static boolean DETECT_FOREIGN_CHARACTERS = false;
  public static boolean VERTICAL_DISPLAY_FULL_PATHS = false;
  
  public static FileRunner[] FILE_RUNNERS;
  public static ExcludedFiles EXCLUDED_EXTENSIONS;
  public static boolean PROCESS_INHERIT_IO = false;
  public static boolean PROCESS_WAIT_FOR_COMPLETION = false;
  
  public static void assignValues(String[] args) {
    ConfLine[] conf = config.readBaseConfig();
    var t1 = new Thread(() ->
    {
      FILE_RUNNERS = config.getFileRunners(conf);
      EXCLUDED_EXTENSIONS = config.getExcludedExtensions(conf);
      PROCESS_INHERIT_IO = config.processInheritIO(conf);
      PROCESS_WAIT_FOR_COMPLETION = config.processWaitForCompletion(conf);
    });
    t1.start();

    SHELL_SILENT = config.silentShell(conf) || cli.silentShell(args);
    SHOW_HIDDEN_FILES = config.showHiddenPaths(conf) || cli.showHiddenFiles(args);
    DISPLAY_VERTICALLY_ONLY = config.displayPathsVertically(conf) || cli.displayVertically(args);
    DETECT_FOREIGN_CHARACTERS = config.checkForeignChars(conf) || cli.checkForeignChars(args);
    VERTICAL_DISPLAY_FULL_PATHS = config.displayFullNames(conf) || cli.displayFullPaths(args);
    
    try {t1.join();}
    catch (InterruptedException e) {e.printStackTrace();}
  }
}
