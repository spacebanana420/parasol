package parasolib;

import java.io.File;
import parasolib.*;

public class numberops {
    // public static float getfilesize(File file) {
    //     long size_bytes = file.length();
    //     float roundedsize = 0;
    //     if (size_bytes > 1000000000) {
    //         roundedsize = size_bytes / 1000000000;
    //     }
    //     else if (size_bytes > 1000000) {
    //         roundedsize = size_bytes / 1000000;
    //     }
    //     else if (size_bytes > 1000) {
    //         roundedsize = size_bytes / 1000;
    //     }
    //     return roundedsize;
    // }

    public static boolean isanumber(String text) {
        char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        boolean isnumber = false;
        for (int i = 0; i < text.length(); i++) {
            for (int x = 0; x < digits.length; x++) {
                if (digits[x] == text.charAt(i)) {isnumber = true;}
            }
            if (isnumber == false) {return false;}
        }
        return true;
    }
}
