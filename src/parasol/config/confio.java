package parasol.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class confio {
  public static final String CONFIG_PATH = getConfigPath();
  
  public static boolean initialize() {
    if (new File(CONFIG_PATH).isDirectory()) {
      try
      {
        var p_bookmarks = Path.of(CONFIG_PATH + "/bookmarks.parasol");
        var p_config = Path.of(CONFIG_PATH + "/config.parasol");
        if (!Files.exists(p_bookmarks)) {Files.createFile(p_bookmarks);}
        if (!Files.exists(p_config))
        {
          Files.createFile(p_config);
          var fo = new FileOutputStream(CONFIG_PATH + "/config.parasol");
          String default_config =
            "==Parasol config=="
            + "\n# Lines starting with # are ignored"
            + "\n\n# Override the program and arguments to run according to the file extension, you can add as many as you want"
            + "\n# You can pass the argument %F to the command to specify where the file you open should be, otherwise it's added at the end"
            + "\n#runner=extension1,extension2,extension3:command arg1 arg2 arg3 %F"
            + "\n#Example: runner=mp4,mov,mkv,avi:vlc"
            + "\n#Example: runner=png,jpg,webp:mpv %F --keep-open"
          ;
          fo.write(default_config.getBytes());
          fo.close();
        }
      }
      catch(IOException e) {e.printStackTrace();return false;}
    }
    return true;
  }
    
  static String readFile(String path) {
    byte[] config_bytes = new byte[0];
    try{
      var stream = new FileInputStream(path);
      config_bytes = stream.readAllBytes();
      stream.close();
      return new String(config_bytes);
    }
    catch(IOException e) {return "";}
  }
  
  static ArrayList<String> readLines(String path) {
    String confstr = readFile(path);
    var lines = new ArrayList<String>();
    String line = "";
    for (int i = 0; i < confstr.length(); i++) {
      char c = confstr.charAt(i);
      if (c == '\n') {
        if (line.length() > 0 && line.charAt(0) != '#') {lines.add(line);}
        line = "";
      }
      else {line += c;}
    }
    return lines;
  }

  static boolean writeFile(String path, String contents, boolean append) {
    try {
      var stream = new FileOutputStream(path, append);
      stream.write(contents.getBytes());
      stream.close();
      return true;
    }
    catch (IOException e) {return false;}
  }
  
  static String getOptionValue(String line, String setting) {
    String full_setting = setting + "=";
    if (!lineStartsWith(line, full_setting)) {return null;}
    String value = "";
    for (int i = full_setting.length(); i < line.length(); i++) {value += line.charAt(i);}
    return value.trim();
  }
  
  static boolean lineStartsWith(String line, String setting) {
    if (line.length() <= setting.length()) {return false;}
    String setting_verify = "";
    for (int i = 0; i < setting.length(); i++) {setting_verify += line.charAt(i);}
    return setting_verify.equals(setting);
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
