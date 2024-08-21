package bananatui;

import java.util.Scanner;
import java.io.File;

public class userinput {
  public static String readUserInput() {return new Scanner(System.in).nextLine();}

  public static String readUserInput(String message) {
    if (message != "") {base.println(message);}
    return new Scanner(System.in).nextLine();
  }

  public static String spawnAndRead(String message) {return readUserInput("\u001B[3J\u001B[1J\u001B[H" + message);}

  public static boolean askPrompt(String ui, boolean clear) {
    String yellow = base.foreground("yellow"); String deflt = base.foreground("default");
    String prompt = (clear) ? "\u001B[3J\u001B[1J\u001B[H" + ui + yellow + "(y/n)" + deflt: ui + yellow + "(y/n)" + deflt;
    String answer = readUserInput(prompt).toLowerCase();
    return answer.equals("yes") || answer.equals("y");
  }

  public static int answerToNumber(String str) {
    try {return Integer.decode(str).intValue();}
    catch(Exception e) {return -1;}
  }

  public static short answerToShort(String str) {
    try {return Integer.decode(str).shortValue();}
    catch(Exception e) {return -1;}
  }

  public static byte answerToByte(String str) {
    try {return Integer.decode(str).byteValue();}
    catch(Exception e) {return -1;}
  }

  public static String pressToContinue(String message) {return readUserInput(message + "\n\nPress enter to continue");}

  private static String formList(String[] l, String title, String first, String txt, int i) {
    if (i >= l.length) {
      return title + "\n\n" + base.foreground("green") + "0: " + base.foreground("default") + first + "\n\n" + txt;
    }
    else {
      String line = base.foreground("green") + (i+1) + ": " + base.foreground("default") + l[i] + "\n";
      return formList(l, title, first, txt + line, i+1);
    }
  }

  private static String formList_long
  (
  String[] l, int size, String title, String first,
  int current, String txt, int i
  ) {
    if (i >= l.length) {return title + "\n\n" + base.foreground("green") + "0: " + base.foreground("default") + first + "\n\n" + txt;}
    if (current >= size-1) {
      String line = base.foreground("green") + (i+1) + ": " + base.foreground("dedfault") + l[i] + "\n";
      return formList_long(l, size, title, first, 0, txt + line, i+1);
    }
    String line = base.foreground("green") + (i+1) + ": " + base.foreground("dedfault") + l[i] + "\t\t";
    return formList_long(l, size, title, first, current+1, txt+line, i+1);
  }

  private static int findStringMatch(String[] opts, String key, int i) {
    if (i >= opts.length) {return -1;}
    if (opts[i].equals(key)) {return i;}
    return findStringMatch(opts, key, i+1);
  }

  public static int readLoop(String txt, int maxval) {
    int answer = answerToNumber(spawnAndRead(txt));
    if (answer == 0 || (answer > 0 && answer <= maxval)) {return answer;}
    return readLoop(txt, maxval);
  }

  public static int chooseOption(String[] l, String title, String first) {
    String txt_list = formList(l, title, first, "", 0);
    return readLoop(txt_list, l.length);
  }

  public static String chooseOption_string(String[] l, String title, String first) {
    int i = chooseOption(l, title, first);
    if (i == 0) {return "";}
    return l[i-1];
  }

  public static int chooseOption_h(String[] l, int size, String title, String first) {
    String txt_list = formList_long(l, size, title, first, 0, "", 0);
    return readLoop(txt_list, l.length);
  }

  public static String chooseOption_hs(String[] l, int size, String title, String first) {
    int i = chooseOption_h(l, size, title, first);
    if (i == 0) {return "";}
    return l[i-1];
  }

  public static int readInt(String txt) {
    int answer = answerToNumber(spawnAndRead(txt));
    if (answer != -1) {return answer;} return readInt(txt);
  }

  public static short readShort(String txt) {
    short answer = answerToShort(spawnAndRead(txt));
    if (answer != -1) {return answer;} return readShort(txt);
  }

  public static byte readByte(String txt) {
    byte answer = answerToByte(spawnAndRead(txt));
    if (answer != -1) {return answer;} return readByte(txt);
  }

  public static String chooseOption_dir(String txt) {
    String answer = spawnAndRead(txt);
    if (answer.equals(""))  {return ".";}
    if (new File(answer).isDirectory()) {return answer;}
    pressToContinue("That is not a real directory in your system!");
    return chooseOption_dir(txt);
  }
  public static String chooseOption_file(String txt) {
    String answer = spawnAndRead(txt);
    if (new File(answer).isFile()) {return answer;}
    pressToContinue("That is not a real file in your system!");
    return chooseOption_file(txt);
  }
}
