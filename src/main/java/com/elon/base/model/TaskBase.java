package com.elon.base.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 异步任务模型基类. 定义模型公共属性
 *
 * @author neo
 * @since 2024-05-14
 */
@Getter
@Setter
public class TaskBase {
    // 任务唯一ID标识, 用UUID
    private String taskId = "";

    // 任务编码(任务类别)
    private String taskCode = "";

    // 创建人
    private String createUser = "";

    // 创建时
    private Date createTime = null;

    // 修改人
    private String updateUser = "";

    // 修改时间
    private Date updateTime = null;

    public TaskBase() {

    }

    public TaskBase(String taskId, String taskCode) {
        this.taskId = taskId;
        this.taskCode = taskCode;
    }
}
