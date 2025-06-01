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
  public static String getConfigFile() {return CONFIG_PATH + "/config.parasol";}
  
  public static boolean initialize() {
    if (new File(CONFIG_PATH).isDirectory()) {
      String config_file = getConfigFile();
      try
      {
        var p_bookmarks = Path.of(CONFIG_PATH + "/bookmarks.parasol");
        var p_config = Path.of(config_file);
        if (!Files.exists(p_bookmarks)) {Files.createFile(p_bookmarks);}
        if (!Files.exists(p_config))
        {
          Files.createFile(p_config);
          var fo = new FileOutputStream(config_file);
          String default_config =
            "==Parasol config=="
            + "\n# Lines starting with # are ignored"
            + "\n"
            + "\n# Override the program and arguments to run according to the file extension, you can add as many as you want"
            + "\n# You can pass the argument %F to the command to specify where the file you open should be, otherwise it's added at the end"
            + "\n#runner=extension1,extension2,extension3:command arg1 arg2 arg3 %F"
            + "\n#Example: runner=mp4,mov,mkv,avi:vlc"
            + "\n#Example: runner=png,jpg,webp:mpv %F --keep-open"
            + "\n"
            + "\n# When set to true, the applications Parasol opens will inherit its standard input and output"
            + "\n# Enable this setting to view the output of CLI and TUI applications and interact with them"
            + "\n#process-inherit-io=false"
            + "\n"
            + "\n# When set to true, Parasol will wait for the files it opens to be closed"
            + "\n# You can also use this setting to interact with CLI and TUI programs"
            + "\n#process-wait-for-completion=true"
            + "\n"
            + "\n# When set to true, Parasol's built-in shell does not print an initial message by default"
            + "\n#silent-shell=false"
            + "\n"
            + "\n# Displays hidden paths by default"
            + "\n#show-hidden-paths=false"
            + "\n"
            + "\n# Displays paths vertically, all separated by lines"
            + "\n#display-paths-vertically=false"
            + "\n"
            + "\n# When paths are displayed vertically, show their full name"
            + "\n#display-full-names=false"
            + "\n"
            + "\n# If foreign characters are detected in a path's name, display paths vertically regardless of configuration"
            + "\n#check-foreign-characters=false"
            + "\n"
            + "\n# Excludes and hides files from the explorer based on their extension"
            +"\n#excluded-extensions=extension1,extension2,extension3"
            +"\n#Example: excluded-extensions=srt,jpg,exe,pdf"
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
        line = line.trim();
        if (line.length() > 0 && line.charAt(0) != '#') {lines.add(line);}
        line = "";
      }
      else {line += c;}
    }
    return lines;
  }
  
  static ConfLine[] getSettings(ArrayList<String> lines) {
    var settings = new ArrayList<ConfLine>();
    for (String line : lines) {
      var setting = new ConfLine(line);
      if (setting.key == null || setting.value == null) {continue;}
      settings.add(setting);
    }
    return settings.toArray(new ConfLine[0]);
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
  
  static String getValue(String key, ConfLine[] settings) {
    int i = findValue(key, settings);
    if (i == -1) {return null;}
    return settings[i].value;
  }
  
  static boolean getValue_bool(String key, ConfLine[] settings) {
    int i = findValue(key, settings);
    if (i == -1) {return false;}
    return settings[i].value.toLowerCase().equals("true");
  }
  
  static boolean hasExtension(String filepath, String extension) {
    if (filepath.length() <= extension.length()) {return false;}
    String full_extension = "." + extension;
    int offset = filepath.length()-full_extension.length();
    for (int i = 0; i < full_extension.length(); i++) {
      char c1 = filepath.charAt(i+offset);
      char c2 = full_extension.charAt(i);
      if (c1 != c2) {return false;}
    }
    return true;
  }
  
  private static int findValue(String key, ConfLine[] settings) {
    for (int i = 0; i < settings.length; i++) {
      if (settings[i].key.equals(key)) {return i;}
    }
    return -1;
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
