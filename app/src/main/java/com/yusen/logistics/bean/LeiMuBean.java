package com.yusen.logistics.bean;

public class LeiMuBean {
    /**
     * PT_ID : 1
     * PT_Name : 药妆
     * PT_Class : 1
     * PT_FID : 0
     */

    private String PT_ID;
    private String PT_Name;
    private String PT_Class;
    private String PT_FID;

    public String getPT_ID() {
        return PT_ID;
    }

    public void setPT_ID(String PT_ID) {
        this.PT_ID = PT_ID;
    }

    public String getPT_Name() {
        return PT_Name;
    }

    public void setPT_Name(String PT_Name) {
        this.PT_Name = PT_Name;
    }

    public String getPT_Class() {
        return PT_Class;
    }

    public void setPT_Class(String PT_Class) {
        this.PT_Class = PT_Class;
    }

    public String getPT_FID() {
        return PT_FID;
    }

    public void setPT_FID(String PT_FID) {
        this.PT_FID = PT_FID;
    }
    //{"isok":"true","info":"获取成功！","data":null,"datas":[{"PT_ID":"1","PT_Name":"药妆","PT_Class":"1","PT_FID":"0"},{"PT_ID":"2","PT_Name":"母婴","PT_Class":"1","PT_FID":"0"},{"PT_ID":"3","PT_Name":"食品","PT_Class":"1","PT_FID":"0"},{"PT_ID":"4","PT_Name":"其它","PT_Class":"1","PT_FID":"0"}],"type":"datas","pagecount":"4"}

}
