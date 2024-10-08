package test;

import java.nio.file.FileSystems;
import java.nio.file.FileStore;

//import bananatui.*;

//sometimes its better to test functionality in a small isolated program
//i mostly use this for testing functionality on systems i dont daily drive, like freebsd and windows

public class test {
  public static void main(String[] args) {
    linux_devicetest();
  }
  private static void linux_devicetest() {
    for (FileStore store: FileSystems.getDefault().getFileStores()) {
      var path = store.toString();
      path = sanitize_path(path);
      println(path); println("Filesystem: " + store.type() + "\n");
    }  
  }

  private static void freebsd_devicetest() {
    String[] invalid_paths = new String[]{
      "/dev", "/usr/src", "/usr/ports", "/var/", "/tmp",
      "/home", "/zroot"
    };
    for (FileStore store: FileSystems.getDefault().getFileStores()) {
      var path = store.toString();
      if (invalidPath_softmatch(invalid_paths, path)) {continue;}
      path = sanitize_path(path);
      println(path); println("Filesystem: " + store.type() + "\n");
    }
  }

  private static boolean invalidPath_softmatch(String[] invalid_paths, String path) {
    for (String p : invalid_paths) {
      if (path.contains(p)) {return true;}
    }
    return false;
  }

  private static int getFilterStart(String path) {
    for (int i = path.length()-1; i >= 0; i-=1) {
      if (path.charAt(i) == '(') {return i-1;} //-1 because the previous character is an unwanted whitespace
    }
    return path.length();
  }

  private static String sanitize_path(String path) {
    String final_path = "";
    int copy_end = getFilterStart(path);

    for (int i = 0; i < copy_end; i++) {
      final_path += path.charAt(i);
    }
    return final_path;
  }

  private static void println(String msg) {System.out.println(msg);}
}