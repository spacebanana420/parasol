package parasolib;

public class platform {
    public static String os;
    //public static boolean iswindows;

    public static void getplatform() {
        os = System.getProperty("os.name");
    }

    public static void execute(String[] command) {
        try {
            new ProcessBuilder(command).inheritIO().start().waitFor();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    public static void executesilent(String command) {
        try {
            Runtime.getRuntime().exec(command);
            }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
