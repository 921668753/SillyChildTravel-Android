package com.yinglan.sct.mine.myorder;

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
import com.yinglan.sct.mine.myorder.charterorder.AllCharterFragment;
import com.yinglan.sct.mine.myorder.charterorder.CompletedCharterFragment;
import com.yinglan.sct.mine.myorder.charterorder.EvaluateCharterFragment;
import com.yinglan.sct.mine.myorder.charterorder.ObligationCharterFragment;
import com.yinglan.sct.mine.myorder.charterorder.OngoingCharterFragment;

/**
 * 我的订单
 * Created by Administrator on 2017/9/2.
 */
public class MyOrderActivity extends BaseActivity {

    @BindView(id = R.id.ll_obligation, click = true)
    private LinearLayout ll_obligation;
    @BindView(id = R.id.tv_obligation)
    private TextView tv_obligation;
    @BindView(id = R.id.tv_obligation1)
    private TextView tv_obligation1;

    @BindView(id = R.id.ll_ongoing, click = true)
    private LinearLayout ll_ongoing;
    @BindView(id = R.id.tv_ongoing)
    private TextView tv_ongoing;
    @BindView(id = R.id.tv_ongoing1)
    private TextView tv_ongoing1;

    @BindView(id = R.id.ll_evaluate, click = true)
    private LinearLayout ll_evaluate;
    @BindView(id = R.id.tv_evaluate)
    private TextView tv_evaluate;
    @BindView(id = R.id.tv_evaluate1)
    private TextView tv_evaluate1;

    @BindView(id = R.id.ll_completed, click = true)
    private LinearLayout ll_completed;
    @BindView(id = R.id.tv_completed)
    private TextView tv_completed;
    @BindView(id = R.id.tv_completed1)
    private TextView tv_completed1;

    @BindView(id = R.id.ll_all, click = true)
    private LinearLayout ll_all;
    @BindView(id = R.id.tv_all)
    private TextView tv_all;
    @BindView(id = R.id.tv_all1)
    private TextView tv_all1;

    private ObligationCharterFragment obligationCharterFragment;
    private OngoingCharterFragment ongoingCharterFragment;
    private EvaluateCharterFragment evaluateCharterFragment;
    private CompletedCharterFragment completedCharterFragment;
    private AllCharterFragment allCharterFragment;
    private int chageIcon;//0:待发货，1：进行中，2：待评价，3：完成，4：全部

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_myorder);
    }

    @Override
    public void initData() {
        super.initData();
        obligationCharterFragment = new ObligationCharterFragment();
        ongoingCharterFragment = new OngoingCharterFragment();
        evaluateCharterFragment = new EvaluateCharterFragment();
        completedCharterFragment = new CompletedCharterFragment();
        allCharterFragment = new AllCharterFragment();
        chageIcon = aty.getIntent().getIntExtra("chageIcon", 0);
    }

    @Override
    public void initWidget() {
        super.initWidget();
        initTitle();
        cleanColors(chageIcon);
    }

    /**
     * 设置标题
     */
    public void initTitle() {
        ActivityTitleUtils.initToolbar(aty, getString(R.string.myOrder), true, R.id.titlebar);
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
            setSimulateClick(ll_obligation, 160, 100);
        } else if (newChageIcon == 1) {
            setSimulateClick(ll_ongoing, 160, 100);
        } else if (newChageIcon == 2) {
            setSimulateClick(ll_evaluate, 160, 100);
        } else if (newChageIcon == 3) {
            setSimulateClick(ll_completed, 160, 100);
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

    public void changeFragment(BaseFragment targetFragment) {
        super.changeFragment(R.id.order_content, targetFragment);
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()) {
            case R.id.ll_obligation:
                chageIcon = 0;
                cleanColors(chageIcon);
                break;
            case R.id.ll_ongoing:
                chageIcon = 1;
                cleanColors(chageIcon);
                break;
            case R.id.ll_evaluate:
                chageIcon = 2;
                cleanColors(chageIcon);
                break;
            case R.id.ll_completed:
                chageIcon = 3;
                cleanColors(chageIcon);
                break;
            case R.id.ll_all:
                chageIcon = 4;
                cleanColors(chageIcon);
                break;
        }

    }

    /**
     * 清除颜色，并添加颜色
     */
    @SuppressWarnings("deprecation")
    public void cleanColors(int chageIcon) {
        tv_obligation.setTextColor(getResources().getColor(R.color.textColor));
        tv_obligation1.setBackgroundResource(R.color.whiteColors);

        tv_ongoing.setTextColor(getResources().getColor(R.color.textColor));
        tv_ongoing1.setBackgroundResource(R.color.whiteColors);

        tv_evaluate.setTextColor(getResources().getColor(R.color.textColor));
        tv_evaluate1.setBackgroundResource(R.color.whiteColors);

        tv_completed.setTextColor(getResources().getColor(R.color.textColor));
        tv_completed1.setBackgroundResource(R.color.whiteColors);

        tv_all.setTextColor(getResources().getColor(R.color.textColor));
        tv_all1.setBackgroundResource(R.color.whiteColors);

        if (chageIcon == 0) {
            tv_obligation.setTextColor(getResources().getColor(R.color.greenColors));
            tv_obligation1.setBackgroundResource(R.color.greenColors);
            changeFragment(obligationCharterFragment);
        } else if (chageIcon == 1) {
            tv_ongoing.setTextColor(getResources().getColor(R.color.greenColors));
            tv_ongoing1.setBackgroundResource(R.color.greenColors);
            changeFragment(ongoingCharterFragment);
        } else if (chageIcon == 2) {
            tv_evaluate.setTextColor(getResources().getColor(R.color.greenColors));
            tv_evaluate1.setBackgroundResource(R.color.greenColors);
            changeFragment(evaluateCharterFragment);
        } else if (chageIcon == 3) {
            tv_completed.setTextColor(getResources().getColor(R.color.greenColors));
            tv_completed1.setBackgroundResource(R.color.greenColors);
            changeFragment(completedCharterFragment);
        } else {
            tv_all.setTextColor(getResources().getColor(R.color.greenColors));
            tv_all1.setBackgroundResource(R.color.greenColors);
            changeFragment(allCharterFragment);
        }
    }

    public int getChageIcon() {
        return chageIcon;
    }
}

