package com.yinglan.sct.homepage.bythedaycharter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.cklibrary.common.BaseActivity;
import com.common.cklibrary.common.BindView;
import com.common.cklibrary.utils.ActivityTitleUtils;
import com.common.cklibrary.utils.JsonUtil;
import com.common.cklibrary.utils.RefreshLayoutUtil;
import com.yinglan.sct.R;
import com.yinglan.sct.adapter.homepage.airporttransportation.SelectProductAirportTransportationViewAdapter;

import com.yinglan.sct.constant.NumericConstants;
import com.yinglan.sct.entity.homepage.airporttransportation.SelectProductAirportTransportationBean;
import com.yinglan.sct.entity.homepage.airporttransportation.SelectProductAirportTransportationBean.DataBean;
import com.yinglan.sct.loginregister.LoginActivity;
import com.yinglan.sct.utils.GlideImageLoader;
import com.yinglan.sct.utils.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.baseadapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;


/**
 * 选择产品
 */
public class SelectProductActivity extends BaseActivity implements SelectProductContract.View, BGARefreshLayout.BGARefreshLayoutDelegate, BGAOnRVItemClickListener {

    @BindView(id = R.id.tv_selectProductName)
    private TextView tv_selectProductName;

    @BindView(id = R.id.mRefreshLayout)
    private BGARefreshLayout mRefreshLayout;

    @BindView(id = R.id.rv_productAirportTransportation)
    private RecyclerView recyclerview;

    private SelectProductAirportTransportationViewAdapter mAdapter;

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

    private int type = 0;
    private int region_id = 0;
    private List<DataBean> list = null;
    private Thread thread = null;

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_selectproduct);
    }

    @Override
    public void initData() {
        super.initData();
        list = new ArrayList<DataBean>();
        mPresenter = new SelectProductPresenter(this);
        mAdapter = new SelectProductAirportTransportationViewAdapter(recyclerview);
        region_id = getIntent().getIntExtra("region_id", 0);
        type = getIntent().getIntExtra("type", 0);
        showLoadingDialog(getString(R.string.dataLoad));
        ((SelectProductContract.Presenter) mPresenter).getProductByRegion(region_id, type);
    }

    @Override
    public void initWidget() {
        super.initWidget();
        ActivityTitleUtils.initToolbar(aty, getIntent().getStringExtra("title") + getString(R.string.product), true, R.id.titlebar);
        tv_selectProductName.setText(getIntent().getStringExtra("name"));
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
        SpacesItemDecoration spacesItemDecoration = new SpacesItemDecoration(13, 10);
        //设置item之间的间隔
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

    @Override
    public void setPresenter(SelectProductContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()) {
            case R.id.tv_button:
                if (tv_button.getText().toString().contains(getString(R.string.retry))) {
                    mRefreshLayout.beginRefreshing();
                    return;
                }
                showActivity(aty, LoginActivity.class);
                break;
        }
    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        Intent intent = new Intent(aty, PriceInformationActivity.class);
        intent.putExtra("product_id", mAdapter.getItem(position).getId());
        intent.putExtra("type", type);
        showActivity(aty, intent);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        mMorePageNumber = NumericConstants.START_PAGE_NUMBER;
        mRefreshLayout.endRefreshing();
        showLoadingDialog(getString(R.string.dataLoad));
        ((SelectProductContract.Presenter) mPresenter).getProductByRegion(region_id, type);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
//        mRefreshLayout.endLoadingMore();
//        if (!isShowLoadingMore) {
//            ViewInject.toast(getString(R.string.noMoreData));
//            return false;
//        }
//        mMorePageNumber++;
//        if (mMorePageNumber > totalPageNumber) {
//            ViewInject.toast(getString(R.string.noMoreData));
//            return false;
//        }
//        showLoadingDialog(getString(R.string.dataLoad));
//        ((SelectProductAirportTransportationContract.Presenter) mPresenter).getProductByAirportId(airport_id, type);
        return false;
    }


    @Override
    public void getSuccess(String success, int flag) {
        SelectProductAirportTransportationBean selectProductAirportTransportationBean = (SelectProductAirportTransportationBean) JsonUtil.getInstance().json2Obj(success, SelectProductAirportTransportationBean.class);
        List<SelectProductAirportTransportationBean.DataBean> selectProductAirportTransportationList = selectProductAirportTransportationBean.getData();
        if (selectProductAirportTransportationList == null || selectProductAirportTransportationList.size() <= 0) {
            errorMsg(getString(R.string.noProduct), 0);
            return;
        }
        mRefreshLayout.setVisibility(View.VISIBLE);
        ll_commonError.setVisibility(View.GONE);
        if (thread != null && !thread.isAlive()) {
            thread.interrupted();
        }
        thread = null;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                list.clear();
                for (int i = 0; i < selectProductAirportTransportationList.size(); i++) {
                    if (selectProductAirportTransportationList.get(i) == null) {
                        continue;
                    }
                    Bitmap bitmap = GlideImageLoader.load(aty, selectProductAirportTransportationList.get(i).getMain_picture());
                    if (bitmap != null) {
                        selectProductAirportTransportationList.get(i).setHeight(bitmap.getHeight());
                        selectProductAirportTransportationList.get(i).setWidth(bitmap.getWidth());
                    }
                    try {
                        list.add(selectProductAirportTransportationList.get(i));
                    } catch (Exception e) {
                    }
                }
//                mMorePageNumber = communityBean.getData().getCurrentPageNo();
//                totalPageNumber = communityBean.getData().getTotalPageCount();
                aty.runOnUiThread(new Runnable() {
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
        } else if (msg.contains(getString(R.string.noProduct))) {
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
    protected void onDestroy() {
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
