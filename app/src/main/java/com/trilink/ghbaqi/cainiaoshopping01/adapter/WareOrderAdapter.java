package com.trilink.ghbaqi.cainiaoshopping01.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.trilink.ghbaqi.cainiaoshopping01.R;
import com.trilink.ghbaqi.cainiaoshopping01.bean.ShoppingCart;

import java.util.List;

/**
 * Created by <a href="http://www.cniao5.com">菜鸟窝</a>
 * 一个专业的Android开发在线教育平台
 */
public class WareOrderAdapter extends BaseAdapter<ShoppingCart> {
private Context mContext;
    private List<ShoppingCart> mDatas;

    public WareOrderAdapter(Context context, List<ShoppingCart> datas) {
        super(context, datas);
        mContext = context;
        mDatas = datas;
    }

    @Override
    protected void bindData(BaseViewHolder holder, ShoppingCart item) {
        ImageView draweeView = holder.findImageView(R.id.drawee_view);
        Glide.with(mContext).load(item.getImgUrl()).into(draweeView);
    }

    @Override
    protected int getItemResId() {
        return R.layout.template_order_wares;
    }

    public  List<ShoppingCart>  getDatas() {

        return mDatas;
    }

    //
//
//
//    public WareOrderAdapter(Context context, List<ShoppingCart> datas) {
//        super(context, R.layout.template_order_wares, datas);
//
//    }
//
//    @Override
//    protected void convert(BaseViewHolder viewHoder, final ShoppingCart item) {
//
////        viewHoder.getTextView(R.id.text_title).setText(item.getName());
////        viewHoder.getTextView(R.id.text_price).setText("￥"+item.getPrice());
//        SimpleDraweeView draweeView = (SimpleDraweeView) viewHoder.getView(R.id.drawee_view);
//        draweeView.setImageURI(Uri.parse(item.getImgUrl()));
//
//    }
//
//
//    public float getTotalPrice(){
//
//        float sum=0;
//        if(!isNull())
//            return sum;
//
//        for (ShoppingCart cart:
//                datas) {
//
//                sum += cart.getCount()*cart.getPrice();
//        }
//
//        return sum;
//
//    }
//
//
//    private boolean isNull(){
//
//        return (datas !=null && datas.size()>0);
//    }






}
