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
                    if (isanumber(answer) == true) {
                        browser.openentry(Integer.parseInt(answer), paths);
                    }
            }
        }
    }

    public static boolean isanumber(String text) {
        char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        boolean isnumber = false;
        for (int i = 0; i < text.length(); i++) {
            for (int x = 0; x < digits.length; x++) {
                if (digits[x] == text.charAt(i)) {isnumber = true;}
            }
            if (isnumber == false) {return false;}
        }
        return true;
    }
}
