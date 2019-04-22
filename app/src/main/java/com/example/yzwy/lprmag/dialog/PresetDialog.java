package com.example.yzwy.lprmag.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import com.example.yzwy.lprmag.bean.HiKEventBus;
import com.example.yzwy.lprmag.bean.PresetBean;
import com.example.yzwy.lprmag.myConstant.HiKEventBusConstant;
import com.example.yzwy.lprmag.myConstant.OrderConstant;
import com.example.yzwy.lprmag.myConstant.WifiMsgConstant;
import com.example.yzwy.lprmag.util.LogUtil;
import com.example.yzwy.lprmag.util.Tools;
import com.example.yzwy.lprmag.util.DisplayUtil;
import com.example.yzwy.lprmag.wifimess.model.SendOrder;
import com.example.yzwy.lprmag.wifimess.util.SocketUtil;
import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.PTZCommand;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.yzwy.lprmag.util.Tools.getWifiRouteIPAddress;
import static com.hikvision.netsdk.PTZPresetCmd.CLE_PRESET;
import static com.hikvision.netsdk.PTZPresetCmd.SET_PRESET;


public class PresetDialog extends Activity {


    /**
     * 端口号
     */

    private WifiManager wifiManager;
    private TextView status_init_dialogPreset;
    private PresetAdapter adapter;


    /**
     * 预置点列表
     * 定义Bean类型的数组
     */
    private List<PresetBean> presetBeansList = new ArrayList<PresetBean>();
    private Button btnSetPreset;

    /**
     * 预置点
     */
    private EditText edtPreset;

    /**
     * 地磁目标地址
     */
    private EditText edt_putGeomagnetismAddressNumber_setPreset;


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
     */
    private Button btn_closePage_dialogPerset;
    private String scwidth_hik = "";
    private String scheight_hik = "";


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
        lp.width = this.getWindowManager().getDefaultDisplay().getWidth() - DisplayUtil.dip2px(PresetDialog.this, 25);
        lp.height = this.getWindowManager().getDefaultDisplay().getHeight() - DisplayUtil.dip2px(PresetDialog.this, 25);
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


