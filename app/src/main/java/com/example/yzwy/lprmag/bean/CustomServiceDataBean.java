package com.example.yzwy.lprmag.bean;

/**
 *
 */
public class CustomServiceDataBean {

    private int keyID;//
    private String key;//
    private String val;//
    private String bgUrl;//
    private int bgUrlID;//

    public CustomServiceDataBean(int keyID, String key, String val, String bgUrl, int bgUrlID) {
        this.keyID = keyID;
        this.key = key;
        this.val = val;
        this.bgUrl = bgUrl;
        this.bgUrlID = bgUrlID;
    }

    public int getKeyID() {
        return keyID;
    }

    public void setKeyID(int keyID) {
        this.keyID = keyID;
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

    public String getBgUrl() {
        return bgUrl;
    }

    public void setBgUrl(String bgUrl) {
        this.bgUrl = bgUrl;
    }

    public int getBgUrlID() {
        return bgUrlID;
    }

    public void setBgUrlID(int bgUrlID) {
        this.bgUrlID = bgUrlID;
    }

    @Override
    public String toString() {
        return "CustomServiceDataBean{" +
                "keyID=" + keyID +
                ", key='" + key + '\'' +
                ", val='" + val + '\'' +
                ", bgUrl='" + bgUrl + '\'' +
                ", bgUrlID=" + bgUrlID +
                '}';
    }
}
