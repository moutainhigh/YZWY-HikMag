package com.example.yzwy.lprmag.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yzwy.lprmag.R;
import com.example.yzwy.lprmag.myConstant.OrderConstant;
import com.example.yzwy.lprmag.myConstant.WifiMsgConstant;
import com.example.yzwy.lprmag.util.LogUtil;
import com.example.yzwy.lprmag.util.Tools;
import com.example.yzwy.lprmag.wifimess.model.SendOrder;
import com.example.yzwy.lprmag.wifimess.util.SocketUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static com.example.yzwy.lprmag.util.Tools.getWifiRouteIPAddress;


public class PriorityDialog extends Activity {

    /**
     * 端口号
     */

    private WifiManager wifiManager;
    private TextView status_init_dialogPriority;



    /**
     * 设置优先级
     */
    private Button btn_setpriority_dialogPriority;

    /**
     * 优先级
     */
    private EditText edt_priority_dialogPriority;


    /**
     * 关闭页面
     */
    private Button btn_closePage_dialogPriority;


    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //去掉标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.dialog_priority);

        //设置窗口对齐屏幕宽度
        Window win = this.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();

        //lp.width = this.getWindowManager().getDefaultDisplay().getWidth() - DisplayUtil.dip2px(PriorityDialog.this, 25);
        //lp.height = this.getWindowManager().getDefaultDisplay().getHeight() - DisplayUtil.dip2px(PriorityDialog.this, 25);

        //lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //lp.height = WindowManager.LayoutParams.MATCH_PARENT;

        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

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


        GetPrioritySocketRequest();


    }

    /**
     * =============================================================================================
     * 获取Intent中的值（视频播放ID）
     */
    private void initIntentData() {

//        m_iPlayID = Integer.valueOf(getIntent().getStringExtra("m_iPlayID"));

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

        status_init_dialogPriority.setText("已连接到：" + wifiManager.getConnectionInfo().getSSID() +
                "\nIP:" + Tools.getIp(wifiManager)
                + "\n路由：" + getWifiRouteIPAddress(PriorityDialog.this));

    }


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
                            case OrderConstant.ORDER_Get_PriorityData:

                                String Priority = jsonObject.getString("Priority");

                                if (!Priority.equals("0")) {
                                    edt_priority_dialogPriority.setText(Priority);
                                }
                                break;


                            //返回查询所有预置点的消息
                            case OrderConstant.ORDER_Set_PriorityData:

                                String Priority_errmsg = jsonObject.getString("errmsg");

                                Tools.Toast(PriorityDialog.this,Priority_errmsg);
                                break;

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;

                case 101:
                    Tools.Toast(PriorityDialog.this, "失败Log" + "\n" + dataMsg);
                    LogUtil.showLog("ResSocket >>>", dataMsg);
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

        status_init_dialogPriority = (TextView) findViewById(R.id.status_init_dialogPriority);

        //设置优先级
        btn_setpriority_dialogPriority = (Button) findViewById(R.id.btn_setpriority_dialogPriority);

        edt_priority_dialogPriority = (EditText) findViewById(R.id.edt_priority_dialogPriority);
        btn_closePage_dialogPriority = (Button) findViewById(R.id.btn_closePage_dialogPriority);
    }

    /**
     * =============================================================================================
     * 加载RecyclerView适配器
     */
    public void initAdapter() {

//        /**
//         * -----------------------------------------------------------------------------------------
//         * 获取预置点列表
//         * */
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recv_item_setPreset);
//        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(layoutManager);
//        adapter = new PresetAdapter(presetBeansList, this.m_iPlayID, handler);
//        recyclerView.setAdapter(adapter);

    }

    /**
     * =============================================================================================
     * 加载事件监听器
     */
    public void initClick() {

        btn_setpriority_dialogPriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String edtPriorityStr = edt_priority_dialogPriority.getText().toString();
                if (edtPriorityStr.equals("")) {
                    Tools.Toast(PriorityDialog.this, "优先级不能为空");
                    return;
                }
//                else if (edtPriorityStr.length() != 8) {
//                    Tools.Toast(PresetDialog.this, "优先级必须输入8位长度");
//                }


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String socketServerMsg = SocketUtil.getInstance().SocketRequest(getWifiRouteIPAddress(PriorityDialog.this), WifiMsgConstant.PORT_wifi, SendOrder.Set_PriorityData(edtPriorityStr));
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


        btn_closePage_dialogPriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });

    }

    /**
     * =============================================================================================
     * 请求获取终端优先级信息
     */
    private void GetPrioritySocketRequest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String socketServerMsg = SocketUtil.getInstance().SocketRequest(getWifiRouteIPAddress(PriorityDialog.this), WifiMsgConstant.PORT_wifi, SendOrder.Get_PriorityData());
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
