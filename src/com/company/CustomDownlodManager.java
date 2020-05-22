package com.company;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CustomDownlodManager {
    ExecutorService es = Executors.newFixedThreadPool(2);
    WorkorManager wm = new WorkorManager();
    int idM = 1;
    public static HashMap<Integer, ExecutorService> executorServiceHashMap = new HashMap<>();
    public static HashMap<Integer, DownlodInfo> downlodInfoHashMap = new HashMap<>();


    public CustomDownlodManager() {

    }

    public static CustomDownlodManager getInstance() {
        return new CustomDownlodManager();
    }

    // return Id
    int addMision(String url, String path, String name) {

        int id = idM++;
        downlodInfoHashMap.put(id, new DownlodInfo());
        es.execute(() -> {
            synchronized (this) {
                ExecutorService s = wm.addMission(wm.NEWDOWNLOD, url, path, name, this.downlodInfoHashMap.get(id));

            }
        });
        System.out.println("Downlod hashmap sixe : " + this.downlodInfoHashMap.size());

        return id;


    }

    int addExitsMission(String url, String path, String name, String exFile) {
        int id = this.getId();
        DownlodInfo d = downlodInfoHashMap.put(id, new DownlodInfo());
        es.execute(() -> {
            ExecutorService s = wm.addMission(wm.RESUMEDOWNLOD, url, path, name, d, exFile);
            this.executorServiceHashMap.put(id, s);
        });


        return id;
    }


    private int getId() {
        System.out.println("Size : " + executorServiceHashMap.size());
        int length = executorServiceHashMap.size();
        this.idM += length;
        int id = length;
        if (length == this.idM) {
            id++;
        }
        return id;

    }

    public void shutDown() {
        es.shutdown();
        try {
            es.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
