package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.vo.CartVo;

/**
 * @描述：购物车模块
 * @作者：Stitch
 * @时间：2019/2/25 10:15
 */
public interface ICartService {

    /**
     * 添加商品
     *
     * @param userId    用户id
     * @param productId 商品id
     * @param count     数量
     * @return
     */
    ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count);

    /**
     * 更新购物车商品信息
     *
     * @param userId
     * @param productId
     * @param count
     * @return
     */
    ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count);

    /**
     * 删除购物车中的商品
     *
     * @param userId     用户id
     * @param productIds 待删除的商品id
     * @return
     */
    ServerResponse<CartVo> deleteProduct(Integer userId, String productIds);

    /**
     * 查看购物车
     *
     * @param userId
     * @return
     */
    ServerResponse<CartVo> list(Integer userId);

    /**
     * 全选或者全不选
     *
     * @param userId
     * @param checked
     * @param productId
     * @return
     */
    ServerResponse<CartVo> checkedOrUnchecked(Integer userId, Integer checked, Integer productId);

    /**
     * 查询用户购物车商品总数量
     *
     * @param userId
     * @return
     */
    ServerResponse<Integer> selectCartProductCount(Integer userId);
}
