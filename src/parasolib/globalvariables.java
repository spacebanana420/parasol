package parasolib;

import bananatui.base;

public class globalvariables {
  public static boolean SHOW_HIDDEN_FILES = false;
  public static boolean DISPLAY_VERTICALLY_ONLY = false;
  public static String PARASOL_VERSION = base.foreground("green")+"0.5"+base.foreground("default"); 

  //  + "   * find [name] - finds entries that contain [name] in their name\n"
  //  + "   * archive - archives all files in current directory\n"
  //  + "      Note: there is currently no implementation to extract the archive, this is an experimental command\n"
  // filter [dir/d/file/f]
  
  public static String getHelpMessage() {
    String default_color = base.foreground("default");
    String green = base.foreground("green");

    return green + "====Parasol Help Menu====" + default_color
    + "\n\nWhile browsing:\n   Press 0 to close the program\n"
    + "   Press 1 to go backwards in your directories\n\nNavigate through directories and open files by typing the number they are assigned to.\n\n"
    + "General usage: parasol [directory-path] [options]\n\n"
    + green+"CLI arguments:\n"+default_color
    + "   -h (--help) - opens this menu\n"
    + "   -H (--hidden) - shows hidden paths\n"
    + "   -home (--home) - opens Parasol on the user's home directory\n"
    + "   -v (--version) - shows Parasol's version\n"
    + "   -V (--vertical) - displays all paths vertically\n"
    + "   -s (--size) [path to file] - displays the size of a file\n\n"
    + green+"List of commands:\n"+default_color
    + "   * help - opens this menu\n"
    + "   * version - displays Parasol's version\n"
    + "   * size [number] - gets the size of the file which is assigned to [number]\n"
    + "   * size-tree - Sorts the files of the current directory by size, from biggest to smallest\n"
    + "   * exec [number] - executes the file which is assigned to [number]\n"
    + "   * find [keyword] - shows all files and directories that contain [keyword] in the name\n"
    + "   * dirs - Displays available directories\n"
    + "   * files - Displays available files\n"
    + "\n"
    + "   * mkdir [name] - creates a directory with name [name]\n"
    + "   * move [file/dir number] [dir number] - moves a file or directory into a directory\n"
    + "   * copy [file number] - copies a file into Parasol's clipboard\n"
    + "   * cut [file number] - cuts a file into Parasol's clipboard\n"
    + "   * paste - pastes the file currently in clipboard into the current directory\n"
    + "   * clear-clipboard - clears the clipboard\n"
    + "   * view-clipboard - views the file that is stored in the clipboard, if any\n"
    + "   * rename [number] [name] - renames the path of value [number] to [name]\n"
    + "   * delete [number] - deletes the path of number [number]\n"
    + "   * mkfile [name] - creates a new file\n"
    + "\n"
    + "   * goto [name] - changes location to the absolute path [name]\n"
    + "   * home - changes location to the user's home directory\n"
    + "   * vertical - toggles displaying all paths vertically\n"
    + "   * tabs - displays currently saved tabs\n"
    + "   * tab - saves the current directory\n"
    + "   * tab [index] - changes directory to the tab's respective directory\n"
    + "   * tab set - changes the path of a tab\n"
    + "   * tab remove - deletes a tab\n"
    + "   * tab clear - clears all tabs\n"
    + "\n";
   }
}
