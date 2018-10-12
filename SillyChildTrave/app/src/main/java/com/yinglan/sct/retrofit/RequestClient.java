package com.yinglan.sct.retrofit;

import android.content.Context;

import com.common.cklibrary.common.KJActivityStack;
import com.common.cklibrary.common.StringConstants;
import com.common.cklibrary.utils.JsonUtil;
import com.common.cklibrary.utils.httputil.HttpRequest;
import com.common.cklibrary.utils.httputil.HttpUtilParams;
import com.common.cklibrary.utils.httputil.ResponseListener;
import com.common.cklibrary.utils.httputil.ResponseProgressbarListener;
import com.common.cklibrary.utils.rx.MsgEvent;
import com.common.cklibrary.utils.rx.RxBus;
import com.kymjs.common.FileUtils;
import com.kymjs.common.Log;
import com.kymjs.common.NetworkUtils;
import com.kymjs.common.PreferenceHelper;
import com.kymjs.common.StringUtils;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.kymjs.rxvolley.client.ProgressListener;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.KeyGenerator;
import com.qiniu.android.storage.Recorder;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadOptions;
import com.qiniu.android.storage.persistent.FileRecorder;
import com.yinglan.sct.R;
import com.yinglan.sct.constant.NumericConstants;
import com.yinglan.sct.constant.StringNewConstants;
import com.yinglan.sct.constant.URLConstants;
import com.yinglan.sct.entity.loginregister.LoginBean;
import com.yinglan.sct.entity.startpage.QiNiuKeyBean;
import com.yinglan.sct.message.interactivemessage.imuitl.UserUtil;
import com.yinglan.sct.retrofit.uploadimg.UploadManagerUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.common.cklibrary.utils.httputil.HttpRequest.doFailure;
import static com.common.cklibrary.utils.httputil.HttpRequest.requestPostFORMHttp;


/**
 * Created by ruitu on 2016/9/17.
 */

public class RequestClient {

