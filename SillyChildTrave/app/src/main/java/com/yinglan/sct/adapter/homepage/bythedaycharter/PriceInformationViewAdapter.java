package com.yinglan.sct.adapter.homepage.bythedaycharter;

import android.content.Context;

import com.yinglan.sct.R;
import com.yinglan.sct.entity.homepage.bythedaycharter.PriceInformationBean.DataBean.ServiceBean;

import cn.bingoogolapple.baseadapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.baseadapter.BGAViewHolderHelper;


public class PriceInformationViewAdapter extends BGAAdapterViewAdapter<ServiceBean> {

    public PriceInformationViewAdapter(Context context) {
        super(context, R.layout.item_containsservice);
    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, ServiceBean model) {
        if (model.getIs_show() == 0) {
            helper.setBackgroundRes(R.id.ll_serviceName, R.drawable.shape_containsservice1);
            helper.setTextColorRes(R.id.tv_serviceName, R.color.hintColors);
        } else {
            helper.setBackgroundRes(R.id.ll_serviceName, R.drawable.shape_containsservice);
            helper.setTextColorRes(R.id.tv_serviceName, R.color.greenColors);
        }
        helper.setText(R.id.tv_serviceName, model.getName());
    }
}
