package com.trilink.ghbaqi.cainiaoshopping01.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.trilink.ghbaqi.cainiaoshopping01.HomeActivity;
import com.trilink.ghbaqi.cainiaoshopping01.R;
import com.trilink.ghbaqi.cainiaoshopping01.bean.Wares;
import com.trilink.ghbaqi.cainiaoshopping01.share.ShareDialog;
import com.trilink.ghbaqi.cainiaoshopping01.utils.CartManager;
import com.trilink.ghbaqi.cainiaoshopping01.utils.Contants;
import com.trilink.ghbaqi.cainiaoshopping01.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.framework.Platform;

/**
 * Created by ghbaqi on 2017/5/2.
 */

public class WaresDetailActivity extends BaseActivity implements View.OnClickListener {
    public static final String EXTRA_FROM_DETAILACTIVITY = "extra_from_detailactivity";
    private ProgressDialog mProgressDialog;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.webview)
    WebView mWebview;
    private Wares mWares;
    private WebSettings mSettings;
    private WebAppInterface mAppInterface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_waredetail);
        ButterKnife.bind(this);
        mWares = (Wares) getIntent().getSerializableExtra(WaresListActivity.EXTRA_WARES);
        if (mWares == null)
            finish();
        initView();
    }

    private void initView() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("加载中...");
        mProgressDialog.setCancelable(false);

        View child = LayoutInflater.from(this).inflate(R.layout.cartfragment_toolbar_head, null, false);
        TextView tv_title = (TextView) child.findViewById(R.id.tv_title);
        TextView tv_share = (TextView) child.findViewById(R.id.tv_edit);
        mToolbar.addView(child);
        tv_share.setText("分享");
        tv_title.setText("商 品 详 情    ");
        tv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });
        mToolbar.setNavigationIcon(R.drawable.icon_back_32px);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WaresDetailActivity.this.finish();
            }
        });

        initWebView();

    }


    private void initWebView() {
        mSettings = mWebview.getSettings();
        mSettings.setJavaScriptEnabled(true);
        mSettings.setBlockNetworkImage(false);
        mSettings.setAppCacheEnabled(true);
        mWebview.loadUrl(Contants.API.WARES_DETAIL);
        mAppInterface = new WebAppInterface();
        mWebview.addJavascriptInterface(mAppInterface,"appInterface");   // 添加 安卓 与 HTML 交互的接口     此处 接口 名字 要与 HTML 中的 一致 !!!!

        mWebview.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mProgressDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgressDialog.dismiss();
                mAppInterface.showDetail();      // 安卓 调用 HTML
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_title:
//                share();
                break;
        }
    }


    private void share() {
        ToastUtil.showToast(this,"分享功能!!");
        Platform.ShareParams shareParams = new Platform.ShareParams();
        shareParams.setImageUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490962864191&di=d0a0848008ceef6fe5aacdddf4b88328&imgtype=0&src=http%3A%2F%2Fpic.yesky.com%2FuploadImages%2F2016%2F336%2F33%2F69VN0ZT5JG5G.JPG");
        shareParams.setAuthor("author");
        shareParams.setAddress("address");
        shareParams.setComment("comment");
        shareParams.setTitle("title");
        shareParams.setTitleUrl("http://cn.zpmc.com/index.html");
        ShareDialog shareDialog = null;
        if (shareDialog == null) {
            shareDialog = new ShareDialog(this, shareParams, null);
            shareDialog.show();
        }
    }

    /**
     * html 与 安卓 交互
     */
    class WebAppInterface{

        // 安卓 调用 HTML
        @JavascriptInterface
        public  void showDetail(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWebview.loadUrl("javascript:showDetail("+mWares.getId()+")");
                }
            });
        }

        //  点击立即购买 则跳转到购物车模块
        @JavascriptInterface
        public void buy(long id){
            Intent intent = new Intent(WaresDetailActivity.this, HomeActivity.class);
            intent.putExtra(EXTRA_FROM_DETAILACTIVITY,EXTRA_FROM_DETAILACTIVITY);
            startActivity(intent);
            WaresDetailActivity.this.finish();
        }

        @JavascriptInterface
        public void addToCart(long id){
            ToastUtil.showToast(WaresDetailActivity.this,"添加到购物车");
            CartManager.getInstance(WaresDetailActivity.this).put(mWares);
        }
    }
}
