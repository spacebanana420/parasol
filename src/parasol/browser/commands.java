package parasol.browser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import parasol.config.config;
import parasol.config.confio;
import parasol.global;
import parasol.misc.*;
import parasol.cli.help;

import bananatui.*;

public class commands {
  public static void runCommand(String cmd_str, String parent, String[][] paths) {
    switch (cmd_str) {
      case "help":
        displayhelp();
        return;
      case "version":
        userinput.pressToContinue("Parasol version " + global.PARASOL_VERSION);
        return;
      // case "archive":
      //   break;
      case "vertical":
        global.DISPLAY_VERTICALLY_ONLY = !global.DISPLAY_VERTICALLY_ONLY;
        return;
      case "size-tree":
        sortFilesBySize(parent, paths[1]);
        return;
      case "home":
        browser.BROWSER_DIRECTORY = System.getProperty("user.home");
        return;
      case "clear-clipboard":
        browserdata.clearClipboard();
        return;
      case "clipboard":
        if (browserdata.clipboardIsEmpty()) {userinput.pressToContinue("The clipboard is empty!"); return;}
        String name = browserdata.clipboardName();
        String path = browserdata.clipboardPath();
        userinput.pressToContinue("Clipboard file: " + name + "\nPath: " + path);
        return;
      case "paste":
        if (browserdata.clipboardIsEmpty()) {
          userinput.pressToContinue("The clipboard is empty!");
          return;
        }
        String name_temp = browserdata.clipboardName();
        String old_path = browserdata.clipboardPath() + "/" + name_temp;
        String new_path = misc.generateFileName(parent, name_temp);
        if (old_path.equals(new_path)) {return;}
        
        String path_type = ""; String operation_type = ""; 
          if (browserdata.clipboard_cut) {
            operation_type = "cut!";
            if (new File(old_path).isDirectory()) {
              path_type = "Directory ";
              boolean result = fileops.moveDirectory(old_path, new_path);
              if (!result) {fileops.printError(); return;}
            }
            else {
              path_type = "File ";
              boolean result = fileops.moveFile(old_path, new_path);
              if (!result) {fileops.printError(); return;}
            }
            browserdata.clearClipboard();
          }
          else {
            operation_type = "pasted!";
            if (new File(old_path).isDirectory()) {
              path_type = "Directory ";
              boolean result = fileops.copyDirectory(old_path, new_path);
              if (!result) {fileops.printError(); return;}
            }
            else {
              path_type = "File ";
              boolean result = fileops.copyFile(old_path, new_path);
              if (!result) {fileops.printError(); return;}
            }
          }
          userinput.pressToContinue(path_type + name_temp + " has been " + operation_type);
        return;
      case "dirs":
        String dirs_txt = browsertui.formString(parent, paths[0], false, 2, false);
        base.clear();
        userinput.pressToContinue("Displaying directories:\n\n" + dirs_txt);
        return;
      case "files":
        String files_txt = browsertui.formString(parent, paths[1], true, 2+paths[0].length, false);
        base.clear();
        userinput.pressToContinue("Displaying files:\n\n" + files_txt);
        return;
      case "tab":
        browserdata.addTab(parent);
        return;
      case "tabs":
        String txt = browserdata.getTabList();
        userinput.pressToContinue(txt);
        return;
      case "tab clear":
        browserdata.clearTabs();
        return;
      case "tab set":
        changeTab(parent);
        return;
      case "tab remove":
        removeTabs();
        return;
      case "devices":
        String[] devices = platform.getSystemDisks();
        if (devices.length == 0) {break;}
        String d = userinput.chooseOption_string(devices, "Choose a device to go to", "Cancel");
        if (!d.equals("")) {browser.BROWSER_DIRECTORY = d; return;}
        return;
      case "bookmarks":
        String[] bookmarks = config.getBookmarks();
        if (bookmarks.length == 0) {
          base.clear();
          userinput.pressToContinue(
            "You didn't add any bookmarks to parasol!"
            + "\nTo add bookmarks, create a file at " + confio.CONFIG_PATH + System.getProperty("file.separator") + "bookmarks.parasol"
            + " and add directory paths, one per line"
            + "\n\nExample:\n/home/user\n/path/to/games\n/home/user/music"
          );
          return;
        }
        String b = userinput.chooseOption_string(bookmarks, "Choose a place to go to", "Cancel");
        if (!b.equals("")) {browser.BROWSER_DIRECTORY = b; return;}
        return;
      case "add-bookmark":
        config.addBookmark(browser.BROWSER_DIRECTORY);
        userinput.pressToContinue(
          "The current directory has been added to the bookmarks list at\n" + confio.CONFIG_PATH + "/bookmarks.parasol"
          );
        return;
      case "shell":
        shell.runShell();
        return;
      case "hidden":
        global.SHOW_HIDDEN_FILES = !global.SHOW_HIDDEN_FILES;
        return;
      case "config":
        browser.openConfig();
        return;
    }
        
    String[] args = misc.groupStrings(cmd_str);
    int buffer_i = 0; //Cases share scope, so this variable is used by multiple commands
    if (args.length < 2) {return;}
    switch(args[0]) {
      case "size":
        if (!numops.isUint(args[1])) {return;}
        buffer_i = browser.answerToIndex(args[1]);
        if (browser.indexLeadsToFile(buffer_i, paths)) {
          printSize_file(parent + "/" + browser.returnFile(buffer_i, paths));
        }
        else if (browser.indexLeadsToDir(buffer_i, paths)) {
          printSize_dir(parent + "/" + browser.returnDir(buffer_i, paths));
        }
        break;
      case "find":
        findPaths(args, paths, false);
        break;
      case "find-strict":
        findPaths(args, paths, true);
        break;
      case "mkdir":
        String mkdir_path = parent + "/" + args[1];
        new File(mkdir_path).mkdir();
        userinput.pressToContinue("Created directory at " + mkdir_path);
        break;
      case "move":
        moveCommand(cmd_str, parent, paths);
        break;
      case "copy":
        copyToClipboard(args, paths, parent, false);
        break;
      case "cut":
        copyToClipboard(args, paths, parent, true);
        break;
      case "rename":
        if (args.length < 3) {return;} //incomplete
        buffer_i = browser.answerToIndex(args[1]);
        String new_name = parent + "/" + args[2];
        String old_name = "";
        if (browser.indexLeadsToDir(buffer_i, paths)) {old_name = browser.returnDir(buffer_i, paths);}
        else if (browser.indexLeadsToFile(buffer_i, paths)) {old_name = browser.returnFile(buffer_i, paths);}

        if (old_name.equals("")) {return;}
        String full_path = parent + "/" + old_name;
        if (full_path.equals(new_name)) {return;}
        new File(full_path).renameTo(new File(new_name));
        userinput.pressToContinue("Renamed path " + old_name + " (of number " + args[1] + ") to " + args[2]);
        break;
      case "exec":
        buffer_i = browser.answerToIndex(args[1]);
        String file_path =
          (browser.indexLeadsToFile(buffer_i, paths)) ? parent + "/" + browser.returnFile(buffer_i, paths) : "";
        if (file_path != "" && new File(file_path).canExecute()) {
          platform.execute(new String[]{file_path});
        }
        else {userinput.pressToContinue("The file does not exist or is not executable!");}
        break;
      case "goto":
        File f = new File(args[1]);
        if (f.isDirectory()) {browser.BROWSER_DIRECTORY = f.getAbsolutePath();}
        break;
      case "delete":
        deleteCommand(args, parent, paths);
        break;
      case "mkfile":
        String filename = misc.generateFileName(parent, args[1]);
        try {
          Files.createFile(Path.of(filename));
          userinput.pressToContinue("File " + filename + " has been created!");
        }
        catch(IOException e) {e.printStackTrace(); userinput.pressToContinue("");}
        break;
      case "tab":
        buffer_i = userinput.answerToNumber(args[1]);
        if (buffer_i < 0 || buffer_i >= browserdata.tabSize()) {return;}
        String tab_path = browserdata.getTab(buffer_i);
        browser.BROWSER_DIRECTORY = tab_path;
        break;
    }
  }

