package com.yinglan.sct.mine.personaldata;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.common.cklibrary.common.BaseActivity;
import com.common.cklibrary.common.BindView;
import com.common.cklibrary.common.StringConstants;
import com.common.cklibrary.common.ViewInject;
import com.common.cklibrary.common.pictureselector.FullyGridLayoutManager;
import com.common.cklibrary.utils.DataUtil;
import com.common.cklibrary.utils.JsonUtil;
import com.common.cklibrary.utils.rx.MsgEvent;
import com.common.cklibrary.utils.rx.RxBus;
import com.kymjs.common.FileUtils;
import com.kymjs.common.PreferenceHelper;
import com.kymjs.common.StringUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.yinglan.sct.R;
import com.yinglan.sct.adapter.mine.setup.GridImageAdapter;
import com.yinglan.sct.constant.NumericConstants;
import com.yinglan.sct.entity.mine.personaldata.AddressRegionBean;
import com.yinglan.sct.entity.mine.personaldata.RegionListBean;
import com.yinglan.sct.loginregister.LoginActivity;
import com.yinglan.sct.message.interactivemessage.imuitl.UserUtil;
import com.yinglan.sct.mine.personaldata.dialog.PictureSourceDialog;
import com.yinglan.sct.mine.personaldata.setnickname.SetNickNameActivity;
import com.yinglan.sct.mine.personaldata.setsex.SetSexActivity;
import com.yinglan.sct.mine.personaldata.setsignature.SetSignatureActivity;
import com.yinglan.sct.startpage.dialog.PermissionsDialog;
import com.yinglan.sct.utils.GlideImageLoader;
import com.yinglan.sct.utils.SoftKeyboardUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.bingoogolapple.titlebar.BGATitleBar;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.yinglan.sct.constant.NumericConstants.RESULT_CODE_BASKET_ADD;
import static com.yinglan.sct.constant.NumericConstants.RESULT_CODE_BASKET_MINUS;
import static com.yinglan.sct.constant.NumericConstants.RESULT_CODE_BASKET_MINUSALL;
import static com.yinglan.sct.constant.NumericConstants.RESULT_CODE_GET;
import static com.yinglan.sct.constant.NumericConstants.RESULT_CODE_ORDER;
import static com.yinglan.sct.constant.NumericConstants.RESULT_CODE_PAYMENT_SUCCEED;

/**
 * 个人资料
 * Created by Administrator on 2017/9/2.
 */

public class PersonalDataActivity extends BaseActivity implements PersonalDataContract.View ,EasyPermissions.PermissionCallbacks{

    @BindView(id = R.id.titlebar)
    private BGATitleBar titlebar;

    @BindView(id = R.id.tv_personalcode)
    private TextView tv_personalcode;

    @BindView(id = R.id.ll_personaldatatx, click = true)
    private LinearLayout ll_personaldatatx;

    @BindView(id = R.id.iv_personaltx)
    private ImageView iv_personaltx;

    @BindView(id = R.id.ll_personaldatanc, click = true)
    private LinearLayout ll_personaldatanc;

    @BindView(id = R.id.tv_personalnickname)
    private TextView tv_personalnickname;

    @BindView(id = R.id.ll_personaldataxb, click = true)
    private LinearLayout ll_personaldataxb;

    @BindView(id = R.id.tv_personalsex)
    private TextView tv_personalsex;

    @BindView(id = R.id.ll_personaldatasr, click = true)
    private LinearLayout ll_personaldatasr;

    @BindView(id = R.id.tv_personalbirthday)
    private TextView tv_personalbirthday;

    @BindView(id = R.id.ll_personaldatadq, click = true)
    private LinearLayout ll_personaldatadq;

    @BindView(id = R.id.tv_personaldiqu)
    private TextView tv_personaldiqu;

    @BindView(id = R.id.ll_personaldatagxqm, click = true)
    private LinearLayout ll_personaldatagxqm;

    @BindView(id = R.id.tv_signature)
    private TextView tv_signature;


    private PictureSourceDialog pictureSourceDialog;

    public static final int REQUEST_CODE_SELECT = 100;

    private long birthday = 0;//生日

    private Calendar birthdaycalendar = null;//生日


    private TimePickerView pvCustomTime = null;

