package com.company;

public class Main {

    public static void main(String[] args) {
        CustomDownlodManager downlodManager = CustomDownlodManager.getInstance();
        String src = "http://www.ict24.tk/static/images/fonts.jpg";
        String src2  ="http://www.ict24.tk/static/images/ict24.png";
        String path = "./downlod";
//        int id = downlodManager.addMision(src,path,"check 1");
        int id1 = downlodManager.addMision(src2,path,"check 2");
   //     System.out.println("id1 ="+id +"id2 ="+id1);

    }
}
