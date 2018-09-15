package com.citzx.cslibrary.net;


import android.content.Context;

import com.citzx.cslibrary.core.LibraryApplication;
import com.citzx.cslibrary.utils.MTextUtils;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.utils.L;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;


public class XutilHttpHelp {
	private XutilHttpHelp() {}
	private static XutilHttpHelp single=null;
	private static  Context mContext;
	/**
	 * �����Ķ���
	 */
	public void setMethod(HttpMethod method) {
		this.method = method;
	}

	public HttpMethod method=HttpMethod.POST;

	//��̬��������
	public static XutilHttpHelp getInstance() {
		if (single == null) {
			single = new XutilHttpHelp();
		}
		return single;
	}
	private Callback.Cancelable cancelable;
	/**
	 * @param Mparams
	 * @param requestCallBack
	 * @param <T>
	 * @return
	 */
	public <T> Callback.Cancelable BaseInfoHttp(final RequestParams Mparams,final Context context,
												final RequestCallBack<T> requestCallBack) {
		mContext=context;
		Mparams.setConnectTimeout(20 * 1000);
//		Mparams.setCharset("GB2312");
		if (method==HttpMethod.GET){
			Mparams.setCacheMaxAge(0);
			if (!MTextUtils.isEmpty(LibraryApplication.getToken())){
				Mparams.addQueryStringParameter("Token",LibraryApplication.getToken());
			}
		}else if(method==HttpMethod.POST){
			if (!MTextUtils.isEmpty(LibraryApplication.getToken())){
				Mparams.addBodyParameter("Token",LibraryApplication.getToken());
			}
		}
		L.d(Mparams.toString());
		cancelable = x.http().request(method, Mparams, new Callback.CommonCallback<String>() {
			@Override
			public void onSuccess(String result) {
				L.d("onSuccess");
				try {
					L.d(result);
					if (String.class != requestCallBack.mType) {
						Object o = new Gson().fromJson(result, requestCallBack.mType);
						if (o == null) {
							requestCallBack.onError(null, "response null");
							return;
						}
						requestCallBack.onSuccess((T) o);
					} else {
						requestCallBack.onSuccess((T) result);
					}
				} catch (Exception e) {
					e.printStackTrace();
					requestCallBack.onError(e, e.getMessage());
				}
			}
			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				L.e("onError");
				L.e(ex == null ? Mparams.toString() : ex.getMessage() + ":" + Mparams.toString());
				requestCallBack.onError(new Exception(ex), "");
			}
			@Override
			public void onCancelled(CancelledException cex) {
				L.d("onCancelled");
				requestCallBack.onCancelled();
			}
			@Override
			public void onFinished() {
				L.d("onFinished");
				requestCallBack.onFinished();
			}
		});
		return  cancelable;
	}
}