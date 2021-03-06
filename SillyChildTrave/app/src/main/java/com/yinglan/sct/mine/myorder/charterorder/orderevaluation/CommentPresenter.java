package com.yinglan.sct.mine.myorder.charterorder.orderevaluation;

import com.common.cklibrary.common.KJActivityStack;
import com.common.cklibrary.common.StringConstants;
import com.common.cklibrary.utils.JsonUtil;
import com.common.cklibrary.utils.httputil.HttpUtilParams;
import com.common.cklibrary.utils.httputil.ResponseListener;
import com.common.cklibrary.utils.httputil.ResponseProgressbarListener;
import com.kymjs.common.PreferenceHelper;
import com.kymjs.common.StringUtils;
import com.kymjs.rxvolley.client.HttpParams;
import com.luck.picture.lib.entity.LocalMedia;
import com.yinglan.sct.R;
import com.yinglan.sct.retrofit.RequestClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ruitu on 2018/9/24.
 */
public class CommentPresenter implements CommentContract.Presenter {
    private CommentContract.View mView;

    public CommentPresenter(CommentContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }


    @Override
    public void getReviewProduct(String order_number) {
        HttpParams httpParams = HttpUtilParams.getInstance().getHttpParams();
        httpParams.put("order_number", order_number);
        RequestClient.getReviewProduct(KJActivityStack.create().topActivity(), httpParams, new ResponseListener<String>() {
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
    public void postAddProductReview(String order_number, int time_degree, int play_experience, int service_attitude, String content, List<LocalMedia> selectList) {
        if (time_degree <= 0) {
            mView.errorMsg(KJActivityStack.create().topActivity().getString(R.string.degreeOnTime1), 1);
            return;
        }
        if (play_experience <= 0) {
            mView.errorMsg(KJActivityStack.create().topActivity().getString(R.string.reasonableDistance1), 1);
            return;
        }
        if (service_attitude <= 0) {
            mView.errorMsg(KJActivityStack.create().topActivity().getString(R.string.serviceAttitude1), 1);
            return;
        }
        if (selectList.size() <= 0) {
            List<String> listStr = new ArrayList<String>();
            postAddProductReview1(order_number, time_degree, play_experience, service_attitude, content, listStr);
            return;
        }
        PreferenceHelper.write(KJActivityStack.create().topActivity(), StringConstants.FILENAME, "selectListSize", 0);
        List<String> listStr = new ArrayList<String>();
        for (int i = 0; i < selectList.size(); i++) {
            if (StringUtils.isEmpty(selectList.get(i).getPath())) {
                mView.errorMsg(KJActivityStack.create().topActivity().getString(R.string.noData), 1);
                return;
            }
            listStr.add("");
            //参数 图片路径,图片名,token,成功的回调
            int finalI = i;
            RequestClient.upLoadImg(KJActivityStack.create().topActivity(), selectList.get(i).getPath(), 1, new ResponseProgressbarListener<String>() {
                @Override
                public void onProgress(String progress) {
                    //  mView.showLoadingDialog(KJActivityStack.create().topActivity().getString(R.string.crossLoad) + progress + "%");
                }

                @Override
                public void onSuccess(String response) {
                    listStr.set(finalI, response);
                    int selectListSize = PreferenceHelper.readInt(KJActivityStack.create().topActivity(), StringConstants.FILENAME, "selectListSize", 0);
                    selectListSize = selectListSize + 1;
                    PreferenceHelper.write(KJActivityStack.create().topActivity(), StringConstants.FILENAME, "selectListSize", selectListSize);
                    if (selectListSize == selectList.size()) {
                        postAddProductReview1(order_number, time_degree, play_experience, service_attitude, content, listStr);
                    }
                }

                @Override
                public void onFailure(String msg) {
                    KJActivityStack.create().topActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mView.errorMsg(msg, 1);
                            return;
                        }
                    });
                }
            });
        }
    }

    private void postAddProductReview1(String order_number, int time_degree, int play_experience, int service_attitude, String content, List<String> selectList) {
        if (time_degree <= 0) {
            mView.errorMsg(KJActivityStack.create().topActivity().getString(R.string.degreeOnTime1), 1);
            return;
        }
        if (play_experience <= 0) {
            mView.errorMsg(KJActivityStack.create().topActivity().getString(R.string.reasonableDistance1), 1);
            return;
        }
        if (service_attitude <= 0) {
            mView.errorMsg(KJActivityStack.create().topActivity().getString(R.string.serviceAttitude1), 1);
            return;
        }
        String imgsStr = "";
        if (selectList.size() > 0) {
            for (int i = 0; i < selectList.size(); i++) {
                imgsStr = imgsStr + "," + selectList.get(i);
            }
        }
        HttpParams httpParams = HttpUtilParams.getInstance().getHttpParams();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("play_experience", String.valueOf(play_experience));
        map.put("service_attitude", String.valueOf(service_attitude));
        map.put("content", content);
        map.put("order_number", order_number);
        map.put("time_degree", String.valueOf(time_degree));
        if (!StringUtils.isEmpty(imgsStr)) {
            map.put("picture", imgsStr.substring(1));
        }
        httpParams.putJsonParams(JsonUtil.getInstance().obj2JsonString(map));
        RequestClient.postAddProductReview(KJActivityStack.create().topActivity(), httpParams, new ResponseListener<String>() {
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


}

