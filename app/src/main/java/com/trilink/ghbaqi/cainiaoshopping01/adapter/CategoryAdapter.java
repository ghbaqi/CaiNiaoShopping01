package com.trilink.ghbaqi.cainiaoshopping01.adapter;

import android.content.Context;

import com.trilink.ghbaqi.cainiaoshopping01.R;
import com.trilink.ghbaqi.cainiaoshopping01.bean.Category;

import java.util.List;

/**
 * Created by ghbaqi on 2017/4/23.
 */

public class CategoryAdapter extends BaseAdapter<Category> {

    public CategoryAdapter(Context context, List<Category> datas) {
        super(context, datas);
    }

    @Override
    protected void bindData(BaseViewHolder holder, Category item) {
        holder.findTextView(R.id.textView).setText(item.getName());

    }

    @Override
    protected int getItemResId() {
        return R.layout.template_single_text;
    }
}
