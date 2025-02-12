package com.elon.base.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 基础模型. 定义模型公共的属性
 *
 * @author neo
 * @since 2025-02-12
 */
@Getter
@Setter
@ApiModel(value = "基础模型")
public class BaseModel {
    @ApiModelProperty(value = "创建人", example = "neo")
    private String createUser = "";

    @ApiModelProperty(value = "创建时间")
    private Date createTime = null;

    @ApiModelProperty(value = "修改人", example = "neo")
    private String updateUser = "";

    @ApiModelProperty(value = "修改时间")
    private Date updateTime = null;
}
