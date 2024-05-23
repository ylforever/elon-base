package com.elon.base.service.kafka;

import lombok.Getter;

/**
 * 任务处理类。派生出各子类实现具体的处理逻辑
 *
 * @author neo
 * @since 2024-05-14
 */
public abstract class TaskHandler {
    // 任务编码
    @Getter
    private final String taskCode;

    /**
     * 任务处理接口
     *
     * @param taskJson 任务对象JSON串
     */
    public abstract void handle(String taskJson);

    protected TaskHandler(String taskCode) {
        this.taskCode = taskCode;
    }
}
