package com.yinglan.sct.adapter.community.search;

import android.content.Context;

import com.yinglan.sct.R;
import com.yinglan.sct.entity.community.search.RecentSearchBean.DataBean;

import cn.bingoogolapple.baseadapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.baseadapter.BGAViewHolderHelper;


/**
 * 最近搜索
 */
public class RecentSearchViewAdapter extends BGAAdapterViewAdapter<DataBean> {

    private int type = -1;

    public RecentSearchViewAdapter(Context context, int type) {
        super(context, R.layout.item_recentsearchcommunity);
        this.type = type;
    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, DataBean model) {
        if (model.getType() != type && type == 0 || model.getType() != type && type == 1) {

        } else {
            String typeStr = "";
            if (model.getType() == 0) {
                typeStr = mContext.getString(R.string.lookForSomeone);
            } else if (model.getType() == 1) {
                typeStr = mContext.getString(R.string.findArticle);
            }
            helper.setText(R.id.tv_name, model.getName() + "  (" + typeStr + ")");
        }
    }
}

