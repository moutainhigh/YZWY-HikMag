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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yzwy.lprmag.R;
import com.example.yzwy.lprmag.adapter.PresetAdapter;
import com.example.yzwy.lprmag.bean.PresetBean;
import com.example.yzwy.lprmag.util.LogUtil;
import com.example.yzwy.lprmag.util.Tools;
import com.example.yzwy.lprmag.util.DisplayUtil;
import com.example.yzwy.lprmag.wifimess.model.SendOrder;
import com.example.yzwy.lprmag.wifimess.thread.ConnectThread;
import com.example.yzwy.lprmag.wifimess.thread.ListenerThread;
import com.hikvision.netsdk.HCNetSDK;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static com.hikvision.netsdk.PTZPresetCmd.CLE_PRESET;
import static com.hikvision.netsdk.PTZPresetCmd.SET_PRESET;


public class DialogPerset extends Activity {

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
     * 热点名称
     */
    private static final String WIFI_HOTSPOT_SSID = "TEST";
    /**
     * 端口号
     */
    private static final int PORT_wifi = 54321;
    private WifiManager wifiManager;
    private TextView status_init;
    private PresetAdapter adapter;


    /**
     * 预置点列表
     * 定义Bean类型的数组
     */
    private List<PresetBean> presetBeansList = new ArrayList<PresetBean>();
    private Button btnSetPreset;

    /**
     * 设置优先级
     */
    private Button btn_setpriority_setPreset;
    //    private Button btnClearPreset;
//    private Button btnGoPreset;

    /**
     * 预置点
     */
    private EditText edtPreset;

    /**
     * 地磁目标地址
     */
    private EditText edt_putGeomagnetismAddressNumber_setPreset;
    /**
     * 优先级
     */
    private EditText edt_priority_setPreset;


    private int m_iPlayID;

    private int EdtPerset = -1;
    //private int persetDelete = -1;

    /**
     * isInitAdapterPreset
     * 是否初始化适配器 false 否   true 是
     */
    private boolean isInitAdapterPreset = false;

    /**
     * 关闭页面
     * */
    private Button btn_closePage_dialogPerset;


    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //去掉标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.dialog_perset);

        //设置窗口对齐屏幕宽度
        Window win = this.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = this.getWindowManager().getDefaultDisplay().getWidth() - DisplayUtil.dip2px(DialogPerset.this, 50);
        lp.height = this.getWindowManager().getDefaultDisplay().getHeight() - DisplayUtil.dip2px(DialogPerset.this, 50);
        //lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;//设置对话框置顶显示
        win.setAttributes(lp);
        //设置点击外部空白处可以关闭Activity
        this.setFinishOnTouchOutside(true);


        /**
         * 获取Intent值
         * */
        initIntentData();


        /**
         * 初始化组件
         * */
        initView();


        /**
         * wifi模块的架子啊
         * */
        initViewWifi();


        /**
         * 按钮事件
         * */
        initClick();


    }

    /**
     * =============================================================================================
     * 获取Intent中的值（视频播放ID）
     */
    private void initIntentData() {

        m_iPlayID = Integer.valueOf(getIntent().getStringExtra("m_iPlayID"));

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
                + "\n路由：" + getWifiRouteIPAddress(DialogPerset.this));

        //initBroadcastReceiver();
        //开启连接线程00

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(getWifiRouteIPAddress(DialogPerset.this), PORT_wifi);
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

