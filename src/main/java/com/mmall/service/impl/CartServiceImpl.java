package com.mmall.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CartMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Cart;
import com.mmall.pojo.Product;
import com.mmall.service.ICartService;
import com.mmall.util.BigDecimalUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.CartProductVo;
import com.mmall.vo.CartVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @描述：购物车模块
 * @作者：Stitch
 * @时间：2019/2/25 10:17
 */
@Service("iCartService")
public class CartServiceImpl implements ICartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;


    @Override
    public ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count) {

        if (userId == null || productId == null || count == null) {
            return ServerResponse.createByErrorMessage("参数条件不能为空");
        }
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if (cart == null) {
            //说明当前商品还未添加到购物车
            Cart cartItem = new Cart();
            cartItem.setQuantity(count);
            cartItem.setChecked(Const.Cart.CART_CHECKED);//默认选中状态
            cartItem.setProductId(productId);
            cartItem.setUserId(userId);
            cartMapper.insert(cartItem);
        } else {
            //说明已存在该商品，进行数量增加
            cart.setQuantity(cart.getQuantity() + count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }
        CartVo cartVoLimit = this.getCartVoLimit(userId);
        return ServerResponse.createBySuccess(cartVoLimit);
    }

    private CartVo getCartVoLimit(Integer userId) {

        CartVo cartVo = new CartVo();
        List<Cart> cartList = cartMapper.selectCartByUserId(userId);//查询当该用户购物车中的所有商品
        List<CartProductVo> cartProductVoList = Lists.newArrayList();
        //初始化总价
        BigDecimal cartTotalPrice = new BigDecimal("0");
        //封装返回结果
        if (CollectionUtils.isNotEmpty(cartList)) {
            for (Cart cartItem : cartList) {
                CartProductVo cartProductVo = new CartProductVo();
                cartProductVo.setId(cartItem.getId());
                cartProductVo.setProductId(cartItem.getProductId());
                cartProductVo.setUserId(cartItem.getUserId());

                //根据商品id查询商品信息，封装到返回结果中
                Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
                if (product != null) {
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductSubtitle(product.getSubtitle());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductStock(product.getStock());
                    //判断库存
                    int buyLimiCount = 0;//初始化可购买库存为0
                    if (product.getStock() >= cartItem.getQuantity()) {//如果库存大于下单的数量
                        buyLimiCount = cartItem.getQuantity();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                    } else {//如果库存小于下单数量
                        buyLimiCount = product.getStock();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
                        //购物车更新有效库存数量
                        Cart cart = new Cart();
                        cart.setId(cartItem.getId());
                        cart.setQuantity(buyLimiCount);
                        cartMapper.updateByPrimaryKeySelective(cart);
                    }
                    cartProductVo.setQuantity(buyLimiCount);

                    //计算某样商品总价
                    cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(), cartProductVo.getQuantity()));
                    cartProductVo.setProductChecked(cartItem.getChecked());//是否选中
                }
                if (cartItem.getChecked() == Const.Cart.CART_CHECKED) {
                    //计算购物车中已选择的商品的总价
                    cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(), cartProductVo.getProductTotalPrice().doubleValue());
                }
                cartProductVoList.add(cartProductVo);
            }
        }
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setCartProductVoList(cartProductVoList);
        cartVo.setAllChecked(this.getAllCheckedStatus(userId));//判断是否全选状态
        cartVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));//文件服务器前缀

        return cartVo;
    }

    /**
     * 判断购物车中的商品是否全选
     *
     * @param userId 用户id
     * @return
     */
    private boolean getAllCheckedStatus(Integer userId) {
        if (userId == null) {
            return false;
        }
        return cartMapper.selectCartProductCheckedStatusByUserId(userId) == 0;
    }

    @Override
    public ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count) {
        if (userId == null || productId == null || count == null) {
            return ServerResponse.createByErrorMessage("参数条件不能为空");
        }
        //先查询该用户该商品的购物车
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if (cart != null) {
            cart.setQuantity(count);//将变更的数量保存到购物车中
        }
        cartMapper.updateByPrimaryKeySelective(cart);
        CartVo cartVoLimit = this.getCartVoLimit(userId);//重新计算购物车总价
        return ServerResponse.createBySuccess(cartVoLimit);
    }

    @Override
    public ServerResponse<CartVo> deleteProduct(Integer userId, String productIds) {
        //先解析商品ids：利用guava进行字符串的切割
        List<String> idList = Splitter.on(",").splitToList(productIds);
        if (CollectionUtils.isEmpty(idList)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getMsg());
        }
        //根据userid和商品id删除对应信息
        cartMapper.deleteByUserIdAndProductIds(userId, idList);
        CartVo cartVoLimit = this.getCartVoLimit(userId);
        return ServerResponse.createBySuccess(cartVoLimit);
    }

    @Override
    public ServerResponse<CartVo> list(Integer userId) {
        CartVo cartVoLimit = this.getCartVoLimit(userId);
        return ServerResponse.createBySuccess(cartVoLimit);
    }

    @Override
    public ServerResponse<CartVo> checkedOrUnchecked(Integer userId, Integer checked, Integer productId) {
        cartMapper.updateCheckedOrUnchecked(userId, productId, checked);
        return this.list(userId);
    }

    @Override
    public ServerResponse<Integer> selectCartProductCount(Integer userId) {
        if (userId == null) {
            return ServerResponse.createBySuccess(0);
        }
        return ServerResponse.createBySuccess(cartMapper.selectCartProductCount(userId));
    }
}
