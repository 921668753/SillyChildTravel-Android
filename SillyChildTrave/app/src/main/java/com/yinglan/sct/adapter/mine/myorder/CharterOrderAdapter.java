package com.yinglan.sct.adapter.mine.myorder;

import android.content.Context;
import android.view.View;

import com.common.cklibrary.utils.DataUtil;
import com.kymjs.common.StringUtils;
import com.yinglan.sct.R;
import com.yinglan.sct.entity.mine.myorder.CharterOrderBean.DataBean.ResultBean;
import com.yinglan.sct.utils.GlideImageLoader;

import cn.bingoogolapple.baseadapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.baseadapter.BGAViewHolderHelper;


/**
 * 我的订单---包车订单   适配器
 * Created by Admin on 2017/8/15.
 */

public class CharterOrderAdapter extends BGAAdapterViewAdapter<ResultBean> {

    public CharterOrderAdapter(Context context) {
        super(context, R.layout.item_charterorder);
    }

    @Override
    protected void setItemChildListener(BGAViewHolderHelper helper) {
        super.setItemChildListener(helper);
        helper.setItemChildClickListener(R.id.tv_confirmPayment);
        helper.setItemChildClickListener(R.id.tv_callUp);
        helper.setItemChildClickListener(R.id.tv_sendPrivateChat);
        helper.setItemChildClickListener(R.id.tv_appraiseOrder);
        helper.setItemChildClickListener(R.id.tv_additionalComments);
    }

    @Override
    public void fillData(BGAViewHolderHelper viewHolderHelper, int position, ResultBean model) {

        viewHolderHelper.setText(R.id.tv_orderNumber, model.getOrder_number());

        switch (model.getStatus()) {
            case 0://待付款
                viewHolderHelper.setText(R.id.tv_charterStatus, R.string.obligation);
                viewHolderHelper.setVisibility(R.id.tv_confirmPayment, View.VISIBLE);
                viewHolderHelper.setVisibility(R.id.tv_callUp, View.GONE);
                viewHolderHelper.setVisibility(R.id.tv_sendPrivateChat, View.GONE);
                viewHolderHelper.setVisibility(R.id.tv_appraiseOrder, View.GONE);
                viewHolderHelper.setVisibility(R.id.tv_additionalComments, View.GONE);
                break;
            case 1://进行中
                viewHolderHelper.setText(R.id.tv_charterStatus, R.string.ongoing);
                viewHolderHelper.setVisibility(R.id.tv_confirmPayment, View.GONE);
                viewHolderHelper.setVisibility(R.id.tv_callUp, View.GONE);
                viewHolderHelper.setVisibility(R.id.tv_sendPrivateChat, View.GONE);
                viewHolderHelper.setVisibility(R.id.tv_appraiseOrder, View.GONE);
                viewHolderHelper.setVisibility(R.id.tv_additionalComments, View.GONE);
                if (model.getService_director_state() == 1) {
                    viewHolderHelper.setVisibility(R.id.tv_callUp, View.VISIBLE);
                    viewHolderHelper.setVisibility(R.id.tv_sendPrivateChat, View.VISIBLE);
                }
                break;
            case 2://待评价
                viewHolderHelper.setText(R.id.tv_charterStatus, R.string.evaluate);
                viewHolderHelper.setVisibility(R.id.tv_confirmPayment, View.GONE);
                viewHolderHelper.setVisibility(R.id.tv_callUp, View.GONE);
                viewHolderHelper.setVisibility(R.id.tv_sendPrivateChat, View.GONE);
                viewHolderHelper.setVisibility(R.id.tv_appraiseOrder, View.VISIBLE);
                viewHolderHelper.setVisibility(R.id.tv_additionalComments, View.GONE);
                break;
            case 3://已完成
            case 30://已完成
                viewHolderHelper.setText(R.id.tv_charterStatus, R.string.completed);
                viewHolderHelper.setVisibility(R.id.tv_confirmPayment, View.GONE);
                viewHolderHelper.setVisibility(R.id.tv_callUp, View.GONE);
                viewHolderHelper.setVisibility(R.id.tv_sendPrivateChat, View.GONE);
                viewHolderHelper.setVisibility(R.id.tv_appraiseOrder, View.GONE);
                viewHolderHelper.setVisibility(R.id.tv_additionalComments, View.GONE);
                break;
            case 4://已完成
                viewHolderHelper.setText(R.id.tv_charterStatus, R.string.closed);
                viewHolderHelper.setVisibility(R.id.tv_confirmPayment, View.GONE);
                viewHolderHelper.setVisibility(R.id.tv_callUp, View.GONE);
                viewHolderHelper.setVisibility(R.id.tv_sendPrivateChat, View.GONE);
                viewHolderHelper.setVisibility(R.id.tv_appraiseOrder, View.GONE);
                viewHolderHelper.setVisibility(R.id.tv_additionalComments, View.GONE);
                break;
            default:
                break;
        }
        GlideImageLoader.glideOrdinaryLoader(mContext, model.getMain_picture(), viewHolderHelper.getImageView(R.id.img_charterOrder), R.mipmap.placeholderfigure1);
        viewHolderHelper.setText(R.id.tv_title, model.getTitle());
        if (model.getProduct_set_cd() == 1 || model.getProduct_set_cd() == 2) {
            viewHolderHelper.setText(R.id.tv_serviceTime, DataUtil.formatData(StringUtils.toLong(model.getService_start_time()), "yyyy-MM-dd HH:mm"));
        } else if (model.getProduct_set_cd() == 3) {
            viewHolderHelper.setText(R.id.tv_serviceTime, DataUtil.formatData(StringUtils.toLong(model.getService_start_time()),
                    "yyyy" + mContext.getString(R.string.year) + "MM" + mContext.getString(R.string.month) + "dd" + mContext.getString(R.string.day)) +
                    "-" + DataUtil.formatData(StringUtils.toLong(model.getService_start_time()),
                    "yyyy" + mContext.getString(R.string.year) + "MM" + mContext.getString(R.string.month) + "dd" + mContext.getString(R.string.day)));
        } else if (model.getProduct_set_cd() == 4 || model.getProduct_set_cd() == 5) {
            viewHolderHelper.setText(R.id.tv_serviceTime, DataUtil.formatData(StringUtils.toLong(model.getService_start_time()), "yyyy-MM-dd"));
        }
        if (model.getService_director_state() == 0 && model.getStatus() == 1) {
            viewHolderHelper.setText(R.id.tv_serviceCompany, mContext.getString(R.string.singleWaitingList));
        } else {
            viewHolderHelper.setText(R.id.tv_serviceCompany, model.getService_director());
        }
        viewHolderHelper.setText(R.id.tv_orderMoney, mContext.getString(R.string.renminbi) + model.getOrder_amount());
        viewHolderHelper.setText(R.id.tv_money, mContext.getString(R.string.renminbi) + model.getPay_amount());
    }


}