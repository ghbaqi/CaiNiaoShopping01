package com.trilink.ghbaqi.cainiaoshopping01.adapter;
import android.content.Context;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.trilink.ghbaqi.cainiaoshopping01.R;
import com.trilink.ghbaqi.cainiaoshopping01.bean.ShoppingCart;
import com.trilink.ghbaqi.cainiaoshopping01.utils.CartManager;
import com.trilink.ghbaqi.cainiaoshopping01.utils.ToastUtil;
import com.trilink.ghbaqi.cainiaoshopping01.view.widget.CustomNumView;
import java.util.List;

/**
 * Created by ghbaqi on 2017/4/26.
 */

public class CartAdapter extends BaseAdapter<ShoppingCart> {

    private final List<ShoppingCart> mDatas;
    private Context mContext;
    private CartManager mCartManager;
    private TextView mTv_Total;

    public CartAdapter(Context context, List<ShoppingCart> datas,TextView tv_Total) {
        super(context, datas);
        mContext = context;
        this.mDatas = datas;
        Logger.init("CartAdapter");
        mCartManager = CartManager.getInstance(mContext);
        mTv_Total = tv_Total;
    }

    @Override
    protected void bindData(BaseViewHolder holder, final ShoppingCart item) {
        CheckBox checkBox = (CheckBox) holder.findViewById(R.id.checkbox);
        ImageView iv = holder.findImageView(R.id.iv);
        TextView tv_title = holder.findTextView(R.id.text_title);
        TextView tv_price = holder.findTextView(R.id.text_price);
        final CustomNumView numberView = (CustomNumView) holder.findViewById(R.id.numberview);

        checkBox.setChecked(item.isChecked());
        Glide.with(mContext).load(item.getImgUrl()).into(iv);
        String description = item.getDescription();
//        tv_title.setText(description);
//        Log.d("CartAdapter",description);         //  ??　
        tv_price.setText("¥ "+item.getPrice());
        numberView.setNumber(item.getCount());

        /**
         *  处理商品数量加减控件 , 更改集合数据 , 并保存
         *  更新价格
         */
        numberView.setOnNumChangeListener(new CustomNumView.OnNumChangeListener() {
            @Override
            public void onAddClicked(int number) {
                if (number > numberView.getMaxNum()) {
                    ToastUtil.showToast(mContext,"最多只能购买 "+numberView.getMaxNum()+" 件商品");
                    return;
                }
                item.setCount(number);
                mCartManager.update(item);
                refreshTotalPrice(mTv_Total);
            }

            @Override
            public void onSubClicked(int number) {
                if (number < numberView.getMinNum())
                    return;
                item.setCount(number);
                mCartManager.update(item);
                refreshTotalPrice(mTv_Total);

            }
        });
    }

    @Override
    protected int getItemResId() {
        return R.layout.template_cart;
    }

    public void refreshTotalPrice(TextView tvTotal) {
        float total = 0;
        for (ShoppingCart data : mDatas) {
            if (data.isChecked()) {
                total = total + data.getPrice() * data.getCount();
            }
        }
        tvTotal.setText("¥ "+total);
    }
}
