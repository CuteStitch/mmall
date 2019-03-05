package com.mmall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @描述：后台的订单管理
 * @作者：Stitch
 * @时间：2019/3/5 17:37
 */
@Controller
@RequestMapping("/manage/order/")
public class OrderManageController {

    @Autowired
    private IOrderService iOrderService;

    /**
     * 后台查询所有订单
     *
     * @param session
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("order_list.do")
    @ResponseBody
    public ServerResponse<PageInfo> orderList(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        //验证登陆
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登陆");
        }
        //验证是否是管理员
        if (user.getRole() == Const.Role.ROLE_CUSTOMER) {
            return ServerResponse.createByErrorMessage("该用户不是管理员");
        }
        return iOrderService.manageList(pageNum, pageSize);
    }


    /**
     * 查看订单详情
     *
     * @param session
     * @param orderNo 订单号
     * @return
     */
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse detail(HttpSession session, Long orderNo) {
        //验证登陆
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登陆");
        }
        //验证是否是管理员
        if (user.getRole() == Const.Role.ROLE_CUSTOMER) {
            return ServerResponse.createByErrorMessage("该用户不是管理员");
        }
        return iOrderService.manageDetail(orderNo);
    }

    /**
     * 根据订单号搜索订单详情
     *
     * @param session
     * @param orderNo  订单号
     * @param pageNum  当前页
     * @param pageSize 每页显示的条数
     * @return
     */
    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse<PageInfo> search(HttpSession session, Long orderNo, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        //验证登陆
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登陆");
        }
        //验证是否是管理员
        if (user.getRole() == Const.Role.ROLE_CUSTOMER) {
            return ServerResponse.createByErrorMessage("该用户不是管理员");
        }
        return iOrderService.manageSearch(orderNo, pageNum, pageSize);
    }

    /**
     * 发货
     *
     * @param session
     * @param orderNo 订单id
     * @return
     */
    @RequestMapping("send_goods.do")
    @ResponseBody
    public ServerResponse sendGoods(HttpSession session, Long orderNo) {
        //验证登陆
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登陆");
        }
        //验证是否是管理员
        if (user.getRole() == Const.Role.ROLE_CUSTOMER) {
            return ServerResponse.createByErrorMessage("该用户不是管理员");
        }
        return iOrderService.sendGoods(orderNo);
    }
}
