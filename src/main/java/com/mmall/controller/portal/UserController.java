package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @描述：
 * @作者：Stitch
 * @时间：2019/2/20 14:53
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService iUserService;

    /**
     * 用户登陆校验
     *
     * @param username 用户名
     * @param password 密码
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        ServerResponse<User> response = iUserService.login(username, password);
        if (response.isSuccess()) {//是否成功登陆?存至session
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    /**
     * 退出登陆
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);//移除session
        return ServerResponse.createBySuccess();
    }

    /**
     * 用户注册
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user) {
        ServerResponse<String> response = iUserService.register(user);
        return response;
    }

    /**
     * 用户注册信息实时校验
     *
     * @param str  信息值
     * @param type 信息名称
     * @return
     */
    @RequestMapping(value = "check_valid.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str, String type) {
        return iUserService.checkValid(str, type);
    }

    /**
     * 获取登陆用户信息
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "get_user_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMessage("用户未登陆！无法获取用户信息");
    }

    /**
     * 忘记密码---获取问题
     *
     * @param username
     * @return
     */
    @RequestMapping(value = "forget_get_question.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username) {
        return iUserService.selectQuestion(username);
    }

    /**
     * 忘记密码---校验问题答案
     *
     * @param username
     * @param question
     * @param answer
     * @return
     */
    @RequestMapping(value = "forget_check_answer.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer) {
        return iUserService.checkAnswer(username, question, answer);
    }

    /**
     * 忘记密码---密码重置
     *
     * @param username    用户名
     * @param password    新密码
     * @param forgetToken 缓存验证token
     * @return
     */
    @RequestMapping(value = "forget_reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String username, String password, String forgetToken) {
        return iUserService.forgetResetPassword(username, password, forgetToken);
    }

    /**
     * 密码重置---登陆下修改密码
     *
     * @param session     登陆用户session
     * @param passwordOld 旧密码
     * @param passwordNew 新密码
     * @return
     */
    @RequestMapping(value = "reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpSession session, String passwordOld, String passwordNew) {
        User user = (User) session.getAttribute(Const.USERNAME);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登陆");
        }
        //进行密码重置
        return iUserService.resetPassword(user, passwordOld, passwordNew);
    }
}
