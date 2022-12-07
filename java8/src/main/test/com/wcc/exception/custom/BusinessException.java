package com.wcc.exception.custom;

import lombok.Data;

@Data
public class BusinessException extends RuntimeException{
    // code 码 6位数字
    // 前三位代表服务，后单位代码服务的业务异常
    //001 代表demo服务 001 代表服务接口不支持
    //
    private String code;
    // 基础信息
    private String fields;
    // 业务提示
    private String msg;

    public BusinessException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BusinessException(String message, String code, String msg) {
        super(message);
        this.code = code;
        this.msg = msg;
    }

    public BusinessException(String message, Throwable cause, String code, String msg) {
        super(message, cause);
        this.code = code;
        this.msg = msg;
    }

    public BusinessException(Throwable cause, String code, String msg) {
        super(cause);
        this.code = code;
        this.msg = msg;
    }

}
