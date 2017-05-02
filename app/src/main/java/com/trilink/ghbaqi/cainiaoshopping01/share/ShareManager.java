package com.trilink.ghbaqi.cainiaoshopping01.share;

import cn.sharesdk.framework.PlatformActionListener;

/**
 * Created by ghbaqi on 2017/4/1.
 */

public class ShareManager {
    private ShareManager(){

    }
    private static ShareManager mInstance;
    public static ShareManager getInstance(){
        if (mInstance == null) {
            mInstance = new ShareManager();
        }
        return mInstance;
    }

    public static void share(ShareBean bean, PlatformActionListener listener){
        bean.getPlatform().share(bean.getShareParams());
        bean.getPlatform().setPlatformActionListener(listener);
    }
}
