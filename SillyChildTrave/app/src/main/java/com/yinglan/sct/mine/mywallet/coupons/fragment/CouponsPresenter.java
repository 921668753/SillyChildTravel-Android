package com.yinglan.sct.mine.mywallet.coupons.fragment;

import android.content.Context;

import com.common.cklibrary.utils.httputil.HttpUtilParams;
import com.common.cklibrary.utils.httputil.ResponseListener;
import com.kymjs.rxvolley.client.HttpParams;
import com.yinglan.sct.retrofit.RequestClient;

/**
 * Created by ruitu on 2018/9/24.
 */

public class CouponsPresenter implements CouponsContract.Presenter {

    private CouponsContract.View mView;

    public CouponsPresenter(CouponsContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }


    @Override
    public void getCoupons(Context context, int type, int page) {
        HttpParams httpParams = HttpUtilParams.getInstance().getHttpParams();
        httpParams.put("type", type);
        httpParams.put("page", page);
        RequestClient.getCouponsList(context, httpParams, new ResponseListener<String>() {
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
