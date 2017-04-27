package com.trilink.ghbaqi.cainiaoshopping01.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.trilink.ghbaqi.cainiaoshopping01.R;
import com.trilink.ghbaqi.cainiaoshopping01.bean.Wares;

import java.util.List;

/**
 * Created by ghbaqi on 2017/4/23.
 */

public class WaresAdapter extends BaseAdapter<Wares> {

    private  Context context;

    public WaresAdapter(Context context, List<Wares> datas) {
        super(context, datas);
        this.context = context;
    }

    @Override
    protected void bindData(BaseViewHolder holder, Wares item) {

        holder.findTextView(R.id.text_title).setText(item.getName());
        holder.findTextView(R.id.text_price).setText("￥"+item.getPrice());
        ImageView draweeView = (ImageView) holder.findImageView(R.id.drawee_view);
        Glide.with(context).load(item.getImgUrl()).into(draweeView);



    }

    @Override
    protected int getItemResId() {
        return R.layout.template_grid_wares;
    }
}
