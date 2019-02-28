package com.mmall.service;

import com.mmall.common.ServerResponse;

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

}
