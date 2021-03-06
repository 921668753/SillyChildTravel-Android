package com.yinglan.sct.community.dynamic.dynamiccomments;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.cklibrary.common.BaseActivity;
import com.common.cklibrary.common.BindView;
import com.common.cklibrary.common.ViewInject;
import com.common.cklibrary.utils.ActivityTitleUtils;
import com.common.cklibrary.utils.JsonUtil;
import com.common.cklibrary.utils.myview.ChildListView;
import com.common.cklibrary.utils.rx.MsgEvent;
import com.common.cklibrary.utils.rx.RxBus;
import com.kymjs.common.StringUtils;
import com.yinglan.sct.R;
import com.yinglan.sct.adapter.community.dynamic.dynamiccomments.CommentDetailsViewAdapter;
import com.yinglan.sct.community.DisplayPageActivity;
import com.yinglan.sct.community.dynamic.dialog.RevertBouncedDialog;
import com.yinglan.sct.entity.community.dynamic.dynamiccomments.CommentDetailsBean;
import com.yinglan.sct.entity.community.dynamic.dynamiccomments.CommentDetailsBean.DataBean;
import com.yinglan.sct.loginregister.LoginActivity;
import com.yinglan.sct.utils.GlideImageLoader;

import cn.bingoogolapple.baseadapter.BGAOnItemChildClickListener;


/**
 * 单条动态评论详情
 */
public class CommentDetailsActivity extends BaseActivity implements CommentDetailsContract.View, AdapterView.OnItemClickListener, BGAOnItemChildClickListener {

    @BindView(id = R.id.img_avatar)
    private ImageView img_avatar;

    @BindView(id = R.id.tv_nickName, click = true)
    private TextView tv_nickName;

    @BindView(id = R.id.ll_zan, click = true)
    private LinearLayout ll_zan;

    @BindView(id = R.id.img_zan)
    private ImageView img_zan;

    @BindView(id = R.id.tv_giveLike)
    private TextView tv_giveLike;

    @BindView(id = R.id.tv_content)
    private TextView tv_content;

    @BindView(id = R.id.tv_time)
    private TextView tv_time;

    @BindView(id = R.id.tv_revert, click = true)
    private TextView tv_revert;

    @BindView(id = R.id.ll_revert)
    private LinearLayout ll_revert;

    @BindView(id = R.id.clv_revert)
    private ChildListView clv_revert;

    private CommentDetailsViewAdapter mAdapter;
    private RevertBouncedDialog revertBouncedDialog = null;
    private int type = 0;
    private int id = 0;
    private DataBean comment;

