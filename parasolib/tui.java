package parasolib;

import java.util.Scanner;
import java.io.*;
import parasolib.*;

public class tui {
    public static String getanswer() {
        Scanner answer = new Scanner(System.in);
        String answertxt = answer.nextLine();
        return answertxt;
    }

    public static String spawn(String text) {
        System.out.println(text);
        return getanswer();
    }

    public static void presstocontinue() {
        System.out.println("Press enter to continue");
        getanswer();
    }

    public static void clearterminal() {
        String[] wincmd = {"cmd", "/c", "cls"}; String[] unixcmd = {"clear"};
        if (platform.os.contains("Windows") == true) {
            platform.execute(wincmd);
        }
        else {
            platform.execute(unixcmd);
        }
    }

    public static void clearterminal_ascii() {
        System.out.print("\033[H\033[2J"); //motherfucking magic
        System.out.flush();
    }
}
