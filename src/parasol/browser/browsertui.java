package parasol.browser;

import parasol.global;
import bananatui.base;
import java.io.File;

public class browsertui {
  private static String color_green = base.foreground("green");
  private static String color_default = base.foreground("default");
  private static String addNumberStr(int n) {return color_green + n + ": " + color_default;}
  
  public static String buildScreen(String parent, String dir_txt, String file_txt) {
    return
      base.boldMode(true) + parent + base.resetMode() + "\n\n"
      + addNumberStr(0) + "Exit\t\t" + addNumberStr(1) + "Go back\n\n"
      + dir_txt + file_txt;
  }
  
  public static String formString(String parent, String[] paths, boolean checkFiles, int baseI) {
    String bold = base.boldMode(true);
    String no_bold = base.resetMode();
    String s =
      (checkFiles)
      ? bold + "[Files]\n" + no_bold
      : bold + "[Directories]\n" + no_bold
    ;
    int column_size = 0;
    int lines_amt = 0;
    
    for (int i = 0; i < paths.length; i++)
    {
      File f = new File(parent + "/" + paths[i]);
      String file_number = addNumberStr(i+baseI);
      boolean path_is_valid = (checkFiles && f.isFile()) || (!checkFiles && f.isDirectory());
      if (!path_is_valid) {continue;}
      
      String path_element = file_number + paths[i]; path_element = shortenName(path_element);
      String separator;
      if (column_size >= 1 || global.DISPLAY_VERTICALLY_ONLY) {
        lines_amt++;
        column_size = 0;
        if (lines_amt >= 10) {separator = "\n\n"; lines_amt=0;} else {separator="\n";}
      }
      else {separator = mkEmptySpace(path_element.length()); column_size++;}
      
      s += path_element + separator;
    }
    if (!global.DISPLAY_VERTICALLY_ONLY && column_size != 0) {return s + "\n\n";}
    else {return s + "\n";}
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
