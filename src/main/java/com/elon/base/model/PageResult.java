package com.elon.base.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页结果模型
 */
@Getter
@Setter
@ApiModel(value = "分页结果模型")
public class PageResult <T> {
    @ApiModelProperty(value = "总数")
    private int totalAmount = 0;

    @ApiModelProperty(value = "分页查询的数据列表")
    private List<T> dataList = new ArrayList<>();

    public static <T> PageResult<T> create(int totalAmount, List<T> dataList) {
        PageResult<T> result = new PageResult<>();
        result.setTotalAmount(totalAmount);
        result.setDataList(dataList);
        return result;
    }
}
