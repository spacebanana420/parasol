import parasolib.*;
import java.io.File;

public class main {
  public static void main(String[] args) {
    browser.runBrowser(getPath(args));
  }

  private static String getPath(String[] args) {
    String absoluteParent = new File("").getAbsolutePath();
    for (int i = 0; i < args.length; i++) {
      if (new File(args[i]).isDirectory()) {
        return new File(args[i]).getAbsolutePath();
      }
      if (args[i] == "") {return absoluteParent;}
    }
    return absoluteParent;
  }
}
