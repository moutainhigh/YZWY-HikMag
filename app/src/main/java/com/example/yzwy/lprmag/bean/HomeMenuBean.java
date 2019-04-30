package com.example.yzwy.lprmag.bean;

/**
 *
 */
public class HomeMenuBean {

    private int keyID;//
    private int imgID;//
    private String MenuText;//

    public HomeMenuBean(int keyID, int imgID, String menuText) {
        this.keyID = keyID;
        this.imgID = imgID;
        MenuText = menuText;
    }


    public int getKeyID() {
        return keyID;
    }

    public void setKeyID(int keyID) {
        this.keyID = keyID;
    }

    public int getImgID() {
        return imgID;
    }

    public void setImgID(int imgID) {
        this.imgID = imgID;
    }

    public String getMenuText() {
        return MenuText;
    }

    public void setMenuText(String menuText) {
        MenuText = menuText;
    }

    @Override
    public String toString() {
        return "HomeMenuBean{" +
                "keyID=" + keyID +
                ", imgID=" + imgID +
                ", MenuText='" + MenuText + '\'' +
                '}';
    }
}
