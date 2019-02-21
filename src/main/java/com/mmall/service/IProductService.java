package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;

/**
 * @描述：商品模块
 * @作者：Stitch
 * @时间：2019/2/21 15:16
 */
public interface IProductService {

    /**
     * 商品的保存或更新
     *
     * @param product 传入商品对象
     * @return
     */
    ServerResponse savaOrUpdateProduct(Product product);

    /**
     * 商品的上下架
     *
     * @param productId 商品ID
     * @param status    状态
     * @return
     */
    ServerResponse setProductSellStatus(Integer productId, Integer status);
}
