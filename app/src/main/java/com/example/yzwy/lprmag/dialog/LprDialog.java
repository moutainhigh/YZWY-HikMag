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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.yzwy.lprmag.R;
import com.example.yzwy.lprmag.myConstant.WifiMsgConstant;
import com.example.yzwy.lprmag.util.InetAddressUtil;
import com.example.yzwy.lprmag.util.LogUtil;
import com.example.yzwy.lprmag.util.Tools;
import com.example.yzwy.lprmag.wifimess.model.SendOrder;
import com.example.yzwy.lprmag.wifimess.thread.ConnectThread;
import com.example.yzwy.lprmag.wifimess.thread.ListenerThread;
import com.example.yzwy.lprmag.wifimess.util.SocketUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import static com.example.yzwy.lprmag.myConstant.OrderConstant.ORDER_OrderPlate;
import static com.example.yzwy.lprmag.util.Tools.getWifiRouteIPAddress;


public class LprDialog extends Activity {


    /**
     *
     * */
    public static final int DEVICE_CONNECTING = WifiMsgConstant.DEVICE_CONNECTING;//有设备正在连接热点
    public static final int DEVICE_CONNECTED = WifiMsgConstant.DEVICE_CONNECTED;//有设备连上热点
    public static final int SEND_MSG_SUCCSEE = WifiMsgConstant.SEND_MSG_SUCCSEE;//发送消息成功
    public static final int SEND_MSG_ERROR = WifiMsgConstant.SEND_MSG_ERROR;//发送消息失败
    public static final int GET_MSG = WifiMsgConstant.GET_MSG;//获取新消息

    private TextView text_state;

    /**
     * 端口号
     */
    private WifiManager wifiManager;
    private TextView status_init;
    private TextView tv_persetnum_dialoglpr;

    /**
     * 关闭按钮
     */
    private Button btn_closePage_dialogLpr;
    private String scwidth_hik;
    private String scheight_hik;


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
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;//设置对话框置顶显示
        win.setAttributes(lp);
        //设置点击外部空白处可以关闭Activity
        this.setFinishOnTouchOutside(true);


        IntentData();

        /**
         * 初始化组件
         * */
        initView();

        initOnClick();


        /**
         * wifi模块的架子啊
         * */
        initViewWifi();


    }

    /**
     * =============================================================================================
     * 获取Intent值
     */
    private void IntentData() {
        Bundle receive = getIntent().getExtras();
        //得到随Intent传递过来的Bundle对象
        scwidth_hik = receive.getString("scwidth_hik");
        scheight_hik = receive.getString("scheight_hik");
    }

    /**
     * =============================================================================================
     * 事件监听器
     */
    private void initOnClick() {
        btn_closePage_dialogLpr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });
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




        LogUtil.showLog("LprDialog scwidth_hik--->" , scwidth_hik);
        LogUtil.showLog("LprDialog scheight_hik--->" , scheight_hik);


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String socketServerMsg = SocketUtil.getInstance().SocketRequest(getWifiRouteIPAddress(LprDialog.this), WifiMsgConstant.PORT_wifi, SendOrder.Get_OrderPlateNum(scwidth_hik,scheight_hik));
                    LogUtil.showLog("ConfigSetActivity /...", socketServerMsg);
                    HandlerMsgSend(handler, 100, "data", socketServerMsg);
                } catch (final IOException e) {
                    e.printStackTrace();
                    LogUtil.showLog("ConfigSetActivity /***", e.toString());
                    HandlerMsgSend(handler, 101, "data", e.toString());
                }
            }
        }).start();

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
        Log.i("wifiIP route ip", "wifi route ip：" + routeIp);
        //Log.i("wifiIP address ip", "wifi route ip：" + InetAddressUtil.getIP());

        return routeIp;
        //return InetAddressUtil.getIP();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            String BackMsgData = msg.getData().getString("data");
            System.out.println("返回的数据>>>>>>>>>>>>>" + BackMsgData);

            switch (msg.what) {
                case 100:


                    try {
                        JSONObject jsonObject = new JSONObject(BackMsgData);

                        int Order = Integer.valueOf(jsonObject.getString("Order"));

                        switch (Order) {

                            //返回查询所有的消息
                            case ORDER_OrderPlate:
                                String errcode = jsonObject.getString("errcode");
                                if (errcode.equals("0")) {
                                    String carNum = jsonObject.getString("carNum");
                                    tv_persetnum_dialoglpr.setText("拍到的车牌号码为：\n" + carNum);
                                } else {
                                    String errmsg = jsonObject.getString("errmsg");
                                    tv_persetnum_dialoglpr.setText(errmsg);
                                }

                                break;

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;

                case 101:
                    Tools.Toast(LprDialog.this, "失败Log" + "\n" + BackMsgData);
                    LogUtil.showLog("ResSocket >>>", BackMsgData);
                    break;
            }
        }
    };

    public void initView() {

        text_state = (TextView) findViewById(R.id.status_info);
        status_init = (TextView) findViewById(R.id.status_init);
        tv_persetnum_dialoglpr = (TextView) findViewById(R.id.tv_persetnum_dialoglpr);
        btn_closePage_dialogLpr = (Button) findViewById(R.id.btn_closePage_dialogLpr);
    }

    /**
     * =============================================================================================
     * 活动销毁活动生命周期
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        LogUtil.showLog("DiaLogPersetActivity", "准备关闭 onDestroy");
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
        //结束当前活动
        finish();
    }

    /**
     * =============================================================================================
     * 海康初次登录发消息
     *
     * @param handler
     * @param what
     * @param Key
     * @param Val
     */
    private void HandlerMsgSend(Handler handler, int what, String Key, String Val) {
        Message messageHiK_111 = new Message();
        messageHiK_111.what = what;
        Bundle bundle = new Bundle();
        bundle.putString(Key, Val);
        messageHiK_111.setData(bundle);
        handler.sendMessage(messageHiK_111);
    }


}
