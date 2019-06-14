package com.example.yzwy.lprmag.myConstant;

/**
 * wifi通信常量
 */
public class OrderConstant {

//    public static String SELECT_PERSET_ALL = "Order0";
//    public static String ORDER_SET = "Order1";
//    public static String ORDER_DELETE = "Order2";
//    public static String ORDER_UPDATE = "Order3";
//    public static String ORDER_OrderPlate = "OrderPlate";
//    public static String ORDER_SetPriority = "ORDER_SetPriority";


    /**
     * 查询所有预置点
     */
    public final static int SELECT_PERSET_ALL = 0;
    /**
     * 设置新增预置点
     */
    public final static int ORDER_SET = 1;
    /**
     * 删除预置点
     */
    public final static int ORDER_DELETE = 2;
    /**
     * 更新预置点
     */
    public final static int ORDER_UPDATE = 3;
    /**
     * 识别车牌算法
     */
    public final static int ORDER_OrderPlate = 4;
//    /**
//     * 设置优先级
//     */
//    public final static int ORDER_SetPriority = 5;


    /**
     * 关闭终端wifi热点
     */
    public final static int ORDER_Close_WifiHotInfo = 6;
    /**
     * 获取热点信息
     */
    public final static int ORDER_Get_WifiHotInfo = 7;
    /**
     * 设置热点信息
     */
    public final static int ORDER_Set_WifiHotInfo = 8;


    /**
     * 同步海康配置信息的命令标识
     */
    public final static int ORDER_PushHiKConfig = 20;


    /**
     * 获取终端上优先级
     */
    public final static int ORDER_Get_PriorityData = 21;


    /**
     * 设置终端上优先级
     */
    public final static int ORDER_Set_PriorityData = 22;
    /**
     * 修改预置点
     */
    public final static int ORDER_Update_Preset = 23;


    /**
     * 获取终端上的数据
     */
    public final static int ORDER_GET_TermDataMag = 24;
    /**
     * 设置终端上的数据
     */
    public final static int ORDER_SET_Update_TermDataMag = 25;


    /**
     * 获取终端上的数据
     */
    public final static int ORDER_GET_GeomList = 26;
    /**
     * 设置终端上的数据
     */
    public final static int ORDER_SET_GeomListRest = 27;

    /**
     * 获取终端上异常日志的数据
     */
    public final static int ORDER_GET_ErrorLog = 28;
//    /**
//     * 设置终端上异常日志的数据
//     */
//    public final static int ORDER_SET_ErrorLog = 29;

}
