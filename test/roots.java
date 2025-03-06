import java.io.File;

public class roots {
    public static void main(String[] args) {
      File[] froots = File.listRoots();
      String txt = "";
      for (File f : froots) {
        txt += f.getAbsolutePath() + "\n";
      }
      System.out.println(txt);
    }
}