  public static void printSize_file(String path) {
    File pathfile = new File(path);
    if (!pathfile.isFile() || !pathfile.canRead()) {
      userinput.pressToContinue("The file " + path + " does not exist, isn't real or cannot be read!");
      return;
    }
    String roundedsize = roundSize(getFileSize(path));
    userinput.pressToContinue(
      "Size of " + path 
      + ":\n" + browsertui.COLOR_GREEN + roundedsize + browsertui.COLOR_DEFAULT);
  }

  public static void printSize_dir(String path) {
    File pathfile = new File(path);
    if (!pathfile.isDirectory() || !pathfile.canRead()) {
      userinput.pressToContinue("The directory " + path + " does not exist, isn't real or cannot be read!");
      return;
    }
    String roundedsize = roundSize(getDirSize(path));
    userinput.pressToContinue(
      "Size of " + path 
      + ":\n" + browsertui.COLOR_GREEN + roundedsize + browsertui.COLOR_DEFAULT);
  }

  private static String roundSize(long size) {
    double scaledsize = size;
    String unit = " bytes";
    boolean roundNumber = false;
    if (size > 1000000000) {scaledsize = size / 1000000000f; unit = " GB"; roundNumber = true;}
    else if (size > 1000000) {scaledsize = size / 1000000f; unit = " MB"; roundNumber = true;}
    else if (size > 1000) {scaledsize = size / 1000f; unit = " KB"; roundNumber = true;}
    
    if (roundNumber){scaledsize = Math.floor(scaledsize * 1000)/1000f;}
    return scaledsize + unit;
  }

