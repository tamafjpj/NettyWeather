package com.company;

import org.junit.Test;

import java.util.PriorityQueue;
import java.util.Queue;

import static org.junit.Assert.*;

public class TaskGeneratorTest {

    @Test
    public void generateTasks() {
        Queue<String> inQueue = new PriorityQueue<>(20);
        TaskGenerator tg = new TaskGenerator(inQueue);
        assertEquals(tg.getCities().length, inQueue.size());
    }
}