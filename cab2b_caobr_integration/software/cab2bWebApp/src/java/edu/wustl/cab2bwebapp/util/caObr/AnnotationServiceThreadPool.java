package edu.wustl.cab2bwebapp.util.caObr;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AnnotationServiceThreadPool {

    private int poolSize = 10;

    private int maxPoolSize = 30;

    private long keepAliveTime = 10;

    private ThreadPoolExecutor threadPool = null;

    final ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(40);

    public AnnotationServiceThreadPool() {
        threadPool = new ThreadPoolExecutor(poolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS, queue);

    }

    public void runTask(Runnable task) {
        threadPool.execute(task);
    }

    public void shutDown() {
        threadPool.shutdown();
    }

    /**
     * @return the poolSize
     */
    public int getPoolSize() {
        return poolSize;
    }

    /**
     * @param poolSize the poolSize to set
     */
    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    /**
     * @return the maxPoolSize
     */
    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    /**
     * @param maxPoolSize the maxPoolSize to set
     */
    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    /**
     * @return the keepAliveTime
     */
    public long getKeepAliveTime() {
        return keepAliveTime;
    }

    /**
     * @param keepAliveTime the keepAliveTime to set
     */
    public void setKeepAliveTime(long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    /**
     * @return the threadPool
     */
    public ThreadPoolExecutor getThreadPool() {
        return threadPool;
    }

    /**
     * @param threadPool the threadPool to set
     */
    public void setThreadPool(ThreadPoolExecutor threadPool) {
        this.threadPool = threadPool;
    }

}
