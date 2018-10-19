package com.yinglan.sct.homepage.bythedaycharter.cityselect.fragment;

import android.content.Context;

import com.common.cklibrary.common.BasePresenter;
import com.common.cklibrary.common.BaseView;

public interface CityClassificationContract {

    interface Presenter extends BasePresenter {

        /**
         * 获取大洲下面的数据
         */
        void getCountryAreaListByParentid(Context context, int id, int type, int flag);

    }

    interface View extends BaseView<Presenter, String> {
    }
}
