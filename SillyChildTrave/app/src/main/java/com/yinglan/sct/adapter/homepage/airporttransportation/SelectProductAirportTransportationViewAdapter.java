package com.yinglan.sct.adapter.homepage.airporttransportation;

import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kymjs.common.DensityUtils;
import com.yinglan.sct.R;
import com.yinglan.sct.entity.homepage.airporttransportation.SelectProductAirportTransportationBean.DataBean;
import com.yinglan.sct.utils.GlideImageLoader;

import cn.bingoogolapple.baseadapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.baseadapter.BGAViewHolderHelper;

/**
 * 选择产品----接送机
 */
public class SelectProductAirportTransportationViewAdapter extends BGARecyclerViewAdapter<DataBean> {

    public SelectProductAirportTransportationViewAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_productairporttransportation);
    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, DataBean model) {

        /**
         * 图片
         */
        ImageView imageView = helper.getImageView(R.id.img_selectProductAirport);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        float width1 = (DensityUtils.getScreenW() - DensityUtils.dip2px(8) * 3) / 2;
        lp.width = (int) width1;
        float scale = 0;
        float tempHeight = 0;
        if (model.getWidth() <= 0 || model.getHeight() <= 0) {
            tempHeight = width1;
        } else {
            scale = (width1 + 0f) / model.getWidth();
            tempHeight = model.getHeight() * scale;
        }
        lp.height = (int) tempHeight;
        imageView.setLayoutParams(lp);
        GlideImageLoader.glideLoader(mContext, model.getMain_picture() + "?imageView2/0/w/" + lp.width + "/h/" + lp.height, helper.getImageView(R.id.img_selectProductAirport), 10, 0, R.mipmap.placeholderfigure);

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
