package parasol.config;

import parasol.browser.shell;
import java.io.File;
import java.util.ArrayList;

public class config {
  public static ArrayList<String> readBaseConfig() {
    return confio.readLines(confio.CONFIG_PATH + "/config.parasol");
  }
  
  public static boolean processInheritIO(ArrayList<String> conf) {
    String result = confio.findOptionValue(conf, "process-inherit-io");
    return result != null && result.toLowerCase().equals("true");
  }
  
  public static boolean processWaitForCompletion(ArrayList<String> conf) {
    String result = confio.findOptionValue(conf, "process-wait-for-completion");
    return result != null && result.toLowerCase().equals("true");
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
  
  public static FileRunner[] getFileRunners(ArrayList<String> lines) {
    var runners = new ArrayList<FileRunner>();
    for (String line : lines)
    {
      String value = confio.getOptionValue(line, "runner");
      if (value == null) {continue;}
      
      int process_i_start = -1;
      var frunner = new FileRunner();
      String buffer = "";
      for (int i = 0; i < value.length(); i++) //add extensions
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
