package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailVo;

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

    /**
     * 根据商品id获取商品信息
     *
     * @param productId
     * @return
     */
    ServerResponse<ProductDetailVo> getDetail(Integer productId);

    /**
     * 获取多个商品信息+分页
     *
     * @param pageNum  页码
     * @param pageSize 每页显示数量
     * @return
     */
    ServerResponse<PageInfo> getDetailList(int pageNum, int pageSize);

    /**
     * 商品搜索
     *
     * @param productName 名称搜索
     * @param productId   id
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize);


    /**
     * 客户端---获取商品详细
     *
     * @param productId
     * @return
     */
    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

    /**
     * 客户端---获取商品列表（分页显示）
     *
     * @param keyword
     * @param categoryId
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy);
}
