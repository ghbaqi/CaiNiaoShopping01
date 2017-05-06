package com.trilink.ghbaqi.cainiaoshopping01.view.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

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
import com.trilink.ghbaqi.cainiaoshopping01.view.widget.CountTimerView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;

import static com.trilink.ghbaqi.cainiaoshopping01.view.activity.GetSmsActivity.EXTRA_COUNTRY_CODE;
import static com.trilink.ghbaqi.cainiaoshopping01.view.activity.GetSmsActivity.EXTRA_PHONE_NUM;
import static com.trilink.ghbaqi.cainiaoshopping01.view.activity.GetSmsActivity.EXTRA_PWD;
import static com.trilink.ghbaqi.cainiaoshopping01.view.activity.GetSmsActivity.SMS_RESPONSE_ERROR;
import static com.trilink.ghbaqi.cainiaoshopping01.view.activity.GetSmsActivity.SUBMIT_VERIFICATION_CODE;

/**
 * Created by ghbaqi on 2017/5/6.
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "RegisterActivity";
    private Toolbar       mToolBar;
    private ClearEditText mEt_code;
    private Button        mBt_resend;
    private Button        mBt_register;
    private TextView      mTv_tips;
    private String        mPhoneNum;
    private String        mPwd;
    private String        mCountryCode;
    private SmsListener   mSmsListener;
    private Handler       mHandler;
    private ProgressDialog mProgressDialog;
    private UserManager mUserManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        mUserManager = UserManager.getInstace();
        initView();
        initSms();
    }

    private void initSms() {
        mSmsListener = new SmsListener();
        SMSSDK.registerEventHandler(mSmsListener);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case GetSmsActivity.GET_VERIFICATION_CODE:    // 获取验证码成功
                        Log.d(TAG, "获取验证码成功");
                        break;
                    case SUBMIT_VERIFICATION_CODE: // 提交验证码 验证成功
                        ToastUtil.showToast(RegisterActivity.this, "提交验证码验证成功,执行请求注册!!");
                        Log.d(TAG, "提交验证码验证成功!!!");
                        registerAccount();
                        break;
                    case SMS_RESPONSE_ERROR:             // 失败
                        ToastUtil.showToast(RegisterActivity.this, msg.obj.toString());
                        Log.d(TAG, "验证码失败:" + msg.obj);
                        break;
                }
            }
        };
    }

    private void registerAccount() {
        Map<String, String> params = new HashMap<>();
        params.put("phone",mPhoneNum);
        params.put("password", DESUtil.encode(Contants.DES_KEY, mPwd));
        OkHttpManager.getInstance().commonPost(Contants.API.REG, params, new BaseCallBack() {
            @Override
            public void onPreExecute() {
                mProgressDialog.show();
            }

            @Override
            public void onFailure(Exception e) {
                mProgressDialog.dismiss();
                ToastUtil.showToast(RegisterActivity.this,e.getMessage());
            }

            @Override
            public void onSuccess(String result) {
                mProgressDialog.dismiss();
                LoginRespMsg loginRespMsg = GsonUtil.parseJsonToBean(result, LoginRespMsg.class);
                if (loginRespMsg.getData() == null) {
                    ToastUtil.showToast(RegisterActivity.this,"注册失败");
                } else {                      // 注册成功 !
                    ToastUtil.showToast(RegisterActivity.this,"注册成功!!!!!");
                    mUserManager.clearUser();
                    mUserManager.clearToken();
                    RegisterActivity.this.finish();
                }
            }
        });
    }

    private void initView() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setTitle("正在注册...");

        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mEt_code = (ClearEditText) findViewById(R.id.et_code);
        mBt_resend = (Button) findViewById(R.id.bt_resend);
        mBt_register = (Button) findViewById(R.id.bt_register);
        mTv_tips = (TextView) findViewById(R.id.tv_tips);

        mBt_register.setOnClickListener(this);
        mBt_resend.setOnClickListener(this);

        mPhoneNum = getIntent().getStringExtra(EXTRA_PHONE_NUM);
        mPwd = getIntent().getStringExtra(EXTRA_PWD);
        mCountryCode = getIntent().getStringExtra(EXTRA_COUNTRY_CODE);
        String formatedPhone = mCountryCode + " " + splitPhoneNum(mPhoneNum);
        String text = getString(R.string.smssdk_send_mobile_detail) + formatedPhone;
        mTv_tips.setText(Html.fromHtml(text));
        CountTimerView timerView = new CountTimerView(mBt_resend);
        timerView.start();
    }

    /**
     * 分割电话号码
     */
    private String splitPhoneNum(String phone) {
        StringBuilder builder = new StringBuilder(phone);
        builder.reverse();
        for (int i = 4, len = builder.length(); i < len; i += 5) {
            builder.insert(i, ' ');
        }
        builder.reverse();
        return builder.toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bt_register:    // 输入验证码 , 提交验证码 , 验证成功后 , 注册
                String vCode = mEt_code.getText().toString().trim();
                if (TextUtils.isEmpty(vCode)) {
                    ToastUtil.showToast(this, getResources().getString(R.string.smssdk_write_identify_code));
                    return;
                }
                SMSSDK.submitVerificationCode(mCountryCode, mPhoneNum, vCode);
                break;

            case R.id.bt_resend:
                SMSSDK.getVerificationCode(mCountryCode, mPhoneNum);
                CountTimerView countTimerView = new CountTimerView(mBt_resend, R.string.smssdk_resend_identify_code);
                countTimerView.start();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(mSmsListener);
    }

    private class SmsListener extends EventHandler {
        /**
         * @param event  代表回调事件的类型(成功事件)
         * @param result 成功 / 失败
         * @param data   是不是智能验证 / 返回验证成功 json 格式的 手机号 和 国家号
         */
        @Override
        public void afterEvent(int event, int result, Object data) {
            Log.d(TAG, "event:" + event + ",result:" + result + ",data:" + data);
            if (result == SMSSDK.RESULT_COMPLETE) {                   //   回调完成

                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {   //   提交验证码 验证成功
                    mHandler.sendEmptyMessage(SUBMIT_VERIFICATION_CODE);
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {  // 获取验证码成功
                    mHandler.sendEmptyMessage(GetSmsActivity.GET_VERIFICATION_CODE);
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) { //返回支持发送验证码的国家列表

                }
            } else {
                ((Throwable) data).printStackTrace();
                //此语句代表接口返回失败
                try {
                    Throwable throwable = (Throwable) data;

                    JSONObject object = new JSONObject(
                            throwable.getMessage());
                    String des = object.optString("detail");
                    if (!TextUtils.isEmpty(des)) {
                        Message msg = Message.obtain();
                        msg.what = SMS_RESPONSE_ERROR;
                        msg.obj = des;
                        mHandler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    SMSLog.getInstance().w(e);
                }
            }
        }
    }
}
