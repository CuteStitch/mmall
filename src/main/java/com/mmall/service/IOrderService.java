package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Order;
import com.mmall.vo.OrderVo;

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

    /**
     * 创建订单
     *
     * @param userId     用户id
     * @param shippingId 地址id
     * @return
     */
    ServerResponse createOrder(Integer userId, Integer shippingId);

    /**
     * 取消订单
     *
     * @param userId  用户id
     * @param orderNo 订单号
     * @return
     */
    ServerResponse<String> cancle(Integer userId, Long orderNo);

    /**
     * 获取购物车中已勾选的商品
     *
     * @param userId 用户
     * @return
     */
    ServerResponse getOrderCartProduct(Integer userId);

    /**
     * 获取订单详情
     *
     * @param userId  用户id
     * @param orderNo 订单号
     * @return
     */
    ServerResponse<OrderVo> getOrderDetail(Integer userId, Long orderNo);

    /**
     * 查询用户订单列表（分页）
     *
     * @param userId   用户id
     * @param pageNum  第一页
     * @param pageSize 每页显示条数
     * @return
     */
    ServerResponse<PageInfo> getOrderList(Integer userId, Integer pageNum, Integer pageSize);

    /**
     * 后台查询所有订单列表（分页）
     *
     * @param pageNum  当前页
     * @param pageSize 每页显示条数
     * @return
     */
    ServerResponse<PageInfo> manageList(Integer pageNum, Integer pageSize);

    /**
     * 查看订单详情
     *
     * @param orderNo 订单号
     * @return
     */
    ServerResponse<OrderVo> manageDetail(Long orderNo);

    /**
     * 搜索订单详情
     *
     * @param orderNo 订单号
     * @return
     */
    ServerResponse<PageInfo> manageSearch(Long orderNo, Integer pageNum, Integer pageSize);

    /**
     * 发货
     *
     * @param orderNo
     * @return
     */
    ServerResponse<String> sendGoods(Long orderNo);
}
