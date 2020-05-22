# Java-Multithread-Multiple-FileDownloder
Supported to resume any  downloded manager Failed file(any fromate: like chrome)

    CustomDownlodManager downlodManager = CustomDownlodManager.getInstance();
        String src = "http://www.ict24.tk/static/images/fonts.jpg";
        String src2  ="http://www.ict24.tk/static/images/ict24.png";
        String path = "./downlod";
        int id = downlodManager.addMision(src,path,"check 1");
        int id1 = downlodManager.addMision(src2,path,"check 2");
