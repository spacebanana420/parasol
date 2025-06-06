package parasol.browser;

import java.io.IOException;
import java.io.File;

import parasol.global;
import parasol.cli.help;
import parasol.misc.misc;

import bananatui.base;
import bananatui.userinput;

public class shell {
  public static void runShell() {
    String previous_line = "";
    base.clear();
    if (!global.SHELL_SILENT) {base.println(getStartMessage());}
    while (true) {
      String lineStart =
        base.boldMode(true)
        + browsertui.COLOR_GREEN
        + "[" + System.getProperty("user.name") + ":" + shortenPath(browser.BROWSER_DIRECTORY)
        + "]$ " + browsertui.COLOR_DEFAULT
        + base.boldMode(false);

      base.print(lineStart);
      String prompt = userinput.readUserInput().trim();
      if (prompt.length() == 0) {continue;}
      if (userPromptedExit(prompt)) {return;}
      if (repeatLastCommand(prompt, previous_line)) {prompt = previous_line;}
      if (prompt.equals(":v")) {
        if (previous_line.length() != 0) {
          base.println("Last executed command: " + base.boldMode(true) + previous_line + base.boldMode(false));
        }
        continue;
      }
      if (prompt.equals("help")) {base.println(help.getHelpMessage()); continue;}
      if (prompt.equals(":h")) {base.println(help.getShellHelp()); continue;}

      String[] cmd = misc.groupStrings(prompt);
      replaceHomeAbbreviation(cmd);
      if (cmd[0].equals("cd")) {
        changeDirectory(cmd);
        continue;
      }
      try {
        new ProcessBuilder(cmd)
          .directory(new File(browser.BROWSER_DIRECTORY))
          .inheritIO()
          .start()
          .waitFor();
      }
      catch (IOException e) {base.println("Error running \"" + cmd[0] + "\": process not found");}
      catch (InterruptedException e) {base.println("Error running \"" + cmd[0] + "\": process was interrupted");}
      previous_line = prompt;
    }
  }
  
  private static String getStartMessage() {
    return
      browsertui.COLOR_GREEN+"[Parasol Shell]\n"+browsertui.COLOR_DEFAULT
      +"Type :h for help and :q or :quit to leave\n";
  }

  public static boolean onlyDots(String path) {
    for (int i = 0; i < path.length(); i++) {
      if (path.charAt(i) != '.') {return false;}
    }
    return true;
  }

  private static boolean userPromptedExit(String prompt) {
    return prompt.equals(":q") || prompt.equals(":quit") || prompt.equals("exit");
  }

  private static boolean repeatLastCommand(String prompt, String previous_line) {
    return prompt.equals(":l") && previous_line.length() != 0;
  }

  private static void changeDirectory(String[] cmd) {
    if (cmd.length == 1) {browser.BROWSER_DIRECTORY = System.getProperty("user.home");}
    else if (cmd[1].equals("..")) {
      String newparent = new File(browser.BROWSER_DIRECTORY).getParent();
      if (newparent != null) {browser.BROWSER_DIRECTORY = newparent;}
    }
    else if (onlyDots(cmd[1])) {return;}
    else if (new File(cmd[1]).isDirectory()) {
      browser.BROWSER_DIRECTORY = new File(cmd[1]).getAbsolutePath();
    }
    else if (new File(browser.BROWSER_DIRECTORY + "/" + cmd[1]).isDirectory()) {
      browser.BROWSER_DIRECTORY += System.getProperty("file.separator") + cmd[1];
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

  private static void replaceHomeAbbreviation(String[] cmd) { //for ~ to be translated into the home directory
    String home = System.getProperty("user.home");
    char path_separator = System.getProperty("file.separator").charAt(0);

    for (int i = 0; i < cmd.length; i++) {
      if (new File(cmd[i]).exists()) {continue;}
      if (cmd[i].equals("~")) {cmd[i] = home; continue;}
      if (
        cmd[i].length() > 1
        && cmd[i].charAt(0) == '~'
        && cmd[i].charAt(1) == path_separator
        ) {
        String replacement = cmd[i].replaceFirst("~", home);
        if (new File(replacement).exists()) {cmd[i] = replacement;}
      }
    }
  }
}
