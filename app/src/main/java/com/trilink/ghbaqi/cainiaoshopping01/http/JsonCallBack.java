package com.trilink.ghbaqi.cainiaoshopping01.http;

/**
 * Created by ghbaqi on 2017/4/20.
 */

public abstract class JsonCallBack<T> {
    /**
     * 封装 okhttp 请求二 , 直接返回 Json 对象 .
     *
     * @param
     */
    public abstract void onPreExecute();           // 请求前 做界面的初始化工作  , 比如显示进度条

    public abstract void onFailure(Exception e);

    public abstract void onSuccess(T t);


}
