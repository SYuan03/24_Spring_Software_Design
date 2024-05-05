package org.example.threadpool;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author SYuan03
 * @date 2024/4/11
 */
public class SimpleThreadPool {
    private static final int NUM_WORKERS = 5;
    private static SimpleThreadPool instance = null;
    private final Queue<Runnable> taskQueue = new LinkedList<>();
    private final SimpleWorkerThread[] workerThreads;

    private SimpleThreadPool() {
        workerThreads = new SimpleWorkerThread[NUM_WORKERS];
        for (int i = 0; i < NUM_WORKERS; i++) {
            workerThreads[i] = new SimpleWorkerThread(this);
            new Thread(workerThreads[i], "MySingleTP-Worker-" + i).start();
        }
    }

    public static synchronized SimpleThreadPool getInstance() {
        if (instance == null) {
            instance = new SimpleThreadPool();
        }
        return instance;
    }

    public synchronized void submit(Runnable task) {
        taskQueue.add(task);
        notifyAll(); // Notify a waiting worker thread
    }

    public synchronized Runnable getNextTask() {
        while (taskQueue.isEmpty()) {
            try {
                wait(); // Wait for a task to be available
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return taskQueue.poll();
    }

    // Shutdown the thread pool and interrupt all worker threads
    public synchronized void shutdown() {
        for (SimpleWorkerThread ignored : workerThreads) {
            Thread.currentThread().interrupt();
        }
    }
}