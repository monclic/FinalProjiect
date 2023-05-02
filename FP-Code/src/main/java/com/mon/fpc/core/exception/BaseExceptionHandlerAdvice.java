package com.mon.fpc.core.exception;



import com.mon.fpc.core.BaseController;
import com.mon.fpc.core.Resp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

/**
 * @author Created by mon
 * @description
 */
@Slf4j
@RestControllerAdvice
public class BaseExceptionHandlerAdvice {

    @ExceptionHandler(DuplicateKeyException.class)
    public Resp DuplicateKeyExHandle(DuplicateKeyException e) {
        e.printStackTrace();
        return BaseController.error(Resp.RespEnum.PARAM_ERROR, "重复数据");
    }

    @ExceptionHandler(BaseException.class)
    public Resp BaseExceptionHandle(BaseException e){
//        e.printStackTrace();
        log.error(e.getMessage());
        return BaseController.error(e.getCode().toString(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Resp handleValidException(MethodArgumentNotValidException e){
        //日志记录错误信息
        log.error(Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
        //将错误信息返回给前台
        return BaseController.error(e.getMessage());
    }

//    捕获抛出@Validated对参数的检测不通过的异常
    @ExceptionHandler(BindException.class)
    public Resp validExceptionHandler(BindException exception) {
        return BaseController.error(exception.getAllErrors().get(0).getDefaultMessage());
    }


}
