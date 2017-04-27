package com.trilink.ghbaqi.cainiaoshopping01.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by ghbaqi on 2017/4/23.
 * 封装  RecycleView  的 Adapter
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    private Context        mContext;
    private List<T>        mDatas;
    private LayoutInflater mInflater;
    private int            itemResId;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    private OnItemClickListener mListener;

    public BaseAdapter(Context context, List<T> datas) {
        mContext = context;
        mDatas = datas;
        mInflater = LayoutInflater.from(context);
        setItemResId();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //        View itemView = mInflater.inflate(itemResId,null,false);
        View itemView = mInflater.inflate(itemResId, null);                  //        !!!!!
        return new BaseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, final int position) {

        bindData(holder, getItem(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(holder.itemView,position);
                }
            }
        });
    }

    /**
     * 通过 抽象方法绑定  ViewHolder 和 数据
     *
     * @param holder
     * @param item
     */
    protected abstract void bindData(BaseViewHolder holder, T item);


    /**
     * 外部传入布局资源文件
     */
    private void setItemResId() {
        this.itemResId = getItemResId();
    }

    ;

    protected abstract int getItemResId();

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    private T getItem(int position) {
        return mDatas.get(position);
    }

    /**
     * 下拉刷新
     * @param list
     */
    public void refreshData(List<T> list) {
        mDatas.clear();
        mDatas.addAll(list);
        notifyDataSetChanged();
    }

    public void loadMoreData(List<T> list) {
        int start = mDatas.size();
        mDatas.addAll(list);
        notifyItemRangeInserted(start,mDatas.size()-1);
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }


}
