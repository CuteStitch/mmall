package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @描述：
 * @作者：Stitch
 * @时间：2019/2/20 14:56
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {

        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        //todo 密码校验（MD5加密）
        password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, password);
        if (user == null) {
            return ServerResponse.createByErrorMessage("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登陆成功", user);
    }

    @Override
    public ServerResponse<String> register(User user) {

        //用户名校验
        ServerResponse validResponse = this.checkValid(user.getUsername(), Const.USERNAME);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        //email校验
        validResponse = this.checkValid(user.getEmail(), Const.EMAIL);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        user.setRole(Const.Role.ROLE_CUSTOMER);
        //todo MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return ServerResponse.createBySuccessMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    @Override
    public ServerResponse<String> checkValid(String str, String type) {

        if (org.apache.commons.lang3.StringUtils.isNotBlank(type)) {
            if (Const.EMAIL.equals(type)) {
                int resultCount = userMapper.checkEmail(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("email已存在");
                }
            }
            if (Const.USERNAME.equals(type)) {
                int resultCount = userMapper.checkUsername(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }
            }
        } else {
            return ServerResponse.createByErrorMessage("注册信息不完整");
        }
        return ServerResponse.createBySuccessMessage("校验通过");
    }

    @Override
    public ServerResponse<String> selectQuestion(String username) {
        ServerResponse response = this.checkValid(username, Const.USERNAME);
        if (response.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if (org.apache.commons.lang3.StringUtils.isNotBlank(question)) {
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("找回密码问题为空");
    }

    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {

        int resultcount = userMapper.checkAnswer(username, question, answer);
        if (resultcount > 0) {//说明答案是正确的，生成随机码，将该码存进缓存中，设置过期时间，后面只需获取该值就可进行密码修改
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey("token_" + username, forgetToken);
            return ServerResponse.createBySuccess(forgetToken);//将value返回
        }
        return ServerResponse.createByErrorMessage("答案错误");
    }

    @Override
    public ServerResponse<String> forgetResetPassword(String username, String password, String forgetToken) {
        if (!org.apache.commons.lang3.StringUtils.isNotBlank(forgetToken)) {
            return ServerResponse.createByErrorMessage("参数错误,缓存token为空");
        }
        ServerResponse response = this.checkValid(username, Const.USERNAME);
        if (response.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        //缓存中取值
        String token = TokenCache.getKey("token_" + username);
        if (org.apache.commons.lang3.StringUtils.isBlank(token)) {
            return ServerResponse.createByErrorMessage("token无效或过期");
        }
        if (org.apache.commons.lang3.StringUtils.equals(forgetToken, token)) {
            //校验通过，更新密码
            password = MD5Util.MD5EncodeUtf8(password);
            int resultCount = userMapper.updatePasswordByUsername(username, password);
            if (resultCount > 0) {
                return ServerResponse.createByErrorMessage("密码修改成功");
            }
        } else {
            return ServerResponse.createByErrorMessage("token错误！请重新进行密码修改操作");
        }
        return ServerResponse.createByErrorMessage("密码修改失败");
    }

    @Override
    public ServerResponse<String> resetPassword(User user, String password, String passwordNew) {
        //先进行旧密码验证
        password = MD5Util.MD5EncodeUtf8(password);
        int resultCount = userMapper.checkOldPassword(password, user.getId());
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("旧密码错误");
        }
        //重置密码
        //int rowCount = userMapper.updatePasswordByUsername(user.getUsername(), MD5Util.MD5EncodeUtf8(passwordNew));
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int rowCount = userMapper.updateByPrimaryKeySelective(user);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("密码修改成功");
        }
        return ServerResponse.createByErrorMessage("密码修改失败");
    }



    /*----------分类模块-----------*/

    @Override
    public ServerResponse checkAdminRole(User user) {

        if (user != null && user.getRole().intValue() == Const.Role.ROLE_ADMIN) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }
}
