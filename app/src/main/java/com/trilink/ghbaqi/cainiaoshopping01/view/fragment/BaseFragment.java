package com.trilink.ghbaqi.cainiaoshopping01.view.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.orhanobut.logger.Logger;
import com.trilink.ghbaqi.cainiaoshopping01.utils.CartManager;

/**
 * Created by ghbaqi on 2017/4/15.
 */

public class BaseFragment extends Fragment {
    public Context mActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this.getActivity();
        Logger.init(this.getClass().getSimpleName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CartManager.getInstance(BaseFragment.this.getActivity()).release();
    }
}
