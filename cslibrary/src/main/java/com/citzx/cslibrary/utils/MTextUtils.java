package com.citzx.cslibrary.utils;

import android.net.Uri;
import android.text.TextUtils;

import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @category 通用文本处理类
 * */
public class MTextUtils {

	/** @category 中文字符 */
	public static int TEXT_CHINESE = 0;
	/** @category 英文字符 */
	public static int TEXT_ABC = 1;
	/** @category 数字字符 */
	public static int TEXT_NUMBER = 2;

	public static String superToString(Object obj) {
		Class<?> cls = obj.getClass();
		Field[] fs = cls.getDeclaredFields();
		String name = cls.getSimpleName();

		String content = "[" + name + "]";

		try {
			for (Field f : fs) {
				String mName = "" + f.getName();
				f.setAccessible(true);// 设置访问权限,将私有属性公开
				content += "," + mName + ":" + f.get(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "" + content;
	}

	/**
	 * @category 以Android URI编码方式 编码URL 默认为UTF-8 没有跳过的字符
	 * */
	public static String uriEncode(String text) {
		return uriEncode(text, null);
	}

	public static String urlEncode(String text, String charSet) {
		try {
			if (MTextUtils.isEmpty(charSet)) {
				charSet = "UTF-8";
			}
			if (MTextUtils.isEmpty(text)) {
				text = "";
			}
			return URLEncoder.encode(text, charSet);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 以Android URI编码方式 编码URL 默认为UTF-8 没有跳过的字符
	 * 
	 * @paramtext需要转义的字符
	 * @param allow
	 *            跳过的字符（不转义）
	 * @return
	 */
	public static String uriEncode(String text, String allow) {
		try {
			return Uri.encode(text, allow);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * @category 验证是不是Email
	 * @param email
	 *            email字符串
	 * @return 是否匹配
	 * */
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";

		if (email == null) {
			return false;
		} else {
			return RegexCheckValidity(email, str);
		}

	}

	/**
	 * 验证是不是中国大陆正确的手机号
	 * 
	 * @param PhoneNum
	 * @return
	 */
	public static boolean isPhone(String PhoneNum) {
		String str = "^((13[0-9])|(147)|(15[0-9])|(17[0678])|(18[0-9]))\\d{8}$";
//		String str="^1([38]\\d|4[57]|5[0-35-9]|7[06-8])(\\-\\d{4}){2}$/";
		if (PhoneNum == null) {
			return false;
		} else {
			return RegexCheckValidity(PhoneNum, str);
		}

	}

	/**
	 * @category 过滤字符串符合正则模式的部分
	 * @param type
	 *            正则模式 0中文，1英文，2数字
	 * @return String
	 * @author jinghq
	 * */
	public static String RegexCheck(String data, int type) {
		switch (type) {
		case 0:
			// TODO 这里手动处理一下正则不能过滤出来的字符
			/* 中文后加x会过滤到的中文有x，不知道是什么原因 */
			String[] sps = { ",", "，", ".", "。", "“", "”", "\"", "-", "+", "{",
					"}", "『", "』", "?", "？", "×", "*", "x", "y", "!", "@", "#",
					"$", "^", "z", "v", "w", "~", "%", "&", "(", ")", "|", "'",
					"/", "～", "！", "￥", "…", "（", "）", "—", "：", ".", "》", "《",
					"；", "‘", "【", "】", "、" };
			for (String s : sps) {
				data = data.replace(s, "");
			}
			return RegexCheck(data, "[u4e00-u9fa5]");
		case 1:
			return RegexCheck(data, "[^A-Za-z]");
		case 2:
			return RegexCheck(data, "[^0-9]");
		}
		return data;
	}

	/**
	 * @author jinghq
	 * @category 根据正则检查数据有效性
	 * @param data
	 *            校验目标字符串
	 * @param regex
	 *            正则表达式
	 * @return boolean 是否匹配正则
	 * */
	public static boolean RegexCheckValidity(String data, String regex) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(data);
		return m.matches();
	}

	/**
	 * @category 过滤字符串 符合 正则部分的字符
	 * @param regex
	 *            正则表达式
	 * @return String
	 * @author jinghq
	 * */
	public static String RegexCheck(String data, String regex) {
		try {
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(data);
			return m.replaceAll("").trim();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * Returns true if the string is null or 0-length
	 * 
	 * @param text
	 * @return
	 */
	public static boolean isEmpty(String text) {
		if (text == null||text.equals("&nbsp;")||text.equals("")||text.equals("null")||text.equals("nullm/s")||text.equals("nullkg")) {
			return true;
		} else {
			return TextUtils.isEmpty(text);
		}
	}

	/**
	 * Returns true if the string is null or 0-length or without space is null
	 * 
	 * @param text
	 * @return
	 */
	public static boolean isAbsolutelyEmpty(String text) {
		if (TextUtils.isEmpty(text)) {
			return true;
		} else {
			if (TextUtils.isEmpty(text.replaceAll(" ", ""))) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * Returns true if the list is null or 0-size
	 * 
	 * @param list
	 * @return
	 */
	public static boolean isEmpty(List<Object> list) {
		boolean is = true;
		if (list != null && list.size() > 0) {
			is = false;
		}
		return is;
	}

	/**
	 * Returns true if the text is not null
	 * 
	 * @param text
	 * @return
	 */
	public static boolean notEmpty(String text) {
		return !isEmpty(text);
	}

	/**
	 * Returns (int)value ,if the str is wrong return def 将Sting 转化为int
	 * 类型，def为默认以及转化失败得到的数值
	 * 
	 * @param str
	 * @param def
	 * @return
	 */
	public static int formatInt(String str, int def) {
		int ret = def;
		try {
			ret = Integer.parseInt(str);
		} catch (Exception e) {
			ret = def;
		}
		return ret;
	}

	/**
	 * 将Sting 转化为int 类型，0为转化失败得到的数值
	 * 
	 * @param str
	 * @return
	 */
	public static int formatInt(String str) {

		return formatInt(str, 0);
	}

	/**
	 * @category String列表转String
	 * @paramList
	 *            <String> list
	 * @return String
	 * */
	public static String ListToString(List<String> list) {
		String res = "";

		for (String str : list) {
			res += "" + str + ";";
		}
		return res;
	}

	/**
	 * @category Cookies String转String哈希表
	 * @paramString
	 *            param
	 * @return HashMap<String, String>
	 * */
	public static HashMap<String, String> String2HashMap(String param) {
		HashMap<String, String> map = new HashMap<String, String>();
		if (notEmpty(param)) {
			String[] kv = param.split("&");
			if (kv.length > 0) {
				for (String ks : kv) {
					String[] k = ks.split("=");
					if (k.length == 2) {
						map.put(k[0], k[1]);
					}
				}
			}
		}
		return map;
	}
	public static String YinCangTel(String tel){
		String returntel=null;
		if(!TextUtils.isEmpty(tel) && tel.length() > 6 ){
			StringBuilder sb  =new StringBuilder();
			for (int i = 0; i < tel.length(); i++) {
				char c = tel.charAt(i);
				if (i >= 3 && i <= 6) {
					sb.append('*');
				} else {
					sb.append(c);
				}
			}
			returntel=sb.toString();
		}
		return returntel;
	}
}
