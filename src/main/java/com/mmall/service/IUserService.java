package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**
 * @描述：
 * @作者：Stitch
 * @时间：2019/2/20 14:55
 */
public interface IUserService {

    /**
     * 登陆校验
     *
     * @param username 用户名
     * @param password 密码
     * @return
     */
    ServerResponse<User> login(String username, String password);

    /**
     * 用户注册
     *
     * @param user
     * @return
     */
    ServerResponse<String> register(User user);

    /**
     * 实时动态校验接口
     *
     * @param str  值
     * @param type 名称
     * @return
     */
    ServerResponse<String> checkValid(String str, String type);

    /**
     * 忘记密码
     *
     * @param username 用户名
     * @return
     */
    ServerResponse<String> selectQuestion(String username);

    /**
     * 忘记密码---校验问题答案
     *
     * @param username 用户名
     * @param question 问题
     * @param answer   问题答案
     * @return
     */
    ServerResponse<String> checkAnswer(String username, String question, String answer);


    /**
     * 重置密码
     *
     * @param username    用户名
     * @param password    密码
     * @param forgetToken 缓存校验值
     * @return
     */
    ServerResponse<String> forgetResetPassword(String username, String password, String forgetToken);

    /**
     * 登陆下重置密码
     * @param user 用户
     * @param password 旧密码
     * @param passwordNew 新密码
     * @return
     */
    ServerResponse<String> resetPassword(User user, String password, String passwordNew);
}
