package com.example.yzwy.lprmag.bean;

/**
 *
 */
public class CustomServiceDataBean {

    private String ID;//
    private String key;//
    private String val;//
    private String bgUrl;//

    public CustomServiceDataBean(String ID, String key, String val, String bgUrl) {
        this.ID = ID;
        this.key = key;
        this.val = val;
        this.bgUrl = bgUrl;
    }

    public String getBgUrl() {
        return bgUrl;
    }

    public void setBgUrl(String bgUrl) {
        this.bgUrl = bgUrl;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return "CustomServiceDataBean{" +
                "ID='" + ID + '\'' +
                ", key='" + key + '\'' +
                ", val='" + val + '\'' +
                ", bgUrl='" + bgUrl + '\'' +
                '}';
    }
}
