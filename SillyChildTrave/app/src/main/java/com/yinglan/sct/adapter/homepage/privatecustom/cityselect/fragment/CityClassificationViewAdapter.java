package com.yinglan.sct.adapter.homepage.privatecustom.cityselect.fragment;

import android.content.Context;
import android.graphics.Typeface;

import com.yinglan.sct.R;
import com.yinglan.sct.entity.homepage.privatecustom.cityselect.fragment.CityClassificationBean.DataBean;

import cn.bingoogolapple.baseadapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.baseadapter.BGAViewHolderHelper;


/**
 * 城市分类
 */
public class CityClassificationViewAdapter extends BGAAdapterViewAdapter<DataBean> {


    public CityClassificationViewAdapter(Context context) {
        super(context, R.layout.item_cities);
    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, DataBean model) {

        /**
         * 背景色
         */
        if (model.getIsSelected() == 0) {
          //  helper.setBackgroundRes(R.id.tv_city, R.color.whiteColors);
            helper.setTextColorRes(R.id.tv_city, R.color.tabColors);
            //设置不为加粗
            helper.getTextView(R.id.tv_city).setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        } else {
           // helper.setBackgroundRes(R.id.tv_city, R.drawable.shape_followed1);
            helper.setTextColorRes(R.id.tv_city, R.color.textColor);
            //设置为加粗
            helper.getTextView(R.id.tv_city).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }

        /**
         * 名字
         */
        helper.setText(R.id.tv_city, model.getName());
    }
}
