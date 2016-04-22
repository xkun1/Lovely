package com.example.kun.lovelier.moudle;

import java.util.List;

/**
 * Created by kun on 2016/4/18.
 */
public class DataBean {

    /**
     * code : 200
     * msg : success
     * newslist : [{"ctime":"2016-03-06 14:11","title":"刘雨欣美女桌面","description":"美女图片","picUrl":"http://m.xxxiao.com/wp-content/uploads/sites/3/2015/06/m.xxxiao.com_2411c2dfab27e4411a27c16f4f87dd22-760x500.jpg","url":"http://m.xxxiao.com/1811"},{"ctime":"2016-03-06 14:11","title":"王馨瑶性感写真壁纸","description":"美女图片","picUrl":"http://m.xxxiao.com/wp-content/uploads/sites/3/2015/04/m.xxxiao.com_6bb61e3b7bce0931da574d19d1d82c884-760x500.jpg","url":"http://m.xxxiao.com/308"},{"ctime":"2016-03-06 14:11","title":"充气娃娃王依萌制服诱惑性爱美女","description":"美女图片","picUrl":"http://m.xxxiao.com/wp-content/uploads/sites/3/2015/04/m.xxxiao.com_b3149ecea4628efd23d2f86e5a72347218-760x500.jpg","url":"http://m.xxxiao.com/775"},{"ctime":"2016-03-06 14:11","title":"[TGOD推女神] 泳池美人虞 75F乳神于姬Una私房泳装","description":"美女图片","picUrl":"http://m.xxxiao.com/wp-content/uploads/sites/3/2015/04/m.xxxiao.com_3e6ffd8abf2d3fbd1040b02edb6bcb66-760x500.jpg","url":"http://m.xxxiao.com/370"},{"ctime":"2016-03-06 14:11","title":"AISS爱丝美女 丝袜美腿\u2026小哲女神","description":"美女图片","picUrl":"http://m.xxxiao.com/wp-content/uploads/sites/3/2015/04/m.xxxiao.com_be45c01a6ad655cf09937e579c486526-760x500.jpg","url":"http://m.xxxiao.com/971"},{"ctime":"2016-03-06 14:11","title":"明星壁纸赵丽颖高清桌面","description":"美女图片","picUrl":"http://m.xxxiao.com/wp-content/uploads/sites/3/2015/06/m.xxxiao.com_4f5d4bcd03ee2ef0c60bfc0e17a00ea6-760x500.jpg","url":"http://m.xxxiao.com/2004"},{"ctime":"2016-03-06 14:11","title":"乳神张优红装演绎古代美女魅惑人心写真","description":"美女图片","picUrl":"http://t1.27270.com/uploads/150725/8-150H5101Q1N6.jpg","url":"http://www.27270.com/ent/meinvtupian/2015/50954.html"},{"ctime":"2016-03-06 14:11","title":"韩国性感车模许允美人体艺术沙龙写真","description":"美女图片","picUrl":"http://m.xxxiao.com/wp-content/uploads/sites/3/2015/05/m.xxxiao.com_26dc4928630738bf68fa09f8b0d93d221-760x500.jpg","url":"http://m.xxxiao.com/1411"},{"ctime":"2016-03-06 14:11","title":"美女诱惑黑色内衣展曼妙身姿艺术照","description":"美女图片","picUrl":"http://t1.27270.com/uploads/150725/8-150H5161423c8.jpg","url":"http://www.27270.com/ent/meinvtupian/2015/51896.html"},{"ctime":"2016-03-06 14:11","title":"气质美女梦幻艺术照迷倒众宅男","description":"美女图片","picUrl":"http://t1.27270.com/uploads/150725/8-150H5155T35Q.jpg","url":"http://www.27270.com/ent/meinvtupian/2015/51901.html"}]
     */

    private int code;
    private String msg;
    /**
     * ctime : 2016-03-06 14:11
     * title : 刘雨欣美女桌面
     * description : 美女图片
     * picUrl : http://m.xxxiao.com/wp-content/uploads/sites/3/2015/06/m.xxxiao.com_2411c2dfab27e4411a27c16f4f87dd22-760x500.jpg
     * url : http://m.xxxiao.com/1811
     */

    private List<NewslistBean> newslist;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<NewslistBean> getNewslist() {
        return newslist;
    }

    public void setNewslist(List<NewslistBean> newslist) {
        this.newslist = newslist;
    }

    public static class NewslistBean {
        private String ctime;
        private String title;
        private String description;
        private String picUrl;
        private String url;

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
