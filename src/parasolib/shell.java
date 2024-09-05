package parasolib;

import java.io.IOException;
import java.io.File;

import bananatui.base;
import bananatui.userinput;

public class shell {
  public static void runShell() {
    base.clear();
    base.println(getStartMessage());
    String lineStart = base.foreground("green") + "~ " + base.foreground("default");
    while (true) {
      base.print(lineStart);
      String prompt = userinput.readUserInput().trim();
      if (prompt.equals(":q") || prompt.equals(":quit")) {return;}
      if (prompt.length() == 0) {continue;}
      if (prompt.length() >= 2 && prompt.charAt(0) == ':' && prompt.charAt(1) == 'c') {continue;}
      String[] cmd = misc.groupStrings(prompt);
      if (cmd[0].equals("cd")) {
        if (cmd.length == 1) {browser.browser_directory = System.getProperty("user.home");}
        else if (new File(cmd[1]).isDirectory()) {browser.browser_directory = cmd[1];}
        else if (new File(browser.browser_directory + "/" + cmd[1]).isDirectory()) {
          browser.browser_directory += System.getProperty("path.separator") + cmd[1];
        }
        else {base.println("cd error: " + cmd[1] + " is not a directory!");}
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
      +"Type :q or :quit to leave\n"
      +"Include :c at the end of your command to cancel its execution\n";
  }
}
