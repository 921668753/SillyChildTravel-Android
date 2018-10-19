package com.yinglan.sct.homepage.bythedaycharter.cityselect;

import com.common.cklibrary.common.KJActivityStack;
import com.common.cklibrary.utils.httputil.HttpUtilParams;
import com.common.cklibrary.utils.httputil.ResponseListener;
import com.kymjs.rxvolley.client.HttpParams;
import com.yinglan.sct.retrofit.RequestClient;

/**
 * Created by ruitu on 2016/9/24.
 */
public class CharterCitySelectPresenter implements CharterCitySelectContract.Presenter {
    private CharterCitySelectContract.View mView;

    public CharterCitySelectPresenter(CharterCitySelectContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }


    @Override
    public void getCountryAreaList(int type) {
        HttpParams httpParams = HttpUtilParams.getInstance().getHttpParams();
        httpParams.put("category", type);
        RequestClient.getCountryAreaList(KJActivityStack.create().topActivity(), httpParams, new ResponseListener<String>() {
            @Override
            public void onSuccess(String response) {
                mView.getSuccess(response, 0);
            }

            @Override
            public void onFailure(String msg) {
                mView.errorMsg(msg, 0);
            }
        });
    }

}
