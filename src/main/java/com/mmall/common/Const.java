package com.mmall.common;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * @描述：常量类
 * @作者：Stitch
 * @时间：2019/2/20 15:23
 */
public class Const {

    public static final String CURRENT_USER = "currentUser";
    public static final String EMAIL = "email";
    public static final String USERNAME = "username";

    public interface ProductListOrderBy {
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc", "price_asc");
    }

    public interface Role {
        int ROLE_CUSTOMER = 0;//普通用户
        int ROLE_ADMIN = 1;//管理员
    }

    public interface Cart {
        int CART_CHECKED = 1;//选中
        int CART_UNCHECKED = 0;//未选中

        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";//限制失败
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";//限制成功
    }

    public enum ProductStatusEnum {
        ON_SALE(1, "在线");


        private String value;
        private int code;

        ProductStatusEnum(int code, String value) {
            this.value = value;
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }
    }
}
