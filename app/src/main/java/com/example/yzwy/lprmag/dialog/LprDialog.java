package com.example.yzwy.lprmag.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.yzwy.lprmag.R;
import com.example.yzwy.lprmag.util.LogUtil;
import com.example.yzwy.lprmag.wifimess.lprthread.ConnectThread;
import com.example.yzwy.lprmag.wifimess.lprthread.ListenerThread;
import com.example.yzwy.lprmag.wifimess.model.SendOrder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;


public class LprDialog extends Activity {


    /**
     *
     * */
    public static final int DEVICE_CONNECTING = 1;//有设备正在连接热点
    public static final int DEVICE_CONNECTED = 2;//有设备连上热点
    public static final int SEND_MSG_SUCCSEE = 3;//发送消息成功
    public static final int SEND_MSG_ERROR = 4;//发送消息失败
    public static final int GET_MSG = 6;//获取新消息

    private TextView text_state;
    /**
     * 连接线程
     */
    private ConnectThread connectThread;


    /**
     * 监听线程
     */
    private ListenerThread listenerThread;

    /**
     * 端口号
     */
    private static final int PORT_wifi = 54321;
    private WifiManager wifiManager;
    private TextView status_init;
    private TextView tv_persetnum_dialoglpr;


    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_lpr);

        //设置窗口对齐屏幕宽度
        Window win = this.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;//设置对话框置顶显示
        win.setAttributes(lp);
        //设置点击外部空白处可以关闭Activity
        this.setFinishOnTouchOutside(true);

        /**
         * 初始化组件
         * */
        initView();


        /**
         * wifi模块的架子啊
         * */
        initViewWifi();


    }

    /**
     * =============================================================================================
     * wifi模块的加载
     */
    private void initViewWifi() {
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        //检查Wifi状态
        if (!wifiManager.isWifiEnabled())
            wifiManager.setWifiEnabled(true);


        status_init.setText("已连接到：" + wifiManager.getConnectionInfo().getSSID() +
                "\nIP:" + getIp()
                + "\n路由：" + getWifiRouteIPAddress(LprDialog.this));

        //initBroadcastReceiver();
        //开启连接线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(getWifiRouteIPAddress(LprDialog.this), PORT_wifi);
                    connectThread = new ConnectThread(socket, handler);
                    connectThread.start();
                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            text_state.setText("通信连接失败");
                        }
                    });

                }
            }
        }).start();

        listenerThread = new ListenerThread(PORT_wifi, handler);
        listenerThread.start();
    }

    /**
     * 获取已连接的热点路由
     *
     * @return
     */
    private String getIp() {
        //检查Wifi状态
        if (!wifiManager.isWifiEnabled())
            wifiManager.setWifiEnabled(true);
        WifiInfo wi = wifiManager.getConnectionInfo();
        //获取32位整型IP地址
        int ipAdd = wi.getIpAddress();
        //把整型地址转换成“*.*.*.*”地址
        String ip = intToIp(ipAdd);
        return ip;
    }

    /**
     * 获取路由
     *
     * @return
     */

    private String getRouterIp() {
        //检查Wifi状态
        if (!wifiManager.isWifiEnabled())
            wifiManager.setWifiEnabled(true);
        WifiInfo wi = wifiManager.getConnectionInfo();
        //获取32位整型IP地址
        int ipAdd = wi.getIpAddress();
        //把整型地址转换成“*.*.*.*”地址
        String ip = intToRouterIp(ipAdd);
        return ip;
    }

    private String intToIp(int i) {
        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }

    private String intToRouterIp(int i) {
        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                1;
    }

    /**
     * wifi获取 已连接网络路由  路由ip地址---方法同上
     *
     * @param context
     * @return
     */
    private static String getWifiRouteIPAddress(Context context) {
        WifiManager wifi_service = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcpInfo = wifi_service.getDhcpInfo();
        //        WifiInfo wifiinfo = wifi_service.getConnectionInfo();
        //        System.out.println("Wifi info----->" + wifiinfo.getIpAddress());
        //        System.out.println("DHCP info gateway----->" + Formatter.formatIpAddress(dhcpInfo.gateway));
        //        System.out.println("DHCP info netmask----->" + Formatter.formatIpAddress(dhcpInfo.netmask));
        //DhcpInfo中的ipAddress是一个int型的变量，通过Formatter将其转化为字符串IP地址
        String routeIp = Formatter.formatIpAddress(dhcpInfo.gateway);
        Log.i("route ip", "wifi route ip：" + routeIp);

        return routeIp;
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DEVICE_CONNECTING:
                    connectThread = new ConnectThread(listenerThread.getSocket(), handler);
                    connectThread.start();
                    break;
                case DEVICE_CONNECTED:
                    text_state.setText("设备连接成功");
                    //向终端发送获取所有预置点的命令
                    connectThread.sendData(SendOrder.getOrderPlateNum());
                    break;
                case SEND_MSG_SUCCSEE:
                    text_state.setText("发送消息成功:" + msg.getData().getString("MSG"));
                    break;
                case SEND_MSG_ERROR:
                    text_state.setText("发送消息失败:" + msg.getData().getString("MSG"));
                    break;
                case GET_MSG:
                    String BackMsgData = msg.getData().getString("MSG");
                    System.out.println("返回的数据>>>>>>>>>>>>>" + BackMsgData);

                    try {
                        JSONObject jsonObject = new JSONObject(BackMsgData);
                        String Order = jsonObject.getString("Order");

                        switch (Order) {

                            //返回查询所有预置点的消息
                            case "OrderPlate":
                                String carNum = jsonObject.getString("carNum");
                                tv_persetnum_dialoglpr.setText("拍到的车牌号码为：\n" + carNum);
                                break;

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
    };

    /**
     * 获取连接到热点上的手机ip
     *
     * @return
     */
    private ArrayList<String> getConnectedIP() {
        ArrayList<String> connectedIP = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(
                    "/proc/net/arp"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] splitted = line.split(" +");
                if (splitted != null && splitted.length >= 4) {
                    String ip = splitted[0];
                    connectedIP.add(ip);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //        Log.i("connectIp:", connectedIP);
        return connectedIP;
    }


    public void initView() {

        text_state = (TextView) findViewById(R.id.status_info);
        status_init = (TextView) findViewById(R.id.status_init);
        tv_persetnum_dialoglpr = (TextView) findViewById(R.id.tv_persetnum_dialoglpr);
    }

    /**
     * =============================================================================================
     * 活动销毁活动生命周期
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        LogUtil.showLog("DiaLogPersetActivity", "准备关闭 onDestroy");


        /**
         * 关闭多线程
         * */
        if (connectThread != null) {
            connectThread.interrupt();
            connectThread.close();
            LogUtil.showLog("DiaLogPersetActivity", "connectThread 关闭 onDestroy");
        }

        if (listenerThread != null) {
            listenerThread.interrupt();
            listenerThread.close();
            LogUtil.showLog("DiaLogPersetActivity", "listenerThread 关闭  onDestroy");
        }
        //结束当前活动
        this.finish();
    }

    /**
     * =============================================================================================
     * 单击退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
        }
        return false;
    }

    private void exit() {
        LogUtil.showLog("DiaLogPersetActivity", "物理按键准备关闭当前活动");
        if (connectThread != null) {
            connectThread.interrupt();
            connectThread.close();
            LogUtil.showLog("DiaLogPersetActivity", "connectThread 关闭");
        }

        if (listenerThread != null) {
            listenerThread.interrupt();
            listenerThread.close();
            LogUtil.showLog("DiaLogPersetActivity", "listenerThread 关闭");
        }
        //结束当前活动
        finish();
    }


}
