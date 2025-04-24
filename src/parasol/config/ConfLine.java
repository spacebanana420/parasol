package parasol.config;

public class ConfLine {
  public String key = null;
  public String value = null;
  
  public ConfLine(String line) {
    String parsed_key = "";
    String parsed_value = "";
    int value_start = -1;
    
    for (int i = 0; i < line.length(); i++) {
      char c = line.charAt(i);
      if (c == '=') {value_start = i+1; break;}
      parsed_key += c;
    }
    if (value_start == -1 || parsed_key.length() == 0) {return;}
    
    for (int i = value_start; i < line.length(); i++) {parsed_value += line.charAt(i);}
    if (parsed_value.length() == 0) {return;}
    
    key = parsed_key.trim();
    value = parsed_value.trim();
  }
}
