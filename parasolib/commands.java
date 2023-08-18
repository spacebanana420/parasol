package parasolib;

import java.io.File;
import parasolib.*;

public class commands {

    public static void commandbase(String answer, String[][] paths) {
        switch(answer) {
        case "help":
            displayhelp();
            break;

        default:
            if (answer.contains("size ") == true) {
                getsize(answer, paths);
            }
            else if (answer.contains("find ") == true) {
                browser.findentry(answer.replaceAll("find ", ""));
            }
            else if (answer.contains("exec ") == true) {
                runprogram(answer, paths);
            }
        }
    }

    private static void displayhelp() {
        tui.clearterminal();
        String help = "-----Parasol help menu-----\n\nWhile browsing:\n     Press 0 to close the program\n     Press 1 to go backwards in your directories\n\nNavigate through directories and open files by typing the number they are assigned to.\n\nList of commands:\n     help - opens this menu\n     size [number] - gets the size of the file which is assigned to [number]\n     find [name] - finds entries that contain [name] in their name\n     exec [number] - executes the file which is assigned to [number]\n\nParasol assumes your default programs for the file format in question by integrating explorer.exe (on Windows) and xdg-open on all other operating systems.\nNot all unix-like systems have the xdg-open implementation, and TTY systems are out of question, so OS support beyond Linux and Windows is a mystery.\nIf you encounter an issue, please report it on the Github project so I can bring support to your OS.\n\nPress enter to continue";
        tui.spawn(help);
    }

    private static void getsize(String answer, String[][] paths) {
        String pathnum = answer.replaceAll("size ", "");
        if (numberops.isanumber(pathnum) == false) {
            tui.spawn("Your choice " + pathnum + " is not a number!");
            return;
        }
        int pathval = Integer.parseInt(pathnum);

        String relativepath = browser.findfile_relative(pathval, paths);
        if (relativepath == null) {
            tui.spawn("The file of value " + pathval + " has not been found!");
            return;
        }
        String absolutepath = browser.currentdirectory + "/" + relativepath;

        File pathfile = new File(absolutepath);
        if (pathfile.exists() == false) {
            tui.spawn("The file " + relativepath + " does not exist!");
            return;
        }

        long size_bytes = pathfile.length();
        float roundedsize = size_bytes;
        String unit = "bytes";
        if (size_bytes > 1000000000) {
            roundedsize = size_bytes / 1000000000;
            unit = "GB";
        }
        else if (size_bytes > 1000000) {
            roundedsize = size_bytes / 1000000;
            unit = "MB";
        }
        else if (size_bytes > 1000) {
            roundedsize = size_bytes / 1000;
            unit = "KB";
        }
        tui.spawn("Size of " + relativepath + ": " + roundedsize + " " + unit + "\nPress enter to continue");
    }

    private static void runprogram(String answer, String[][] paths) {
        String pathnum = answer.replaceAll("exec ", "");
        if (numberops.isanumber(pathnum) == false) {
            tui.spawn("Your choice " + pathnum + " is not a number!");
            return;
        }
        int pathval = Integer.parseInt(pathnum);

        String relativepath = browser.findfile_relative(pathval, paths);
        if (relativepath == null) {
            tui.spawn("The file of value " + pathval + " has not been found!");
            return;
        }
        if (new File(browser.currentdirectory + "/" + relativepath).canExecute() == false) {
            tui.spawn("The file " + relativepath + " does not have execute permissions!");
            return;
        }
        String[] command = {browser.currentdirectory + "/" + relativepath};
        System.out.println("Launching " + relativepath);
        if (platform.iswindows == true) {
            command[0] = platform.convertpath(command[0]);
        }
        platform.execute(command);
        tui.presstocontinue();
    }
}
