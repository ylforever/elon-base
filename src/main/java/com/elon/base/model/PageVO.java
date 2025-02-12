package com.elon.base.model;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 分页查询参数
 */
@Getter
@Setter
@ApiModel(value = "分页查询参数")
public class PageVO {
    @ApiModelProperty(value = "查询页号. 从1开始", example = "-1")
    private int pageNo = 1;

    @ApiModelProperty(value = "查询数量. 如:10,20,50", example = "0")
    private int amount = 0;

    @Override
    public String toString(){
        return JSONObject.toJSONString(this);
    }

    public static PageVO create(int pageNo, int amount) {
        PageVO pageVO = new PageVO();
        pageVO.setPageNo(pageNo);
        pageVO.setAmount(amount);

        return pageVO;
    }
}
