package com.trilink.ghbaqi.cainiaoshopping01.view.fragment;

import android.app.ProgressDialog;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.trilink.ghbaqi.cainiaoshopping01.R;
import com.trilink.ghbaqi.cainiaoshopping01.adapter.BaseAdapter;
import com.trilink.ghbaqi.cainiaoshopping01.adapter.BaseViewHolder;
import com.trilink.ghbaqi.cainiaoshopping01.bean.HotSellBean;
import com.trilink.ghbaqi.cainiaoshopping01.bean.Wares;
import com.trilink.ghbaqi.cainiaoshopping01.http.JsonCallBack;
import com.trilink.ghbaqi.cainiaoshopping01.http.OkHttpManager;
import com.trilink.ghbaqi.cainiaoshopping01.utils.CartManager;
import com.trilink.ghbaqi.cainiaoshopping01.utils.Contants;
import com.trilink.ghbaqi.cainiaoshopping01.utils.ToastUtil;
import com.trilink.ghbaqi.cainiaoshopping01.view.activity.WaresDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.trilink.ghbaqi.cainiaoshopping01.view.activity.WaresListActivity.EXTRA_WARES;

/**
 * Created by ghbaqi on 2017/4/15.
 */

public class HotFragment extends BaseFragment {

    @BindView(R.id.toolbar)
    Toolbar               mToolbar;
    @BindView(R.id.recycleview)
    RecyclerView          mRecycleview;
    @BindView(R.id.refreshlayout)
    MaterialRefreshLayout mRefreshlayout;
    private ProgressDialog mProgressDialog;
    private List<Wares> mDatas = new ArrayList<>();
    //    private HotWaresAdapter mAdapter;
    private BaseAdapter mAdapter;
    private              int mCurPage     = 0;
    private final        int mPageSize    = 10;
    private static final int REFRESH_LOAD = 0x011;         // 下拉刷新  模式
    private static final int MORE_LOAD    = 0x012;         // 加载更多
    private static       int LOAD_MODE    = REFRESH_LOAD;
    private int     mMaxPage;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hot, container, false);
        ButterKnife.bind(this, view);
        initView();
        getNetData();
        return view;
    }

    private void getNetData() {
        OkHttpManager.getInstance().getBeanData(Contants.API.WARES_HOT + "?curPage=" + mCurPage + "&pageSize=" + mPageSize, HotSellBean.class, new JsonCallBack<HotSellBean>() {
            @Override
            public void onPreExecute() {
                mProgressDialog.show();
            }

            @Override
            public void onFailure(Exception e) {
                mProgressDialog.dismiss();
            }

            @Override
            public void onSuccess(HotSellBean hotSellBean) {
                mProgressDialog.dismiss();
                mCurPage = hotSellBean.getCurrentPage();
                mMaxPage = hotSellBean.getTotalPage();
                if (LOAD_MODE == REFRESH_LOAD) {                // 下拉刷新
                    mRefreshlayout.finishRefresh();
                    mAdapter.refreshData(hotSellBean.getList());
                    mRecycleview.scrollToPosition(0);
                    //                    mDatas.clear();
                    //                    mDatas.addAll(hotSellBean.getList());
                    //                    mAdapter.notifyDataSetChanged();
                } else {                                        // 加载更多
                    mRefreshlayout.finishRefreshLoadMore();
                    mAdapter.loadMoreData(hotSellBean.getList());
                    mRecycleview.smoothScrollToPosition(mDatas.size() - 1);
                }
            }
        });
    }

    private void initView() {
        mToolbar.addView(LayoutInflater.from(getActivity()).inflate(R.layout.home_toolbar_child, null));

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
                LOAD_MODE = MORE_LOAD;
                mCurPage += 1;
                if (mCurPage >= mMaxPage) {
                    mRefreshlayout.finishRefreshLoadMore();
                    //                    ToastUtil.getInstance().showToast(getActivity(),"没有更多数据了!");
                    ToastUtil.showToast(getActivity(), "没有更多数据了!");
                } else {
                    getNetData();
                }
            }
        });

        //        mAdapter = new HotWaresAdapter(mDatas);
        mAdapter = new BaseAdapter<Wares>(getActivity(), mDatas) {

            @Override
            protected void bindData(BaseViewHolder holder, final Wares item) {
                TextView tv_title = holder.findTextView(R.id.text_title);
                ImageView iv = holder.findImageView(R.id.drawee_view);
                TextView tv_price = holder.findTextView(R.id.text_price);

                holder.findButton(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtil.showToast(HotFragment.this.getActivity(), "加入购物车!" + item.getId());
                        mActivity = HotFragment.this.getActivity();
                        CartManager.getInstance(mActivity).put(item);
                    }
                });
                tv_title.setText(item.getName());
                tv_price.setText("￥" + item.getPrice());
                Glide.with(HotFragment.this.getActivity()).load(item.getImgUrl()).into(iv);
            }

            @Override
            protected int getItemResId() {
                return R.layout.template_hot_wares;
            }
        };
        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                ToastUtil.showToast(HotFragment.this.getActivity(), "单击 :  " + position);
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
