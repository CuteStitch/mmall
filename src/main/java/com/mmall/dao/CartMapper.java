package com.mmall.dao;

import com.mmall.pojo.Cart;
import com.mmall.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    /**
     * 根据用户id和商品id查询购物从=车
     *
     * @param userId    用户id
     * @param productId 商品id
     * @return
     */
    Cart selectCartByUserIdAndProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);

    /**
     * 通过用户id查询购物车
     *
     * @param userId 用户id
     * @return
     */
    List<Cart> selectCartByUserId(Integer userId);

    /**
     * 根据用户id检查购物车选中状态
     *
     * @param userId
     * @return
     */
    int selectCartProductCheckedStatusByUserId(Integer userId);

    /**
     * 清空购物车
     *
     * @param userId
     * @param productIdList
     */
    int deleteByUserIdAndProductIds(@Param("userId") Integer userId, @Param("productIdList") List<String> productIdList);

    /**
     * 全选或者全不选
     *
     * @param userId
     * @param checked
     */
    int updateCheckedOrUnchecked(@Param("userId") Integer userId, @Param("productId") Integer productId, @Param("checked") Integer checked);

    /**
     * 查询用户的购物车商品数量
     *
     * @param userId
     * @return
     */
    int selectCartProductCount(Integer userId);

    /**
     * 查询购物车中被选中的商品
     *
     * @param userId
     * @return
     */
    List<Cart> selectCheckedCartByUserId(Integer userId);

}