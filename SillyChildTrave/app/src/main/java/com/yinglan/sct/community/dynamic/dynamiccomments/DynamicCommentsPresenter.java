package com.yinglan.sct.community.dynamic.dynamiccomments;

import com.common.cklibrary.common.KJActivityStack;
import com.common.cklibrary.utils.httputil.HttpUtilParams;
import com.common.cklibrary.utils.httputil.ResponseListener;
import com.kymjs.common.StringUtils;
import com.kymjs.rxvolley.client.HttpParams;
import com.yinglan.sct.R;
import com.yinglan.sct.retrofit.RequestClient;

/**
 * Created by ruitu on 2018/9/24.
 */
public class DynamicCommentsPresenter implements DynamicCommentsContract.Presenter {
    private DynamicCommentsContract.View mView;

    public DynamicCommentsPresenter(DynamicCommentsContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void getPostComment(int post_id, int pageno, int type) {
        HttpParams httpParams = HttpUtilParams.getInstance().getHttpParams();
        httpParams.put("post_id", post_id);
        httpParams.put("pageno", pageno);
        httpParams.put("type", type);
        httpParams.put("pagesize", 10);
        RequestClient.getPostComment(KJActivityStack.create().topActivity(), httpParams, type, new ResponseListener<String>() {
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

    @Override
    public void postAddCommentLike(int id, int type) {
        HttpParams httpParams = HttpUtilParams.getInstance().getHttpParams();
        httpParams.put("comment_id", id);
        httpParams.put("type", type);
        RequestClient.postAddCommentLike(KJActivityStack.create().topActivity(), httpParams, new ResponseListener<String>() {
            @Override
            public void onSuccess(String response) {
                mView.getSuccess(response, 1);
            }

            @Override
            public void onFailure(String msg) {
                mView.errorMsg(msg, 1);
            }
        });
    }

    @Override
    public void postAddComment(String body, int post_id, int reply_comment_id, int reply_member_id, int type) {
        if (StringUtils.isEmpty(body)) {
            mView.errorMsg(KJActivityStack.create().topActivity().getString(R.string.writeComment1), 2);
            return;
        }
        HttpParams httpParams = HttpUtilParams.getInstance().getHttpParams();
        httpParams.put("body", body);
        httpParams.put("post_id", post_id);
        httpParams.put("reply_comment_id", reply_comment_id);
        httpParams.put("reply_member_id", reply_member_id);
        httpParams.put("type", type);
        RequestClient.postAddComment(KJActivityStack.create().topActivity(), httpParams, new ResponseListener<String>() {
            @Override
            public void onSuccess(String response) {
                mView.getSuccess(response, 2);
            }

            @Override
            public void onFailure(String msg) {
                mView.errorMsg(msg, 2);
            }
        });

    }


}
