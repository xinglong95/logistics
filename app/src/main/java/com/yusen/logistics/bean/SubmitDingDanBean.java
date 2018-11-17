package com.yusen.logistics.bean;

import java.io.Serializable;
import java.util.List;

public class SubmitDingDanBean implements Serializable {

    /**
     * W_ID : 0
     * W_DeliveryDate : 2018-09-13
     * W_F_Province : 北海道
     * W_F_City : 札幌市
     * W_F_District : 白石区
     * W_F_Address : 测试地址
     * W_F_Name : 陈龙军
     * W_F_Tel : 18562873018
     * W_S_Province : 山东省
     * W_S_City : 青岛市
     * W_S_District : 市北区
     * W_S_Address : 台东邮电局
     * W_S_Name : 吴鹏
     * W_S_Tel : 18562873018
     * W_D_Name :
     * W_D_Tel :
     * IdCard_Name : 吴鹏
     * IdCard_Number : 370203198802203767
     * IdCard_Sex : 男
     * IdCard_National : 汉族
     * IdCard_BrithDate : 1988-02-20
     * IdCard_Address : 测试身份证地址
     * IdCard_Organ : 青岛市北公安局
     * IdCard_BeginDate : 2011
     * IdCard_EndDate : 2028
     * IdCard_Head : /Image/IdCard/1.png
     * IdCard_Tail : /Image/IdCard/2.png
     *
     */

    private String W_ID="0";
    private String W_OrderNo;
    private String W_State;
    private String W_DeliveryDate;
    private String W_F_Province;
    private String W_F_City;
    private String W_F_District;
    private String W_F_Address;
    private String W_F_Name;
    private String W_F_Tel;
    private String W_S_Province;
    private String W_S_City;
    private String W_S_District;
    private String W_S_Address;
    private String W_S_Name;
    private String W_S_Tel;
    private String W_D_Name;
    private String W_D_Tel;
    private String IdCard_Name;
    private String IdCard_Number;
    private String IdCard_Sex;
    private String IdCard_National;
    private String IdCard_BrithDate;
    private String IdCard_Address;
    private String IdCard_Organ;
    private String IdCard_BeginDate;
    private String IdCard_EndDate;
    private String IdCard_Head;
    private String IdCard_Tail;
    private String W_Actual_Weight;
    private String D_ID;
    private String W_Ems_Number;
    private String W_Ems_Company;
    private String W_Courier_Number;
    private String W_Fee;
    private String W_Firstcount;


    public String getW_Courier_Number() {
        return W_Courier_Number;
    }

    public void setW_Courier_Number(String w_Courier_Number) {
        W_Courier_Number = w_Courier_Number;
    }

    public String getW_Firstcount() {
        return W_Firstcount;
    }

    public void setW_Firstcount(String w_Firstcount) {
        W_Firstcount = w_Firstcount;
    }

    public String getD_ID() {
        return D_ID;
    }

    public void setD_ID(String d_ID) {
        D_ID = d_ID;
    }

    public String getW_Ems_Number() {
        return W_Ems_Number;
    }

    public void setW_Ems_Number(String w_Ems_Number) {
        W_Ems_Number = w_Ems_Number;
    }

    public String getW_Ems_Company() {
        return W_Ems_Company;
    }

    public void setW_Ems_Company(String w_Ems_Company) {
        W_Ems_Company = w_Ems_Company;
    }

    public String getW_Fee() {
        return W_Fee;
    }

    public void setW_Fee(String WS_Fee) {
        this.W_Fee = WS_Fee;
    }

    public String getW_Actual_Weight() {
        return W_Actual_Weight;
    }

    public void setW_Actual_Weight(String w_Actual_Weight) {
        W_Actual_Weight = w_Actual_Weight;
    }

    private List<ShangPinInfoBean> Sub;

    public String getW_OrderNo() {
        return W_OrderNo;
    }

    public void setW_OrderNo(String w_OrderNo) {
        W_OrderNo = w_OrderNo;
    }

    public String getW_State() {
        return W_State;
    }

    public void setW_State(String w_State) {
        W_State = w_State;
    }

    public List<ShangPinInfoBean> getSub() {
        return Sub;
    }

    public void setSub(List<ShangPinInfoBean> sub) {
        Sub = sub;
    }

    public String getW_ID() {
        return W_ID;
    }

    public void setW_ID(String W_ID) {
        this.W_ID = W_ID;
    }

    public String getW_DeliveryDate() {
        return W_DeliveryDate;
    }

    public void setW_DeliveryDate(String W_DeliveryDate) {
        this.W_DeliveryDate = W_DeliveryDate;
    }

