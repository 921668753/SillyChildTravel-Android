package com.yinglan.sct.mine.mycollection;

import android.content.Intent;
import android.graphics.Bitmap;
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
import com.common.cklibrary.utils.rx.MsgEvent;
import com.yinglan.sct.R;
import com.yinglan.sct.adapter.mine.mycollection.DynamicStateRVViewAdapter;
import com.yinglan.sct.community.dynamic.DynamicDetailsActivity;
import com.yinglan.sct.community.dynamic.DynamicVideoDetailsActivity;
import com.yinglan.sct.constant.NumericConstants;
import com.yinglan.sct.entity.mine.mycollection.DynamicStateBean;
import com.yinglan.sct.loginregister.LoginActivity;
import com.yinglan.sct.utils.GlideImageLoader;
import com.yinglan.sct.utils.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.baseadapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

import static android.app.Activity.RESULT_OK;
import static com.yinglan.sct.constant.NumericConstants.REQUEST_CODE_PREVIEW1;

/**
 * 我的收藏中的司导
 * Created by Administrator on 2017/9/2.
 */

public class CompanyGuideFragment extends BaseFragment implements CollectionContract.View, BGAOnRVItemClickListener, BGARefreshLayout.BGARefreshLayoutDelegate {

    @BindView(id = R.id.mRefreshLayout)
    private BGARefreshLayout mRefreshLayout;

    @BindView(id = R.id.rv)
    private RecyclerView recyclerView;

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

    private DynamicStateRVViewAdapter mAdapter;

    private MyCollectionActivity aty;
    private SpacesItemDecoration spacesItemDecoration;
    private StaggeredGridLayoutManager layoutManager;

    private int type_id = 3;

    private Thread thread = null;

    private List<DynamicStateBean.DataBean> list = null;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        aty = (MyCollectionActivity) getActivity();
        return View.inflate(aty, R.layout.fragment_dynamicstate, null);
    }

    @Override
    public void initData() {
        super.initData();
        list = new ArrayList<DynamicStateBean.DataBean>();
        mPresenter = new CollectionPresenter(this);
        mAdapter = new DynamicStateRVViewAdapter(recyclerView);
        spacesItemDecoration = new SpacesItemDecoration(7, 14);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);//不设置的话，图片闪烁错位，有可能有整列错位的情况。
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
        recyclerView.setLayoutManager(layoutManager);
        //设置item之间的间隔
        recyclerView.addItemDecoration(spacesItemDecoration);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnRVItemClickListener(this);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                layoutManager.invalidateSpanAssignments();
            }
        });
    }

    /**
     * 控件监听事件
     */
    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()) {
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
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        mMorePageNumber = NumericConstants.START_PAGE_NUMBER;
        mRefreshLayout.endRefreshing();
        showLoadingDialog(getString(R.string.dataLoad));
        ((CollectionContract.Presenter) mPresenter).getFavoriteList(mMorePageNumber, type_id);
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
        ((CollectionContract.Presenter) mPresenter).getFavoriteList(mMorePageNumber, type_id);
        return true;
    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        if (mAdapter.getItem(position).getType_id() == 1) {//动态
            Intent intent = new Intent(aty, DynamicDetailsActivity.class);
            intent.putExtra("id", mAdapter.getItem(position).getId());
            intent.putExtra("title", mAdapter.getItem(position).getPost_title());
            startActivityForResult(intent, REQUEST_CODE_PREVIEW1);
        } else if (mAdapter.getItem(position).getType_id() == 2) {//视频
            Intent intent = new Intent(aty, DynamicVideoDetailsActivity.class);
            intent.putExtra("id", mAdapter.getItem(position).getId());
            intent.putExtra("title", mAdapter.getItem(position).getPost_title());
            startActivityForResult(intent, REQUEST_CODE_PREVIEW1);
        } else if (mAdapter.getItem(position).getType_id() == 3) {//攻略
            Intent intent = new Intent(aty, DynamicDetailsActivity.class);
            intent.putExtra("id", mAdapter.getItem(position).getId());
            intent.putExtra("title", mAdapter.getItem(position).getPost_title());
            startActivityForResult(intent, REQUEST_CODE_PREVIEW1);
        }
    }

    @Override
    public void setPresenter(CollectionContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void getSuccess(String success, int flag) {
        if (flag == 0) {
            isShowLoadingMore = true;
            mRefreshLayout.setPullDownRefreshEnable(true);
            ll_commonError.setVisibility(View.GONE);
            mRefreshLayout.setVisibility(View.VISIBLE);
            DynamicStateBean dynamicStateBean = (DynamicStateBean) JsonUtil.getInstance().json2Obj(success, DynamicStateBean.class);
            if (dynamicStateBean.getData() == null && mMorePageNumber == NumericConstants.START_PAGE_NUMBER ||
                    dynamicStateBean.getData().size() <= 0 && mMorePageNumber == NumericConstants.START_PAGE_NUMBER) {
                errorMsg(getString(R.string.noCollectDynamic), 0);
                return;
            } else if (dynamicStateBean.getData() == null && mMorePageNumber > NumericConstants.START_PAGE_NUMBER ||
                    dynamicStateBean.getData().size() <= 0 && mMorePageNumber > NumericConstants.START_PAGE_NUMBER) {
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
                    for (int i = 0; i < dynamicStateBean.getData().size(); i++) {
                        Bitmap bitmap = GlideImageLoader.load(aty, dynamicStateBean.getData().get(i).getPicture());
                        if (bitmap != null) {
                            dynamicStateBean.getData().get(i).setHeight(bitmap.getHeight());
                            dynamicStateBean.getData().get(i).setWidth(bitmap.getWidth());
                        }
                        list.add(dynamicStateBean.getData().get(i));
                    }
                    //  mMorePageNumber = myCollectionBean.getData().getCurrentPageNo();
                    //    totalPageNumber = myCollectionBean.getData().getTotalPageCount();
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
            dismissLoadingDialog();
        }
    }

    @Override
    public void errorMsg(String msg, int flag) {
        dismissLoadingDialog();
        if (flag == 0) {
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
                // ViewInject.toast(getString(R.string.reloginPrompting));
                aty.showActivity(aty, LoginActivity.class);
                return;
            } else if (msg.contains(getString(R.string.checkNetwork))) {
                img_err.setImageResource(R.mipmap.no_network);
                tv_hintText.setText(msg);
                tv_button.setText(getString(R.string.retry));
            } else if (msg.contains(getString(R.string.noCollectDynamic))) {
                img_err.setImageResource(R.mipmap.no_data);
                tv_hintText.setText(msg);
                tv_button.setVisibility(View.GONE);
            } else {
                img_err.setImageResource(R.mipmap.no_data);
                tv_hintText.setText(msg);
                tv_button.setText(getString(R.string.retry));
            }
        } else {
            mRefreshLayout.setPullDownRefreshEnable(true);
            mRefreshLayout.setVisibility(View.VISIBLE);
            ll_commonError.setVisibility(View.GONE);
            ViewInject.toast(msg);
        }
    }


    /**
     * 在接收消息的时候，选择性接收消息：
     */
    @Override
    public void callMsgEvent(MsgEvent msgEvent) {
        super.callMsgEvent(msgEvent);
        if (((String) msgEvent.getData()).equals("RxBusLoginEvent") && mPresenter != null) {
            mMorePageNumber = NumericConstants.START_PAGE_NUMBER;
            ((CollectionContract.Presenter) mPresenter).getFavoriteList(mMorePageNumber, type_id);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == REQUEST_CODE_PREVIEW1 && resultCode == RESULT_OK) {
            mRefreshLayout.beginRefreshing();
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
