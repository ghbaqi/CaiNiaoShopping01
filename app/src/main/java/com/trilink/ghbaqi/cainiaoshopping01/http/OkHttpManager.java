package com.trilink.ghbaqi.cainiaoshopping01.http;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by ghbaqi on 2017/4/20.
 */

public class OkHttpManager {

    private static OkHttpManager mInstance;
    private        OkHttpClient  mOkHttpClient;
    private        Handler       mHandler;


    private OkHttpManager() {
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS)
                .build();
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static OkHttpManager getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpManager.class) {
                mInstance = new OkHttpManager();
            }

        }
        return mInstance;
    }


    public void commonGet(String url, final BaseCallBack callBack) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        callBack.onPreExecute();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onFailure(e);
                    }
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                final String result = response.body().string();
                if (response.isSuccessful()) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onSuccess(result);
                        }
                    });
                } else {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onFailure(new HttpException("onResponse is failed"));
                        }
                    });
                }
            }
        });
    }


    public void commonGet(String url, HashMap<String, Object> params, final BaseCallBack callBack) {
        commonGet(buildUrl(url, params), callBack);
    }

    /**
     * 封装基本的 post 请求 , post 请求将参数封装到 FormBody 中 .
     *
     * @param url
     * @param params
     * @param callBack
     */
    public void commonPost(String url, Map<String, Object> params, final BaseCallBack callBack) {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue() + "");
            }
        }
        FormBody body = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        callBack.onPreExecute();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onFailure(e);
                    }
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                final String result = response.body().string();
                if (response.isSuccessful()) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onSuccess(result);
                        }
                    });
                } else {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onFailure(new HttpException("onResponse is failed ! "));
                            Log.d("CreateOrderActivity", "result:" + result);
                        }
                    });
                }
            }
        });
    }

    /**
     * get 请求
     */
    public <T> void getBeanData(String url, final Class<T> clazz, final JsonCallBack<T> callBack) {


        Request request = new Request.Builder()
                .url(url)
                .build();
        callBack.onPreExecute();
        mOkHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(okhttp3.Call call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onFailure(e);
                    }
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                final String result = response.body().string();
                if (response.isSuccessful()) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {                                //  坑的贼惨 !!!!
                            if (clazz == null) {    // 集合数据
                                T t = new Gson().fromJson(result, callBack.mType);
                                callBack.onSuccess(t);
                            } else {                // javabean 对象
                                T t = GsonUtil.parseJsonToBean(result, clazz);
                                callBack.onSuccess(t);
                            }
                        }
                    });
                } else {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onFailure(new HttpException("onResponse is failed"));
                        }
                    });
                }
            }

        });
    }

    public <T> void getBeanList(String url, final Class<T> clazz, final BeanListCallback<T> callBack) {

        Request request = new Request.Builder()
                .url(url)
                .build();
        callBack.onPreExecute();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onFailure(e);
                    }
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                final String result = response.body().string();
                if (response.isSuccessful()) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<T> mDatas = null;
                            ArrayList<T> list = GsonUtil.parseJsonToList2(result, clazz);
                            callBack.onSuccess(list);
                        }
                    });
                } else {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onFailure(new HttpException("onResponse is failed"));
                        }
                    });
                }
            }

        });

    }

    private String buildUrl(String url, HashMap<String, Object> params) {
        String newUrl = null;
        if (params == null || params.size() == 0)
            return url;
        newUrl = url + "?";
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            newUrl = newUrl + entry.getKey() + "=" + entry.getValue() + "&";
        }
        newUrl = newUrl.substring(0, newUrl.length() - 1);
        return newUrl;
    }

}
