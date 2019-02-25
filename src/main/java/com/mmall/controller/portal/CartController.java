package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICartService;
import com.mmall.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @描述：购物车模块
 * @作者：Stitch
 * @时间：2019/2/25 10:12
 */
@Controller
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    private ICartService iCartService;

    /**
     * 添加购物车
     *
     * @param session
     * @param count
     * @param productId
     * @return
     */
    @RequestMapping(value = "add.do")
    @ResponseBody
    public ServerResponse<CartVo> add(HttpSession session, Integer count, Integer productId) {
        //判断登陆
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登陆");
        }
        return iCartService.add(user.getId(), productId, count);

    }

    /**
     * 更新购物车
     *
     * @param session
     * @param count
     * @param productId
     * @return
     */
    @RequestMapping(value = "update.do")
    @ResponseBody
    public ServerResponse<CartVo> update(HttpSession session, Integer count, Integer productId) {
        //判断登陆
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登陆");
        }
        return iCartService.update(user.getId(), productId, count);

    }

    /**
     * 删除购物车
     *
     * @param session
     * @param productIds
     * @return
     */
    @RequestMapping(value = "delete.do")
    @ResponseBody
    public ServerResponse<CartVo> delete(HttpSession session, String productIds) {
        //判断登陆
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登陆");
        }
        return iCartService.deleteProduct(user.getId(), productIds);

    }

    /**
     * 查看购物车
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "list.do")
    @ResponseBody
    public ServerResponse<CartVo> list(HttpSession session) {
        //判断登陆
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登陆");
        }
        return iCartService.list(user.getId());

    }

    /**
     * 全选操作
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "select_all.do")
    @ResponseBody
    public ServerResponse<CartVo> selectAll(HttpSession session) {
        //判断登陆
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登陆");
        }
        return iCartService.checkedOrUnchecked(user.getId(), Const.Cart.CART_CHECKED, null);
    }

    /**
     * 全不选操作
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "un_select_all.do")
    @ResponseBody
    public ServerResponse<CartVo> unSelectAll(HttpSession session) {
        //判断登陆
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登陆");
        }
        return iCartService.checkedOrUnchecked(user.getId(), Const.Cart.CART_UNCHECKED, null);
    }

    /**
     * 单选
     *
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping(value = "select_one.do")
    @ResponseBody
    public ServerResponse<CartVo> selectOne(HttpSession session, Integer productId) {
        //判断登陆
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登陆");
        }
        return iCartService.checkedOrUnchecked(user.getId(), Const.Cart.CART_CHECKED, productId);
    }

    /**
     * 单不选
     *
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping(value = "un_select_one.do")
    @ResponseBody
    public ServerResponse<CartVo> unSelectOne(HttpSession session, Integer productId) {
        //判断登陆
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登陆");
        }
        return iCartService.checkedOrUnchecked(user.getId(), Const.Cart.CART_UNCHECKED, productId);
    }

    /**
     * 查询某个用户的购物车商品数量
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "select_count.do")
    @ResponseBody
    public ServerResponse<Integer> selectCount(HttpSession session) {
        //判断登陆
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登陆");
        }
        return iCartService.selectCartProductCount(user.getId());
    }
}
