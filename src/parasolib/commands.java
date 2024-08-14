package parasolib;

import bananatui.*;

public class commands {
  public static void runCommand(String cmd_str) { //parasol commands, not system processes
    switch (cmd_str.trim()) {
      case "help":
        displayhelp();
        break;
      case "archive":
        break;
    }
  }

  private static void displayhelp() {
    base.clear();
    String help = "-----Parasol help menu-----\n\nWhile browsing:\n   Press 0 to close the program\n   Press 1 to go backwards in your directories\n\nNavigate through directories and open files by typing the number they are assigned to.\n\nList of commands:\n   * help - opens this menu\n   * size [number] - gets the size of the file which is assigned to [number]\n   * find [name] - finds entries that contain [name] in their name\n   * exec [number] - executes the file which is assigned to [number]\n   * archive - archives all files in current directory\n      Note: there is currently no implementation to extract the archive, this is an experimental command\n   * mkdir [name] - creates a directory with name [name]\n   * rename [number] [name] - renames the path of value [number] to [name]\n   * goto [name] - changes location to the absolute path [name]\n   * delete [number] - deletes the path of number [number]\n\nParasol assumes your default programs for the file format in question by integrating explorer.exe (on Windows) and xdg-open on all other operating systems.\nNot all unix-like systems have the xdg-open implementation, and TTY systems are out of question, so OS support beyond Linux and Windows is a mystery.\nIf you encounter an issue, please report it on the Github project so I can bring support to your OS.";
    userinput.pressToContinue(help);
  }
}