package com.citzx.cslibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Map;

/**
 * @author jinghq
 * @category 通用配置管理类
 * */
public class MSPUtils {

	public SharedPreferences sp = null;
	// 默认使用工具集配置文件
	private String name = "LOG";
	Context context = null;

	public MSPUtils(Context context) {
		this.context = context;
		init();
	}

	public MSPUtils(Context context, String name) {
		this.context = context;
		setName(name);
		init();
	}

	public void init() {
		sp = context.getSharedPreferences(name,
//				android.content.Context.MODE_PRIVATE);
				Context.MODE_MULTI_PROCESS);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @category 删除配置项
	 * */
	public void remove(String key) {
		Editor editor = sp.edit();
		editor.remove(key);
		editor.commit();
	}

	/**
	 * 获取sp中指定key的long型数据
	 * 
	 * @category 获取sp中指定key的long型数据
	 * @param key
	 *            键
	 * @return long
	 * */
	public long getLong(String key) {
		return sp.getLong(key, 0);
	}

	/**
	 * 设置sp中指定key的long型数据
	 * 
	 * @category 设置sp中指定key的long型数据
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * */
	public void setLong(String key, long value) {
		Editor editor = sp.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	public String getString(String key) {
		return sp.getString(key, "");
	}

	public void setString(String key, String value) {
		Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}
	


	public int getInt(String key) {
		return sp.getInt(key, 0);
	}

	public void setInt(String key, int value) {
		Editor editor = sp.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public float getFloat(String key) {
		return sp.getFloat(key, 0);
	}

	public void setInt(String key, float value) {
		Editor editor = sp.edit();
		editor.putFloat(key, value);
		editor.commit();
	}

	public boolean getBoolean(String key) {
		return getBoolean(key, false);
	}
	public boolean getBoolean(String key, Boolean def) {
		return sp.getBoolean(key, def);
	}
	public void setBoolean(String key, boolean value) {
		Editor editor = sp.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public Map<String, ?> getAll() {
		return sp.getAll();
	}

}
