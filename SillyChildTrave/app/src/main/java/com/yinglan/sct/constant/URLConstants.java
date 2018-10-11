package com.yinglan.sct.constant;

/**
 * 用于存放url常量的类
 * Created by ruitu ck on 2016/9/14.
 */
public class URLConstants {
    /**
     * 正式服务器地址URL
     */
    public static String SERVERURL = "http://user.api.shahaizhi.com/";
    public static String SERVERURL1 = "http://www.shahaizhi.tech/";

    /**
     * 测试服务器地址URL
     */
//    public static String SERVERURL = "http://192.168.1.247:8080/";

    /**
     * 请求地址URL
     */
    public static String APIURL = SERVERURL + "api/mobile/";

    /**
     * 获取七牛云key-ok
     */
    public static String QINIUKEY = SERVERURL + "api/public/key/qiniu.do";

    /**
     * 根据融云token获取头像性别昵称
     */
    public static String SYSRONGCLOUD = APIURL + "member/rongCloud.do";

    /**
     * 置换Token  get请求
     */
    public static String REFRESHTOKEN = APIURL + "m=Api&c=User&a=flashToken";


    /**
     * 登录
     */
    public static String USERLOGIN = APIURL + "member/login.do";

    /**
     * 获取会员登录状态
     */
    public static String ISLOGIN = APIURL + "member/islogin.do";

    /**
     * 退出登录
     */
    public static String LOGOUT = APIURL + "member/logout.do";

    /**
     * 第三方登录
     */
    public static String USERTHIRDLOGIN = APIURL + "member/thirdLogin.do";

    /**
     * 获取第三方登录验证码
     */
    public static String THIRDCODE = APIURL + "member/thirdCode.do";

    /**
     * 短信验证码【手机号注册】
     * 验证码类型 reg=注册 resetpwd=找回密码 login=登陆 bind=绑定手机号.
     */
    public static String SENDREGISTER = APIURL + "member/send-register-code.do";

    /**
     * 短信验证码【找回、修改密码】
     * 验证码类型 reg=注册 resetpwd=找回密码 login=登陆 bind=绑定手机号.
     */
    public static String SENDFINFDCODE = APIURL + "member/send-find-code.do";

    /**
     * 用户注册
     */
    public static String REGISTER = APIURL + "member/mobile-register.do";

    /**
     * 用户注册协议
     */
    public static String REGISTPROTOOL = SERVERURL1 + "dist/pages/registProtocol.html";

    /**
     * 更改密码【手机】
     */
    public static String USERRESTPWD = APIURL + "member/mobile-change-pass.do";

    /**
     * 获取首页信息
     */
    public static String HOMEPAGE = APIURL + "home/get_home_data.do";

    /**
     * 城市与机场 - 获取国家列表
     */
    public static String AIRPOTCOUNTRYLIST = APIURL + "airport/get_country_list.do";

    /**
     * 城市与机场 - 通过国家编号获取城市与机场信息
     */
    public static String AIRPOTBYCOUNTRYID = APIURL + "airport/get_airport_by_countryId.do";

    /**
     * 包车服务 - 获取城市包车列表
     */
    public static String REGIONBYCOUNTRYID = APIURL + "airport/get_region.do";

    /**
     * 精品线路 - 获取精品线路城市列表
     */
    public static String ROUTEREGION = APIURL + "airport/get_route_region.do";

    /**
     * 精品线路 - 获取精品线路商品列表
     */
    public static String ROUTELIST = APIURL + "products/get_route_list.do";

    /**
     * 精品线路 - 获取精品线路商品列表
     */
    public static String PRODUCTLINEDETAILS = APIURL + "products/get_product_details.do";

    /**
     * 精品线路 - 线路详细信息
     */
    public static String ROUTEDETAIL = APIURL + "products/get_route_detail.do";

    /**
     * 城市与机场 - 通过国家编号获取城市与机场信息
     */
    public static String PRODUCTBYAIRPORTID = APIURL + "products/get_product_by_airport_id.do";

    /**
     * 搜索 - 获取某商品列表
     */
    public static String PRODUCTBYTYPE = APIURL + "products/get_product_by_type.do";

    /**
     * 包车服务 - 通过城市的编号来获取产品信息
     */
    public static String PRODUCTBYREGION = APIURL + "products/get_product_by_region.do";

    /**
     * 城市与机场 - 通过国家编号获取城市与机场信息
     */
    public static String PRODUCTDETAILS = APIURL + "products/get_product_details.do";

    /**
     * 接机产品 -  获取某商品的评价列表
     */
    public static String EVALUATIONPAGE = APIURL + "evaluation/get_evaluation_page.do";

    /**
     * 接机产品 - 用户填写接机预定信息
     */
    public static String ADDREQUIREMENTS = APIURL + "products/add_requirements.do";

    /**
     * 精品线路 - 用户填写线路需求
     */
    public static String ADDROUTEREQUIREMENTS = APIURL + "products/add_route_requirement.do";

    /**
     * 包车服务 - 用户填写包车需求
     */
    public static String ADDCARREQUIREMENTS = APIURL + "products/add_car_requirements.do";


    /**
     * 接机---支付订单
     */
    public static String TRAVELORDERDETAIL = APIURL + "products/get_travel_order_detail.do";


