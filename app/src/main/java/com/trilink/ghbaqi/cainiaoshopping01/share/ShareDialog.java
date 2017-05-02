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
 *   TODO : 加一个分享的接口回调
 */

public class ShareDialog extends Dialog implements View.OnClickListener {

    private RelativeLayout         mBt_share_qq;
    private Context                context;
    private Platform.ShareParams   shareParams;
    private PlatformActionListener mListener;

    public void setListener(PlatformActionListener listener) {
        mListener = listener;
    }

    public ShareDialog(Context context, Platform.ShareParams shareParams, PlatformActionListener listener) {
        super(context, R.style.ShareDialogCustom);
        this.context = context;
        this.shareParams = shareParams;

    }

    public ShareDialog(Context context, Platform.ShareParams shareParams, int themeResId) {
        super(context, themeResId);
        this.context = context;
        this.shareParams = shareParams;
    }

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qq_layout:
                ShareSDK.getPlatform(QQ.NAME).share(shareParams);
                break;
            case R.id.moment_layout:   // 微信朋友圈
                ShareSDK.getPlatform(WechatMoments.NAME).share(shareParams);
                break;
            case R.id.weixin_layout:   // 微信好友
                ShareSDK.getPlatform(Wechat.NAME).share(shareParams);
                break;
            case R.id.qzone_layout:
                ShareSDK.getPlatform(QZone.NAME).share(shareParams);
                break;
        }
//        ShareManager.getInstance().share(bean,mListener);
    }
}