        GetAllPersetSocketRequest();


    }

    /**
     * =============================================================================================
     * 获取Intent中的值（视频播放ID）
     */
    private void initIntentData() {

        m_iPlayID = Integer.valueOf(getIntent().getStringExtra("m_iPlayID"));
        Bundle receive = getIntent().getExtras();
        //得到随Intent传递过来的Bundle对象
        scwidth_hik = receive.getString("scwidth_hik");
        scheight_hik = receive.getString("scheight_hik");
        m_iPlayID = Integer.valueOf(receive.getString("m_iPlayID"));

    }


    /**
     * =============================================================================================
     * wifi模块的加载
     */
    @SuppressLint("SetTextI18n")
    private void initViewWifi() {
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        //Tools.Toast(PresetDialog.this,wifiManager.isWifiEnabled()+"<-----------");

        //检查Wifi状态
        if (!wifiManager.isWifiEnabled())
            wifiManager.setWifiEnabled(true);

        LogUtil.showLog("PresetDialog --->", "已连接到：" + wifiManager.getConnectionInfo().getSSID() + "\nIP:" + getIp() + "\n路由：" + getWifiRouteIPAddress(PresetDialog.this));

        status_init_dialogPreset.setText("已连接到：" + wifiManager.getConnectionInfo().getSSID() + "\nIP:" + getIp() + "\n路由：" + getWifiRouteIPAddress(PresetDialog.this));

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

//    /**
//     * wifi获取 已连接网络路由  路由ip地址---方法同上
//     *
//     * @param context
//     * @return
//     */
//    private static String getWifiRouteIPAddress(Context context) {
//        WifiManager wifi_service = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//        DhcpInfo dhcpInfo = wifi_service.getDhcpInfo();
//        String routeIp = Formatter.formatIpAddress(dhcpInfo.gateway);
//        Log.i("route ip", "wifi route ip：" + routeIp);
//
//        return routeIp;
//    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {


            final String dataMsg = msg.getData().getString("data");

            System.out.println("返回的数据>>>>>>>>>>>>>" + dataMsg);

            switch (msg.what) {

                case 100:


                    try {
                        JSONObject jsonObject = new JSONObject(dataMsg);
                        int Order = Integer.valueOf(jsonObject.getString("Order"));

                        switch (Order) {

                            //返回查询所有预置点的消息
                            case OrderConstant.SELECT_PERSET_ALL:

                                JSONArray jsonArrayPrset = jsonObject.getJSONArray("data");

                                String Priority = jsonObject.getString("Priority");

                                if (presetBeansList.size() > 0) {
                                    presetBeansList.clear();//清除之前的数据
                                }

                                for (int i = 0; i < jsonArrayPrset.length(); i++) {//内部不锁定，效率最高，但在多线程要考虑并发操作的问题。
                                    JSONObject jsonArrayPrsetObj = jsonArrayPrset.getJSONObject(i);
                                    PresetBean itemPresetBean = new PresetBean(jsonArrayPrsetObj.getString("GeomagnetismAddressNumber"), i + "", jsonArrayPrsetObj.getString("PersetNumber"), jsonArrayPrsetObj.getString("ScaleWidth"), jsonArrayPrsetObj.getString("ScaleHeight"));
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

//                                if (!Priority.equals("0")) {
//                                    edt_priority_setPreset.setText(Priority);
//                                }


                                break;

                            //新增预置点
                            case OrderConstant.ORDER_SET:
                                String errcode = jsonObject.getString("errcode");
                                String errmsg = jsonObject.getString("errmsg");
                                if (errcode.equals("0")) {
                                    /**
                                     * 重新设置数据
                                     * */
                                    boolean b = HCNetSDK.getInstance().NET_DVR_PTZPreset(m_iPlayID, SET_PRESET, EdtPerset);
                                    Tools.Toast(PresetDialog.this, "预置点设置成功");

                                    //请求获取所有的预置点信息
                                    GetAllPersetSocketRequest();

                                } else {
                                    Tools.Toast(PresetDialog.this, errmsg);
                                }
                                break;

                            case OrderConstant.ORDER_DELETE:
                                String errcodeDelete = jsonObject.getString("errcode");
                                String errmsgDelete = jsonObject.getString("errmsg");
                                if (errcodeDelete.equals("0")) {
                                    boolean b = HCNetSDK.getInstance().NET_DVR_PTZPreset(m_iPlayID, CLE_PRESET, EdtPerset);
                                    if (b) {
                                        Tools.Toast(PresetDialog.this, "预置点删除成功");

                                        //请求获取所有的预置点信息
                                        GetAllPersetSocketRequest();

                                    } else {
                                        Tools.Toast(PresetDialog.this, "预置点删除失败");
                                    }


                                } else {
                                    Tools.Toast(PresetDialog.this, errmsgDelete);
                                }
                                break;


                            case OrderConstant.ORDER_Update_Preset:
                                String errcodeUpdate = jsonObject.getString("errcode");
                                String errmsgUpdate = jsonObject.getString("errmsg");
                                String PersetNumberUpdate = jsonObject.getString("PersetNumber");
                                if (errcodeUpdate.equals("0")) {
                                    boolean b = HCNetSDK.getInstance().NET_DVR_PTZPreset(m_iPlayID, SET_PRESET, Integer.parseInt(PersetNumberUpdate));
                                    Tools.Toast(PresetDialog.this, errmsgUpdate);

                                    //请求获取所有的预置点信息
                                    GetAllPersetSocketRequest();


                                } else {
                                    Tools.Toast(PresetDialog.this, errmsgUpdate);
                                }
                                break;

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;

                case 101:
                    Tools.Toast(PresetDialog.this, "失败Log" + "\n" + dataMsg);
                    LogUtil.showLog("ResSocket >>>", dataMsg);
                    break;

                case 102:

                    //请求删除的预置点信息
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String socketServerMsg = SocketUtil.getInstance().SocketRequest(getWifiRouteIPAddress(PresetDialog.this), WifiMsgConstant.PORT_wifi, SendOrder.Delete_PersetData(dataMsg));
                                LogUtil.showLog("ConfigSetActivity /...", socketServerMsg);
                                HandlerMsgSend(handler, 100, "data", socketServerMsg);
                            } catch (final IOException e) {
                                e.printStackTrace();
                                LogUtil.showLog("ConfigSetActivity /***", e.toString());
                                HandlerMsgSend(handler, 101, "data", e.toString());
                            }
                        }
                    }).start();

                    break;

                //修改预置点
                case 103:

                    //请求删除的预置点信息
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String socketServerMsg = SocketUtil.getInstance().SocketRequest(getWifiRouteIPAddress(PresetDialog.this), WifiMsgConstant.PORT_wifi, SendOrder.Update_PersetData(dataMsg, scwidth_hik, scheight_hik));
                                LogUtil.showLog("ConfigSetActivity /...", socketServerMsg);
                                HandlerMsgSend(handler, 100, "data", socketServerMsg);
                            } catch (final IOException e) {
                                e.printStackTrace();
                                LogUtil.showLog("ConfigSetActivity /***", e.toString());
                                HandlerMsgSend(handler, 101, "data", e.toString());
                            }
                        }
                    }).start();

                    break;

                //转到预置点
                case 104:

                    boolean b = HCNetSDK.getInstance().NET_DVR_PTZPreset(m_iPlayID, PTZCommand.GOTO_PRESET, Integer.valueOf(dataMsg));
                    if (b) {
                        String ScaleWidth = msg.getData().getString("ScaleWidth");
                        String ScaleHeight = msg.getData().getString("ScaleHeight");
                        HiKEventBus hiKEventBus = new HiKEventBus();
                        hiKEventBus.setEventCode(HiKEventBusConstant.Change_Scale);
                        hiKEventBus.setScaleWidth(ScaleWidth);
                        hiKEventBus.setScaleHeight(ScaleHeight);
                        EventBus.getDefault().post(hiKEventBus);
                        Tools.Toast(PresetDialog.this, "预置点转到成功");
                    } else {

                        Tools.Toast(PresetDialog.this, "预置点转到失败");
                    }
                    break;
            }
        }
    };


    public void initView() {

        status_init_dialogPreset = (TextView) findViewById(R.id.status_init_dialogPreset);

        //实例化设置预置点
        btnSetPreset = (Button) findViewById(R.id.btn_setPreset_setPreset);

        //设置获取预置点的序号
        edtPreset = (EditText) findViewById(R.id.edt_putPreset_setPreset);

        edt_putGeomagnetismAddressNumber_setPreset = (EditText) findViewById(R.id.edt_putGeomagnetismAddressNumber_setPreset);

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
        adapter = new PresetAdapter(presetBeansList, this.m_iPlayID, handler);
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

                String EdtPersetStr = edtPreset.getText().toString().trim();

                //判断预置点的范围
                if (EdtPersetStr.equals("")) {
                    Toast.makeText(PresetDialog.this, "预置点不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                //获取输入框的预置点序号 转化为INT类型
                EdtPerset = Integer.valueOf(EdtPersetStr);
                final String GeomagnetismAddressNumber = edt_putGeomagnetismAddressNumber_setPreset.getText().toString();

                //判断预置点的范围
                if (EdtPerset > 255 || EdtPerset < 0) {
                    Toast.makeText(PresetDialog.this, "请设置预置点1-255之间", Toast.LENGTH_SHORT).show();
                    return;
                }

                //判断预置点的范围
                if (GeomagnetismAddressNumber == null || GeomagnetismAddressNumber.equals("")) {
                    Toast.makeText(PresetDialog.this, "请输入地磁预置点", Toast.LENGTH_SHORT).show();
                    return;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            String socketServerMsg = SocketUtil.getInstance()
                                    .SocketRequest(getWifiRouteIPAddress(PresetDialog.this), WifiMsgConstant.PORT_wifi, SendOrder.Set_PersetData(GeomagnetismAddressNumber, String.valueOf(EdtPerset), scwidth_hik, scheight_hik));
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
        });

//        btn_setpriority_setPreset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final String edtPriorityStr = edt_priority_setPreset.getText().toString();
//                if (edtPriorityStr.equals("")) {
//                    Tools.Toast(PresetDialog.this, "优先级不能为空");
//                }
////                else if (edtPriorityStr.length() != 8) {
////                    Tools.Toast(PresetDialog.this, "优先级必须输入8位长度");
////                }
//
//
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            String socketServerMsg = SocketUtil.getInstance().SocketRequest(getWifiRouteIPAddress(PresetDialog.this), WifiMsgConstant.PORT_wifi, SendOrder.Set_Priority(edtPriorityStr));
//                            LogUtil.showLog("ConfigSetActivity /...", socketServerMsg);
//                            HandlerMsgSend(handler, 100, "data", socketServerMsg);
//                        } catch (final IOException e) {
//                            e.printStackTrace();
//                            LogUtil.showLog("ConfigSetActivity /***", e.toString());
//                            HandlerMsgSend(handler, 101, "data", e.toString());
//                        }
//                    }
//                }).start();
//            }
//        });


        btn_closePage_dialogPerset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });

    }

    /**
     * =============================================================================================
     * 请求获取所有的预置点信息
     */
    private void GetAllPersetSocketRequest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String socketServerMsg = SocketUtil.getInstance().SocketRequest(getWifiRouteIPAddress(PresetDialog.this), WifiMsgConstant.PORT_wifi, SendOrder.Get_PersetData());
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


    /**
     * =============================================================================================
     * 活动销毁活动生命周期
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        //结束当前活动
        finish();
    }


}
