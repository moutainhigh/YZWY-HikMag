package com.example.yzwy.lprmag.wifimess.model;

import com.example.yzwy.lprmag.application.MyApp;
import com.example.yzwy.lprmag.myConstant.OrderConstant;
import com.example.yzwy.lprmag.myConstant.UserInfoConstant;
import com.example.yzwy.lprmag.util.SharePreferencesUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SendOrder {

    /**
     * =============================================================================================
     * 获取终端上所有预置点
     */
    public static String Get_PersetData() {
        JSONObject jo1 = new JSONObject();
        try {
            jo1.put("Order", String.valueOf(OrderConstant.SELECT_PERSET_ALL));
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
    public static String Set_PersetData(String GeomagnetismAddressNumber, String PersetNumber, String scwidth_hik, String scheight_hik) {
        JSONObject jo1 = new JSONObject();
        try {
            jo1.put("Order", String.valueOf(OrderConstant.ORDER_SET));
            jo1.put("GeomagnetismAddressNumber", GeomagnetismAddressNumber);
            jo1.put("PersetNumber", PersetNumber);
            jo1.put("ScaleWidth", scwidth_hik);
            jo1.put("ScaleHeight", scheight_hik);
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
    public static String Delete_PersetData(String PersetNumber) {
        JSONObject jo1 = new JSONObject();
        try {
            jo1.put("Order", String.valueOf(OrderConstant.ORDER_DELETE));
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
     * 修改单个预置点
     */
    public static String Update_PersetData(String PersetNumber, String scwidth_hik, String scheight_hik) {
        JSONObject jo1 = new JSONObject();
        try {
            jo1.put("Order", String.valueOf(OrderConstant.ORDER_Update_Preset));
            jo1.put("PersetNumber", PersetNumber);
            jo1.put("ScaleWidth", scwidth_hik);
            jo1.put("ScaleHeight", scheight_hik);
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
     *
     * @param scwidth_hik
     * @param scheight_hik
     */
    public static String Get_OrderPlateNum(String scwidth_hik, String scheight_hik) {
        JSONObject jo1 = new JSONObject();
        try {
            jo1.put("Order", String.valueOf(OrderConstant.ORDER_OrderPlate));
            jo1.put("scwidth_hik", scwidth_hik);
            jo1.put("scheight_hik", scheight_hik);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String json = jo1.toString();
        System.out.println("发送命令 获取车牌>>>>>" + json);
        return json;
    }


//    /**
//     * =============================================================================================
//     * 设置摄像头或者终端的优先级
//     */
//    public static String Set_Priority(String Priority) {
//        JSONObject jo1 = new JSONObject();
//        try {
//            jo1.put("Order", String.valueOf(OrderConstant.ORDER_SetPriority));
//            jo1.put("Priority", Priority);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        String json = jo1.toString();
//        System.out.println("发送命令 设置优先级>>>>>" + json);
//        return json;
//    }


    /**
     * =============================================================================================
     * 获取终端热点信息
     */
    public static String Get_WifiHotInfo() {
        JSONObject jo1 = new JSONObject();
        try {
            jo1.put("Order", String.valueOf(OrderConstant.ORDER_Get_WifiHotInfo));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String json = jo1.toString();
        System.out.println("发送命令 获取终端热点信息>>>>>" + json);
        return json;
    }


    /**
     * =============================================================================================
     * 设置终端热点信息
     */
    public static String Set_WifiHotInfo(String wifiName, String wifiPwd) {
        JSONObject jo1 = new JSONObject();
        try {
            jo1.put("Order", String.valueOf(OrderConstant.ORDER_Set_WifiHotInfo));
            jo1.put("wifiName", wifiName);
            jo1.put("wifiPwd", wifiPwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String json = jo1.toString();
        System.out.println("发送命令 获取终端热点信息>>>>>" + json);
        return json;
    }


    /**
     * =============================================================================================
     * 关闭终端热点
     */
    public static String Close_WifiHotInfo() {
        JSONObject jo1 = new JSONObject();
        try {
            jo1.put("Order", String.valueOf(OrderConstant.ORDER_Close_WifiHotInfo));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String json = jo1.toString();
        System.out.println("发送命令 获取终端热点信息>>>>>" + json);
        return json;
    }


    /**
     * =============================================================================================
     * 同步终端海康地址
     */
    public static String ORDER_PushHiKConfig(String HiKIP, String HiKPort, String HiKUserName, String HiKPwd) {
        JSONObject jo1 = new JSONObject();
        try {
            jo1.put("Order", String.valueOf(OrderConstant.ORDER_PushHiKConfig));
            jo1.put("HiKPwd", HiKPwd);
            jo1.put("HiKIP", HiKIP);
            jo1.put("HiKPort", HiKPort);
            jo1.put("HiKUserName", HiKUserName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String json = jo1.toString();
        System.out.println("发送命令 同步终端海康地址>>>>>" + json);
        return json;
    }


    /**
     * =============================================================================================
     * 获取终端上优先级
     */
    public static String Get_PriorityData() {
        JSONObject jo1 = new JSONObject();
        try {
            jo1.put("Order", String.valueOf(OrderConstant.ORDER_Get_PriorityData));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String json = jo1.toString();
        System.out.println("发送命令 获取终端上优先级>>>>>" + json);
        return json;
    }

    /**
     * =============================================================================================
     * 设置终端上优先级
     */
    public static String Set_PriorityData(String Priority) {
        JSONObject jo1 = new JSONObject();
        try {
            jo1.put("Order", String.valueOf(OrderConstant.ORDER_Set_PriorityData));
            jo1.put("Priority", Priority);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String json = jo1.toString();
        System.out.println("发送命令 设置终端上优先级>>>>>" + json);
        return json;
    }


    /**
     * =============================================================================================
     * 获取终端数据信息
     */
    public static String Get_TermDataInfo() {
        JSONObject jo1 = new JSONObject();
        try {
            jo1.put("Order", String.valueOf(OrderConstant.ORDER_GET_TermDataMag));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String json = jo1.toString();
        System.out.println("发送命令 获取终端数据信息>>>>>" + json);
        return json;
    }

    /**
     * =============================================================================================
     * 获取终端数据信息
     *
     * @param termpriority 设备终端优先级
     * @param termaddress  设备终端地址
     * @return
     */
    public static String Set_TermDataInfo(String termpriority, String termaddress) {
        JSONObject jo1 = new JSONObject();
        try {
            jo1.put("Order", String.valueOf(OrderConstant.ORDER_SET_Update_TermDataMag));
            jo1.put("termpriority", termpriority);
            jo1.put("termaddress", termaddress);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String json = jo1.toString();
        System.out.println("发送命令 获取终端数据信息>>>>>" + json);
        return json;
    }


    /**
     * =============================================================================================
     * 获取终端地磁列表
     */
    public static String Get_GeomRest() {
        JSONObject jo1 = new JSONObject();
        try {
            jo1.put("Order", String.valueOf(OrderConstant.ORDER_GET_GeomList));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String json = jo1.toString();
        System.out.println("发送命令 获取终端地磁列表>>>>>" + json);
        return json;
    }

    /**
     * =============================================================================================
     * 设置终端地磁重置信息
     *
     * @return
     */
    public static String Set_GeomRest(JSONArray jsonGeom) {
        JSONObject jo1 = new JSONObject();
        try {
            jo1.put("Order", String.valueOf(OrderConstant.ORDER_SET_GeomListRest));
            jo1.put("userID", SharePreferencesUtil.getStringValue(MyApp.getContext(), UserInfoConstant.userID, "0"));
            jo1.put("data", jsonGeom);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String json = jo1.toString();
        System.out.println("发送命令 设置终端地磁重置>>>>>" + json);
        return json;
    }
}
