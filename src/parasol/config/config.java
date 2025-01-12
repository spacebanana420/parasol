package parasol.config;

import parasol.browser.shell;
import java.io.File;
import java.util.ArrayList;

public class config {
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
}
