package parasol;

import bananatui.base;
import parasol.config.config;
import parasol.config.FileRunner;

import java.util.ArrayList;

public class global {
  public static boolean SHOW_HIDDEN_FILES = false;
  public static boolean DISPLAY_VERTICALLY_ONLY = false;
  public static String PARASOL_VERSION = base.foreground("green")+"1.0"+base.foreground("default"); 
  public static boolean SHELL_SILENT = false;
  
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
    
    var t2 = new Thread(() ->
    {
      SHELL_SILENT = config.silentShell(conf);
      SHELL_SILENT = cli.silentShell(args);
      
      SHOW_HIDDEN_FILES = config.showHiddenPaths(conf);
      SHOW_HIDDEN_FILES = cli.showHiddenFiles(args);
      
      DISPLAY_VERTICALLY_ONLY = config.displayPathsVertically(conf);
      DISPLAY_VERTICALLY_ONLY = cli.displayVertically(args);
    });
    
    t1.start(); t2.start();
    try {t1.join(); t2.join();}
    catch (InterruptedException e) {e.printStackTrace();}
  }
  
  public static String getHelpMessage() {
    String default_color = base.foreground("default");
    String green = base.foreground("green");
    String bold_on = base.boldMode(true);
    String bold_off = base.boldMode(false);

    return green + bold_on + "====Parasol Help Menu====" + bold_off + default_color
    + "\n\nWhile browsing:\n   Press 0 to close the program\n"
    + "   Press 1 to go backwards in your directories\n\nNavigate through directories and open files by typing the number they are assigned to.\n\n"
    + "General usage: parasol [directory-path] [options]\n\n"
    + green+"CLI arguments:\n"+default_color
    + "   -h (--help) - opens this menu\n"
    + "   -H (--hidden) - shows hidden paths\n"
    + "   -home (--home) - opens Parasol on the user's home directory\n"
    + "   -v (--version) - shows Parasol's version\n"
    + "   -V (--vertical) - displays all paths vertically\n"
    + "   -s (--size) [path to file] - displays the size of a file\n"
    + "   -S (--shell) - opens Parasol's shell instead\n"
    + "   --silent-shell - does not print the start message when running the shell\n"
    + "   -c (--config) - open Parasol's main config with a text editor\n"
    + "\n"
    + green+"List of commands:\n"+default_color
    + "   * help - opens this menu\n"
    + "   * version - displays Parasol's version\n"
    + "   * size [number] - gets the size of the file/directory which is assigned to [number]\n"
    + "   * size-tree - Sorts the files of the current directory by size, from biggest to smallest\n"
    + "   * exec [number] - executes the file which is assigned to [number]\n"
    + "   * find [keyword] - shows all files and directories that contain [keyword] in the name\n"
    + "   * find-strict [keyword] - shows all files and directories that contain [keyword] in the name (case-sensitive)\n"
    + "   * dirs - Displays available directories\n"
    + "   * files - Displays available files\n"
    + "   * config - open Parasol's main config with a text editor\n"
    + "\n"
    + "   * mkdir [name] - creates a directory with name [name]\n"
    + "   * mkfile [name] - creates a new file\n"
    + "   * move [file/dir number] [dir number] - moves a file or directory into a directory\n"
    + "   * copy [number] - copies a file/directory into Parasol's clipboard\n"
    + "   * cut [number] - cuts a file/directory into Parasol's clipboard\n"
    + "   * paste - pastes the file currently in clipboard into the current directory\n"
    + "   * clear-clipboard - clears the clipboard\n"
    + "   * clipboard - views the file that is stored in the clipboard, if any\n"
    + "   * rename [number] [name] - renames the path of value [number] to [name]\n"
    + "   * delete [number] - deletes the path of number [number]\n"
    + "\n"
    + "   * goto [name] - changes location to the absolute path [name]\n"
    + "   * home - changes location to the user's home directory\n"
    + "   * vertical - toggles displaying all paths vertically\n"
    + "   * hidden - toggles viewing hidden paths\n"
    + "   * devices - opens a list of disk partitions you can jump to\n"
    + "   * bookmarks - opens a list of bookmarks you can jump to\n"
    + "   * add-bookmark - adds the current directory to the bookmarks list\n"
    + "   * shell - opens an interactive shell\n"
    + "\n"
    + "   * tabs - displays currently saved tabs\n"
    + "   * tab - saves the current directory\n"
    + "   * tab [index] - changes directory to the tab's respective directory\n"
    + "   * tab set - changes the path of a tab\n"
    + "   * tab remove - deletes a tab\n"
    + "   * tab clear - clears all tabs\n"
    + "\n";
   }

   public static String getShellHelp() {
    String default_color = base.foreground("default");
    String green = base.foreground("green");

    return green + base.boldMode(true) + "====Parasol Shell====\n\n" + base.boldMode(false) + default_color
    + "Command list:\n"
    + "   :h - opens this menu\n"
    + "   help - opens Parasol's main help menu\n"
    + "   :q, :quit or exit - exits the shell\n"
    + "   :l - repeats last command \n"
    + "   :v - views last command\n";
   }
}
