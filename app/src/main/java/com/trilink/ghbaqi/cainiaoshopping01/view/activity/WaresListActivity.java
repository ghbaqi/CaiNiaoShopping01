package com.trilink.ghbaqi.cainiaoshopping01.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.trilink.ghbaqi.cainiaoshopping01.R;
import com.trilink.ghbaqi.cainiaoshopping01.adapter.HotWaresAdapter;
import com.trilink.ghbaqi.cainiaoshopping01.adapter.decoration.DividerItemDecoration;
import com.trilink.ghbaqi.cainiaoshopping01.bean.Page;
import com.trilink.ghbaqi.cainiaoshopping01.bean.Wares;
import com.trilink.ghbaqi.cainiaoshopping01.http.JsonCallBack;
import com.trilink.ghbaqi.cainiaoshopping01.http.OkHttpManager;
import com.trilink.ghbaqi.cainiaoshopping01.utils.Contants;
import com.trilink.ghbaqi.cainiaoshopping01.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ghbaqi on 2017/4/27.
 * 商品列表
 */

public class WaresListActivity extends BaseActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener, HotWaresAdapter.OnItemClickListener {
    public static final String COMPAIGN_ID = "compaignId";
    public static final String EXTRA_WARES   = "extra_wares";
    @BindView(R.id.toolbar)
    Toolbar   mToolbar;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.tv_total)
    TextView  mTvTotal;

    @BindView(R.id.recycleview)
    RecyclerView          mRecycleview;
    @BindView(R.id.refreshlayout)
    MaterialRefreshLayout mRefreshlayout;
    private ImageView       mIv_list_grid;
    private List<Wares>     mDatas;
    private HotWaresAdapter mWaresAdapter;
    private long            mCompaignId;
    ProgressDialog mProgressDialog;
    private int mOrderBy     = 0;
    private int mCurrentPage = 1;

    private static final int REFRESH_LOAD = 0x011;         // 下拉刷新  模式
    private static final int MORE_LOAD    = 0x012;         // 加载更多
    private static       int LOAD_MODE    = REFRESH_LOAD;
    private int mMaxPage;

    private final int ACTION_LIST    = 0x22;
    private final int ACTION_GRID    = 0x23;
    private       int mCurrentAction = ACTION_LIST;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_wareslist);
        ButterKnife.bind(this);
        mCompaignId = getIntent().getLongExtra(COMPAIGN_ID, 0);
        initView();
        getNetData();
    }

    private void getNetData() {
        String url = Contants.API.WARES_CAMPAIN_LIST + "?campaignId=" + mCompaignId + "&orderBy=" + mOrderBy + "&curPage=" + mCurrentPage + "&pageSize=10";
        OkHttpManager.getInstance().getBeanData(url, Page.class, new JsonCallBack<Page>() {
            @Override
            public void onPreExecute() {
                mProgressDialog.show();
            }

            @Override
            public void onFailure(Exception e) {
                mProgressDialog.dismiss();
            }

            @Override
            public void onSuccess(Page page) {
                mMaxPage = page.getTotalPage();
                mProgressDialog.dismiss();
                if (page.getList() == null || page.getList().size() == 0) {
                    mTvTotal.setText("sorry 服务器没有相关商品 ");
                    ToastUtil.showToast(WaresListActivity.this, "服务器没有相关数据 ! ");
                    mRefreshlayout.finishRefreshLoadMore();
                    return;
                }
                if (LOAD_MODE == REFRESH_LOAD) {   // 下拉刷新
                    mRefreshlayout.finishRefresh();
                    mWaresAdapter.refreshData(page.getList());
                } else {                         // 加载跟多
                    mRefreshlayout.finishRefreshLoadMore();
                    mWaresAdapter.loadMoreData(page.getList());
                }
                mTvTotal.setText("共有 " + mDatas.size() + " 件商品");
            }
        });
    }

    private void initView() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("加载中...");
        mProgressDialog.setCancelable(false);

        mToolbar.setNavigationIcon(R.drawable.icon_back_32px);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WaresListActivity.this.finish();
            }
        });
        View childView = LayoutInflater.from(this).inflate(R.layout.wareslistact_toolbar_child, null, false);
        mIv_list_grid = (ImageView) childView.findViewById(R.id.iv_list_grid);
        mIv_list_grid.setOnClickListener(this);
        mToolbar.addView(childView);

        mTabLayout.addOnTabSelectedListener(this);

        mDatas = new ArrayList<>();
        mRecycleview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecycleview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mRecycleview.setItemAnimator(new DefaultItemAnimator());
        mWaresAdapter = new HotWaresAdapter(mDatas);
        mRecycleview.setAdapter(mWaresAdapter);
        mWaresAdapter.setOnItemClickListener(this);

        mRefreshlayout.setLoadMore(true);
        mRefreshlayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                LOAD_MODE = REFRESH_LOAD;
                mCurrentPage = 1;
                getNetData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                LOAD_MODE = MORE_LOAD;
                mCurrentPage++;
                if (mCurrentPage >= mMaxPage) {
                    ToastUtil.showToast(WaresListActivity.this, "没有跟多数据了!");
                    mRefreshlayout.finishRefreshLoadMore();
                    return;
                }
                getNetData();
            }
        });


    }


    /**
     * 选中不同的标签  更改排序参数重新 加载数据
     */
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case 0:
                mOrderBy = 0;
                getNetData();
                break;
            case 1:
                mOrderBy = 1;
                getNetData();
                break;
            case 2:
                getNetData();
                mOrderBy = 2;
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_list_grid:       // listView 与 GridView 的切换显示
                if (mDatas != null && mDatas.size() > 0) {

                    if (mCurrentAction == ACTION_GRID) {
                        mWaresAdapter.resetItemLayout(R.layout.template_hot_wares);
                        mRecycleview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                        mIv_list_grid.setBackgroundResource(R.drawable.icon_grid_32);
                        mCurrentAction = ACTION_LIST;
                        mRecycleview.setAdapter(mWaresAdapter);
                    } else {
                        mWaresAdapter.resetItemLayout(R.layout.template_grid_wares);
                        mRecycleview.setLayoutManager(new GridLayoutManager(WaresListActivity.this, 2));
                        mIv_list_grid.setBackgroundResource(R.drawable.icon_list_32);
                        mCurrentAction = ACTION_GRID;
                        mRecycleview.setAdapter(mWaresAdapter);
                    }
                }
                break;
        }
    }

    // 条目点击事件 , 点击跳转到商品详情 .
    @Override
    public void onItemClick(View v, int position) {
        Wares wares = mDatas.get(position);
        Intent intent = new Intent(WaresListActivity.this, WaresDetailActivity.class);
        intent.putExtra(EXTRA_WARES,wares);
        startActivity(intent);
    }
}
