package com.trilink.ghbaqi.cainiaoshopping01.share;

import cn.sharesdk.framework.Platform;


/**
 * Created by ghbaqi on 2017/4/1.
 *  Platform p = ShareSDK.getPlatform(Wechat.NAME);
 */

public class ShareBean {
    public Platform.ShareParams mShareParams;
    public Platform             mPlatform;

    public ShareBean() {
    }

    public ShareBean(Platform.ShareParams shareParams, Platform platform) {
        mShareParams = shareParams;
        mPlatform = platform;
    }

    public Platform.ShareParams getShareParams() {
        return mShareParams;
    }

    public void setShareParams(Platform.ShareParams shareParams) {
        mShareParams = shareParams;
    }

    public Platform getPlatform() {
        return mPlatform;
    }

    public void setPlatform(Platform platform) {
        mPlatform = platform;
    }
}
