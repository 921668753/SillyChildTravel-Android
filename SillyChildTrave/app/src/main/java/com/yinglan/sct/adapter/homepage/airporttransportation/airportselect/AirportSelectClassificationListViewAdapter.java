package com.yinglan.sct.adapter.homepage.airporttransportation.airportselect;

import android.content.Context;
import android.graphics.Typeface;

import com.kymjs.common.StringUtils;
import com.yinglan.sct.R;
import com.yinglan.sct.entity.homepage.privatecustom.cityselect.CitySelectListBean.DataBean;

import cn.bingoogolapple.baseadapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.baseadapter.BGAViewHolderHelper;


/**
 * 城市选择分类---ListView
 */
public class AirportSelectClassificationListViewAdapter extends BGAAdapterViewAdapter<DataBean> {


    public AirportSelectClassificationListViewAdapter(Context context) {
        super(context, R.layout.item_countries);
    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, DataBean model) {

        /**
         * 背景色
         */
        if (model.getIsSelected() == 0) {
            helper.setBackgroundRes(R.id.tv_country, R.color.whiteColors);
            helper.setTextColorRes(R.id.tv_country, R.color.tabColors);
        } else {
            helper.setBackgroundRes(R.id.tv_country, R.drawable.shape_followed1);
            helper.setTextColorRes(R.id.tv_country, R.color.whiteColors);
        }

        if (!StringUtils.isEmpty(model.getName()) && model.getName().startsWith(mContext.getString(R.string.recommended)) && model.getIsSelected() == 0) {
            helper.setTextColorRes(R.id.tv_country, R.color.textColor);
            //设置为加粗
            helper.getTextView(R.id.tv_country).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        } else {
            //设置不为加粗
            helper.getTextView(R.id.tv_country).setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }

        /**
         * 名字
         */
        helper.setText(R.id.tv_country, model.getName());
    }
}
