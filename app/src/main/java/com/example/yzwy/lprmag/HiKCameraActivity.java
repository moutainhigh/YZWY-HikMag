/**
 * <p>DemoActivity Class</p>
 *
 * @author zhuzhenlei 2014-7-17
 * @version V1.0
 * @modificationHistory
 * @modify by user:
 * @modify by reason:
 */
package com.example.yzwy.lprmag;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.yzwy.lprmag.bean.HiKEventBus;
import com.example.yzwy.lprmag.dialog.PresetDialog;
import com.example.yzwy.lprmag.dialog.LprDialog;
import com.example.yzwy.lprmag.dialog.MessageDialog;
import com.example.yzwy.lprmag.dialog.PriorityDialog;
import com.example.yzwy.lprmag.hik.model.CameraManager;
import com.example.yzwy.lprmag.hik.util.NotNull;
import com.example.yzwy.lprmag.myConstant.HiKConfigDataConstant;
import com.example.yzwy.lprmag.myConstant.HiKEventBusConstant;
import com.example.yzwy.lprmag.myConstant.HiKLineWHRectLintScreen;
import com.example.yzwy.lprmag.util.ConvertUtil;
import com.example.yzwy.lprmag.control.activityStackExtends.util.ActivityStackManager;
import com.example.yzwy.lprmag.util.LogUtil;
import com.example.yzwy.lprmag.util.Tools;
import com.example.yzwy.lprmag.util.SharePreferencesUtil;
import com.hikvision.netsdk.ExceptionCallBack;
import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.NET_DVR_DEVICEINFO_V30;
import com.hikvision.netsdk.NET_DVR_PREVIEWINFO;
import com.hikvision.netsdk.RealPlayCallBack;

import org.MediaPlayer.PlayM4.Player;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * #################################################################################################
 * Copyright: Copyright (c) 2018
 * Created on 2019-04-03
 * Author: 仲超(zhongchao)
 * Version 1.0
 * Describe: 海康摄像头预览和控制面板
 * #################################################################################################
 */
public class HiKCameraActivity extends AppCompatActivity implements Callback, OnTouchListener {

    /**
     * 退出事件的时间
     */
    private long exitTime;

    /**
     * 定义一个SurfaceView用于预览图像
     */
    private SurfaceView m_osurfaceView = null;


    /**
     * 设备信息 模拟通道数bychannum 数字通道数byipchanum
     */
    private NET_DVR_DEVICEINFO_V30 m_oNetDvrDeviceInfoV30 = null;

    /**
     * 登入标记 -1未登入，0已登入
     */
    private int m_iLogID = -1; // return by NET_DVR_Login_v30

    /**
     * 播放标记 -1未播放，0正在播放
     */
    private int m_iPlayID = -1; // return by NET_DVR_RealPlay_V30
    private int m_iPlaybackID = -1; // return by NET_DVR_PlayBackByTime
    private int m_iPort = -1; // play port //播放端口
    private int m_iStartChan = 0; // start channel no //开始频道编号
    private int m_iChanNum = 0; // channel number //通道号
    private final String TAG = "HiKCameraActivity  ";
    private boolean m_bNeedDecode = true;
    private boolean m_bStopPlayback = false;
    private Thread startSinglePreviewThread;
    private boolean isShow = true;

    /**
     * 摄像头控制按钮 上下左右 拉近，放大
     */
    private Button btnUp;
    private Button btnDown;
    private Button btnLeft;
    private Button btnRight;
    private Button btnZoomIn;
    private Button btnZoomOut;


    /**
     * 地磁管理按钮事件
     */
    private Button btn_geomagneticMag_hik;
    /**
     * 车牌识别按钮事件
     */
    private Button btn_carnum_hik;//打开设置预置点的界面

    /**
     * 定义摄像机管理类
     */
    private CameraManager h1;

    /**
     * 海康摄像头IP地址
     */
    private String ADDRESS = "192.168.1.64";//摄像头IP地址
    /**
     * 海康摄像头端口号
     */
    private int PORT = 8000;//摄像头端口号
    /**
     * 海康摄像头登录用户名
     */
    private String USER = "admin";//摄像头用户名
    /**
     * 海康摄像头登录密码
     */
    private String PSD = "admin123";//摄像头密码

    /**
     * 海康摄像头需要识别车牌要裁剪的区域
     */
    private View rectLine;

    /**
     * 海康摄像头登录线程
     */
    private LoginHiKThread loginHiKThread;

    /**
     * 操作层CMD 面板
     */
    private LinearLayout li_magPage_hik;

    /**
     * 进度框
     */
    private TextView tv_Loading;

    /**
     * 速度条
     */
    private SeekBar skb_speed_hik;
    /**
     * 显示速度条
     */
    private TextView tv_speed_hik;
    /**
     * 速度值
     */
    private int speed_hik = 0;
    /**
     * 速度条控制面板
     */
    private RelativeLayout rltv_cmd_hik;
    private SeekBar skb_scwidth_hik;
    private TextView tv_scwidth_hik;
    private SeekBar skb_scheight_hik;
    private TextView tv_scheight_hik;

    /**
     * 识别区域的宽高比列
     */
    private double scwidth_hik = HiKLineWHRectLintScreen.WidthProportion;
    private double scheight_hik = HiKLineWHRectLintScreen.HeightProportion;

