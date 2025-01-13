package parasol.browser;

import parasol.global;
import bananatui.*;
import java.io.File;

public class browsertui {
  private static String color_green = base.foreground("green");
  private static String color_default = base.foreground("default");
  private static String addNumberStr(int n) {return color_green + n + ": " + color_default;}
  
  public static String buildScreen(String parent, String dir_txt, String file_txt) {
    return parent + "\n\n" + addNumberStr(0) + "Exit\t\t" + addNumberStr(1) + "Go back\n\n" + dir_txt + file_txt;
  }
  
  public static String formString(String parent, String[] paths, boolean checkFiles, int baseI) {
    String s = (checkFiles) ? "===Files===\n" : "===Directories===\n";
    int column_size = 0;
    
    for (int i = 0; i < paths.length; i++)
    {
      File f = new File(parent + "/" + paths[i]);
      String num = addNumberStr(i+baseI);
      if ((checkFiles && f.isFile()) || (!checkFiles && f.isDirectory()))
      {
        String path_element = num + paths[i]; path_element = shortenName(path_element);
        String separator = (column_size >= 1 || global.DISPLAY_VERTICALLY_ONLY) ? "\n" : mkEmptySpace(path_element.length());

        s = s + path_element + separator;
        if (column_size < 1) {column_size += 1;} else {column_size = 0;}
      }
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
    for (int i = 0; i < max_length-1; i++) {
      buf += name.charAt(i);
    }
    return buf + "[...]";
  }
}
