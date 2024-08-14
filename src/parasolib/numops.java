package parasolib;

public class numops {
  public static boolean isUint(String s) {
    if (s.length() == 0 || s == null) {return false;}
    return parse(s, 0);
  }
  public static boolean isInt(String s) {
    if (s.length() == 0 || s == null) {return false;}
    
    int i = (s.charAt(0) == '-') ? 1 : 0;
    return parse(s, i);
  }
  private static boolean parse(String s, int i) {
    char[] digits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    if (i >= s.length()) {return true;}
    boolean isDigit = false;
    for (int d = 0; d < digits.length; d++) {
      if (s.charAt(i) == digits[d]) {isDigit = true; break;}
    }
    if (!isDigit) {return false;}
    return parse(s, i+1);
  }
}