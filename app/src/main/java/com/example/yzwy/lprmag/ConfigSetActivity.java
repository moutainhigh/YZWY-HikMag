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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yzwy.lprmag.myConstant.HiKConfigDataConstant;
import com.example.yzwy.lprmag.myConstant.OrderConstant;
import com.example.yzwy.lprmag.myConstant.WifiMsgConstant;
import com.example.yzwy.lprmag.control.activityStackExtends.util.ActivityStackManager;
import com.example.yzwy.lprmag.util.InetAddressUtil;
import com.example.yzwy.lprmag.util.LogUtil;
import com.example.yzwy.lprmag.util.NetUtils;
import com.example.yzwy.lprmag.util.Tools;
import com.example.yzwy.lprmag.util.SharePreferencesUtil;
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
 * Describe: 海康摄像头配置页面
 * #################################################################################################
 */
public class ConfigSetActivity extends AppCompatActivity {

    /**
     * 本机IP
     */
    private EditText edt_locIP_tremconn;
    /**
     * 本机Internet状态
     */
    private EditText edt_wwwnet_tremconn;
    /**
     * 本机Internet类型
     */
    private EditText edt_nettype_tremconn;
    /**
     * 海康本地地址
     */
    private EditText edt_hkIp_cfgset;
    /**
     * 海康本地端口号
     */
    private EditText edt_hkport_cfgset;
    /**
     * 海康本地用户名
     */
    private EditText edt_hikusername_cfgset;
    /**
     * 海康本地密码
     */
    private EditText edt_hikpwd_cfgset;
    /**
     * 确认按钮
     */
    private Button btn_enter_cfgset;

    /**
     * 返回按钮
     */
    private ImageButton imgbtn_back_cfgset;

    /**
     * 刷新按钮
     */
    private Button btn_f5data_cfgset;

    /**
     * 海康同步终端文本显示
     */
    private TextView tv_hikpushstatus_cfgset;

