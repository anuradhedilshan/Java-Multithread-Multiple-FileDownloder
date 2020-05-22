package com.company;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

class WorkerRunnabel implements Runnable {

    int off;
    int stop;
    int progress = 0;
    URLConnection con;
    File file;
    RandomAccessFile raf;
    URL url;
    BufferedInputStream bis;
    DownlodInfo df;
    String src;

    public WorkerRunnabel(int off, int stop, String src, File file, DownlodInfo df) {
        this.off = off;
        this.stop = stop;
        System.out.println(file.getAbsoluteFile());
        try {
            this.url = new URL(src);
            this.con = url.openConnection();
        } catch (Exception ex) {
            System.out.println("downlodmannager.worker.<init>()" + ex);
        }
        this.df = df;
        this.src = src;
        this.file = file;

    }

    @Override
    public void run() {
        System.out.println("downlodmannager.worker.run()");
        this.df.name = this.file.getName();
        try {
            this.down();
        } catch (IOException ex) {
            Logger.getLogger(WorkerRunnabel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void down() throws IOException {
        System.out.println("Down");
        this.con.setRequestProperty("Range", "bytes=" + off + "-" + stop);
        this.raf = new RandomAccessFile(this.file, "rw");
        this.bis = new BufferedInputStream(con.getInputStream());
        byte[] buffer = new byte[8 * 1024];
        raf.seek(off);
        int read = 0;
        while ((read = bis.read(buffer)) != -1) {
            this.progress += read;
            raf.write(buffer, 0, read);
            this.df.setProgress(read);
            System.out.println("Name = : " + this.df.name + " - " + "full size : " + this.df.filesize + " - " + "downloded : " + this.df.downloded + " - REaded :" + read);

        }
        this.raf.close();
    }

}