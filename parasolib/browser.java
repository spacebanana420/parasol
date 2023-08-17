package parasolib;

import java.io.File;
import parasolib.*;

public class browser {
    public static String currentdirectory = new File("").getAbsolutePath();

    public static String[][] browse() {
        tui.clearterminal();
        String output = "Current Directory: " + currentdirectory +  "\n\n0. Exit     1. Go back\n\n-----Directories-----\n";

        String[] paths = new File(currentdirectory).list();
        String[][] finalpaths = new String[2][paths.length];
        int dirs_i = 0; int files_i = 0;
        int count = 2;
        int entriesadded = 0;

        for (int i = 0; i < paths.length; i++) {
            File pathobject = new File(currentdirectory + "/" + paths[i]);
            //if (pathobject.exists() == false) {System.out.println(currentdirectory + paths[i] + " does not exist"); tui.getanswer();}
            if (pathobject.isFile() == true) {
                finalpaths[1][files_i] = paths[i]; files_i += 1;
            }
            else if (pathobject.exists() == true) {
                finalpaths[0][dirs_i] = paths[i]; dirs_i += 1;
            }
        }


        for (int i = 0; i < finalpaths[0].length; i++) {
            if (finalpaths[0][i] == null) {break;}
            if (entriesadded == 3) {
                output += "\n"; entriesadded = 0;
            }
            output += count + ": " + finalpaths[0][i] + "     ";
            entriesadded += 1;
            count += 1;
        }

        output += "\n\n-----Files-----\n";
        entriesadded = 0;
        for (int i = 0; i < finalpaths[1].length; i++) {
        if (finalpaths[1][i] == null) {break;}
            if (entriesadded == 3) {
                output += "\n"; entriesadded = 0;
            }
            output += count + ": " + finalpaths[1][i] + "     ";
            entriesadded += 1;
            count += 1;
        }
        output += "\n\nChoose an entry";
        System.out.println(output);
        return finalpaths;
    }

public static void findentry(String subname) {
        tui.clearterminal();
        String output = "Current Directory: " + currentdirectory +  "\n\nFound the following entries that contain \"" + subname + "\":\n\n-----Directories-----\n";

        String[] paths = new File(currentdirectory).list();
        String[][] finalpaths = new String[2][paths.length];
        int dirs_i = 0; int files_i = 0;
        int count = 2;
        int entriesadded = 0;

        for (int i = 0; i < paths.length; i++) {
            File pathobject = new File(currentdirectory + "/" + paths[i]);
            if (pathobject.isFile() == true) {
                finalpaths[1][files_i] = paths[i]; files_i += 1;
            }
            else if (pathobject.exists() == true) {
                finalpaths[0][dirs_i] = paths[i]; dirs_i += 1;
            }
        }


        for (int i = 0; i < finalpaths[0].length; i++) {
            if (finalpaths[0][i] == null) {break;}
            if (entriesadded == 3) {
                output += "\n"; entriesadded = 0;
            }
            if (finalpaths[0][i].contains(subname) == true) {
                output += count + ": " + finalpaths[0][i] + "     ";
                entriesadded += 1;
            }
            count += 1;
        }

        output += "\n\n-----Files-----\n";
        entriesadded = 0;
        for (int i = 0; i < finalpaths[1].length; i++) {
        if (finalpaths[1][i] == null) {break;}
            if (entriesadded == 3) {
                output += "\n"; entriesadded = 0;
            }
            if (finalpaths[1][i].contains(subname) == true) {
                output += count + ": " + finalpaths[1][i] + "     ";
                entriesadded += 1;
            }
            count += 1;
        }
        output += "\n\nPress enter to continue";
        tui.spawn(output);
    }


    public static void openentry(int answer, String[][] paths) {
        int count = 2;
        for (int i = 0; i < paths[0].length; i++) {
            if (paths[0][i] == null) {break;}
            if (answer == count) {
                currentdirectory = currentdirectory + "/" + paths[0][i];
                return;
            }
            count += 1;
        }
        for (int i = 0; i < paths[1].length; i++) {
            if (paths[1][i] == null) {break;}
            if (answer == count) {
                openfile(currentdirectory + "/" + paths[1][i]);
                return;
            }
            count += 1;
        }
    }

    private static void openfile(String filename) {
        String[] wincmd = {"explorer.exe", filename};
        //String[] unixcmd = {"xdg-open", filename};
        String unixsilent = "xdg-open " + filename;
        if (platform.os.contains("Windows") == true) {
            platform.execute(wincmd);
        }
        else {
            platform.executesilent(unixsilent);
            //platform.execute(unixcmd);
        }
    }

    public static String findpath_relative(int val, String[][] paths) {
        int count = 2;
        for (int i = 0; i < paths[0].length; i++) {
            if (paths[0][i] == null) {break;}
            if (val == count) {
                return paths[0][i];
            }
            count += 1;
        }
        for (int i = 0; i < paths[1].length; i++) {
            if (paths[1][i] == null) {break;}
            if (val == count) {
                return paths[1][i];
            }
            count += 1;
        }
        return null;
    }

    public static String findfile_relative(int val, String[][] paths) {
        int count = 2;
        for (int i = 0; i < paths[0].length; i++) {
            if (paths[0][i] == null) {break;}
            count += 1;
        }
        for (int i = 0; i < paths[1].length; i++) {
            if (paths[1][i] == null) {break;}
            if (val == count) {
                return paths[1][i];
            }
            count += 1;
        }
        return null;
    }

    public static String findpath_absolute(int val, String[][] paths) {
        int count = 2;
        for (int i = 0; i < paths[0].length; i++) {
            if (paths[0][i] == null) {break;}
            if (val == count) {
                return currentdirectory + "/" + paths[0][i];
            }
            count += 1;
        }
        for (int i = 0; i < paths[1].length; i++) {
            if (paths[1][i] == null) {break;}
            if (val == count) {
                return currentdirectory + "/" + paths[1][i];
            }
            count += 1;
        }
        return null;
    }
}
