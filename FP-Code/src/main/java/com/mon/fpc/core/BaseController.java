package com.mon.fpc.core;

public class BaseController {

    public static  Resp success() {
        Resp resp=new Resp();
        resp.setResult(true);
        return resp;
    }
    public static  Resp success(String errorMsg) {
        Resp resp=new Resp();
        resp.setResult(true);
        resp.setErrorMsg(errorMsg);
        return resp;
    }

    public static  Resp success(Resp resp) {
        resp.setResult(true);
        return resp;
    }

    public static Resp error(Resp.RespEnum status) {
        Resp resp=new Resp();
        resp.setResult(false);
        resp.setErrorCode(status.getCode().toString());
        resp.setErrorMsg(status.getKeyDescription());
        return resp;
    }

    public static Resp error(String errorMsg) {
        Resp resp=new Resp();
        resp.setResult(false);
        resp.setErrorCode(Resp.RespEnum.INNER_ERROR.getCode().toString());
        resp.setErrorMsg(errorMsg);
        return resp;
    }

    public static Resp error(Resp.RespEnum status,String errorMsg) {
        Resp resp=new Resp();
        resp.setResult(false);
        resp.setErrorCode(status.getCode().toString());
        resp.setErrorMsg(errorMsg);
        return resp;
    }

    public static Resp error(String errCode,String errorMsg) {
        Resp resp=new Resp();
        resp.setResult(false);
        resp.setErrorCode(errCode);
        resp.setErrorMsg(errorMsg);
        return resp;
    }

//    public static RespKeyEnum getStatusByCode(Integer code) {
//        for (RespKeyEnum status : RespKeyEnum.values()) {
//            if (code.equals(status.getCode())) {
//                return status;
//            }
//        }
//        return RespKeyEnum.INNER_ERROR;
//    }
}
