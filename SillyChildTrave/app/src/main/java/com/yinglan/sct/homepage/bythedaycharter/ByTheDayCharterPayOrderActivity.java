package com.yinglan.sct.homepage.bythedaycharter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.cklibrary.common.BaseActivity;
import com.common.cklibrary.common.BindView;
import com.common.cklibrary.common.KJActivityStack;
import com.common.cklibrary.common.ViewInject;
import com.common.cklibrary.utils.ActivityTitleUtils;
import com.common.cklibrary.utils.DataUtil;
import com.common.cklibrary.utils.JsonUtil;
import com.common.cklibrary.utils.MathUtil;
import com.common.cklibrary.utils.myview.WebViewLayout;
import com.kymjs.common.StringUtils;
import com.yinglan.sct.R;
import com.yinglan.sct.entity.homepage.airporttransportation.airportpickup.CreateTravelOrderBean;
import com.yinglan.sct.entity.homepage.bythedaycharter.ByTheDayCharterPayOrderBean;
import com.yinglan.sct.homepage.airporttransportation.paymentorder.PaymentTravelOrderActivity;
import com.yinglan.sct.homepage.airporttransportation.airportselect.search.ProductSearchActivity;
import com.yinglan.sct.homepage.airporttransportation.airportselect.search.ProductSearchListActivity;
import com.yinglan.sct.homepage.bythedaycharter.cityselect.CharterCitySelectActivity;
import com.yinglan.sct.mine.mywallet.coupons.CouponsActivity;
import com.yinglan.sct.utils.GlideImageLoader;

import static com.yinglan.sct.constant.NumericConstants.RESULT_CODE_GET;

/**
 * 按天包车支付订单
 */
public class ByTheDayCharterPayOrderActivity extends BaseActivity implements ByTheDayCharterPayOrderContract.View {

    @BindView(id = R.id.img_byTheDayCharter)
    private ImageView img_byTheDayCharter;

    @BindView(id = R.id.tv_tourSpots)
    private TextView tv_tourSpots;

    @BindView(id = R.id.tv_tourSpots1)
    private TextView tv_tourSpots1;

    @BindView(id = R.id.web_priceDescription)
    private WebViewLayout web_priceDescription;

    @BindView(id = R.id.web_dueThat)
    private WebViewLayout web_dueThat;

    @BindView(id = R.id.tv_adult)
    private TextView tv_adult;

    @BindView(id = R.id.tv_children)
    private TextView tv_children;

    @BindView(id = R.id.tv_luggage)
    private TextView tv_luggage;

    @BindView(id = R.id.tv_travelTime)
    private TextView tv_travelTime;

    @BindView(id = R.id.tv_contact)
    private TextView tv_contact;

    @BindView(id = R.id.tv_contactWay)
    private TextView tv_contactWay;

    @BindView(id = R.id.tv_remark)
    private TextView tv_remark;

    @BindView(id = R.id.tv_orderPriceNoSign)
    private TextView tv_orderPriceNoSign;

    @BindView(id = R.id.tv_orderNumber)
    private TextView tv_orderNumber;

    @BindView(id = R.id.tv_totalOrder)
    private TextView tv_totalOrder;

    @BindView(id = R.id.tv_vouchers, click = true)
    private TextView tv_vouchers;
    @BindView(id = R.id.img_right)
    private ImageView img_right;

    @BindView(id = R.id.tv_actualPayment)
    private TextView tv_actualPayment;

    @BindView(id = R.id.tv_confirmPayment, click = true)
    private TextView tv_confirmPayment;

    private int requirement_id = 0;
    private String totalPrice = "0";

    private int bonusid = 0;

    private int product_id = 0;
    private String order_number = "";


    @Override
    public void setRootView() {
        setContentView(R.layout.activity_bythedaycharterpayorder);
    }

    @Override
    public void initData() {
        super.initData();
        mPresenter = new ByTheDayCharterPayOrderPresenter(this);
        requirement_id = getIntent().getIntExtra("requirement_id", 0);
        showLoadingDialog(getString(R.string.dataLoad));
        ((ByTheDayCharterPayOrderContract.Presenter) mPresenter).getTravelOrderDetail(requirement_id);
    }

