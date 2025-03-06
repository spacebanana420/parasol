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

  public static boolean enableAsciiSupportWindows() {
    try {
      // check if the VirtualTerminalLevel key exists
      String[] checkCmd = {"cmd", "/c", "reg", "query", "HKCU\\Console", "/v", "VirtualTerminalLevel"};
      Process checkProcess = Runtime.getRuntime().exec(checkCmd);
      checkProcess.waitFor();

      if (checkProcess.exitValue() != 0) {
        // Enable Virtual Terminal Processing (enables ANSI support for Windows terminals)
        String[] enableCmd = {"cmd", "/c", "reg", "add", "HKCU\\Console", "/v", "VirtualTerminalLevel", "/t", "REG_DWORD", "/d", "1", "/f"};
        Process enableProcess = Runtime.getRuntime().exec(enableCmd);
        enableProcess.waitFor();

        if (enableProcess.exitValue() == 0) {
          System.out.println("Windows ANSI support was enabled, please launch Parasol again.");
          Runtime.getRuntime().exit(0);
        }
        return enableProcess.exitValue() == 0;
      }
      return true;
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
      return false;
    }
  }
}
