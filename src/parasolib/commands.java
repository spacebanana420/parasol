package parasolib;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import bananatui.*;

public class commands {
  public static boolean runCommand(String cmd_str, String parent, String[][] paths) { //parasol commands, not system processes
    switch (cmd_str) {
      case "help":
        displayhelp();
        break;
      case "archive":
        break;
      default:
        if (misc.startsWith(cmd_str, "size ")) {
          String[] args = misc.groupStrings(cmd_str);
          if (args.length >= 2 && numops.isUint(args[1])) {
            int i = browser.answerToIndex(args[1]);
            if (browser.indexLeadsToFile(i, paths)) {
              getsize(parent + "/" + browser.returnFile(i, paths));
            }
          }
        }
        else if (misc.startsWith(cmd_str, "mkdir ")) {
          String[] args = misc.groupStrings(cmd_str);
          if (args.length >= 2) {
            String mkdir_path = parent + "/" + args[1];
            new File(mkdir_path).mkdir();
            userinput.pressToContinue("Created directory at " + mkdir_path);
          }
        }
        else if (misc.startsWith(cmd_str, "rename ")) {
          String[] args = misc.groupStrings(cmd_str);
          if (args.length >= 3) { //incomplete
            int i = browser.answerToIndex(args[1]);
            String new_name = parent + "/" + args[2];
            String old_name = "";
            if (browser.indexLeadsToDir(i, paths)) {old_name = browser.returnDir(i, paths);}
            else if (browser.indexLeadsToFile(i, paths)) {old_name = browser.returnFile(i, paths);}

            if (old_name != "") {
              String full_path = parent + "/" + old_name;
              new File(full_path).renameTo(new File(new_name));
            }
            else {return true;}
            userinput.pressToContinue("Renamed path " + old_name + " (of number " + args[1] + ") to " + args[2]);
          }
        }
        else if (misc.startsWith(cmd_str, "exec ")) {
          String[] args = misc.groupStrings(cmd_str);
          if (args.length >= 2) {
            int i = browser.answerToIndex(args[1]);
            String file_path =
              (browser.indexLeadsToFile(i, paths)) ? parent + "/" + browser.returnFile(i, paths) : "";
            if (file_path != "" && new File(file_path).canExecute()) {
              runner.execute(new String[]{file_path});
            }
            else {userinput.pressToContinue("The file does not exist or is not executable!");}
          }
        }
        else if (misc.startsWith(cmd_str, "goto ")) {
          String[] args = misc.groupStrings(cmd_str);
          if (args.length >= 2 && new File(args[1]).isDirectory()) {
            browser.runBrowser(new File(args[1]).getAbsolutePath());
            return false;
          }
        }
        else if (misc.startsWith(cmd_str, "delete ")) {
          String[] args = misc.groupStrings(cmd_str);
          if (args.length >= 2 && new File(args[1]).isFile()) {
            if (new File(args[1]).canWrite()
            && numops.isUint(args[1])
            && browser.indexLeadsToFile(browser.answerToIndex(args[1]), paths)) {
              int i = browser.answerToIndex(args[1]);
              String file = browser.returnFile(i, paths);
              if (userinput.askPrompt("The file " + file + " will be deleted, this is not reversible. Proceed?", false)) {
                try{
                  Files.delete(Path.of(file));
                  userinput.pressToContinue("The file " + file + "has been deleted!");
                }
                catch(IOException e) {e.printStackTrace(); userinput.pressToContinue("");}                
              }
            }
          }
        }
        break;
    }
    return true;
  }

  private static void getsize(String path) {
    File pathfile = new File(path);
    if (!pathfile.isFile() || !pathfile.canRead()) {
      userinput.pressToContinue("The file " + path + " does not exist, isn't a file or cannot be read!");
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
    userinput.pressToContinue("Size of " + path + ":\n" + roundedsize + " " + unit);
  }

  private static void displayhelp() {
    base.clear();
    String help = globalvariables.getHelpMessage();
    userinput.pressToContinue(help);
  }
}