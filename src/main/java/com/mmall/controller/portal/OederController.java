package com.mmall.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;

/**
 * @描述：订单模块
 * @作者：Stitch
 * @时间：2019/2/28 15:46
 */
@Controller
@RequestMapping("/order/")
public class OederController {

    private static final Logger logger = LoggerFactory.getLogger(OederController.class);

    @Autowired
    private IOrderService iOrderService;

    /**
     * 下单生成支付宝支付二维码
     *
     * @param session
     * @param orderNo 订单号
     * @param request
     * @return
     */
    @RequestMapping("pay.do")
    @ResponseBody
    public ServerResponse pay(HttpSession session, Long orderNo, HttpServletRequest request) {
        //先判断登陆
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登陆");
        }
        //获取本机upload文件夹的路径
        String path = request.getSession().getServletContext().getRealPath("upload");
        return iOrderService.pay(orderNo, user.getId(), path);
    }

    /**
     * 支付宝回调接口
     *
     * @param request 支付宝的回调请求
     * @return
     */
    @RequestMapping("alipay_callback.do")
    @ResponseBody
    public Object alipayCallback(HttpServletRequest request) {
        Map parameterMap = request.getParameterMap();//支付宝回调时会将所有数据放到request中，所以我们需要自己从中取

        Map<String, String> newHashMap = Maps.newHashMap();//新建一个结果集存放结果
        for (Iterator iterator = parameterMap.keySet().iterator(); iterator.hasNext(); ) {
            //遍历map
            String name = (String) iterator.next();//按顺序获取key
            String[] values = (String[]) parameterMap.get(name);//根据key获取结果，强转为字符串数组
            //遍历该数据
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1 ? valueStr + values[i] : valueStr + values[i] + ",");
            }
            newHashMap.put(name, valueStr);
        }
        logger.info("支付宝回调：sign:{},trade_status:{},参数:{}", parameterMap.get("sign"), parameterMap.get("trade_status"), parameterMap.toString());

        //重要：进行回调验证（验证该回调是否是由支付宝发+起的）
        try {
            boolean result = AlipaySignature.rsaCheckV2(newHashMap, Configs.getPublicKey(), "utf_8", Configs.getSignType());
            if (!result) {//如果回调验证不通过：不是由支付宝发起
                return ServerResponse.createByErrorMessage("非法请求！系统已拒绝");
            }
        } catch (AlipayApiException e) {
            logger.error("支付宝回调异常", e);
        }
        //todo:进行结果判断

        ServerResponse result = iOrderService.callBack(newHashMap);
        if (result.isSuccess()) {
            return Const.AlipayCallBack.RESPONSE_SUCCESS;
        }
        return Const.AlipayCallBack.RESPONSE_FAILED;
    }


    /**
     * 前端轮询查询订单状态
     *
     * @param session
     * @param orderNo
     * @return
     */
    @RequestMapping("query_order_pay_status.do")
    @ResponseBody
    public ServerResponse queryOrderPayStatus(HttpSession session, Long orderNo) {
        //先判断登陆
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登陆");
        }
        ServerResponse<Boolean> booleanServerResponse = iOrderService.queryOrderPayStatus(orderNo, user.getId());
        if (booleanServerResponse.isSuccess()) {
            return ServerResponse.createBySuccess(true);
        }
        return ServerResponse.createBySuccess(false);
    }

    /**
     * 创建订单
     *
     * @param session
     * @param shippingId
     * @return
     */
    @RequestMapping("create.do")
    @ResponseBody
    public ServerResponse create(HttpSession session, Integer shippingId) {
        //先判断登陆
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登陆");
        }
        return iOrderService.createOrder(user.getId(), shippingId);
    }


    /**
     * 取消订单
     *
     * @param session
     * @param orderNo 订单号
     * @return
     */
    @RequestMapping("cancle.do")
    @ResponseBody
    public ServerResponse cancle(HttpSession session, Long orderNo) {
        //先判断登陆
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登陆");
        }
        return iOrderService.cancle(user.getId(), orderNo);
    }

    /**
     * 获取购物车选中的商品信息
     *
     * @param session
     * @return
     */
    @RequestMapping("get_order_cart_product.do")
    @ResponseBody
    public ServerResponse getOrderCartProduct(HttpSession session) {
        //先判断登陆
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登陆");
        }
        return iOrderService.getOrderCartProduct(user.getId());
    }

    /**
     * 获取订单详情
     *
     * @param session
     * @param orderNo 订单号
     * @return
     */
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse detail(HttpSession session, Long orderNo) {
        //先判断登陆
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登陆");
        }
        return iOrderService.getOrderDetail(user.getId(), orderNo);
    }

    /**
     * 查看用户的订单（分页）
     *
     * @param session
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse list(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        //先判断登陆
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登陆");
        }
        return iOrderService.getOrderList(user.getId(), pageNum, pageSize);
    }
}
