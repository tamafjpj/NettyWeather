package com.company;

import java.util.Arrays;
import java.util.Queue;

public class TaskGenerator {
    private Queue<String> taskQueue;
    private String[] cities = new String[]{"Moscow", "Kaliningrad", "Pyatigorsk", "Saint-petersburg", "Chelyabinsk",
            "Nizhny-novgorod", "Novosibirsk", "Vladivostok", "Krasnoyarsk", "Kazan",
            "Ufa", "Rostov-na-donu", "Samara", "Yekaterinburg", "Omsk"};

    TaskGenerator(Queue<String> inQueue) {
        this.taskQueue = inQueue;
        generateTasks();
    }

    public void generateTasks() {
        taskQueue.addAll(Arrays.asList(cities));
    }

    public String[] getCities() {
        return cities;
    }
}
