package parasol.browser;

import java.util.ArrayList;

public class browserdata {
  private static String[] file_clipboard = new String[]{null, null}; //parent path and filename
  public static boolean clipboard_cut = false;
  
  public static void setClipboard(String path, String name) {file_clipboard[0] = path; file_clipboard[1] = name;}
  public static String clipboardPath() {return file_clipboard[0];}
  public static String clipboardName() {return file_clipboard[1];}
  public static boolean clipboardIsEmpty() {return file_clipboard[0] == null || file_clipboard[1] == null;}
  public static void clearClipboard() {file_clipboard[0] = null; file_clipboard[1] = null;}
  

  public static ArrayList<String> browser_tabs = new ArrayList<String>();

  public static void addTab(String path) {browser_tabs.add(path);}
  public static String getTab(int i) {return browser_tabs.get(i);}
  public static void setTab(String path, int i) {browser_tabs.set(i, path);}
  public static void removeTab(int i) {browser_tabs.remove(i);}
  public static void clearTabs() {browser_tabs.clear();}
  public static int tabSize() {return browser_tabs.size();}

  public static String getTabList() {
    if (browser_tabs.isEmpty()) {return "There are currently no saved tabs!";}
    StringBuilder txt = new StringBuilder("Currently saved tabs:\n");
    
    for (int i = 0; i < browser_tabs.size(); i++)
    {
      txt
        .append('\n')
        .append(browsertui.COLOR_GREEN).append(i).append(": ").append(browsertui.COLOR_DEFAULT)
        .append(browser_tabs.get(i));
    }
    return txt.toString();
  }
}
