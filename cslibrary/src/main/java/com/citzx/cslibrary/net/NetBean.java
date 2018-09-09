/*
 * @Title:  NetBean.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:
 * @author: JohnWell
 * @data:  2014-8-29 上午10:27:52
 * @version:  V1.0
 */
package com.citzx.cslibrary.net;

import java.util.ArrayList;

/**
 * TODO
 * @author  JohnWell
 * @data:  2014-8-29 上午10:27:52
 * @version:  V1.0
 */
public class NetBean<T,K> {
private boolean isok=false;
private String info="";
private T data=null;
private ArrayList<K> datas=null;
private int pagecount=1;
private String type="";

public String getInfo() {
	return info;
}
public void setInfo(String info) {
	this.info = info;
}
public T getData() {
	return data;
}
public void setData(T data) {
	this.data = data;
}
public ArrayList<K> getDatas() {
	return datas;
}
public void setDatas(ArrayList<K> datas) {
	this.datas = datas;
}
public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}
public boolean isOk() {
	return isok;
}
public void setOk(boolean isOk) {
	this.isok = isOk;
}
public int getPagecount() {
	return pagecount;
}
public void setPagecount(int pagecount) {
	this.pagecount = pagecount;
}

}
