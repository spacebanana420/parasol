package parasolib;

public class misc {
  public static void selectionSort(String[] s) {
    String temp = "";
    for (int i = 0; i < s.length; i++) {
      int biggest = findBiggest(s, i);
      if (i != biggest) {
        temp = s[i];
        s[i] = s[biggest];
        s[biggest] = temp;
      }
    }
  }
  private static int findBiggest(String[] s, int i) {
    int biggest_i = i;
    for (int c = i; c < s.length; c++) {
      if (s[c].compareTo(s[biggest_i]) < 0) {biggest_i = c;}
    }
    return biggest_i;
  }
}
