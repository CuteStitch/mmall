package com.mmall.dao;

import com.mmall.pojo.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);

    /**
     * 根据订单号和用户id获取订单中包含的商品信息
     *
     * @param userId  用户id
     * @param orderNo 订单号
     * @return
     */
    List<OrderItem> getByUserIdAndOrderNo(@Param("userId") Integer userId, @Param("orderNo") Long orderNo);
}