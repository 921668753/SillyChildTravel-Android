package com.yinglan.sct.homepage.boutiqueline;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.cklibrary.common.BaseActivity;
import com.common.cklibrary.common.BindView;
import com.common.cklibrary.common.ViewInject;
import com.common.cklibrary.utils.ActivityTitleUtils;
import com.common.cklibrary.utils.JsonUtil;
import com.common.cklibrary.utils.RefreshLayoutUtil;
import com.common.cklibrary.utils.myview.PopupWindow.CommonPopupWindow;
import com.yinglan.sct.R;
import com.yinglan.sct.adapter.homepage.boutiqueline.BoutiqueLineViewAdapter;
import com.yinglan.sct.constant.NumericConstants;
import com.yinglan.sct.entity.homepage.boutiqueline.fragment.BoutiqueLineBean;
import com.yinglan.sct.entity.homepage.boutiqueline.fragment.BoutiqueLineBean.DataBean.ResultBean;
import com.yinglan.sct.loginregister.LoginActivity;
import com.yinglan.sct.utils.GlideImageLoader;
import com.yinglan.sct.utils.SpacesItemDecoration;

import java.util.ArrayList;

import cn.bingoogolapple.baseadapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * 精品线路
 */
public class BoutiqueLineActivity extends BaseActivity implements BoutiqueLineContract.View, BGARefreshLayout.BGARefreshLayoutDelegate, BGAOnRVItemClickListener, CommonPopupWindow.ViewInterface {

    @BindView(id = R.id.mRefreshLayout)
    private BGARefreshLayout mRefreshLayout;

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
    private RecyclerView recyclerview;

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

