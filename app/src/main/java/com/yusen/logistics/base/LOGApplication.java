package com.yusen.logistics.base;

import android.app.Application;
import android.content.Context;

import com.citzx.cslibrary.core.LibraryApplication;
import com.citzx.cslibrary.utils.MSPUtils;
import com.code19.library.L;
import com.yusen.logistics.bean.LoginInfoBean;

import org.xutils.x;


/**
 * Created by Administrator on 2017/2/23.
 */

public class LOGApplication extends Application{
    private static LOGApplication mApplication;

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        LOGApplication.context = context;
    }

    private static Context context;
    public static MSPUtils sp = null;
    public final static String TOKEN="Token";//验证令牌
    public static String token="";//验证令牌

    public static LoginInfoBean getUserinfo() {
        return userinfo;
    }

    public static void setUserinfo(LoginInfoBean userinfo) {
        LOGApplication.userinfo = userinfo;
    }

    public static LoginInfoBean userinfo;//用户信息
    public static String getToken() {
        return sp.getString(TOKEN);
    }

    public static void setToken(String str) {
        token = str;
        sp.setString(TOKEN, str);
        LibraryApplication.setToken(str);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mApplication=this;
        LOGApplication.context = mApplication;
        LibraryApplication.init(mApplication);
        x.Ext.init(mApplication);
        context = getApplicationContext();
        sp = new MSPUtils(this);
        L.init(true,"Log");
    }
    /**
     * 移除存在sp的账号密码
     */
    public static void reMoveLoginInfo(){
        sp.remove(TOKEN);
        LibraryApplication.setToken("");
    }
}
