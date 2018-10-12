package com.yinglan.sct.adapter.mine.myfocus;

import android.content.Context;

import com.kymjs.common.StringUtils;
import com.yinglan.sct.R;
import com.yinglan.sct.entity.mine.myfocus.UserFocusBean.DataBean.ResultBean;
import com.yinglan.sct.utils.GlideImageLoader;

import cn.bingoogolapple.baseadapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.baseadapter.BGAViewHolderHelper;

/**
 * 我的关注中的用户 适配器
 * Created by Admin on 2017/8/15.
 */
public class UserViewAdapter extends BGAAdapterViewAdapter<ResultBean> {

    public UserViewAdapter(Context context) {
        super(context, R.layout.item_userfocus);
    }

    @Override
    protected void setItemChildListener(BGAViewHolderHelper helper) {
        helper.setItemChildClickListener(R.id.tv_followed);
    }

    @Override
    public void fillData(BGAViewHolderHelper viewHolderHelper, int position, ResultBean listBean) {

        /**
         *头像图片
         */
        GlideImageLoader.glideLoader(mContext, listBean.getFace(), viewHolderHelper.getImageView(R.id.img_head), 0, R.mipmap.avatar);

        /**
         *昵称
         */
        viewHolderHelper.setText(R.id.tv_nickName, listBean.getNickname());

        /**
         *篇日记
         */
        if (StringUtils.isEmpty(listBean.getPost_number())) {
            viewHolderHelper.setText(R.id.tv_diary, "0");
        } else {
            viewHolderHelper.setText(R.id.tv_diary, listBean.getPost_number());
        }


        /**
         *个粉丝
         */
        if (StringUtils.isEmpty(listBean.getFans_number())) {
            viewHolderHelper.setText(R.id.tv_fan, "0");
        } else {
            viewHolderHelper.setText(R.id.tv_fan, listBean.getFans_number());
        }

        viewHolderHelper.setText(R.id.tv_followed, mContext.getString(R.string.followed));
        viewHolderHelper.setBackgroundRes(R.id.tv_followed, R.drawable.shape_followed);
    }

}