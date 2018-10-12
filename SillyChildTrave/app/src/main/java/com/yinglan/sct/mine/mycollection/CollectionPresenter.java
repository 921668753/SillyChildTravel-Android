package com.yinglan.sct.mine.mycollection;

import com.common.cklibrary.common.KJActivityStack;
import com.common.cklibrary.utils.httputil.HttpUtilParams;
import com.common.cklibrary.utils.httputil.ResponseListener;
import com.kymjs.rxvolley.client.HttpParams;
import com.yinglan.sct.retrofit.RequestClient;

/**
 * Created by ruitu on 2018/9/24.
 */

public class CollectionPresenter implements CollectionContract.Presenter {

    private CollectionContract.View mView;

    public CollectionPresenter(CollectionContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void getFavoriteList(int page, int type_id) {
        HttpParams httpParams = HttpUtilParams.getInstance().getHttpParams();
        httpParams.put("pageno", page);
        httpParams.put("type_id", type_id);
        httpParams.put("pagesize", 10);
        RequestClient.getFavoriteGoodList(KJActivityStack.create().topActivity(), httpParams, new ResponseListener<String>() {
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
