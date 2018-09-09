package com.yusen.logistics.bean;

public class SubmitShangPinBean {
    private String barcode;
    private String brand="";
    private String brandid="0";
    private String p_type="";
    private String p_typeid="0";
    private String name;
    private String spec;
    private String funcation;
    private String weight;
    private String price;
    private String volume;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBrandid() {
        return brandid;
    }

    public void setBrandid(String brandid) {
        this.brandid = brandid;
    }

    public String getP_typ() {
        return p_type;
    }

    public void setP_typ(String p_typ) {
        this.p_type = p_typ;
    }

    public String getP_typeid() {
        return p_typeid;
    }

    public void setP_typeid(String p_typeid) {
        this.p_typeid = p_typeid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getFuncation() {
        return funcation;
    }

    public void setFuncation(String funcation) {
        this.funcation = funcation;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }
}
