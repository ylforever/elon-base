package com.elon.base.model;

import com.elon.base.constant.TaskCodeConst;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 生成BI报告的任务模型。不同的任务可以根据需要定义不同的模型
 *
 * @author neo
 * @since 2024-05-14
 */
@Getter
@Setter
public class BIReportTask extends TaskBase {
    // 存放报告的路径
    private String path;

    // 参数
    private Map<String, String> paramMap = new HashMap<>();

    public BIReportTask() {
        super(UUID.randomUUID().toString(), TaskCodeConst.KAFKA_REPORT_EXPORT_BI_REPORT);
    }
}
