package com.yinglan.sct.homepage.boutiqueline;

import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.cklibrary.common.BaseActivity;
import com.common.cklibrary.common.BindView;
import com.common.cklibrary.utils.ActivityTitleUtils;
import com.yinglan.sct.R;
import com.yinglan.sct.constant.NumericConstants;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * 精品线路
 */
public class BoutiqueLineActivity extends BaseActivity {

    /**
     * 轮播图
     */
    @BindView(id = R.id.banner_ad)
    private BGABanner mForegroundBanner;

    @BindView(id = R.id.ll_topbar)
    private LinearLayout ll_topbar;

    @BindView(id = R.id.ll_visitCity, click = true)
    private LinearLayout ll_visitCity;

    @BindView(id = R.id.tv_visitCity)
    private TextView tv_visitCity;

    @BindView(id = R.id.ll_playNumberDays, click = true)
    private LinearLayout ll_playNumberDays;

    @BindView(id = R.id.tv_playNumberDays)
    private TextView tv_playNumberDays;

    @BindView(id = R.id.ll_travelPreferences, click = true)
    private LinearLayout ll_travelPreferences;

    @BindView(id = R.id.tv_travelPreferences)
    private TextView tv_travelPreferences;

    @BindView(id = R.id.rv_boutiqueLine)
    private RecyclerView rv_boutiqueLine;

    @BindView(id = R.id.ll_bottom)
    private LinearLayout ll_bottom;

    /**
     * 错误提示页
     */
    @BindView(id = R.id.ll_commonError)
    private LinearLayout ll_commonError;

    @BindView(id = R.id.img_err)
    private ImageView img_err;

    @BindView(id = R.id.tv_hintText)
    private TextView tv_hintText;

    @BindView(id = R.id.tv_button, click = true)
    private TextView tv_button;

    @BindView(id = R.id.ll_topbar1)
    private LinearLayout ll_topbar1;

    @BindView(id = R.id.ll_visitCity1, click = true)
    private LinearLayout ll_visitCity1;

    @BindView(id = R.id.tv_visitCity1)
    private TextView tv_visitCity1;

    @BindView(id = R.id.ll_playNumberDays1, click = true)
    private LinearLayout ll_playNumberDays1;

    @BindView(id = R.id.tv_playNumberDays1)
    private TextView tv_playNumberDays1;

    @BindView(id = R.id.ll_travelPreferences1, click = true)
    private LinearLayout ll_travelPreferences1;

    @BindView(id = R.id.tv_travelPreferences1)
    private TextView tv_travelPreferences1;

    /**
     * 当前页码
     */
    private int mMorePageNumber = NumericConstants.START_PAGE_NUMBER;

    /**
     * 总页码
     */
    private int totalPageNumber = NumericConstants.START_PAGE_NUMBER;

    /**
     * 是否加载更多
     */
    private boolean isShowLoadingMore = false;


    @Override
    public void setRootView() {
        setContentView(R.layout.activity_boutiqueline);
    }


    @Override
    public void initData() {
        super.initData();

    }


    @Override
    public void initWidget() {
        super.initWidget();
        initTitle();

    }

    /**
     * 设置标题
     */
    public void initTitle() {
        ActivityTitleUtils.initToolbar(aty, getString(R.string.selectLine), true, R.id.titlebar);
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()) {
            case R.id.ll_visitCity:
            case R.id.ll_visitCity1:
                break;
            case R.id.ll_playNumberDays:
            case R.id.ll_playNumberDays1:
                break;
            case R.id.ll_travelPreferences:
            case R.id.ll_travelPreferences1:
                break;
            case R.id.tv_button:

                break;
            default:
                break;
        }
    }


}
