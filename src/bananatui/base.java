package bananatui;

public class base {
  public static void println(String msg) {System.out.println(msg);}
  public static void print(String msg) {System.out.print(msg);}

  public static void spawnScreen(String msg) {print("\u001B[3J\u001B[1J\u001B[H" + msg);}
  public static void clear() {print("\u001B[3J\u001B[1J\u001B[H");}

  public static void saveScreen() {print("\u001B[?47h");}
  public static void restoreScreen() {print("\u001B[?47l");}

  public static void moveCursor(String mode, int lines) {
    if (mode.equals("up")) {print("\u001B[" + lines + "A");}
    else {print("\u001B[" + lines + "B");}
  }

  public static void clearBelowCursor() {print("\u001B[0K");}

  public static void printStatus(String msg, boolean isError) {
    String deflt = foreground("default");
    if (isError) {println("[" + foreground("red") + "Error" + deflt + "]" + msg);}
    else {println("[" + foreground("yellow") + "Warning" + deflt + "]" + msg);}
  }

  public static String foreground(String color) {
    switch(color) {
      case "black": return "\u001B[30m";
      case "red": return "\u001B[31m";
      case "green": return "\u001B[32m";
      case "yellow": return "\u001B[33m";
      case "blue": return "\u001B[34m";
      case "magenta": return "\u001B[35m";
      case "cyan": return "\u001B[36m";
      case "white": return "\u001B[37m";
      case "default": return "\u001B[39m";
      case "reset": return "\u001B[0m";
      default: return "\u001B[39m";
    }
  }

  public static String background(String color) {
    switch(color) {
      case "black": return "\u001B[40m";
      case "red": return "\u001B[41m";
      case "green": return "\u001B[42m";
      case "yellow": return "\u001B[43m";
      case "blue": return "\u001B[44m";
      case "magenta": return "\u001B[45m";
      case "cyan": return "\u001B[46m";
      case "white": return "\u001B[47m";
      case "default": return "\u001B[49m";
      case "reset": return "\u001B[0m";
      default: return "\u001B[49m";
    }
  }

  public static String resetMode() {return "\u001B[0m";}
  public static String boldMode(boolean enable) {return (enable) ? "\u001B[1m" : "\u001B[22m";}
  public static String dimMode(boolean enable) {return (enable) ? "\u001B[2m" : "\u001B[22m";}
  public static String italicMode(boolean enable) {return (enable) ? "\u001B[3m" : "\u001B[23m";}
  public static String underlineMode(boolean enable) {return (enable) ? "\u001B[4m" : "\u001B[24m";}
}
