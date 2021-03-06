package com.yinglan.sct.homepage.privatecustom.cityselect;

import com.common.cklibrary.common.KJActivityStack;
import com.common.cklibrary.utils.httputil.HttpUtilParams;
import com.common.cklibrary.utils.httputil.ResponseListener;
import com.kymjs.rxvolley.client.HttpParams;
import com.yinglan.sct.retrofit.RequestClient;

/**
 * Created by ruitu on 2016/9/24.
 */
public class CitySelectPresenter implements CitySelectContract.Presenter {
    private CitySelectContract.View mView;

    public CitySelectPresenter(CitySelectContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }


    @Override
    public void getCountryAreaList() {
        HttpParams httpParams = HttpUtilParams.getInstance().getHttpParams();
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
