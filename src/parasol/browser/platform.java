package parasol.browser;
import java.io.File;
import java.io.IOException;

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

  public static void enableANSIWindows() {
    try {
      // check if the VirtualTerminalLevel key exists
      String[] cmd = new String[]{"cmd", "/c", "reg", "query", "HKCU\\Console", "/v", "VirtualTerminalLevel"};
      Process checkProcess = Runtime.getRuntime().exec(cmd);
      checkProcess.waitFor();

      if (checkProcess.exitValue() == 0) {return;}
      // Enable Virtual Terminal Processing (enables ANSI support for Windows terminals)
      cmd = new String[]{"cmd", "/c", "reg", "add", "HKCU\\Console", "/v", "VirtualTerminalLevel", "/t", "REG_DWORD", "/d", "1", "/f"};
      Process enableProcess = Runtime.getRuntime().exec(cmd);
      enableProcess.waitFor();

      if (enableProcess.exitValue() == 0) {
        System.out.println("Windows ANSI support was enabled, please launch Parasol again.");
        System.exit(0);
      }
    }
    catch (IOException | InterruptedException e) {
      e.printStackTrace();
      System.exit(0);
    }
  }
}
