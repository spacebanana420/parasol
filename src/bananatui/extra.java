package bananatui;

public class extra {
  public static boolean windows_enableASCII() {
    try {
      new ProcessBuilder("cmd", "/c", "echo", "").start();
      return true;
    }
    catch(Exception e) {return false;}
  }
}
