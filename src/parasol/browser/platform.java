package parasol.browser;
import java.io.File;

//for platform-related stuff
public class platform {
  public static String[] getSystemDisks() {
    File[] froots = File.listRoots();
    String[] root_paths = new String[froots.length];
    for (int i = 0; i < root_paths.length; i++) {
      root_paths[i] = froots[i].getAbsolutePath();
    }
    return root_paths;
  }
}