    /**
     * 屏幕的宽高
     */
    private int screenWidth = 0;
    private int screenHeight = 0;
    private Button btn_priority_hik;
    //    private ImageView img_loading_hik;
//    private Animation LoadingAnimation;
    private LinearLayout li_loading_hik;
    private LinearLayout li_cmdtop_hik;
    private boolean li_cmdtop_hikBool = false;

    /**
     * 退出按钮
     */
    private ImageButton imgbtn_back_hik;
    private TextView tv_wifiStrength_hik;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        //去掉标题栏
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);


        /**
         * 加载布局
         * */
        setContentView(R.layout.ly_hik);


        //==========================================================================================
        ActivityStackManager.getInstance().addActivity(this);

        //注册 EventBus 事件
        EventBus.getDefault().register(HiKCameraActivity.this);

        //获取屏幕的宽高
        GetscreenWidthHeight();

        ///**
        // * 摄像机错误工具类 暂时未使用
        // * */
        //CrashUtil crashUtil = CrashUtil.getInstance();
        //crashUtil.init(this);

        initHiKConnectConfig();

        //==========================================================================================

        //加载海康SDK
        initHiKSdk();

        //加载海康需要用到的组件
        initHiKActivity();

        //------------------------------------------------------------------------------------------
        // 登录海康  开始20次登录  延时通过线程的方式
        loginHiKThread = new LoginHiKThread();
        //开启线程
        loginHiKThread.start();

        //获取异常回调实例并设置
        initHiKExceptionCallBack();

        //事件监听
        initOnClick();

    }

    //在onResume()方法注册
    @Override
    protected void onResume() {
        //注册广播：
        registerReceiver(rssiReceiver, new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));
        super.onResume();
    }

    //onPause()方法注销
    @Override
    protected void onPause() {
        unregisterReceiver(rssiReceiver);
        System.out.println("实时监测网络注销");
        super.onPause();
    }

    //广播接收信号强度变化的处理：
    public BroadcastReceiver rssiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int s = obtainWifiInfo();
            tv_wifiStrength_hik.setText(String.valueOf(s));
        }
    };

    /**
     * 先来了解下Android如何获取wifi的信息：
     * WifiManager wifi_service = (WifiManager)getSystemService(WIFI_SERVICE);
     * WifiInfo wifiInfo = wifi_service.getConnectionInfo();
     * <p>
     * 其中wifiInfo有以下的方法：
     * wifiinfo.getBSSID()；
     * wifiinfo.getSSID()；
     * wifiinfo.getIpAddress()；获取IP地址。
     * wifiinfo.getMacAddress()；获取MAC地址。
     * wifiinfo.getNetworkId()；获取网络ID。
     * wifiinfo.getLinkSpeed()；获取连接速度，可以让用户获知这一信息。
     * wifiinfo.getRssi()；获取RSSI，RSSI就是接受信号强度指示。
     * 这里得到信号强度就靠wifiinfo.getRssi()这个方法。得到的值是一个0到-100的区间值，是一个int型数据，其中0到-50表示信号最好，-50到-70表示信号偏差，小于-70表示最差，有可能连接不上或者掉线，一般Wifi已断则值为-200。
     * ---------------------
     * 作者：筱丶新
     * 来源：CSDN
     * 原文：https://blog.csdn.net/qq_26981913/article/details/52276732/
     * 版权声明：本文为博主原创文章，转载请附上博文链接！
     */
    //获取wifi信号强度：
    private int obtainWifiInfo() {
        // Wifi的连接速度及信号强度：
        int strength = 0;
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        // WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        WifiInfo info = wifiManager.getConnectionInfo();
        if (info.getBSSID() != null) {
            // 链接信号强度，5为获取的信号强度值在5以内
            strength = WifiManager.calculateSignalLevel(info.getRssi(), 100);
            // 链接速度
            int speed = info.getLinkSpeed();
            // 链接速度单位
            String units = WifiInfo.LINK_SPEED_UNITS;
            // Wifi源名称
            String ssid = info.getSSID();
        }
//        return info.toString();
        return strength;
    }

    /**
     * =============================================================================================
     * 获取屏幕的宽高
     */
    private void GetscreenWidthHeight() {
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;

        //1812===1080
        LogUtil.showLog("HiKCameraActivity --->", screenWidth + "===" + screenHeight);
    }


    /**
     * =============================================================================================
     * 获取异常回调实例并设置
     */
    private void initHiKExceptionCallBack() {
        //------------------------------------------------------------------------------------------
        //获取异常回调实例并设置  get instance of exception callback and set
        ExceptionCallBack oexceptionCbf = getExceptiongCbf();
        if (oexceptionCbf == null) {
            //exceptioncallback对象失败！
            Log.e(TAG, "ExceptionCallBack object is failed!");
        }
        if (!HCNetSDK.getInstance().NET_DVR_SetExceptionCallBack(oexceptionCbf)) {
            //net_dvr_setexceptioncallback失败！
            Log.e(TAG, "NET_DVR_SetExceptionCallBack is failed!");
        }
        //==========================================================================================
    }

    /**
     * =============================================================================================
     * 事件监听
     */
    private void initOnClick() {

        /**
         * 设置预置点
         * */
        btn_geomagneticMag_hik.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //Tools.Toast(HiKCameraActivity.this, "点击了设置预置点页面");
                //Tools.IntentDataBack(HiKCameraActivity.this, PresetDialog.class, "m_iPlayID", m_iPlayID + "");
                //SetPresetOnclick();

                /**
                 * 如果小于零就什么也拍不到
                 * */
                if (scwidth_hik == 0 || scheight_hik == 0 || String.valueOf(scwidth_hik).equals("0") || String.valueOf(scheight_hik).equals("0")) {
                    Tools.Toast(HiKCameraActivity.this, "识别区域的宽高比必须大于0");
                    return;
                }

                Intent in = new Intent(HiKCameraActivity.this, PresetDialog.class);
                in.putExtra("scwidth_hik", String.valueOf(scwidth_hik));
                in.putExtra("scheight_hik", String.valueOf(scheight_hik));
                in.putExtra("m_iPlayID", String.valueOf(m_iPlayID));
                HiKCameraActivity.this.startActivity(in);


            }
        });


        /**
         * 通信开始识别车牌
         * */
        btn_carnum_hik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Tools.Toast(HiKCameraActivity.this, "点击了开始识别车牌");
                //Tools.IntentBack(HiKCameraActivity.this, LprDialog.class);

                /**
                 * 如果小于零就什么也拍不到
                 * */
                if (scwidth_hik == 0 || scheight_hik == 0 || String.valueOf(scwidth_hik).equals("0") || String.valueOf(scheight_hik).equals("0")) {
                    Tools.Toast(HiKCameraActivity.this, "识别区域的宽高比必须大于0");
                    return;
                }

                Intent in = new Intent(HiKCameraActivity.this, LprDialog.class);
                in.putExtra("scwidth_hik", String.valueOf(scwidth_hik));
                in.putExtra("scheight_hik", String.valueOf(scheight_hik));
                HiKCameraActivity.this.startActivity(in);

            }
        });
        /**
         * 设置优先级
         * */
        btn_priority_hik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Tools.Toast(HiKCameraActivity.this, "点击了开始识别车牌");
                Tools.IntentBack(HiKCameraActivity.this, PriorityDialog.class);

