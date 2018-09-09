package com.citzx.cslibrary.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

public class GsonUtils {

	/**
	 * @author jinghq
	 * @category 模板用法，将json装成Bean<BR>
	 *           new TypeToken<T>(){}.getType()
	 * @param json
	 *            目标json字符�?
	 * @param classz
	 *            bean class类型
	 * 
	 * 
	 * */
	public static <T> T parseJson(String json, Type classz) {
		if (MTextUtils.notEmpty(json) && classz != null) {
			return new Gson().fromJson(json, classz);
		} else {
			return null;
		}
	}

	/**
	 * @author jinghq
	 * @category 模板用法，将json装成List&lt;Bean&gt;<BR>
	 *           new TypeToken<T>(){}.getType()
	 * @param json
	 * @param classz
	 * @return
	 */
	public static <T> ArrayList<T> parseJsonList(String json, Type classz) {
		if (MTextUtils.notEmpty(json) && classz != null) {
			return new Gson().fromJson(json, classz);
		} else {
			return null;
		}
	}
	
	/** 
	 * @category 实体类转Json
	 * @param src
	 * @return
	 * @return String
	 */
	public static String toJSON(Object src){
		return new Gson().toJson(src);
	}

	/**
	 * JSON to map
	 * 
	 * @paramjsonObject
	 * @return
	 * @throws JSONException
	 */
	public static Map<String, String> JSONtoMap(String jsonString)
			throws JSONException {

		@SuppressWarnings("unchecked")
		Map<String, String> result = (Map<String, String>) new Gson().fromJson(
				jsonString, new TypeToken<Map<String, String>>() {
				}.getType());
		return result;
	}

	/** 
	 * @category map to JSON
	 * @param map
	 * @return
	 * @return String
	 */
	public static String MapToJSON(Map<String, ?> map) {
		String mapToJson = new Gson().toJson(map);
		return mapToJson;
	}

}
