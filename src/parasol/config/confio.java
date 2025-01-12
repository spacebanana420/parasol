package parasol.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class confio {
  public static String CONFIG_PATH = getConfigPath();
  
  public static boolean initialize() {
    if (new File(CONFIG_PATH).isDirectory()) {
      try {Files.createFile(Path.of(CONFIG_PATH + "/bookmarks.parasol"));}
      catch(IOException e) {return false;}
    }
    return true;
  }
    
  public static String readFile(String path) {
    byte[] config_bytes = new byte[0];
    try{
      var stream = new FileInputStream(path);
      config_bytes = stream.readAllBytes();
      stream.close();
      return new String(config_bytes);
    }
    catch(IOException e) {return "";}
  }
  
  public static ArrayList<String> readLines(String path) {
    String confstr = readFile(path);
    var lines = new ArrayList<String>();
    String line = "";
    for (int i = 0; i < confstr.length(); i++) {
      char c = confstr.charAt(i);
      boolean isnewline = c == '\n';
      if (isnewline) {
        if (line.length() > 0) {lines.add(line); line = "";}
      }
      else {line += c;}
    }
    return lines;
  }

  public static boolean writeFile(String path, String contents, boolean append) {
    try {
      var stream = new FileOutputStream(path, append);
      stream.write(contents.getBytes());
      stream.close();
      return true;
    }
    catch (IOException e) {return false;}
  }
  
  private static String getConfigPath() {
    String os = System.getProperty("os.name");
    String home = System.getProperty("user.home");
    String slash = System.getProperty("file.separator");

    String path = "";
    if (os.contains("Windows")) {path = home + slash + "parasol";}
    else if (new File(home + slash + ".config").isDirectory()) {
      path = home + slash + ".config" + slash + "parasol";
    }
    else {path = home + slash + ".parasol";}

    File f = new File(path);
    if (!f.isDirectory()) {f.mkdir();}
    return path;
  }
}
