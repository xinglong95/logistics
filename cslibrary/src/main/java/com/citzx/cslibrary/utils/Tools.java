package com.citzx.cslibrary.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import org.xutils.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Locale;

public class Tools {
    /**
     * @param context
     * @param id
     * @return String
     * @category 根据id获取String
     */
    public static String getString(Context context, int id) {
        return context.getResources().getString(id);
    }

    /**
     * @param context
     * @param id
     * @return Drawable
     * @category 根据id获取drawable对象
     */
    public static Drawable getDrawable(Context context, int id) {
        return context.getResources().getDrawable(id);
    }

    /**
     * 获取设备识别码
     *
     * @return
     */
    public static String getDeviceNum() {
        String SerialNumber = android.os.Build.SERIAL;
        return SerialNumber;
    }


    /**
     * @param model 模型
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static boolean CheckNullByReflect(Object model, String[] nulls) {

        Field[] field = model.getClass().getDeclaredFields(); // 获取实体类的所有属性，返回Field数组
        try {
            for (int j = 0; j < field.length; j++) { // 遍历所有属性
                String name = field[j].getName(); // 获取属性的名字
                // Out.d("attribute name" , name);
                String type = field[j].getGenericType().toString(); // 获取属性的类型
                if (type.equals("class java.lang.String")) { // 如果type是类类型，则前面包含"class "，后面跟类名
                    Method m;
                    String nameSub = name.substring(0, 1).toUpperCase(
                            Locale.ENGLISH);
                    name = nameSub + name.substring(1);
                    boolean isnull = false;
                    if (nulls.length != 0) {
                        for (int i = 0; i < nulls.length; i++) {
                            if (name.equals(nulls[i])) {
                                isnull = true;
                            }
                        }
                    }
                    if (isnull) continue;
                    m = model.getClass().getMethod("get" + name);
                    String value = (String) m.invoke(model); // 调用getter方法获取属性值
                    if (MTextUtils.isEmpty(value.trim())) {
                        // Out.d("attribute value:" + value);
                        return false;
                    }
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalAccessException e) {

            e.printStackTrace();
            return false;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * @param model 模型
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static ArrayList<String> CheckNullByReflect4Keys(Object model, String[] nulls) {
        ArrayList<String> isNullstrs = new ArrayList<>();
        Field[] field = model.getClass().getDeclaredFields(); // 获取实体类的所有属性，返回Field数组
        try {
            for (int j = 0; j < field.length; j++) { // 遍历所有属性
                String name = field[j].getName(); // 获取属性的名字
                // Out.d("attribute name" , name);
                String type = field[j].getGenericType().toString(); // 获取属性的类型
                if (type.equals("class java.lang.String")) { // 如果type是类类型，则前面包含"class "，后面跟类名
                    Method m;
                    String nameSub = name.substring(0, 1).toUpperCase(
                            Locale.ENGLISH);
                    name = nameSub + name.substring(1);
                    boolean isnull = false;
                    if (nulls.length != 0) {
                        for (int i = 0; i < nulls.length; i++) {
                            if (name.equals(nulls[i])) {
                                isnull = true;
                            }
                        }
                    }
                    if (isnull) continue;
                    m = model.getClass().getMethod("get" + name);
                    String value = (String) m.invoke(model); // 调用getter方法获取属性值
                    if (!MTextUtils.isEmpty(value)) {
                        if (MTextUtils.isEmpty(value.trim())) {
                            // Out.d("attribute value:" + value);
                            isNullstrs.add(name);
                        }
                    } else {
                        isNullstrs.add(name);
                    }

                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return isNullstrs;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return isNullstrs;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return isNullstrs;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return isNullstrs;
        }
        return isNullstrs;
    }

    /**
     * @param model 模型
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static RequestParams testReflect(Object model, RequestParams params) {

        Field[] field = model.getClass().getDeclaredFields(); // 获取实体类的所有属性，返回Field数组
        try {
            for (int j = 0; j < field.length; j++) { // 遍历所有属性
                String name = field[j].getName(); // 获取属性的名字

                // Out.d("attribute name" , name);
                String type = field[j].getGenericType().toString(); // 获取属性的类型
                if (type.equals("class java.lang.String")) { // 如果type是类类型，则前面包含"class "，后面跟类名
                    Method m;
                    String nameSub = name.substring(0, 1).toUpperCase(
                            Locale.ENGLISH);
                    name = nameSub + name.substring(1);
                    m = model.getClass().getMethod("get" + name);
                    String value = (String) m.invoke(model); // 调用getter方法获取属性值
                    if (value != null) {
                        // Out.d("attribute value:" + value);
                        params.addBodyParameter(name, value);
                    }
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {

            e.printStackTrace();
            return null;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
        return params;
    }

//	/**
//	 * @param model
//	 *            模型
//	 * @throws NoSuchMethodException
//	 * @throws IllegalAccessException
//	 * @throws IllegalArgumentException
//	 * @throws InvocationTargetException
//	 */
//	public static VWRequest Reflect(Object model, VWRequest params) {
//
//		Field[] field = model.getClass().getDeclaredFields(); // 获取实体类的所有属性，返回Field数组
//		try {
//			for (int j = 0; j < field.length; j++) { // 遍历所有属性
//				String name = field[j].getName(); // 获取属性的名字
//
//				Out.d("attribute name", name);
//				String type = field[j].getGenericType().toString(); // 获取属性的类型
//				if (type.equals("class java.lang.String")) { // 如果type是类类型，则前面包含"class "，后面跟类名
//					Method m;
//					m = model.getClass().getMethod("get" + name);
//					String value = (String) m.invoke(model); // 调用getter方法获取属性值
//					if (value != null) {
//						Out.d("attribute value:" + value);
//						params.addParam(name, value);
//					}
//				}
//			}
//		} catch (NoSuchMethodException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		} catch (InvocationTargetException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}
//		return params;
//	}
//

    public static void saveBitmap(Bitmap bitmap, String picName) {
        File f = new File(picName);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            Log.e("Photo", "已经保存");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return  version;
        } catch (Exception e) {
            e.printStackTrace();
            return "获取版本号失败";
        }
    }
}
