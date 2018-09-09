package com.citzx.cslibrary.net;

import com.code19.library.L;
import com.google.gson.internal.$Gson$Types;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 网络请求回调
 * Created by Goodsrc on 2016/4/12.
 */
public abstract class RequestCallBack<T> {
    public Type mType;

    public RequestCallBack() {
        mType = getSuperclassTypeParameter(getClass());
    }

    /**
     *等待
     */
    public void onWaiting() {

    }

    /**
     * 开始
     */
    public void onStarted() {
    }

    /**
     * 已取消
     */
    public void onCancelled() {
    }

    /**
     * 正在加载
     * @param total 总进度
     * @param current 当前进度
     * @param isDownloading 是否正在下载
     */
    public void onLoading(long total, long current, boolean isDownloading) {
    }

    /**
     * 请求成功
     * @param result
     */
    public abstract void onSuccess(T result);

    /**
     * 失败
     * @param error 异常
     * @param msg 异常信息
     */
    public  void onError(Exception error, String msg) {
        L.e(msg);
    }

    /**
     * 结束（包括取消、成功、失败）
     */
    public  void   onFinished(){

    }

    public static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }
}
