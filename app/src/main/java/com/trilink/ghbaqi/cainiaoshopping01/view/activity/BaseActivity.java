package com.trilink.ghbaqi.cainiaoshopping01.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.orhanobut.logger.Logger;

import static com.cjj.MaterialRefreshLayout.Tag;

/**
 * Created by ghbaqi on 2017/4/15.
 */

public class BaseActivity extends AppCompatActivity {

    public  String TAG = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getSimpleName();
        Logger.init(TAG);
    }
}
