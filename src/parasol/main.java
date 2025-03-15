package parasol;

import parasol.browser.*;
import parasol.config.confio;
import parasol.cli.cli;
import parasol.cli.help;

import java.io.File;

import bananatui.base;
import bananatui.extra;

public class main {
  public static void main(String[] args) {
    if (System.getProperty("os.name").contains("Windows")) {platform.enableANSIWindows();}
    if (displayedInfo(args)) {return;}
    
    confio.initialize();
    global.assignValues(args);
    browser.BROWSER_DIRECTORY = cli.getPath(args);
    
    if (cli.checkOpenConfig(args)) {browser.openConfig();}
    else if (cli.checkShell(args)) {shell.runShell();}
    else {browser.runBrowser();}
  }
  
  static boolean displayedInfo(String[] args) {
    if (cli.checkForHelp(args)) {
      base.println(help.getHelpMessage());
      return true;
    }
    if (cli.checkForVersion(args)) {
      base.println("Parasol version " + global.PARASOL_VERSION);
      return true;
    }
    if (cli.checkForSize(args)) {
      base.println(help.getHelpMessage());
      return true;
    }
    return cli.checkForSize(args);
  }
}