    public String getW_F_Province() {
        return W_F_Province;
    }

    public void setW_F_Province(String W_F_Province) {
        this.W_F_Province = W_F_Province;
    }

    public String getW_F_City() {
        return W_F_City;
    }

    public void setW_F_City(String W_F_City) {
        this.W_F_City = W_F_City;
    }

    public String getW_F_District() {
        return W_F_District;
    }

    public void setW_F_District(String W_F_District) {
        this.W_F_District = W_F_District;
    }

    public String getW_F_Address() {
        return W_F_Address;
    }

    public void setW_F_Address(String W_F_Address) {
        this.W_F_Address = W_F_Address;
    }

    public String getW_F_Name() {
        return W_F_Name;
    }

    public void setW_F_Name(String W_F_Name) {
        this.W_F_Name = W_F_Name;
    }

    public String getW_F_Tel() {
        return W_F_Tel;
    }

    public void setW_F_Tel(String W_F_Tel) {
        this.W_F_Tel = W_F_Tel;
    }

    public String getW_S_Province() {
        return W_S_Province;
    }

    public void setW_S_Province(String W_S_Province) {
        this.W_S_Province = W_S_Province;
    }

    public String getW_S_City() {
        return W_S_City;
    }

    public void setW_S_City(String W_S_City) {
        this.W_S_City = W_S_City;
    }

    public String getW_S_District() {
        return W_S_District;
    }

    public void setW_S_District(String W_S_District) {
        this.W_S_District = W_S_District;
    }

    public String getW_S_Address() {
        return W_S_Address;
    }

    public void setW_S_Address(String W_S_Address) {
        this.W_S_Address = W_S_Address;
    }

    public String getW_S_Name() {
        return W_S_Name;
    }

    public void setW_S_Name(String W_S_Name) {
        this.W_S_Name = W_S_Name;
    }

    public String getW_S_Tel() {
        return W_S_Tel;
    }

    public void setW_S_Tel(String W_S_Tel) {
        this.W_S_Tel = W_S_Tel;
    }

    public String getW_D_Name() {
        return W_D_Name;
    }

    public void setW_D_Name(String W_D_Name) {
        this.W_D_Name = W_D_Name;
    }

    public String getW_D_Tel() {
        return W_D_Tel;
    }

    public void setW_D_Tel(String W_D_Tel) {
        this.W_D_Tel = W_D_Tel;
    }

    public String getIdCard_Name() {
        return IdCard_Name;
    }

    public void setIdCard_Name(String IdCard_Name) {
        this.IdCard_Name = IdCard_Name;
    }

    public String getIdCard_Number() {
        return IdCard_Number;
    }

    public void setIdCard_Number(String IdCard_Number) {
        this.IdCard_Number = IdCard_Number;
    }

    public String getIdCard_Sex() {
        return IdCard_Sex;
    }

    public void setIdCard_Sex(String IdCard_Sex) {
        this.IdCard_Sex = IdCard_Sex;
    }

    public String getIdCard_National() {
        return IdCard_National;
    }

    public void setIdCard_National(String IdCard_National) {
        this.IdCard_National = IdCard_National;
    }

    public String getIdCard_BrithDate() {
        return IdCard_BrithDate;
    }

    public void setIdCard_BrithDate(String IdCard_BrithDate) {
        this.IdCard_BrithDate = IdCard_BrithDate;
    }

    public String getIdCard_Address() {
        return IdCard_Address;
    }

    public void setIdCard_Address(String IdCard_Address) {
        this.IdCard_Address = IdCard_Address;
    }

    public String getIdCard_Organ() {
        return IdCard_Organ;
    }

    public void setIdCard_Organ(String IdCard_Organ) {
        this.IdCard_Organ = IdCard_Organ;
    }

    public String getIdCard_BeginDate() {
        return IdCard_BeginDate;
    }

    public void setIdCard_BeginDate(String IdCard_BeginDate) {
        this.IdCard_BeginDate = IdCard_BeginDate;
    }

    public String getIdCard_EndDate() {
        return IdCard_EndDate;
    }

    public void setIdCard_EndDate(String IdCard_EndDate) {
        this.IdCard_EndDate = IdCard_EndDate;
    }

    public String getIdCard_Head() {
        return IdCard_Head;
    }

    public void setIdCard_Head(String IdCard_Head) {
        this.IdCard_Head = IdCard_Head;
    }

    public String getIdCard_Tail() {
        return IdCard_Tail;
    }

    public void setIdCard_Tail(String IdCard_Tail) {
        this.IdCard_Tail = IdCard_Tail;
    }

}