    private int is_like = 0;
    private int type1 = 0;

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_dynamiccommentdetails);
    }

    @Override
    public void initData() {
        super.initData();
        type = getIntent().getIntExtra("type", 0);
        id = getIntent().getIntExtra("id", 0);
        type1 = getIntent().getIntExtra("type1", 0);
        mPresenter = new CommentDetailsPresenter(this);
        mAdapter = new CommentDetailsViewAdapter(this);
        showLoadingDialog(getString(R.string.dataLoad));
        ((CommentDetailsContract.Presenter) mPresenter).getCommentDetails(id);
    }


    @Override
    public void initWidget() {
        super.initWidget();
        ActivityTitleUtils.initToolbar(aty, getString(R.string.commentDetails), true, R.id.titlebar);
        clv_revert.setAdapter(mAdapter);
        clv_revert.setOnItemClickListener(this);
        mAdapter.setOnItemChildClickListener(this);
    }

    private void initDialog(int type2, int position) {
        if (revertBouncedDialog == null) {
            revertBouncedDialog = new RevertBouncedDialog(this) {
                @Override
                public void toSuccess() {
                    if (revertBouncedDialog != null && revertBouncedDialog.isShowing()) {
                        this.dismiss();
                    }
                    /**
                     * 发送消息
                     */
                    RxBus.getInstance().post(new MsgEvent<String>("RxBusDynamicDetailsEvent"));
                }
            };
        }
        if (type2 == 1 && position == 0) {
            if (revertBouncedDialog != null && !revertBouncedDialog.isShowing()) {
                type1 = 0;
                revertBouncedDialog.show();
                revertBouncedDialog.setHintText(getString(R.string.revert) + comment.getNickname(), comment.getPost_id(), comment.getId(), comment.getMember_id(), type);
            }
        } else if (type2 == 2) {
            if (revertBouncedDialog != null && !revertBouncedDialog.isShowing()) {
                revertBouncedDialog.show();
                revertBouncedDialog.setHintText(getString(R.string.revert) + mAdapter.getItem(position).getNickname(), StringUtils.toInt(mAdapter.getItem(position).getPost_id()), comment.getId(), mAdapter.getItem(position).getMember_id(), type);
            }
        }
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()) {
            case R.id.tv_nickName:
                Intent intent = new Intent(aty, DisplayPageActivity.class);
                intent.putExtra("user_id", comment.getMember_id());
                intent.putExtra("isRefresh", 0);
                showActivity(aty, intent);
                break;
            case R.id.ll_zan:
                showLoadingDialog(getString(R.string.dataLoad));
                ((CommentDetailsContract.Presenter) mPresenter).postAddCommentLike(id, type);
                break;
            case R.id.tv_revert:
                initDialog(1, 0);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        initDialog(2, position);
    }

    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {
        if (childView.getId() == R.id.tv_nickName1) {
            Intent intent = new Intent(aty, DisplayPageActivity.class);
            intent.putExtra("user_id", mAdapter.getItem(position).getMember_id());
            intent.putExtra("isRefresh", 0);
            showActivity(aty, intent);
        } else if (childView.getId() == R.id.tv_nickName2) {
            Intent intent = new Intent(aty, DisplayPageActivity.class);
            intent.putExtra("user_id", mAdapter.getItem(position).getReply_member_id());
            intent.putExtra("isRefresh", 0);
            showActivity(aty, intent);
        }
    }


    @Override
    public void setPresenter(CommentDetailsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void getSuccess(String success, int flag) {
        if (flag == 0) {
            CommentDetailsBean commentDetailsBean = (CommentDetailsBean) JsonUtil.json2Obj(success, CommentDetailsBean.class);
            comment = commentDetailsBean.getData();
            GlideImageLoader.glideLoader(this, comment.getFace(), img_avatar, 0, R.mipmap.avatar);
            tv_nickName.setText(comment.getNickname());
            tv_content.setText(comment.getBody());
            tv_time.setText(comment.getCreate_time());
            is_like = comment.getIs_comment_like();
            if (is_like == 1) {
                img_zan.setImageResource(R.mipmap.dynamicdetails_zan1);
                tv_giveLike.setTextColor(getResources().getColor(R.color.greenColors));
            } else {
                img_zan.setImageResource(R.mipmap.dynamicdetails_zan);
                //   tv_giveLike.setText(getString(R.string.giveLike));
                tv_giveLike.setTextColor(getResources().getColor(R.color.tabColors));
            }
            tv_giveLike.setText(comment.getComment_like_number());
            if (comment.getReplyList() == null || comment.getReplyList().size() <= 0) {
                ll_revert.setVisibility(View.GONE);
            } else {
                ll_revert.setVisibility(View.VISIBLE);
                mAdapter.clear();
                mAdapter.addMoreData(comment.getReplyList());
            }
            initDialog(type1, 0);
        } else if (flag == 1) {
            if (is_like == 1) {
                is_like = 0;
                img_zan.setImageResource(R.mipmap.dynamicdetails_zan);
//                tv_giveLike.setText(getString(R.string.giveLike));
                tv_giveLike.setText(StringUtils.toInt(comment.getComment_like_number(), 0) - 1 + "");
                tv_giveLike.setTextColor(getResources().getColor(R.color.tabColors));
                ViewInject.toast(getString(R.string.cancelZanSuccess));
            } else {
                is_like = 1;
                img_zan.setImageResource(R.mipmap.dynamicdetails_zan1);
                tv_giveLike.setText(StringUtils.toInt(comment.getComment_like_number(), 0) + 1 + "");
                tv_giveLike.setTextColor(getResources().getColor(R.color.greenColors));
                ViewInject.toast(getString(R.string.zanSuccess));
            }
        }
        dismissLoadingDialog();
    }

    @Override
    public void errorMsg(String msg, int flag) {
        dismissLoadingDialog();
        if (isLogin(msg)) {
            showActivity(aty, LoginActivity.class);
        } else {
            ViewInject.toast(msg);
        }
    }


    /**
     * 在接收消息的时候，选择性接收消息：
     */
    @Override
    public void callMsgEvent(MsgEvent msgEvent) {
        super.callMsgEvent(msgEvent);
        if (((String) msgEvent.getData()).equals("RxBusDynamicDetailsEvent") && mPresenter != null) {
            ((CommentDetailsContract.Presenter) mPresenter).getCommentDetails(id);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (revertBouncedDialog != null) {
            revertBouncedDialog.cancel();
        }
        revertBouncedDialog = null;
        mAdapter.clear();
        mAdapter = null;
    }


}
