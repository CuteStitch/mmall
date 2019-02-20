package com.mmall.controller.backend;


import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;


/**
 * 分类控制层
 */
@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private ICategoryService iCategoryService;


    /**
     * 新增分类
     *
     * @param session
     * @param categoryName
     * @param parentId
     * @return
     */
    @RequestMapping(value = "add_category.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> addCategory(HttpSession session, String categoryName, @RequestParam(value = "parentId", defaultValue = "0") int parentId) {

        //判断登录
        User user = (User) session.getAttribute(Const.USERNAME);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        //判断该用户角色是否是管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {// 是
            return iCategoryService.addCategory(categoryName, parentId);
        } else {
            return ServerResponse.createByErrorMessage("当前用户没有权限");
        }

    }


    @RequestMapping(value = "set_category_name.add", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setCategoryName(HttpSession session, Integer categoryId, String categoryName) {
        //判断登录
        User user = (User) session.getAttribute(Const.USERNAME);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        //判断该用户角色是否是管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {// 是
            return iCategoryService.updateCategoryName(categoryId, categoryName);
        } else {
            return ServerResponse.createByErrorMessage("当前用户没有权限");
        }

    }

}
