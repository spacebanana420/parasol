package test;

import java.nio.file.FileSystems;
import java.nio.file.FileStore;

//import bananatui.*;

public class test {
  public static void main(String[] args) {
    freebsd_test();
  }
  private static void freebsd_test() {
    for (FileStore store: FileSystems.getDefault().getFileStores()) {
      var a = store.toString();
      //if (!a.contains("(/dev")) {continue;}
      println(a); println(store.type());
    }
  }

  private static void println(String msg) {System.out.println(msg);}
}