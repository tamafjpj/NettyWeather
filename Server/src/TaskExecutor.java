package com.company;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Queue;

public class TaskExecutor implements Runnable{
    private dbService db = dbService.INSTANCE;
    private Queue<String> inQueue;
    private String city;
    private Thread thread;

    public TaskExecutor(Queue<String> inQueue){
        this.inQueue=inQueue;
        thread=new Thread(this,"Executor");
        thread.start();
    }

    public void run() {
        System.out.println(thread.getName());
        executeTask();
    }
    private void executeTask() {

            synchronized (inQueue) {
                if (!inQueue.isEmpty()) {
                    this.city = inQueue.poll();
                }
            }
                try {
                    Weather weather = new Weather(city);
                    if (weather.isParsed) {
                        Date date = new Date();
                        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat tm = new SimpleDateFormat("kk:mm:ss");
                        System.out.println("Writing to Data Base...");
                        db.insert(weather.getCity(), weather.getWindSpeed(),
                                weather.getTemperature(), weather.getPressure(),
                                dt.format(date), tm.format(date));
                    }
                    }catch(NullPointerException e){
                        e.printStackTrace();
                    }


            }
        }


