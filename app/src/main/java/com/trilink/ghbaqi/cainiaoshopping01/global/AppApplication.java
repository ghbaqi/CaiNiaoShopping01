package com.trilink.ghbaqi.cainiaoshopping01.global;

import android.app.Application;

import cn.sharesdk.framework.ShareSDK;

/**
 * Created by ghbaqi on 2017/5/2.
 */

public class AppApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ShareSDK.initSDK(this,"1d7d5880ee5f7");
    }
}
