package com.trilink.ghbaqi.cainiaoshopping01.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.trilink.ghbaqi.cainiaoshopping01.R;
import com.trilink.ghbaqi.cainiaoshopping01.global.UserManager;
import com.trilink.ghbaqi.cainiaoshopping01.view.activity.AddressActivity;
import com.trilink.ghbaqi.cainiaoshopping01.view.activity.BaseActivity;
import com.trilink.ghbaqi.cainiaoshopping01.view.activity.LoginActivity;
import com.trilink.ghbaqi.cainiaoshopping01.view.activity.OrderActivity;
import com.trilink.ghbaqi.cainiaoshopping01.view.activity.TreasureActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ghbaqi on 2017/4/15.
 */

public class MineFragment extends BaseFragment {
    private static final int REQUEST_CODE_LOGIN = 0x66;
    @BindView(R.id.img_head)
    CircleImageView mImgHead;
    @BindView(R.id.tv_login)
    TextView        mTvUserName;
    @BindView(R.id.tv_order)
    TextView        mTvOrder;
    @BindView(R.id.tv_favorite)
    TextView        mTvFavorite;
    @BindView(R.id.tv_address)
    TextView        mTvAddress;
    @BindView(R.id.bt_exit)
    Button          mBtExit;
    @BindView(R.id.tv_tips)
    TextView        mTvTips;
    private UserManager mUserManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.bind(this, view);
        initUserInfo();
        return view;
    }

    private void initUserInfo() {
        mUserManager = UserManager.getInstace();
        if (mUserManager.getUser() == null) {
            mTvUserName.setText("");
            Glide.with(mActivity).load(R.drawable.default_head).into(mImgHead);
            mBtExit.setText(R.string.to_login);
            mTvTips.setVisibility(View.VISIBLE);
        } else {
            mTvUserName.setText(mUserManager.getUser().getUsername());
            Glide.with(mActivity).load(mUserManager.getUser().getLogo_url()).into(mImgHead);
            mBtExit.setText(R.string.logout);
            mTvTips.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.tv_order, R.id.tv_favorite, R.id.tv_address, R.id.bt_exit})
    public void onClick(View view) {
        Class clazz = BaseActivity.class;
        switch (view.getId()) {
            case R.id.tv_order:
                clazz = OrderActivity.class;
                break;
            case R.id.tv_favorite:
                clazz = TreasureActivity.class;
                break;
            case R.id.tv_address:
                clazz = AddressActivity.class;
                break;
            case R.id.bt_exit:     //  登录 或者 退出登录 .
                clazz = BaseActivity.class;
                if (mUserManager.getUser() == null) {      // 登录
                    clazz = LoginActivity.class;
                } else {
                    logOut();    // 退出登录
                }
                break;
        }
        if (clazz != BaseActivity.class) {
            Intent intent = new Intent(mActivity, clazz);
            startActivityForResult(intent, REQUEST_CODE_LOGIN);
        }
    }

    /**
     * 退出登录
     */
    private void logOut() {
        mUserManager.clearUser();
        Glide.with(mActivity).load(R.drawable.default_head).into(mImgHead);
        mTvUserName.setText("");
        mBtExit.setText("登录");
        mTvTips.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //  登录页面 登录成功
        if (requestCode == REQUEST_CODE_LOGIN && resultCode == LoginActivity.RESULT_CODE_LOGIN) {
            Glide.with(mActivity).load(mUserManager.getUser().getLogo_url()).into(mImgHead);
            mTvUserName.setText(mUserManager.getUser().getUsername());
            mBtExit.setText("退出登录");
            mTvTips.setVisibility(View.GONE);
        }
    }
}
