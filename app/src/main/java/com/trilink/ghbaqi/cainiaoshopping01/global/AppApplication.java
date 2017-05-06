package com.trilink.ghbaqi.cainiaoshopping01.global;

import android.app.Application;

import cn.sharesdk.framework.ShareSDK;
import cn.smssdk.SMSSDK;

/**
 * Created by ghbaqi on 2017/5/2.
 */

public class AppApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ShareSDK.initSDK(this,"1d7d5880ee5f7");
        SMSSDK.initSDK(this, "1d9234283ae20", "3f12f0eb39e719f6e1444cf2ea8dbda0");
    }
}
