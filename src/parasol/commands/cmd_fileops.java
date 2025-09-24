package parasol.commands;

import parasol.browser.*;
import parasol.misc.*;
import bananatui.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

class cmd_fileops {
  static void deleteCommand(String[] args, String parent, String[][] paths) {
    var deletePaths = new ArrayList<String>();
    var usedArgs = new ArrayList<String>(); //args used for deletion are added here so duplicates are ignored
    var txt_confirm = new StringBuilder("The following paths will be delted:");
    var txt_skipped = new StringBuilder("Skipped the following arguments for not leading to a file or directory:");
    var txt_deleted = new StringBuilder();
    boolean added_any_paths = false;
    boolean skipped_any_args = false;

    //Get the paths to delete, build the confirmation text, etc
    for (int i = 1; i < args.length; i++) {
      //prevent duplicates
      if (added_any_paths && repeatedArgument(usedArgs, args[i])) {continue;}

      usedArgs.add(args[i]);
      int index = browser.answerToIndex(args[i]);
      if (browser.indexLeadsToFile(index, paths)) {
        String file_name = browser.returnFile(index, paths);
        txt_confirm.append("\n  * ").append(file_name);
        deletePaths.add(parent + "/" + file_name);
        added_any_paths = true;
      }
      else if (browser.indexLeadsToDir(index, paths)) {
        String dir_name = browser.returnDir(index, paths);
        txt_confirm.append("\n  * ").append(dir_name);
        deletePaths.add(parent + "/" + dir_name);        
        added_any_paths = true;
      }
      else {
        txt_skipped.append("\n  * ").append(args[i]);
        skipped_any_args = true;
      }
    }
    if (!added_any_paths) {
      userinput.pressToContinue("You did not add any numbers pointing to files or directory, nothing will be deleted!");
      return;
    }
    txt_confirm.append("\n\nDeleting this paths is irreversible, proceed?");
    String confirm_message = skipped_any_args ? txt_skipped.append("\n\n").append(txt_confirm).toString() : txt_confirm.toString(); 
    boolean answer = userinput.askPrompt(confirm_message, false);
    if (!answer) {return;}

    //Delete the chosen paths
    for (String path : deletePaths) {
      txt_deleted.append("Deleting path at ").append(path);
      File f = new File(path);
      if (!f.exists()) {txt_deleted.append("\nFailed to delete path, it does not exist!");}
      else if (!f.canWrite()) {txt_deleted.append("\nFailed to delete path, you lack write permission!");}
      else if (f.isFile()) {
        boolean result = fileops.deleteFile(path);
        if (result) {txt_deleted.append("\nSuccessfully deleted the file at path ").append(path);}
        else {txt_deleted.append("\nFailed to delete the file at path ").append(path);}
      }
      else {
        boolean result = fileops.deleteDirectory(path);
        if (result) {txt_deleted.append("\nSuccessfully deleted the directory at path ").append(path);}
        else {txt_deleted.append("\nFailed to delete the directory at path ").append(path);}        
      }
      txt_deleted.append("\n\n");
    }
    userinput.pressToContinue(txt_deleted.toString());
  }

  static void moveCommand(String cmd_str, String parent, String[][] paths) {
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
    
    var txt = new StringBuilder();
    for (int i = 1; i < target; i++) //first value is the command
    {
      int source_i = browser.answerToIndex(args[i]);
      if (invalidIndex(source_i, i, indexes)) {
        txt.append("Skipping argument ").append(args[i]).append(": already-used path or not a path number\n");
        continue;
      }
      indexes[i] = source_i;
      if (browser.indexLeadsToFile(source_i, paths)) {
        txt.append(moveFile(browser.returnFile(source_i, paths), target_dir, parent, targetIsParent)).append("\n");
      }
      else if (browser.indexLeadsToDir(source_i, paths)) {
        txt.append(moveDirectory(browser.returnDir(source_i, paths), target_dir, parent, targetIsParent)).append("\n");
      }
    }
    txt.append("\nFinished moving all paths");
    userinput.pressToContinue(txt.toString());
  }
  
  static String moveFile(String source_name, String target_name, String parent, boolean moveOutsideParent) {
      if (moveOutsideParent) {
        target_name = new File(parent).getParent();
        new File(parent + "/" + source_name).renameTo(new File(target_name + "/" + source_name));
      }
      else {
        new File(parent + "/" + source_name).renameTo(new File(parent + "/" + target_name + "/" + source_name));
      }
      return (source_name + " has been moved to " + target_name);
  }
  
  static String moveDirectory(String source_name, String target_name, String parent, boolean moveOutsideParent) {
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

  static void mkDirs(String parent, String[] args) {
    if (!new File(parent).canWrite()) {
      userinput.pressToContinue("Current directory lacks write permissions, cannot create directories!");
      return;
    }
    var used_names = new ArrayList<String>();
    StringBuilder error_txt = new StringBuilder();
    for (int i = 1; i < args.length; i++) {
      if (used_names.contains(args[i])) {continue;}
      if (args[i].contains("/") || args[i].contains("\\")) {
        error_txt.append("Directory name ").append(args[i]).append(" cannot contain forward slashes or back slashes! Skipping");
        continue;
      }
      new File(parent + "/" + args[i]).mkdir();
      used_names.add(args[i]);
    }
    if (error_txt.length() > 0){userinput.pressToContinue(error_txt.toString());}
  }
  
  static void mkFiles(String parent, String[] args) {
    if (!new File(parent).canWrite()) {
      userinput.pressToContinue("Current directory lacks write permissions, cannot create files!");
      return;
    }
    StringBuilder error_txt = new StringBuilder();
    for (int i = 1; i < args.length; i++) {
      String filename = misc.generateFileName(parent, args[i]);
      if (args[i].contains("/") || args[i].contains("\\")) {
        error_txt.append("Directory name ").append(args[i]).append(" cannot contain forward slashes or back slashes! Skipping");
        continue;
      }
      try {
        Files.createFile(Path.of(filename));
      }
      catch(IOException e) {e.printStackTrace(); userinput.pressToContinue("");}
    }
    if (error_txt.length() > 0) {userinput.pressToContinue(error_txt.toString());}
  }

  private static boolean repeatedArgument(ArrayList<String> used_args, String arg) {
    for (String used : used_args) {if (used.equals(arg)) {return true;}}
    return false;
  }
  
  private static boolean invalidIndex(int index, int current_i, int[] indexes) {
    if (index < 0) {return true;}
    for (int i = 1; i < current_i; i++) { //current_i prevents iterating uninitialized values
      if (index == indexes[i]) {return true;}
    }
    return false;
  }
}
