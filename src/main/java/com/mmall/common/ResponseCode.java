package com.mmall.common;

/**
 * @描述：接口响应枚举
 * @作者：Stitch
 * @时间：2019/2/20 14:15
 */
public enum ResponseCode {


    //自定义错误信息
    SUCCESS(0, "SUCCESS"),
    ERROR(1, "ERROR"),
    NEED_LOGIN(10, "NEED_LOGIN"),
    ILLEGAL_ARGUMENT(2, "ILLEGAL_ARGUMENT");


    private final int code;
    private final String msg;

    ResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
