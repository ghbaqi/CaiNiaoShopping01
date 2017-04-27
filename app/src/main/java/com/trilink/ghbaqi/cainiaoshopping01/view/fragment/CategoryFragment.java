package com.trilink.ghbaqi.cainiaoshopping01.view.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.trilink.ghbaqi.cainiaoshopping01.R;
import com.trilink.ghbaqi.cainiaoshopping01.adapter.BaseAdapter;
import com.trilink.ghbaqi.cainiaoshopping01.adapter.CategoryAdapter;
import com.trilink.ghbaqi.cainiaoshopping01.adapter.WaresAdapter;
import com.trilink.ghbaqi.cainiaoshopping01.adapter.decoration.DividerGridItemDecoration;
import com.trilink.ghbaqi.cainiaoshopping01.bean.Category;
import com.trilink.ghbaqi.cainiaoshopping01.bean.Page;
import com.trilink.ghbaqi.cainiaoshopping01.bean.Wares;
import com.trilink.ghbaqi.cainiaoshopping01.http.BeanListCallback;
import com.trilink.ghbaqi.cainiaoshopping01.http.JsonCallBack;
import com.trilink.ghbaqi.cainiaoshopping01.http.OkHttpManager;
import com.trilink.ghbaqi.cainiaoshopping01.utils.Contants;
import com.trilink.ghbaqi.cainiaoshopping01.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ghbaqi on 2017/4/15.
 */

public class CategoryFragment extends BaseFragment implements BaseAdapter.OnItemClickListener {
    @BindView(R.id.toolbar)
    Toolbar               mToolbar;
    @BindView(R.id.recycleview_left)
    RecyclerView          mRecycleviewLeft;
    @BindView(R.id.recycleview_right)
    RecyclerView          mRecycleviewRight;
    @BindView(R.id.refreshlayout)
    MaterialRefreshLayout mRefreshlayout;
    private List<Category>  mCategoryList;
    private List<Wares>     mWaresList;
    private CategoryAdapter mCategoryAdapter;
    private ProgressDialog  mProgressDialog;
    private WaresAdapter    mWaresAdapter;
    private              long categoryId   = 1;
    private              int  currPage     = 1;    //  !!!!!
    private              int  pageSize     = 3;
    private static final int  REFRESH_LOAD = 0x011;         // 下拉刷新  模式
    private static final int  MORE_LOAD    = 0x012;         // 加载更多
    private static       int  LOAD_MODE    = REFRESH_LOAD;
    private int mMaxPage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category1, container, false);
        ButterKnife.bind(this, view);
        mActivity = getActivity();
        initToolBar();
        initView();
        getCategoryData();
        getWaresData();
        return view;
    }


    private void initView() {
        mProgressDialog = new ProgressDialog(mActivity);
        mProgressDialog.setTitle("加载中...");
        mProgressDialog.setCancelable(false);

        mCategoryList = new ArrayList<>();
        mWaresList = new ArrayList<>();
        mCategoryAdapter = new CategoryAdapter(mActivity, mCategoryList);
        mRecycleviewLeft.setAdapter(mCategoryAdapter);
        mRecycleviewLeft.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));
        mRecycleviewLeft.setItemAnimator(new DefaultItemAnimator());
        mRecycleviewLeft.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        mCategoryAdapter.setOnItemClickListener(this);

        mWaresAdapter = new WaresAdapter(mActivity, mWaresList);
        mRecycleviewRight.setAdapter(mWaresAdapter);
        mRecycleviewRight.setItemAnimator(new DefaultItemAnimator());
        mRecycleviewRight.setLayoutManager(new GridLayoutManager(mActivity, 2));
        mRecycleviewRight.addItemDecoration(new DividerGridItemDecoration(getContext()));

        mRefreshlayout.setLoadMore(true);
        mRefreshlayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                LOAD_MODE = REFRESH_LOAD;
                getWaresData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                LOAD_MODE = MORE_LOAD;
                getWaresData();
            }
        });
    }

    private void initToolBar() {
        View titleView = LayoutInflater.from(mActivity).inflate(R.layout.category_toolbar_title, null);
        mToolbar.addView(titleView);
    }

    public void getCategoryData() {
        OkHttpManager.getInstance().getBeanList(Contants.API.CATEGORY_LIST, Category.class, new BeanListCallback<Category>() {
            @Override
            public void onPreExecute() {
                mProgressDialog.show();
            }

            @Override
            public void onFailure(Exception e) {
                mProgressDialog.dismiss();
            }

            @Override
            public void onSuccess(List<Category> mDatas) {
                mCategoryAdapter.refreshData(mDatas);
                mProgressDialog.dismiss();
            }
        });

    }

    /**
     * 左侧 recycleView 的条目点击事件 , 点击一级菜单 , 刷新二次菜单数据 .
     *
     * @param itemView
     * @param position
     */
    @Override
    public void onItemClick(View itemView, int position) {
        ToastUtil.showToast(mActivity, mCategoryList.get(position).getName());
        Category category = mCategoryList.get(position);
        categoryId = category.getId();
        currPage = 0;
        LOAD_MODE = REFRESH_LOAD;
        getWaresData();
        mRecycleviewRight.scrollToPosition(0);
    }

    /**
     * 第一次进来 , 下拉刷新 , 加载更多 都是不同的加载数据方式 .
     */
    public void getWaresData() {
        if (LOAD_MODE == REFRESH_LOAD) {
            currPage = 0;
        } else {
            currPage += 1;
            if (currPage >= mMaxPage) {
                ToastUtil.showToast(mActivity, "已结没有更多数据了!");
                mRefreshlayout.finishRefreshLoadMore();
                return;
            }

        }
        String url = Contants.API.WARES_LIST + "?categoryId=" + categoryId + "&curPage=" + currPage + "&pageSize=" + pageSize;
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
                currPage = page.getCurrentPage();
                mProgressDialog.dismiss();
                if (LOAD_MODE == REFRESH_LOAD) {
                    mRefreshlayout.finishRefresh();
                    mWaresAdapter.refreshData(page.getList());
                } else {
                    mRefreshlayout.finishRefreshLoadMore();
                    mWaresAdapter.loadMoreData(page.getList());
                    mRecycleviewRight.scrollToPosition(mWaresList.size() - 1);
                }

            }
        });
    }
}
