package com.yinglan.sct.community.search;

import com.common.cklibrary.common.BasePresenter;
import com.common.cklibrary.common.BaseView;

/**
 * Created by ruitu on 2016/9/24.
 */

public interface SearchPeopleContract {

    interface Presenter extends BasePresenter {

        /**
         * 搜索发现
         */
        void getMemberList(String name, int pageno);

    }

    interface View extends BaseView<Presenter, String> {
    }

}


