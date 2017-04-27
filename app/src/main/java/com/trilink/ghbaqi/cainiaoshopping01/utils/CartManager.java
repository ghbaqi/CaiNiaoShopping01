package com.trilink.ghbaqi.cainiaoshopping01.utils;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.trilink.ghbaqi.cainiaoshopping01.bean.ShoppingCart;
import com.trilink.ghbaqi.cainiaoshopping01.bean.Wares;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ghbaqi on 2017/4/26.
 * <p>
 * 管理购物车数据的 管理类 , 封装了对购物车数据的各种业务操作 .
 */

public class CartManager {
    private static final String CART_JSON = "ghbaqi_cniao_cart";
    private static CartManager                 mInstance;
    private        HashMap<Long, ShoppingCart> mDatas;                   // ! ! ! 用 HashMap 来存储 购物车商品信息
    private        String                      json;
    private static Context                     mContext;

    private CartManager() {
        mDatas = new HashMap<>();
        Logger.init("CartManager");
    }

    public static CartManager getInstance(Context context) {
        mContext = context;
        if (mInstance == null) {
            synchronized (CartManager.class) {
                if (mInstance == null)
                    mInstance = new CartManager();
            }
        }
        return mInstance;
    }

    /**
     * 向购物车中添加商品 ,如果已经存在 则数量加一 , 如果不存在 添加一个
     * 最后都要保存到本地
     *
     * @param
     */
    public void put(Wares ware) {
        ShoppingCart cart = convertData(ware);
        cart.setIsChecked(true);
        if (mDatas.containsKey(cart.getId())) {
            ShoppingCart preCart = mDatas.get(cart.getId());
            preCart.setIsChecked(true);                        //       此处 将商品是否选中属性改为  true  ! ! !!!
            preCart.setCount(preCart.getCount() + 1);
            mDatas.put(cart.getId(), preCart);
        } else {
            cart.setCount(1);
            mDatas.put(cart.getId(), cart);
        }
        saveToLocal();
    }

    /**
     *  将 HashMMP 中的商品数据保存到本地 .
     */
    private void saveToLocal() {
        List<ShoppingCart> carts = hashMapToList();
        Logger.d(carts);
        SpUtils.putString(mContext, CART_JSON, JSONUtil.toJSON(carts));

    }

    private List<ShoppingCart> hashMapToList() {
        ArrayList<ShoppingCart> carts = new ArrayList<>();
        for (Map.Entry<Long, ShoppingCart> entry : mDatas.entrySet()) {
            carts.add(mDatas.get(entry.getKey()));
        }
        return carts;
    }

    public void delete(ShoppingCart cart) {
        mDatas.remove(cart.getId());
        saveToLocal();
    }

    /**
     * 将集合数据保存到本地
     * @param cart
     */
    public void update(ShoppingCart cart) {
        mDatas.put(cart.getId(), cart);
        saveToLocal();
    }

    /**
     * 从本地 sp 获取购物车数据 . 获取到 xml 数据 --> json  ----> 集合数据
     */
    public List<ShoppingCart> getAll() {
        String json = SpUtils.getString(mContext,CART_JSON);
        List<ShoppingCart> carts =null;
        if(json !=null ){
            carts = JSONUtil.fromJson(json,new TypeToken<List<ShoppingCart>>(){}.getType());
        }

        return  carts;
    }

    public ShoppingCart convertData(Wares item) {
        ShoppingCart cart = new ShoppingCart();
        cart.setId(item.getId());
        cart.setDescription(item.getDescription());
        cart.setImgUrl(item.getImgUrl());
        cart.setName(item.getName());
        cart.setPrice(item.getPrice());
        return cart;
    }

    /**
     * 在 activity 或者 fragment onDestory() 时要 释放资源 .
     */
    public void release() {
        mInstance = null;
        mContext = null;
    }

}