    /**
     * 支付订单 - 创建订单
     */
    public static String CREATETRAVEORDER = APIURL + "products/create_travel_order.do";

    /**
     * 订单支付信息接口
     */
    public static String ONLINEPAY = APIURL + "online/pay.do";

    /**
     * 大洲与国家 - 获取大洲信息
     */
    public static String COUNTRYAREALIST = APIURL + "airport/get_country_area_list.do";

    /**
     * 大洲与国家 - 获取大洲下面的数据
     */
    public static String COUNTRYAREALISTBYPARENTID = APIURL + "airport/get_country_area_list_by_parentid.do";

    /**
     * 大洲与国家 - 获取用户搜索的城市
     */
    public static String AREABYNAME = APIURL + "airport/get_area_by_name.do";

    /**
     * 获取偏好列表
     */
    public static String CATEGORYLIST = APIURL + "travel/get_category_list.do";

    /**
     * 用户填写定制要求
     */
    public static String ADDCUSTOMIZED = APIURL + "travel/add_customized.do";

    /**
     * 社区----分类信息列表
     */
    public static String CLASSIFITCATIONLIST = APIURL + "classification/get_classification_list.do";

    /**
     * 社区----发动态分类信息列表
     */
    public static String POSTCLASSIFITCATIONLIST = APIURL + "classification/get_post_classification_list.do";

    /**
     * 社区----帖子列表
     */
    public static String POSTLIST = APIURL + "post/get_post_list.do";

    /**
     * 社区----取消收藏帖子
     */
    public static String UNFAVORIT = APIURL + "favorite/unfavorite.do";

    /**
     * 社区----收藏帖子
     */
    public static String FAVORITADD = APIURL + "favorite/add.do";

    /**
     * 社区----检索会员的信息
     */
    public static String MEMBERLIST = APIURL + "post/get_member_list.do";

    /**
     * 社区----获取帖子详情
     */
    public static String POSTDETAIL = APIURL + "post/get_post_detail.do";

    /**
     * 社区----关注或取消关注
     */
    public static String ADDCONCERN = APIURL + "concern/add_concern.do";

    /**
     * 社区----获取其他用户信息
     */
    public static String OTHERUSERINFO = APIURL + "post/get_other_user_info.do";

    /**
     * 社区----获取用户帖子列表
     */
    public static String OTHERUSERPOST = APIURL + "post/get_other_user_post.do";

    /**
     * 社区----点赞和取消
     */
    public static String ADDLIKE = APIURL + "like/add_like.do";

    /**
     * 社区----给评论点赞
     */
    public static String ADDCOMMRENTLIKE = APIURL + "comment/add_commentLike.do";

    /**
     * 社区----添加评论
     */
    public static String ADDCOMMENT = APIURL + "comment/add_comment.do";

    /**
     * 社区----举报用户帖子
     */
    public static String REPORT = APIURL + "post/report.do";

    /**
     * 社区----获取帖子评论列表
     */
    public static String POSTCOMMENT = APIURL + "comment/get_post_comment.do";

    /**
     * 社区----获取帖视频评论列表
     */
    public static String VIDEOCOMMENT = APIURL + "comment/get_video_comment.do";

    /**
     * 获取某一个评论的详细信息
     */
    public static String CINMENTDETAIL = APIURL + "comment/get_comment_detail.do";

    /**
     * 获取我关注的用户列表
     */
    public static String MYCONCERNLIST = APIURL + "concern/get_my_concern_list.do";


    /**
     * 获取系统消息首页
     */
    public static String NEWLISTBUYTITLE = APIURL + "news/listByTitle.do";

    /**
     * 获取消息列表
     */
    public static String NEWTITLE = APIURL + "news/title.do";

    /**
     * 选中某条消息并设为已读
     */
    public static String NEWSELECT = APIURL + "news/select.do";

    /**
     * 活动  活动
     */
    public static String PROCESSACTIVITY = APIURL + "activity/get_processing_activity.do";

    /**
     * 活动  往期精彩
     */
    public static String ALLACTIVITY = APIURL + "activity/get_all_activity.do";

    /**
     * 活动---活动详情
     */
    public static String ACTIVITYDETAILS = SERVERURL1 + "activity_share/activity_share.html?id=";


    /**
     * 获取个人信息
     */
    public static String MEMBERINFO = APIURL + "member/get.do";

    /**
     * 获取钱包余额
     */
    public static String PURSEGET = APIURL + "purse/get.do";

    /**
     * 提交意见反馈
     */
    public static String ADVICEPOST = SERVERURL + "api/member/advice/post.do";

    /**
     * 关于我们
     */
    public static String ABOUTUS = SERVERURL1 + "dist/pages/about_us.html";

    /**
     * 帮助中心
     */
    public static String HELP = SERVERURL1 + "dist/pages/help.html";

    /**
     * 帮助中心详情
     */
    public static String HELPDETAIL = SERVERURL1 + "dist/pages/helpDetal.html";

    /**
     * VIP救助电话
     */
    public static String VIPEMERGENCYCALL = SERVERURL1 + "pages/rescue_phone.html";

    /**
     * 分享有礼
     */
    public static String SHARE = SERVERURL1 + "html/share.html?icode=";

    /**
     * 分享有礼分享网址
     */
    public static String REGISTERHTML = SERVERURL1 + "html/login.html?icode=";
}
