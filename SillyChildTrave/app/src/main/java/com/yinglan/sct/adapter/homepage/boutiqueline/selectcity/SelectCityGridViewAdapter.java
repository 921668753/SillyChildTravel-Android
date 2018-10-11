package com.yinglan.sct.adapter.homepage.boutiqueline.selectcity;

import android.content.Context;
import android.view.View;

import com.yinglan.sct.R;
import com.yinglan.sct.entity.homepage.boutiqueline.selectcity.SelectCityBean.DataBean;
import com.yinglan.sct.utils.GlideImageLoader;

import cn.bingoogolapple.baseadapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.baseadapter.BGAViewHolderHelper;


/**
 * 接送机分类---GridView
 */
public class SelectCityGridViewAdapter extends BGAAdapterViewAdapter<DataBean> {


    public SelectCityGridViewAdapter(Context context) {
        super(context, R.layout.item_countriesclassification);
    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, DataBean model) {

        /**
         * 图片
         */
        GlideImageLoader.glideOrdinaryLoader(mContext, model.getPicture(), helper.getImageView(R.id.img_classification), R.mipmap.placeholderfigure);


        /**
         * 城市名字
         */
        helper.setText(R.id.tv_classificationName, model.getRegion_name());

        /**
         * 机场名字
         */
        helper.setVisibility(R.id.tv_airportName, View.GONE);

    }
}
