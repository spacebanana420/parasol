package parasol.parasolib;

import java.util.ArrayList;
import java.io.File;

public class misc {
  public static void selectionSort(String[] s) {
    String temp = "";
    for (int i = 0; i < s.length; i++) {
      int biggest = findBiggest(s, i);
      if (i != biggest) {
        temp = s[i];
        s[i] = s[biggest];
        s[biggest] = temp;
      }
    }
  }
  private static int findBiggest(String[] s, int i) {
    int biggest_i = i;
    for (int c = i; c < s.length; c++) {
      if (s[c].compareTo(s[biggest_i]) < 0) {biggest_i = c;}
    }
    return biggest_i;
  }

  public static String[] groupStrings(String s) {
    ArrayList<String> group = new ArrayList<String>();
    String buffer = "";
    boolean ignore = false;
    for (int i = 0; i < s.length(); i++) {
      if (s.charAt(i) == ' ' && !ignore) {
       if (buffer.length() != 0) {group.add(buffer); buffer = "";}
      }
      else if (s.charAt(i) == '"') {ignore = !ignore;}
      else {buffer += s.charAt(i);}
    }
    if (buffer.length() != 0) {group.add(buffer);}
    return group.toArray(new String[0]);
  }

  // public static boolean startsWith(String s, String keyword) {
  //   if (s.length() < keyword.length()) {return false;}
  //   String buf = "";
  //   for (int i = 0; i < keyword.length(); i++) {
  //     buf += s.charAt(i);
  //   }
  //   return buf.equals(keyword);
  // }
  public static boolean startsWith(String s, String keyword) {
    if (s.length() < keyword.length()) {return false;}
    for (int i = 0; i < keyword.length(); i++) {
      if (s.charAt(i) != keyword.charAt(i)) {return false;}
    }
    return true;
  }

  public static String generateFileName(String path, String name) {
    if (!new File(path + "/" + name).isFile()) {return path + "/" + name;}
    int i = 0;
    while (true) {
      String full_path = path + "/" + i + "_" + name;
      if (!new File(full_path).isFile()) {return full_path;}
      i+=1;
    }
  }
}
