package com.yinglan.sct.entity.community.dynamic.dynamiccomments;

import com.common.cklibrary.entity.BaseResult;

import java.util.List;

public class DynamicCommentsBean extends BaseResult<DynamicCommentsBean.DataBean> {

    public class DataBean {
        /**
         * totalPageCount : 1
         * list : [{"body":"如图去学校","create_time":"2018-07-25 14:40:36","face":"http://img.shahaizhi.com/SHZS_M14_IMG_20180703_142510.jpg","id":6,"member_id":14,"nickname":"天若有情","post_id":1,"reply_comment_id":0,"reply_member_id":0,"type":1},{"body":"如图","create_time":"2018-07-25 14:40:24","face":"http://img.shahaizhi.com/SHZS_M14_IMG_20180703_142510.jpg","id":5,"member_id":14,"nickname":"天若有情","post_id":1,"reply_comment_id":0,"reply_member_id":0,"type":1},{"body":"这是一条主评论","create_time":"2018-07-23 09:02:30","face":"http://thirdqq.qlogo.cn/qqapp/1106946302/540B2058F11FE47600B4EC1D65A17835/100","id":2,"member_id":13,"nickname":"15675198215","post_id":1,"replyList":[{"id":3,"member_id":14,"nickname":"天若有情","post_id":"1","reply_body":"这是一条子评论","reply_comment_id":2,"reply_face":"http://img.shahaizhi.com/SHZS_M14_IMG_20180703_142510.jpg","reply_member_id":13,"reply_nickname":"15675198215","reply_time":"2018-07-23 09:02:50","type":1},{"id":4,"member_id":13,"nickname":"15675198215","post_id":"1","reply_body":"回复的回复","reply_comment_id":2,"reply_face":"http://thirdqq.qlogo.cn/qqapp/1106946302/540B2058F11FE47600B4EC1D65A17835/100","reply_member_id":14,"reply_nickname":"天若有情","reply_time":"2018-07-23-10:55:50","type":1}],"reply_comment_id":0,"reply_member_id":0,"type":1},{"body":"这是一条简单的评论","create_time":"2018-07-23 09:02:20","face":"http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83erVaYiaEpP1VibXTX4yFHPicaShMSQYgmzwbVuB3UCbGtMQmFP6K8c4VATujp09iatnf5P2mekR2licv7g/132","id":1,"member_id":12,"nickname":"粉红色叶子","post_id":1,"reply_comment_id":0,"reply_member_id":0,"type":1}]
         * totalCount : 4
         * currentPageNo : 1
         */

        private int totalPageCount;
        private int totalCount;
        private int currentPageNo;
        private List<ListBean> list;

        public int getTotalPageCount() {
            return totalPageCount;
        }

