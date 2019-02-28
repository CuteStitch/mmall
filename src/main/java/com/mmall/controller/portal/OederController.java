package com.mmall.controller.portal;

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

    @RequestMapping("alipay_callback.do")
    @ResponseBody
    public Object alipayCallback(HttpServletRequest request) {
        Map parameterMap = request.getParameterMap();//支付宝回调时会将所有数据放到request中，所以我们需要自己从中取

        Map newHashMap = Maps.newHashMap();//新建一个结果集存放结果
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

        //重要：进行回调验证（验证该回调是否是由支付宝发起的）

        return null;
    }
}
