package com.elon.base.model;

import com.elon.base.constant.TaskCodeConst;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class WordCountTask extends TaskBase {
    // 单词
    private String word = "";

    // 统计次数
    private long wordNum = 0;

    public WordCountTask() {
        super(UUID.randomUUID().toString(), TaskCodeConst.KAFKA_WORD_COUNT);
    }
}
