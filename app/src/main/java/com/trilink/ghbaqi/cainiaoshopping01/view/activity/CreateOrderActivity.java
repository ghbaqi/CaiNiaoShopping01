package com.trilink.ghbaqi.cainiaoshopping01.view.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pingplusplus.android.PaymentActivity;
import com.trilink.ghbaqi.cainiaoshopping01.R;
import com.trilink.ghbaqi.cainiaoshopping01.adapter.WareOrderAdapter;
import com.trilink.ghbaqi.cainiaoshopping01.adapter.layoutmanager.FullyLinearLayoutManager;
import com.trilink.ghbaqi.cainiaoshopping01.bean.Charge;
import com.trilink.ghbaqi.cainiaoshopping01.bean.CreateOrderRespMsg;
import com.trilink.ghbaqi.cainiaoshopping01.bean.ShoppingCart;
import com.trilink.ghbaqi.cainiaoshopping01.global.UserManager;
import com.trilink.ghbaqi.cainiaoshopping01.http.BaseCallBack;
import com.trilink.ghbaqi.cainiaoshopping01.http.GsonUtil;
import com.trilink.ghbaqi.cainiaoshopping01.http.OkHttpManager;
import com.trilink.ghbaqi.cainiaoshopping01.utils.CartManager;
import com.trilink.ghbaqi.cainiaoshopping01.utils.Contants;
import com.trilink.ghbaqi.cainiaoshopping01.utils.JSONUtil;
import com.trilink.ghbaqi.cainiaoshopping01.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ghbaqi on 2017/5/7.
 */

public class CreateOrderActivity extends BaseActivity {

    /**
     * 银联支付渠道
     */
    private static final String CHANNEL_UPACP   = "upacp";
    /**
     * 微信支付渠道
     */
    private static final String CHANNEL_WECHAT  = "wx";
    /**
     * 支付宝支付渠道
     */
    private static final String CHANNEL_ALIPAY  = "alipay";
    private              String mCurrentChannel = "alipay";
    @BindView(R.id.toolbar)
    Toolbar        mToolbar;
    @BindView(R.id.tv_phone)
    TextView       mTvPhone;
    @BindView(R.id.tv_address)
    TextView       mTvAddress;
    @BindView(R.id.rl_address)
    RelativeLayout mRlAddress;
    @BindView(R.id.recycleview)
    RecyclerView   mRecycleview;
    @BindView(R.id.rb_alipay)
    RadioButton    mRbAlipay;
    @BindView(R.id.rl_alipay)
    RelativeLayout mRlAlipay;
    @BindView(R.id.rb_webpay)
    RadioButton    mRbWebpay;
    @BindView(R.id.rl_wepay)
    RelativeLayout mRlWepay;
    @BindView(R.id.tv_totalprice)
    TextView       mTvTotalprice;
    @BindView(R.id.bt_submitOrder)
    Button         mBtSubmitOrder;
    private ProgressDialog     mProgressDialog;
    private CartManager        mCartManager;
    private WareOrderAdapter   mAdapter;
    private List<ShoppingCart> mDatas;
    private String             mOrderNumber;
    /**
     * 百度支付渠道
     */
    //   private static final String CHANNEL_BFB = "bfb";

