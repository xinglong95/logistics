package com.yusen.logistics.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.citzx.cslibrary.core.LibraryApplication;
import com.citzx.cslibrary.utils.MSPUtils;
import com.code19.library.L;
import com.yusen.logistics.bean.LoginInfoBean;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


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
    public final static String USERNAME="UserName";//验证令牌
    public final static String PASSWORD="PassWord";//验证令牌
    public static String token="";//验证令牌
    public static String username="";
    public static String password="";

    public static String getUsername() {
        return sp.getString(USERNAME);
    }

    public static void setUsername(String username) {
        LOGApplication.username = username;
        sp.setString(USERNAME, username);
    }

    public static String getPassword() {
        return sp.getString(PASSWORD);
    }

    public static void setPassword(String password) {
        LOGApplication.password = password;
        sp.setString(PASSWORD,password);
    }

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
    public static List<Activity> activities=new ArrayList<>();
    public static void addActivity(Activity activity){
        activities.add(activity);

    }
    public static void removeAllActiviyies(){
        for(Activity activity:activities){
            activity.finish();
        }
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
    /**
     * 移除存在sp的账号密码
     */
    public static void reMoveLoginIUserName(){
        sp.remove(USERNAME);
    }
    /**
     * 移除存在sp的账号密码
     */
    public static void reMoveLoginPassWord(){
        sp.remove(PASSWORD);
    }
}
