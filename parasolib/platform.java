package parasolib;

import java.io.File;
import parasolib.*;

public class platform {
    public static String os;
    public static boolean iswindows = false;
    public static boolean isunix = false;
    public static boolean ismac = false;

    public static void getplatform() {
        os = System.getProperty("os.name");
        if (os.contains("Windows") == true) {
            iswindows = true;
        }
        else if (os.contains("Mac") == true) {
            ismac = true;
        }
        else {
            isunix = true;
        }
    }

    public static void execute(String[] command) {
        // String output = "Executing commands: ";
        // for (int i = 0; i < command.length; i++) {
        //     output += command[i] + " ";
        // }
        // System.out.println(output);
        try {
            new ProcessBuilder(command).inheritIO().start().waitFor();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void executesilent(String[] command) {
        String redirectto = "";
        if (iswindows == true) {
            redirectto = "NULL";
        }
        else {
            redirectto = "/dev/null";
        }
        File redirectfile = new File(redirectto);
        try {
            ProcessBuilder cmdexec = new ProcessBuilder(command).inheritIO();
            cmdexec.redirectOutput(redirectfile);
            cmdexec.redirectError(redirectfile);
            cmdexec.start().waitFor();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    // public static void executesilent_old(String command) {
    //     //System.out.println("Executing command: " + command);
    //     try {
    //         Runtime.getRuntime().exec(command);
    //         }
    //     catch(Exception e) {
    //         e.printStackTrace();
    //     }
    // }

    public static String convertpath(String path) {
        boolean ignorechar = false;
        String finalpath = "";
        char pathchar;
        for (int i = 0; i < path.length(); i++) {
            pathchar = path.charAt(i);
            if (pathchar == '/') {
                if (path.charAt(i+1) == '/') {ignorechar = true;}
                finalpath += '\\';
            }
            else if (ignorechar == false) {finalpath += pathchar;}
            else {ignorechar = false;}
        }
        return finalpath;
    }
}