    /**
     * 海康同步终端按钮
     */
    private ImageButton imgbtn_hikpush_cfgset;
    private EditText edt_conntern_tremconn;
    private LinearLayout li_conntern_tremconn;
    private View view_conntern_tremconn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ly_config_set);
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
//        edt_locIP_tremconn = (EditText) findViewById(R.id.edt_locIP_cfgset);
//        edt_wwwnet_tremconn = (EditText) findViewById(R.id.edt_wwwnet_cfgset);
//        edt_nettype_tremconn = (EditText) findViewById(R.id.edt_nettype_cfgset);


        edt_locIP_tremconn = (EditText) findViewById(R.id.edt_locIP_tremconn);
        edt_wwwnet_tremconn = (EditText) findViewById(R.id.edt_wwwnet_tremconn);
        edt_nettype_tremconn = (EditText) findViewById(R.id.edt_nettype_tremconn);
        edt_conntern_tremconn = (EditText) findViewById(R.id.edt_conntern_tremconn);

        /**
         * 隐藏
         * */
        li_conntern_tremconn = (LinearLayout) findViewById(R.id.li_conntern_tremconn);
        view_conntern_tremconn = (View) findViewById(R.id.view_conntern_tremconn);
        li_conntern_tremconn.setVisibility(View.GONE);
        view_conntern_tremconn.setVisibility(View.GONE);


        edt_hkIp_cfgset = (EditText) findViewById(R.id.edt_hkIp_cfgset);
        edt_hkport_cfgset = (EditText) findViewById(R.id.edt_hkport_cfgset);
        edt_hikusername_cfgset = (EditText) findViewById(R.id.edt_hikusername_cfgset);
        edt_hikpwd_cfgset = (EditText) findViewById(R.id.edt_hikpwd_cfgset);


        btn_enter_cfgset = (Button) findViewById(R.id.btn_enter_cfgset);
        imgbtn_back_cfgset = (ImageButton) findViewById(R.id.imgbtn_back_cfgset);
        imgbtn_hikpush_cfgset = (ImageButton) findViewById(R.id.imgbtn_hikpush_cfgset);
        btn_f5data_cfgset = (Button) findViewById(R.id.btn_f5data_cfgset);
        tv_hikpushstatus_cfgset = (TextView) findViewById(R.id.tv_hikpushstatus_cfgset);

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
         * -----------------------------------------------------------------------------------------
         * 确认按钮事件
         * */
        btn_enter_cfgset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String edt_hkIp_cfgset_str = edt_hkIp_cfgset.getText().toString().trim();
                String edt_hkport_cfgset_str = edt_hkport_cfgset.getText().toString().trim();
                String edt_hikusername_cfgset_str = edt_hikusername_cfgset.getText().toString().trim();
                String edt_hikpwd_cfgset_str = edt_hikpwd_cfgset.getText().toString().trim();

                //数据修改
                if (SharedPreferencesConfigData(edt_hkIp_cfgset_str, edt_hkport_cfgset_str, edt_hikusername_cfgset_str, edt_hikpwd_cfgset_str)) {
                    final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });


        /**
         * -----------------------------------------------------------------------------------------
         * 返回按钮事件
         * */
        imgbtn_back_cfgset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfigSetActivity.this.finish();
            }
        });

        /**
         * -----------------------------------------------------------------------------------------
         * 刷新按钮事件
         * */
        btn_f5data_cfgset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEditText();
                Tools.Toast(ConfigSetActivity.this, "刷新成功~");
            }
        });

        imgbtn_hikpush_cfgset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String edt_hkIp_cfgset_str = edt_hkIp_cfgset.getText().toString().trim();
                final String edt_hkport_cfgset_str = edt_hkport_cfgset.getText().toString().trim();
                final String edt_hikusername_cfgset_str = edt_hikusername_cfgset.getText().toString().trim();
                final String edt_hikpwd_cfgset_str = edt_hikpwd_cfgset.getText().toString().trim();

                //数据修改
                if (SharedPreferencesConfigData(edt_hkIp_cfgset_str, edt_hkport_cfgset_str, edt_hikusername_cfgset_str, edt_hikpwd_cfgset_str)) {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String socketServerMsg = SocketUtil.getInstance().SocketRequest(getWifiRouteIPAddress(ConfigSetActivity.this), WifiMsgConstant.PORT_wifi, SendOrder.ORDER_PushHiKConfig(edt_hkIp_cfgset_str, edt_hkport_cfgset_str, edt_hikusername_cfgset_str, edt_hikpwd_cfgset_str));
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
            }
        });

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

    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String dataMsg = msg.getData().getString("data");
            switch (msg.what) {
                case 100:

                    try {
                        JSONObject jsonObject = new JSONObject(dataMsg);

                        int Order = Integer.valueOf(jsonObject.getString("Order"));

                        switch (Order) {

                            //返回同步海康配置的的消息
                            case OrderConstant.ORDER_PushHiKConfig:
                                String errcode = jsonObject.getString("errcode");
                                if (errcode.equals("0")) {
                                    String errmsg = jsonObject.getString("errmsg");
                                    Tools.Toast(ConfigSetActivity.this, "同步成功");
                                    tv_hikpushstatus_cfgset.setText("同步成功");
                                } else {
                                    String errmsg = jsonObject.getString("errmsg");
                                    Tools.Toast(ConfigSetActivity.this, errmsg);
                                    tv_hikpushstatus_cfgset.setText("同步失败");
                                }

                                break;

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                case 101:
                    Tools.Toast(ConfigSetActivity.this, "终端连接异常，请检查~");
                    LogUtil.showLog("ResSocket >>>", dataMsg);
                    break;
            }
        }
    };

    /**
     * =============================================================================================
     * 存储配置信息
     */
    private boolean SharedPreferencesConfigData(String edt_hkIp_cfgset_str, String edt_hkport_cfgset_str, String edt_hikusername_cfgset_str, String edt_hikpwd_cfgset_str) {

        /**
         * 获取所有的数据框的数据，去除首位空格
         * */
        //String edt_locIP_cfgset_str = edt_locIP_tremconn.getText().toString().trim();
        //String edt_wwwnet_cfgset_str = edt_wwwnet_tremconn.getText().toString().trim();
        //String edt_nettype_cfgset_str = edt_nettype_tremconn.getText().toString().trim();

//        String edt_hkIp_cfgset_str = edt_hkIp_cfgset.getText().toString().trim();
//        String edt_hkport_cfgset_str = edt_hkport_cfgset.getText().toString().trim();
//        String edt_hikusername_cfgset_str = edt_hikusername_cfgset.getText().toString().trim();
//        String edt_hikpwd_cfgset_str = edt_hikpwd_cfgset.getText().toString().trim();

        //==========================================================================================
        //海康IP地址校验
        if (!Tools.isIP(edt_hkIp_cfgset_str)) {
            Tools.Toast(ConfigSetActivity.this, "海康IP地址格式不正确，请重新输入");
            return false;
        }

        //==========================================================================================
        //海康端口号校验
        if (edt_hkport_cfgset_str.equals("")) {
            Tools.Toast(ConfigSetActivity.this, "海康端口号不能为空，请重新输入");
            return false;
        }
        if (edt_hkport_cfgset_str.substring(0, 1).equals("0")) {
            Tools.Toast(ConfigSetActivity.this, "海康端口号首字符不能为0，请重新输入");
            return false;
        }

        //==========================================================================================
        //海康用户名校验
        if (edt_hikusername_cfgset_str.equals("")) {
            Tools.Toast(ConfigSetActivity.this, "海康用户名不能为空，请重新输入");
            return false;
        }
        if (edt_hikusername_cfgset_str.length() <= 4 || edt_hikusername_cfgset_str.length() >= 11) {
            Tools.Toast(ConfigSetActivity.this, "海康用户名必须大于4位小于11位，请重新输入");
            return false;
        }


        //==========================================================================================
        //海康密码校验
        if (edt_hikpwd_cfgset_str.equals("")) {
            Tools.Toast(ConfigSetActivity.this, "海康密码不能为空，请重新输入");
            return false;
        }
        if (edt_hikpwd_cfgset_str.length() <= 4 || edt_hikpwd_cfgset_str.length() >= 21) {
            Tools.Toast(ConfigSetActivity.this, "海康密码必须大于4位小于21位，请重新输入");
            return false;
        }

        PutSpHiConfig(edt_hkIp_cfgset_str, edt_hkport_cfgset_str, edt_hikusername_cfgset_str, edt_hikpwd_cfgset_str);

        return true;
    }

    /**
     * =============================================================================================
     * 提交数据，保存数据
     */
    private void PutSpHiConfig(String edt_hkIp_cfgset_str, String edt_hkport_cfgset_str, String edt_hikusername_cfgset_str, String edt_hikpwd_cfgset_str) {
        SharePreferencesUtil.putStringValue(ConfigSetActivity.this, HiKConfigDataConstant.hkIp_cfgset_str, edt_hkIp_cfgset_str);
        SharePreferencesUtil.putStringValue(ConfigSetActivity.this, HiKConfigDataConstant.hkport_cfgset_str, edt_hkport_cfgset_str);
        SharePreferencesUtil.putStringValue(ConfigSetActivity.this, HiKConfigDataConstant.hikusername_cfgset_str, edt_hikusername_cfgset_str);
        SharePreferencesUtil.putStringValue(ConfigSetActivity.this, HiKConfigDataConstant.hikpwd_cfgset_str, edt_hikpwd_cfgset_str);

    }

    /**
     * =============================================================================================
     * 设置 EditText 不可编辑
     */
    private void setEnabled() {
        edt_locIP_tremconn.setEnabled(false);
        edt_wwwnet_tremconn.setEnabled(false);
        edt_nettype_tremconn.setEnabled(false);
        //edt_hkIp_cfgset.setEnabled(false);
        //edt_hkport_cfgset.setEnabled(false);
        //edt_hikusername_cfgset.setEnabled(false);
        //edt_hikpwd_cfgset.setEnabled(false);
    }

    /**
     * =============================================================================================
     * 初始化值
     */
    private void setEditText() {

        String InetAddress = InetAddressUtil.getIP();
        if (InetAddress == null || InetAddress.equals("")) {
            edt_locIP_tremconn.setText("0.0.0.0");
        } else {
            edt_locIP_tremconn.setText(InetAddressUtil.getIP());
        }


        boolean netConnected = NetUtils.isNetConnected(ConfigSetActivity.this);
        if (netConnected) {
            edt_wwwnet_tremconn.setText("Internet网访问");
        } else {
            edt_wwwnet_tremconn.setText("网络未连接");
        }

        //java数组初始化
        String[] networkStateTypeArray = {"没有网络连接", "wifi连接", "2G", "3G", "4G", "手机流量"};
        int networkStateType = NetUtils.getNetworkState(ConfigSetActivity.this);
        edt_nettype_tremconn.setText(networkStateTypeArray[networkStateType]);

        String hkIp_cfgset_str = SharePreferencesUtil.getStringValue(ConfigSetActivity.this, HiKConfigDataConstant.hkIp_cfgset_str, HiKConfigDataConstant.hkIp_cfgset_str_default);
        String hkport_cfgset = SharePreferencesUtil.getStringValue(ConfigSetActivity.this, HiKConfigDataConstant.hkport_cfgset_str, HiKConfigDataConstant.hkport_cfgset_str_default);
        String hikusername_cfgset = SharePreferencesUtil.getStringValue(ConfigSetActivity.this, HiKConfigDataConstant.hikusername_cfgset_str, HiKConfigDataConstant.hikusername_cfgset_str_default);
        String hikpwd_cfgset = SharePreferencesUtil.getStringValue(ConfigSetActivity.this, HiKConfigDataConstant.hikpwd_cfgset_str, HiKConfigDataConstant.hikpwd_cfgset_str_default);

        edt_hkIp_cfgset.setText(hkIp_cfgset_str);
        edt_hkport_cfgset.setText(hkport_cfgset);
        edt_hikusername_cfgset.setText(hikusername_cfgset);
        edt_hikpwd_cfgset.setText(hikpwd_cfgset);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
