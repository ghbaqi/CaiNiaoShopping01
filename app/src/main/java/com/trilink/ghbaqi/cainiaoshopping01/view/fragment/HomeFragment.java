package com.trilink.ghbaqi.cainiaoshopping01.view.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.trilink.ghbaqi.cainiaoshopping01.R;
import com.trilink.ghbaqi.cainiaoshopping01.adapter.HomeCatgoryAdapter;
import com.trilink.ghbaqi.cainiaoshopping01.bean.HomeCampaign;
import com.trilink.ghbaqi.cainiaoshopping01.http.BeanListCallback;
import com.trilink.ghbaqi.cainiaoshopping01.http.OkHttpManager;
import com.trilink.ghbaqi.cainiaoshopping01.utils.Contants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ghbaqi on 2017/4/15.
 */

public class HomeFragment extends BaseFragment {
    @BindView(R.id.toolbar)
    Toolbar      mToolbar;
    @BindView(R.id.recycleview)
    RecyclerView mRecycleview;
    private ProgressDialog     mProgressDialog;
    private HomeCatgoryAdapter mCatgoryAdapter;
    private List<HomeCampaign> mDatas;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        initToolBar();
        initRecycleView();
        return view;

    }


    private void initRecycleView() {
        mDatas = new ArrayList<>();
        mCatgoryAdapter = new HomeCatgoryAdapter(mDatas, getActivity());
        mRecycleview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecycleview.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mRecycleview.setAdapter(mCatgoryAdapter);
        OkHttpManager.getInstance().getBeanList(Contants.API.CAMPAIGN_HOME, HomeCampaign.class, new BeanListCallback<HomeCampaign>() {
            @Override
            public void onPreExecute() {
                mProgressDialog = new ProgressDialog(HomeFragment.this.getActivity());
                mProgressDialog.setTitle("正在加载...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(HomeFragment.this.getActivity(), "onFailure:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
            }

            @Override
            public void onSuccess(List<HomeCampaign> datas) {
                mDatas.clear();
                mDatas.addAll(datas);
//                mDatas = datas;                      // 不能 这样子修改集合 !
                mCatgoryAdapter.notifyDataSetChanged();
                mProgressDialog.dismiss();

            }


        });
    }

    private void initToolBar() {
        mToolbar.addView(LayoutInflater.from(getActivity()).inflate(R.layout.home_toolbar_child, null));

    }
}