//                Intent in = new Intent(HiKCameraActivity.this, LprDialog.class);
//                in.putExtra("scwidth_hik", String.valueOf(scwidth_hik));
//                in.putExtra("scheight_hik", String.valueOf(scheight_hik));
//                HiKCameraActivity.this.startActivity(in);

            }
        });


        /**
         * 设置优先级
         * */
        m_osurfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!li_cmdtop_hikBool) {
                    li_cmdtop_hik.setVisibility(View.VISIBLE);
                    li_cmdtop_hikBool = true;
                    li_cmdtop_hik.bringToFront();
                    li_magPage_hik.setVisibility(View.GONE);

                } else {
                    li_cmdtop_hik.setVisibility(View.GONE);
                    li_cmdtop_hikBool = false;
                    li_magPage_hik.setVisibility(View.VISIBLE);
                }


            }
        });
        /**
         * 设置优先级
         * */
        imgbtn_back_hik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                exit();


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
    private void HanderhiKMsgSend(Handler handler, int what, String Key, String Val) {
        Message messageHiK_111 = new Message();
        messageHiK_111.what = what;
        Bundle bundle = new Bundle();
        bundle.putString(Key, Val);
        messageHiK_111.setData(bundle);
        handler.sendMessage(messageHiK_111);
    }


    /**
     * =============================================================================================
     * 海康登录UI更新和Log打印和m_iLogID设置
     */
    @SuppressLint("HandlerLeak")
    private Handler handlerHiK = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            String HiKMsg = msg.getData().getString("HiKMsg");

            switch (msg.what) {

                case 111:


                    LogUtil.showLog(TAG, HiKMsg);
                    PREVIEWINFO();
                    try {
                        Thread.sleep(1000 * 3);
                        /**
                         * 显示面板
                         * */
                        ShowCmdPage();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;

                case 101:
                    LogUtil.showLog(TAG, HiKMsg);
                    break;

                case 102:
                    LogUtil.showLog(TAG, HiKMsg);
                    break;

                //海康设备登录失败
                case 404:
                    tv_Loading.setVisibility(View.GONE);
                    li_loading_hik.setVisibility(View.GONE);
                    //LoadingAnimation.cancel();
                    LogUtil.showLog(TAG, HiKMsg);
                    try {
                        new MessageDialog(HiKCameraActivity.this)
                                .setTitle("提示")
                                .setMessage("设备登陆失败请重新进入")
                                .setCancel("")
                                .setConfirm("返回")
                                .setAutoDismiss(false)
                                .setCancelable(true)
                                .setListener(new MessageDialog.OnListener() {
                                    @Override
                                    public void onConfirm(Dialog dialog) {
                                        //关闭弹出框
                                        dialog.dismiss();

                                        CloseHiKActivity();
                                    }

                                    @Override
                                    public void onCancel(Dialog dialog) {
                                    }
                                })
                                .show();
                    } catch (Exception e) {

                    }


                    break;

                default:
                    break;

            }

        }


    };

    /**
     * =============================================================================================
     * 登录成功后需要显示的内容
     */
    private void ShowCmdPage() {
        li_magPage_hik.setVisibility(View.VISIBLE);
        rectLine.setVisibility(View.VISIBLE);
        rltv_cmd_hik.setVisibility(View.VISIBLE);
        tv_Loading.setVisibility(View.GONE);

        li_loading_hik.setVisibility(View.GONE);
        //LoadingAnimation.cancel();

        m_osurfaceView.setEnabled(true);

    }

    /**
     * =============================================================================================
     * 开始预览
     */
    private void PREVIEWINFO() {
        //预览
        final NET_DVR_PREVIEWINFO ClientInfo = new NET_DVR_PREVIEWINFO();
        ClientInfo.lChannel = 0;
        ClientInfo.dwStreamType = 0; // substream
        ClientInfo.bBlocked = 1;
        startSinglePreviewThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    SystemClock.sleep(500);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isShow)
                                startSinglePreview();
                        }
                    });
                }
            }
        });
        startSinglePreviewThread.start();
    }

    /**
     * =============================================================================================
     * 初始化海康连接诶信息
     */
    private void initHiKConnectConfig() {

        ADDRESS = SharePreferencesUtil.getStringValue(HiKCameraActivity.this, HiKConfigDataConstant.hkIp_cfgset_str, HiKConfigDataConstant.hkIp_cfgset_str_default);
        PORT = Integer.valueOf(SharePreferencesUtil.getStringValue(HiKCameraActivity.this, HiKConfigDataConstant.hkport_cfgset_str, HiKConfigDataConstant.hkport_cfgset_str_default));
        USER = SharePreferencesUtil.getStringValue(HiKCameraActivity.this, HiKConfigDataConstant.hikusername_cfgset_str, HiKConfigDataConstant.hikusername_cfgset_str_default);
        PSD = SharePreferencesUtil.getStringValue(HiKCameraActivity.this, HiKConfigDataConstant.hikpwd_cfgset_str, HiKConfigDataConstant.hikpwd_cfgset_str_default);

    }


    /**
     * =============================================================================================
     *
     * @param holder
     */
    // @Override
    public void surfaceCreated(SurfaceHolder holder) {
        m_osurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        Log.i(TAG, "surface is created" + m_iPort);
        if (-1 == m_iPort) {
            return;
        }
        Surface surface = holder.getSurface();
        if (surface.isValid()) {
            if (!Player.getInstance().setVideoWindow(m_iPort, 0, holder)) {
                Log.e(TAG, "Player setVideoWindow failed!");
            }
        }
    }

    /**
     * =============================================================================================
     *
     * @param holder
     * @param format
     * @param width
     * @param height
     */
    // @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * =============================================================================================
     *
     * @param holder
     */
    // @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i(TAG, "Player setVideoWindow release!" + m_iPort);
        if (-1 == m_iPort) {
            return;
        }
        if (holder.getSurface().isValid()) {
            if (!Player.getInstance().setVideoWindow(m_iPort, 0, null)) {
                Log.e(TAG, "Player setVideoWindow failed!");
            }
        }
    }

    /**
     * =============================================================================================
     * 注意
     * 1、如果是用户自动按下返回键，或程序调用finish()退出程序，是不会触发onSaveInstanceState()和onRestoreInstanceState()的。
     * 2、每次用户旋转屏幕时，您的Activity将被破坏并重新创建。当屏幕改变方向时，系统会破坏并重新创建前台Activity，
     * 因为屏幕配置已更改，您的Activity可能需要加载替代资源（例如布局）。
     * 即会执行onSaveInstanceState()和onRestoreInstanceState()的。
     * =============================================================================================
     * */

    /**
     * 保存你的Activity状态
     * 当您的Activity开始停止时，系统会调用，onSaveInstanceState()以便您的Activity可以使用一组键值对来保存状态信息。
     * 此方法的默认实现保存有关Activity视图层次结构状态的信息，例如EditText小部件中的文本或ListView的滚动位置。
     * 为了保存Activity的附加状态信息，您必须实现onSaveInstanceState()并向对象添加键值对Bundle
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("m_iPort", m_iPort);
        LogUtil.showLog(TAG, "onSaveInstanceState" + m_iPort);
    }

    /**
     * 恢复您的Activity状态
     * 当您的Activity在之前被破坏后重新创建时，您可以从Bundle系统通过您的Activity中恢复您的保存状态。这两个方法onCreate()和onRestoreInstanceState()回调方法都会收到Bundle包含实例状态信息的相同方法。
     * 因为onCreate()调用该方法是否系统正在创建一个新的Activity实例或重新创建一个以前的实例，所以您必须Bundle在尝试读取之前检查该状态是否为空。如果它为空，那么系统正在创建一个Activity的新实例，而不是恢复之前被销毁的实例。
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        m_iPort = savedInstanceState.getInt("m_iPort");
        LogUtil.showLog(TAG, "onRestoreInstanceState" + m_iPort);
    }

    /**
     * =============================================================================================
     * 加载摄像头海康SDK
     *
     * @return true - success;false - fail
     * @fn initHiKSdk
     * @author zhuzhenlei
     * @brief SDK init
     */
    private boolean initHiKSdk() {
        // init net sdk
        if (!HCNetSDK.getInstance().NET_DVR_Init()) {
            //加载海康SDK失败
            Log.e(TAG, "HCNetSDK init is failed!");

            //设置连接时间与重连时间
            //超时时间，单位毫秒，取值范围[300,75000]，实际最大超时时间因系统的connect超时时间而不同。
            HCNetSDK.getInstance().NET_DVR_SetConnectTime(2000);
            //dwInterval [in] 重连间隔，单位:毫秒 bEnableRecon [in] 是否重连，0-不重连，1-重连，参数默认值为1
            HCNetSDK.getInstance().NET_DVR_SetReconnect(3000, true);

            return false;
        }
        HCNetSDK.getInstance().NET_DVR_SetLogToFile(3, "/mnt/sdcard/sdklog/", true);
        return true;
    }

    /**
     * =============================================================================================
     * GUI（主要布局）初始化
     * // GUI init
     *
     * @return
     */
    private boolean initHiKActivity() {
        findViews();
        //添加
        m_osurfaceView.getHolder().addCallback(this);
        return true;
    }

    /**
     * =============================================================================================
     * get controller instance  获取控制器实例
     */
    private void findViews() {

        this.btnZoomOut = (Button) findViewById(R.id.btn_ZoomOut);
        this.btnZoomIn = (Button) findViewById(R.id.btn_ZoomIn);
        this.btnRight = (Button) findViewById(R.id.btn_Right);
        this.btnLeft = (Button) findViewById(R.id.btn_Left);
        this.btnDown = (Button) findViewById(R.id.btn_Down);
        this.btnUp = (Button) findViewById(R.id.btn_Up);

        this.btn_geomagneticMag_hik = (Button) findViewById(R.id.btn_geomagneticMag_hik);
        this.btn_carnum_hik = (Button) findViewById(R.id.btn_carnum_hik);
        this.btn_priority_hik = (Button) findViewById(R.id.btn_priority_hik);
        this.imgbtn_back_hik = (ImageButton) findViewById(R.id.imgbtn_back_hik);
        this.li_magPage_hik = (LinearLayout) findViewById(R.id.li_magPage_hik);
        this.tv_Loading = (TextView) findViewById(R.id.tv_Loading);


        this.tv_wifiStrength_hik = (TextView) findViewById(R.id.tv_wifiStrength_hik);


        this.skb_speed_hik = (SeekBar) findViewById(R.id.skb_speed_hik);
        this.tv_speed_hik = (TextView) findViewById(R.id.tv_speed_hik);

        this.skb_scwidth_hik = (SeekBar) findViewById(R.id.skb_scwidth_hik);
        this.tv_scwidth_hik = (TextView) findViewById(R.id.tv_scwidth_hik);

        this.skb_scheight_hik = (SeekBar) findViewById(R.id.skb_scheight_hik);
        this.tv_scheight_hik = (TextView) findViewById(R.id.tv_scheight_hik);

        this.m_osurfaceView = (SurfaceView) findViewById(R.id.sf_VideoMonitor);


        this.rltv_cmd_hik = (RelativeLayout) findViewById(R.id.rltv_cmdSpeed_hik);
        this.li_loading_hik = (LinearLayout) findViewById(R.id.li_loading_hik);
        this.li_cmdtop_hik = (LinearLayout) findViewById(R.id.li_cmdtop_hik);
        li_cmdtop_hik.setVisibility(View.GONE);
        m_osurfaceView.setEnabled(false);


        /**
         * 设置中间需要裁剪的车牌宽度的红框
         * */
        rectLine = (View) findViewById(R.id.rectLine);

        /**
         * 设置SeekBar的最大值 并且减1
         * */
        skb_scwidth_hik.setMax(screenWidth - 1);
        skb_scheight_hik.setMax(screenHeight - 1);
        skb_scwidth_hik.setProgress(250);
        skb_scheight_hik.setProgress(120);


        //btn_geomagneticMag_hik.setVisibility(View.GONE);
        //btn_carnum_hik.setVisibility(View.GONE);
        li_magPage_hik.setVisibility(View.GONE);
        rectLine.setVisibility(View.GONE);
        rltv_cmd_hik.setVisibility(View.GONE);


        btnUp.setOnTouchListener(this);
        btnDown.setOnTouchListener(this);
        btnLeft.setOnTouchListener(this);
        btnRight.setOnTouchListener(this);
        btnZoomIn.setOnTouchListener(this);
        btnZoomOut.setOnTouchListener(this);


        //SeekBarChangeListener
        SkbSetOnSeekBarChangeListener();

        initRectLineWH();

        //LoadingDialog loadingDialog = new LoadingDialog(this, "正在登录...", R.mipmap.ic_dialog_loading);
        //loadingDialog.show();

//        /**
//         * 加载图片
//         * */
//        img_loading_hik = (ImageView) findViewById(R.id.img_loading_hik);
//        img_loading_hik.setImageResource(R.drawable.ic_dialog_loading);
//        LoadingAnimation = AnimationUtils.loadAnimation(HiKCameraActivity.this, R.anim.loading);
//        LoadingAnimation.setInterpolator(new LinearInterpolator());
//        img_loading_hik.startAnimation(LoadingAnimation);

    }

    /**
     * =============================================================================================
     * SeekBarChangeListener
     */
    private void SkbSetOnSeekBarChangeListener() {

        /**
         * -----------------------------------------------------------------------------------------
         * 云台速度
         * */
        skb_speed_hik.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 当拖动条的滑块位置发生改变时触发该方法,在这里直接使用参数progress，即当前滑块代表的进度值
                tv_speed_hik.setText("速度:" + Integer.toString(progress + 1));
                speed_hik = progress + 1;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.e("------------", "开始滑动！");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.e("------------", "停止滑动！");
            }
        });

        /**
         * -----------------------------------------------------------------------------------------
         * 识别区域宽度
         * */
        skb_scwidth_hik.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 当拖动条的滑块位置发生改变时触发该方法,在这里直接使用参数progress，即当前滑块代表的进度值
                int progress_1 = progress + 1;
                //scwidth_hik = screenWidth / progress_1;
                scwidth_hik = Tools.divisorScale(screenWidth, progress_1, 100);
                LogUtil.showLog("HiKCameraActivity --->", "scwidth_hik: " + scwidth_hik + " = " + screenWidth + "/" + progress_1);
                tv_scwidth_hik.setText("宽度比:" + Double.toString(scwidth_hik));
                /**
                 * 动态识别区域高度宽度设置比例值和显示RectLine
                 * */
                ScRectLine();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.e("------------", "开始滑动！");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.e("------------", "停止滑动！");
            }
        });


        /**
         * -----------------------------------------------------------------------------------------
         * 识别区域高度
         * */
        skb_scheight_hik.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 当拖动条的滑块位置发生改变时触发该方法,在这里直接使用参数progress，即当前滑块代表的进度值
                int progress_1 = progress + 1;
                //scheight_hik = screenHeight / progress_1;
                scheight_hik = Tools.divisorScale(screenHeight, progress_1, 100);
                tv_scheight_hik.setText("高度比:" + Double.toString(scheight_hik));
                LogUtil.showLog("HiKCameraActivity --->", "scheight_hik: " + scheight_hik + " = " + screenHeight + "/" + progress_1);

                /**
                 * 动态识别区域高度宽度设置比例值和显示RectLine
                 * */
                ScRectLine();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.e("------------", "开始滑动！");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.e("------------", "停止滑动！");
            }
        });
    }

    /**
     * =============================================================================================
     * 动态识别区域高度设置比例值和显示RectLine
     */
    private void ScRectLine() {

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) rectLine.getLayoutParams();
        params.width = (int) (screenWidth / scwidth_hik);
        params.height = (int) (screenHeight / scheight_hik);
        rectLine.setLayoutParams(params);

    }


    /**
     * =============================================================================================
     * 设置中间需要裁剪的车牌宽度的红框
     */
    private void initRectLineWH() {
        int HikSurfaceViewWidth = m_osurfaceView.getWidth();
        int HikSurfaceViewHeight = m_osurfaceView.getHeight();
        //设置固定大小
        /**
         * 注册一个ViewTreeObserver的监听回调，这个监听回调，就是专门监听绘图的，既然是监听绘图，
         * 那么我们自然可以获取测量值了，同时，我们在每次监听前remove前一次的监听，避免重复监听。
         * */
        ViewTreeObserver vto = m_osurfaceView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                m_osurfaceView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int HikSurfaceViewHeight = m_osurfaceView.getHeight();
                int HikSurfaceViewWidth = m_osurfaceView.getWidth();

                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) rectLine.getLayoutParams();
                params.width = (int) (HikSurfaceViewWidth / HiKLineWHRectLintScreen.WidthProportion);
                params.height = (int) (HikSurfaceViewHeight / HiKLineWHRectLintScreen.HeightProportion);
                rectLine.setLayoutParams(params);

                /**
                 * 初始化识别区域显示值
                 * */
                tv_scheight_hik.setText(String.valueOf(HiKLineWHRectLintScreen.WidthProportion));
                tv_scwidth_hik.setText(String.valueOf(HiKLineWHRectLintScreen.HeightProportion));

            }
        });


    }


    /**
     * =============================================================================================
     * 控制摄像头的上向左右和放大缩小
     *
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(final View v, final MotionEvent event) {
        if (!NotNull.isNotNull(h1)) return false;
        Log.d(TAG, "onTouch: ");
        new Thread() {
            @Override
            public void run() {
                switch (v.getId()) {
                    /**
                     * 向上移动
                     * */
                    case R.id.btn_Up:
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            h1.startMove(8, m_iLogID, m_iPlayID, speed_hik);
                        }
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            h1.stopMove(8, m_iLogID, m_iPlayID, speed_hik);
                        }
                        break;
                    /**
                     * 向上移动
                     * */
                    case R.id.btn_Left:
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            h1.startMove(4, m_iLogID, m_iPlayID, speed_hik);
                        }
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            h1.stopMove(4, m_iLogID, m_iPlayID, speed_hik);
                        }
                        break;
                    /**
                     * 向上移动
                     * */
                    case R.id.btn_Right:
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            h1.startMove(6, m_iLogID, m_iPlayID, speed_hik);
                        }
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            h1.stopMove(6, m_iLogID, m_iPlayID, speed_hik);
                        }
                        break;
                    /**
                     * 向上移动
                     * */
                    case R.id.btn_Down:
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            h1.startMove(2, m_iLogID, m_iPlayID, speed_hik);
                        }
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            h1.stopMove(2, m_iLogID, m_iPlayID, speed_hik);
                        }
                        break;
                    /**
                     * 大
                     * */
                    case R.id.btn_ZoomIn:
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            h1.startZoom(1, m_iLogID, m_iPlayID, speed_hik);
                        }
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            h1.stopZoom(1, m_iLogID, m_iPlayID, speed_hik);
                        }
                        break;
                    /**
                     * 小
                     * */
                    case R.id.btn_ZoomOut:
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            h1.startZoom(-1, m_iLogID, m_iPlayID, speed_hik);
                        }
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            h1.stopZoom(-1, m_iLogID, m_iPlayID, speed_hik);
                        }
                        break;
                    default:
                        break;
                }
            }
        }.start();
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * =============================================================================================
     * 双击退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
        }
        return false;
    }

    private void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Tools.Toast(HiKCameraActivity.this, "再按一次退出屏幕");
            exitTime = System.currentTimeMillis();
        } else {

            LogUtil.showLog("HicActivity", "准备关闭");

            //解绑EventBus事件
            EventBus.getDefault().unregister(HiKCameraActivity.this);

            CloseHiKActivity();

        }
    }

    private void CloseHiKActivity() {
        //关闭海康登录线程
        if (loginHiKThread != null) {
            loginHiKThread.interrupt();
        }


        if (startSinglePreviewThread != null) {
            startSinglePreviewThread.interrupt();
        }

        //停止单个预览
        stopSinglePreview();

        //注销当前用户
        // whether we have logout
        if (!HCNetSDK.getInstance().NET_DVR_Logout_V30(m_iLogID)) {
            Log.e(TAG, " NET_DVR_Logout is failed!");
            //return;
        }
        m_iLogID = -1;

        //释放资源 播放资源和SDK资源
        Cleanup();

        finish();
    }


    /**
     * =============================================================================================
     * 开始单个预览
     */
    private void startSinglePreview() {
        if (m_iPlaybackID >= 0) {
            Log.i(TAG, "Please stop palyback first");
            return;
        }
        RealPlayCallBack fRealDataCallBack = getRealPlayerCbf();
        if (fRealDataCallBack == null) {
            Log.e(TAG, "fRealDataCallBack object is failed!");
            return;
        }
        Log.i(TAG, "m_iStartChan:" + m_iStartChan);

        NET_DVR_PREVIEWINFO previewInfo = new NET_DVR_PREVIEWINFO();
        previewInfo.lChannel = m_iStartChan;
        previewInfo.dwStreamType = 0; // substream
        previewInfo.bBlocked = 1;
//
        m_iPlayID = HCNetSDK.getInstance().NET_DVR_RealPlay_V40(m_iLogID,
                previewInfo, fRealDataCallBack);
        if (m_iPlayID < 0) {
            Log.e(TAG, "NET_DVR_RealPlay is failed!Err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
            return;
        }
        isShow = false;
        if (NotNull.isNotNull(startSinglePreviewThread)) {
            startSinglePreviewThread.interrupt();
        }
        h1 = new CameraManager();
        h1.setLoginId(m_iLogID);
    }

    /**
     * =============================================================================================
     * 停止单个预览
     *
     * @return NULL
     * @fn stopSinglePreview
     * @author zhuzhenlei
     * @brief stop preview
     */
    private void stopSinglePreview() {
        if (m_iPlayID < 0) {
            Log.e(TAG, "m_iPlayID < 0");
            return;
        }
        // net sdk stop preview
        if (!HCNetSDK.getInstance().NET_DVR_StopRealPlay(m_iPlayID)) {
            Log.e(TAG, "StopRealPlay is failed!Err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
            return;
        }

        m_iPlayID = -1;
        stopSinglePlayer();
    }

    /**
     * =============================================================================================
     * 停止单个播放机
     */
    private void stopSinglePlayer() {
        Player.getInstance().stopSound();

        /**
         * 播放器停止播放
         * */
        // player stop play
        if (!Player.getInstance().stop(m_iPort)) {
            Log.e(TAG, "stop is failed!");
            return;
        }

        if (!Player.getInstance().closeStream(m_iPort)) {
            Log.e(TAG, "closeStream is failed!");
            return;
        }
        if (!Player.getInstance().freePort(m_iPort)) {
            Log.e(TAG, "freePort is failed!" + m_iPort);
            return;
        }
        m_iPort = -1;
    }

    /**
     * =============================================================================================
     * 获取登陆设备ID
     *
     * @return login ID
     * @fn loginNormalDevice
     * @author zhuzhenlei
     * @brief login on device
     */
    private int loginNormalDevice() {
        // get instance
        m_oNetDvrDeviceInfoV30 = new NET_DVR_DEVICEINFO_V30();
        if (null == m_oNetDvrDeviceInfoV30) {
            Log.e(TAG, "HKNetDvrDeviceInfoV30 new is failed!");
            return -1;
        }
        // call NET_DVR_Login_v30 to login on, port 8000 as default
        int iLogID = HCNetSDK.getInstance().NET_DVR_Login_V30(ADDRESS, PORT, USER, PSD, m_oNetDvrDeviceInfoV30);
        if (iLogID < 0) {
            Log.e(TAG, "NET_DVR_Login is failed!Err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
            return -1;
        }
        if (m_oNetDvrDeviceInfoV30.byChanNum > 0) {
            m_iStartChan = m_oNetDvrDeviceInfoV30.byStartChan;
            m_iChanNum = m_oNetDvrDeviceInfoV30.byChanNum;
        } else if (m_oNetDvrDeviceInfoV30.byIPChanNum > 0) {
            m_iStartChan = m_oNetDvrDeviceInfoV30.byStartDChan;
            m_iChanNum = m_oNetDvrDeviceInfoV30.byIPChanNum
                    + m_oNetDvrDeviceInfoV30.byHighDChanNum * 256;
        }
        Log.i(TAG, "NET_DVR_Login is Successful!");
        return iLogID;
//        return -1;
    }

    /**
     * =============================================================================================
     * 获取登录设备ID
     *
     * @return login ID
     * @fn loginDevice
     * @author zhangqing
     * @brief login on device
     */
    private int loginDevice() {
        int iLogID = -1;
        iLogID = loginNormalDevice();
        return iLogID;
    }

    /**
     * =========================================================================================
     *
     * @return exception instance
     * @fn getExceptiongCbf
     */
    private ExceptionCallBack getExceptiongCbf() {
        ExceptionCallBack oExceptionCbf = new ExceptionCallBack() {
            public void fExceptionCallBack(int iType, int iUserID, int iHandle) {
                System.out.println("recv exception, type:" + iType);
            }
        };
        return oExceptionCbf;
    }

    /**
     * =============================================================================================
     *
     * @return callback instance
     * @fn getRealPlayerCbf
     * @brief get realplay callback instance
     */
    private RealPlayCallBack getRealPlayerCbf() {
        RealPlayCallBack cbf = new RealPlayCallBack() {
            public void fRealDataCallBack(int iRealHandle, int iDataType, byte[] pDataBuffer, int iDataSize) {
                //播放器频道1
                // player channel 1
                HiKCameraActivity.this.processRealData(iDataType, pDataBuffer, iDataSize, Player.STREAM_REALTIME);
            }
        };
        return cbf;
    }

    /**
     * @param iDataType   - data type [in]
     * @param pDataBuffer - data buffer [in]
     * @param iDataSize   - data size [in]
     * @param iStreamMode - stream mode [in]
     * @return NULL
     * @fn processRealData
     * @author zhuzhenlei
     * @brief process real data
     */
    public void processRealData(int iDataType, byte[] pDataBuffer, int iDataSize, int iStreamMode) {
        if (!m_bNeedDecode) {
            Log.i(TAG, ",iDataType:" + iDataType + ",iDataSize:" + iDataSize);
        } else {
            if (HCNetSDK.NET_DVR_SYSHEAD == iDataType) {
                if (m_iPort >= 0) {
                    return;
                }
                m_iPort = Player.getInstance().getPort();
                if (m_iPort == -1) {
                    Log.e(TAG, "getPort is failed with: " + Player.getInstance().getLastError(m_iPort));
                    return;
                }
                Log.i(TAG, "getPort succ with: " + m_iPort);
                if (iDataSize > 0) {
                    if (!Player.getInstance().setStreamOpenMode(m_iPort, iStreamMode)) // set stream mode
                    {
                        Log.e(TAG, "setStreamOpenMode failed");
                        return;
                    }
                    if (!Player.getInstance().openStream(m_iPort, pDataBuffer, iDataSize, 2 * 1024 * 1024)) // open stream
                    {
                        Log.e(TAG, "openStream failed");
                        return;
                    }
                    if (!Player.getInstance().play(m_iPort, m_osurfaceView.getHolder())) {
                        Log.e(TAG, "play failed");
                        return;
                    }
                    if (!Player.getInstance().playSound(m_iPort)) {
                        Log.e(TAG, "playSound failed with error code:" + Player.getInstance().getLastError(m_iPort));
                        return;
                    }
                }
            } else {
                if (!Player.getInstance().inputData(m_iPort, pDataBuffer, iDataSize)) {
                    // Log.e(TAG, "inputData failed with: " +
                    // Player.getInstance().getLastError(m_iPort));
                    for (int i = 0; i < 4000 && m_iPlaybackID >= 0
                            && !m_bStopPlayback; i++) {
                        if (Player.getInstance().inputData(m_iPort, pDataBuffer, iDataSize)) {
                            break;

                        }

                        if (i % 100 == 0) {
                            Log.e(TAG, "inputData failed with: " + Player.getInstance().getLastError(m_iPort) + ", i:" + i);
                        }
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /**
     * =============================================================================================
     * 释放资源 播放资源和SDK资源
     *
     * @return NULL
     * @fn Cleanup
     * @author zhuzhenlei
     * @brief cleanup
     */
    public void Cleanup() {
        // release player resource
        //释放播放机资源
        Player.getInstance().freePort(m_iPort);
        m_iPort = -1;
        // release net SDK resource
        //必须执行的释放IPC_SDK
        HCNetSDK.getInstance().NET_DVR_Cleanup();
    }


    /**
     * =============================================================================================
     * 海康登录线程
     */
    private class LoginHiKThread extends Thread {
        @Override
        public void run() {
            super.run();


            if (m_iLogID < 0) {
                Log.e(TAG, "尝试重新登入");
                HanderhiKMsgSend(handlerHiK, 101, "HiKMsg", "开始登录海康");
                int count = 0;
                //判断进程是否在运行，更安全的结束进程
                while (!this.isInterrupted() && count < 5) {
                    //while (count < 10) {
                    HanderhiKMsgSend(handlerHiK, 102, "HiKMsg", "正在第" + (count + 1) + "次重新登入");
                    m_iLogID = loginDevice();
                    if (m_iLogID < 0) {
                        count++;
                        //SystemClock.sleep(1000 * 2);
                        try {
                            Thread.sleep(1000 * 2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        //发消息开始预览
                        HanderhiKMsgSend(handlerHiK, 111, "HiKMsg", "第" + (count + 1) + "次登入成功m_iLogID=" + m_iLogID);
                        break;
                    }
                }


                //通过延迟发送消息，每隔一秒发送一条消息
                if (m_iLogID < 0) {
                    HanderhiKMsgSend(handlerHiK, 404, "HiKMsg", "尝试登入" + count + "次均失败！");
                } else {
                    //System.out.println("m_iLogID=" + m_iLogID);
                    //发消息开始预览
                    HanderhiKMsgSend(handlerHiK, 111, "HiKMsg", "登陆成功m_iLogID=" + m_iLogID);

                }
            }

        }
    }


    /**
     * =============================================================================================
     * Event 事件的消息处理
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void msgInHiK(HiKEventBus hiKEventBus) {

        switch (hiKEventBus.getEventCode()) {

            case HiKEventBusConstant.Change_Scale:
                this.scwidth_hik = ConvertUtil.convertToDouble(hiKEventBus.getScaleWidth(), 1);
                this.scheight_hik = ConvertUtil.convertToDouble(hiKEventBus.getScaleHeight(), 1);
                ScRectLine();
                break;

        }

    }
}
