package parasol.config;

import java.util.ArrayList;

public class FileRunner {
  public ArrayList<String> process = new ArrayList<String>();
  public ArrayList<String> file_extensions = new ArrayList<String>();
  
  public void setProcess(ArrayList<String> new_process) {process = new_process;}
  
  public boolean processIsSet() {return process.size() > 0;}
  
  public void addExtension(String extension) {
    file_extensions.add(extension);
  }
  
  public void addExtensions(ArrayList<String> new_extensions) {
    file_extensions.addAll(new_extensions);
  }
  
  public void addExtensions(String[] new_extensions) {
    for (String e : new_extensions){file_extensions.add(e);}
  }
  
  public boolean hasExtensions() {return file_extensions.size() > 0;}
  
  public String[] buildCommand(String filepath) {
    var command = new ArrayList<String>();
    boolean hasFileArgument = false;
    
    for (int i = 0; i < process.size(); i++) {
      String arg = process.get(i);
      if (arg.equals("%F")) {command.add(filepath); hasFileArgument = true;}
      else {command.add(arg);}
    }
    if (!hasFileArgument) {command.add(filepath);}
    return command.toArray(new String[0]);
  }
  
  public boolean hasValidExtension(String filepath) {
    for (int i = 0; i < file_extensions.size(); i++) {
      String e = file_extensions.get(i);
      if (hasExtension(filepath, e)) {return true;}
    }
    return false;
  }
  
  private boolean hasExtension(String filepath, String extension) {
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
}