  private static long getFileSize(String path) {return new File(path).length();}

  public static long getDirSize(String path) {
    String[] path_list = new File(path).list();
    long dir_size = 0L;
    for (String p : path_list) {
      String full_path = path + "/" + p; File pf = new File(full_path);
      if (pf.isFile()) {dir_size += pf.length();}
      else {dir_size += getDirSize(full_path);}
    }
    return dir_size;
  }

  private static void displayhelp() {
    base.clear();
    userinput.pressToContinue(help.getHelpMessage());
  }

  private static void sortFilesBySize(String parent, String[] files) {
    if (files.length == 0) {userinput.pressToContinue("There are no files in this directory!"); return;}
    long[] fileSizes = new long[files.length];
    String[] files_with_size = new String[files.length];

    for (int i = 0; i < files.length; i++) {
      fileSizes[i] = getFileSize(parent + "/" + files[i]);
      files_with_size[i] =
        files[i] + "  "
        + browsertui.COLOR_GREEN
        + roundSize(fileSizes[i])
        + browsertui.COLOR_DEFAULT;
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
      String num = browsertui.COLOR_GREEN + (i+1) + ": " + browsertui.COLOR_DEFAULT;
      s = s + num + paths[i] + "\n";
    }
    return s;
  }
  
  private static void deleteCommand(String[] args, String parent, String[][] paths) {
    if (args.length < 2) { //some prior skipping is happening?
      userinput.pressToContinue("Not enough arguments!");
      return;
    }
    int[] indexes = new int[args.length];
    boolean[] isFile = new boolean[args.length];
    boolean[] isDir = new boolean[args.length];
    String[] obtainedPaths = new String[args.length]; //processing everything beforehand to ask for user confirmation
    String skipped = "";
    boolean addedAnyPaths = false;
    for (int i = 1; i < args.length; i++) {indexes[i] = -1;}
    for (int i = 1; i < args.length; i++) {
      int index = browser.answerToIndex(args[i]);
      if (invalidIndex(index, i, indexes)) {continue;}
      indexes[i] = index;
      isFile[i] = browser.indexLeadsToFile(indexes[i], paths);
      if (isFile[i]) {
       obtainedPaths[i] = browser.returnFile(indexes[i], paths);
       addedAnyPaths = true;
      }
      else {
        isDir[i] = browser.indexLeadsToDir(indexes[i], paths);
        if (!isDir[i]) {continue;}
        obtainedPaths[i] = browser.returnDir(indexes[i], paths);
        addedAnyPaths = true;
      }
    }
    
    String txt = "The following files/directories will be deleted:\n";
    for (int i = 1; i < args.length; i++) {
      if (!isFile[i] && !isDir[i]){
        skipped += "Skipping invalid or duplicated argument: " + args[i] + "\n";
        continue;
      }
      txt += "  * " + obtainedPaths[i] + "\n";
    }
    txt += "\nDeleting them is irreversible, proceed?";
    if (!addedAnyPaths) {
      if (skipped.length() > 0) {
        skipped += "\nYou haven't provided any valid argument, make sure you add the number of the files/directories you want to delete";
        userinput.pressToContinue(skipped);
      }
      return;
    }
    else if (skipped.length() > 0) {txt = skipped + "\n" + txt;} 
    if (!userinput.askPrompt(txt, false)) {return;}
    
    txt = "";
    for (int i = 1; i < args.length; i++)
    {
      int file_index = indexes[i];
      if (isFile[i]) {
        String file_name = obtainedPaths[i];
        String full_path = parent + "/" + file_name;
        if (!new File(full_path).canWrite()) {
          txt += "The file " + file_name + " cannot be deleted, it lacks write permissions!\n";
          continue;
        }
        try {
          Files.delete(Path.of(full_path));
          txt += "The file " + file_name + " has been deleted\n";
        }
        catch(IOException e) {e.printStackTrace();}
      }
      else if (isDir[i]) {
        String dir_name = obtainedPaths[i];
        boolean result = fileops.deleteDirectory(parent + "/" + dir_name);
        if (result) {txt += "The directory " + dir_name + " has been deleted\n";}
        else {txt += "Failed to delete the directory " + dir_name + ", maybe it lacks write permissions!\n";}
      }
    }
    userinput.pressToContinue(txt);
  }

