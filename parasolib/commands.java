package parasolib;

import java.io.File;
import parasolib.*;

public class commands {

    public static void commandbase(String answer, String[][] paths) {
        switch(answer) {
        case "help":
            displayhelp();
            break;

        default: //get absolute path from NUMBER
            if (answer.contains("size ") == true) {
                String pathnum = getvaluefromanswer(answer, "size ");
                if (numberops.isanumber(pathnum) == false) {
                    tui.spawn("Your choice " + pathnum + " is not a number!");
                    return;
                }
                int pathval = Integer.parseInt(pathnum);
                String absolutepath = browser.findpath_absolute(pathval, paths);
                String relativepath = browser.findpath_relative(pathval, paths);
                if (absolutepath == null) {
                    tui.spawn("The path of value " + pathval + " has not been found!");
                    return;
                }
                File pathfile = new File(absolutepath);
                if (pathfile.exists() == false) {
                    tui.spawn("The file " + relativepath + " does not exist!");
                    return;
                }
                float filesize = numberops.getfilesize(pathfile);
                tui.spawn("Size of " + relativepath + ": " + filesize);
            }
        }
    }

    private static void displayhelp() {
        tui.clearterminal();
        String help = "------Parasol help menu-----\n\nWhile browsing:\n     Press 0 to close the program\n     Press 1 to go backwards in your directories\n\nNavigate through directories and open files by typing the number they are assigned to.\n\nList of commands:\n    help - opens this menu\n     size [number] - gets the size of the file\n\nParasol assumes your default programs for the file format in question by integrating explorer.exe (on Windows) and xdg-open on all other operating systems.\nNot all unix-like systems have the xdg-open implementation, and TTY systems are out of question, so OS support beyond Linux and Windows is a mystery.\nIf you encounter an issue, please report it on the Github project so I can bring support to your OS.\n\nPress enter to continue";
        tui.spawn(help);
    }

    private static String getvaluefromanswer(String answer, String cmd) {
        String value = "";
        String temp = "";
        boolean startcopying = false;

        for (int i = 0; i < answer.length(); i++) {
            if (temp == cmd) {
                startcopying = true;
            }
            if (startcopying == true) {value += answer.charAt(i);}
            else {temp += answer.charAt(i);}
        }
        return value;
    }
}
