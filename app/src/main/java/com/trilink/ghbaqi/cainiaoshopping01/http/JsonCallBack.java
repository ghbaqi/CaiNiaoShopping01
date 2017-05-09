package com.trilink.ghbaqi.cainiaoshopping01.http;

import com.google.gson.internal.$Gson$Types;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by ghbaqi on 2017/4/20.
 *  泛型 T 可以JavaBean 也可以是 集合泛型
 */

public abstract class JsonCallBack<T> {

    public Type mType;

    static Type getSuperclassTypeParameter(Class<?> subclass)
    {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class)
        {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }


    public JsonCallBack()
    {
        mType = getSuperclassTypeParameter(getClass());
    }
    /**
     * 封装 okhttp 请求二 , 直接返回 Json 对象 .
     *
     * @param
     */
    public abstract void onPreExecute();           // 请求前 做界面的初始化工作  , 比如显示进度条

    public abstract void onFailure(Exception e);

    public abstract void onSuccess(T t);


}
