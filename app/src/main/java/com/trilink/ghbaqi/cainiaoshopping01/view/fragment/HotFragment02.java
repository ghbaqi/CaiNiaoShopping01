package com.trilink.ghbaqi.cainiaoshopping01.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.trilink.ghbaqi.cainiaoshopping01.R;
import com.trilink.ghbaqi.cainiaoshopping01.adapter.BaseAdapter;
import com.trilink.ghbaqi.cainiaoshopping01.adapter.HotWaresAdapter02;
import com.trilink.ghbaqi.cainiaoshopping01.bean.HotSellBean02;
import com.trilink.ghbaqi.cainiaoshopping01.bean.Wares;
import com.trilink.ghbaqi.cainiaoshopping01.utils.Contants;
import com.trilink.ghbaqi.cainiaoshopping01.utils.ToastUtil;
import com.trilink.ghbaqi.cainiaoshopping01.view.activity.WaresDetailActivity;

import java.util.ArrayList;
import java.util.List;

import static com.trilink.ghbaqi.cainiaoshopping01.view.activity.WaresListActivity.EXTRA_WARES;

/**
 * Created by ghbaqi on 2017/5/9.
 */

public class HotFragment02 extends BasicFrament<HotSellBean02> {  //  踩坑 : 泛型 的位置放错了 ! ! ! !

    Toolbar               mToolbar;
    RecyclerView          mRecycleview;
    MaterialRefreshLayout mRefreshlayout;
    private BaseAdapter<Wares> mAdapter;
    private List<Wares>        mDatas;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        initData();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initData() {
        mDatas = new ArrayList<>();
    }

    @Override
    protected View createView(ViewGroup container) {

        setPageSize(3);    // 可以从外部设置 每页加载数量 和 是否支持加载更多
        setLoadMore(true);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_hot, container, false);
        initView(view);

        return view;
    }

    private void initView(View view) {
        mRefreshlayout = (MaterialRefreshLayout) view.findViewById(R.id.refreshlayout);
        mRecycleview = (RecyclerView) view.findViewById(R.id.recycleview);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolbar.setTitle("测试 BasicFragment ");
    }


    // 加载更多
    @Override
    protected void loadMoreData(HotSellBean02 bean) {
        mAdapter.loadMoreData(bean.getList());
    }

    // 下拉刷新
    @Override
    protected void refreshData(HotSellBean02 bean) {
        mAdapter.refreshData(bean.getList());

    }

    @Override
    protected MaterialRefreshLayout setRefreshLayout() {
        return mRefreshlayout;
    }

    @Override
    protected RecyclerView setRecycleView() {
        return mRecycleview;
    }

    @Override
    protected String setBaseUrl() {
        return Contants.API.WARES_HOT;
    }


    @Override
    protected void initRecycleView() {
        mAdapter = new HotWaresAdapter02(mActivity, mDatas);
        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                ToastUtil.showToast(HotFragment02.this.getActivity(), "单击 :  " + position);
                Wares wares = mDatas.get(position);
                Intent intent = new Intent(mActivity, WaresDetailActivity.class);
                intent.putExtra(EXTRA_WARES,wares);
                startActivity(intent);
            }
        });
        mRecycleview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycleview.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mRecycleview.setAdapter(mAdapter);
    }
}
