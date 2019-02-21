package com.mmall.controller.backend;

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
 * @描述：后台的用户管理类
 * @作者：Stitch
 * @时间：2019/2/21 11:22
 */
@Controller
@RequestMapping("/manage/user/")
public class UserManagerController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        ServerResponse<User> userServerResponse = iUserService.login(username, password);
        //登陆成功
        if (userServerResponse.isSuccess()) {
            //判断是否是管理员
            if (userServerResponse.getData().getRole() == Const.Role.ROLE_ADMIN) {
                //保存至session
                session.setAttribute(Const.CURRENT_USER, userServerResponse.getData());
                return userServerResponse;
            } else {
                return ServerResponse.createByErrorMessage("该用户不是管理员");
            }
        }
        return userServerResponse;
    }
}
