package com.example.kun.lovelier.view;

import android.app.Application;

import com.umeng.socialize.PlatformConfig;


/**
 * Created by kun on 2016/4/18.
 */
public class MyAppContext extends Application {

    //增加分享

    {
//        PlatformConfig.setWeixin("***", "***");
//        //微信 appid appsecret
//        PlatformConfig.setSinaWeibo("***","***");
        //新浪微博 appkey appsecret
        PlatformConfig.setQQZone("1105320835", "qzCN0uJXdmQ07WX8");
        // QQ和Qzone appid appkey
    }



    @Override
    public void onCreate() {
        super.onCreate();

    }


}