    @Override
    public void initWidget() {
        super.initWidget();
        ActivityTitleUtils.initToolbar(aty, getString(R.string.paymentOrder), true, R.id.titlebar);
        web_priceDescription.setTitleVisibility(false);
        web_dueThat.setTitleVisibility(false);
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()) {
            case R.id.tv_vouchers:
                Intent intent1 = new Intent(aty, CouponsActivity.class);
                intent1.putExtra("type", -1);
                intent1.putExtra("money", totalPrice);
                startActivityForResult(intent1, RESULT_CODE_GET);
                break;
            case R.id.tv_confirmPayment:
                showLoadingDialog(getString(R.string.submissionLoad));
                ((ByTheDayCharterPayOrderContract.Presenter) mPresenter).createTravelOrder(product_id, order_number, bonusid);
                break;
        }
    }

    @Override
    public void setPresenter(ByTheDayCharterPayOrderContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void getSuccess(String success, int flag) {
        dismissLoadingDialog();
        if (flag == 0) {
            ByTheDayCharterPayOrderBean byTheDayCharterPayOrderBean = (ByTheDayCharterPayOrderBean) JsonUtil.getInstance().json2Obj(success, ByTheDayCharterPayOrderBean.class);
            GlideImageLoader.glideOrdinaryLoader(aty, byTheDayCharterPayOrderBean.getData().getMain_picture(), img_byTheDayCharter, R.mipmap.placeholderfigure2);
            tv_tourSpots.setText(byTheDayCharterPayOrderBean.getData().getTitle());
            tv_tourSpots1.setText(byTheDayCharterPayOrderBean.getData().getProduct_description());
            String price_description = "<!DOCTYPE html><html lang=\"zh\"><head>\t<meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no\" /><title></title></head><body>" + byTheDayCharterPayOrderBean.getData().getPrice_description()
                    + "</body></html>";
            web_priceDescription.loadDataWithBaseURL("baseurl", price_description, "text/html", "utf-8", null);
            web_priceDescription.getWebView().setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            String schedule_description = "<!DOCTYPE html><html lang=\"zh\"><head>\t<meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no\" /><title></title></head><body>" + byTheDayCharterPayOrderBean.getData().getSchedule_description()
                    + "</body></html>";
            web_dueThat.loadDataWithBaseURL("baseurl", schedule_description, "text/html", "utf-8", null);
            web_dueThat.getWebView().setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            tv_adult.setText(byTheDayCharterPayOrderBean.getData().getAudit_number() + "");
            tv_children.setText(byTheDayCharterPayOrderBean.getData().getChildren_number() + "");
            tv_luggage.setText(byTheDayCharterPayOrderBean.getData().getBaggage_number() + "");
            tv_travelTime.setText(DataUtil.formatData(StringUtils.toLong(byTheDayCharterPayOrderBean.getData().getTravel_start_time()), "yyyy" + getString(R.string.year) + "MM" + getString(R.string.month) + "dd" + getString(R.string.day)) + "-" +
                    DataUtil.formatData(StringUtils.toLong(byTheDayCharterPayOrderBean.getData().getTravel_end_time()), "yyyy" + getString(R.string.year) + "MM" + getString(R.string.month) +
                            "dd" + getString(R.string.day)) + "(" + getString(R.string.general) + byTheDayCharterPayOrderBean.getData().getDays() + getString(R.string.day) + ")");
            tv_contact.setText(byTheDayCharterPayOrderBean.getData().getContact());
            tv_contactWay.setText(byTheDayCharterPayOrderBean.getData().getContact_number());
            if (StringUtils.isEmpty(byTheDayCharterPayOrderBean.getData().getRemarks())) {
                tv_remark.setText(getString(R.string.noRemark));
            } else {
                tv_remark.setText(byTheDayCharterPayOrderBean.getData().getRemarks());
            }
            tv_orderPriceNoSign.setText(getString(R.string.renminbi) + byTheDayCharterPayOrderBean.getData().getPrice());
            tv_orderNumber.setText("X" + byTheDayCharterPayOrderBean.getData().getDays());
            totalPrice = byTheDayCharterPayOrderBean.getData().getPay_amount();
            tv_totalOrder.setText(getString(R.string.renminbi) + totalPrice);
            if (byTheDayCharterPayOrderBean.getData().getBonus_number() != 0) {
                tv_vouchers.setText(byTheDayCharterPayOrderBean.getData().getBonus_number() + getString(R.string.usable1));
            } else {
                tv_vouchers.setText(getString(R.string.renminbi) + "0.00");
                img_right.setVisibility(View.GONE);
                tv_vouchers.setOnClickListener(null);
            }
            tv_actualPayment.setText(getString(R.string.renminbi) + totalPrice);
            order_number = byTheDayCharterPayOrderBean.getData().getOrder_number();
            product_id = byTheDayCharterPayOrderBean.getData().getProduct_id();
        } else if (flag == 1) {
            KJActivityStack.create().finishActivity(ByTheDayCharterActivity.class);
            KJActivityStack.create().finishActivity(CharterCitySelectActivity.class);
            KJActivityStack.create().finishActivity(ProductSearchListActivity.class);
            KJActivityStack.create().finishActivity(ProductSearchActivity.class);
            KJActivityStack.create().finishActivity(PriceInformationActivity.class);
            KJActivityStack.create().finishActivity(SelectProductActivity.class);
            CreateTravelOrderBean createTravelOrderBean = (CreateTravelOrderBean) JsonUtil.getInstance().json2Obj(success, CreateTravelOrderBean.class);
            Intent intent = new Intent(aty, PaymentTravelOrderActivity.class);
            intent.putExtra("order_id", createTravelOrderBean.getData().getOrder_id());
            intent.putExtra("type", 1);
            intent.putExtra("end_time", createTravelOrderBean.getData().getEnd_time());
            intent.putExtra("start_time", createTravelOrderBean.getData().getStart_time());
            intent.putExtra("pay_amount", createTravelOrderBean.getData().getPay_amount());
            intent.putExtra("order_number", order_number);
            skipActivity(aty, intent);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_CODE_GET && resultCode == RESULT_OK) {
            String money = data.getStringExtra("money");
            bonusid = data.getIntExtra("id", 0);
            tv_vouchers.setText(getString(R.string.renminbi) + "-" + MathUtil.keepTwo(StringUtils.toDouble(money)));
            tv_actualPayment.setText(calculationTotal());
        }
    }

    /**
     * 计算合计
     */
    private String calculationTotal() {
        double total = 0;
        total = StringUtils.toDouble(tv_totalOrder.getText().toString().trim().substring(1)) + StringUtils.toDouble(tv_vouchers.getText().toString().trim().substring(1));
        if (total <= 0) {
            total = 0;
        }
        return MathUtil.keepTwo(total);
    }


    @Override
    public void errorMsg(String msg, int flag) {
        dismissLoadingDialog();
        if (isLogin(msg)) {
            finish();
            return;
        }
        ViewInject.toast(msg);
    }
}
