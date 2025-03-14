package parasol.browser;

import parasol.global;
import bananatui.base;
import java.io.File;

public class browsertui {
  private static String color_green = base.foreground("green");
  private static String color_default = base.foreground("default");
  private static String addNumberStr(int n) {return color_green + n + ": " + color_default;}
  
  public static String buildScreen(String parent, String dirs[], String files[], boolean forceVertical) {
    String dir_txt = formString(parent, dirs, false, 2, forceVertical);
    String file_txt = formString(parent, files, true, 2+dirs.length, forceVertical);
    return
      base.boldMode(true) + parent + base.resetMode() + "\n\n"
      + addNumberStr(0) + "Exit\t\t" + addNumberStr(1) + "Go back\n\n"
      + dir_txt + file_txt;
  }
  
  public static String formString(String parent, String[] paths, boolean checkFiles, int baseI, boolean forceVertical) {
    String bold = base.boldMode(true);
    String no_bold = base.resetMode();
    String final_screen =
      (checkFiles)
      ? bold + "[Files]\n" + no_bold
      : bold + "[Directories]\n" + no_bold
    ;
    int column_size = 0;
    int lines_amt = 0;
    
    for (int i = 0; i < paths.length; i++)
    {
      String file_number = addNumberStr(i+baseI);
      String path_element = shortenName(file_number + paths[i]);
      String separator;
      if (column_size >= 1 || global.DISPLAY_VERTICALLY_ONLY || forceVertical) {
        lines_amt++;
        column_size = 0;
        if (lines_amt >= 10) {separator = "\n\n"; lines_amt=0;} else {separator="\n";}
      }
      else {separator = mkEmptySpace(path_element.length()); column_size++;}
      
      final_screen += path_element + separator;
    }
    if (!global.DISPLAY_VERTICALLY_ONLY && !forceVertical && column_size != 0) {return final_screen + "\n\n";}
    else {return final_screen + "\n";}
  }

  private static String mkEmptySpace(int len) {
    String empty_space = "";
    for (int i = 0; i < 65-len; i++) {empty_space += " ";}
    return empty_space;
  }

  private static String shortenName(String name) {
    int max_length = (global.DISPLAY_VERTICALLY_ONLY) ? 90 : 55;
    if (name.length() < max_length) {return name;}
    
    String buf = "";
    for (int i = 0; i < max_length-1; i++) {buf += name.charAt(i);}
    return buf + "[...]";
  }
}
