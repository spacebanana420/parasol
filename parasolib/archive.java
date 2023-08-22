package parasolib;

import parasolib.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.FileOutputStream;

public class archive { //4bytes filesize 2bytes namesize Nbytes name Nbytes file
    public static void createarchive(String basedir) {
        String[] paths = new File(basedir).list();
        // short filecount = howmanyfiles(basedir, paths);

        try {Files.createFile(Paths.get(basedir + "/" + "archive.bana"));}
        catch(Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < paths.length; i++) {
            File pathfile = new File(basedir + "/" + paths[i]);
            if (pathfile.isFile() == true) {
                System.out.println("Writing to archive: " + paths[i]);
                addfile(paths[i], basedir + "/" + paths[i], basedir + "/" + "archive.bana");
            }
        }
        tui.spawn("Archive created!");
    }

    private static void addfile(String path, String fullpath, String fullarchive) {
        Path archivepath = Paths.get(fullarchive);
        Path fullpath_path = Paths.get(fullpath);

        long filesize = 0;;
        try {
            filesize = Files.size(fullpath_path);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        int namelength = path.length();

        int filesize_limited = (int) filesize;
        short namelength_limited = (short) namelength;

        if (filesize_limited != filesize) {
            tui.spawn("Size " + filesize_limited + " is not the same as " + filesize);
            return;
        }
        if (namelength_limited != namelength) {
            tui.spawn("Name " + namelength_limited + " is not the same as " + namelength);
            return;
        }
        try {
            FileOutputStream archivefile = new FileOutputStream(fullarchive, true);
            byte[] namelength_final = shortToBytes(namelength_limited);
            byte[] filesize_final = intToBytes(filesize_limited);
            archivefile.write(filesize_final); archivefile.write(namelength_final);
            //Files.write(archivepath, filesize_final); Files.write(archivepath, namelength_final);

            byte[] namebytes = path.getBytes();
            byte[] filebytes = Files.readAllBytes(fullpath_path);
            archivefile.write(namebytes); archivefile.write(filebytes);
            //Files.write(archivepath, namebytes); Files.write(archivepath, filebytes);

            archivefile.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void extractArchive(String basedir) {
    }

    private static byte[] intToBytes(int num) {
        return new byte[] {
        (byte) (num >> 24), (byte) (num >> 16), (byte) (num >> 8), (byte) (num)
        };
    }

    private static byte[] shortToBytes(short num) {
        return new byte[] {
        (byte) (num >> 8), (byte) (num)
        };
    }

    // private static short howmanyfiles(String basedir, String[] paths) {
    //     File pathfile;
    //     short filecount = 0;
    //     for (int i = 0; i < paths.length; i++) {
    //         pathfile = new File(basedir + "/" + paths[i]);
    //         if (pathfile.isFile() == true) {filecount += 1;}
    //     }
    //     return filecount;
    // }
}
