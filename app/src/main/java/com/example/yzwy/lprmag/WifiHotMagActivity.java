package com.example.yzwy.lprmag;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.yzwy.lprmag.myConstant.OrderConstant;
import com.example.yzwy.lprmag.myConstant.WifiMsgConstant;
import com.example.yzwy.lprmag.control.activityStackExtends.util.ActivityStackManager;
import com.example.yzwy.lprmag.util.InetAddressUtil;
import com.example.yzwy.lprmag.util.LogUtil;
import com.example.yzwy.lprmag.util.NetUtils;
import com.example.yzwy.lprmag.util.Tools;
import com.example.yzwy.lprmag.view.SwitchButton;
import com.example.yzwy.lprmag.wifimess.model.SendOrder;
import com.example.yzwy.lprmag.wifimess.util.SocketUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static com.example.yzwy.lprmag.util.Tools.getWifiRouteIPAddress;

/**
 * #################################################################################################
 * Copyright: Copyright (c) 2018
 * Created on 2019-04-03
 * Author: 仲超(zhongchao)
 * Version 1.0
 * Describe: wifi热点管理
 * #################################################################################################
 */
public class WifiHotMagActivity extends AppCompatActivity {

    /**
     *
     * */
    private EditText edt_locIP_wifihotmag;
    /**
     *
     * */
    private EditText edt_wwwnet_wifihotmag;
    /**
     *
     * */
    private EditText edt_nettype_wifihotmag;
    /**
     *
     * */
    private EditText edt_hikusername_wifihotmag;
    /**
     *
     * */
    private EditText edt_hikpwd_wifihotmag;
    /**
     *
     * */
    private Button btn_enter_wifihotmag;
    private ImageButton imgbtn_back_wifihotmag;
    private Button btn_f5data_wifihotmag;
    private SwitchButton swhbtn_wifihot_wifihotmag;
    private TextView tv_wifihotstatus_wifihotmag;

    private boolean CloseWifiHotBlo = false;

