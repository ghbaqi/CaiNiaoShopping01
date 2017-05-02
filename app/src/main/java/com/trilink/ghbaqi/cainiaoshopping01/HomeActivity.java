package com.trilink.ghbaqi.cainiaoshopping01;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.trilink.ghbaqi.cainiaoshopping01.view.activity.BaseActivity;
import com.trilink.ghbaqi.cainiaoshopping01.view.activity.WaresDetailActivity;
import com.trilink.ghbaqi.cainiaoshopping01.view.fragment.CartFragment;
import com.trilink.ghbaqi.cainiaoshopping01.view.fragment.CategoryFragment;
import com.trilink.ghbaqi.cainiaoshopping01.view.fragment.HomeFragment;
import com.trilink.ghbaqi.cainiaoshopping01.view.fragment.HotFragment;
import com.trilink.ghbaqi.cainiaoshopping01.view.fragment.MineFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity {


    @BindView(R.id.fl_content)
    FrameLayout     mFlContent;
    @BindView(android.R.id.tabcontent)
    FrameLayout     mTabcontent;
    @BindView(android.R.id.tabhost)
    FragmentTabHost mTabhost;
    private String[] mTabTitle = new String[]{"首页","热卖","分类","购物车","我的"};
    private int[] mTabIcon = new int[]{R.drawable.selector_tab_home,R.drawable.selector_tab_hot,R.drawable.selector_tab_category,R.drawable.selector_tab_cart,R.drawable.selector_tab_mine};
    private Class[] mTabclasses = new Class[]{HomeFragment.class, HotFragment.class, CategoryFragment.class,CartFragment.class, MineFragment.class};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        initTabHost();
    }

    //  Activity 启动模式 的应用
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        // 从 商品详情 点击立即购买 则 跳转到购物车模块 .
        String extraFromDetailAct = getIntent().getStringExtra(WaresDetailActivity.EXTRA_FROM_DETAILACTIVITY);
        if (extraFromDetailAct != null && extraFromDetailAct.equals(WaresDetailActivity.EXTRA_FROM_DETAILACTIVITY)) {
            mTabhost.setCurrentTab(3);
        }
    }

    private void initTabHost() {
        mTabhost.setup(this,getSupportFragmentManager(),R.id.fl_content);
        addTabs();
    }

    private void addTabs() {
        for (int i = 0; i < 5; i++) {
            View indicatorView = View.inflate(this, R.layout.tab_view,null);
            ImageView iv = (ImageView) indicatorView.findViewById(R.id.iv_icon);
            iv.setBackgroundResource(mTabIcon[i]);
            TextView tv = (TextView) indicatorView.findViewById(R.id.tv_title);
            tv.setText(mTabTitle[i]);
            TabHost.TabSpec homeTab = mTabhost.newTabSpec(mTabTitle[i]).setIndicator(indicatorView);
            mTabhost.addTab(homeTab, mTabclasses[i],null);
        }

    }

    public void showHotFragment() {
        mTabhost.setCurrentTab(1);
    }
}
