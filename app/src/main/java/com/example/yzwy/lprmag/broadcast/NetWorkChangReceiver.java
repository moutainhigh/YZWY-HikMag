package com.example.yzwy.lprmag.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.example.yzwy.lprmag.myinterface.NetBroadcastListener;

/**
 * 监听网络状态变化
 * Created by Travis on 2017/10/11.
 */
public class NetWorkChangReceiver extends BroadcastReceiver {

    /**
     * 没有连接网络
     */
    private static final int NETWORK_NONE_NO = 500;
    /**
     * 连接网络
     */
    private static final int NETWORK_NONE_YES = 200;

    /**
     * 移动网络
     */
    private static final int NETWORK_MOBILE = 0;
    /**
     * 无线网络
     */
    private static final int NETWORK_WIFI = 1;

    /**
     * 以太网
     */
    private static final int NETWORK_ETHERNET = 9;


    /**
     * 无线网络移动网络
     */
    private static final int NETWORK_WIFI_MOBILE = 20;


    //private List<PresetBean> presetBeansList = new ArrayList<PresetBean>();


    /**
     * 定义一个网络数据的监听事件
     */
    private NetBroadcastListener netBroadcastListener;

    /**
     * =============================================================================================
     * 设置网络监听接口
     *
     * @param netBroadcastListener
     */
    public NetWorkChangReceiver(NetBroadcastListener netBroadcastListener) {
        this.netBroadcastListener = netBroadcastListener;
    }

    /**
     * 获取连接类型
     *
     * @param type
     * @return
     */
    private String getConnectionType(int type) {
        String connType = "";
        if (type == ConnectivityManager.TYPE_MOBILE) {
            connType = "3G网络数据";
        } else if (type == ConnectivityManager.TYPE_WIFI) {
            connType = "WIFI网络";
        } else if (type == ConnectivityManager.TYPE_BLUETOOTH) {
            connType = "WIFI网络";
        }
        return connType;
    }


    @Override
    public void onReceive(Context context, Intent intent) {

        // 如果相等的话就说明网络状态发生了变化
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {


            //System.out.println("网络状态发生变化");
            //检测API是不是小于23，因为到了API23之后getNetworkInfo(int networkType)方法被弃用
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {

                //获得ConnectivityManager对象
                ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                //获取ConnectivityManager对象对应的NetworkInfo对象
                //获取WIFI连接的信息
                NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                //获取以太网连接的信息
                //NetworkInfo ETHERNETNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
                //获取移动数据连接的信息
                NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                    SendNetBroadcastListener(NETWORK_NONE_YES, NETWORK_WIFI_MOBILE, (wifiNetworkInfo.isAvailable() == true && dataNetworkInfo.isAvailable() == true) ? true : false);
                    Toast.makeText(context, "WIFI已连接,移动数据已连接", Toast.LENGTH_SHORT).show();
                } else if (wifiNetworkInfo.isConnected() && !dataNetworkInfo.isConnected()) {
                    SendNetBroadcastListener(NETWORK_NONE_YES, NETWORK_WIFI, wifiNetworkInfo.isAvailable());
                    Toast.makeText(context, "WIFI已连接,移动数据已断开", Toast.LENGTH_SHORT).show();
                } else if (!wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                    SendNetBroadcastListener(NETWORK_NONE_YES, NETWORK_MOBILE, dataNetworkInfo.isAvailable());
                    Toast.makeText(context, "WIFI已断开,移动数据已连接", Toast.LENGTH_SHORT).show();
                } else {
                    SendNetBroadcastListener(NETWORK_NONE_NO, -1, false);
                    Toast.makeText(context, "WIFI已断开,移动数据已断开", Toast.LENGTH_SHORT).show();
                }
                //API大于23时使用下面的方式进行网络监听
            } else {

                System.out.println("API level 大于23");
                //获得ConnectivityManager对象
                ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                //获取所有网络连接的信息
                Network[] networks = connMgr.getAllNetworks();
                //用于存放网络连接信息
                //StringBuilder sb = new StringBuilder();
                //通过循环将网络信息逐个取出来
                for (int i = 0; i < networks.length; i++) {
                    //获取ConnectivityManager对象对应的NetworkInfo对象
                    NetworkInfo networkInfo = connMgr.getNetworkInfo(networks[i]);
                    //sb.append(networkInfo.getTypeName() + " connect is " + networkInfo.isConnected() + " connect is " + networkInfo.getType());
                    if (networkInfo.isConnected()) {
                        SendNetBroadcastListener(NETWORK_NONE_YES, networkInfo.getType(), networkInfo.isAvailable());
                    } else {
                        SendNetBroadcastListener(NETWORK_NONE_NO, -1, false);
                    }

                }
                if (networks.length <= 0) {
                    SendNetBroadcastListener(NETWORK_NONE_NO, -1, false);
                }
                //Toast.makeText(context, sb.toString(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    /**
     * =============================================================================================
     *
     * @param netStatus
     * @param netMobile
     */
    private void SendNetBroadcastListener(int netStatus, int netMobile, boolean isAvailable) {
        if (netBroadcastListener != null) {
            netBroadcastListener.netBroadcastReceiver(netStatus, netMobile, isAvailable);
        }
    }


}
