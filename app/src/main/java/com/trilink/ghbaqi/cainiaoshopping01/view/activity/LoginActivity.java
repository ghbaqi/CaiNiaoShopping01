package com.trilink.ghbaqi.cainiaoshopping01.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.trilink.ghbaqi.cainiaoshopping01.R;
import com.trilink.ghbaqi.cainiaoshopping01.bean.LoginRespMsg;
import com.trilink.ghbaqi.cainiaoshopping01.global.UserManager;
import com.trilink.ghbaqi.cainiaoshopping01.http.BaseCallBack;
import com.trilink.ghbaqi.cainiaoshopping01.http.GsonUtil;
import com.trilink.ghbaqi.cainiaoshopping01.http.OkHttpManager;
import com.trilink.ghbaqi.cainiaoshopping01.utils.Contants;
import com.trilink.ghbaqi.cainiaoshopping01.utils.DESUtil;
import com.trilink.ghbaqi.cainiaoshopping01.utils.ToastUtil;
import com.trilink.ghbaqi.cainiaoshopping01.view.widget.ClearEditText;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ghbaqi on 2017/5/4.
 */

public class LoginActivity extends BaseActivity {
    public static final int RESULT_CODE_LOGIN = 0x35;
    @BindView(R.id.toolbar)
    Toolbar       mToolbar;
    @BindView(R.id.et_number)
    ClearEditText mEtNumber;
    @BindView(R.id.et_pwd)
    ClearEditText mEtPwd;
    @BindView(R.id.bt_login)
    Button        mBtLogin;
    @BindView(R.id.tv_register)
    TextView      mTvRegister;
    @BindView(R.id.tv_forgetsecret)
    TextView      mTvForgetsecret;
    private ProgressDialog mProgressDialog;
    private UserManager mUserManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mUserManager = UserManager.getInstace();
        initView();
    }

    private void initView() {
        mProgressDialog = new ProgressDialog(LoginActivity.this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setTitle("正在登录...");

        mToolbar.setNavigationIcon(R.drawable.icon_back_32px);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.finish();
            }
        });
    }

    @OnClick({R.id.bt_login, R.id.tv_register, R.id.tv_forgetsecret})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                login();
                break;
            case R.id.tv_register:
                ToastUtil.showToast(LoginActivity.this,"注册账号");
                startActivity(new Intent(LoginActivity.this,GetSmsActivity.class));
                break;
            case R.id.tv_forgetsecret:
                ToastUtil.showToast(LoginActivity.this,"忘记密码!");
                break;
        }
    }

    private void login() {
        Map<String, Object> params = new HashMap<>();
        String phoneNum = mEtNumber.getText().toString().trim();
        String pwd = mEtPwd.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNum)||TextUtils.isEmpty(pwd)) {
            ToastUtil.showToast(LoginActivity.this, "用户名和密码不能为空!");
            return;
        }
        params.put("phone",phoneNum);
        params.put("password", DESUtil.encode(Contants.DES_KEY,pwd));
        OkHttpManager.getInstance().commonPost(Contants.API.LOGIN, params, new BaseCallBack() {
            @Override
            public void onPreExecute() {
                mProgressDialog.show();
            }

            @Override
            public void onFailure(Exception e) {
                Logger.d("onFailure :"+e.getMessage());
                ToastUtil.showToast(LoginActivity.this,"登陆失败!");
                mUserManager.clearUser();
                mUserManager.clearToken();
                mProgressDialog.dismiss();
            }

            @Override
            public void onSuccess(String result) {
                mProgressDialog.dismiss();
                LoginRespMsg loginRespMsg = GsonUtil.parseJsonToBean(result, LoginRespMsg.class);
                if (loginRespMsg.getData() == null) {
                    ToastUtil.showToast(LoginActivity.this,"登陆失败");
                } else {
                    mUserManager.setUser(loginRespMsg.getData());
                    mUserManager.setToken(loginRespMsg.getToken());
                    // 登录成功后 跳转到 我的 模块
                    LoginActivity.this.setResult(RESULT_CODE_LOGIN);
                    LoginActivity.this.finish();
                }

            }
        });
    }
}
