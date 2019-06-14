package com.example.yzwy.lprmag.bean;

/**
 *
 */
public class ErrorLogDataBean {

    private String keyID;
    private Boolean selectboll;
    private String addresscarid;
    private String carnum;
    private String geomagnetismAddressNumber;
    private String inserttime;
    private String updatetime;
    private String type;
    private String eventTime;
    private String fileName;

    public ErrorLogDataBean(String keyID, Boolean selectboll, String addresscarid, String carnum, String geomagnetismAddressNumber, String inserttime, String updatetime, String type, String eventTime, String fileName) {
        this.keyID = keyID;
        this.selectboll = selectboll;
        this.addresscarid = addresscarid;
        this.carnum = carnum;
        this.geomagnetismAddressNumber = geomagnetismAddressNumber;
        this.inserttime = inserttime;
        this.updatetime = updatetime;
        this.type = type;
        this.eventTime = eventTime;
        this.fileName = fileName;
    }

    public String getKeyID() {
        return keyID;
    }

    public void setKeyID(String keyID) {
        this.keyID = keyID;
    }

    public Boolean getSelectboll() {
        return selectboll;
    }

    public void setSelectboll(Boolean selectboll) {
        this.selectboll = selectboll;
    }

    public String getAddresscarid() {
        return addresscarid;
    }

    public void setAddresscarid(String addresscarid) {
        this.addresscarid = addresscarid;
    }

    public String getCarnum() {
        return carnum;
    }

    public void setCarnum(String carnum) {
        this.carnum = carnum;
    }

    public String getGeomagnetismAddressNumber() {
        return geomagnetismAddressNumber;
    }

    public void setGeomagnetismAddressNumber(String geomagnetismAddressNumber) {
        this.geomagnetismAddressNumber = geomagnetismAddressNumber;
    }

    public String getInserttime() {
        return inserttime;
    }

    public void setInserttime(String inserttime) {
        this.inserttime = inserttime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "ErrorLogDataBean{" +
                "keyID='" + keyID + '\'' +
                ", selectboll='" + selectboll + '\'' +
                ", addresscarid='" + addresscarid + '\'' +
                ", carnum='" + carnum + '\'' +
                ", geomagnetismAddressNumber='" + geomagnetismAddressNumber + '\'' +
                ", inserttime='" + inserttime + '\'' +
                ", updatetime='" + updatetime + '\'' +
                ", type='" + type + '\'' +
                ", eventTime='" + eventTime + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
