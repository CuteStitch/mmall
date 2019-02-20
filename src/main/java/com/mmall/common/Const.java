package com.mmall.common;

/**
 * @描述：常量类
 * @作者：Stitch
 * @时间：2019/2/20 15:23
 */
public class Const {

    public static final String CURRENT_USER = "currentUser";
    public static final String EMAIL = "email";
    public static final String USERNAME = "username";

    public interface Role {
        int ROLE_CUSTOMER = 0;//普通用户
        int ROLE_ADMIN = 1;//管理员
    }
}
