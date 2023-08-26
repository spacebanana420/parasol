import parasolib.*;
import java.io.File;

public class main {

    public static void main(String[] args) {
        platform.getplatform();
        while (true) {
            String[][] paths = browser.browse();
            String answer = tui.getanswer();
            switch (answer) {
                case "0":
                    return;
                case "1":
                    String newdir = new File(browser.currentdirectory).getParent();
                    if (newdir != null) {
                        browser.currentdirectory = newdir;
                    }
                    break;
                default:
                    if (numberops.isanumber(answer) == true) {
                        browser.openentry(Integer.parseInt(answer), paths);
                    }
                    else if (answer != "") {
                        commands.commandbase(answer, paths);
                    }
            }
        }
    }

}
