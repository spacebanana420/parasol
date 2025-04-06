package parasol.misc;

import bananatui.userinput;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class fileops {
  public static boolean moveFile(String source, String target) {
    Path sp = Path.of(source);
    Path tp = Path.of(target);
    try{Files.move(sp, tp); return true;}
    catch (IOException e) {return false;}
  }
  
  public static boolean copyFile(String source, String target) {
    Path sp = Path.of(source);
    Path tp = Path.of(target);
    try{Files.copy(sp, tp); return true;}
    catch (IOException e) {return false;}
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
    return copy_move(path, target, false);
  }

  public static boolean copyDirectory(String path, String target) {
    return copy_move(path, target, true);
  }
  
  public static void printError() {
    userinput.pressToContinue
    (
      "An error occurred while attempting to paste the file\n"
      +"Make sure you have read and write permissions for your source file and the current directory."
    );
  }
  
  public static int countLines(String file_path) {
    var p = Path.of(file_path);
    try {return Files.readAllLines(p).size();}
    catch (IOException e) {return 0;}
  }

  private static boolean copy_move(String path, String target, boolean copy) {
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

        if (new File(full_path).isFile()) {
          if (copy) {Files.copy(pp, tp);}
          else {Files.move(pp, tp);}
        }
        else {copy_move(full_path, target + "/" + p, copy);}
      }
      if (!copy) {Files.delete(Path.of(path));}
      return true;
    } catch(IOException e) {e.printStackTrace(); return false;}
  }
}
