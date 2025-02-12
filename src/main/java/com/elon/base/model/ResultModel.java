package com.elon.base.model;

import com.elon.base.constant.IErrorCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 通用结果模型
 *
 * @author neo
 * @since 2025-02-12
 */
@Getter
@Setter
@ApiModel(value = "通用结果模型")
public class ResultModel <T> {
    @ApiModelProperty(name = "错误码", example = "")
    private String errorCode = IErrorCode.OPERATE_SUCCESS;

    @ApiModelProperty(name = "错误描述", example = "")
    private String errorMsg = "";

    @ApiModelProperty(name = "数据")
    private T data = null;

    /**
     * 创建操作成功的结果模型
     *
     * @param data 数据
     * @param <T> 参数类型
     * @return 结果模型
     */
    public static <T> ResultModel<T> success(T data) {
        ResultModel<T> resultModel = new ResultModel<>();
        resultModel.setData(data);
        return resultModel;
    }

    /**
     * 创建操作失败的结果模型
     *
     * @param errorMsg 错误信息
     * @param <T> 参数类型
     * @return 结果模型
     */
    public static <T> ResultModel<T> fail(String errorMsg) {
        ResultModel<T> resultModel = new ResultModel<>();
        resultModel.setErrorCode(IErrorCode.OPERATE_FAIL);
        resultModel.setErrorMsg(errorMsg);
        return resultModel;
    }
}
