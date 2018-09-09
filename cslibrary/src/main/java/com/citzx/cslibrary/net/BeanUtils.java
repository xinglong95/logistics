package com.citzx.cslibrary.net;

import android.content.Context;

import com.citzx.cslibrary.utils.GsonUtils;
import com.code19.library.L;

import java.lang.reflect.Type;

public class BeanUtils<T,K> {
	private NetBean<T,K> netBean = null;
	Context context;
	String json;

	/** 
	 * <默认构造函数>
	 * 
	 * 用法:
	 * String Json=JSON数据;
	 * LoginBean 为demo实体类
	 * NetBean<LoginBean, LoginBean> esponseT = new BeanUtils<LoginBean, LoginBean>(context, Json,
					new TypeToken<NetBean<LoginBean, LoginBean>>() {
					}.getType()).getNetBean();

	 */
	public BeanUtils(Context context,String json,Type classz) {
		this.context = context;
		this.netBean=new NetBean<T,K>();
		getBean(json, classz);
	}

	private void getBean(String json,Type classz) {
		try {
			netBean= GsonUtils.parseJson(
					json,
					classz);
		} catch (Exception e) {
			L.e("BeanUtils getBean" + e.getMessage(), e+"");
			L.e("BeanUtils getBean", "JSON数据格式错误");
			netBean.setOk(false);
			netBean.setInfo("Server  JSON format error");
		}
	}

	public NetBean<T,K> getNetBean() {
		
		return netBean;
	}

	
}