//    /**
//     * 查找当前连接状态
//     */
//    private void initBroadcastReceiver() {
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
//        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
//        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
//        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//        //        intentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
//
//        registerReceiver(receiver, intentFilter);
//    }
//
//    private BroadcastReceiver receiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            final String action = intent.getAction();
//            if (action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
//                Log.i("BBB", "SCAN_RESULTS_AVAILABLE_ACTION");
//                // wifi已成功扫描到可用wifi。
//                //                List<ScanResult> scanResults = wifiManager.getScanResults();
//                //                wifiListAdapter.clear();
//                //                wifiListAdapter.addAll(scanResults);
//            } else if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
//                Log.i("BBB", "WifiManager.WIFI_STATE_CHANGED_ACTION");
//                int wifiState = intent.getIntExtra(
//                        WifiManager.EXTRA_WIFI_STATE, 0);
//                switch (wifiState) {
//                    case WifiManager.WIFI_STATE_ENABLED:
//                        //获取到wifi开启的广播时，开始扫描
//                        //wifiManager.startScan();
//                        break;
//                    case WifiManager.WIFI_STATE_DISABLED:
//                        //wifi关闭发出的广播
//                        break;
//                }
//            } else if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
//                Log.i("BBB", "WifiManager.NETWORK_STATE_CHANGED_ACTION");
//                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
//                if (info.getState().equals(NetworkInfo.State.DISCONNECTED)) {
//                    text_state.setText("连接已断开");
//                } else if (info.getState().equals(NetworkInfo.State.CONNECTED)) {
//                    WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//                    final WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//                    text_state.setText("已连接到网络:" + wifiInfo.getSSID()
//                            + "\n" + wifiInfo.getIpAddress()
//                            + "\n" + wifiInfo.getNetworkId()
//                            + "\n" + wifiInfo.getMacAddress());
//                    Log.i("AAA", "wifiInfo.getSSID():" + wifiInfo.getSSID() +
//                            "  WIFI_HOTSPOT_SSID:" + WIFI_HOTSPOT_SSID);
//                    if (wifiInfo.getSSID().equals(WIFI_HOTSPOT_SSID)) {
//                        //如果当前连接到的wifi是热点,则开启连接线程
//
//
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                try {
//                                    ArrayList<String> connectedIP = getConnectedIP();
//                                    for (String ip : connectedIP) {
//                                        if (ip.contains(".")) {
//                                            Log.i("AAA", "IP:" + ip);
//                                            Socket socket = new Socket(ip, PORT_wifi);
//                                            connectThread = new ConnectThread(socket, handler);
//                                            connectThread.start();
//                                        }
//                                    }
//
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }).start();
//                    }
//                } else {
//                    NetworkInfo.DetailedState state = info.getDetailedState();
//                    if (state == state.CONNECTING) {
//                        text_state.setText("连接中...");
//                    } else if (state == state.AUTHENTICATING) {
//                        text_state.setText("正在验证身份信息...");
//                    } else if (state == state.OBTAINING_IPADDR) {
//                        text_state.setText("正在获取IP地址...");
//                    } else if (state == state.FAILED) {
//                        text_state.setText("连接失败");
//                    }
//                }
//
//            }
//           /* else if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
//                if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)) {
//                    text_state.setText("连接已断开");
//                    wifiManager.removeNetwork(wcgID);
//                } else {
//                    WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
//                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//                    text_state.setText("已连接到网络:" + wifiInfo.getSSID());
//                }
//            }*/
//        }
//    };

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
                    connectThread.sendData(SendOrder.getPersetData());
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
                            case "Order0":

                                JSONArray jsonArrayPrset = jsonObject.getJSONArray("data");

                                String Priority = jsonObject.getString("Priority");

                                if (presetBeansList.size() > 0) {
                                    presetBeansList.clear();//清除之前的数据
                                }

                                for (int i = 0; i < jsonArrayPrset.length(); i++) {//内部不锁定，效率最高，但在多线程要考虑并发操作的问题。
                                    JSONObject jsonArrayPrsetObj = jsonArrayPrset.getJSONObject(i);
                                    PresetBean itemPresetBean = new PresetBean(jsonArrayPrsetObj.getString("GeomagnetismAddressNumber"), i + "", jsonArrayPrsetObj.getString("PersetNumber"));
                                    presetBeansList.add(itemPresetBean);
                                }

                                if (!isInitAdapterPreset) {
                                    /**
                                     * 初始化适配器
                                     * */
                                    initAdapter();
                                    isInitAdapterPreset = true;
                                } else {
                                    adapter.notifyDataSetChanged(); //更新界面
                                }

                                if (!Priority.equals("0")) {
                                    edt_priority_setPreset.setText(Priority);
                                }


                                break;

                            //新增预置点
                            case "Order1":
                                String errcode = jsonObject.getString("errcode");
                                String errmsg = jsonObject.getString("errmsg");
                                if (errcode.equals("0")) {
                                    /**
                                     * 重新设置数据
                                     * */
                                    //adapter.notifyDataSetChanged();

                                    boolean b = HCNetSDK.getInstance().NET_DVR_PTZPreset(m_iPlayID, SET_PRESET, EdtPerset);
                                    Tools.Toast(DialogPerset.this, "预置点设置成功");
                                    //向终端发送获取所有预置点的命令
                                    connectThread.sendData(SendOrder.getPersetData());
                                    //EdtPerset = -1;
                                } else {
                                    Tools.Toast(DialogPerset.this, errmsg);
                                }
                                break;

                            case "Order2":
                                String errcodeDelete = jsonObject.getString("errcode");
                                String errmsgDelete = jsonObject.getString("errmsg");
                                if (errcodeDelete.equals("0")) {
                                    boolean b = HCNetSDK.getInstance().NET_DVR_PTZPreset(m_iPlayID, CLE_PRESET, EdtPerset);
                                    if (b) {
                                        Tools.Toast(DialogPerset.this, "预置点删除成功");
                                        //向终端发送获取所有预置点的命令
                                        connectThread.sendData(SendOrder.getPersetData());
                                    } else {
                                        Tools.Toast(DialogPerset.this, "预置点删除失败");
                                    }


                                } else {
                                    Tools.Toast(DialogPerset.this, errmsgDelete);
                                }
                                break;


                            case "Order3":
                                String errcodeUpdate = jsonObject.getString("errcode");
                                String errmsgUpdate = jsonObject.getString("errmsg");
                                if (errcodeUpdate.equals("0")) {
                                    boolean b = HCNetSDK.getInstance().NET_DVR_PTZPreset(m_iPlayID, SET_PRESET, EdtPerset);
                                    Tools.Toast(DialogPerset.this, "预置点修改成功");
                                    //向终端发送获取所有预置点的命令
                                    connectThread.sendData(SendOrder.getPersetData());
                                } else {
                                    Tools.Toast(DialogPerset.this, errmsgUpdate);
                                }
                                break;


                            case "ORDER_SetPriority":
                                String errcodePriority = jsonObject.getString("errcode");
                                String errmsgPriority = jsonObject.getString("errmsg");
                                if (errcodePriority.equals("0")) {
                                    Tools.Toast(DialogPerset.this, errmsgPriority);
                                } else {
                                    Tools.Toast(DialogPerset.this, errmsgPriority);
                                }
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

        //实例化设置预置点
        btnSetPreset = (Button) findViewById(R.id.btn_setPreset_setPreset);
