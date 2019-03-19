package com.yzwy.lprmag.bean;

/**
 *
 */
public class PresetBean {

    private String GeomagnetismAddressNumber;//地磁地址
    private String RK3399AddressNumber;//RK3399地址
    private String PersetNumber;//预置点序号


    public PresetBean(String geomagnetismAddressNumber, String RK3399AddressNumber, String persetNumber) {
        GeomagnetismAddressNumber = geomagnetismAddressNumber;
        this.RK3399AddressNumber = RK3399AddressNumber;
        PersetNumber = persetNumber;
    }

    public String getGeomagnetismAddressNumber() {
        return GeomagnetismAddressNumber;
    }

    public void setGeomagnetismAddressNumber(String geomagnetismAddressNumber) {
        GeomagnetismAddressNumber = geomagnetismAddressNumber;
    }

    public String getRK3399AddressNumber() {
        return RK3399AddressNumber;
    }

    public void setRK3399AddressNumber(String RK3399AddressNumber) {
        this.RK3399AddressNumber = RK3399AddressNumber;
    }

    public String getPersetNumber() {
        return PersetNumber;
    }

    public void setPersetNumber(String persetNumber) {
        PersetNumber = persetNumber;
    }

    @Override
    public String toString() {
        return "PresetBean{" +
                "GeomagnetismAddressNumber='" + GeomagnetismAddressNumber + '\'' +
                ", RK3399AddressNumber='" + RK3399AddressNumber + '\'' +
                ", PersetNumber='" + PersetNumber + '\'' +
                '}';
    }
}
