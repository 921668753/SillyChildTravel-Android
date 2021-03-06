package com.yinglan.sct.homepage.bythedaycharter.cityselect.fragment;

import android.content.Context;

import com.common.cklibrary.utils.httputil.HttpUtilParams;
import com.common.cklibrary.utils.httputil.ResponseListener;
import com.kymjs.rxvolley.client.HttpParams;
import com.yinglan.sct.retrofit.RequestClient;

public class CityClassificationPresenter implements CityClassificationContract.Presenter {

    private CityClassificationContract.View mView;

    public CityClassificationPresenter(CityClassificationContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void getCountryAreaListByParentid(Context context, int id, int type, int flag) {
        HttpParams httpParams = HttpUtilParams.getInstance().getHttpParams();
        httpParams.put("id", id);
        httpParams.put("category", type);
        RequestClient.getCountryAreaListByParentid(context, httpParams, new ResponseListener<String>() {
            @Override
            public void onSuccess(String response) {
                mView.getSuccess(response, flag);
            }

            @Override
            public void onFailure(String msg) {
                mView.errorMsg(msg, flag);
            }
        });
    }

}
