package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @描述：商品模块
 * @作者：Stitch
 * @时间：2019/2/21 15:07
 */
@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IProductService iProductService;

    /**
     * 新增产品
     *
     * @param session
     * @param product
     * @return
     */
    @RequestMapping(value = "product_save.do")
    @ResponseBody
    public ServerResponse productSave(HttpSession session, Product product) {
        //验证登陆
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登陆");
        }
        //验证是否是管理员
        if (user.getRole() == Const.Role.ROLE_CUSTOMER) {
            return ServerResponse.createByErrorMessage("该用户不是管理员");
        }
        return iProductService.savaOrUpdateProduct(product);

    }

    /**
     * 商品上下架
     *
     * @param session
     * @param productId 商品id
     * @param status    上/下架
     * @return
     */
    @RequestMapping(value = "set_product_status.do")
    @ResponseBody
    public ServerResponse setProductSellStatus(HttpSession session, Integer productId, Integer status) {
        //验证登陆
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登陆");
        }
        //验证是否是管理员
        if (user.getRole() == Const.Role.ROLE_CUSTOMER) {
            return ServerResponse.createByErrorMessage("该用户不是管理员");
        }
        return iProductService.setProductSellStatus(productId, status);
    }
}
