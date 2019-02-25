package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;

/**
 * @描述：收获地址模块
 * @作者：Stitch
 * @时间：2019/2/25 17:04
 */
public interface IShippingService {

    /**
     * 新增收获地址
     *
     * @param userId   用户id
     * @param shipping 地址信息对象
     * @return
     */
    ServerResponse add(Integer userId, Shipping shipping);

    /**
     * 删除地址
     *
     * @param userId
     * @param shippingId
     * @return
     */
    ServerResponse<String> delete(Integer userId, Integer shippingId);

    /**
     * 更新收货地址
     *
     * @param userId
     * @param shipping
     * @return
     */
    ServerResponse<String> update(Integer userId, Shipping shipping);


    /**
     * 查询收货地址
     *
     * @param userId
     * @param shippingId
     * @return
     */
    ServerResponse<Shipping> select(Integer userId, Integer shippingId);

    /**
     * 查询收货地址（分页）
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo> list(Integer userId, Integer pageNum, Integer pageSize);

}
