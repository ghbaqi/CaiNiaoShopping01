package com.trilink.ghbaqi.cainiaoshopping01.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ghbaqi on 2017/4/23.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder {


    private SparseArray<View> views;
    public  View              itemView;

    public BaseViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        views = new SparseArray<>();

    }

    public View findViewById(int id) {
        View view = views.get(id);
        if (view == null) {
            view = itemView.findViewById(id);
            views.put(id, view);
        }
        return view;
    }

    public TextView findTextView(int tvId) {
        View view = views.get(tvId);
        if (view == null) {
            view = (TextView) itemView.findViewById(tvId);
            views.put(tvId, view);
        }
        return (TextView) view;
    }

    public ImageView findImageView(int ivId) {
        View view = views.get(ivId);
        if (view == null) {
            view = (ImageView) itemView.findViewById(ivId);
            views.put(ivId, view);
        }
        return (ImageView) view;
    }

    public Button findButton(int btId) {
        View view = views.get(btId);
        if (view == null) {
            view = (Button) itemView.findViewById(btId);
            views.put(btId, view);
        }
        return (Button) view;
    }


}
