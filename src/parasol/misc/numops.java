package parasol.misc;

public class numops {
  public static boolean isUint(String s) {
    if (s.length() == 0 || s == null) {return false;}
    return verifyDigits(s, 0, (byte)'0', (byte)'9');
  }
  public static boolean isInt(String s) {
    if (s.length() == 0 || s == null) {return false;}
    
    int i = (s.charAt(0) == '-') ? 1 : 0;
    return verifyDigits(s, i, (byte)'0', (byte)'9');
  }
  private static boolean verifyDigits(String s, int i, byte min_char_value, byte max_char_value) {
    if (i >= s.length()) {return true;}
    int char_value = (int)s.charAt(i);
    if (char_value < min_char_value || char_value > max_char_value) {return false;}
    return verifyDigits(s, i+1, min_char_value, max_char_value);
  }
}
