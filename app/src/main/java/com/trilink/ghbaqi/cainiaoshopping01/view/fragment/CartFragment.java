package com.trilink.ghbaqi.cainiaoshopping01.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.trilink.ghbaqi.cainiaoshopping01.HomeActivity;
import com.trilink.ghbaqi.cainiaoshopping01.R;
import com.trilink.ghbaqi.cainiaoshopping01.adapter.BaseAdapter;
import com.trilink.ghbaqi.cainiaoshopping01.adapter.CartAdapter;
import com.trilink.ghbaqi.cainiaoshopping01.adapter.decoration.DividerItemDecoration;
import com.trilink.ghbaqi.cainiaoshopping01.bean.ShoppingCart;
import com.trilink.ghbaqi.cainiaoshopping01.utils.CartManager;
import com.trilink.ghbaqi.cainiaoshopping01.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ghbaqi on 2017/4/15.
 */

public class CartFragment extends BaseFragment implements BaseAdapter.OnItemClickListener, View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar        mToolbar;
    @BindView(R.id.recycleview)
    RecyclerView   mRecycleview;
    @BindView(R.id.tv_total)
    TextView       mTvTotal;
    @BindView(R.id.cb_all)
    CheckBox       mCbAll;
    @BindView(R.id.bt_order)
    Button         mBtOrder;
    @BindView(R.id.bt_del)
    Button         mBtDel;
    @BindView(R.id.tv_wander)
    TextView       mTvWander;
    @BindView(R.id.rl_bottom)
    RelativeLayout mRlBottom;
    private TextView           mTv_edit;
    private List<ShoppingCart> mDatas;
    private CartAdapter        mCartAdapter;
    private CartManager        mCartManager;

    private static final int ACTION_EDIT     = 0x011;
    private static final int ACTION_COMPLISH = 0x022;
    private              int mAction         = ACTION_EDIT;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);                                //   ! ! ! !!!
        ButterKnife.bind(this, view);
        initView();
        getData();
        return view;
    }

    /**
     * 通过 CartManager 获取本地购物车数据 展示
     * 更新总价格
     */
    private void getData() {
        mCartManager = CartManager.getInstance(mActivity);
        List<ShoppingCart> list = mCartManager.getAll();

        if (list != null && list.size() > 0) {                       //  踩了一个坑 ! ! !!!
            mRecycleview.setVisibility(View.VISIBLE);
            mTvWander.setVisibility(View.GONE);
            mCartAdapter.refreshData(list);
            mCartAdapter.refreshTotalPrice(mTvTotal);
            mRlBottom.setVisibility(View.VISIBLE);
            mTv_edit.setVisibility(View.VISIBLE);
        } else {
            mRecycleview.setVisibility(View.GONE);
            mTvWander.setVisibility(View.VISIBLE);
            mRlBottom.setVisibility(View.GONE);
            mTv_edit.setVisibility(View.INVISIBLE);
        }

    }

    private void initView() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.cartfragment_toolbar_head, null);
        mTv_edit = (TextView) view.findViewById(R.id.tv_edit);
        mToolbar.addView(view);
        mTv_edit.setOnClickListener(this);
        mCbAll.setOnClickListener(this);
        mBtDel.setOnClickListener(this);
        mBtOrder.setOnClickListener(this);
        mTvWander.setOnClickListener(this);

        mDatas = new ArrayList<>();
        mRecycleview.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        mRecycleview.setItemAnimator(new DefaultItemAnimator());
        mRecycleview.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST));
        mCartAdapter = new CartAdapter(mActivity, mDatas, mTvTotal);
        mRecycleview.setAdapter(mCartAdapter);
        mCartAdapter.setOnItemClickListener(this);
    }

    /**
     * Recycleview 条目点击事件 , 更新集合数据,刷新 Recycleview
     * 同时更改本地数据
     * 更改总价格
     * 更改全选按钮状态
     */
    @Override
    public void onItemClick(View itemView, int position) {
        ShoppingCart cart = mDatas.get(position);
        cart.setIsChecked(!cart.isChecked());
        mDatas.remove(position);
        mDatas.add(position, cart);
        mCartAdapter.notifyItemChanged(position);
        mCartAdapter.refreshTotalPrice(mTvTotal);
        mCartManager.update(cart);      // 修改 商品是否 选中 的属性 并且保存
        refreshCheckBox();
    }

    private void refreshCheckBox() {
        int checkedItems = 0;
        for (ShoppingCart data : mDatas) {
            if (data.isChecked())
                checkedItems++;
        }
        if (checkedItems == mDatas.size()) {
            mCbAll.setChecked(true);
        } else {
            mCbAll.setChecked(false);
        }
    }

    /**
     * 全选按钮 , 根据 CheckBox 选中状态 , 对集合数据进行更改 .
     * 同时保存到本地
     * 并刷新界面 , 修改总价格
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cb_all:
                for (ShoppingCart data : mDatas) {
                    data.setIsChecked(mCbAll.isChecked());
                    mCartManager.update(data);                  // 应该在子线程中做 . . .
                }
                mCartAdapter.notifyDataSetChanged();
                mCartAdapter.refreshTotalPrice(mTvTotal);
                break;

            // 编辑按钮
            case R.id.tv_edit:
                if (mAction == ACTION_COMPLISH) {
                    mTv_edit.setText("编辑");
                    mAction = ACTION_EDIT;
                    mBtOrder.setVisibility(View.VISIBLE);
                    mBtDel.setVisibility(View.GONE);


                } else {                       // 执行编辑 的 逻辑

                    mCbAll.setChecked(false);  // 在编辑状态下 将所有商品改为 未选中状态
                    for (ShoppingCart data : mDatas) {
                        if (data.isChecked()) {
                            data.setIsChecked(false);
                            mCartManager.update(data);
                        }
                    }
                    mCartAdapter.notifyDataSetChanged();
                    mCartAdapter.refreshTotalPrice(mTvTotal);  // 肯定为 0

                    mTv_edit.setText("完成");
                    mAction = ACTION_COMPLISH;
                    mBtDel.setVisibility(View.VISIBLE);
                    mBtOrder.setVisibility(View.GONE);


                }
                break;
            case R.id.bt_del:               // 编辑 删除 操作
                deleteCarts();
                break;
            case R.id.bt_order:            // 结算操作
                toBuy();
                break;
            case R.id.tv_wander:
                HomeActivity homeActivity = (HomeActivity) mActivity;
                homeActivity.showHotFragment();
                break;
        }
    }

    /**
     * 去结算
     */
    private void toBuy() {
        ToastUtil.showToast(mActivity,"去结算");
    }

    /**
     * 在 recycleView 的条目点击事件中 对集合数据的选中状态进行了修改
     * 此处遍历集合数据 , 删除掉 选中 的商品
     */
    private void deleteCarts() {
        Iterator<ShoppingCart> iterator = mDatas.iterator();
        while (iterator.hasNext()) {
            ShoppingCart cart = iterator.next();
            if (cart.isChecked()) {
                iterator.remove();
                mCartManager.delete(cart);
            }
        }
        mCartAdapter.refreshTotalPrice(mTvTotal);
        mCartAdapter.notifyDataSetChanged();
    }

}
