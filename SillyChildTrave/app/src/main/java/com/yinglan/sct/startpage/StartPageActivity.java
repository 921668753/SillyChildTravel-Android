package com.yinglan.sct.startpage;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.common.cklibrary.common.StringConstants;
import com.common.cklibrary.utils.JsonUtil;
import com.kymjs.common.FileUtils;
import com.kymjs.common.PreferenceHelper;
import com.kymjs.common.StringUtils;
import com.kymjs.okhttp3.OkHttpStack;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.http.RequestQueue;
import com.yinglan.sct.R;
import com.yinglan.sct.constant.NumericConstants;
import com.yinglan.sct.entity.startpage.QiNiuKeyBean;
import com.yinglan.sct.main.MainActivity;
import com.yinglan.sct.startpage.dialog.PermissionsDialog;
import com.yinglan.sct.utils.activity.BaseInstrumentedActivity;

import java.util.List;

import okhttp3.OkHttpClient;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 用户端
 * 启动页暂定为集成beasactivity
 * 若添加极光推送等需更换极光推送activity   InstrumentedActivity
 * Created by ruitu ck on 2016/9/14.
 */

public class StartPageActivity extends BaseInstrumentedActivity implements StartPageContract.View, EasyPermissions.PermissionCallbacks {
    // private LocationClient mLocationClient;
    //  private MyLocationListener myListener;

    /**
     * 高德定位
     */
    @Override
    public void setRootView() {
        setContentView(R.layout.activity_startpage);
    }

    /**
     * 设置定位
     */
    @Override
    public void initData() {
        super.initData();
        mPresenter = new StartPagePresenter(this);
//        mLocationClient = new LocationClient(aty.getApplicationContext());
//        myListener = new MyLocationListener();
//        //声明LocationClient类
//        mLocationClient.registerLocationListener(myListener);
//        //注册监听函数
//        ((StartPageContract.Presenter) mPresenter).initLocation(aty, mLocationClient);
    }

    @Override
    public void initWidget() {
        super.initWidget();
        initView();
    }

    public void initView() {
        ImageView image = new ImageView(aty);
        image.setImageResource(R.mipmap.startpage);
        Animation anim = AnimationUtils.loadAnimation(aty, R.anim.splash_start);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                readAndWriteTask();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        image.setAnimation(anim);
        setContentView(image);
    }

    private void jumpTo() {
//        startService(new Intent(aty, CommonService.class));
        boolean isFirst = PreferenceHelper.readBoolean(this, StringConstants.FILENAME, "firstOpen", true);
        Intent jumpIntent = new Intent();
        if (isFirst) {
            PreferenceHelper.write(this, StringConstants.FILENAME, "firstOpen", false);
            jumpIntent.setClass(this, GuideViewActivity.class);
        } else {
            jumpIntent.setClass(this, MainActivity.class);
            jumpIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        }
        skipActivity(aty, jumpIntent);
        overridePendingTransition(0, 0);
    }


    @AfterPermissionGranted(NumericConstants.READ_AND_WRITE_CODE)
    public void readAndWriteTask() {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CAMERA, Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS, Manifest.permission.CHANGE_WIFI_STATE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Have permissions, do the thing!
            RxVolley.setRequestQueue(RequestQueue.newRequestQueue(FileUtils.getSaveFolder(StringConstants.CACHEPATH), new OkHttpStack(new OkHttpClient())));
            LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            try {
                if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    // 未打开位置开关，可能导致定位失败或定位不准，提示用户或做相应处理
                    doFinishDialog(getString(R.string.notOpenPositionSwitchClose));
                    return;
                }
            } catch (Exception e) {
                doFinishDialog(getString(R.string.notOpenPositionSwitchClose));
                return;
            }
            PreferenceHelper.write(aty, StringConstants.FILENAME, "selectCity", "");
//            jumpTo(true);
//            ((StartPageContract.Presenter) mPresenter).getSystemMessage();
            ((StartPageContract.Presenter) mPresenter).getQiNiuKey();
        } else {
            // Ask for both permissions
            EasyPermissions.requestPermissions(this, getString(R.string.readAndWrite), NumericConstants.READ_AND_WRITE_CODE, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.d("tag", "onPermissionsDenied:" + requestCode + ":" + perms.size());
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d("tag", "onPermissionsDenied:" + requestCode + ":" + perms.size());
        if (requestCode == NumericConstants.READ_AND_WRITE_CODE) {
            doFinishDialog(getString(R.string.sdPermission));
        } else if (requestCode == NumericConstants.LOCATION_CODE) {
            doFinishDialog(getString(R.string.locationRelatedPermission));
        }
    }


    @Override
    public void setPresenter(StartPageContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void getSuccess(String success, int flag) {
        QiNiuKeyBean qiNiuKeyBean = (QiNiuKeyBean) JsonUtil.getInstance().json2Obj(success, QiNiuKeyBean.class);
        if (qiNiuKeyBean != null && !StringUtils.isEmpty(qiNiuKeyBean.getData().getAuthToken())) {
            PreferenceHelper.write(this, StringConstants.FILENAME, "qiNiuToken", qiNiuKeyBean.getData().getAuthToken());
            PreferenceHelper.write(this, StringConstants.FILENAME, "qiNiuImgHost", qiNiuKeyBean.getData().getHost());
            PreferenceHelper.write(this, StringConstants.FILENAME, "qiNiuImgTime", String.valueOf(System.currentTimeMillis()));
        }
        jumpTo();
    }

    @Override
    public void errorMsg(String msg, int flag) {
        jumpTo();
    }

    private void doFinishDialog(String content) {
        PermissionsDialog dialog = new PermissionsDialog(this) {
            @Override
            public void doAction() {
                finish();
            }
        };
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.setContent(content);
    }
}
