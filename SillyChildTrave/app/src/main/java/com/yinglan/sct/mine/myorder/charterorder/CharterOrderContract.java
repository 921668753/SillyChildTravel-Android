package com.yinglan.sct.mine.myorder.charterorder;

import android.content.Context;

import com.common.cklibrary.common.BasePresenter;
import com.common.cklibrary.common.BaseView;

/**
 * Created by ruitu on 2016/9/24.
 */

public interface CharterOrderContract {
    interface Presenter extends BasePresenter {
        /**
         * 获取订单信息
         */
        void getChartOrder(Context context, String status, int page);


        /**
         * 获取会员登录状态
         */
        void getIsLogin(Context context, int flag);
    }

    interface View extends BaseView<Presenter, String> {
    }

}


