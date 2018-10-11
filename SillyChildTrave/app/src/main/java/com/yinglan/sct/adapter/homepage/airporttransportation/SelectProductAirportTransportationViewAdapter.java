package com.yinglan.sct.adapter.homepage.airporttransportation;

import android.content.Context;

import com.yinglan.sct.R;
import com.yinglan.sct.entity.homepage.airporttransportation.SelectProductAirportTransportationBean.DataBean;
import com.yinglan.sct.utils.GlideImageLoader;

import cn.bingoogolapple.baseadapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.baseadapter.BGAViewHolderHelper;


/**
 * 选择产品----接送机
 */
public class SelectProductAirportTransportationViewAdapter extends BGAAdapterViewAdapter<DataBean> {

    public SelectProductAirportTransportationViewAdapter(Context context) {
        super(context, R.layout.item_productairporttransportation);
    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, DataBean model) {

        /**
         * 图片
         */
        GlideImageLoader.glideLoader(mContext, model.getMain_picture(), helper.getImageView(R.id.img_selectProductAirport), 10, 0, R.mipmap.placeholderfigure);

        /**
         * 名字
         */
        helper.setText(R.id.tv_name, model.getProduct_name());


        /**
         * 车辆名字
         */
        helper.setText(R.id.tv_vehicleName, model.getModel());


        /**
         * 人数
         */
        helper.setText(R.id.tv_people, model.getPassenger_number());


        /**
         * 预定次数
         */
        helper.setText(R.id.tv_bookingNum, model.getOrder_number());

    }
}