  private static void moveCommand(String cmd_str, String parent, String[][] paths) {
    String args[] = misc.groupStrings(cmd_str);
    if (args.length < 3) {
      userinput.pressToContinue("Not enough arguments!");
      return;
    }
    
    int target = args.length-1;
    int target_i = browser.answerToIndex(args[target]);
    boolean targetIsParent = args[target].equals("1");
    if (!browser.indexLeadsToDir(target_i, paths) && !targetIsParent) {
      userinput.pressToContinue("Destination path does not lead to a directory!");
      return;
    }
    String target_dir = (targetIsParent) ? null : browser.returnDir(target_i, paths);
    
    for (int i = 1; i < target; i++) {
      if (args[i].equals(args[target]) || args[i].equals("1")) {
        userinput.pressToContinue("Input path must not be the same as destination!");
        return;
      }
    }
    
    int[] indexes = new int[args.length]; //stores previously-used indexes so duplicates are ignored
    for (int i = 0; i < indexes.length; i++) {indexes[i] = -1;} //negative values are unused
    
    String txt = "";
    for (int i = 1; i < target; i++) //first value is the command
    {
      int source_i = browser.answerToIndex(args[i]);
      if (invalidIndex(source_i, i, indexes)) {
        txt += "Skipping argument " + args[i] + ": already-used path or not a path number\n";
        continue;
      }
      indexes[i] = source_i;
      if (browser.indexLeadsToFile(source_i, paths)) {
        txt += moveFile(browser.returnFile(source_i, paths), target_dir, parent, targetIsParent) + "\n";
      }
      else if (browser.indexLeadsToDir(source_i, paths)) {
        txt += moveDirectory(browser.returnDir(source_i, paths), target_dir, parent, targetIsParent) + "\n";
      }
    }
    txt += "\nFinished moving all paths";
    userinput.pressToContinue(txt);
  }
  
  private static boolean invalidIndex(int index, int current_i, int[] indexes) {
    if (index < 0) {return true;}
    for (int i = 1; i < current_i; i++) { //current_i prevents iterating uninitialized values
      if (index == indexes[i]) {return true;}
    }
    return false;
  }
  
  private static String moveFile(String source_name, String target_name, String parent, boolean moveOutsideParent) {
      if (moveOutsideParent) {
        target_name = new File(parent).getParent();
        new File(parent + "/" + source_name).renameTo(new File(target_name + "/" + source_name));
      }
      else {
        new File(parent + "/" + source_name).renameTo(new File(parent + "/" + target_name + "/" + source_name));
      }
      return (source_name + " has been moved to " + target_name);
  }
  
