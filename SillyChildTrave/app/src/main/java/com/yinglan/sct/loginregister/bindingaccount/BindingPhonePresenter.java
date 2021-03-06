package com.yinglan.sct.loginregister.bindingaccount;

import android.net.Uri;
import android.util.Log;

import com.common.cklibrary.common.KJActivityStack;
import com.common.cklibrary.utils.JsonUtil;
import com.common.cklibrary.utils.httputil.HttpUtilParams;
import com.common.cklibrary.utils.httputil.ResponseListener;
import com.kymjs.common.StringUtils;
import com.kymjs.rxvolley.client.HttpParams;
import com.yinglan.sct.R;
import com.yinglan.sct.entity.loginregister.LoginBean;
import com.yinglan.sct.entity.loginregister.RongCloudBean;
import com.yinglan.sct.message.interactivemessage.imuitl.UserUtil;
import com.yinglan.sct.retrofit.RequestClient;

import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

/**
 * Created by ruitu on 2017/8/24.
 */

public class BindingPhonePresenter implements BindingPhoneContract.Presenter {

    private BindingPhoneContract.View mView;

    public BindingPhonePresenter(BindingPhoneContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void postCode(String phone, String countryCode, String opt) {
        if (StringUtils.isEmpty(phone)) {
            mView.errorMsg(KJActivityStack.create().topActivity().getString(R.string.hintPhoneText), 0);
            return;
        }
        if (phone.startsWith("0")) {
            mView.errorMsg(KJActivityStack.create().topActivity().getString(R.string.hintPhoneText2), 0);
            return;
        }
        HttpParams httpParams = HttpUtilParams.getInstance().getHttpParams();
        // Map<String, Object> map = new HashMap<String, Object>();
        httpParams.put("phone", phone);
        httpParams.put("country_code", countryCode);
//        String codeI = String.valueOf(System.currentTimeMillis());
//        String codeId = CipherUtils.md5(codeI.substring(2, codeI.length() - 1));
//        httpParams.put("codeId", codeId);
//        String validationI = phone.substring(1, phone.length() - 1) + codeId.substring(3, codeId.length() - 1);
//        String validationId = CipherUtils.md5(validationI);
//        httpParams.put("validationId", validationId);
//        httpParams.put("opt", opt);
        // httpParams.putJsonParams(JsonUtil.getInstance().obj2JsonString(map).toString());
        RequestClient.postThirdCode(KJActivityStack.create().topActivity(), httpParams, new ResponseListener<String>() {
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
    public void postBindingPhone(String openid,String face, String from, String phone, String code, String recommendcode) {
        if (StringUtils.isEmpty(phone)) {
            mView.errorMsg(KJActivityStack.create().topActivity().getString(R.string.hintPhoneText), 0);
            return;
        }

        if (phone.startsWith("0")) {
            mView.errorMsg(KJActivityStack.create().topActivity().getString(R.string.hintPhoneText2), 0);
            return;
        }

        if (StringUtils.isEmpty(code)) {
            mView.errorMsg(KJActivityStack.create().topActivity().getString(R.string.errorCode), 0);
            return;
        }

        HttpParams httpParams = HttpUtilParams.getInstance().getHttpParams();
        httpParams.put("open_id", openid);
        httpParams.put("type", from);
        httpParams.put("phone", phone);
        httpParams.put("face", face);
        httpParams.put("code", code);
        httpParams.put("registration_id", JPushInterface.getRegistrationID(KJActivityStack.create().topActivity()));
        RequestClient.postBindingPhone(KJActivityStack.create().topActivity(), httpParams, new ResponseListener<String>() {
            @Override
            public void onSuccess(String response) {
                mView.getSuccess(response, 1);
            }

            @Override
            public void onFailure(String msg) {
                mView.errorMsg(msg, 0);
            }
        });
    }


    @Override
    public void loginRongYun(String rongYunToken, LoginBean bean) {
        RongIM.getInstance().logout();
        if (!StringUtils.isEmpty(rongYunToken)) {
            RongIM.connect(rongYunToken, new RongIMClient.ConnectCallback() {
                /**
                 * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
                 * 2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
                 */
                @Override
                public void onTokenIncorrect() {
                    KJActivityStack.create().topActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mView.errorMsg(KJActivityStack.create().topActivity().getString(R.string.failedCloudInformation1), 1);
                        }
                    });
                }

                /**
                 * 连接融云成功
                 *
                 * @param userid 当前 token 对应的用户 id
                 */
                @Override
                public void onSuccess(String userid) {
                    Log.i("XJ", "application--RongIM.connect--onSuccess" + userid);

                    /**
                     * 获取用户信息
                     */
                    UserUtil.saveRcTokenId(KJActivityStack.create().topActivity(), bean.getData().getRong_cloud(), userid);
                    if (RongIM.getInstance() != null && bean.getData() != null && !StringUtils.isEmpty(bean.getData().getUsername())) {
                        getRongYunUserInfo(userid);
                        return;
                    }
                    mView.errorMsg(KJActivityStack.create().topActivity().getString(R.string.loginErr1), 1);
                    //getRongYunUserInfo(userid);
                }

                /**
                 * 连接融云失败
                 *
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Log.i("XJ", "--errorCode" + errorCode);
                    mView.errorMsg(KJActivityStack.create().topActivity().getString(R.string.loginErr1), 1);
                }
            });
        }
    }


    private void getRongYunUserInfo(String userid) {
        HttpParams httpParams = HttpUtilParams.getInstance().getHttpParams();
        httpParams.put("userId", userid);
        RequestClient.getRongCloud(KJActivityStack.create().topActivity(), httpParams, new ResponseListener<String>() {
            @Override
            public void onSuccess(String response) {
                RongCloudBean rongCloudBean = (RongCloudBean) JsonUtil.json2Obj(response, RongCloudBean.class);
                if (RongIM.getInstance() != null && rongCloudBean.getData() != null && !StringUtils.isEmpty(rongCloudBean.getData().getNickname())) {
                    UserInfo userInfo = new UserInfo(userid, rongCloudBean.getData().getNickname(), Uri.parse(rongCloudBean.getData().getFace()));
                    RongIM.getInstance().setCurrentUserInfo(userInfo);
                    RongIM.getInstance().setMessageAttachedUserInfo(true);
                    mView.getSuccess("", 2);
                    return;
                }
                mView.errorMsg(KJActivityStack.create().topActivity().getString(R.string.loginErr1), 1);
            }

            @Override
            public void onFailure(String msg) {
                Log.d("RongYun", "onFailure");
                mView.errorMsg(KJActivityStack.create().topActivity().getString(R.string.loginErr1), 1);
            }
        });
    }


}