        public void setTotalPageCount(int totalPageCount) {
            this.totalPageCount = totalPageCount;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public int getCurrentPageNo() {
            return currentPageNo;
        }

        public void setCurrentPageNo(int currentPageNo) {
            this.currentPageNo = currentPageNo;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public class ListBean {
            /**
             * body : 如图去学校
             * create_time : 2018-07-25 14:40:36
             * face : http://img.shahaizhi.com/SHZS_M14_IMG_20180703_142510.jpg
             * id : 6
             * member_id : 14
             * nickname : 天若有情
             * post_id : 1
             * reply_comment_id : 0
             * reply_member_id : 0
             * type : 1
             * replyList : [{"id":3,"member_id":14,"nickname":"天若有情","post_id":"1","reply_body":"这是一条子评论","reply_comment_id":2,"reply_face":"http://img.shahaizhi.com/SHZS_M14_IMG_20180703_142510.jpg","reply_member_id":13,"reply_nickname":"15675198215","reply_time":"2018-07-23 09:02:50","type":1},{"id":4,"member_id":13,"nickname":"15675198215","post_id":"1","reply_body":"回复的回复","reply_comment_id":2,"reply_face":"http://thirdqq.qlogo.cn/qqapp/1106946302/540B2058F11FE47600B4EC1D65A17835/100","reply_member_id":14,"reply_nickname":"天若有情","reply_time":"2018-07-23-10:55:50","type":1}]
             */

            private String body;
            private String create_time;
            private String face;
            private int id;
            private int member_id;
            private String nickname;
            private int post_id;
            private int reply_comment_id;
            private int reply_member_id;
            private int type;
            private String comment_like_number;
            private int is_comment_like;

            public String getComment_like_number() {
                return comment_like_number;
            }

            public void setComment_like_number(String comment_like_number) {
                this.comment_like_number = comment_like_number;
            }

            public int getIs_comment_like() {
                return is_comment_like;
            }

            public void setIs_comment_like(int is_comment_like) {
                this.is_comment_like = is_comment_like;
            }

            private List<ReplyListBean> replyList;

            public String getBody() {
                return body;
            }

            public void setBody(String body) {
                this.body = body;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public String getFace() {
                return face;
            }

            public void setFace(String face) {
                this.face = face;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getMember_id() {
                return member_id;
            }

            public void setMember_id(int member_id) {
                this.member_id = member_id;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public int getPost_id() {
                return post_id;
            }

            public void setPost_id(int post_id) {
                this.post_id = post_id;
            }

            public int getReply_comment_id() {
                return reply_comment_id;
            }

            public void setReply_comment_id(int reply_comment_id) {
                this.reply_comment_id = reply_comment_id;
            }

            public int getReply_member_id() {
                return reply_member_id;
            }

            public void setReply_member_id(int reply_member_id) {
                this.reply_member_id = reply_member_id;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public List<ReplyListBean> getReplyList() {
                return replyList;
            }

            public void setReplyList(List<ReplyListBean> replyList) {
                this.replyList = replyList;
            }

            public class ReplyListBean {
                /**
                 * id : 3
                 * member_id : 14
                 * nickname : 天若有情
                 * post_id : 1
                 * reply_body : 这是一条子评论
                 * reply_comment_id : 2
                 * reply_face : http://img.shahaizhi.com/SHZS_M14_IMG_20180703_142510.jpg
                 * reply_member_id : 13
                 * reply_nickname : 15675198215
                 * reply_time : 2018-07-23 09:02:50
                 * type : 1
                 */

                private int id;
                private int member_id;
                private String nickname;
                private String post_id;
                private String reply_body;
                private int reply_comment_id;
                private String reply_face;
                private int reply_member_id;
                private String reply_nickname;
                private String reply_time;
                private int type;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getMember_id() {
                    return member_id;
                }

                public void setMember_id(int member_id) {
                    this.member_id = member_id;
                }

                public String getNickname() {
                    return nickname;
                }

                public void setNickname(String nickname) {
                    this.nickname = nickname;
                }

                public String getPost_id() {
                    return post_id;
                }

                public void setPost_id(String post_id) {
                    this.post_id = post_id;
                }

                public String getReply_body() {
                    return reply_body;
                }

                public void setReply_body(String reply_body) {
                    this.reply_body = reply_body;
                }

                public int getReply_comment_id() {
                    return reply_comment_id;
                }

                public void setReply_comment_id(int reply_comment_id) {
                    this.reply_comment_id = reply_comment_id;
                }

                public String getReply_face() {
                    return reply_face;
                }

                public void setReply_face(String reply_face) {
                    this.reply_face = reply_face;
                }

                public int getReply_member_id() {
                    return reply_member_id;
                }

                public void setReply_member_id(int reply_member_id) {
                    this.reply_member_id = reply_member_id;
                }

                public String getReply_nickname() {
                    return reply_nickname;
                }

                public void setReply_nickname(String reply_nickname) {
                    this.reply_nickname = reply_nickname;
                }

                public String getReply_time() {
                    return reply_time;
                }

                public void setReply_time(String reply_time) {
                    this.reply_time = reply_time;
                }

                public int getType() {
                    return type;
                }

                public void setType(int type) {
                    this.type = type;
                }
            }
        }
    }
}
