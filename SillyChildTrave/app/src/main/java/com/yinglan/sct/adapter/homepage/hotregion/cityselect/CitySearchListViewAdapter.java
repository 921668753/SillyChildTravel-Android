package com.yinglan.sct.adapter.homepage.hotregion.cityselect;

import android.content.Context;

import com.yinglan.sct.R;
import com.yinglan.sct.entity.homepage.privatecustom.cityselect.CitySearchRListBean.DataBean;
import com.yinglan.sct.utils.GlideImageLoader;

import cn.bingoogolapple.baseadapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.baseadapter.BGAViewHolderHelper;


public class CitySearchListViewAdapter extends BGAAdapterViewAdapter<DataBean> {


    public CitySearchListViewAdapter(Context context) {
        super(context, R.layout.item_citysearchlist);
    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, DataBean model) {


        /**
         * 图片
         */
        GlideImageLoader.glideOrdinaryLoader(mContext, model.getCity_picture(), helper.getImageView(R.id.img_classification), R.mipmap.placeholderfigure);


        /**
         * 城市名字
         */
        helper.setText(R.id.tv_classificationName, model.getCity_name());

    }
}
