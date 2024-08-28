package parasolib;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

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
      case "home":
        browser.runBrowser(System.getProperty("user.home")); return false;
      case "clear-clipboard":
        browserdata.file_clipboard = new String[]{"", ""};
        break;
      case "view-clipboard":
        String name = browserdata.file_clipboard[1];
        String path = browserdata.file_clipboard[0];
        if (name == "" || path == "") {userinput.pressToContinue("The clipboard is empty!");}
        else {userinput.pressToContinue("Clipboard file: " + name + "\nPath: " + path);}
        break;
      case "paste":
        if (browserdata.file_clipboard[0] == "") {
          userinput.pressToContinue("The clipboard is empty!");
          return true;
        }
        String new_path = misc.generateFileName(parent, browserdata.file_clipboard[1]);
        Path source = Path.of(browserdata.file_clipboard[0] + "/" + browserdata.file_clipboard[1]);
        Path target = Path.of(new_path);
        try {
          Files.copy(source, target);
          userinput.pressToContinue("File " + browserdata.file_clipboard[1] + " has been pasted!");
        }
        catch (IOException e) {
          String message =
            "An error occurred while attempting to paste the file\n"
            + "Make sure you have read and write permissions for your source file and the current directory.";
          userinput.pressToContinue(message);
        }
        break;
      case "dirs":
        String dirs_txt = browser.formString(parent, paths[0], false, 2);
        base.clear();
        userinput.pressToContinue("Displaying directories:\n\n" + dirs_txt);
        break;
      case "files":
        String files_txt = browser.formString(parent, paths[1], true, 2+paths[0].length);
        base.clear();
        userinput.pressToContinue("Displaying files:\n\n" + files_txt);
        break;
      case "tab":
        browserdata.addTab(parent);
        break;
      case "tabs":
        String txt = browserdata.getTabList();
        userinput.pressToContinue(txt);
        break;
      case "tab clear":
        browserdata.clearTabs();
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
        else if (misc.startsWith(cmd_str, "find ")) {
          String[] args = misc.groupStrings(cmd_str);
          if (args.length < 2) {return true;}
          String[][] filtered_paths = filterByMatch(args[1], paths);
          String dir_txt = formString_findCommand(filtered_paths[0], false, 2);
          String file_txt = formString_findCommand(filtered_paths[1], true, 2+filtered_paths[0].length);
          String screen =
            "Keyword: " + base.foreground("green") + args[1] + base.foreground("default")
            + "\nThe following paths have been found:\n\n"
            + dir_txt + file_txt;
          base.clear();
          userinput.pressToContinue(screen);
        }
        else if (misc.startsWith(cmd_str, "mkdir ")) {
          String[] args = misc.groupStrings(cmd_str);
          if (args.length < 2) { return true;}

          String mkdir_path = parent + "/" + args[1];
          new File(mkdir_path).mkdir();
          userinput.pressToContinue("Created directory at " + mkdir_path);
        }
        else if (misc.startsWith(cmd_str, "move ")) {
          moveCommand(cmd_str, parent, paths);
        }
        else if (misc.startsWith(cmd_str, "copy ")) {
          String[] args = misc.groupStrings(cmd_str);
          if (args.length < 2) {return true;}
          int i = browser.answerToIndex(args[1]);
          if (!browser.indexLeadsToFile(i, paths)) {return true;}
          String file_name = browser.returnFile(i, paths);
          String file_path = parent;
          browserdata.file_clipboard = new String[]{file_path, file_name};
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
          if (args.length < 2) {return true;}

          int file_index = browser.answerToIndex(args[1]);
          if (browser.indexLeadsToFile(file_index, paths)) {
            String file_name = browser.returnFile(file_index, paths);
            deleteFile(file_name, parent + "/" + file_name);
          }
          else if (browser.indexLeadsToDir(file_index, paths)) {
            String dir_name = browser.returnDir(file_index, paths);
            String message = "You are about to delete the directory " + dir_name + " and all contents inside it. Proceed? ";
            if (userinput.askPrompt(message, false)) {
              base.print("Deleting directory...");
              boolean result = deleteDirectory(parent + "/" + dir_name);
              if (result) {userinput.pressToContinue("The directory " + dir_name + " has been deleted!");}
              else {userinput.pressToContinue("An error has occurred finishing the deletion of the directory\nMaybe you lack write permission!");}
            }
          }
        }
        else if (misc.startsWith(cmd_str, "mkfile ")){
          String args[] = misc.groupStrings(cmd_str);
          if (args.length < 2) {return true;}
          String filename = misc.generateFileName(parent, args[1]);
          try {
            Files.createFile(Path.of(filename));
            userinput.pressToContinue("File " + filename + " has been created!");
          }
          catch(IOException e) {e.printStackTrace(); userinput.pressToContinue("");}
        }
        else if (misc.startsWith(cmd_str, "tab ")) {
          String[] args = misc.groupStrings(cmd_str);
          if (args.length < 2) {return true;}
          int i = userinput.answerToNumber(args[1]);
          if (i < 0 || i >= browserdata.tabSize()) {return true;}
          String tab_path = browserdata.getTab(i);
          browser.runBrowser(tab_path); return false;
        }
        else if (misc.startsWith(cmd_str, "tab set ")) {
          String[] args = misc.groupStrings(cmd_str);
          if (args.length < 2) {return true;}
          int i = userinput.answerToNumber(args[1]);
          if (i < 0 || i >= browserdata.tabSize()) {return true;}
          browserdata.setTab(parent, i);
          return true;          
        }
        else if (misc.startsWith(cmd_str, "tab remove ")) {
          String[] args = misc.groupStrings(cmd_str);
          if (args.length < 2) {return true;}
          int i = userinput.answerToNumber(args[1]);
          if (i < 0 || i >= browserdata.tabSize()) {return true;}
          browserdata.removeTab(i);
          return true;          
        }
        break;
    }
    return true;
  }

  public static void printSize(String path) {
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

  public static void deleteFile(String name, String full_path) {
    if (!new File(full_path).canWrite()) {
      userinput.pressToContinue("The file cannot be deleted, it lacks write permissions!");
      return;
    }
    if (!userinput.askPrompt("The file " + name + " will be deleted, this is not reversible. Proceed? ", false)) {return;}
    try {
      Files.delete(Path.of(full_path));
      userinput.pressToContinue("The file " + name + " has been deleted!");
    }
    catch(IOException e) {e.printStackTrace(); userinput.pressToContinue("");}
  }

  private static void moveCommand(String cmd_str, String parent, String[][] paths) {
    String args[] = misc.groupStrings(cmd_str);
    if (args.length < 3) {return;}
    int source_i = browser.answerToIndex(args[1]);
    int target_i = browser.answerToIndex(args[2]);
    if (!browser.indexLeadsToDir(target_i, paths) && args[2] != "1") {return;}
    
    if (browser.indexLeadsToFile(source_i, paths)) {
      String file_name = browser.returnFile(source_i, paths);
      String dir_name = browser.returnDir(target_i, paths);
      if (args[3] == "1") {
        dir_name = new File(parent).getParent();
        new File(parent + "/" + file_name).renameTo(new File(dir_name + "/" + file_name));
      }
      else {
        new File(parent + "/" + file_name).renameTo(new File(parent + "/" + dir_name + "/" + file_name));
      }
      userinput.pressToContinue(file_name + " has been moved to " + dir_name);
    }
    else if (browser.indexLeadsToDir(source_i, paths) && source_i != target_i) {
      String source_name = browser.returnDir(source_i, paths);
      String full_source = parent + "/" + source_name;
      String target_name = "";
      String full_target = "";
      if (args[3] == "1") {
        full_target = new File(parent).getParent() + "/" + source_name;
        target_name = full_target;
      }
      else {
        target_name = browser.returnDir(target_i, paths);
        full_target = parent + "/" + target_name + "/" + source_name;
      }

      base.println("Moving directory " + source_name);
      if (args[3] == "1") {}
      
      boolean result = moveDirectory(full_source, full_target);
      if (result) {userinput.pressToContinue("Directory has been moved to " + target_name + "/" + source_name);}
      else {userinput.pressToContinue("The operation has failed! Maybe you lack file read and write permissions!");}
    }
  }

  public static boolean deleteDirectory(String path) {
    String[] path_list = new File(path).list();
    try {
      for (String p : path_list) {
        String full_path = path + "/" + p;
        File pf = new File(full_path);
        if (pf.isFile()) {Files.delete(Path.of(full_path));}
        else {deleteDirectory(full_path);}
      }
      Files.delete(Path.of(path));
      return true;
    } catch(IOException e) {e.printStackTrace(); return false;}
  }

  public static boolean moveDirectory(String path, String target) {
    String[] path_list = new File(path).list();
    if (!new File(target).isDirectory()) {
      new File(target).mkdir();
    }
    try {
      for (String p : path_list) {
        String full_path = path + "/" + p;
        String target_path = target + "/" + p;
        Path pp = Path.of(full_path);
        Path tp = Path.of(target_path);

        if (new File(full_path).isFile()) {Files.move(pp, tp);}
        else {moveDirectory(full_path, target + "/" + p);}
      }
      Files.delete(Path.of(path));
      return true;
    } catch(IOException e) {e.printStackTrace(); return false;}
  }

  private static String[][] filterByMatch(String keyword, String[][] paths) {
    ArrayList<String> files = new ArrayList<String>();
    ArrayList<String> dirs = new ArrayList<String>();
    int i = 2;
    for (String d : paths[0]) {
      String num = base.foreground("green") + i + ": " + base.foreground("default");
      if (d.contains(keyword)) {dirs.add(num+d);}
      i+=1;
    }
    for (String f : paths[1]) {
      String num = base.foreground("green") + i + ": " + base.foreground("default");
      if (f.contains(keyword)) {files.add(num+f);}
      i+=1;
    }
    return new String[][]{dirs.toArray(new String[0]), files.toArray(new String[0])};
  }

  private static String formString_findCommand(String[] paths, boolean checkFiles, int baseI) {
    String s = (checkFiles) ? "===Files===\n" : "===Directories===\n";
    for (String p : paths) {s = s + p + "\n";}
    return s + "\n";
  }
}