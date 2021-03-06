package com.yinglan.sct.entity.homepage.boutiqueline;

import com.common.cklibrary.entity.BaseResult;

import java.util.List;

public class DueDemandBean extends BaseResult<DueDemandBean.DataBean> {


    public class DataBean {
        /**
         * service_description : 服务说明服务说明服务说明服务说明服务说明服务说明服务说明服务说明服务说明服务说明服务说明服务说明服务说明服务说明服务说明服务说明服务说明服务说明服务说明服务说明服务说明服务说明服务说明服务说明服务说明服务说明服务说明服务说明服务说明服务说明服务说明服务说明服务说明服务说明服务说明
         * passenger_number : 5
         * review_count : 1
         * title : 日本东京
         * baggage_number : 2
         * picture : ["http://img.shahaizhi.com/FunhyNhzfv8WldU1JGnTD3XVebkD","http://img.shahaizhi.com/FrWs95yWS4yejCaO3BJTCasCfj-0","http://img.shahaizhi.com/FmUzSKFwhIY9aJwMiLIvqXRsEQ75","http://img.shahaizhi.com/FqfG2lnm_E-fZ_jWlBeR81bt7lXn","http://img.shahaizhi.com/Fhz-ISZdjqQ617mdjJTyoO6HweAi","http://img.shahaizhi.com/FvYRAD-2HjShs9DvUQ2Kzodo9P51","http://img.shahaizhi.com/Fu72EZOO_V_I2XbFRQfUrkJo88PZ","http://img.shahaizhi.com/FtGnTVeBKP_4YRt4Con8a0IHGGD1","http://img.shahaizhi.com/FkYFLE6amYzTqsaAFHukeOQnAKw7"]
         * review_list : [{"id":7,"content":"图片测试4张图片测试4张图片测试4张图片测试4张图片测试4张图片测试4张图片测试4张图片测试4张图片测试4张图片测试4张图片测试4张图片测试4张图片测试4张图片测试4张图片测试4张图片测试4张图片测试4张图片测试4张","satisfaction_level":4,"create_time":"1535357219","nickname":"天若有情","face":"http://img.shahaizhi.com/FleRaZdDvarW8yR3IOxH0Ti-LqZ2","like_number":0,"is_like":false}]
         * service_note : 服务备注服务备注服务备注服务备注服务备注服务备注服务备注服务备注服务备注服务备注服务备注服务备注服务备注服务备注服务备注服务备注服务备注服务备注服务备注服务备注服务备注服务备注服务备注服务备注服务备注服务备注服务备注服务备注服务备注服务备注服务备注服务备注服务备注
         * service_policy_content : <p style="font-size:15px;color:green;">&nbsp;&nbsp;&nbsp;&nbsp;这个是简短的退订政策，该订单不支持退订，请与服务人员了解详情</p>
         * passenger : 5人2行李
         * price : 0.01
         * service : [{"is_show":1,"name":"小费"},{"is_show":1,"name":"燃油费"},{"is_show":1,"name":"过路费"},{"is_show":1,"name":"停车费"},{"is_show":1,"name":"举牌"},{"is_show":0,"name":"车载WIFI"},{"is_show":1,"name":"雨伞"},{"is_show":0,"name":"其他"}]
         * product_id : 4
         * model : 丰田阿尔法
         * service_policy : 不支持退订
         */

        private String service_description;
        private int passenger_number;
        private int review_count;
        private String title;
        private int baggage_number;
        private String service_note;
        private String service_policy_content;
        private String passenger;
        private String price;
        private int product_id;
        private String model;
        private String service_policy;
        private List<String> picture;
        private List<ReviewListBean> review_list;
        private List<ServiceBean> service;

        public String getService_description() {
            return service_description;
        }

        public void setService_description(String service_description) {
            this.service_description = service_description;
        }

        public int getPassenger_number() {
            return passenger_number;
        }

        public void setPassenger_number(int passenger_number) {
            this.passenger_number = passenger_number;
        }

        public int getReview_count() {
            return review_count;
        }

        public void setReview_count(int review_count) {
            this.review_count = review_count;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getBaggage_number() {
            return baggage_number;
        }

        public void setBaggage_number(int baggage_number) {
            this.baggage_number = baggage_number;
        }

        public String getService_note() {
            return service_note;
        }

        public void setService_note(String service_note) {
            this.service_note = service_note;
        }

        public String getService_policy_content() {
            return service_policy_content;
        }

        public void setService_policy_content(String service_policy_content) {
            this.service_policy_content = service_policy_content;
        }

        public String getPassenger() {
            return passenger;
        }

        public void setPassenger(String passenger) {
            this.passenger = passenger;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public int getProduct_id() {
            return product_id;
        }

        public void setProduct_id(int product_id) {
            this.product_id = product_id;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getService_policy() {
            return service_policy;
        }

        public void setService_policy(String service_policy) {
            this.service_policy = service_policy;
        }

        public List<String> getPicture() {
            return picture;
        }

        public void setPicture(List<String> picture) {
            this.picture = picture;
        }

        public List<ReviewListBean> getReview_list() {
            return review_list;
        }

        public void setReview_list(List<ReviewListBean> review_list) {
            this.review_list = review_list;
        }

        public List<ServiceBean> getService() {
            return service;
        }

        public void setService(List<ServiceBean> service) {
            this.service = service;
        }

        public class ReviewListBean {
            /**
             * id : 7
             * content : 图片测试4张图片测试4张图片测试4张图片测试4张图片测试4张图片测试4张图片测试4张图片测试4张图片测试4张图片测试4张图片测试4张图片测试4张图片测试4张图片测试4张图片测试4张图片测试4张图片测试4张图片测试4张
             * satisfaction_level : 4
             * create_time : 1535357219
             * nickname : 天若有情
             * face : http://img.shahaizhi.com/FleRaZdDvarW8yR3IOxH0Ti-LqZ2
             * like_number : 0
             * is_like : false
             */

            private int id;
            private String content;
            private int satisfaction_level;
            private String create_time;
            private String nickname;
            private String face;
            private int like_number;
            private boolean is_like;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getSatisfaction_level() {
                return satisfaction_level;
            }

            public void setSatisfaction_level(int satisfaction_level) {
                this.satisfaction_level = satisfaction_level;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getFace() {
                return face;
            }

            public void setFace(String face) {
                this.face = face;
            }

            public int getLike_number() {
                return like_number;
            }

            public void setLike_number(int like_number) {
                this.like_number = like_number;
            }

            public boolean isIs_like() {
                return is_like;
            }

            public void setIs_like(boolean is_like) {
                this.is_like = is_like;
            }
        }

        public class ServiceBean {
            /**
             * is_show : 1
             * name : 小费
             */

            private int is_show;
            private String name;

            public int getIs_show() {
                return is_show;
            }

            public void setIs_show(int is_show) {
                this.is_show = is_show;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
