package parasolib;

import bananatui.base;

public class globalvariables {
  public static boolean SHOW_HIDDEN_FILES = false;
  public static boolean DISPLAY_VERTICALLY_ONLY = false;
  public static String PARASOL_VERSION = "0.3"; 

  //  + "   * find [name] - finds entries that contain [name] in their name\n"
  //  + "   * archive - archives all files in current directory\n"
  //  + "      Note: there is currently no implementation to extract the archive, this is an experimental command\n"
  // filter [dir/d/file/f]
  
  public static String getHelpMessage() {
    String default_color = base.foreground("default");
    String green = base.foreground("green");

    return green + "=====Parasol help menu=====" + default_color
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
    + "\n"
    + "   * mkdir [name] - creates a directory with name [name]\n"
    + "   * move [file number] [dir number] - moves a file into a directory\n"
    + "   * copy [file number] - copies a file into Parasol's clipboard\n"
    + "   * paste - pastes the file currently in clipboard into the current directory\n"
    + "   * clear-clipboard - clears the clipboard\n"
    + "   * view-clipboard - views the file that is stored in the clipboard, if any\n"
    + "   * rename [number] [name] - renames the path of value [number] to [name]\n"
    + "   * delete [number] - deletes the path of number [number]\n"
    + "   * create-file [name] - creates a new file\n"
    + "\n"
    + "   * goto [name] - changes location to the absolute path [name]\n"
    + "   * home - changes location to the user's home directory\n"
    + "   * vertical - toggles displaying all paths vertically\n\n";
   }
}
