package com.elon.base.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 异步任务模型基类. 定义模型公共属性
 *
 * @author neo
 * @since 2024-05-14
 */
@Getter
@Setter
public class TaskBase extends BaseModel {
    // 任务唯一ID标识, 用UUID
    private String taskId = "";

    // 任务编码(任务类别)
    private String taskCode = "";

    public TaskBase() {

    }

    public TaskBase(String taskId, String taskCode) {
        this.taskId = taskId;
        this.taskCode = taskCode;
    }
}