    /**
     * 开关
     */
    private ImageButton imgbtn_wifihot_wifihotmag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ly_wifihotmag_set);


        //==========================================================================================
        ActivityStackManager.getInstance().addActivity(this);

        /**
         * 加载View
         * */
        initView();
    }

    /**
     * =============================================================================================
     * 加載View
     */
    private void initView() {
        edt_locIP_wifihotmag = (EditText) findViewById(R.id.edt_locIP_wifihotmag);
        edt_wwwnet_wifihotmag = (EditText) findViewById(R.id.edt_wwwnet_wifihotmag);
        edt_nettype_wifihotmag = (EditText) findViewById(R.id.edt_nettype_wifihotmag);
        edt_hikusername_wifihotmag = (EditText) findViewById(R.id.edt_hikusername_wifihotmag);
        edt_hikpwd_wifihotmag = (EditText) findViewById(R.id.edt_hikpwd_wifihotmag);


        btn_enter_wifihotmag = (Button) findViewById(R.id.btn_enter_wifihotmag);
        imgbtn_back_wifihotmag = (ImageButton) findViewById(R.id.imgbtn_back_wifihotmag);
        btn_f5data_wifihotmag = (Button) findViewById(R.id.btn_f5data_wifihotmag);

        //SwitchButton
        imgbtn_wifihot_wifihotmag = (ImageButton) findViewById(R.id.imgbtn_wifihot_wifihotmag);

        tv_wifihotstatus_wifihotmag = (TextView) findViewById(R.id.tv_wifihotstatus_wifihotmag);

        //设置输入框不可编辑
        setEnabled();

        //设置显示的字符
        setEditText();

        //按钮监听事件
        initOnClick();


    }

    /**
     * =============================================================================================
     * 按钮监听事件
     */
    private void initOnClick() {

        /**
         * 确认修改
         * */
        btn_enter_wifihotmag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //RestartAPPTool.restartAPP(ConfigSetActivity.this,1000);

                //数据修改
                if (UpdateCmdWifiHotConfig()) {
//                    final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
                }
            }
        });

        /**
         * 返回
         * */
        imgbtn_back_wifihotmag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WifiHotMagActivity.this.finish();
            }
        });

        /**
         * 刷新
         * */
        btn_f5data_wifihotmag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setEditText();

                Tools.Toast(WifiHotMagActivity.this, "刷新成功~");


            }
        });


        /**
         * 关闭热点按钮
         * */
        imgbtn_wifihot_wifihotmag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CloseWifiHotBlo) {
                    CloseWifiHotClick();
                } else {
                    //关闭状态下不可点击开启状态，因为无法开启终端热点
                    Tools.Toast(WifiHotMagActivity.this, "打开热点请重启终端");
                    //CloseWifiHotBlo = false;
                    //tv_wifihotstatus_wifihotmag.setText("关闭中");
                }
            }
        });
    }

    /**
     * =============================================================================================
     * 存储配置信息
     */
    private boolean UpdateCmdWifiHotConfig() {

        /**
         * 获取所有的数据框的数据，去除首位空格
         * */
        String edt_hikusername_wifihotmag_str = edt_hikusername_wifihotmag.getText().toString().trim();
        String edt_hikpwd_wifihotmag_str = edt_hikpwd_wifihotmag.getText().toString().trim();


        //==========================================================================================
        //热点名称校验
        if (edt_hikusername_wifihotmag_str.equals("")) {
            Tools.Toast(WifiHotMagActivity.this, "热点名称不能为空，请重新输入");
            return false;
        }
        if (edt_hikusername_wifihotmag_str.length() <= 4 || edt_hikusername_wifihotmag_str.length() >= 11) {
            Tools.Toast(WifiHotMagActivity.this, "热点名称必须大于4位小于11位，请重新输入");
            return false;
        }


        //==========================================================================================
        //热点密码校验
        if (edt_hikpwd_wifihotmag_str.equals("")) {
            Tools.Toast(WifiHotMagActivity.this, "热点密码不能为空，请重新输入");
            return false;
        }
        if (edt_hikpwd_wifihotmag_str.length() <= 4 || edt_hikpwd_wifihotmag_str.length() >= 21) {
            Tools.Toast(WifiHotMagActivity.this, "热点密码必须大于4位小于21位，请重新输入");
            return false;
        }

        //向终端发送设置终端热点的命令
        SetTerminalWifiHotInfio(edt_hikusername_wifihotmag_str, edt_hikpwd_wifihotmag_str);

        return true;
    }

    /**
     * =============================================================================================
     * 向终端发送获取终端热点的命令
     */
    private void GetTerminalWifiHotInfio() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String socketServerMsg = SocketUtil.getInstance().SocketRequest(getWifiRouteIPAddress(WifiHotMagActivity.this), WifiMsgConstant.PORT_wifi, SendOrder.Get_WifiHotInfo());
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
     * 向终端发送设置终端热点的命令
     */
    private void SetTerminalWifiHotInfio(final String edt_hikusername_wifihotmag_str, final String edt_hikpwd_wifihotmag_str) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String socketServerMsg = SocketUtil.getInstance().SocketRequest(getWifiRouteIPAddress(WifiHotMagActivity.this), WifiMsgConstant.PORT_wifi, SendOrder.Set_WifiHotInfo(edt_hikusername_wifihotmag_str, edt_hikpwd_wifihotmag_str));
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
     * HandlerMsgSend
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
     * 设置 EditText 不可编辑
     */
    private void setEnabled() {
        edt_locIP_wifihotmag.setEnabled(false);
        edt_wwwnet_wifihotmag.setEnabled(false);
        edt_nettype_wifihotmag.setEnabled(false);
    }

    /**
     * =============================================================================================
     * 初始化值
     */
    private void setEditText() {

        String InetAddress = InetAddressUtil.getIP();
        if (InetAddress == null || InetAddress.equals("")) {
            edt_locIP_wifihotmag.setText("0.0.0.0");
        } else {
            edt_locIP_wifihotmag.setText(InetAddressUtil.getIP());
        }


        boolean netConnected = NetUtils.isNetConnected(WifiHotMagActivity.this);
        if (netConnected) {
            edt_wwwnet_wifihotmag.setText("Internet网访问");
        } else {
            edt_wwwnet_wifihotmag.setText("网络未连接");
        }

        //java数组初始化
        String[] networkStateTypeArray = {"没有网络连接", "wifi连接", "2G", "3G", "4G", "手机流量"};
        int networkStateType = NetUtils.getNetworkState(WifiHotMagActivity.this);
        edt_nettype_wifihotmag.setText(networkStateTypeArray[networkStateType]);


        /**
         * 向终端获取热点信息
         * */
        GetTerminalWifiHotInfio();

    }

    /**
     * =============================================================================================
     * 发son关闭终端热点
     */
    private void CloseWifiHotClick() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String socketServerMsg = SocketUtil.getInstance().SocketRequest(getWifiRouteIPAddress(WifiHotMagActivity.this), WifiMsgConstant.PORT_wifi, SendOrder.Close_WifiHotInfo());
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


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String dataMsg = msg.getData().getString("data");
            System.out.println("返回的数据>>>>>>>>>>>>>" + dataMsg);

            switch (msg.what) {

                case 100:
                    try {
                        JSONObject jsonObject = new JSONObject(dataMsg);
                        int Order = Integer.valueOf(jsonObject.getString("Order"));
                        String errmsg = jsonObject.getString("errmsg");
                        String errcode = jsonObject.getString("errcode");

                        switch (Order) {

                            //返回查询所有wifi热点
                            case OrderConstant.ORDER_Get_WifiHotInfo:
                                CloseWifiHotBlo = true;
                                tv_wifihotstatus_wifihotmag.setText("打开中");
                                imgbtn_wifihot_wifihotmag.setImageResource(R.drawable.ic_swhbtn_open);
                                String wifiName = jsonObject.getString("wifiName");
                                String wifiPwd = jsonObject.getString("wifiPwd");
                                edt_hikusername_wifihotmag.setText(wifiName);
                                edt_hikpwd_wifihotmag.setText(wifiPwd);


                                break;

                            //设置终端wifi热点
                            case OrderConstant.ORDER_Set_WifiHotInfo:
                                if (errcode.equals("0")) {
                                    final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                                break;

                            //关闭终端wifi热点
                            case OrderConstant.ORDER_Close_WifiHotInfo:
                                CloseWifiHotBlo = false;
                                tv_wifihotstatus_wifihotmag.setText("关闭中");
                                Tools.Toast(WifiHotMagActivity.this, errmsg);
                                imgbtn_wifihot_wifihotmag.setImageResource(R.drawable.ic_swhbtn_close);
                                break;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        edt_hikusername_wifihotmag.setText("~~~异常~~~");
                        edt_hikpwd_wifihotmag.setText("~~~异常~~~");


                        CloseWifiHotBlo = false;
                        tv_wifihotstatus_wifihotmag.setText("异常");

                    }
                    break;

                case 101:
                    Tools.Toast(WifiHotMagActivity.this, "终端连接异常，请检查~");
                    LogUtil.showLog("ResSocket >>>", dataMsg);

                    CloseWifiHotBlo = false;
                    tv_wifihotstatus_wifihotmag.setText("异常");
                    
                    break;
            }

        }
    };


}
