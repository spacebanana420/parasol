package parasolib;

import bananatui.base;

public class globalvariables {
  public static boolean SHOW_HIDDEN_FILES = false;
  public static boolean DISPLAY_VERTICALLY_ONLY = false;
  public static String PARASOL_VERSION = "0.2"; 

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
    + "   -v (--version) - shows Parasol's version\n"
    + "   -V (--vertical) - displays all paths vertically\n\n"
    + green+"List of commands:\n"+default_color
    + "   * help - opens this menu\n"
    + "   * size [number] - gets the size of the file which is assigned to [number]\n"
    + "   * exec [number] - executes the file which is assigned to [number]\n"
    + "   * mkdir [name] - creates a directory with name [name]\n"
    + "   * rename [number] [name] - renames the path of value [number] to [name]\n"
    + "   * goto [name] - changes location to the absolute path [name]\n"
    + "   * delete [number] - deletes the path of number [number]\n"
    + "   * vertical - toggles displaying all paths vertically\n\n";
   }
}
