package parasol.commands;

import bananatui.base;
import bananatui.userinput;
import parasol.browser.browsertui;
import java.util.ArrayList;

class cmd_search {  
  static void findPaths(String[] args, String[][] paths, boolean strict) {
    String[][] filtered_paths = filterByMatch(args[1], paths, strict);
    String dir_txt = formString_findCommand(filtered_paths[0], false, 2);
    String file_txt = formString_findCommand(filtered_paths[1], true, 2+filtered_paths[0].length);
    
    String screen =
      "Keyword: " + browsertui.COLOR_GREEN + args[1] + browsertui.COLOR_DEFAULT
      + "\nThe following paths have been found:\n\n"
      + dir_txt + file_txt;
    base.clear();
    userinput.pressToContinue(screen);
  }

  private static String[][] filterByMatch(String keyword, String[][] paths, boolean strict) {
    ArrayList<String> files = new ArrayList<String>();
    ArrayList<String> dirs = new ArrayList<String>();
    int i = 2;
    String keyword_match = (strict) ? keyword : keyword.toLowerCase();

    for (String d : paths[0]) {
      String num = browsertui.COLOR_GREEN + i + ": " + browsertui.COLOR_DEFAULT;
      if (
        (d.contains(keyword_match) && strict)
        || (d.toLowerCase().contains(keyword_match) && !strict)
        ) {dirs.add(num+d);}
      i+=1;
    }
    for (String f : paths[1]) {
      String num = browsertui.COLOR_GREEN + i + ": " + browsertui.COLOR_DEFAULT;
      if (
        (f.contains(keyword_match) && strict)
        || (f.toLowerCase().contains(keyword_match) && !strict)
        ) {files.add(num+f);}
      i+=1;
    }
    return new String[][]{dirs.toArray(new String[0]), files.toArray(new String[0])};
  }

  private static String formString_findCommand(String[] paths, boolean checkFiles, int baseI) {
    var screen = new StringBuilder();
    if (checkFiles) {screen.append("===Files===\n");} else {screen.append("===Directories===\n");}
    for (String p : paths) {screen.append(p).append('\n');}
    return screen.append('\n').toString();
  }
}
