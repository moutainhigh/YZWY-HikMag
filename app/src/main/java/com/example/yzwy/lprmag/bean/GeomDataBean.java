package com.example.yzwy.lprmag.bean;

/**
 *
 */
public class GeomDataBean {

    private final String persetNumber;
    private int position;//
    private int keyID;//
    private boolean selectboll;//
    private String geomID;//

    public GeomDataBean(int position, int keyID, boolean selectboll, String geomID, String persetNumber) {
        this.position = position;
        this.keyID = keyID;
        this.selectboll = selectboll;
        this.geomID = geomID;
        this.persetNumber = persetNumber;
    }

    public String getPersetNumber() {
        return persetNumber;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getKeyID() {
        return keyID;
    }

    public void setKeyID(int keyID) {
        this.keyID = keyID;
    }

    public boolean isSelectboll() {
        return selectboll;
    }

    public void setSelectboll(boolean selectboll) {
        this.selectboll = selectboll;
    }

    public String getGeomID() {
        return geomID;
    }

    public void setGeomID(String geomID) {
        this.geomID = geomID;
    }

    @Override
    public String toString() {
        return "GeomDataBean{" +
                "persetNumber='" + persetNumber + '\'' +
                ", position=" + position +
                ", keyID=" + keyID +
                ", selectboll=" + selectboll +
                ", geomID='" + geomID + '\'' +
                '}';
    }
}
