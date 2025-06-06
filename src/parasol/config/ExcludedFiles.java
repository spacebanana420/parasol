package parasol.config;

import java.util.ArrayList;

public class ExcludedFiles {
  private String[] file_extensions = null;
  
  public ExcludedFiles(String value) {
    if (value == null) {return;}
    String buffer = "";
    var extensions = new ArrayList<String>();
    for (int i = 0; i < value.length(); i++) {
      char c = value.charAt(i);
      if (c == ',') {
        if (buffer.length() == 0) {continue;}
        extensions.add(buffer.trim());
        buffer = "";
      }
      else {buffer += c;}
    }
    if (buffer.length() > 0) {extensions.add(buffer.trim());}
    if (extensions.size() > 0) {file_extensions = extensions.toArray(new String[0]);}
  }
  public boolean isExcluded(String filename) {
    if (file_extensions == null) {return false;}
    for (String e : file_extensions) {
      if (confio.hasExtension(filename, e)) {return true;}
    }
    return false;
  }
}
