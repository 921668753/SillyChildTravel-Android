package com.yinglan.sct.adapter.homepage.airporttransportation.comments;

import android.content.Context;
import android.widget.ImageView;

import com.luck.picture.lib.entity.LocalMedia;
import com.yinglan.sct.R;
import com.yinglan.sct.utils.GlideImageLoader;

import cn.bingoogolapple.baseadapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.baseadapter.BGAViewHolderHelper;


/**
 * Created by Admin on 2017/9/29.
 */

public class CharterCommentsGridViewAdapter extends BGAAdapterViewAdapter<LocalMedia> {


    public CharterCommentsGridViewAdapter(Context context) {
        super(context, R.layout.item_imgcomment);
    }


    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, LocalMedia model) {

        /**
         * 图片
         */
        GlideImageLoader.glideOrdinaryLoader(mContext, model.getPath(), (ImageView) helper.getView(R.id.img_comment), R.mipmap.placeholderfigure);
    }


}
