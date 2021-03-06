package com.yinglan.sct.adapter.community.dynamic.dynamiccomments;


import android.content.Context;
import android.view.View;

import com.yinglan.sct.R;
import com.yinglan.sct.entity.community.dynamic.dynamiccomments.DynamicCommentsBean.DataBean.ListBean;
import com.yinglan.sct.utils.GlideImageLoader;

import cn.bingoogolapple.baseadapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.baseadapter.BGAViewHolderHelper;


/**
 * 动态评论
 */
public class DynamicCommentsViewAdapter extends BGAAdapterViewAdapter<ListBean> {

    public DynamicCommentsViewAdapter(Context context) {
        super(context, R.layout.item_dynamicdetails);
    }


    @Override
    protected void setItemChildListener(BGAViewHolderHelper helper) {
        super.setItemChildListener(helper);
        helper.setItemChildClickListener(R.id.ll_giveLike);
        helper.setItemChildClickListener(R.id.tv_revert);
        helper.setItemChildClickListener(R.id.ll_revertNum);
        helper.setItemChildClickListener(R.id.tv_nickName);
        helper.setItemChildClickListener(R.id.tv_nickName1);
        helper.setItemChildClickListener(R.id.tv_nickName2);
    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, ListBean model) {

        GlideImageLoader.glideLoader(mContext, model.getFace(), helper.getImageView(R.id.img_avatar), 0, R.mipmap.avatar);

        helper.setText(R.id.tv_nickName, model.getNickname());

        helper.setText(R.id.tv_content, model.getBody());

        helper.setText(R.id.tv_time, model.getCreate_time());

        if (model.getIs_comment_like() == 1) {
            helper.setImageResource(R.id.img_giveLike, R.mipmap.dynamic_zan1);
            helper.setTextColorRes(R.id.tv_zanNum, R.color.greenColors);
        } else {
            helper.setImageResource(R.id.img_giveLike, R.mipmap.dynamic_zan);
            //  helper.setText(R.id.tv_zanNum, mContext.getString(R.string.giveLike));
            helper.setTextColorRes(R.id.tv_zanNum, R.color.tabColors);
        }
        helper.setText(R.id.tv_zanNum, model.getComment_like_number());

        if (model.getReplyList() == null || model.getReplyList().size() <= 0) {
            helper.setVisibility(R.id.ll_revert, View.GONE);
        } else {
            helper.setVisibility(R.id.ll_revert, View.VISIBLE);

            GlideImageLoader.glideLoader(mContext, model.getReplyList().get(0).getReply_face(), helper.getImageView(R.id.img_head), 0, R.mipmap.avatar);

            helper.setText(R.id.tv_nickName1, model.getReplyList().get(0).getNickname());

            helper.setText(R.id.tv_time1, model.getReplyList().get(0).getReply_time());

            helper.setText(R.id.tv_nickName2, model.getReplyList().get(0).getReply_nickname());

            helper.setText(R.id.tv_content1, model.getReplyList().get(0).getReply_body());

            if (model.getReplyList().size() > 1) {
                helper.setVisibility(R.id.ll_revertNum, View.VISIBLE);
                helper.setText(R.id.tv_revertNum, model.getReplyList().size() + "");
            } else {
                helper.setVisibility(R.id.ll_revertNum, View.GONE);
            }
        }
    }
}
