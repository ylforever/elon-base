package com.elon.base.model;

import com.elon.base.constant.EnumThreadTaskType;
import lombok.Getter;

/**
 * 异步线程任务基类
 *
 * @author neo
 * @since 2025-02-23
 */
@Getter
public class ThreadTaskBase implements Runnable {
    /**
     * 线程UUID
     */
    private final String taskId;

    /**
     * 线程类别
     */
    private final EnumThreadTaskType taskType;

    protected ThreadTaskBase(String taskId, EnumThreadTaskType taskType){
        this.taskId = taskId;
        this.taskType = taskType;
    }

    @Override
    public void run() {

    }
}
