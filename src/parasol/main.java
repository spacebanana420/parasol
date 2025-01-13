package parasol;

import parasol.browser.*;
import parasol.config.confio;
import java.io.File;

import bananatui.base;
import bananatui.extra;

public class main {
  public static void main(String[] args) {
    if (System.getProperty("os.name").contains("Windows")) {extra.windows_enableASCII();}
    if (displayedInfo(args)) {return;}
    
    confio.initialize();
    global.assignConfigValues();
    global.SHELL_SILENT = cli.silentShell(args);
    global.SHOW_HIDDEN_FILES = cli.showHiddenFiles(args);
    global.DISPLAY_VERTICALLY_ONLY = cli.displayVertically(args);
    
    browser.BROWSER_DIRECTORY = cli.getPath(args);
    if (cli.checkShell(args)) {shell.runShell();}
    else {browser.runBrowser();}
  }
  
  static boolean displayedInfo(String[] args) {
    if (cli.checkForHelp(args)) {
      String help = global.getHelpMessage();
      base.println(help);
      return true;
    }
    if (cli.checkForVersion(args)) {
      base.println("Parasol version " + global.PARASOL_VERSION);
      return true;
    }
    if (cli.checkForSize(args)) {
      String help = global.getHelpMessage();
      base.println(help);
      return true;
    }
    return cli.checkForSize(args);
  }
}
