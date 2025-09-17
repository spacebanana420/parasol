package parasol.config;

public class ConfLine {
  public String key = null;
  public String value = null;
  
  public ConfLine(String line) {
    var parsed_key = new StringBuilder();
    var parsed_value = new StringBuilder();
    int value_start = -1;
    
    for (int i = 0; i < line.length(); i++) {
      char c = line.charAt(i);
      if (c == '=') {value_start = i+1; break;}
      parsed_key.append(c);
    }
    if (value_start == -1 || parsed_key.length() == 0) {return;}
    
    for (int i = value_start; i < line.length(); i++) {parsed_value.append(line.charAt(i));}
    if (parsed_value.length() == 0) {return;}
    
    key = parsed_key.toString().trim();
    value = parsed_value.toString().trim();
  }
}
