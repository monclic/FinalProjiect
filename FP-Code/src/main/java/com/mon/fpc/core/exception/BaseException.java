package com.mon.fpc.core.exception;


import cn.hutool.core.util.StrUtil;
import com.mon.fpc.core.Resp;
import com.mon.fpc.core.RespKeyInterface;
import lombok.Getter;

/**
 * @author Created by mon
 * @description
 */
@Getter
public class BaseException extends RuntimeException {

    private Integer code;
    private boolean isShowErrorStack = false;

    public BaseException(String message) {
        super(message);
        this.code = Resp.RespEnum.PARAM_ERROR.getCode();
    }

    public BaseException(String message, Object... params) {
        super(StrUtil.format(message, params));
        this.code = Resp.RespEnum.PARAM_ERROR.getCode();
    }

    public BaseException(RespKeyInterface respEnum) {
        super(respEnum.getKeyDescription());
        this.code = respEnum.getCode();
    }

    public BaseException(RespKeyInterface respEnum, String message) {
        super(message);
        this.code = respEnum.getCode();
    }

    public BaseException(RespKeyInterface respEnum, String message, Object... params) {
        super(StrUtil.format(message, params));
        this.code = respEnum.getCode();
    }

    public BaseException(String message, boolean isShowErrorStack) {
        super(message);
        this.code = Resp.RespEnum.PARAM_ERROR.getCode();
        this.isShowErrorStack = isShowErrorStack;
    }

}
