package com.trilink.ghbaqi.cainiaoshopping01.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.trilink.ghbaqi.cainiaoshopping01.R;
import com.trilink.ghbaqi.cainiaoshopping01.bean.Wares;
import com.trilink.ghbaqi.cainiaoshopping01.utils.CartManager;
import com.trilink.ghbaqi.cainiaoshopping01.utils.ToastUtil;

import java.util.List;

/**
 * Created by ghbaqi on 2017/5/9.
 */

public class HotWaresAdapter02 extends BaseAdapter<Wares> {
    private Context mActivity;
    public HotWaresAdapter02(Context context, List<Wares> datas) {
        super(context, datas);
        mActivity   = context;
    }

    @Override
    protected void bindData(BaseViewHolder holder, final Wares item) {


        TextView tv_title = holder.findTextView(R.id.text_title);
        ImageView iv = holder.findImageView(R.id.drawee_view);
        TextView tv_price = holder.findTextView(R.id.text_price);

        holder.findButton(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(mActivity, "加入购物车!" + item.getId());
                CartManager.getInstance(mActivity).put(item);
            }
        });
        tv_title.setText(item.getName());
        tv_price.setText("￥" + item.getPrice());
        Glide.with(mActivity).load(item.getImgUrl()).into(iv);


    }

    @Override
    protected int getItemResId() {
        return R.layout.template_hot_wares;
    }
}
