package com.trilink.ghbaqi.cainiaoshopping01.http;

import java.util.List;

/**
 * Created by ghbaqi on 2017/4/20.
 */

public interface BeanListCallback<T> {
    /**
     * 封装 okhttp 请求三 , 直接返回 beanList 集合 .
     *
     * @param
     */
     void onPreExecute();           // 请求前 做界面的初始化工作  , 比如显示进度条

     void onFailure(Exception e);

     void onSuccess(List<T> mDatas);

}
