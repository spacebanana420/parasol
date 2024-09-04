package parasolib;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.util.ArrayList;

import bananatui.userinput;

//for platform-specific implementations

public class platform {
  public static String[] getSystemDisks() {
    switch(System.getProperty("os.name")) {
      case "Linux":
        return getSystemDisks_linux();
      case "FreeBSD":
        return getSystemDisks_freebsd();
      default:
        userinput.pressToContinue("This feature is only available for Linux systems and FreeBSD!");
        return new String[]{};
    }

  }
  private static String[] getSystemDisks_linux() { //linux-only at least for now
    var cmd = new String[]{"lsblk", "-A", "-l", "-o", "MOUNTPOINT"};
    try {
      Process process = new ProcessBuilder(cmd).start();
      String result = new String(process.getInputStream().readAllBytes());

      String[] invalid_paths = new String[]{"", "MOUNTPOINT", "MOUNT", "/boot", "/efi", "/boot/efi"};
      String buf = "";
      ArrayList<String> devices = new ArrayList<>();
      for(int i = 0; i < result.length(); i++) {
        char c = result.charAt(i);
        if (c == '\n') {
          if (buf.length() != 0 && !invalidPath(invalid_paths, buf)) {devices.add(buf);}
          buf = "";
          continue;
        }
        buf += c;
      }
      return devices.toArray(new String[0]);
    }
    catch(IOException e) {
      userinput.pressToContinue("Failed to list available disk devices! Make sure you have lsblk installed!");
      return new String[]{};
    }
  }
  
  private static boolean invalidPath(String[] invalid_paths, String path) {
    for (String p : invalid_paths) {
      if (p.equals(path)) {return true;}
    }
    return false;
  }

  private static boolean invalidPath_softmatch(String[] invalid_paths, String path) {
    for (String p : invalid_paths) {
      if (path.contains(p)) {return true;}
    }
    return false;
  }

  private static String[] getSystemDisks_freebsd() {
    String[] invalid_paths = new String[]{
      "/dev", "/usr/src", "/usr/ports", "/var/", "/tmp",
      "/home", "/zroot"
    };

    ArrayList<String> devices = new ArrayList<>();
    for (FileStore store: FileSystems.getDefault().getFileStores()) {
      var path = store.toString();
      if (invalidPath_softmatch(invalid_paths, path)) {continue;}
      path = sanitize_path(path);
      devices.add(path);
    }
    return devices.toArray(new String[0]);
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
}