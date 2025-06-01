package parasol.cli;

import parasol.browser.browsertui;
import bananatui.base;

public class help {
  public static String getHelpMessage() {
    return browsertui.COLOR_GREEN + browsertui.BOLD_ENABLE + "====Parasol Help Menu====" + browsertui.BOLD_DISABLE + browsertui.COLOR_DEFAULT
    + "\n\nWhile browsing:\n   Press 0 to close the program\n"
    + "   Press 1 to go backwards in your directories\n\nNavigate through directories and open files by typing the number they are assigned to.\n\n"
    + "General usage: parasol [directory-path] [options]\n\n"
    + browsertui.COLOR_GREEN+"CLI arguments:\n"+browsertui.COLOR_DEFAULT
    + "   -h (--help)                     opens this menu\n"
    + "   -H (--hidden)                   shows hidden paths\n"
    + "   -home (--home)                  opens Parasol on the user's home directory\n"
    + "   -v (--version)                  shows Parasol's version\n"
    + "   -V (--vertical)                 displays all paths vertically\n"
    + "   -Vf (--full-names)              when displaying paths vertically, do not shorten their names\n"
    + "   -s (--size) [path to file]      displays the size of a file\n"
    + "   -S (--shell)                    opens Parasol's shell instead\n"
    + "   --silent-shell                  does not print the start message when running the shell\n"
    + "   -c (--config)                   open Parasol's main config with a text editor\n"
    + "\n"
    + browsertui.COLOR_GREEN+"List of commands:\n"+browsertui.COLOR_DEFAULT
    + "   * help                                            opens this menu\n"
    + "   * version                                         displays Parasol's version\n"
    + "   * size [number]                                   gets the size of the file/directory which is assigned to [number]\n"
    + "   * size-tree                                       sorts the files of the current directory by size, from biggest to smallest\n"
    + "   * exec [number]                                   executes the file which is assigned to [number]\n"
    + "   * find [keyword]                                  shows all files and directories that contain [keyword] in the name\n"
    + "   * find-strict [keyword]                           shows all files and directories that contain [keyword] in the name (case-sensitive)\n"
    + "   * dirs                                            displays available directories\n"
    + "   * files                                           displays available files\n"
    + "   * count-lines [number]                            counts the lines of a text file\n"
    + "   * config                                          open Parasol's main config with a text editor\n"
    + "   * inherit-io                                      toggles inheriting the standard input and output of programs\n"
    + "   * wait-completion                                 toggles waiting for a program to close\n"
    + "\n"
    + "   * mkdir [dir name 1] [dir name 2] ...             creates one or multiple directories\n"
    + "   * mkfile [file name 1] [file name 2] ...          creates one or multiple files\n"
    + "   * move [number 1] [number 2] ... [destination]    moves one or multiple files or directories into a directory\n"
    + "   * copy [number]                                   copies a file/directory into Parasol's clipboard\n"
    + "   * cut [number]                                    cuts a file/directory into Parasol's clipboard\n"
    + "   * paste                                           pastes the file currently in clipboard into the current directory\n"
    + "   * clear-clipboard                                 clears the clipboard\n"
    + "   * clipboard                                       views the file that is stored in the clipboard, if any\n"
    + "   * rename [number] [name]                          renames the path of value [number] to [name]\n"
    + "   * delete [number 1] [number 2] ...                deletes one or multiple paths according to their number\n"
    + "\n"
    + "   * goto [name]                                     changes location to the absolute path [name]\n"
    + "   * home                                            changes location to the user's home directory\n"
    + "   * vertical                                        toggles displaying all paths vertically\n"
    + "   * hidden                                          toggles viewing hidden paths\n"
    + "   * devices                                         opens a list of disk partitions you can jump to\n"
    + "   * bookmarks                                       opens a list of bookmarks you can jump to\n"
    + "   * add-bookmark                                    adds the current directory to the bookmarks list\n"
    + "   * shell                                           opens an interactive shell\n"
    + "\n"
    + "   * tabs                                            displays currently saved tabs\n"
    + "   * tab                                             saves the current directory\n"
    + "   * tab [index]                                     changes directory to the tab's respective directory\n"
    + "   * tab set                                         changes the path of a tab\n"
    + "   * tab remove                                      deletes a tab\n"
    + "   * tab clear                                       clears all tabs\n"
    ;
   }

   public static String getShellHelp() {
    return browsertui.COLOR_GREEN + browsertui.BOLD_ENABLE + "====Parasol Shell====\n\n" + browsertui.BOLD_DISABLE + browsertui.COLOR_DEFAULT
    + "Command list:\n"
    + "   :h - opens this menu\n"
    + "   help - opens Parasol's main help menu\n"
    + "   :q, :quit or exit - exits the shell\n"
    + "   :l - repeats last command \n"
    + "   :v - views last command\n";
   }
}
