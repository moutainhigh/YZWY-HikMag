package com.example.yzwy.lprmag.myinterface;


public interface NetBroadcastListener {

    /**
     * =========================================================================================
     *
     * @param netStatus   网络状态
     * @param netMobile   网络类型
     * @param isAvailable 网络是否可用
     */
    void netBroadcastReceiver(int netStatus, int netMobile, boolean isAvailable);
}
