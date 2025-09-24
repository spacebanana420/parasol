package parasol.commands;

import parasol.browser.*;
import java.io.File;
import bananatui.*;

public class cmd_filesize {
  public static void printSize_file(String path, boolean cli_mode) {
    File pathfile = new File(path);
    if (!pathfile.isFile() || !pathfile.canRead()) {
      userinput.pressToContinue("The file " + path + " does not exist, isn't real or cannot be read!");
      return;
    }
    String roundedsize = roundSize(getFileSize(path));
    String message = "Size of " + path + ":\n" + browsertui.COLOR_GREEN + roundedsize + browsertui.COLOR_DEFAULT;
    if (cli_mode) {base.println(message);}
    else {userinput.pressToContinue(message);}
  }

  public static void printSize_dir(String path, boolean cli_mode) {
    File pathfile = new File(path);
    if (!pathfile.isDirectory() || !pathfile.canRead()) {
      userinput.pressToContinue("The directory " + path + " does not exist, isn't real or cannot be read!");
      return;
    }
    String roundedsize = roundSize(getDirSize(path));
    String message = "Size of " + path + ":\n" + browsertui.COLOR_GREEN + roundedsize + browsertui.COLOR_DEFAULT;
    if (cli_mode) {base.println(message);}
    else {userinput.pressToContinue(message);}
  }

  private static String roundSize(long size) {
    double scaledsize = size;
    String unit = " bytes";
    boolean roundNumber = false;
    if (size > 1000000000) {scaledsize = size / 1000000000f; unit = " GB"; roundNumber = true;}
    else if (size > 1000000) {scaledsize = size / 1000000f; unit = " MB"; roundNumber = true;}
    else if (size > 1000) {scaledsize = size / 1000f; unit = " KB"; roundNumber = true;}
    
    if (roundNumber){scaledsize = Math.floor(scaledsize * 1000)/1000f;}
    return scaledsize + unit;
  }

  private static long getFileSize(String path) {return new File(path).length();}

  public static long getDirSize(String path) {
    String[] path_list = new File(path).list();
    long dir_size = 0L;
    for (String p : path_list) {
      String full_path = path + "/" + p; File pf = new File(full_path);
      if (pf.isFile()) {dir_size += pf.length();}
      else {dir_size += getDirSize(full_path);}
    }
    return dir_size;
  }

  static void sortFilesBySize(String parent, String[] files) {
    if (files.length == 0) {userinput.pressToContinue("There are no files in this directory!"); return;}
    long[] fileSizes = new long[files.length];
    String[] files_with_size = new String[files.length];

    for (int i = 0; i < files.length; i++) {
      fileSizes[i] = getFileSize(parent + "/" + files[i]);
      files_with_size[i] =
        files[i] + "  "
        + browsertui.COLOR_GREEN
        + roundSize(fileSizes[i])
        + browsertui.COLOR_DEFAULT;
    }
    sortSizes(files_with_size, fileSizes);
    
    var screen = new StringBuilder();
    screen.append("Sorting files by size:\n\n");
    for (int i = 0; i < files_with_size.length; i++) {
      screen.append(browsertui.COLOR_GREEN).append(i+1).append(": ").append(browsertui.COLOR_DEFAULT);
      screen.append(files_with_size[i]).append("\n");
    }
    
    base.clear();
    userinput.pressToContinue(screen.toString());
  }

  private static void sortSizes(String[] files, long[] sizes) {
    String temp = "";
    long templ = 0;
    for (int i = 0; i < files.length; i++) {
      int biggest = findBiggestFile(sizes, i);
      if (i != biggest) {
        temp = files[i];
        files[i] = files[biggest];
        files[biggest] = temp;

        templ = sizes[i];
        sizes[i] = sizes[biggest];
        sizes[biggest] = templ;
      }
    }
  }

  private static int findBiggestFile(long[] sizes, int i) {
    int biggest_i = i;
    for (int c = i; c < sizes.length; c++) {
      if (sizes[c] > sizes[biggest_i]) {biggest_i = c;}
    }
    return biggest_i;
  }
}
