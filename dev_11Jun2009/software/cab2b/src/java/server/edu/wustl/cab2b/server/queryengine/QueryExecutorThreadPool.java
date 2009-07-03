package edu.wustl.cab2b.server.queryengine;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author chandrakant_talele
 */
class QueryExecutorThreadPool extends ThreadPoolExecutor {
    private int localThreadCount = 0;

    private boolean incrementCalled = false;
    public QueryExecutorThreadPool(
            int corePoolSize,
            int maximumPoolSize,
            long keepAliveTime,
            TimeUnit unit,
            BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    /**
     * @see java.util.concurrent.ThreadPoolExecutor#afterExecute(java.lang.Runnable, java.lang.Throwable)
     */
    protected void afterExecute(Runnable r, Throwable t) {
        decrementLocalThreadCount();
    }

    @Override
    public void execute(Runnable command) {
        incrementLocalThreadCount();
        super.execute(command);
    }

    /**
     * @return the isProcessingFinished
     */
    public synchronized boolean isProcessingFinished() {
        return (localThreadCount == 0) && incrementCalled;
    }

    private synchronized void decrementLocalThreadCount() {
        localThreadCount--;
    }

    private synchronized void incrementLocalThreadCount() {
        incrementCalled = true;
        localThreadCount++;
    }
}