    /**
     * 京东支付渠道
     */
    //  private static final String CHANNEL_JDPAY_WAP = "jdpay_wap";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_createorder);
        ButterKnife.bind(this);
        mCartManager = CartManager.getInstance(this);
        initView();

    }

    private void initView() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("提交订单中...");
        mProgressDialog.setCancelable(false);

        View child = LayoutInflater.from(this).inflate(R.layout.cartfragment_toolbar_head, null, false);
        TextView tv_title = (TextView) child.findViewById(R.id.tv_title);
        TextView tv_share = (TextView) child.findViewById(R.id.tv_edit);
        mToolbar.addView(child);
        tv_share.setVisibility(View.INVISIBLE);
        tv_title.setText("填写订单");
        mToolbar.setNavigationIcon(R.drawable.icon_back_32px);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateOrderActivity.this.finish();
            }
        });

        initRecycleView();
    }

    private void initRecycleView() {
        mDatas = mCartManager.getAll();
        mAdapter = new WareOrderAdapter(this, mDatas);
        FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(this);
        layoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        mRecycleview.setLayoutManager(layoutManager);
        mRecycleview.setAdapter(mAdapter);

        mTvTotalprice.setText("￥" + getTotalPrice());
    }

    @OnClick({R.id.rl_alipay, R.id.rl_wepay, R.id.bt_submitOrder})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_alipay:
            case R.id.rl_wepay:
                if (mCurrentChannel == CHANNEL_ALIPAY) {
                    mRbAlipay.setChecked(false);
                    mRbWebpay.setChecked(true);
                    mCurrentChannel = CHANNEL_WECHAT;
                } else {
                    mRbAlipay.setChecked(true);
                    mRbWebpay.setChecked(false);
                    mCurrentChannel = CHANNEL_ALIPAY;
                }
                break;
            case R.id.bt_submitOrder:
                submitOrder();
                break;
        }
    }

    private void submitOrder() {
        final List<ShoppingCart> carts = mAdapter.getDatas();
        List<WareItem> items = new ArrayList<>(carts.size());
        for (ShoppingCart c : carts) {
            WareItem item = new WareItem(c.getId(), c.getPrice().intValue());
            items.add(item);
        }
        String item_json = JSONUtil.toJSON(items);
        Map<String, Object> params = new HashMap<>(5);
        params.put("user_id", UserManager.getInstace().getUser().getId() + "");
        params.put("item_json", item_json);
        params.put("pay_channel", mCurrentChannel);
        params.put("amount", (int) getTotalPrice() + "");
        params.put("addr_id", 1 + "");
        params.put("token", UserManager.getInstace().getToken());
        mBtSubmitOrder.setEnabled(false);
        // 第一步 去自己的服务器 获取 Charge 对象
        OkHttpManager.getInstance().commonPost(Contants.API.ORDER_CREATE, params, new BaseCallBack() {
            @Override
            public void onPreExecute() {
                mProgressDialog.show();
            }

            @Override
            public void onFailure(Exception e) {
                mBtSubmitOrder.setEnabled(true);
                mProgressDialog.dismiss();
                Log.d(TAG, "failure:" + e.getMessage());
            }

            @Override
            public void onSuccess(String result) {  //
                Log.d(TAG, "onSuccess:" + result);
                mBtSubmitOrder.setEnabled(true);
                mProgressDialog.dismiss();
                CreateOrderRespMsg msg = GsonUtil.parseJsonToBean(result, CreateOrderRespMsg.class);
                Log.d(TAG, "订单:" + msg.getMessage());
                if (msg.getStatus() == 405) {     // 没有权限使用该接口,请购买课程  !!!!
                    ToastUtil.showToast(CreateOrderActivity.this, msg.getMessage());
                    return;
                }
                mOrderNumber = msg.getData().getOrderNum();
                Charge charge = msg.getData().getCharge();
                //  第二步 : 发起支付
                createPaymentActivity(JSONUtil.toJSON(charge));

            }
        });
    }

    private void createPaymentActivity(String goods) {
        Intent intent = new Intent();
        String packageName = getPackageName();
        ComponentName componentName = new ComponentName(packageName, packageName + ".wxapi.WXPayEntryActivity");
        intent.setComponent(componentName);
        intent.putExtra(PaymentActivity.EXTRA_CHARGE, goods);
        startActivityForResult(intent, Contants.REQUEST_CODE_PAYMENT);
    }


    // 第三步 : 获取支付状态
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //支付页面返回处理
        if (requestCode == Contants.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");

                if (result.equals("success"))
                    changeOrderStatus(1);
                else if (result.equals("fail"))
                    changeOrderStatus(-1);
                else if (result.equals("cancel"))
                    changeOrderStatus(-2);
                else
                    changeOrderStatus(0);

            /* 处理返回值
             * "success" - payment succeed
             * "fail"    - payment failed
             * "cancel"  - user canceld
             * "invalid" - payment plugin not installed
             *
             * 如果是银联渠道返回 invalid，调用 UPPayAssistEx.installUPPayPlugin(this); 安装银联安全支付控件。
             */
                //                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                //                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息

            }
        }
    }

    // 第四步 : 去自己的服务器更改订单状态
    private void changeOrderStatus(final int status) {

        Map<String, Object> params = new HashMap<>(5);
        params.put("order_num", mOrderNumber);
        params.put("status", status + "");
        params.put("token", UserManager.getInstace().getToken());

        OkHttpManager.getInstance().commonPost(Contants.API.ORDER_COMPLEPE, params, new BaseCallBack() {
            @Override
            public void onPreExecute() {
                mProgressDialog.show();
            }

            @Override
            public void onFailure(Exception e) {
                mProgressDialog.dismiss();
                toPayResultActivity(-1);
            }

            @Override
            public void onSuccess(String result) {
                mProgressDialog.dismiss();
                toPayResultActivity(status);
            }
        });
    }


    // 第五步 : 通知用户 支付结果
    private void toPayResultActivity(int status) {
        Intent intent = new Intent(this, PayResultActivity.class);
        intent.putExtra("status", status);
        startActivity(intent);
        this.finish();
    }


    public float getTotalPrice() {
        float sum = 0;
        if (mDatas == null || mDatas.size() == 0)
            return sum;
        for (ShoppingCart cart :
                mDatas) {
            sum += cart.getCount() * cart.getPrice();
        }
        return sum;
    }

    class WareItem {
        private Long ware_id;
        private int  amount;

        public WareItem(Long ware_id, int amount) {
            this.ware_id = ware_id;
            this.amount = amount;
        }

        public Long getWare_id() {
            return ware_id;
        }

        public void setWare_id(Long ware_id) {
            this.ware_id = ware_id;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }
    }
}
