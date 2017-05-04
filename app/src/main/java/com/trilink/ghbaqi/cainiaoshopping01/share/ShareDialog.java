package com.trilink.ghbaqi.cainiaoshopping01.share;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.trilink.ghbaqi.cainiaoshopping01.R;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by ghbaqi on 2017/4/1.
 *
 */

public class ShareDialog extends Dialog implements View.OnClickListener {

    private RelativeLayout         mBt_share_qq;
    private Context                context;
    private Platform.ShareParams   shareParams;
    private   OnShareActionListener mShareActionListener;

    public void setShareActionListener(OnShareActionListener shareActionListener) {
        mShareActionListener = shareActionListener;
    }

    public ShareDialog(Context context, Platform.ShareParams shareParams, OnShareActionListener listener) {
        super(context, R.style.ShareDialogCustom);
        this.context = context;
        this.shareParams = shareParams;
        mShareActionListener = listener;
    }

    public ShareDialog(Context context, Platform.ShareParams shareParams) {
        super(context, R.style.ShareDialogCustom);
        this.context = context;
        this.shareParams = shareParams;
    }

//    private ShareDialog(Context context, Platform.ShareParams shareParams, int themeResId) {
//        super(context, themeResId);
//        this.context = context;
//        this.shareParams = shareParams;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share_layout);
        initView();

    }

    private void initView() {
        /**
         * 通过获取到dialog的window来控制dialog的宽高及位置
         */
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = context.getResources().getDisplayMetrics().widthPixels; //设置宽度
        dialogWindow.setAttributes(lp);

        mBt_share_qq = (RelativeLayout) findViewById(R.id.qq_layout);
        mBt_share_qq.setOnClickListener(this);
        findViewById(R.id.moment_layout).setOnClickListener(this);
        findViewById(R.id.qzone_layout).setOnClickListener(this);
        findViewById(R.id.weixin_layout).setOnClickListener(this);
        findViewById(R.id.tv_cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String platform = null;
        switch (v.getId()) {
            case R.id.qq_layout:
                platform = QQ.NAME;
                ShareSDK.getPlatform(platform).share(shareParams);

                break;
            case R.id.moment_layout:   // 微信朋友圈
                platform = WechatMoments.NAME;
                ShareSDK.getPlatform(platform).share(shareParams);
                break;
            case R.id.weixin_layout:   // 微信好友
                platform = Wechat.NAME;
                ShareSDK.getPlatform(platform).share(shareParams);
                break;
            case R.id.qzone_layout:
                platform = QZone.NAME;
                ShareSDK.getPlatform(platform).share(shareParams);
                break;
            case R.id.tv_cancel:
                ShareDialog.this.dismiss();
                break;
        }

        if (mShareActionListener != null) {
            ShareSDK.getPlatform(platform).setPlatformActionListener(new PlatformActionListener() {
                @Override
                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                    mShareActionListener.onComplete(platform,i,hashMap);
                }

                @Override
                public void onError(Platform platform, int i, Throwable throwable) {
                    mShareActionListener.onError(platform,i,throwable);
                }

                @Override
                public void onCancel(Platform platform, int i) {
                    mShareActionListener.onCancel(platform,i);
                }
            });
        }
    }

   public interface OnShareActionListener{
        void onComplete(Platform platform, int i, HashMap<String, Object> hashMap);
        void onError(Platform platform, int i, Throwable throwable);
        void onCancel(Platform platform, int i);
    }
}
