package com.trilink.ghbaqi.cainiaoshopping01.view.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.trilink.ghbaqi.cainiaoshopping01.bean.BasicBean;
import com.trilink.ghbaqi.cainiaoshopping01.http.JsonCallBack;
import com.trilink.ghbaqi.cainiaoshopping01.http.OkHttpManager;
import com.trilink.ghbaqi.cainiaoshopping01.utils.ToastUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by ghbaqi on 2017/5/8.
 * 抽取一个具有 下拉刷新 , 加载更多 , 分页加载功能  的 BasicFragment
 * 可以设置每页显示数量 和 是否支持 加载更多
 */

public abstract class BasicFrament<T extends BasicBean> extends BaseFragment {


    RecyclerView          mRecycleview;
    MaterialRefreshLayout mRefreshlayout;

    private ProgressDialog mProgressDialog;

    private int mCurPage = 1;

    public void setPageSize(int pageSize) {
        mPageSize = pageSize;
    }

    public void setLoadMore(boolean loadMore) {
        isLoadMore = loadMore;
    }

    private              int mPageSize    = 5;    // 默认每页加载 5 条数据
    private static final int REFRESH_LOAD = 0x011;         // 下拉刷新  模式
    private static final int MORE_LOAD    = 0x012;         // 加载更多
    private static       int LOAD_MODE    = REFRESH_LOAD;
    private int     mMaxPage;
    public  Context mActivity;
    private boolean isLoadMore = false;   // 默认不支持加载更多
    private String mUrl;
    private int    mTotalCount;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //        View view = inflater.inflate(mLayoutId, container, false);
        View view = createView(container);
        mUrl = setBaseUrl();
        mRecycleview = setRecycleView();
        mRefreshlayout = setRefreshLayout();
        mActivity = super.mActivity;
        initView();
        getNetData();
        return view;
    }

    protected abstract View createView(ViewGroup container);

    protected abstract MaterialRefreshLayout setRefreshLayout();

    protected abstract RecyclerView setRecycleView();

    protected abstract String setBaseUrl();


    private void getNetData() {  // getBeanData() 根据  JsonCallBack<T> 的泛型可以返回 javabean 或者 集合

        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        Class entityClass = (Class) params[0];  // http://112.124.22.238:8081/course_api/wares/hot?curPage=1&pageSize=2

        OkHttpManager.getInstance().getBeanData(mUrl + "?curPage=" + mCurPage + "&pageSize=" + mPageSize, entityClass, new JsonCallBack<T>() {


            @Override
            public void onPreExecute() {
                mProgressDialog.show();
            }

            @Override
            public void onFailure(Exception e) {
                mProgressDialog.dismiss();
            }

            @Override
            public void onSuccess(T bean) {
                mProgressDialog.dismiss();
                mCurPage = bean.getCurrentPage();
                mMaxPage = bean.getTotalPage();                             //   解决了 !!!!!!!  T extends BasicBean
                mTotalCount = bean.getTotalCount();
                if (LOAD_MODE == REFRESH_LOAD) {                // 下拉刷新
                    mRefreshlayout.finishRefresh();
                    //                        mAdapter.refreshData(bean.getList());
                    //                        mRecycleview.scrollToPosition(0);
                    refreshData(bean);
                } else {                                        // 加载更多
                    mRefreshlayout.finishRefreshLoadMore();

                    //                        mAdapter.loadMoreData(bean.getList());
                    //                        mRecycleview.smoothScrollToPosition(mDatas.size() - 1);
                    if (isLoadMore)
                        loadMoreData(bean);
                }
            }
        });
    }


    protected abstract void loadMoreData(T bean);

    protected abstract void refreshData(T bean);

    private void initView() {

        mRefreshlayout.setLoadMore(isLoadMore);

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle("加载中...");
        mProgressDialog.setCancelable(false);

        mRefreshlayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                LOAD_MODE = REFRESH_LOAD;
                mCurPage = 0;
                getNetData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                if (!isLoadMore)               // 是否支持加载更多 .
                    return;
                LOAD_MODE = MORE_LOAD;
                mCurPage += 1;
                if (mCurPage * mPageSize >= mTotalCount) {
                    mRefreshlayout.finishRefreshLoadMore();
                    //                    ToastUtil.getInstance().showToast(getActivity(),"没有更多数据了!");
                    ToastUtil.showToast(getActivity(), "没有更多数据了!");
                } else {
                    getNetData();
                }
            }
        });

        initRecycleView();
        //        mRecycleview.setLayoutManager(new LinearLayoutManager(getActivity()));
        //        mRecycleview.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        //        mRecycleview.setAdapter(mAdapter);
    }

    protected abstract void initRecycleView();

}