    private BoutiqueLineViewAdapter mAdapter;
    private ArrayList<ResultBean> list;
    private Thread thread = null;

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_boutiqueline);
    }

    @Override
    public void initData() {
        super.initData();
        mAdapter = new BoutiqueLineViewAdapter(recyclerview);
        list = new ArrayList<ResultBean>();
    }

    @Override
    public void initWidget() {
        super.initWidget();
        initTitle();
        RefreshLayoutUtil.initRefreshLayout(mRefreshLayout, this, aty, true);
        initRecyclerView();
        mRefreshLayout.beginRefreshing();
    }

    /**
     * 设置RecyclerView控件部分属性
     */
    private void initRecyclerView() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setAutoMeasureEnabled(true);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setHasFixedSize(true);
        recyclerview.setNestedScrollingEnabled(false);
        //设置item之间的间隔
        SpacesItemDecoration spacesItemDecoration = new SpacesItemDecoration(7, 14);
        recyclerview.addItemDecoration(spacesItemDecoration);
        recyclerview.setAdapter(mAdapter);
        //    layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);//不设置的话，图片闪烁错位，有可能有整列错位的情况。
        mAdapter.setOnRVItemClickListener(this);
        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                layoutManager.invalidateSpanAssignments();
            }
        });
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
                if (tv_button.getText().toString().contains(getString(R.string.retry))) {
                    mRefreshLayout.beginRefreshing();
                    return;
                }
                showActivity(aty, LoginActivity.class);
                break;
            default:
                break;
        }
    }

    @Override
    public void getChildView(View view, int layoutResId) {
        switch (layoutResId) {
//            case R.layout.popup_down:
//                RecyclerView recycle_view = (RecyclerView) view.findViewById(R.id.recycle_view);
//                recycle_view.setLayoutManager(new GridLayoutManager(this, 3));
//                PopupAdapter mAdapter = new PopupAdapter(this);
//                recycle_view.setAdapter(mAdapter);
//                mAdapter.setOnItemClickListener(new MyOnclickListener() {
//                    @Override
//                    public void onItemClick(View view, int position) {
//                        if (popupWindow != null) {
//                            popupWindow.dismiss();
//                        }
//                    }
//                });
//                break;
        }
    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        Intent intent = new Intent(aty, LineDetailsActivity.class);
        intent.putExtra("id", mAdapter.getItem(position).getId());
        showActivity(aty, intent);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        mMorePageNumber = NumericConstants.START_PAGE_NUMBER;
        mRefreshLayout.endRefreshing();
        showLoadingDialog(getString(R.string.dataLoad));
        // ((BoutiqueLineContract.Presenter) mPresenter).getRouteList(aty, region_id, is_recommand, mMorePageNumber);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        mRefreshLayout.endLoadingMore();
        if (!isShowLoadingMore) {
            ViewInject.toast(getString(R.string.noMoreData));
            return false;
        }
        mMorePageNumber++;
        if (mMorePageNumber > totalPageNumber) {
            ViewInject.toast(getString(R.string.noMoreData));
            return false;
        }
        showLoadingDialog(getString(R.string.dataLoad));
        //  ((BoutiqueLineContract.Presenter) mPresenter).getRouteList(aty, region_id, is_recommand, mMorePageNumber);
        return true;
    }

    @Override
    public void setPresenter(BoutiqueLineContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void getSuccess(String success, int flag) {
        isShowLoadingMore = true;
        ll_commonError.setVisibility(View.GONE);
        mRefreshLayout.setVisibility(View.VISIBLE);
        mRefreshLayout.setPullDownRefreshEnable(true);
        BoutiqueLineBean boutiqueLineBean = (BoutiqueLineBean) JsonUtil.getInstance().json2Obj(success, BoutiqueLineBean.class);
        if (boutiqueLineBean.getData() == null && mMorePageNumber == NumericConstants.START_PAGE_NUMBER ||
                boutiqueLineBean.getData().getResultX() == null && mMorePageNumber == NumericConstants.START_PAGE_NUMBER ||
                boutiqueLineBean.getData().getResultX().size() <= 0 && mMorePageNumber == NumericConstants.START_PAGE_NUMBER) {
            errorMsg(getString(R.string.noBoutiqueLine), 0);
            return;
        } else if (boutiqueLineBean.getData() == null && mMorePageNumber > NumericConstants.START_PAGE_NUMBER ||
                boutiqueLineBean.getData().getResultX() == null && mMorePageNumber > NumericConstants.START_PAGE_NUMBER ||
                boutiqueLineBean.getData().getResultX().size() <= 0 && mMorePageNumber > NumericConstants.START_PAGE_NUMBER) {
            ViewInject.toast(getString(R.string.noMoreData));
            isShowLoadingMore = false;
            dismissLoadingDialog();
            mRefreshLayout.endLoadingMore();
            return;
        }
        if (thread != null && !thread.isAlive()) {
            thread.interrupted();
        }
        thread = null;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                list.clear();
                for (int i = 0; i < boutiqueLineBean.getData().getResultX().size(); i++) {
                    Bitmap bitmap = GlideImageLoader.load(aty, boutiqueLineBean.getData().getResultX().get(i).getMain_picture());
                    if (bitmap != null) {
                        boutiqueLineBean.getData().getResultX().get(i).setHeight(bitmap.getHeight());
                        boutiqueLineBean.getData().getResultX().get(i).setWidth(bitmap.getWidth());
                    }
                    list.add(boutiqueLineBean.getData().getResultX().get(i));
                }
                mMorePageNumber = boutiqueLineBean.getData().getCurrentPageNo();
                totalPageNumber = boutiqueLineBean.getData().getTotalPageCount();
               runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mMorePageNumber == NumericConstants.START_PAGE_NUMBER) {
                            mRefreshLayout.endRefreshing();
                            mAdapter.clear();
                            mAdapter.addNewData(list);
                        } else {
                            mRefreshLayout.endLoadingMore();
                            mAdapter.addMoreData(list);
                        }
                        dismissLoadingDialog();
                    }
                });
            }
        });
        thread.start();
    }

    @Override
    public void errorMsg(String msg, int flag) {
        dismissLoadingDialog();
        isShowLoadingMore = false;
        if (mMorePageNumber == NumericConstants.START_PAGE_NUMBER) {
            mRefreshLayout.endRefreshing();
        } else {
            mRefreshLayout.endLoadingMore();
        }
        mRefreshLayout.setPullDownRefreshEnable(false);
        mRefreshLayout.setVisibility(View.GONE);
        ll_commonError.setVisibility(View.VISIBLE);
        tv_hintText.setVisibility(View.VISIBLE);
        tv_button.setVisibility(View.VISIBLE);
        if (isLogin(msg)) {
            img_err.setImageResource(R.mipmap.no_login);
            tv_hintText.setVisibility(View.GONE);
            tv_button.setText(getString(R.string.login));
            showActivity(aty, LoginActivity.class);
            return;
        } else if (msg.contains(getString(R.string.checkNetwork))) {
            img_err.setImageResource(R.mipmap.no_network);
            tv_hintText.setText(msg);
            tv_button.setText(getString(R.string.retry));
        } else if (msg.contains(getString(R.string.noBoutiqueLine))) {
            img_err.setImageResource(R.mipmap.no_data);
            tv_hintText.setText(msg);
            tv_button.setVisibility(View.GONE);
        } else {
            img_err.setImageResource(R.mipmap.no_data);
            tv_hintText.setText(msg);
            tv_button.setText(getString(R.string.retry));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        list.clear();
        list = null;
        if (thread != null) {
            thread.interrupted();
        }
        thread = null;
        mAdapter.clear();
        mAdapter = null;
    }


}