  private static String moveDirectory(String source_name, String target_name, String parent, boolean moveOutsideParent) {
      String full_source = parent + "/" + source_name;
      String full_target = "";
      if (moveOutsideParent) {
        target_name = new File(parent).getParent();
        full_target = target_name + "/" + source_name;
      }
      else {
        full_target = parent + "/" + target_name + "/" + source_name;
      }
      
      base.println("Moving directory " + source_name);
      boolean result = fileops.moveDirectory(full_source, full_target);
      if (result) {return "Directory has been moved to " + target_name + "/" + source_name;}
      else {return "The operation has failed! Maybe you lack file read and write permissions!";}
  }

  private static String[][] filterByMatch(String keyword, String[][] paths, boolean strict) {
    ArrayList<String> files = new ArrayList<String>();
    ArrayList<String> dirs = new ArrayList<String>();
    int i = 2;
    String keyword_match = (strict) ? keyword : keyword.toLowerCase();

    for (String d : paths[0]) {
      String num = browsertui.COLOR_GREEN + i + ": " + browsertui.COLOR_DEFAULT;
      if (
        (d.contains(keyword_match) && strict)
        || (d.toLowerCase().contains(keyword_match) && !strict)
        ) {dirs.add(num+d);}
      i+=1;
    }
    for (String f : paths[1]) {
      String num = browsertui.COLOR_GREEN + i + ": " + browsertui.COLOR_DEFAULT;
      if (
        (f.contains(keyword_match) && strict)
        || (f.toLowerCase().contains(keyword_match) && !strict)
        ) {files.add(num+f);}
      i+=1;
    }
    return new String[][]{dirs.toArray(new String[0]), files.toArray(new String[0])};
  }

  private static String formString_findCommand(String[] paths, boolean checkFiles, int baseI) {
    String s = (checkFiles) ? "===Files===\n" : "===Directories===\n";
    for (String p : paths) {s = s + p + "\n";}
    return s + "\n";
  }

  private static void removeTabs() { //must be improved, can't cancel operation
    String txt = browserdata.getTabList();
    if (browserdata.tabSize() == 0) {
      userinput.pressToContinue(txt);
      return;
    }
    String answer = userinput.readUserInput(txt + "\nChoose a tab to remove");
    int i = userinput.answerToNumber(answer);
    if (i >= 0 && i < browserdata.tabSize()) {
      browserdata.removeTab(i);
    }
  }
  private static void changeTab(String parent) {
    String txt = browserdata.getTabList();
    if (browserdata.tabSize() == 0) {
      userinput.pressToContinue(txt);
      return;
    }
    String answer = userinput.readUserInput(txt + "\nChoose a tab to change its directory");
    int i = userinput.answerToNumber(answer);
    if (i >= 0 && i < browserdata.tabSize()) {
      browserdata.setTab(parent, i);;
    }
  }

  private static void copyToClipboard(String[] args, String[][] paths, String parent, boolean cut) {
    int i = browser.answerToIndex(args[1]);
    String file_name = "";
    boolean path_valid = false;
    if (browser.indexLeadsToFile(i, paths)) {
      path_valid = true;
      file_name = browser.returnFile(i, paths);
    }
    else if (browser.indexLeadsToDir(i, paths)) {
      path_valid = true;
      file_name = browser.returnDir(i, paths);
    }
    if (!path_valid) {return;}
     
    String file_path = parent;
    browserdata.setClipboard(file_path, file_name);
    browserdata.clipboard_cut = cut;
  }

  private static void findPaths(String[] args, String[][] paths, boolean strict) {
    String[][] filtered_paths = filterByMatch(args[1], paths, strict);
    String dir_txt = formString_findCommand(filtered_paths[0], false, 2);
    String file_txt = formString_findCommand(filtered_paths[1], true, 2+filtered_paths[0].length);
    
    String screen =
      "Keyword: " + browsertui.COLOR_GREEN + args[1] + browsertui.COLOR_DEFAULT
      + "\nThe following paths have been found:\n\n"
      + dir_txt + file_txt;
    base.clear();
    userinput.pressToContinue(screen);
  }
}
