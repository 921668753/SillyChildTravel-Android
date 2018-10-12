package com.yinglan.sct.adapter.mine.myrelease;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kymjs.common.DensityUtils;
import com.yinglan.sct.R;
import com.yinglan.sct.entity.mine.myrelease.mydynamic.MyDynamicBean;
import com.yinglan.sct.utils.GlideImageLoader;

import cn.bingoogolapple.baseadapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.baseadapter.BGAViewHolderHelper;


/**
 * 我的发布----攻略  适配器
 * Created by Administrator on 2017/9/6.
 */

public class MyStrategyViewAdapter extends BGARecyclerViewAdapter<MyDynamicBean.DataBean.ResultBean> {

    public MyStrategyViewAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_mystrategy);
    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, MyDynamicBean.DataBean.ResultBean model) {
        ImageView imageView = helper.getImageView(R.id.img_strategy);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        float width1 = (DensityUtils.getScreenW() - 6 * 3 - 10 * 2) / 2;
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
        if (model.getType() == 1) {
            GlideImageLoader.glideLoaderRaudio(mContext, model.getPicture() + "?imageView2/0/w/" + lp.width + "/h/" + lp.height, imageView, 4, (int) lp.width, (int) lp.height, R.mipmap.placeholderfigure);
        } else {
            GlideImageLoader.glideLoaderRaudio(mContext, model.getPicture()+ "/w/" + lp.width + "/h/" + lp.height, imageView, 4, (int) lp.width, (int) lp.height, R.mipmap.placeholderfigure);
        }
        helper.setText(R.id.tv_strategy, model.getPost_title());

        GlideImageLoader.glideLoader(mContext, model.getFace(), helper.getImageView(R.id.img_head), 0, R.mipmap.avatar);

        helper.setText(R.id.tv_nickName, model.getNickname());

        helper.setVisibility(R.id.ll_attention, View.GONE);

//        if (StringUtils.isEmpty(model.getConcern_number())) {
//            helper.setText(R.id.tv_attentionNum, "0");
//        } else {
//            helper.setText(R.id.tv_attentionNum, model.getConcern_number());
//        }

    }

}
