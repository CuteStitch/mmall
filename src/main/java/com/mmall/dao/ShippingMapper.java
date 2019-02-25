package com.mmall.dao;

import com.mmall.pojo.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);

    /**
     * 根据用户id删除对应得收获地址
     *
     * @param shippingId
     * @param userId
     * @return
     */
    int deleteByShippingIdAndUserId(@Param("shippingId") Integer shippingId, @Param("userId") Integer userId);

    /**
     * 更新收货地址
     *
     * @param record
     * @return
     */
    int updateByShipping(Shipping record);

    /**
     * 根据地址id查询地址详情
     *
     * @param shippingId
     * @param userId
     * @return
     */
    Shipping selectByShippingIdAndUserId(@Param("shippingId") Integer shippingId, @Param("userId") Integer userId);

    /**
     * 查询地址列表
     *
     * @param userId
     * @return
     */
    List<Shipping> selectByUserId(@Param("userId") Integer userId);
}