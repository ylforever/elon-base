package com.elon.base.service.thread;

import com.elon.base.model.ThreadTaskBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池工具类。
 *
 * @author elon
 * @since 2021/11/6
 */
public class ThreadPoolUtils {
    private static final Logger LOGGER = LogManager.getLogger(ThreadPoolUtils.class);

    private int maximumPoolSize;

    private ThreadPoolExecutor poolExecutor = null;

    private ThreadTaskLinkedBlockingDeque<Runnable> queue = new ThreadTaskLinkedBlockingDeque<>();

    public ThreadPoolUtils(int corePoolSize, int maximumPoolSize){
        this.maximumPoolSize = maximumPoolSize;
        poolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 10, TimeUnit.SECONDS, queue);

        LOGGER.info("[ThreadPoolUtils]Init thread pool success. corePoolSize:{}|maximumPoolSize:{}", corePoolSize,
                maximumPoolSize);
    }

    public void executeTask(ThreadTaskBase task){
        int activeThreadNum = poolExecutor.getActiveCount();
        LOGGER.info("[ThreadPoolUtils]Current task id:{}|task type:{}|Number of active threads:{}|Task queue size:{}",
                task.getTaskId(), task.getTaskType(), activeThreadNum, queue.size());
        poolExecutor.execute(task);
    }

    /**
     * 自定义线程任务阻塞队列. 在活跃线程数小于最大支持线程数的情况下，新任务不放到队列从而激发线程池创建新线程及时处理.
     * 解决使用LinkedBlockingDeque无限队列，线程池只有核心线程在处理。maximumPoolSize未启作用的问题。
     *
     * @author elon
     * @since 2021/11/6
     */
    private class ThreadTaskLinkedBlockingDeque<E> extends LinkedBlockingDeque<E> {
        @Override
        public boolean offer(E e) {
            int activeThreadNum = poolExecutor.getActiveCount();
            if (activeThreadNum < maximumPoolSize) {
                return false;
            }

            return offerLast(e);
        }
    }
}

