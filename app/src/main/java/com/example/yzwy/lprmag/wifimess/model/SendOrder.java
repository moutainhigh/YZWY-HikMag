package com.example.yzwy.lprmag.wifimess.model;

import com.example.yzwy.lprmag.myConstant.PersetOrderConstant;

import org.json.JSONException;
import org.json.JSONObject;

public class SendOrder {

    /**
     * =============================================================================================
     * 获取终端上所有预置点
     */
    public static String getPersetData() {
        JSONObject jo1 = new JSONObject();
        try {
            jo1.put("Order", PersetOrderConstant.SELECT_PERSET_ALL);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String json = jo1.toString();
        System.out.println("发送命令 获取所有的预置点>>>>>" + json);
        return json;
    }


    /**
     * =============================================================================================
     * 设置单个预置点
     */
    public static String setPersetData(String GeomagnetismAddressNumber, String PersetNumber) {
        JSONObject jo1 = new JSONObject();
        try {
            jo1.put("Order", PersetOrderConstant.ORDER_SET);
            jo1.put("GeomagnetismAddressNumber", GeomagnetismAddressNumber);
            jo1.put("PersetNumber", PersetNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String json = jo1.toString();
        System.out.println("发送命令 设置预置点预置点>>>>>" + json);
        return json;
    }


    /**
     * =============================================================================================
     * 删除单个预置点
     */
    public static String deletePersetData(String PersetNumber) {
        JSONObject jo1 = new JSONObject();
        try {
            jo1.put("Order", PersetOrderConstant.ORDER_DELETE);
            jo1.put("PersetNumber", PersetNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String json = jo1.toString();
        System.out.println("发送命令 设置预置点预置点>>>>>" + json);
        return json;
    }


    /**
     * =============================================================================================
     * 获取终端上所有预置点
     */
    public static String getOrderPlateNum() {
        JSONObject jo1 = new JSONObject();
        try {
            jo1.put("Order", PersetOrderConstant.ORDER_OrderPlate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String json = jo1.toString();
        System.out.println("发送命令 获取车牌>>>>>" + json);
        return json;
    }


}
