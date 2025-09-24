package parasol.commands;

import java.io.File;
import java.util.ArrayList;

import parasol.browser.*;
import parasol.config.config;
import parasol.config.confio;
import parasol.global;
import parasol.misc.*;
import parasol.cli.help;

import bananatui.*;


//The main file for commands that you can launch from within parasol
public class commands {
  public static void runCommand(String cmd_str, String parent, String[][] paths) {
    switch (cmd_str) {
      case "help":
        base.clear();
        userinput.pressToContinue(help.getHelpMessage());
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
        cmd_filesize.sortFilesBySize(parent, paths[1]);
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
            + "\nTo add bookmarks, create a file at " + confio.CONFIG_PATH + global.FILE_SEPARATOR + "bookmarks.parasol"
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
      case "inherit-io":
        global.PROCESS_INHERIT_IO = !global.PROCESS_INHERIT_IO;
        return;
      case "wait-completion":
        global.PROCESS_WAIT_FOR_COMPLETION = !global.PROCESS_WAIT_FOR_COMPLETION;
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
          cmd_filesize.printSize_file(parent + "/" + browser.returnFile(buffer_i, paths), false);
        }
        else if (browser.indexLeadsToDir(buffer_i, paths)) {
          cmd_filesize.printSize_dir(parent + "/" + browser.returnDir(buffer_i, paths), false);
        }
        break;
      case "count-lines":
        if (!numops.isUint(args[1])) {return;}
        buffer_i = browser.answerToIndex(args[1]);
        if (!browser.indexLeadsToFile(buffer_i, paths)) {return;}
          int lines_amount = fileops.countLines(browser.returnFile(buffer_i, paths));
          userinput.pressToContinue("File line count: " + lines_amount);
        break;
      case "find":
        findPaths(args, paths, false);
        break;
      case "find-strict":
        findPaths(args, paths, true);
        break;
      case "mkdir":
        cmd_fileops.mkDirs(parent, args);
        break;
      case "mkfile":
        cmd_fileops.mkFiles(parent, args);
        break;
      case "move":
        cmd_fileops.moveCommand(cmd_str, parent, paths);
        break;
      case "copy":
        copyToClipboard(args, paths, parent, false);
        break;
      case "cut":
        copyToClipboard(args, paths, parent, true);
        break;
      case "rename":
        if (args.length < 3) {
          userinput.pressToContinue("To rename a file or directory, you must choose it (specify its number) and then prompt the new name!");
          return;
        }
        if (args[2].contains("/") || args[2].contains("\\")) {
          userinput.pressToContinue("Failed to rename path, the new name must not contain slashes!");
          return;
        }
        buffer_i = browser.answerToIndex(args[1]);
        String new_name = parent + "/" + args[2];
        String old_name = null;
        if (browser.indexLeadsToDir(buffer_i, paths)) {old_name = browser.returnDir(buffer_i, paths);}
        else if (browser.indexLeadsToFile(buffer_i, paths)) {old_name = browser.returnFile(buffer_i, paths);}
        else {
          userinput.pressToContinue("You must specify an existing file or path to rename by propmting its number!");
          return;          
        }
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
        cmd_fileops.deleteCommand(args, parent, paths);
        break;
      case "tab":
        buffer_i = userinput.answerToNumber(args[1]);
        if (buffer_i < 0 || buffer_i >= browserdata.tabSize()) {return;}
        String tab_path = browserdata.getTab(buffer_i);
        browser.BROWSER_DIRECTORY = tab_path;
        break;
    }
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
    var screen = new StringBuilder();
    if (checkFiles) {screen.append("===Files===\n");} else {screen.append("===Directories===\n");}
    for (String p : paths) {screen.append(p).append('\n');}
    return screen.append('\n').toString();
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
    String file_name = null;
    if (browser.indexLeadsToFile(i, paths)) {
      file_name = browser.returnFile(i, paths);
    }
    else if (browser.indexLeadsToDir(i, paths)) {
      file_name = browser.returnDir(i, paths);
    }
    if (file_name == null) {return;}
     
    browserdata.setClipboard(parent, file_name);
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
