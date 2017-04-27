package com.trilink.ghbaqi.cainiaoshopping01.view.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.trilink.ghbaqi.cainiaoshopping01.R;

/**
 * Created by ghbaqi on 2017/4/25.
 */

public class CustomNumView extends FrameLayout implements View.OnClickListener {
    private Button   mTv_add;
    private TextView mTv_num;
    private Button   mTv_sub;
    private int mMaxNum = 10;
    private int mMinNum = 1;
    private int mNumber;

    private int mDefaultNum = 5;
    private OnNumChangeListener mListener;

    public void setOnNumChangeListener(OnNumChangeListener listener) {
        mListener = listener;
    }

    public CustomNumView(@NonNull Context context) {
        this(context, null);
    }

    public CustomNumView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomNumView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.customnum_view_layout, this, true);
        initView();
        if (attrs != null) {
            initAttrs(context, attrs, defStyleAttr);
        }
    }

    /**
     * 从 xml 中读取自定义属性 , 并设置给控件
     */
    private void initAttrs(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, attrs, R.styleable.NumberAddSubView, defStyleAttr, 0);
        int defValue = a.getInt(R.styleable.NumberAddSubView_defValue, 5);
        int maxValue = a.getInt(R.styleable.NumberAddSubView_maxValue, 10);
        int minValue = a.getInt(R.styleable.NumberAddSubView_minValue, 0);
        int bt_color = a.getInt(R.styleable.NumberAddSubView_addsubColor, Color.BLACK);
        int num_color = a.getInt(R.styleable.NumberAddSubView_numColor, Color.BLACK);

        mTv_num.setText(defValue + "");
        mMaxNum = maxValue;
        mMinNum = minValue;
        mTv_sub.setTextColor(bt_color);
        mTv_add.setTextColor(bt_color);
        mTv_num.setTextColor(num_color);
        a.recycle();
    }

    private void initView() {
        mTv_add = (Button) findViewById(R.id.tv_add);
        mTv_num = (TextView) findViewById(R.id.tv_num);
        mTv_sub = (Button) findViewById(R.id.tv_sub);
        mTv_add.setOnClickListener(this);
        mTv_sub.setOnClickListener(this);
        mTv_num.setText(mDefaultNum + "");

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add:
                numAdd();
                break;
            case R.id.tv_sub:
                numSub();
                break;
        }
    }


    private void numSub() {
//        mNumber = Integer.valueOf(mTv_num.getText().toString().trim());
        mNumber -= 1;
        if (mNumber < mMinNum)
            return;
        if (mListener != null) {
            mListener.onSubClicked(mNumber);
        }
        mTv_num.setText(mNumber + "");


    }

    private void numAdd() {
        mNumber += 1;
//        mNumber = Integer.valueOf(mTv_num.getText().toString().trim());
        if (mNumber > mMaxNum)
            return;
        mTv_num.setText(mNumber + "");
        if (mListener != null) {
            mListener.onAddClicked(mNumber);
        }
    }

    /**
     * 设置数字 不能超过边界值
     */
    public void setNumber(int count) {
        if (count > mMaxNum) {
            mNumber = mMaxNum;
        } else if (count < mMinNum) {
            mNumber = mMinNum;
        } else {
            mNumber = count;
        }

        mTv_num.setText(mNumber + "");
    }


    public interface OnNumChangeListener {
        void onAddClicked(int number);

        void onSubClicked(int number);
    }

    public int getMaxNum() {
        return mMaxNum;
    }

    public int getMinNum() {
        return mMinNum;
    }

    public int getDefaultNum() {
        return mDefaultNum;
    }
}
