package parasol;

import parasol.browser.browsertui;
import parasol.config.config;
import parasol.config.FileRunner;
import parasol.cli.cli;

import java.util.ArrayList;

public class global {
  public static final String PARASOL_VERSION = browsertui.COLOR_GREEN+"1.1.2"+browsertui.COLOR_DEFAULT;
  
  public static boolean SHOW_HIDDEN_FILES = false;
  public static boolean DISPLAY_VERTICALLY_ONLY = false;
  public static boolean SHELL_SILENT = false;
  public static boolean DETECT_FOREIGN_CHARACTERS = false;
  
  public static FileRunner[] FILE_RUNNERS;
  public static boolean PROCESS_INHERIT_IO = false;
  public static boolean PROCESS_WAIT_FOR_COMPLETION = false;
  
  public static void assignValues(String[] args) {
    ArrayList<String> conf = config.readBaseConfig();
    var t1 = new Thread(() ->
    {
      FILE_RUNNERS = config.getFileRunners(conf);
      PROCESS_INHERIT_IO = config.processInheritIO(conf);
      PROCESS_WAIT_FOR_COMPLETION = config.processWaitForCompletion(conf);
    });
    t1.start();
    
    boolean temp = false;
    SHELL_SILENT = config.silentShell(conf);
    temp = cli.silentShell(args);
    if (temp != false) {SHELL_SILENT = temp;}
    
    SHOW_HIDDEN_FILES = config.showHiddenPaths(conf);
    temp = cli.showHiddenFiles(args);
    if (temp != false) {SHOW_HIDDEN_FILES = temp;}
    
    DISPLAY_VERTICALLY_ONLY = config.displayPathsVertically(conf);
    temp = cli.displayVertically(args);
    if (temp != false) {DISPLAY_VERTICALLY_ONLY = temp;}
    
    DETECT_FOREIGN_CHARACTERS = config.checkForeignChars(conf);
    temp = cli.checkForeignChars(args);
    if (temp != false) {DETECT_FOREIGN_CHARACTERS = temp;}
    
    try {t1.join();}
    catch (InterruptedException e) {e.printStackTrace();}
  }
}
