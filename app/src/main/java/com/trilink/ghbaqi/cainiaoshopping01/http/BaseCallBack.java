package com.trilink.ghbaqi.cainiaoshopping01.http;

/**
 * Created by ghbaqi on 2017/4/20.
 */

public interface BaseCallBack {

    /**
     * 封装 okhttp 最基本的的 get 请求 , 返回 String 即可 .
     * @param e
     */
    void onPreExecute();           // 请求前 做界面的初始化工作  , 比如显示进度条
    void onFailure(Exception e);
    void onSuccess(String result);
}
