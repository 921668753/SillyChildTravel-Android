package com.yinglan.sct.mine.personaldata.setsignature;

import android.text.TextUtils;

import com.common.cklibrary.common.KJActivityStack;
import com.common.cklibrary.utils.JsonUtil;
import com.common.cklibrary.utils.httputil.HttpUtilParams;
import com.common.cklibrary.utils.httputil.ResponseListener;
import com.kymjs.rxvolley.client.HttpParams;
import com.yinglan.sct.R;
import com.yinglan.sct.retrofit.RequestClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ruitu on 2016/9/24.
 */

public class SetSignaturePresenter implements SetSignatureContract.Presenter {
    private SetSignatureContract.View mView;

    public SetSignaturePresenter(SetSignatureContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void setSignature(String personalized_signature) {
        if (TextUtils.isEmpty(personalized_signature)) {
            mView.errorMsg(KJActivityStack.create().topActivity().getString(R.string.fillSignature), 0);
            return;
        }
        HttpParams httpParams = HttpUtilParams.getInstance().getHttpParams();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("signature", personalized_signature);
        httpParams.putJsonParams(JsonUtil.obj2JsonString(map));
        RequestClient.postSaveInfo(KJActivityStack.create().topActivity(), httpParams, new ResponseListener<String>() {
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
