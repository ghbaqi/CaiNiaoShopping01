package com.trilink.ghbaqi.cainiaoshopping01.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.trilink.ghbaqi.cainiaoshopping01.R;
import com.trilink.ghbaqi.cainiaoshopping01.utils.ToastUtil;
import com.trilink.ghbaqi.cainiaoshopping01.view.widget.ClearEditText;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;

public class GetSmsActivity extends BaseActivity implements View.OnClickListener {

    public static final int    GET_VERIFICATION_CODE    = 0x03;
    public static final int    SUBMIT_VERIFICATION_CODE = 0x04;
    public static final int    SMS_RESPONSE_ERROR       = 0X05;
    public static final String EXTRA_PWD                = "EXTRA_PWD";
    public static final String EXTRA_PHONE_NUM          = "EXTRA_PHONE_NUM";
    public static final String EXTRA_COUNTRY_CODE       = "EXTRA_COUNTRY_CODE";
    private SmsListener   mSmsListener;
    private Handler       mHandler;
    private ClearEditText mEt_phonenum;
    private ClearEditText mEt_pwd;
    private TextView      mTv_countrycode;
    private String        mPhoneNum;
    private String        mPwd;
    private String        mCountryCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_getsms);
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
                    case GET_VERIFICATION_CODE:    // 获取验证码成功
                        Log.d(TAG, "获取验证码成功");
                        Intent intent = new Intent(GetSmsActivity.this, RegisterActivity.class);
                        intent.putExtra(EXTRA_PWD, mPwd);
                        intent.putExtra(EXTRA_PHONE_NUM, mPhoneNum);
                        intent.putExtra(EXTRA_COUNTRY_CODE, mCountryCode);
                        startActivity(intent);
                        finish();
                        break;
                    case SUBMIT_VERIFICATION_CODE: // 提交验证码 验证成功
                        Log.d(TAG, "提交验证码验证成功");
                        break;
                    case SMS_RESPONSE_ERROR:             // 失败
                        Log.d(TAG, "验证码 失败" + msg.obj);
                        ToastUtil.showToast(GetSmsActivity.this, msg.obj.toString());
                        break;
                }
            }
        };
    }


    private void initView() {
        findViewById(R.id.bt_next).setOnClickListener(this);
        mEt_phonenum = (ClearEditText) findViewById(R.id.et_phonenumber);
        mEt_pwd = (ClearEditText) findViewById(R.id.et_pwd);
        mTv_countrycode = (TextView) findViewById(R.id.tv_CountryCode);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_next:
                // 请求获取短信验证码，在监听中返回
                mPhoneNum = mEt_phonenum.getText().toString().trim().replaceAll("\\s*", "");
                mCountryCode = mTv_countrycode.getText().toString().trim();
                mPwd = mEt_pwd.getText().toString().trim();
                if (!checkPhoneNum(mPhoneNum, mCountryCode)) {
                    //not 86   +86
                    return;
                }
                if (TextUtils.isEmpty(mPwd)) {
                    ToastUtil.showToast(this, "密码不能为空!");
                    return;
                } else if (mPwd.length() < 6 || mPwd.length() > 12) {
                    ToastUtil.showToast(this, "密码长度需要在6至12位");
                    return;
                }
                SMSSDK.getVerificationCode(mCountryCode, mPhoneNum);
                break;
        }
    }

    private boolean checkPhoneNum(String phone, String code) {
        if (code.startsWith("+")) {
            code = code.substring(1);
        }

        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showToast(this, "请输入手机号码");
            return false;
        }

        if (code == "86") {
            if (phone.length() != 11) {
                ToastUtil.showToast(this, "手机号码长度不对");
                return false;
            }

        }

        String rule = "^1(3|5|7|8|4)\\d{9}";
        Pattern p = Pattern.compile(rule);
        Matcher m = p.matcher(phone);

        if (!m.matches()) {
            ToastUtil.showToast(this, "您输入的手机号码格式不正确");
            return false;
        }

        return true;
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

                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {   //   提交验证码成功
                    mHandler.sendEmptyMessage(SUBMIT_VERIFICATION_CODE);
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {  // 获取验证码成功
                    mHandler.sendEmptyMessage(GET_VERIFICATION_CODE);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(mSmsListener);
    }
}
