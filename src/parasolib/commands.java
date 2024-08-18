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
      case "version":
        userinput.pressToContinue("Parasol version " + globalvariables.PARASOL_VERSION);
      break;
      // case "archive":
      //   break;
      case "vertical":
        globalvariables.DISPLAY_VERTICALLY_ONLY = !globalvariables.DISPLAY_VERTICALLY_ONLY;
        break;
      case "size-tree":
        sortFilesBySize(parent, paths[1]);
        break;
      default:
        if (misc.startsWith(cmd_str, "size ")) {
          String[] args = misc.groupStrings(cmd_str);
          if (args.length < 2 || !numops.isUint(args[1])) {return true;}

          int i = browser.answerToIndex(args[1]);
          if (browser.indexLeadsToFile(i, paths)) {
            printSize(parent + "/" + browser.returnFile(i, paths));
          }
        }
        else if (misc.startsWith(cmd_str, "mkdir ")) {
          String[] args = misc.groupStrings(cmd_str);
          if (args.length < 2) { return true;}

          String mkdir_path = parent + "/" + args[1];
          new File(mkdir_path).mkdir();
          userinput.pressToContinue("Created directory at " + mkdir_path);
        }
        else if (misc.startsWith(cmd_str, "move ")) {
          String args[] = misc.groupStrings(cmd_str);
          if (args.length < 3) {return true;}
          int file_i = browser.answerToIndex(args[1]);
          int dir_i = browser.answerToIndex(args[2]);
          if (!browser.indexLeadsToFile(file_i, paths) || (!browser.indexLeadsToDir(dir_i, paths) && args[2] != "1")) {return true;}
          String file_name = browser.returnFile(file_i, paths);
          String dir_name = browser.returnDir(dir_i, paths);
          if (args[3] == "1") {
            dir_name = new File(parent).getParent();
            new File(parent + "/" + file_name).renameTo(new File(dir_name + "/" + file_name));
          }
          else {
            new File(parent + "/" + file_name).renameTo(new File(parent + "/" + dir_name + "/" + file_name));
          }
          userinput.pressToContinue(file_name + " has been moved to " + dir_name);
        }
        else if (misc.startsWith(cmd_str, "rename ")) {
          String[] args = misc.groupStrings(cmd_str);
          if (args.length < 3) {return true;} //incomplete
          int i = browser.answerToIndex(args[1]);
          String new_name = parent + "/" + args[2];
          String old_name = "";
          if (browser.indexLeadsToDir(i, paths)) {old_name = browser.returnDir(i, paths);}
          else if (browser.indexLeadsToFile(i, paths)) {old_name = browser.returnFile(i, paths);}

          if (old_name == "") {return true;}
          String full_path = parent + "/" + old_name;
          new File(full_path).renameTo(new File(new_name));
          userinput.pressToContinue("Renamed path " + old_name + " (of number " + args[1] + ") to " + args[2]);
        }
        else if (misc.startsWith(cmd_str, "exec ")) {
          String[] args = misc.groupStrings(cmd_str);
          if (args.length < 2) {return true;}
          int i = browser.answerToIndex(args[1]);
          String file_path =
            (browser.indexLeadsToFile(i, paths)) ? parent + "/" + browser.returnFile(i, paths) : "";
          if (file_path != "" && new File(file_path).canExecute()) {
            runner.execute(new String[]{file_path});
          }
          else {userinput.pressToContinue("The file does not exist or is not executable!");}
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
                try {
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

  private static void printSize(String path) {
    File pathfile = new File(path);
    if (!pathfile.isFile() || !pathfile.canRead()) {
      userinput.pressToContinue("The file " + path + " does not exist, isn't a file or cannot be read!");
      return;
    }
    String roundedsize = roundSize(getFileSize(path));
    userinput.pressToContinue(
      "Size of " + path 
      + ":\n" + base.foreground("green") + roundedsize + base.foreground("default"));
  }

  private static String roundSize(long size) {
    float roundedsize = size;
    String unit = "bytes";
    if (size > 1000000000) {
      roundedsize = size / 1000000000f;
      unit = "GB";
    }
    else if (size > 1000000) {
      roundedsize = size / 1000000f;
      unit = "MB";
    }
    else if (size > 1000) {
      roundedsize = size / 1000f;
      unit = "KB";
    }
    return roundedsize + " " + unit;
  }

  private static long getFileSize(String path) {return new File(path).length();}

  private static void displayhelp() {
    base.clear();
    String help = globalvariables.getHelpMessage();
    userinput.pressToContinue(help);
  }

  private static void sortFilesBySize(String parent, String[] files) {
    if (files.length == 0) {userinput.pressToContinue("There are no files in this directory!"); return;}
    long[] fileSizes = new long[files.length];
    String[] files_with_size = new String[files.length];

    for (int i = 0; i < files.length; i++) {
      fileSizes[i] = getFileSize(parent + "/" + files[i]);
      files_with_size[i] =
        files[i] + "  "
        + base.foreground("green")
        + roundSize(fileSizes[i])
        + base.foreground("default");
    }
    sortSizes(files_with_size, fileSizes);
    String screen =
      "Sorting files by size:\n\n" + formSizeTreeString(parent, files_with_size);
    
    base.clear();
    userinput.pressToContinue(screen);
  }

  public static void sortSizes(String[] files, long[] sizes) {
    String temp = "";
    long templ = 0;
    for (int i = 0; i < files.length; i++) {
      int biggest = findBiggestFile(sizes, i);
      if (i != biggest) {
        temp = files[i];
        files[i] = files[biggest];
        files[biggest] = temp;

        templ = sizes[i];
        sizes[i] = sizes[biggest];
        sizes[biggest] = templ;
      }
    }
  }
  private static int findBiggestFile(long[] sizes, int i) {
    int biggest_i = i;
    for (int c = i; c < sizes.length; c++) {
      if (sizes[c] > sizes[biggest_i]) {biggest_i = c;}
    }
    return biggest_i;
  }

  public static String formSizeTreeString(String parent, String[] paths) {
    String s = "===Files===\n";
    for (int i = 0; i < paths.length; i++) {
      String num = base.foreground("green") + (i+1) + ": " + base.foreground("default");
      s = s + num + paths[i] + "\n";
    }
    return s;
  }
}