//        //实例化清除预置点
//        btnClearPreset = (Button) findViewById(R.id.btn_clearPreset_setPreset);
//        //实例化转到预置点
//        btnGoPreset = (Button) findViewById(R.id.btn_goTo_setPreset);
        //设置优先级
        btn_setpriority_setPreset = (Button) findViewById(R.id.btn_setpriority_setPreset);

        //设置获取预置点的序号
        edtPreset = (EditText) findViewById(R.id.edt_putPreset_setPreset);
        edt_putGeomagnetismAddressNumber_setPreset = (EditText) findViewById(R.id.edt_putGeomagnetismAddressNumber_setPreset);
        edt_priority_setPreset = (EditText) findViewById(R.id.edt_priority_setPreset);
        btn_closePage_dialogPerset = (Button) findViewById(R.id.btn_closePage_dialogPerset);
    }

    /**
     * =============================================================================================
     * 加载RecyclerView适配器
     */
    public void initAdapter() {

        /**
         * -----------------------------------------------------------------------------------------
         * 获取预置点列表
         * */
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recv_item_setPreset);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PresetAdapter(presetBeansList, this.m_iPlayID, this.connectThread);
        recyclerView.setAdapter(adapter);

    }

    /**
     * =============================================================================================
     * 加载事件监听器
     */
    public void initClick() {

        /**
         * 设置预置点
         * */
        btnSetPreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取输入框的预置点序号 转化为INT类型
                EdtPerset = Integer.valueOf(edtPreset.getText().toString());
                String GeomagnetismAddressNumber = edt_putGeomagnetismAddressNumber_setPreset.getText().toString();

                //判断预置点的范围
                if (EdtPerset > 255 || EdtPerset < 0) {
                    Toast.makeText(DialogPerset.this, "请设置预置点1-255之间", Toast.LENGTH_SHORT).show();
                    return;
                }

                //判断预置点的范围
                if (GeomagnetismAddressNumber == null || GeomagnetismAddressNumber.equals("")) {
                    Toast.makeText(DialogPerset.this, "请输入地磁预置点", Toast.LENGTH_SHORT).show();
                    return;
                }
                //发送数据
                connectThread.sendData(SendOrder.setPersetData(GeomagnetismAddressNumber, String.valueOf(EdtPerset)));
            }
        });//设置预置点

//        /**
//         * 清除预置点，是清除的某一个预置点
//         * */
//        btnClearPreset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EdtPerset = Integer.valueOf(edtPreset.getText().toString());
//                if (EdtPerset > 255 || EdtPerset < 0) {
//                    Toast.makeText(DialogPerset.this, "请设置1-255之间", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                connectThread.sendData(SendOrder.deletePersetData(String.valueOf(EdtPerset)));
//            }
//        });
//        /**
//         * 转到预置点
//         * */
//        btnGoPreset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EdtPerset = Integer.valueOf(edtPreset.getText().toString());
//                if (EdtPerset > 255 || EdtPerset < 0) {
//                    Toast.makeText(DialogPerset.this, "请设置0-255之间", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                boolean b = HCNetSDK.getInstance().NET_DVR_PTZPreset(m_iPlayID, PTZCommand.GOTO_PRESET, EdtPerset);
//                if (b) {
//                    Tools.Toast(DialogPerset.this, "预置点转到成功");
//                } else {
//
//                    Tools.Toast(DialogPerset.this, "预置点转到失败");
//                }
//            }
//        });


        btn_setpriority_setPreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edtPriorityStr = edt_priority_setPreset.getText().toString();
                if (edtPriorityStr.equals("")) {
                    Tools.Toast(DialogPerset.this, "优先级不能为空");
                }
//                else if (edtPriorityStr.length() != 8) {
//                    Tools.Toast(DialogPerset.this, "优先级必须输入8位长度");
//                }
                //发送数据
                connectThread.sendData(SendOrder.setPriority(edtPriorityStr));
            }
        });


        btn_closePage_dialogPerset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });

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
