package com.mmall.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * @描述：前端接口响应对象（将接口请求的数据封装到该对象内，然后序列号成json对象返回到前端）
 * @作者：Stitch
 * @时间：2019/2/20 14:04
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)//保证json在序列化时如果对象是null,key也会消失
public class ServerResponse<T> implements Serializable {

    private int status;//状态码
    private String msg;//返回信息
    private T data;//返回数据

    private ServerResponse(int status) {
        this.status = status;
    }

    private ServerResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }

    private ServerResponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    private ServerResponse(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 下面的是该类开放的方法
     *
     * @return
     */
    //@jsonignore作用：返回json数据时不包含这个属性或者结果
    @JsonIgnore
    public boolean isSuccess() {
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    /**
     * 只返回一个成功码
     *
     * @param <T>
     * @return
     */
    public static <T> ServerResponse<T> createBySuccess() {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }

    /**
     * 返回成功码+成功信息
     *
     * @param msg
     * @param <T>
     * @return
     */
    public static <T> ServerResponse<T> createBySuccessMessage(String msg) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), msg);
    }

    /**
     * 返回成功码+请求数据
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ServerResponse<T> createBySuccess(T data) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), data);
    }

    /**
     * 返回成功码+成功信息+请求数据
     *
     * @param msg
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ServerResponse<T> createBySuccess(String msg, T data) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), msg, data);
    }

    /**
     * 返回错误码
     *
     * @param <T>
     * @return
     */
    public static <T> ServerResponse<T> createByError() {
        return new ServerResponse<T>(ResponseCode.ERROR.getCode());
    }

    /**
     * 返回错误码+错误信息
     *
     * @param msg
     * @param <T>
     * @return
     */
    public static <T> ServerResponse<T> createByErrorMessage(String msg) {
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(), msg);
    }

    /**
     * 返回错误码+错误信息
     *
     * @param msg
     * @param <T>
     * @return
     */
    public static <T> ServerResponse<T> createByErrorCodeMessage(int erroeCode, String msg) {
        return new ServerResponse<T>(erroeCode, msg);
    }
}
