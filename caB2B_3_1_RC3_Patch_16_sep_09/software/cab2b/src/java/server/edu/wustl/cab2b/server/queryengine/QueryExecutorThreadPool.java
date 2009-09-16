package edu.wustl.cab2b.server.queryengine;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author chandrakant_talele
 */
class QueryExecutorThreadPool extends ThreadPoolExecutor {
    private int localThreadCount = 0;

    /**This variable is introduced to ensure isProcessingFinished returns true 
     * when threadPoolExecutor is terminated. This can happen when 
     * threadPoolExecutor is shutDown due to per query thread limit exceeded or 
     * global thread limit exceeded or per query ICR limit crossed or global 
     * ICR limit crossed. In all these cases threadPoolExecutor should return 
     * isProcessingFinished true so that UI does not wait infinitely 
     * for isProcessingFinished to be set true */
    private boolean isTerminated = false;

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
        super.afterExecute(r,t);
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
    public synchronized boolean noTasksToExecuteOrTerminated() {
        return localThreadCount == 0  || isTerminated;
    }

    private synchronized void decrementLocalThreadCount() {
        localThreadCount--;
    }

    private synchronized void incrementLocalThreadCount() {
        localThreadCount++;
    }

    @Override
    protected void terminated() {
        super.terminated();
        isTerminated = true;
    }
}