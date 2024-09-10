package parasolib;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class config {

  public static String CONFIG_PATH = getConfigPath();

  public static String[] getBookmarks() {
    String config = readFile(CONFIG_PATH + "/bookmarks.parasol");
    String buf = "";

    ArrayList<String> bookmarks = new ArrayList<>();
    for (int i = 0; i < config.length(); i++) {
      char c = config.charAt(i);
      if (c == '\n') {
        File f = new File(buf);
        if (
          buf.length() > 0
          && f.isDirectory()
          && !shell.onlyDots(buf)
          )
        {bookmarks.add(buf); buf = "";}
        continue;
      }
      buf += c;
    }
    return bookmarks.toArray(new String[0]);
  }

  private static String getConfigPath() {
    String os = System.getProperty("os.name");
    String home = System.getProperty("user.home");
    String slash = System.getProperty("file.separator");

    if (os.contains("Windows")) {return home + slash + "parasol";}
    return
      (new File(home + slash + ".config").isDirectory())
      ? home + slash + ".config" + "parasol"
      : home + slash + ".parasol";
  }

  private static String readFile(String path) {
    byte[] config_bytes = new byte[0];
    try{
      var stream = new FileInputStream(path);
      config_bytes = stream.readAllBytes();
      stream.close();
    }
    catch(Exception e) {return "";}
    return new String(config_bytes);
  }
}
