package parasolib;

import java.io.IOException;
import java.io.File;

import bananatui.base;
import bananatui.userinput;

public class shell {
  public static void runShell() {
    base.clear();
    base.println(getStartMessage());
    while (true) {
      String lineStart =
        base.foreground("green")
        + "[" + System.getProperty("user.name") + ":" + shortenPath(browser.browser_directory)
        + "]$ " + base.foreground("default");

      base.print(lineStart);
      String prompt = userinput.readUserInput().trim();
      if (userPromptedExit(prompt)) {return;}
      if (prompt.length() == 0) {continue;}

      String[] cmd = misc.groupStrings(prompt);
      if (cmd[0].equals("cd")) {
        changeDirectory(cmd);
        continue;
      }
      try {
        new ProcessBuilder(cmd)
          .directory(new File(browser.browser_directory))
          .inheritIO()
          .start()
          .waitFor();
      }
      catch (IOException e) {base.println("Error running \"" + cmd[0] + "\": process not found");}
      catch (InterruptedException e) {base.println("Error running \"" + cmd[0] + "\": process was improperly interrupted");}
    }
  }

  // private static String getPathIndicator() {

  // }

  private static String getStartMessage() {
    return
      base.foreground("green")+"[Parasol Shell]\n"+base.foreground("default")
      +"Type :q or :quit to leave\n";
  }

  private static boolean onlyDots(String path) {
    for (int i = 0; i < path.length(); i++) {
      if (path.charAt(i) != '.') {return false;}
    }
    return true;
  }

  private static boolean userPromptedExit(String prompt) {
    return prompt.equals(":q") || prompt.equals(":quit") || prompt.equals("exit");
  }

  private static void changeDirectory(String[] cmd) {
    if (cmd.length == 1) {browser.browser_directory = System.getProperty("user.home");}
    else if (cmd[1].equals("..")) {
      String newparent = new File(browser.browser_directory).getParent();
      if (newparent != null) {browser.browser_directory = newparent;}
    }
    else if (onlyDots(cmd[1])) {return;}
    else if (new File(cmd[1]).isDirectory()) {
      browser.browser_directory = new File(cmd[1]).getAbsolutePath();
    }
    else if (new File(browser.browser_directory + "/" + cmd[1]).isDirectory()) {
      browser.browser_directory += System.getProperty("path.separator") + cmd[1];
    }
    else {base.println("cd error: " + cmd[1] + " is not a directory!");}
  }

  private static String shortenPath(String path) {
    String home = System.getProperty("user.home");
    if (misc.startsWith(path, home)) {
      return  "~" + path.replaceFirst(home, "");
    }
    return path;
  }
}