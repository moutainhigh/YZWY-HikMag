package com.example.yzwy.lprmag;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.yzwy.lprmag.control.activityStackExtends.util.ActivityStackManager;
import com.example.yzwy.lprmag.myConstant.OrderConstant;
import com.example.yzwy.lprmag.myConstant.WifiMsgConstant;
import com.example.yzwy.lprmag.util.HanderUtil;
import com.example.yzwy.lprmag.util.InetAddressUtil;
import com.example.yzwy.lprmag.util.LogUtil;
import com.example.yzwy.lprmag.util.NetUtils;
import com.example.yzwy.lprmag.util.Tools;
import com.example.yzwy.lprmag.wifimess.model.SendOrder;
import com.example.yzwy.lprmag.wifimess.util.SocketUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static com.example.yzwy.lprmag.util.Tools.getWifiRouteIPAddress;


public class TerminalDataManageActivity extends AppCompatActivity {

    /**
     *
     * */
    private EditText edt_locIP_tremconn;
    /**
     *
     * */
    private EditText edt_wwwnet_tremconn;
    /**
     *
     * */
    private EditText edt_nettype_tremconn;
    /**
     *
     * */
    private EditText edt_conntern_tremconn;
    /**
     *
     * */
    private EditText edt_termaddress_tremdatamag;
    /**
     *
     * */
    private Button btn_enter_tremdatamag;
    private ImageButton imgbtn_back_tremdatamag;
    private Button btn_f5data_tremdatamag;
    private EditText edt_termpriority_tremdatamag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ly_trem_data_mag);


        //==========================================================================================
        ActivityStackManager.getInstance().addActivity(this);

        //加载View
        initView();

        //设置输入框不可编辑
        setEnabled();

        //设置显示的字符
        setEditText();

        //向终端获取热点信息
        GetTerminalDataInfo();

        //按钮监听事件
        initOnClick();
    }

    /**
     * =============================================================================================
     * 加載View
     */
    private void initView() {
        edt_locIP_tremconn = (EditText) findViewById(R.id.edt_locIP_tremconn);
        edt_wwwnet_tremconn = (EditText) findViewById(R.id.edt_wwwnet_tremconn);
        edt_nettype_tremconn = (EditText) findViewById(R.id.edt_nettype_tremconn);
        edt_conntern_tremconn = (EditText) findViewById(R.id.edt_conntern_tremconn);

        edt_termaddress_tremdatamag = (EditText) findViewById(R.id.edt_termaddress_tremdatamag);
        edt_termpriority_tremdatamag = (EditText) findViewById(R.id.edt_termpriority_tremdatamag);

        btn_enter_tremdatamag = (Button) findViewById(R.id.btn_enter_tremdatamag);
        imgbtn_back_tremdatamag = (ImageButton) findViewById(R.id.imgbtn_back_tremdatamag);
        btn_f5data_tremdatamag = (Button) findViewById(R.id.btn_f5data_tremdatamag);
    }

    /**
     * =============================================================================================
     * 按钮监听事件
     */
    private void initOnClick() {

        /**
         * 确认修改
         * */
        btn_enter_tremdatamag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //数据修改
                UpdateCmdWifiHotConfig();
            }
        });

        /**
         * 返回
         * */
        imgbtn_back_tremdatamag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TerminalDataManageActivity.this.finish();
            }
        });

        /**
         * 刷新
         * */
        btn_f5data_tremdatamag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEditText();
                //向终端获取热点信息
                GetTerminalDataInfo();
                Tools.Toast(TerminalDataManageActivity.this, "刷新成功~");
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
        String termpriority = edt_termpriority_tremdatamag.getText().toString().trim();
        String termaddress = edt_termaddress_tremdatamag.getText().toString().trim();

        //==========================================================================================
        //
        if (termaddress.equals("")) {
            Tools.Toast(TerminalDataManageActivity.this, "终端地址不能为空，请重新输入");
            return false;
        }
        //向终端发送设置终端热点的命令
        SetTerminalDataInfo(termpriority, termaddress);

        return true;
    }

    /**
     * =============================================================================================
     * 向终端发送获取终端热点的命令
     */
    private void GetTerminalDataInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String socketServerMsg = SocketUtil.getInstance().SocketRequest(getWifiRouteIPAddress(TerminalDataManageActivity.this), WifiMsgConstant.PORT_wifi, SendOrder.Get_TermDataInfo());
                    LogUtil.showLog("SocketUtilRs /...", socketServerMsg);
                    HanderUtil.HanderMsgSend(handler, 100, "data", socketServerMsg);
                } catch (final IOException e) {
                    e.printStackTrace();
                    LogUtil.showLog("SocketUtilRs /***", e.toString());
                    HanderUtil.HanderMsgSend(handler, 101, "data", e.toString());
                }
            }
        }).start();
    }

    /**
     * =============================================================================================
     * 向终端发送设置终端热点的命令
     */
    private void SetTerminalDataInfo(final String termpriority, final String termaddress) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String socketServerMsg = SocketUtil.getInstance().SocketRequest(getWifiRouteIPAddress(TerminalDataManageActivity.this), WifiMsgConstant.PORT_wifi, SendOrder.Set_TermDataInfo(termpriority, termaddress));
                    LogUtil.showLog("ConfigSetActivity /...", socketServerMsg);
                    HanderUtil.HanderMsgSend(handler, 100, "data", socketServerMsg);
                } catch (final IOException e) {
                    e.printStackTrace();
                    LogUtil.showLog("ConfigSetActivity /***", e.toString());
                    HanderUtil.HanderMsgSend(handler, 101, "data", e.toString());
                }
            }
        }).start();
    }

    /**
     * =============================================================================================
     * 设置 EditText 不可编辑
     */
    private void setEnabled() {
        edt_locIP_tremconn.setEnabled(false);
        edt_wwwnet_tremconn.setEnabled(false);
        edt_nettype_tremconn.setEnabled(false);
        edt_conntern_tremconn.setEnabled(false);
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


        boolean netConnected = NetUtils.isNetConnected(TerminalDataManageActivity.this);
        if (netConnected) {
            edt_wwwnet_tremconn.setText("Internet网访问");
        } else {
            edt_wwwnet_tremconn.setText("网络未连接");
        }

        //java数组初始化
        String[] networkStateTypeArray = {"没有网络连接", "wifi连接", "2G", "3G", "4G", "手机流量"};
        int networkStateType = NetUtils.getNetworkState(TerminalDataManageActivity.this);
        edt_nettype_tremconn.setText(networkStateTypeArray[networkStateType]);


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

                            //获取终端上的数据
                            case OrderConstant.ORDER_GET_TermDataMag:
                                String termpriority = jsonObject.getString("termpriority");
                                String termaddress = jsonObject.getString("termaddress");
                                edt_termpriority_tremdatamag.setText(termpriority);
                                edt_termaddress_tremdatamag.setText(termaddress);
                                edt_conntern_tremconn.setText("是");
                                break;

                            //设置终端上的数据
                            case OrderConstant.ORDER_SET_Update_TermDataMag:
                                if (errcode.equals("0")) {
                                    Tools.Toast(TerminalDataManageActivity.this, "数据设置成功");
                                } else {
                                    Tools.Toast(TerminalDataManageActivity.this, errmsg);
                                }
                                break;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Tools.Toast(TerminalDataManageActivity.this, "终端数据获取异常");

                    }
                    break;

                case 101:
                    Tools.Toast(TerminalDataManageActivity.this, "终端连接异常，请检查~");
                    LogUtil.showLog("ResSocket >>>", dataMsg);
                    edt_conntern_tremconn.setText("否");
                    break;
            }

        }
    };


}

