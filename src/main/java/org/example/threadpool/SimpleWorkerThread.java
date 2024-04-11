package org.example.threadpool;

/**
 * @author SYuan03
 * @date 2024/4/11
 */
public class SimpleWorkerThread implements Runnable {
    private final SimpleThreadPool threadPool;

    public SimpleWorkerThread(SimpleThreadPool threadPool) {
        this.threadPool = threadPool;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            Runnable task = threadPool.getNextTask();
            if (task != null) {
                task.run();
            }
        }
    }
}