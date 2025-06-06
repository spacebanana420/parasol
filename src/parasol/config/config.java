package parasol.config;

import parasol.browser.shell;
import java.io.File;
import java.util.ArrayList;

public class config {
  public static ConfLine[] readBaseConfig() {
    var lines = confio.readLines(confio.CONFIG_PATH + "/config.parasol");
    return confio.getSettings(lines);
  }
  
  public static boolean processInheritIO(ConfLine[] conf) {
    return confio.getValue_bool("process-inherit-io", conf);
  }
  
  public static boolean processWaitForCompletion(ConfLine[] conf) {
    return confio.getValue_bool("process-wait-for-completion", conf);
  }
  
  public static boolean silentShell(ConfLine[] conf) {
    return confio.getValue_bool("silent-shell", conf);
  }
  
  public static boolean showHiddenPaths(ConfLine[] conf) {
    return confio.getValue_bool("show-hidden-paths", conf);
  }
  
  public static boolean displayPathsVertically(ConfLine[] conf) {
    return confio.getValue_bool("display-paths-vertically", conf);
  }
  
  public static boolean displayFullNames(ConfLine[] conf) {
    return confio.getValue_bool("display-full-names", conf);
  }
  
  public static boolean checkForeignChars(ConfLine[] conf) {
    return confio.getValue_bool("check-foreign-characters", conf);
  }
  
  public static ExcludedFiles getExcludedExtensions(ConfLine[] conf) {
    String value = confio.getValue("excluded-extensions", conf);
    return new ExcludedFiles(value);
  }
  
  public static String[] getBookmarks() {
    ArrayList<String> lines = confio.readLines(confio.CONFIG_PATH + "/bookmarks.parasol");
    ArrayList<String> bookmarks = new ArrayList<>();
    
    for (String line : lines) {
      File f = new File(line);  
      if (f.isDirectory() && !shell.onlyDots(line)) {bookmarks.add(line);}
    }
    return bookmarks.toArray(new String[0]);
  }

  public static void addBookmark(String bookmark) {
    if (!new File(bookmark).isDirectory()) {return;}
    confio.writeFile(confio.CONFIG_PATH + "/bookmarks.parasol", bookmark + "\n", true);
  }
  
  public static FileRunner[] getFileRunners(ConfLine[] settings) {
    var runners = new ArrayList<FileRunner>();
    for (ConfLine setting : settings)
    {
      if (!setting.key.equals("runner")) {continue;}
      
      String value = setting.value;
      int process_i_start = -1;
      var frunner = new FileRunner();
      String buffer = "";
      for (int i = 0; i < value.length(); i++) //add the file extensions
      {
        char c = value.charAt(i);
        if (c == ':') {
          if (buffer.length() > 0) {frunner.addExtension(buffer); buffer = "";}
          process_i_start = i+1;
          break;
        }
        if (c == ',') {
          if (buffer.length() > 0) {frunner.addExtension(buffer); buffer = "";}
          continue;
        }
        buffer += c;
      }
      if (process_i_start == -1 || !frunner.hasExtensions()) {continue;}
      
      var command = new ArrayList<String>();
      buffer = "";
      for (int i = process_i_start; i < value.length(); i++) //finally, get the process
      {
        char c = value.charAt(i);
        if (c == ' ' || c == '\t') {
          if (buffer.length() > 0) {command.add(buffer); buffer = "";}
          continue;
        }
        buffer += c;
      }
      if (buffer.length() > 0) {command.add(buffer); buffer = "";}
      if (command.size() == 0) {continue;}
      frunner.setProcess(command);
      runners.add(frunner);
    }
    return runners.toArray(new FileRunner[0]);
  }
}
