package parasol.browser;

import parasol.global;
import bananatui.base;
import java.io.File;

public class browsertui {
  public static final String COLOR_GREEN = base.foreground("green");
  public static final String COLOR_DEFAULT = base.foreground("default");
  public static final String BOLD_ENABLE = base.boldMode(true);
  public static final String BOLD_DISABLE = base.resetMode();
  
  private static String addNumberStr(int n) {return COLOR_GREEN + n + ": " + COLOR_DEFAULT;}
  
  public static String buildScreen(String parent, String dirs[], String files[], boolean forceVertical) {
    String dir_txt = formString(parent, dirs, false, 2, forceVertical);
    String file_txt = formString(parent, files, true, 2+dirs.length, forceVertical);
    var full_tui = new StringBuilder()
      .append(BOLD_ENABLE).append(parent).append(BOLD_DISABLE).append("\n\n") //Parent directory display
      .append(addNumberStr(0)).append("Exit\t\t").append(addNumberStr(1)).append("Go back\n\n") //Option 0 and option 1
      .append(dir_txt).append("\n\n") //Directories
      .append(file_txt).append('\n'); //Files
    return full_tui.toString();
  }
  
  public static String formString(String parent, String[] paths, boolean checkFiles, int baseI, boolean forceVertical) {
    var final_screen = new StringBuilder();
    final_screen.append(BOLD_ENABLE);
    if (checkFiles) {final_screen.append("[Files]\n");}
    else {final_screen.append("[Directories]\n");}
    final_screen.append(BOLD_DISABLE);

    int column_size = 0;
    int lines_amt = 0;
    int last_path = paths.length-1;
    boolean vertical = global.DISPLAY_VERTICALLY_ONLY || forceVertical;
    
    for (int i = 0; i < paths.length; i++)
    {
      String file_number = addNumberStr(i+baseI);
      String path_element = shortenName(file_number + paths[i], vertical);
      final_screen.append(path_element);
      if (i == last_path) {continue;} //Do not add newlines or empty spaces for the last element
      
      if (column_size >= 1 || vertical) {
        lines_amt++;
        column_size = 0;
        if (lines_amt == 10) {final_screen.append("\n\n"); lines_amt=0;} else {final_screen.append("\n");}
      }
      else {
        final_screen.append(mkEmptySpace(path_element.length()));
        column_size++;
      }
    }
    return final_screen.toString();
  }

  private static String mkEmptySpace(int len) {
    var empty_space = new StringBuilder();
    for (int i = 0; i < 65-len; i++) {empty_space.append(' ');}
    return empty_space.toString();
  }

  private static String shortenName(String name, boolean vertical) {
    int max_length = (vertical) ? 100 : 55;
    if (name.length() <= max_length || (vertical && global.VERTICAL_DISPLAY_FULL_PATHS)) {return name;}
    
    var buf = new StringBuilder();
    for (int i = 0; i < max_length; i++) {buf.append(name.charAt(i));}
    return buf.append("[...]").toString();
  }
}
