package com.trilink.ghbaqi.cainiaoshopping01.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.trilink.ghbaqi.cainiaoshopping01.R;
import com.trilink.ghbaqi.cainiaoshopping01.bean.Wares;

import java.util.List;

/**
 * Created by <a href="http://www.cniao5.com">菜鸟窝</a>
 * 一个专业的Android开发在线教育平台
 */
public class HotWaresAdapter extends RecyclerView.Adapter<HotWaresAdapter.ViewHolder>  {
    private List<Wares> mDatas;
    private LayoutInflater mInflater;
    private Context mContext;

    public HotWaresAdapter(List<Wares> wares){

        mDatas = wares;


    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        mInflater = LayoutInflater.from(mContext);
        View view = mInflater.inflate(R.layout.template_hot_wares,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Wares wares = getData(position);
        Glide.with(mContext).load(wares.getImgUrl()).into(holder.draweeView);
//        holder.draweeView.setImageURI(Uri.parse(wares.getImgUrl()));  cao  !
        holder.textTitle.setText(wares.getName());
        holder.textPrice.setText("￥"+wares.getPrice());


    }


    public Wares getData(int position){

        return mDatas.get(position);
    }


    public List<Wares> getDatas(){

        return  mDatas;
    }
    public void clearData(){

        mDatas.clear();
        notifyItemRangeRemoved(0,mDatas.size());
    }

    public void addData(List<Wares> datas){

        addData(0,datas);
    }

    public void addData(int position,List<Wares> datas){

        if(datas !=null && datas.size()>0) {

            mDatas.addAll(datas);
            notifyItemRangeChanged(position, mDatas.size());
        }

    }


    @Override
    public int getItemCount() {

        if(mDatas!=null && mDatas.size()>0)
            return mDatas.size();

        return 0;
    }

    public void refreshData(List<Wares> list) {
        mDatas.clear();
        mDatas.addAll(list);
        notifyDataSetChanged();

    }

    public void loadMoreData(List<Wares> list) {
        int startPoi = mDatas.size()-1;
        mDatas.addAll(list);
        notifyItemRangeInserted(startPoi,mDatas.size()-1);
    }


    class ViewHolder extends RecyclerView.ViewHolder{


        ImageView draweeView;
        TextView  textTitle;
        TextView  textPrice;


        public ViewHolder(View itemView) {
            super(itemView);


            draweeView = (ImageView) itemView.findViewById(R.id.drawee_view);
            textTitle= (TextView) itemView.findViewById(R.id.text_title);
            textPrice= (TextView) itemView.findViewById(R.id.text_price);
        }
    }
}
