package parasol.browser;

import parasol.global;
import parasol.config.FileRunner;
import bananatui.userinput;

import java.lang.ProcessBuilder.Redirect;
import java.io.File;
import java.io.IOException;

//for platform-related stuff
public class platform {
  public static void openFile(String parent, String file) {
    String full_path = parent + "/" + file;
    for (FileRunner fr : global.FILE_RUNNERS) 
    {
      if (fr.hasValidExtension(file))
      {
        String[] cmd = fr.buildCommand(full_path);
        execute(cmd);
        return;
      }
    }
    String[] cmd = getRunnerCMD(full_path);
    execute(cmd);
  }

  public static void execute(String[] command) {
    try
    {
      ProcessBuilder pbuilder = new ProcessBuilder(command);
      if (global.PROCESS_INHERIT_IO) {pbuilder.inheritIO();}
      else {
        pbuilder.redirectOutput(Redirect.DISCARD); //java 9 and later support
        pbuilder.redirectError(Redirect.DISCARD);
      }
      if (global.PROCESS_WAIT_FOR_COMPLETION) {
        try{pbuilder.start().waitFor();}
        catch (InterruptedException e) {return;}
      }
      else {pbuilder.start();}
    }
    catch(IOException e) {
      e.printStackTrace();
      var txt = new StringBuilder("===Command arguments===\n");
      for (String arg : command) {txt.append(arg).append(' ');}
      userinput.pressToContinue(txt.append("\n============\n\nFailed to open file!").toString());
    }
  }
  
  private static String[] getRunnerCMD(String path) {
    String os = System.getProperty("os.name");
    if (os.contains("Windows")) {return new String[]{"explorer.exe", path};}
    else if (os.contains("Mac") || os.equals("Haiku")) {return new String[]{"open", path};}
    else {return new String[]{"xdg-open", path};}
  }  

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
        System.out.println("Windows ANSI support has been enabled, please restart your console and launch Parasol again.");
        System.exit(0);
      }
    }
    catch (IOException | InterruptedException e) {e.printStackTrace(); System.exit(0);}
  }
}
