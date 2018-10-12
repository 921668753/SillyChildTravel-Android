package com.yinglan.sct.mine.mycollection;

import android.content.Intent;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.cklibrary.common.BaseActivity;
import com.common.cklibrary.common.BaseFragment;
import com.common.cklibrary.common.BindView;
import com.common.cklibrary.utils.ActivityTitleUtils;
import com.kymjs.common.Log;
import com.yinglan.sct.R;

/**
 * 我的收藏
 * Created by Administrator on 2018/9/2.
 */

public class MyCollectionActivity extends BaseActivity {

    @BindView(id = R.id.ll_route, click = true)
    private LinearLayout ll_route;
    @BindView(id = R.id.tv_route)
    private TextView tv_route;
    @BindView(id = R.id.tv_route1)
    private TextView tv_route1;

    @BindView(id = R.id.ll_dynamicState, click = true)
    private LinearLayout ll_dynamicState;
    @BindView(id = R.id.tv_dynamicState)
    private TextView tv_dynamicState;
    @BindView(id = R.id.tv_dynamicState1)
    private TextView tv_dynamicState1;

    @BindView(id = R.id.ll_all, click = true)
    private LinearLayout ll_all;
    @BindView(id = R.id.tv_all)
    private TextView tv_all;
    @BindView(id = R.id.tv_all1)
    private TextView tv_all1;

    private BaseFragment baseFragment;
    private BaseFragment baseFragment1;
    private BaseFragment baseFragment2;

    private int chageIcon;

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_mycollection);
    }

    @Override
    public void initData() {
        super.initData();
        baseFragment = new BoutiqueLineFragment();
        baseFragment1 = new DynamicStateFragment();
        baseFragment2 = new CompanyGuideFragment();
        chageIcon = getIntent().getIntExtra("chageIcon", 0);
    }

    @Override
    public void initWidget() {
        super.initWidget();
        initTitle();
        cleanColors(chageIcon);
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()) {
            case R.id.ll_route:
                chageIcon = 0;
                cleanColors(chageIcon);
                break;
            case R.id.ll_dynamicState:
                chageIcon = 1;
                cleanColors(chageIcon);
                break;
            case R.id.ll_all:
                chageIcon = 2;
                cleanColors(chageIcon);
                break;
        }
    }

    public void changeFragment(BaseFragment targetFragment) {
        super.changeFragment(R.id.fl_mycollection, targetFragment);
    }

    /**
     * 设置标题
     */
    public void initTitle() {
        ActivityTitleUtils.initToolbar(aty, getString(R.string.myCollection), true, R.id.titlebar);
    }

    /**
     * Activity的启动模式变为singleTask
     * 调用onNewIntent(Intent intent)方法。
     * Fragment调用的时候，一定要在onResume方法中。
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        int newChageIcon = intent.getIntExtra("newChageIcon", 0);
        Log.d("newChageIcon", newChageIcon + "");
        if (newChageIcon == 0) {
            setSimulateClick(ll_route, 160, 100);
        } else if (newChageIcon == 1) {
            setSimulateClick(ll_dynamicState, 160, 100);
        } else {
            setSimulateClick(ll_all, 160, 100);
        }
    }

    /**
     * 模拟点击
     *
     * @param view
     * @param x
     * @param y
     */
    private void setSimulateClick(View view, float x, float y) {
        long downTime = SystemClock.uptimeMillis();
        final MotionEvent downEvent = MotionEvent.obtain(downTime, downTime,
                MotionEvent.ACTION_DOWN, x, y, 0);
        downTime += 1000;
        final MotionEvent upEvent = MotionEvent.obtain(downTime, downTime,
                MotionEvent.ACTION_UP, x, y, 0);
        view.onTouchEvent(downEvent);
        view.onTouchEvent(upEvent);
        downEvent.recycle();
        upEvent.recycle();
    }


    /**
     * 清除颜色，并添加颜色
     */
    @SuppressWarnings("deprecation")
    public void cleanColors(int chageIcon) {
        tv_route.setTextColor(getResources().getColor(R.color.textColor));
        tv_route1.setBackgroundResource(R.color.whiteColors);

        tv_dynamicState.setTextColor(getResources().getColor(R.color.textColor));
        tv_dynamicState1.setBackgroundResource(R.color.whiteColors);

        tv_all.setTextColor(getResources().getColor(R.color.textColor));
        tv_all1.setBackgroundResource(R.color.whiteColors);
        switch (chageIcon) {
            case 0:
                tv_route.setTextColor(getResources().getColor(R.color.greenColors));
                tv_route1.setBackgroundResource(R.color.greenColors);
                changeFragment(baseFragment);
                break;
            case 1:
                tv_dynamicState.setTextColor(getResources().getColor(R.color.greenColors));
                tv_dynamicState1.setBackgroundResource(R.color.greenColors);
                changeFragment(baseFragment1);
                break;
            case 2:
                tv_all.setTextColor(getResources().getColor(R.color.greenColors));
                tv_all1.setBackgroundResource(R.color.greenColors);
                changeFragment(baseFragment2);
                break;
            default:
                tv_all.setTextColor(getResources().getColor(R.color.greenColors));
                tv_all1.setBackgroundResource(R.color.greenColors);
                changeFragment(baseFragment2);
                break;
        }
    }

    public int getChageIcon() {
        return chageIcon;
    }

}
