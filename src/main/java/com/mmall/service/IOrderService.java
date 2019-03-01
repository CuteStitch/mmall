package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Order;

import java.util.Map;

/**
 * @描述：订单模块
 * @作者：Stitch
 * @时间：2019/2/28 15:50
 */
public interface IOrderService {

    /**
     * 发起支付
     *
     * @param orderNo 订单号
     * @param userId  用户id
     * @param path    生成的二维码保存路径
     * @return
     */
    ServerResponse pay(Long orderNo, Integer userId, String path);

    /**
     * 回调处理
     *
     * @param params
     * @return
     */
    ServerResponse callBack(Map<String, String> params);

    /**
     * 查询订单的支付状态
     *
     * @param orderNo
     * @param userId
     * @return
     */
    ServerResponse<Boolean> queryOrderPayStatus(Long orderNo, Integer userId);

}
