package com.yinglan.sct.community.search;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.common.cklibrary.common.BaseActivity;
import com.common.cklibrary.common.BindView;
import com.common.cklibrary.common.ViewInject;
import com.common.cklibrary.utils.ActivityTitleUtils;
import com.common.cklibrary.utils.JsonUtil;
import com.common.cklibrary.utils.RefreshLayoutUtil;
import com.yinglan.sct.R;
import com.yinglan.sct.adapter.community.search.SearchPeopleViewAdapter;
import com.yinglan.sct.community.DisplayPageActivity;
import com.yinglan.sct.constant.NumericConstants;
import com.yinglan.sct.entity.community.search.SearchPeopleBean;
import com.yinglan.sct.loginregister.LoginActivity;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

import static com.yinglan.sct.constant.NumericConstants.REQUEST_CODE;

/**
 * 找人
 */
public class SearchPeopleActivity extends BaseActivity implements SearchPeopleContract.View, BGARefreshLayout.BGARefreshLayoutDelegate, AdapterView.OnItemClickListener {

    @BindView(id = R.id.ll_search, click = true)
    private LinearLayout ll_search;

    @BindView(id = R.id.mRefreshLayout)
    private BGARefreshLayout mRefreshLayout;

    @BindView(id = R.id.lv)
    private ListView lv;

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

    private SearchPeopleViewAdapter mAdapter;

    private String name = "";

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_searchpeople);
    }


    @Override
    public void initData() {
        super.initData();
        name = getIntent().getStringExtra("name");
        mPresenter = new SearchPeoplePresenter(this);
        mAdapter = new SearchPeopleViewAdapter(this);
    }

    @Override
    public void initWidget() {
        super.initWidget();
        ActivityTitleUtils.initToolbar(aty, getString(R.string.lookForSomeone), true, R.id.titlebar);
        RefreshLayoutUtil.initRefreshLayout(mRefreshLayout, this, aty, true);
        lv.setAdapter(mAdapter);
        lv.setOnItemClickListener(this);
        mRefreshLayout.beginRefreshing();
    }


    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()) {
            case R.id.ll_search:
                Intent intent = new Intent(aty, CommunitySearchActivity.class);
                intent.putExtra("type", 0);
                startActivityForResult(intent, REQUEST_CODE);
                break;
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(aty, DisplayPageActivity.class);
        //   intent.putExtra("user_id", mAdapter.getItem(position).getMember_id());
        intent.putExtra("isRefresh", 0);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        mMorePageNumber = NumericConstants.START_PAGE_NUMBER;
        mRefreshLayout.endRefreshing();
        showLoadingDialog(getString(R.string.dataLoad));
        ((SearchPeopleContract.Presenter) mPresenter).getMemberList(name, mMorePageNumber);
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
        ((SearchPeopleContract.Presenter) mPresenter).getMemberList(name, mMorePageNumber);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {// 如果等于1
            name = data.getStringExtra("name");
            mMorePageNumber = NumericConstants.START_PAGE_NUMBER;
////            showLoadingDialog(getString(R.string.dataLoad));
////            ((SearchArticleContract.Presenter) mPresenter).getGoodsList(mMorePageNumber, cat, sort, keyword, mark);
            mRefreshLayout.beginRefreshing();
        }
    }


    @Override
    public void setPresenter(SearchPeopleContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void getSuccess(String success, int flag) {
        if (flag == 0) {
            isShowLoadingMore = true;
            ll_commonError.setVisibility(View.GONE);
            mRefreshLayout.setVisibility(View.VISIBLE);
            mRefreshLayout.setPullDownRefreshEnable(true);
            SearchPeopleBean searchPeopleBean = (SearchPeopleBean) JsonUtil.getInstance().json2Obj(success, SearchPeopleBean.class);
            if (searchPeopleBean.getData() == null && mMorePageNumber == NumericConstants.START_PAGE_NUMBER ||
                    searchPeopleBean.getData().getList() == null && mMorePageNumber == NumericConstants.START_PAGE_NUMBER ||
                    searchPeopleBean.getData().getList().size() <= 0 && mMorePageNumber == NumericConstants.START_PAGE_NUMBER) {
                errorMsg(getString(R.string.noSearchPeople), 0);
                return;
            } else if (searchPeopleBean.getData() == null && mMorePageNumber > NumericConstants.START_PAGE_NUMBER ||
                    searchPeopleBean.getData().getList() == null && mMorePageNumber > NumericConstants.START_PAGE_NUMBER ||
                    searchPeopleBean.getData().getList().size() <= 0 && mMorePageNumber > NumericConstants.START_PAGE_NUMBER) {
                ViewInject.toast(getString(R.string.noMoreData));
                isShowLoadingMore = false;
                dismissLoadingDialog();
                mRefreshLayout.endLoadingMore();
                return;
            }
            mMorePageNumber = searchPeopleBean.getData().getCurrentPageNo();
            totalPageNumber = searchPeopleBean.getData().getTotalPageCount();
            if (mMorePageNumber == NumericConstants.START_PAGE_NUMBER) {
                mRefreshLayout.endRefreshing();
                mAdapter.clear();
                mAdapter.addNewData(searchPeopleBean.getData().getList());
            } else {
                mRefreshLayout.endLoadingMore();
                mAdapter.addMoreData(searchPeopleBean.getData().getList());
            }
            dismissLoadingDialog();
        }
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
        } else if (msg.contains(getString(R.string.noSearchPeople))) {
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
        mAdapter.clear();
        mAdapter = null;
    }
}
