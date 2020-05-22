package com.company;

import java.io.Serializable;

public class DownlodInfo {
    public int downloded = 0;
    public int filesize = 0;
    public boolean PAUSE = false;
    public boolean CANCELL = false;
    String name = null;

    public void setProgress(int downlodedP) {


        this.downloded = this.downloded + downlodedP;
        System.err.println("file size = " + filesize + " - " + "downloded  = " + downloded);
        if (filesize <= downloded) {
            System.err.println("downloded Complete");
        }
    }


}
