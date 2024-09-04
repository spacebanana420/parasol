package parasolib;

import java.io.IOException;
import java.util.ArrayList;

//for platform-specific implementations

public class platform {
  public static String[] getSystemDisks() { //linux-only at least for now
    var cmd = new String[]{"lsblk", "-A", "-l", "-o", "MOUNTPOINT"};
    if (!System.getProperty("os.name").equals("Linux")) {return new String[]{};}
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
    catch(IOException e) {return new String[]{};}
  }
  
  private static boolean invalidPath(String[] invalid_paths, String path) {
    for (String p : invalid_paths) {
      if (p.equals(path)) {return true;}
    }
    return false;
  }
}

//alternative implementation that doesnt rely on a cli tool
//this will definitely be needed for freebsd if i cant find a proper alternative to lsblk

// for (FileStore store: FileSystems.getDefault().getFileStores()) {
//   var a = store.toString();
//   if (!a.contains("(/dev")) {continue;}
//   base.println(a); base.println(store.type());
// }
// userinput.pressToContinue("");