    /**
     * 上传头像图片
     */
    public static void upLoadImg(Context context, String filePath, int type, ResponseListener<String> listener) {
        long nowTime = System.currentTimeMillis();
        String qiNiuImgTime = PreferenceHelper.readString(context, StringConstants.FILENAME, "qiNiuImgTime", "");
        long qiNiuImgTime1 = 0;
        if (StringUtils.isEmpty(qiNiuImgTime)) {
            qiNiuImgTime1 = 0;
        } else {
            qiNiuImgTime1 = Long.decode(qiNiuImgTime);
        }
        long refreshTime = nowTime - qiNiuImgTime1 - (8 * 60 * 60 * 1000);
        if (refreshTime <= 0) {
            upLoadImgQiNiuYun(context, filePath, type, listener);
            return;
        }
        HttpParams httpParams = HttpUtilParams.getInstance().getHttpParams();
        getQiNiuKey(context, httpParams, new ResponseListener<String>() {
            @Override
            public void onSuccess(String response) {
                QiNiuKeyBean qiNiuKeyBean = (QiNiuKeyBean) JsonUtil.getInstance().json2Obj(response, QiNiuKeyBean.class);
                if (qiNiuKeyBean == null && StringUtils.isEmpty(qiNiuKeyBean.getData().getAuthToken())) {
                    listener.onFailure(context.getString(R.string.serverReturnsDataNullJsonError));
                    return;
                }
                PreferenceHelper.write(context, StringConstants.FILENAME, "qiNiuToken", qiNiuKeyBean.getData().getAuthToken());
                PreferenceHelper.write(context, StringConstants.FILENAME, "qiNiuImgHost", qiNiuKeyBean.getData().getHost());
                PreferenceHelper.write(context, StringConstants.FILENAME, "qiNiuImgTime", String.valueOf(System.currentTimeMillis()));
                upLoadImgQiNiuYun(context, filePath, type, listener);
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });

    }


    /**
     * 获取七牛云Token
     */
    private static void upLoadImgQiNiuYun(Context context, String filePath, int type, ResponseListener<String> listener) {
        String token = PreferenceHelper.readString(context, StringConstants.FILENAME, "qiNiuToken");
        //     if (type == 0) {
        String key = null;
        Log.d("ReadFragment", "key" + key);
        if (type == 0) {
            //参数 图片路径,图片名,token,成功的回调
            UploadManagerUtil.getInstance().getUploadManager().put(filePath, key, token, new UpCompletionHandler() {
                @Override
                public void complete(String key, ResponseInfo responseInfo, JSONObject jsonObject) {
                    Log.d("ReadFragment", "key" + key + "responseInfo" + JsonUtil.obj2JsonString(responseInfo) + "jsObj:" + String.valueOf(jsonObject));
                    if (responseInfo.isOK()) {
                        String host = PreferenceHelper.readString(context, StringConstants.FILENAME, "qiNiuImgHost");
                        String headpicPath = null;
                        try {
                            headpicPath = host + jsonObject.getString("name");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onFailure(context.getString(R.string.failedUploadPicture));
                            return;
                        }
                        Log.i("ReadFragment", "complete: " + headpicPath);
                        listener.onSuccess(headpicPath);
                        return;
                    }
                    listener.onFailure(context.getString(R.string.failedUploadPicture));
                }
            }, null);
        } else if (type == 1) {
            String dirPath = FileUtils.getSaveFolder(StringConstants.CACHEPATH).getAbsolutePath();
            Recorder recorder = null;
            try {
                recorder = new FileRecorder(dirPath);
            } catch (Exception e) {
            }
            //避免记录文件冲突（特别是key指定为null时），也可自定义文件名(下方为默认实现)：
            KeyGenerator keyGen = new KeyGenerator() {
                public String gen(String key, File file) {
                    // 不必使用url_safe_base64转换，uploadManager内部会处理
                    // 该返回值可替换为基于key、文件内容、上下文的其它信息生成的文件名
                    return key + "_._" + new StringBuffer(file.getAbsolutePath()).reverse();
                }
            };
            Configuration config = new Configuration.Builder()
                    // recorder分片上传时，已上传片记录器
                    // keyGen分片上传时，生成标识符，用于片记录器区分是哪个文件的上传记录
                    .recorder(recorder, keyGen)
                    .build();
            //参数 图片路径,图片名,token,成功的回调
            UploadManagerUtil.getInstance(config).getUploadManager().put(filePath, key, token, new UpCompletionHandler() {
                @Override
                public void complete(String key, ResponseInfo responseInfo, JSONObject jsonObject) {
                    Log.d("ReadFragment", "key" + key + "responseInfo" + JsonUtil.obj2JsonString(responseInfo) + "jsObj:" + String.valueOf(jsonObject));
                    if (responseInfo.isOK()) {
                        String host = PreferenceHelper.readString(context, StringConstants.FILENAME, "qiNiuImgHost");
                        String headpicPath = null;
                        try {
                            headpicPath = host + jsonObject.getString("name");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onFailure(context.getString(R.string.failedUploadPicture));
                            return;
                        }
                        Log.i("ReadFragment", "complete: " + headpicPath);
                        listener.onSuccess(headpicPath);
                        return;
                    }
                    listener.onFailure(context.getString(R.string.failedUploadVideo));
                }
            }, new UploadOptions(new HashMap<>(), null, false, new UpProgressHandler() {
                public void progress(String key, double percent) {
                    Log.i("qiniu", key + ": " + percent);
                    int progress = (int) (percent * 100);
                    ((ResponseProgressbarListener) listener).onProgress(String.valueOf(progress));
                }
            }, new UpCancellationSignal() {
                @Override
                public boolean isCancelled() {
                    return false;
                }
            }));
        }
    }


    /**
     * 获取七牛云Token
     */

    public static void getQiNiuKey(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        doServer(context, new TokenCallback() {
            @Override
            public void execute() {
                String cookies = PreferenceHelper.readString(KJActivityStack.create().topActivity(), StringConstants.FILENAME, "Cookie", "");
                if (StringUtils.isEmpty(cookies)) {
                    listener.onFailure(NumericConstants.TOLINGIN + "");
                    return;
                }
                httpParams.putHeaders("Cookie", cookies);
                HttpRequest.requestGetHttp(context, URLConstants.QINIUKEY, httpParams, listener);
            }
        }, listener);

    }

    /**
     * 上传头像图片
     */
    public static void upLoadImg(Context context, File file, int type, UploadOptions options, ResponseListener<String> listener) {
        long nowTime = System.currentTimeMillis();
        String qiNiuImgTime = PreferenceHelper.readString(context, StringConstants.FILENAME, "qiNiuImgTime", "");
        long qiNiuImgTime1 = 0;
        if (StringUtils.isEmpty(qiNiuImgTime)) {
            qiNiuImgTime1 = 0;
        } else {
            qiNiuImgTime1 = Long.decode(qiNiuImgTime);
        }
        long refreshTime = nowTime - qiNiuImgTime1 - (8 * 60 * 60 * 1000);
        if (refreshTime <= 0) {
            upLoadImgQiNiuYun(context, file, type, options, listener);
            return;
        }
        HttpParams httpParams = HttpUtilParams.getInstance().getHttpParams();
        getQiNiuKey(context, httpParams, new ResponseListener<String>() {
            @Override
            public void onSuccess(String response) {
                QiNiuKeyBean qiNiuKeyBean = (QiNiuKeyBean) JsonUtil.getInstance().json2Obj(response, QiNiuKeyBean.class);
                if (qiNiuKeyBean == null && StringUtils.isEmpty(qiNiuKeyBean.getData().getAuthToken())) {
                    listener.onFailure(context.getString(R.string.serverReturnsDataNullJsonError));
                    return;
                }
                PreferenceHelper.write(context, StringConstants.FILENAME, "qiNiuToken", qiNiuKeyBean.getData().getAuthToken());
                PreferenceHelper.write(context, StringConstants.FILENAME, "qiNiuImgHost", qiNiuKeyBean.getData().getHost());
                PreferenceHelper.write(context, StringConstants.FILENAME, "qiNiuImgTime", String.valueOf(System.currentTimeMillis()));
                upLoadImgQiNiuYun(context, file, type, options, listener);
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });

    }


    /**
     * 获取七牛云Token
     */
    private static void upLoadImgQiNiuYun(Context context, File file, int type, UploadOptions options, ResponseListener<String> listener) {
        String token = PreferenceHelper.readString(context, StringConstants.FILENAME, "qiNiuToken");
        //     if (type == 0) {
        String key = null;
        Log.d("ReadFragment", "key" + key);
        if (type == 0) {
            //参数 图片路径,图片名,token,成功的回调
            UploadManagerUtil.getInstance().getUploadManager().put(file.getPath(), key, token, new UpCompletionHandler() {
                @Override
                public void complete(String key, ResponseInfo responseInfo, JSONObject jsonObject) {
                    Log.d("ReadFragment", "key" + key + "responseInfo" + JsonUtil.obj2JsonString(responseInfo) + "jsObj:" + String.valueOf(jsonObject));
                    if (responseInfo.isOK()) {
                        String host = PreferenceHelper.readString(context, StringConstants.FILENAME, "qiNiuImgHost");
                        String headpicPath = host + key;
                        Log.i("ReadFragment", "complete: " + headpicPath);
                        listener.onSuccess(headpicPath);
                        return;
                    }
                    listener.onFailure(context.getString(R.string.failedUploadPicture));
                }
            }, options);
        } else if (type == 1) {
            String dirPath = FileUtils.getSaveFolder(StringConstants.CACHEPATH).getAbsolutePath();
            Recorder recorder = null;
            try {
                recorder = new FileRecorder(dirPath);
            } catch (Exception e) {
            }
            //避免记录文件冲突（特别是key指定为null时），也可自定义文件名(下方为默认实现)：
            KeyGenerator keyGen = new KeyGenerator() {
                public String gen(String key, File file) {
                    // 不必使用url_safe_base64转换，uploadManager内部会处理
                    // 该返回值可替换为基于key、文件内容、上下文的其它信息生成的文件名
                    return key + "_._" + new StringBuffer(file.getAbsolutePath()).reverse();
                }
            };
            Configuration config = new Configuration.Builder()
                    // recorder分片上传时，已上传片记录器
                    // keyGen分片上传时，生成标识符，用于片记录器区分是哪个文件的上传记录
                    .recorder(recorder, keyGen)
                    .build();
            //参数 图片路径,图片名,token,成功的回调
            UploadManagerUtil.getInstance(config).getUploadManager().put(file.getPath(), key, token, new UpCompletionHandler() {
                @Override
                public void complete(String key, ResponseInfo responseInfo, JSONObject jsonObject) {
                    Log.d("ReadFragment", "key" + key + "responseInfo" + JsonUtil.obj2JsonString(responseInfo) + "jsObj:" + String.valueOf(jsonObject));
                    if (responseInfo.isOK()) {
                        String host = PreferenceHelper.readString(context, StringConstants.FILENAME, "qiNiuImgHost");
                        String headpicPath = host + key;
                        Log.i("ReadFragment", "complete: " + headpicPath);
                        listener.onSuccess(headpicPath);
                        return;
                    }
                    listener.onFailure(context.getString(R.string.failedUploadVideo));
                }
            }, options);
        }
    }

    /**
     * 根据融云token获取头像性别昵称
     */
    public static void getRongCloud(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        doServer(context, new TokenCallback() {
            @Override
            public void execute() {
                String cookies = PreferenceHelper.readString(KJActivityStack.create().topActivity(), StringConstants.FILENAME, "Cookie", "");
                if (StringUtils.isEmpty(cookies)) {
                    listener.onFailure(NumericConstants.TOLINGIN + "");
                    return;
                }
                httpParams.putHeaders("Cookie", cookies);
                HttpRequest.requestGetHttp(context, URLConstants.SYSRONGCLOUD, httpParams, listener);
            }
        }, listener);
    }

    /**
     * 登录
     */
    public static void postLogin(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        HttpRequest.requestPostFORMHttp(context, URLConstants.USERLOGIN, httpParams, listener);
    }


    /**
     * 获取第三方登录验证码
     */
    public static void postThirdCode(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        HttpRequest.requestPostFORMHttp(context, URLConstants.THIRDCODE, httpParams, listener);
    }


    /**
     * 第三方登录
     */
    public static void postThirdLogin(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        HttpRequest.requestPostFORMHttp(context, URLConstants.USERTHIRDLOGIN, httpParams, listener);
    }

    /**
     * 绑定手机
     */
    public static void postBindingPhone(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        HttpRequest.requestPostFORMHttp(context, URLConstants.REGISTER, httpParams, listener);
    }

    /**
     * 发送验证码
     */
    public static void postCaptcha(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        HttpRequest.requestPostFORMHttp(context, URLConstants.SENDREGISTER, httpParams, listener);
    }

    /**
     * 短信验证码【找回、修改密码】
     */
    public static void postSendFindCode(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        HttpRequest.requestPostFORMHttp(context, URLConstants.SENDFINFDCODE, httpParams, listener);
    }

    /**
     * 注册
     */
    public static void postRegister(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        HttpRequest.requestPostFORMHttp(context, URLConstants.REGISTER, httpParams, listener);
    }


    /**
     * 更改密码【手机】
     */
    public static void postResetpwd(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        HttpRequest.requestPostFORMHttp(context, URLConstants.USERRESTPWD, httpParams, listener);
    }

    /**
     * 获取首页信息
     */
    public static void getHomePageData(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        HttpRequest.requestGetHttp(context, URLConstants.HOMEPAGE, httpParams, false, listener);
    }

    /**
     * 城市与机场 - 获取国家列表
     */
    public static void getAirportCountryList(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        HttpRequest.requestGetHttp(context, URLConstants.AIRPOTCOUNTRYLIST, httpParams, false, listener);
    }

    /**
     * 城市与机场 - 通过国家编号获取城市与机场信息
     */
    public static void getAirportByCountryId(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        HttpRequest.requestGetHttp(context, URLConstants.AIRPOTBYCOUNTRYID, httpParams, false, listener);
    }

    /**
     * 包车服务 - 获取城市包车列表
     */
    public static void getRegionByCountryId(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        HttpRequest.requestGetHttp(context, URLConstants.REGIONBYCOUNTRYID, httpParams, false, listener);
    }

    /**
     * 精品线路 - 获取精品线路城市列表
     */
    public static void getRouteRegion(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        HttpRequest.requestGetHttp(context, URLConstants.ROUTEREGION, httpParams, false, listener);
    }

    /**
     * 精品线路 - 获取精品线路商品列表
     */
    public static void getRouteList(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        HttpRequest.requestGetHttp(context, URLConstants.ROUTELIST, httpParams, false, listener);
    }

    /**
     * 精品线路 - 线路详情
     */
    public static void getProductLineDetails(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        String cookies = PreferenceHelper.readString(context, StringConstants.FILENAME, "Cookie", "");
        if (!StringUtils.isEmpty(cookies)) {
            httpParams.putHeaders("Cookie", cookies);
        }
        HttpRequest.requestGetHttp(context, URLConstants.PRODUCTLINEDETAILS, httpParams, false, listener);
    }

    /**
     * 精品线路 - 线路详细信息
     */
    public static void getRouteDetail(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        String cookies = PreferenceHelper.readString(context, StringConstants.FILENAME, "Cookie", "");
        if (!StringUtils.isEmpty(cookies)) {
            httpParams.putHeaders("Cookie", cookies);
        }
        HttpRequest.requestGetHttp(context, URLConstants.ROUTEDETAIL, httpParams, false, listener);
    }

    /**
     * 接机产品 - 通过机场的编号来获取产品信息
     */
    public static void getProductByAirportId(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        HttpRequest.requestGetHttp(context, URLConstants.PRODUCTBYAIRPORTID, httpParams, false, listener);
    }

    /**
     * 搜索 - 获取某商品列表
     */
    public static void postProductByType(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        HttpRequest.requestPostFORMHttp(context, URLConstants.PRODUCTBYTYPE, httpParams, listener);
    }

    /**
     * 包车服务 - 通过城市的编号来获取产品信息
     */
    public static void getProductByRegion(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        HttpRequest.requestGetHttp(context, URLConstants.PRODUCTBYREGION, httpParams, false, listener);
    }


    /**
     * 接机产品 - 通过产品编号获取车辆服务信息
     */
    public static void getProductDetails(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        String cookies = PreferenceHelper.readString(context, StringConstants.FILENAME, "Cookie", "");
        if (!StringUtils.isEmpty(cookies)) {
            httpParams.putHeaders("Cookie", cookies);
        }
        HttpRequest.requestGetHttp(context, URLConstants.PRODUCTDETAILS, httpParams, false, listener);
    }

    /**
     * 接机产品 - 用户填写接机预定信息
     */
    public static void postAddRequirements(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        doServer(context, new TokenCallback() {
            @Override
            public void execute() {
                String cookies = PreferenceHelper.readString(context, StringConstants.FILENAME, "Cookie", "");
                if (StringUtils.isEmpty(cookies)) {
                    listener.onFailure(NumericConstants.TOLINGIN + "");
                    return;
                }
                httpParams.putHeaders("Cookie", cookies);
                HttpRequest.requestPostFORMHttp(context, URLConstants.ADDREQUIREMENTS, httpParams, listener);
            }
        }, listener);
    }


    /**
     * 精品线路 - 用户填写线路需求
     */
    public static void postAddRouteRequirements(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        doServer(context, new TokenCallback() {
            @Override
            public void execute() {
                String cookies = PreferenceHelper.readString(context, StringConstants.FILENAME, "Cookie", "");
                if (StringUtils.isEmpty(cookies)) {
                    listener.onFailure(NumericConstants.TOLINGIN + "");
                    return;
                }
                httpParams.putHeaders("Cookie", cookies);
                HttpRequest.requestPostFORMHttp(context, URLConstants.ADDROUTEREQUIREMENTS, httpParams, listener);
            }
        }, listener);
    }

    /**
     * 包车服务 - 用户填写包车需求
     */
    public static void postAddCarRequirements(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        doServer(context, new TokenCallback() {
            @Override
            public void execute() {
                String cookies = PreferenceHelper.readString(context, StringConstants.FILENAME, "Cookie", "");
                if (StringUtils.isEmpty(cookies)) {
                    listener.onFailure(NumericConstants.TOLINGIN + "");
                    return;
                }
                httpParams.putHeaders("Cookie", cookies);
                HttpRequest.requestPostFORMHttp(context, URLConstants.ADDCARREQUIREMENTS, httpParams, listener);
            }
        }, listener);
    }


    /**
     * 接机产品 ---支付订单
     */
    public static void getTravelOrderDetail(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        doServer(context, new TokenCallback() {
            @Override
            public void execute() {
                String cookies = PreferenceHelper.readString(context, StringConstants.FILENAME, "Cookie", "");
                if (StringUtils.isEmpty(cookies)) {
                    listener.onFailure(NumericConstants.TOLINGIN + "");
                    return;
                }
                httpParams.putHeaders("Cookie", cookies);
                HttpRequest.requestGetHttp(context, URLConstants.TRAVELORDERDETAIL, httpParams, listener);
            }
        }, listener);
    }

    /**
     * 支付订单 - 创建订单
     */
    public static void postCreateTravelOrder(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        doServer(context, new TokenCallback() {
            @Override
            public void execute() {
                String cookies = PreferenceHelper.readString(context, StringConstants.FILENAME, "Cookie", "");
                if (StringUtils.isEmpty(cookies)) {
                    listener.onFailure(NumericConstants.TOLINGIN + "");
                    return;
                }
                httpParams.putHeaders("Cookie", cookies);
                HttpRequest.requestPostFORMHttp(context, URLConstants.CREATETRAVEORDER, httpParams, listener);
            }
        }, listener);
    }

    /**
     * 订单支付信息接口
     */
    public static void getOnlinePay(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        Log.d("tag", "getOnlinePay");
        doServer(context, new TokenCallback() {
            @Override
            public void execute() {
                String cookies = PreferenceHelper.readString(context, StringConstants.FILENAME, "Cookie", "");
                if (StringUtils.isEmpty(cookies)) {
                    listener.onFailure(NumericConstants.TOLINGIN + "");
                    return;
                }
                httpParams.putHeaders("Cookie", cookies);
                HttpRequest.requestPostFORMHttp(context, URLConstants.ONLINEPAY, httpParams, listener);
            }
        }, listener);
    }

    /**
     * 首页----大洲与国家 - 获取大洲信息
     */
    public static void getCountryAreaList(Context context, HttpParams httpParams, final ResponseListener<String> listener) {
        HttpRequest.requestGetHttp(context, URLConstants.COUNTRYAREALIST, httpParams, listener);
    }

    /**
     * 首页----大洲与国家 - 获取大洲下面的数据
     */
    public static void getCountryAreaListByParentid(Context context, HttpParams httpParams, final ResponseListener<String> listener) {
        HttpRequest.requestGetHttp(context, URLConstants.COUNTRYAREALISTBYPARENTID, httpParams, listener);
    }

    /**
     * 首页----大洲与国家 - 获取用户搜索的城市
     */
    public static void getAreaByName(Context context, HttpParams httpParams, final ResponseListener<String> listener) {
        HttpRequest.requestPostFORMHttp(context, URLConstants.AREABYNAME, httpParams, listener);
    }

    /**
     * 获取偏好列表
     */
    public static void getCategoryList(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        HttpRequest.requestGetHttp(context, URLConstants.CATEGORYLIST, httpParams, false, listener);
    }

    /**
     * 用户填写定制要求
     */
    public static void postAddCustomized(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        doServer(context, new TokenCallback() {
            @Override
            public void execute() {
                String cookies = PreferenceHelper.readString(context, StringConstants.FILENAME, "Cookie", "");
                if (StringUtils.isEmpty(cookies)) {
                    listener.onFailure(NumericConstants.TOLINGIN + "");
                    return;
                }
                httpParams.putHeaders("Cookie", cookies);
                HttpRequest.requestPostFORMHttp(context, URLConstants.ADDCUSTOMIZED, httpParams, listener);
            }
        }, listener);
    }

    /**
     * 社区----分类信息列表
     */
    public static void getClassificationList(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        HttpRequest.requestGetHttp(context, URLConstants.CLASSIFITCATIONLIST, httpParams, false, listener);
    }

    /**
     * 社区----分类信息列表
     */
    public static void getPostClassificationList(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        HttpRequest.requestGetHttp(context, URLConstants.POSTCLASSIFITCATIONLIST, httpParams, false, listener);
    }

    /**
     * 社区----帖子列表
     */
    public static void getPostList(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        HttpRequest.requestPostFORMHttp(context, URLConstants.POSTLIST, httpParams, listener);
    }

    /**
     * 社区----获取帖子评论列表
     */
    public static void getEvaluationPage(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        Log.d("tag", "getPostComment");
        String cookies = PreferenceHelper.readString(context, StringConstants.FILENAME, "Cookie", "");
        if (!StringUtils.isEmpty(cookies)) {
            httpParams.putHeaders("Cookie", cookies);
        }
        HttpRequest.requestGetHttp(context, URLConstants.EVALUATIONPAGE, httpParams, listener);
    }

    /**
     * 社区----取消收藏帖子
     */
    public static void postUnfavorite(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        doServer(context, new TokenCallback() {
            @Override
            public void execute() {
                String cookies = PreferenceHelper.readString(context, StringConstants.FILENAME, "Cookie", "");
                if (StringUtils.isEmpty(cookies)) {
                    listener.onFailure(NumericConstants.TOLINGIN + "");
                    return;
                }
                httpParams.putHeaders("Cookie", cookies);
                HttpRequest.requestPostFORMHttp(context, URLConstants.UNFAVORIT, httpParams, listener);
            }
        }, listener);
    }

    /**
     * 社区----收藏帖子
     */
    public static void postFavoriteAdd(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        doServer(context, new TokenCallback() {
            @Override
            public void execute() {
                String cookies = PreferenceHelper.readString(context, StringConstants.FILENAME, "Cookie", "");
                if (StringUtils.isEmpty(cookies)) {
                    listener.onFailure(NumericConstants.TOLINGIN + "");
                    return;
                }
                httpParams.putHeaders("Cookie", cookies);
                HttpRequest.requestPostFORMHttp(context, URLConstants.FAVORITADD, httpParams, listener);
            }
        }, listener);
    }

    /**
     * 社区----检索会员的信息
     */
    public static void getMemberList(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        HttpRequest.requestPostFORMHttp(context, URLConstants.MEMBERLIST, httpParams, listener);
    }

    /**
     * 社区----获取帖子详情
     */
    public static void getDynamicDetails(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        String cookies = PreferenceHelper.readString(context, StringConstants.FILENAME, "Cookie", "");
        if (!StringUtils.isEmpty(cookies)) {
            httpParams.putHeaders("Cookie", cookies);
        }
        HttpRequest.requestGetHttp(context, URLConstants.POSTDETAIL, httpParams, listener);
    }

    /**
     * 社区----关注或取消关注
     */
    public static void postAddConcern(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        Log.d("tag", "postAddConcern");
        doServer(context, new TokenCallback() {
            @Override
            public void execute() {
                String cookies = PreferenceHelper.readString(context, StringConstants.FILENAME, "Cookie", "");
                if (StringUtils.isEmpty(cookies)) {
                    listener.onFailure(NumericConstants.TOLINGIN + "");
                    return;
                }
                httpParams.putHeaders("Cookie", cookies);
                HttpRequest.requestPostFORMHttp(context, URLConstants.ADDCONCERN, httpParams, listener);
            }
        }, listener);
    }

    /**
     * 社区----获取其他用户信息
     */
    public static void getOtherUserInfo(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        Log.d("tag", "getOtherUserInfo");
        String cookies = PreferenceHelper.readString(context, StringConstants.FILENAME, "Cookie", "");
        if (!StringUtils.isEmpty(cookies)) {
            httpParams.putHeaders("Cookie", cookies);
        }
        HttpRequest.requestGetHttp(context, URLConstants.OTHERUSERINFO, httpParams, listener);
    }

    /**
     * 社区----获取用户帖子列表
     */
    public static void getOtherUserPost(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        Log.d("tag", "getOtherUserPost");
        HttpRequest.requestGetHttp(context, URLConstants.OTHERUSERPOST, httpParams, listener);
    }

    /**
     * 获取某一个评论的详细信息
     */
    public static void getCommentDetails(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        Log.d("tag", "getCommentDetails");
        String cookies = PreferenceHelper.readString(context, StringConstants.FILENAME, "Cookie", "");
        if (!StringUtils.isEmpty(cookies)) {
            httpParams.putHeaders("Cookie", cookies);
        }
        HttpRequest.requestGetHttp(context, URLConstants.CINMENTDETAIL, httpParams, listener);
    }

    /**
     * 获取我关注的用户列表
     */
    public static void getMyConcernList(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        Log.d("tag", "getMyConcernList");
        doServer(context, new TokenCallback() {
            @Override
            public void execute() {
                String cookies = PreferenceHelper.readString(context, StringConstants.FILENAME, "Cookie", "");
                if (StringUtils.isEmpty(cookies)) {
                    listener.onFailure(NumericConstants.TOLINGIN + "");
                    return;
                }
                httpParams.putHeaders("Cookie", cookies);
                HttpRequest.requestGetHttp(context, URLConstants.MYCONCERNLIST, httpParams, listener);
            }
        }, listener);
    }


    /**
     * 社区----点赞和取消
     */
    public static void postAddLike(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        Log.d("tag", "postAddConcern");
        doServer(context, new TokenCallback() {
            @Override
            public void execute() {
                String cookies = PreferenceHelper.readString(context, StringConstants.FILENAME, "Cookie", "");
                if (StringUtils.isEmpty(cookies)) {
                    listener.onFailure(NumericConstants.TOLINGIN + "");
                    return;
                }
                httpParams.putHeaders("Cookie", cookies);
                HttpRequest.requestPostFORMHttp(context, URLConstants.ADDLIKE, httpParams, listener);
            }
        }, listener);
    }

    /**
     * 社区----给评论点赞
     */
    public static void postAddCommentLike(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        Log.d("tag", "postAddCommentLike");
        doServer(context, new TokenCallback() {
            @Override
            public void execute() {
                String cookies = PreferenceHelper.readString(context, StringConstants.FILENAME, "Cookie", "");
                if (StringUtils.isEmpty(cookies)) {
                    listener.onFailure(NumericConstants.TOLINGIN + "");
                    return;
                }
                httpParams.putHeaders("Cookie", cookies);
                HttpRequest.requestPostFORMHttp(context, URLConstants.ADDCOMMRENTLIKE, httpParams, listener);
            }
        }, listener);
    }


    /**
     * 社区----添加评论
     */
    public static void postAddComment(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        Log.d("tag", "postAddConcern");
        doServer(context, new TokenCallback() {
            @Override
            public void execute() {
                String cookies = PreferenceHelper.readString(context, StringConstants.FILENAME, "Cookie", "");
                if (StringUtils.isEmpty(cookies)) {
                    listener.onFailure(NumericConstants.TOLINGIN + "");
                    return;
                }
                httpParams.putHeaders("Cookie", cookies);
                HttpRequest.requestPostFORMHttp(context, URLConstants.ADDCOMMENT, httpParams, listener);
            }
        }, listener);
    }


    /**
     * 社区----举报用户帖子
     */
    public static void postReport(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        Log.d("tag", "postReport");
        doServer(context, new TokenCallback() {
            @Override
            public void execute() {
                String cookies = PreferenceHelper.readString(context, StringConstants.FILENAME, "Cookie", "");
                if (StringUtils.isEmpty(cookies)) {
                    listener.onFailure(NumericConstants.TOLINGIN + "");
                    return;
                }
                httpParams.putHeaders("Cookie", cookies);
                HttpRequest.requestPostFORMHttp(context, URLConstants.REPORT, httpParams, listener);
            }
        }, listener);
    }


    /**
     * 社区----获取帖子评论列表
     */
    public static void getPostComment(Context context, HttpParams httpParams, int type, ResponseListener<String> listener) {
        Log.d("tag", "getPostComment");
        String cookies = PreferenceHelper.readString(context, StringConstants.FILENAME, "Cookie", "");
        if (!StringUtils.isEmpty(cookies)) {
            httpParams.putHeaders("Cookie", cookies);
        }
        if (type == 1) {
            HttpRequest.requestPostFORMHttp(context, URLConstants.POSTCOMMENT, httpParams, listener);
        } else if (type == 2) {
            HttpRequest.requestPostFORMHttp(context, URLConstants.VIDEOCOMMENT, httpParams, listener);
        }
    }


    /**
     * 获取系统消息首页
     */
    public static void getSystemMessage(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        doServer(context, new TokenCallback() {
            @Override
            public void execute() {
                String cookies = PreferenceHelper.readString(KJActivityStack.create().topActivity(), StringConstants.FILENAME, "Cookie", "");
                if (StringUtils.isEmpty(cookies)) {
                    listener.onFailure(NumericConstants.TOLINGIN + "");
                    return;
                }
                httpParams.putHeaders("Cookie", cookies);
                HttpRequest.requestGetHttp(context, URLConstants.NEWLISTBUYTITLE, httpParams, listener);
            }
        }, listener);
    }

    /**
     * 获取消息列表
     */
    public static void getSystemMessageList(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        doServer(context, new TokenCallback() {
            @Override
            public void execute() {
                String cookies = PreferenceHelper.readString(KJActivityStack.create().topActivity(), StringConstants.FILENAME, "Cookie", "");
                if (StringUtils.isEmpty(cookies)) {
                    listener.onFailure(NumericConstants.TOLINGIN + "");
                    return;
                }
                httpParams.putHeaders("Cookie", cookies);
                HttpRequest.requestPostFORMHttp(context, URLConstants.NEWTITLE, httpParams, listener);
            }
        }, listener);
    }

    /**
     * 选中某条消息并设为已读
     */
    public static void getSystemMessageDetails(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        doServer(context, new TokenCallback() {
            @Override
            public void execute() {
                String cookies = PreferenceHelper.readString(KJActivityStack.create().topActivity(), StringConstants.FILENAME, "Cookie", "");
                if (StringUtils.isEmpty(cookies)) {
                    listener.onFailure(NumericConstants.TOLINGIN + "");
                    return;
                }
                httpParams.putHeaders("Cookie", cookies);
                HttpRequest.requestGetHttp(context, URLConstants.NEWSELECT, httpParams, listener);
            }
        }, listener);
    }

    /**
     * 活动
     */
    public static void getProcessingActivity(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        HttpRequest.requestGetHttp(context, URLConstants.PROCESSACTIVITY, httpParams, false, listener);
    }

    /**
     * 活动 往期精彩
     */
    public static void getAllActivity(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        HttpRequest.requestGetHttp(context, URLConstants.ALLACTIVITY, httpParams, false, listener);
    }

    /**
     * 获取个人信息
     */
    public static void getInfo(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        Log.d("tag", "getInfo");
        doServer(context, new TokenCallback() {
            @Override
            public void execute() {
                String cookies = PreferenceHelper.readString(context, StringConstants.FILENAME, "Cookie", "");
                if (StringUtils.isEmpty(cookies)) {
                    listener.onFailure(NumericConstants.TOLINGIN + "");
                    return;
                }
                httpParams.putHeaders("Cookie", cookies);
                HttpRequest.requestGetHttp(context, URLConstants.MEMBERINFO, httpParams, listener);
            }
        }, listener);
    }

    /**
     * 根据父id获取地址列表
     */
    public static void getAddressRegionList(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        Log.d("tag", "getAddressRegionList");
        HttpRequest.requestGetHttp(context, URLConstants.ADDRESSREGIONLIST, httpParams, listener);
    }

    /**
     * 根据父id获取地址列表
     */
    public static void getRegionList(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        Log.d("tag", "getRegionList");
        HttpRequest.requestGetHttp(context, URLConstants.REGIONLIST, httpParams, listener);
    }

    /**
     * 更新用户信息时不修改省市区
     */
    public static void postSaveInfo(Context context, HttpParams httpParams, final ResponseListener<String> listener) {
        Log.d("tag", "postSaveInfo");
        doServer(context, new TokenCallback() {
            @Override
            public void execute() {
                String cookies = PreferenceHelper.readString(context, StringConstants.FILENAME, "Cookie", "");
                if (StringUtils.isEmpty(cookies)) {
                    listener.onFailure(NumericConstants.TOLINGIN + "");
                    return;
                }
                httpParams.putHeaders("Cookie", cookies);
                HttpRequest.requestPostHttp(context, URLConstants.SAVEINFO, httpParams, listener);
            }
        }, listener);
    }

    /**
     * 获取收藏商品列表
     */
    public static void getFavoriteGoodList(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        Log.d("tag", "getFavoriteGoodList");
        doServer(context, new TokenCallback() {
            @Override
            public void execute() {
                String cookies = PreferenceHelper.readString(context, StringConstants.FILENAME, "Cookie", "");
                if (StringUtils.isEmpty(cookies)) {
                    listener.onFailure(NumericConstants.TOLINGIN + "");
                    return;
                }
                httpParams.putHeaders("Cookie", cookies);
                HttpRequest.requestGetHttp(context, URLConstants.FAVORITEGOODLIST, httpParams, listener);
            }
        }, listener);
    }


    /**
     * 获取我的粉丝列表
     */
    public static void getMyFansList(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        Log.d("tag", "getMyFansList");
        doServer(context, new TokenCallback() {
            @Override
            public void execute() {
                String cookies = PreferenceHelper.readString(context, StringConstants.FILENAME, "Cookie", "");
                if (StringUtils.isEmpty(cookies)) {
                    listener.onFailure(NumericConstants.TOLINGIN + "");
                    return;
                }
                httpParams.putHeaders("Cookie", cookies);
                HttpRequest.requestGetHttp(context, URLConstants.MYFANSLIST, httpParams, listener);
            }
        }, listener);
    }


    /**
     * 获取用户发布的帖子
     */
    public static void getUserPost(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        Log.d("tag", "getUserPost");
        doServer(context, new TokenCallback() {
            @Override
            public void execute() {
                String cookies = PreferenceHelper.readString(context, StringConstants.FILENAME, "Cookie", "");
                if (StringUtils.isEmpty(cookies)) {
                    listener.onFailure(NumericConstants.TOLINGIN + "");
                    return;
                }
                httpParams.putHeaders("Cookie", cookies);
                HttpRequest.requestGetHttp(context, URLConstants.USERPOST, httpParams, listener);
            }
        }, listener);
    }

    /**
     * 用户发布帖子
     */
    public static void postAddPost(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        Log.d("tag", "postAddPost");
        doServer(context, new TokenCallback() {
            @Override
            public void execute() {
                String cookies = PreferenceHelper.readString(context, StringConstants.FILENAME, "Cookie", "");
                if (StringUtils.isEmpty(cookies)) {
                    listener.onFailure(NumericConstants.TOLINGIN + "");
                    return;
                }
                httpParams.putHeaders("Cookie", cookies);
                HttpRequest.requestPostHttp(context, URLConstants.ADDPOST, httpParams, listener);
            }
        }, listener);
    }

    /**
     * 编辑帖子
     */
    public static void postEditPost(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        Log.d("tag", "postEditPost");
        doServer(context, new TokenCallback() {
            @Override
            public void execute() {
                String cookies = PreferenceHelper.readString(context, StringConstants.FILENAME, "Cookie", "");
                if (StringUtils.isEmpty(cookies)) {
                    listener.onFailure(NumericConstants.TOLINGIN + "");
                    return;
                }
                httpParams.putHeaders("Cookie", cookies);
                HttpRequest.requestPostHttp(context, URLConstants.EDITPOST, httpParams, listener);
            }
        }, listener);
    }

    /**
     * 用户删除帖子
     */
    public static void postDeletePost(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        Log.d("tag", "postDeletePost");
        doServer(context, new TokenCallback() {
            @Override
            public void execute() {
                String cookies = PreferenceHelper.readString(context, StringConstants.FILENAME, "Cookie", "");
                if (StringUtils.isEmpty(cookies)) {
                    listener.onFailure(NumericConstants.TOLINGIN + "");
                    return;
                }
                httpParams.putHeaders("Cookie", cookies);
                HttpRequest.requestPostFORMHttp(context, URLConstants.DELETEPOST, httpParams, listener);
            }
        }, listener);
    }


    /**
     * 包车服务 - 分页查询用户提交的订单
     */
    public static void getChartOrderList(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        Log.d("tag", "getChartOrderList");
        doServer(context, new TokenCallback() {
            @Override
            public void execute() {
                String cookies = PreferenceHelper.readString(context, StringConstants.FILENAME, "Cookie", "");
                if (StringUtils.isEmpty(cookies)) {
                    listener.onFailure(NumericConstants.TOLINGIN + "");
                    return;
                }
                httpParams.putHeaders("Cookie", cookies);
                HttpRequest.requestGetHttp(context, URLConstants.CHARTORDERLIST, httpParams, listener);
            }
        }, listener);
    }

    /**
     * 订单列表 - 查询订单详细信息
     */
    public static void getCharterOrderDetails(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        Log.d("tag", "getCharterOrderDetails");
        doServer(context, new TokenCallback() {
            @Override
            public void execute() {
                String cookies = PreferenceHelper.readString(context, StringConstants.FILENAME, "Cookie", "");
                if (StringUtils.isEmpty(cookies)) {
                    listener.onFailure(NumericConstants.TOLINGIN + "");
                    return;
                }
                httpParams.putHeaders("Cookie", cookies);
                HttpRequest.requestGetHttp(context, URLConstants.CHARTERORDERDETAILS, httpParams, listener);
            }
        }, listener);
    }

    /**
     * 订单列表 - 获取私人定制单的详细信息
     */
    public static void getCustomizeOrderDetail(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        Log.d("tag", "getCustomizeOrderDetail");
        doServer(context, new TokenCallback() {
            @Override
            public void execute() {
                String cookies = PreferenceHelper.readString(context, StringConstants.FILENAME, "Cookie", "");
                if (StringUtils.isEmpty(cookies)) {
                    listener.onFailure(NumericConstants.TOLINGIN + "");
                    return;
                }
                httpParams.putHeaders("Cookie", cookies);
                HttpRequest.requestGetHttp(context, URLConstants.CUSTOMIZEORDERDETAILS, httpParams, listener);
            }
        }, listener);
    }

    /**
     * 订单列表 - 获取要评价的商品信息
     */
    public static void getReviewProduct(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        Log.d("tag", "getReviewProduct");
        doServer(context, new TokenCallback() {
            @Override
            public void execute() {
                String cookies = PreferenceHelper.readString(context, StringConstants.FILENAME, "Cookie", "");
                if (StringUtils.isEmpty(cookies)) {
                    listener.onFailure(NumericConstants.TOLINGIN + "");
                    return;
                }
                httpParams.putHeaders("Cookie", cookies);
                HttpRequest.requestGetHttp(context, URLConstants.REVIEWPRODUCT, httpParams, listener);
            }
        }, listener);
    }

    /**
     * 订单列表 - 添加商品评价
     */
    public static void postAddProductReview(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        Log.d("tag", "postAddProductReview");
        doServer(context, new TokenCallback() {
            @Override
            public void execute() {
                String cookies = PreferenceHelper.readString(context, StringConstants.FILENAME, "Cookie", "");
                if (StringUtils.isEmpty(cookies)) {
                    listener.onFailure(NumericConstants.TOLINGIN + "");
                    return;
                }
                httpParams.putHeaders("Cookie", cookies);
                HttpRequest.requestPostHttp(context, URLConstants.ADDPRODUCTREVIEW, httpParams, listener);
            }
        }, listener);
    }

    /**
     * 获取钱包余额
     */
    public static void getMyWallet(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        Log.d("tag", "getMyWallet");
        doServer(context, new TokenCallback() {
            @Override
            public void execute() {
                String cookies = PreferenceHelper.readString(context, StringConstants.FILENAME, "Cookie", "");
                if (StringUtils.isEmpty(cookies)) {
                    listener.onFailure(NumericConstants.TOLINGIN + "");
                    return;
                }
                httpParams.putHeaders("Cookie", cookies);
                HttpRequest.requestGetHttp(context, URLConstants.PURSEGET, httpParams, listener);
            }
        }, listener);
    }

    /**
     * 获取账户钱包明细
     */
    public static void getAccountDetail(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        Log.d("tag", "getAccountDetail");
        doServer(context, new TokenCallback() {
            @Override
            public void execute() {
                String cookies = PreferenceHelper.readString(context, StringConstants.FILENAME, "Cookie", "");
                if (StringUtils.isEmpty(cookies)) {
                    listener.onFailure(NumericConstants.TOLINGIN + "");
                    return;
                }
                httpParams.putHeaders("Cookie", cookies);
                HttpRequest.requestGetHttp(context, URLConstants.PURSEDETAIL, httpParams, listener);
            }
        }, listener);
    }

    /**
     * 获取优惠券列表
     */
    public static void getCouponsList(Context context, HttpParams httpParams, final ResponseListener<String> listener) {
        Log.d("tag", "getCouponsList");
        doServer(context, new TokenCallback() {
            @Override
            public void execute() {
                String cookies = PreferenceHelper.readString(context, StringConstants.FILENAME, "Cookie", "");
                if (StringUtils.isEmpty(cookies)) {
                    listener.onFailure(NumericConstants.TOLINGIN + "");
                    return;
                }
                httpParams.putHeaders("Cookie", cookies);
                HttpRequest.requestGetHttp(context, URLConstants.COUPONS, httpParams, listener);
            }
        }, listener);
    }

    /**
     * 余额提现
     */
    public static void postWithdrawal(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        Log.d("tag", "postWithdrawal");
        doServer(context, new TokenCallback() {
            @Override
            public void execute() {
                String cookies = PreferenceHelper.readString(context, StringConstants.FILENAME, "Cookie", "");
                if (StringUtils.isEmpty(cookies)) {
                    listener.onFailure(NumericConstants.TOLINGIN + "");
                    return;
                }
                httpParams.putHeaders("Cookie", cookies);
                HttpRequest.requestPostFORMHttp(context, URLConstants.PURSECASH, httpParams, listener);
            }
        }, listener);
    }

    /**
     * 银行卡列表
     */
    public static void getMyBankCard(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        Log.d("tag", "getMyBankCard");
        doServer(context, new TokenCallback() {
            @Override
            public void execute() {
                String cookies = PreferenceHelper.readString(context, StringConstants.FILENAME, "Cookie", "");
                if (StringUtils.isEmpty(cookies)) {
                    listener.onFailure(NumericConstants.TOLINGIN + "");
                    return;
                }
                httpParams.putHeaders("Cookie", cookies);
                HttpRequest.requestGetHttp(context, URLConstants.PURSELIST, httpParams, listener);
            }
        }, listener);
    }


    /**
     * 获取银行列表
     */
    public static void getBank(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        Log.d("tag", "getBank");
        doServer(context, new TokenCallback() {
            @Override
            public void execute() {
                String cookies = PreferenceHelper.readString(context, StringConstants.FILENAME, "Cookie", "");
                if (StringUtils.isEmpty(cookies)) {
                    listener.onFailure(NumericConstants.TOLINGIN + "");
                    return;
                }
                httpParams.putHeaders("Cookie", cookies);
                HttpRequest.requestGetHttp(context, URLConstants.PURSEBANK, httpParams, listener);
            }
        }, listener);
    }


    /**
     * 删除银行卡
     */
    public static void postRemoveBank(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        Log.d("tag", "getMyBankCard");
        doServer(context, new TokenCallback() {
            @Override
            public void execute() {
                String cookies = PreferenceHelper.readString(context, StringConstants.FILENAME, "Cookie", "");
                if (StringUtils.isEmpty(cookies)) {
                    listener.onFailure(NumericConstants.TOLINGIN + "");
                    return;
                }
                httpParams.putHeaders("Cookie", cookies);
                HttpRequest.requestPostFORMHttp(context, URLConstants.PURSEREMOVE, httpParams, listener);
            }
        }, listener);
    }


    /**
     * 设置默认银行卡
     */
    public static void postPurseDefault(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        Log.d("tag", "postPurseDefault");
        doServer(context, new TokenCallback() {
            @Override
            public void execute() {
                String cookies = PreferenceHelper.readString(context, StringConstants.FILENAME, "Cookie", "");
                if (StringUtils.isEmpty(cookies)) {
                    listener.onFailure(NumericConstants.TOLINGIN + "");
                    return;
                }
                httpParams.putHeaders("Cookie", cookies);
                HttpRequest.requestPostFORMHttp(context, URLConstants.PURSEDEFAULT, httpParams, listener);
            }
        }, listener);
    }

    /**
     * 添加银行卡
     */
    public static void postAddBankCard(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        Log.d("tag", "postAddBankCard");
        doServer(context, new TokenCallback() {
            @Override
            public void execute() {
                String cookies = PreferenceHelper.readString(context, StringConstants.FILENAME, "Cookie", "");
                if (StringUtils.isEmpty(cookies)) {
                    listener.onFailure(NumericConstants.TOLINGIN + "");
                    return;
                }
                httpParams.putHeaders("Cookie", cookies);
                HttpRequest.requestPostFORMHttp(context, URLConstants.PURSEADD, httpParams, listener);
            }
        }, listener);
    }

    /**
     * 充值
     */
    public static void postRecharge(Context context, HttpParams httpParams, final ResponseListener<String> listener) {
        Log.d("tag", "postRecharge");
        doServer(context, new TokenCallback() {
            @Override
            public void execute() {
                String cookies = PreferenceHelper.readString(context, StringConstants.FILENAME, "Cookie", "");
                if (StringUtils.isEmpty(cookies)) {
                    listener.onFailure(NumericConstants.TOLINGIN + "");
                    return;
                }
                httpParams.putHeaders("Cookie", cookies);
                HttpRequest.requestPostFORMHttp(context, URLConstants.ONLINEREC, httpParams, listener);
            }
        }, listener);
    }

    /**
     * 提交意见反馈
     */
    public static void postAdvice(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        Log.d("tag", "postAddBankCard");
        doServer(context, new TokenCallback() {
            @Override
            public void execute() {
                String cookies = PreferenceHelper.readString(context, StringConstants.FILENAME, "Cookie", "");
                if (StringUtils.isEmpty(cookies)) {
                    listener.onFailure(NumericConstants.TOLINGIN + "");
                    return;
                }
                httpParams.putHeaders("Cookie", cookies);
                HttpRequest.requestPostHttp(context, URLConstants.ADVICEPOST, httpParams, listener);
            }
        }, listener);
    }


    /**
     * 获取会员登录状态
     */
    public static void getIsLogin(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        String cookies = PreferenceHelper.readString(context, StringConstants.FILENAME, "Cookie", "");
        if (StringUtils.isEmpty(cookies)) {
            /**
             * 发送消息
             */
            RxBus.getInstance().post(new MsgEvent<String>("RxBusLogOutEvent"));
            listener.onFailure(NumericConstants.TOLINGIN + "");
            return;
        }
        try {
            httpParams.putHeaders("Cookie", cookies);
        } catch (NullPointerException n) {
            listener.onFailure(NumericConstants.TOLINGIN + "");
            return;
        }
        HttpRequest.requestGetHttp(context, URLConstants.ISLOGIN, httpParams, listener);
    }

    /**
     * 退出登录
     */
    public static void postLogout(Context context, HttpParams httpParams, ResponseListener<String> listener) {
        doServer(context, new TokenCallback() {
            @Override
            public void execute() {
                String cookies = PreferenceHelper.readString(KJActivityStack.create().topActivity(), StringConstants.FILENAME, "Cookie", "");
                if (StringUtils.isEmpty(cookies)) {
                    listener.onFailure(NumericConstants.TOLINGIN + "");
                    return;
                }
                httpParams.putHeaders("Cookie", cookies);
                HttpRequest.requestPostFORMHttp(context, URLConstants.LOGOUT, httpParams, listener);
            }
        }, listener);
    }

    /**
     * 刷新Token
     */
    public static void doRefreshToken(Context context, String refreshToken, TokenCallback callback, ResponseListener listener) {
        Log.d("tag", "doRefreshToken");
        HttpParams params = HttpUtilParams.getInstance().getHttpParams();
        params.put("token", refreshToken);
        requestPostFORMHttp(context, URLConstants.REFRESHTOKEN, params, new ResponseListener<String>() {
            @Override
            public void onSuccess(String response) {
                LoginBean response1 = (LoginBean) JsonUtil.getInstance().json2Obj(response, LoginBean.class);
//                PreferenceHelper.write(context, StringConstants.FILENAME, "userId", response1.getResult().getUser_id());
//                PreferenceHelper.write(context, StringConstants.FILENAME, "accessToken", response1.getResult().getToken());
//                PreferenceHelper.write(context, StringConstants.FILENAME, "hx_user_name", response1.getResult().getHx_user_name());
//                PreferenceHelper.write(context, StringConstants.FILENAME, "expireTime", response1.getResult().getExpireTime());
//                PreferenceHelper.write(context, StringConstants.FILENAME, "timeBefore", System.currentTimeMillis() + "");
                for (int i = 0; i < unDoList.size(); i++) {
                    unDoList.get(i).execute();
                }
                unDoList.clear();
                isRefresh = false;
                callback.execute();
            }

            @Override
            public void onFailure(String msg) {
                unDoList.clear();
                isRefresh = false;
                PreferenceHelper.write(context, StringConstants.FILENAME, "userId", 0);
                PreferenceHelper.write(context, StringConstants.FILENAME, "accessToken", "");
                PreferenceHelper.write(context, StringConstants.FILENAME, "expireTime", "0");
                PreferenceHelper.write(context, StringConstants.FILENAME, "timeBefore", "0");
                listener.onFailure(NumericConstants.TOLINGIN + "");
            }
        });
    }

    /**
     * 刷新token回调
     */
    public static boolean isRefresh = false;

    public static void doServer(Context context, final TokenCallback callback, ResponseListener listener) {
        if (!NetworkUtils.isNetWorkAvailable(context)) {
            doFailure(-1, "NetWork err", listener);
            return;
        }
        Log.d("tag", "isNetWorkAvailable" + true);
        String cookies = PreferenceHelper.readString(KJActivityStack.create().topActivity(), StringConstants.FILENAME, "Cookie", "");
        if (StringUtils.isEmpty(cookies)) {
            Log.d("tag", "onFailure");
            UserUtil.clearUserInfo(context);
            if (!(context.getClass().getName().contains("MainActivity") || context.getClass().getName().contains("OrderReceivingFragment") || context.getClass().getName().contains("MineFragment"))) {
                /**
                 * 发送消息
                 */
                RxBus.getInstance().post(new MsgEvent<String>("RxBusLogOutEvent"));
            }
            listener.onFailure(NumericConstants.TOLINGIN + "");
            return;
        }
        long nowTime = System.currentTimeMillis();
        String expireTime = PreferenceHelper.readString(context, StringConstants.FILENAME, "expireTime", "");
        long expireTime1 = 0;
        if (StringUtils.isEmpty(expireTime)) {
            expireTime1 = 0;
        } else {
            expireTime1 = Long.decode(expireTime);
        }
        long refreshTime = expireTime1 * 1000 - nowTime - 200000;
        Log.d("tag", "onSuccess" + refreshTime);
        Log.d("tag", "onSuccess1" + nowTime);
        if (refreshTime >= 0) {
            if (isRefresh) {
                unDoList.add(callback);
                return;
            }
            isRefresh = true;
            String refreshToken = PreferenceHelper.readString(context, StringConstants.FILENAME, "accessToken");
            doRefreshToken(context, refreshToken, callback, listener);
        } else {
            HttpParams params = HttpUtilParams.getInstance().getHttpParams();
            getIsLogin(context, params, new ResponseListener<String>() {
                @Override
                public void onSuccess(String response) {
                    Log.d("tag", "onSuccess");
                    callback.execute();
                }

                @Override
                public void onFailure(String msg) {
                    UserUtil.clearUserInfo(context);
                    if (!(context.getClass().getName().contains("MainActivity") || context.getClass().getName().contains("MineFragment"))) {
                        /**
                         * 发送消息
                         */
                        RxBus.getInstance().post(new MsgEvent<String>("RxBusLogOutEvent"));
                    }
                    listener.onFailure(NumericConstants.TOLINGIN + "");
                }
            });
        }
    }


    public interface TokenCallback {
        void execute();
    }

    private static List<TokenCallback> unDoList = new ArrayList<>();

}


