package com.yinglan.sct.homepage.boutiqueline.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.cklibrary.common.BaseFragment;
import com.common.cklibrary.common.BindView;
import com.common.cklibrary.common.ViewInject;
import com.common.cklibrary.utils.JsonUtil;
import com.common.cklibrary.utils.RefreshLayoutUtil;
import com.yinglan.sct.R;
import com.yinglan.sct.adapter.homepage.boutiqueline.BoutiqueLineViewAdapter;
import com.yinglan.sct.constant.NumericConstants;

import com.yinglan.sct.entity.homepage.boutiqueline.fragment.BoutiqueLineBean;
import com.yinglan.sct.homepage.boutiqueline.BoutiqueLineActivity;
import com.yinglan.sct.homepage.boutiqueline.BoutiqueLineContract;
import com.yinglan.sct.homepage.boutiqueline.BoutiqueLinePresenter;
import com.yinglan.sct.homepage.boutiqueline.LineDetailsActivity;
import com.yinglan.sct.homepage.boutiqueline.selectcity.SelectCityActivity;
import com.yinglan.sct.loginregister.LoginActivity;
import com.yinglan.sct.utils.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.baseadapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

import static android.app.Activity.RESULT_OK;
import static com.yinglan.sct.constant.NumericConstants.RESULT_CODE_GET;

/**
 * 热门推荐列表
 */
public class HotRecommendedFragment extends BaseFragment implements BoutiqueLineContract.View, BGARefreshLayout.BGARefreshLayoutDelegate, BGAOnRVItemClickListener {

    private BoutiqueLineActivity aty;

    @BindView(id = R.id.mRefreshLayout)
    private BGARefreshLayout mRefreshLayout;

    @BindView(id = R.id.ll_selectCity, click = true)
    private LinearLayout ll_selectCity;

    @BindView(id = R.id.tv_selectCity)
    private TextView tv_selectCity;

    @BindView(id = R.id.rv)
    private RecyclerView recyclerview;

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

    private SpacesItemDecoration spacesItemDecoration = null;

    private int is_recommand = 1;

    private Thread thread = null;

    private List<BoutiqueLineBean.DataBean.ResultBean> list = null;

    private BoutiqueLineViewAdapter mAdapter;

    private int region_id = 0;

    private String region_name = "";

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        aty = (BoutiqueLineActivity) getActivity();
        return View.inflate(aty, R.layout.fragment_hotrecommended, null);
    }


    @Override
    protected void initData() {
        super.initData();
        list = new ArrayList<BoutiqueLineBean.DataBean.ResultBean>();
        mPresenter = new BoutiqueLinePresenter(this);
        spacesItemDecoration = new SpacesItemDecoration(7, 14);
        mAdapter = new BoutiqueLineViewAdapter(recyclerview);
    }


    @Override
    protected void initWidget(View parentView) {
        super.initWidget(parentView);
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
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()) {
            case R.id.ll_selectCity:
                Intent intent = new Intent(aty, SelectCityActivity.class);
                startActivityForResult(intent, RESULT_CODE_GET);
                //   showActivityForResult(aty, SelectCityActivity.class, RESULT_CODE_GET);
                break;
            case R.id.tv_button:
                if (tv_button.getText().toString().contains(getString(R.string.retry))) {
                    mRefreshLayout.beginRefreshing();
                    return;
                }
                aty.showActivity(aty, LoginActivity.class);
                break;
        }
    }


    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        Intent intent = new Intent(aty, LineDetailsActivity.class);
        intent.putExtra("id", mAdapter.getItem(position).getId());
        aty.showActivity(aty, intent);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        mMorePageNumber = NumericConstants.START_PAGE_NUMBER;
        mRefreshLayout.endRefreshing();
        showLoadingDialog(getString(R.string.dataLoad));
        ((BoutiqueLineContract.Presenter) mPresenter).getRouteList(aty, region_id, is_recommand, mMorePageNumber);
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
        ((BoutiqueLineContract.Presenter) mPresenter).getRouteList(aty, region_id, is_recommand, mMorePageNumber);
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
//        if (boutiqueLineBean.getData() == null && mMorePageNumber == NumericConstants.START_PAGE_NUMBER ||
//                boutiqueLineBean.getData().getResult() == null && mMorePageNumber == NumericConstants.START_PAGE_NUMBER ||
//                boutiqueLineBean.getData().getResult().size() <= 0 && mMorePageNumber == NumericConstants.START_PAGE_NUMBER) {
//            errorMsg(getString(R.string.noBoutiqueLine), 0);
//            return;
//        } else if (boutiqueLineBean.getData() == null && mMorePageNumber > NumericConstants.START_PAGE_NUMBER ||
//                boutiqueLineBean.getData().getResult() == null && mMorePageNumber > NumericConstants.START_PAGE_NUMBER ||
//                boutiqueLineBean.getData().getResult().size() <= 0 && mMorePageNumber > NumericConstants.START_PAGE_NUMBER) {
//            ViewInject.toast(getString(R.string.noMoreData));
//            isShowLoadingMore = false;
//            dismissLoadingDialog();
//            mRefreshLayout.endLoadingMore();
//            return;
//        }
//        if (thread != null && !thread.isAlive()) {
//            thread.interrupted();
//        }
//        thread = null;
//        thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                list.clear();
//                for (int i = 0; i < boutiqueLineBean.getData().getResult().size(); i++) {
//                    Bitmap bitmap = GlideImageLoader.load(aty, boutiqueLineBean.getData().getResult().get(i).getMain_picture());
//                    if (bitmap != null) {
//                        boutiqueLineBean.getData().getResult().get(i).setHeight(bitmap.getHeight());
//                        boutiqueLineBean.getData().getResult().get(i).setWidth(bitmap.getWidth());
//                    }
//                    list.add(boutiqueLineBean.getData().getResult().get(i));
//                }
//                mMorePageNumber = boutiqueLineBean.getData().getCurrentPageNo();
//                totalPageNumber = boutiqueLineBean.getData().getTotalPageCount();
//                aty.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (mMorePageNumber == NumericConstants.START_PAGE_NUMBER) {
//                            mRefreshLayout.endRefreshing();
//                            mAdapter.clear();
//                            mAdapter.addNewData(list);
//                        } else {
//                            mRefreshLayout.endLoadingMore();
//                            mAdapter.addMoreData(list);
//                        }
//                        dismissLoadingDialog();
//                    }
//                });
//            }
//        });
//        thread.start();

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
            aty.showActivity(aty, LoginActivity.class);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_CODE_GET && resultCode == RESULT_OK) {// 如果等于1
            region_id = data.getIntExtra("region_id", 0);
            region_name = data.getStringExtra("region_name");
            tv_selectCity.setText(region_name);
            mMorePageNumber = NumericConstants.START_PAGE_NUMBER;
            ((BoutiqueLineContract.Presenter) mPresenter).getRouteList(aty, region_id, is_recommand, mMorePageNumber);
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