    private int province_id = 0;
    private int city_id = 0;
    private int region_id = 0;
    private int town_id = 0;
    private String province = "";
    private String city = "";
    private String region = "";
    private OptionsPickerView pvNoLinkOptions = null;
    private List<RegionListBean.DataBean> provinceList = null;
    private List<RegionListBean.DataBean> cityList = null;
    private List<RegionListBean.DataBean> areaList = null;
    private int areaOptions3 = 0;
    private int cityOptions2 = 0;
    private int provinceOptions1 = 0;

    private List<AddressRegionBean.DataBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<AddressRegionBean.DataBean.ChildrenBeanX>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<AddressRegionBean.DataBean.ChildrenBeanX.ChildrenBean>>> options3Items = new ArrayList<>();

    private OptionsPickerView pvLinkOptions = null;

    private Thread mThread = null;

    private int themeId;
    private int aspect_ratio_x = 16, aspect_ratio_y = 9;
    private int maxSelectNum = 9;

    public int type = -1;
    private PermissionsDialog permissionsDialog;

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_personaldata);
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        super.initData();
        mPresenter = new PersonalDataPresenter(this);
        provinceList = new ArrayList<RegionListBean.DataBean>();
        cityList = new ArrayList<RegionListBean.DataBean>();
        areaList = new ArrayList<RegionListBean.DataBean>();
        initCustomTimePicker();
        initLinkOptionsPicker();
        themeId = R.style.picture_default_style;
        initDialog();
        ((PersonalDataContract.Presenter) mPresenter).getAddress(0);
        // ((PersonalDataContract.Presenter) mPresenter).getRegionList(0, 4);
    }


    private void initDialog() {
        permissionsDialog = new PermissionsDialog(this) {
            @Override
            public void doAction() {

            }
        };
    }

    /**
     * 渲染控件
     */
    @Override
    public void initWidget() {
        super.initWidget();
        initTitle();
        initView();
        // 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
        RxPermissions permissions = new RxPermissions(this);
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    PictureFileUtils.deleteCacheDirFile(PersonalDataActivity.this);
                } else {
                    ViewInject.toast(getString(R.string.picture_jurisdiction));
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });
    }

    /**
     * 渲染页面
     */
    private void initView() {
        String shz = PreferenceHelper.readString(aty, StringConstants.FILENAME, "shz");
        tv_personalcode.setText(shz);
        String face = PreferenceHelper.readString(aty, StringConstants.FILENAME, "face");
        if (StringUtils.isEmpty(face)) {
            iv_personaltx.setImageResource(R.mipmap.avatar);
        } else {
            GlideImageLoader.glideLoader(aty, face, iv_personaltx, 0, R.mipmap.avatar);
        }
        String nick_name = PreferenceHelper.readString(aty, StringConstants.FILENAME, "nick_name");
        String mobile = PreferenceHelper.readString(aty, StringConstants.FILENAME, "mobile");
        if (StringUtils.isEmpty(nick_name)) {
            tv_personalnickname.setText(mobile);
        } else {
            tv_personalnickname.setText(nick_name);
        }
        int sex = PreferenceHelper.readInt(aty, StringConstants.FILENAME, "sex", 0);
        if (sex == 1) {
            tv_personalsex.setText(getString(R.string.nan));
        } else if (sex == 2) {
            tv_personalsex.setText(getString(R.string.nv));
        } else {
            tv_personalsex.setText(getString(R.string.secret));
        }
        birthday = StringUtils.toLong(PreferenceHelper.readString(aty, StringConstants.FILENAME, "birthday"));
        if (birthday > 0) {
            String birthdayStr = DataUtil.formatData(birthday, "yyyy-MM-dd");
            tv_personalbirthday.setText(birthdayStr);
        } else {
            tv_personalbirthday.setText(getString(R.string.pleaseSelect));
        }
        province = PreferenceHelper.readString(aty, StringConstants.FILENAME, "province");
        city = PreferenceHelper.readString(aty, StringConstants.FILENAME, "city");
        region = PreferenceHelper.readString(aty, StringConstants.FILENAME, "region");
        if (StringUtils.isEmpty(province) || StringUtils.isEmpty(city) || StringUtils.isEmpty(region)) {
            tv_personaldiqu.setText(getString(R.string.pleaseSelect));
        } else {
            tv_personaldiqu.setText(province + city + region);
        }
        String signature = PreferenceHelper.readString(aty, StringConstants.FILENAME, "signature");
        tv_signature.setText(signature);
    }


    /**
     * 设置标题
     */
    public void initTitle() {
        titlebar.setTitleText(getString(R.string.personaData));
        BGATitleBar.SimpleDelegate simpleDelegate = new BGATitleBar.SimpleDelegate() {
            @Override
            public void onClickLeftCtv() {
                super.onClickLeftCtv();
                aty.finish();
            }

            @Override
            public void onClickRightCtv() {
                super.onClickRightCtv();
            }
        };
        titlebar.setDelegate(simpleDelegate);
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()) {
            case R.id.tv_personalcode:
//                if (updatanum < 2) {
//                    Intent jumpintent = new Intent(this, SetSillyCodeActivity.class);
////                    jumpintent.putExtra("nickname", et_personalcode.getText().toString());
//                    showActivityForResult(this, jumpintent, RESULT_CODE_GET);
//                } else {
//                    ViewInject.toast(getString(R.string.sillyChildNumberNoChange));
//                }
                break;
            case R.id.ll_personaldatatx:
                PictureDialog();
                break;
            case R.id.ll_personaldatanc:
                Intent setNickNameIntent = new Intent(this, SetNickNameActivity.class);
                setNickNameIntent.putExtra("nickname", tv_personalnickname.getText().toString());
                showActivityForResult(this, setNickNameIntent, RESULT_CODE_BASKET_ADD);
                break;
            case R.id.ll_personaldataxb:
                int sex = PreferenceHelper.readInt(aty, StringConstants.FILENAME, "sex", 0);
                Intent setSexIntent = new Intent(this, SetSexActivity.class);
                setSexIntent.putExtra("sex", sex);
                startActivityForResult(setSexIntent, RESULT_CODE_BASKET_MINUS);
                break;
            case R.id.ll_personaldatasr:
                if (birthday > 0) {
                    birthdaycalendar.setTimeInMillis(birthday);
                    pvCustomTime.setDate(birthdaycalendar);
                }
                pvCustomTime.show(tv_personalbirthday);
                break;
            case R.id.ll_personaldatadq:
                //  pvNoLinkOptions.show(tv_personaldiqu);
                SoftKeyboardUtils.packUpKeyboard(aty);
                pvLinkOptions.show(tv_personaldiqu);
                break;
            case R.id.ll_personaldatagxqm:
                Intent setSignatureIntent = new Intent(this, SetSignatureActivity.class);
                setSignatureIntent.putExtra("signature", tv_signature.getText().toString().trim());
                showActivityForResult(this, setSignatureIntent, RESULT_CODE_BASKET_MINUSALL);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case RESULT_CODE_GET:
                    //     shzcode = data.getStringExtra("nickname");

                    //    et_personalcode.setText(shzcode);
                    /**
                     * 发送消息
                     */
                    RxBus.getInstance().post(new MsgEvent<String>("RxBusPersonalDataEvent"));
                    break;
                case RESULT_CODE_BASKET_ADD:
                    tv_personalnickname.setText(data.getStringExtra("nickname"));
                    /**
                     * 发送消息
                     */
                    RxBus.getInstance().post(new MsgEvent<String>("RxBusPersonalDataEvent"));
                    break;
                case RESULT_CODE_BASKET_MINUS:
                    int sex = data.getIntExtra("sex", 0);
                    if (sex == 1) {
                        tv_personalsex.setText(getString(R.string.nan));
                    } else if (sex == 2) {
                        tv_personalsex.setText(getString(R.string.nv));
                    } else {
                        tv_personalsex.setText(getString(R.string.secret));
                    }
                    PreferenceHelper.write(aty, StringConstants.FILENAME, "sex", sex);
                    break;
                case RESULT_CODE_BASKET_MINUSALL:
                    String signature = data.getStringExtra("signature");
                    tv_signature.setText(signature);
                    PreferenceHelper.write(aty, StringConstants.FILENAME, "signature", signature);
                    /**
                     * 发送消息
                     */
                    RxBus.getInstance().post(new MsgEvent<String>("RxBusPersonalDataEvent"));
                    break;
                case REQUEST_CODE_SELECT:
                    // 图片选择结果回调
                    List<LocalMedia> selectList1 = PictureSelector.obtainMultipleResult(data);
                    if (selectList1 == null || selectList1.size() <= 0) {
                        ViewInject.toast(getString(R.string.noData));
                        return;
                    }
                    LocalMedia localMedia = selectList1.get(0);
                    String headPath = "";
                    if (localMedia.isCut() && localMedia.isCompressed()) {
                        headPath = localMedia.getCompressPath();
                    } else if (localMedia.isCut() && !localMedia.isCompressed()) {
                        headPath = localMedia.getCutPath();
                    } else {
                        headPath = localMedia.getPath();
                    }
                    showLoadingDialog(getString(R.string.saveLoad));
                    ((PersonalDataContract.Presenter) mPresenter).upPictures(headPath);
                    break;
            }
        }

    }


    /**
     * 选择更换头像的弹窗
     */
    public void PictureDialog() {
        if (pictureSourceDialog == null) {
            pictureSourceDialog = new PictureSourceDialog(aty) {
                @Override
                public void takePhoto() {
                    type = RESULT_CODE_ORDER;
                    choicePhotoWrapper();
                }

                @Override
                public void chooseFromAlbum() {
                    type = RESULT_CODE_PAYMENT_SUCCEED;
                    choicePhotoWrapper();
                }
            };
        }
        pictureSourceDialog.show();
    }

    @AfterPermissionGranted(NumericConstants.REQUEST_CODE_PERMISSION_PHOTO_PICKER)
    private void choicePhotoWrapper() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms) && type == RESULT_CODE_ORDER) {
            PictureSelector.create(PersonalDataActivity.this)
                    .openCamera(PictureMimeType.ofImage())
                    .enableCrop(true)// 是否裁剪
                    .compress(true)// 是否压缩
                    .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                    .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                    .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                    .withAspectRatio(aspect_ratio_x, aspect_ratio_y)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                    .circleDimmedLayer(true)// 是否圆形裁剪
                    .cropCompressQuality(90)// 裁剪压缩质量 默认90 int
                    .minimumCompressSize(StringConstants.COMPRESSION_SIZE)// 小于100kb的图片不压缩
                    .setOutputCameraPath(FileUtils.getSaveFolder(StringConstants.PHOTOPATH).getAbsolutePath())// 自定义拍照保存路径
                    .compressSavePath(FileUtils.getSaveFolder(StringConstants.PHOTOPATH).getAbsolutePath())//压缩图片保存地址
                    .forResult(REQUEST_CODE_SELECT);
        } else if (EasyPermissions.hasPermissions(this, perms) && type == RESULT_CODE_PAYMENT_SUCCEED) {
            PictureSelector.create(PersonalDataActivity.this)
                    .openGallery(PictureMimeType.ofImage())
                    .selectionMode(PictureConfig.SINGLE)
                    .isCamera(false)// 是否显示拍照按钮
                    .previewImage(false)// 是否可预览图片 true or false
                    .enableCrop(true)// 是否裁剪
                    .compress(true)// 是否压缩
                    .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                    .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                    .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                    .withAspectRatio(aspect_ratio_x, aspect_ratio_y)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                    .circleDimmedLayer(true)// 是否圆形裁剪
                    .cropCompressQuality(90)// 裁剪压缩质量 默认90 int
                    .minimumCompressSize(StringConstants.COMPRESSION_SIZE)// 小于100kb的图片不压缩
                    .setOutputCameraPath(FileUtils.getSaveFolder(StringConstants.PHOTOPATH).getAbsolutePath())// 自定义拍照保存路径
                    .compressSavePath(FileUtils.getSaveFolder(StringConstants.PHOTOPATH).getAbsolutePath())//压缩图片保存地址
                    .forResult(REQUEST_CODE_SELECT);
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.needPermission), NumericConstants.REQUEST_CODE_PERMISSION_PHOTO_PICKER, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == NumericConstants.REQUEST_CODE_PERMISSION_PHOTO_PICKER) {
            ViewInject.toast(getString(R.string.denyPermission));
        }
    }


    /**
     * 选择时间的控件
     */
    private void initCustomTimePicker() {

        /**
         * @description
         *
         * 注意事项：
         * 1.自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针.
         * 具体可参考demo 里面的两个自定义layout布局。
         * 2.因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
         * setRangDate方法控制起始终止时间(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
         */
        birthdaycalendar = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(birthdaycalendar.get(Calendar.YEAR) - 99, birthdaycalendar.get(Calendar.MONTH), birthdaycalendar.get(Calendar.DAY_OF_MONTH));
        Calendar endDate = Calendar.getInstance();
        endDate.set(birthdaycalendar.get(Calendar.YEAR), birthdaycalendar.get(Calendar.MONTH), birthdaycalendar.get(Calendar.DAY_OF_MONTH));
        //时间选择器 ，自定义布局
        pvCustomTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                birthday = date.getTime() / 1000;
                //birthdaycalendar.setTime(date);
                showLoadingDialog(getString(R.string.saveLoad));
                ((PersonalDataContract.Presenter) mPresenter).setBirthday(birthday);
                //  ((TextView) v).setText(format.format(date));
            }
        })
                .setDate(birthdaycalendar)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final Button btnSubmit = (Button) v.findViewById(R.id.btnSubmit);
                        Button btnCancel = (Button) v.findViewById(R.id.btnCancel);
                        btnSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.returnData();
                                pvCustomTime.dismiss();
                            }
                        });
                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.dismiss();
                            }
                        });
                    }
                })
                .setContentTextSize(18)
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel(getString(R.string.year), getString(R.string.month), getString(R.string.day),  getString(R.string.hour),  getString(R.string.minute),  getString(R.string.seconds))
                .setTextXOffset(0, 0, 0, 40, 0, -40)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(0xFFd5d5d5)
                .build();
    }

    /**
     * 联动地区选择
     */
    private void initLinkOptionsPicker() {
        pvLinkOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                province_id = options1Items.get(options1).getRegion_id();
                city_id = options2Items.get(options1).get(options2).getRegion_id();
                region_id = options3Items.get(options1).get(options2).get(options3).getRegion_id();
                province = options1Items.get(options1).getLocal_name();
                city = options2Items.get(options1).get(options2).getLocal_name();
                region = options3Items.get(options1).get(options2).get(options3).getLocal_name();
                ((PersonalDataContract.Presenter) mPresenter).setRegion(options1Items.get(options1).getLocal_name(), province_id, options2Items.get(options1).get(options2).getLocal_name(), city_id, options3Items.get(options1).get(options2).get(options3).getLocal_name(), region_id);
            }
        }).build();
    }


    /**
     * 获取地区二级列表
     */
    private void getRegionList(List<RegionListBean.DataBean> list, String positionName, int flag) {
        for (int i = 0; i < list.size(); i++) {
            if (StringUtils.isEmpty(positionName) && i == 0 && flag == 5 || !StringUtils.isEmpty(positionName) && positionName.contains(list.get(i).getLocal_name()) && flag == 5) {
                provinceOptions1 = i;
                ((PersonalDataContract.Presenter) mPresenter).getRegionList(list.get(i).getRegion_id(), flag);
                return;
            } else if (StringUtils.isEmpty(positionName) && i == 0 && flag == 6 || !StringUtils.isEmpty(positionName) && positionName.contains(list.get(i).getLocal_name()) && flag == 6) {
                cityOptions2 = i;
                ((PersonalDataContract.Presenter) mPresenter).getRegionList(list.get(i).getRegion_id(), flag);
                return;
            } else if (StringUtils.isEmpty(positionName) && i == 0 && flag == 7 || !StringUtils.isEmpty(positionName) && positionName.contains(list.get(i).getLocal_name()) && flag == 7) {
                areaOptions3 = i;
                return;
            }
        }
    }


    @Override
    public void setPresenter(PersonalDataContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void getSuccess(String success, int flag) {
        dismissLoadingDialog();
        switch (flag) {
            case -1:
                AddressRegionBean addressRegionBean = (AddressRegionBean) JsonUtil.getInstance().json2Obj(success, AddressRegionBean.class);
                options1Items = addressRegionBean.getData();
                if (options1Items == null || options1Items.size() <= 0) {
                    errorMsg(getString(R.string.serverReturnsDataNullJsonError), -1);
                    return;
                }
                Log.d("tag1", options1Items.size() + "=province");
                try {
                    if (mThread != null && !mThread.isAlive()) {
                        mThread.run();
                    } else {
                        mThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                setPickerData();
                            }
                        });
                        mThread.start();
                    }
                } catch (Exception e) {
                    errorMsg(getString(R.string.serverReturnsDataNullJsonError), -1);
                }
                break;
            case 0:

                break;
            case 1:
                GlideImageLoader.glideLoader(aty, success, iv_personaltx, 0, R.mipmap.avatar);
                /**
                 * 发送消息
                 */
                RxBus.getInstance().post(new MsgEvent<String>("RxBusPersonalDataEvent"));
                UserInfo userInfo = new UserInfo(UserUtil.getRcId(this), tv_personalnickname.getText().toString(), Uri.parse(success));
                RongIM.getInstance().refreshUserInfoCache(userInfo);
                break;
            case 2:
                String birthdayStr = DataUtil.formatData(birthday, "yyyy-MM-dd");
                tv_personalbirthday.setText(birthdayStr);
                PreferenceHelper.write(aty, StringConstants.FILENAME, "birthday", String.valueOf(birthday));
                break;
            case 3:
                tv_personaldiqu.setText(province + city + region);
                // tv_personaldiqu.setText(provinceList.get(provinceOptions1).getLocal_name() + cityList.get(cityOptions2).getLocal_name() + areaList.get(areaOptions3).getLocal_name());
                PreferenceHelper.write(aty, StringConstants.FILENAME, "province", province);
                PreferenceHelper.write(aty, StringConstants.FILENAME, "province_id", String.valueOf(province_id));
                PreferenceHelper.write(aty, StringConstants.FILENAME, "city", city);
                PreferenceHelper.write(aty, StringConstants.FILENAME, "city_id", String.valueOf(city_id));
                PreferenceHelper.write(aty, StringConstants.FILENAME, "region", region);
                PreferenceHelper.write(aty, StringConstants.FILENAME, "region_id", String.valueOf(region_id));
                break;
            case 4:
                RegionListBean regionListBean = (RegionListBean) JsonUtil.json2Obj(success, RegionListBean.class);
                if (regionListBean.getData() != null && regionListBean.getData().size() > 0) {
                    provinceList.addAll(regionListBean.getData());
                    getRegionList(provinceList, province, 5);
                }
                break;
            case 5:
                RegionListBean regionListBean1 = (RegionListBean) JsonUtil.json2Obj(success, RegionListBean.class);
                if (regionListBean1.getData() != null && regionListBean1.getData().size() > 0) {
                    cityList.clear();
                    cityList.addAll(regionListBean1.getData());
                    getRegionList(cityList, city, 6);
                } else {
                    pvNoLinkOptions.setNPicker(provinceList, cityList, areaList);
                    pvNoLinkOptions.setSelectOptions(provinceOptions1, cityOptions2, areaOptions3);
                }
                break;
            case 6:
                RegionListBean regionListBean2 = (RegionListBean) JsonUtil.json2Obj(success, RegionListBean.class);
                if (regionListBean2.getData() != null && regionListBean2.getData().size() > 0) {
                    areaList.clear();
                    areaList.addAll(regionListBean2.getData());
                    getRegionList(areaList, region, 7);
                } else {
                    areaList.clear();
                    RegionListBean.DataBean dataBean = new RegionListBean.DataBean();
                    dataBean.setLocal_name("");
                    areaList.add(dataBean);
                }
                pvNoLinkOptions.setNPicker(provinceList, cityList, areaList);
                pvNoLinkOptions.setSelectOptions(provinceOptions1, cityOptions2, areaOptions3);
                break;
        }
    }

    /**
     * 地区选择添加数据
     */
    private void setPickerData() {
        for (int i = 0; i < options1Items.size(); i++) {//遍历省份
            ArrayList<AddressRegionBean.DataBean.ChildrenBeanX> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<AddressRegionBean.DataBean.ChildrenBeanX.ChildrenBean>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）
            if (StringUtils.isEmpty(options1Items.get(i).getLocal_name())) {
                continue;
            }
            if (province != null && province.equals(options1Items.get(i).getLocal_name())) {
                provinceOptions1 = i;
                Log.d("tag1", provinceOptions1 + "=province");
            }
            if (options1Items.get(i).getChildren() == null || options1Items.get(i).getChildren().size() <= 0) {
                AddressRegionBean.DataBean.ChildrenBeanX childrenBeanX = new AddressRegionBean.DataBean.ChildrenBeanX();
                childrenBeanX.setRegion_id(0);
                childrenBeanX.setLocal_name("");
                CityList.add(childrenBeanX);//添加城市
                ArrayList<AddressRegionBean.DataBean.ChildrenBeanX.ChildrenBean> childrenBeanList1 = new ArrayList<>();//该城市的所有地区列表
                AddressRegionBean.DataBean.ChildrenBeanX.ChildrenBean childrenBean = new AddressRegionBean.DataBean.ChildrenBeanX.ChildrenBean();
                childrenBean.setRegion_id(0);
                childrenBean.setLocal_name("");
                childrenBeanList1.add(childrenBean);
                Province_AreaList.add(childrenBeanList1);
                options2Items.add(CityList);
                options3Items.add(Province_AreaList);
                continue;
            }
            for (int c = 0; c < options1Items.get(i).getChildren().size(); c++) {//遍历该省份的所有城市
                AddressRegionBean.DataBean.ChildrenBeanX CityName = options1Items.get(i).getChildren().get(c);
                if (StringUtils.isEmpty(CityName.getLocal_name())) {
                    CityName = new AddressRegionBean.DataBean.ChildrenBeanX();
                    CityName.setRegion_id(0);
                    CityName.setLocal_name("");
                }
                CityList.add(CityName);//添加城市
                ArrayList<AddressRegionBean.DataBean.ChildrenBeanX.ChildrenBean> City_AreaList = new ArrayList<>();//该城市的所有地区列表
                if (city != null && city.equals(options1Items.get(i).getChildren().get(c).getLocal_name())) {
                    cityOptions2 = c;
                    Log.d("tag1", cityOptions2 + "=city");
                }
                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (options1Items.get(i).getChildren().get(c).getChildren() == null
                        || options1Items.get(i).getChildren().get(c).getChildren().size() == 0) {
                    AddressRegionBean.DataBean.ChildrenBeanX.ChildrenBean AreaName = new AddressRegionBean.DataBean.ChildrenBeanX.ChildrenBean();
                    AreaName.setRegion_id(0);
                    AreaName.setLocal_name("");
                    City_AreaList.add(AreaName);
                } else {
                    for (int d = 0; d < options1Items.get(i).getChildren().get(c).getChildren().size(); d++) {//该城市对应地区所有数据
                        AddressRegionBean.DataBean.ChildrenBeanX.ChildrenBean AreaName = options1Items.get(i).getChildren().get(c).getChildren().get(d);
                        if (region != null && region.startsWith(AreaName.getLocal_name())) {
                            areaOptions3 = d;
                            Log.d("tag1", areaOptions3 + "=Area");
                        }
                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }
            /**
             * 添加城市数据
             */
            options2Items.add(CityList);
            Log.d("tag1", options2Items.size() + "=CityList");
            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
            Log.d("tag1", options3Items.size() + "=Province_AreaList");
        }
        pvLinkOptions.setPicker(options1Items, options2Items, options3Items);
        pvLinkOptions.setSelectOptions(provinceOptions1, cityOptions2, areaOptions3);
    }


    @Override
    public void errorMsg(String msg, int flag) {
        dismissLoadingDialog();
        if (isLogin(msg)) {
            showActivity(this, LoginActivity.class);
            finish();
            return;
        }
        ViewInject.toast(msg);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pictureSourceDialog != null) {
            pictureSourceDialog.cancel();
        }
        if (pvNoLinkOptions != null && pvNoLinkOptions.isShowing()) {
            pvNoLinkOptions.dismiss();
        }
        pvNoLinkOptions = null;
        pictureSourceDialog = null;
        if (pvLinkOptions != null && pvLinkOptions.isShowing()) {
            pvLinkOptions.dismiss();
        }
        pvLinkOptions = null;
        if (mThread != null) {
            mThread.interrupted();
        }
        mThread = null;
    }
}
