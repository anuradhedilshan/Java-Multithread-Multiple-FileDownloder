package com.company;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WorkorManager {
    public static int NEWDOWNLOD = 100;
    public static int RESUMEDOWNLOD = 200;

    public ExecutorService addMission(int TaskId, String url, String path, String name, DownlodInfo di) {
        URLConnection con = this.getUrlConnetion(url, di);
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        File f = null;

        try {
            f = this.creatFile(url, path, name, con);

            int length = con.getContentLength();
            di.filesize = length;
            int off = 0;
            int start = 0;
            int stop = length / 2;
            String src = url;

            if (TaskId == this.NEWDOWNLOD) {
                executorService.execute(new WorkerRunnabel(off, stop, src, f, di));
                executorService.execute(new WorkerRunnabel(stop, length, src, f, di));
                executorService.shutdown();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return executorService;
    }

    public ExecutorService addMission(int TaskId, String url, String path, String name, DownlodInfo di, String ex) {
        URLConnection con = this.getUrlConnetion(url, di);
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        try {
            File f = new File(ex);
            String mimeType = con.getContentType();
            String extension = "." + MimeTypes.getDefaultExt(mimeType);
            if (f.exists() && f.canWrite()) {
                File d = new File(name + extension);
                if (!f.renameTo(d)) {
                    System.err.println("Cant Rename File");
                } else if (f.getFreeSpace() < con.getContentLength()) {
                    System.err.println("Not Enjoy Space");
                } else {
                    int length = con.getContentLength();
                    int downlodedSize = (int) f.length();
                    int needDownlod = length - downlodedSize;
                    int partSize = needDownlod / 2;
                    String src = url;
                    if (TaskId == this.RESUMEDOWNLOD) {
                        executorService.execute(new WorkerRunnabel(downlodedSize, partSize, src, f, di));
                        executorService.execute(new WorkerRunnabel(partSize, length, src, f, di));
                        executorService.shutdown();

                    }


                }

            } else {

                System.err.println("File Not exits or cant write");
            }

        } catch (Exception e) {

            Logger.getLogger(WorkorManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return executorService;
    }

    //-----------------------------------------------------------------------------------------------------------------------------------
    //Create FIle
    public File creatFile(String url, String paths, String names, URLConnection con) throws IOException {
        File downFile = null;
        File path = new File(paths);
        if (path.exists()) {
            String mimeType = con.getContentType();
            String extension = "." + MimeTypes.getDefaultExt(mimeType);
            System.err.println("extension = " + extension + "s" + path + "/" + names + extension);
            downFile = new File(paths + "/" + names + extension);

            downFile.createNewFile();
            return downFile;

        } else {
            path.mkdir();
            this.creatFile(url, paths, names, con);
        }
        return downFile;

    }

    //Creat Connection
    public URLConnection getUrlConnetion(String src, DownlodInfo di) {
        URLConnection con;
        try {
            URL url = new URL(src);
            con = url.openConnection();
            con.setDoOutput(true);

        } catch (Exception ex) {
            Logger.getLogger(WorkorManager.class.getName()).log(Level.SEVERE, null, ex);

            con = null;
        }
        return con;

    }
}
