/**
 * <p>DemoActivity Class</p>
 *
 * @author zhuzhenlei 2014-7-17
 * @version V1.0
 * @modificationHistory
 * @modify by user:
 * @modify by reason:
 */
package com.yzwy.lprmag;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hikvision.netsdk.ExceptionCallBack;
import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.NET_DVR_DEVICEINFO_V30;
import com.hikvision.netsdk.NET_DVR_PREVIEWINFO;
import com.hikvision.netsdk.PTZCommand;
import com.hikvision.netsdk.RealPlayCallBack;
import com.yzwy.lprmag.adapter.PresetAdapter;
import com.yzwy.lprmag.bean.PresetBean;
import com.yzwy.lprmag.dialog.DialogPerset;
import com.yzwy.lprmag.dialog.LprDialog;
import com.yzwy.lprmag.hik.model.CameraManager;
import com.yzwy.lprmag.hik.util.CrashUtil;
import com.yzwy.lprmag.hik.util.NotNull;
import com.yzwy.lprmag.util.LogUtil;
import com.yzwy.lprmag.util.Tools;
import com.yzwy.lprmag.util.TsSharePreferences;
import com.yzwy.lprmag.wifimess.model.SendOrder;
import com.yzwy.lprmag.wifimess.thread.ConnectThread;
import com.yzwy.lprmag.wifimess.thread.ListenerThread;

import org.MediaPlayer.PlayM4.Player;
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

/**
 * <pre>
 *  ClassName  DemoActivity Class
 * </pre>
 *
 * @author zhuzhenlei
 * @version V1.0
 */
public class HiKCameraActivity extends Activity implements Callback, OnTouchListener {

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
    private final String TAG = "CameraActivity";
    private boolean m_bNeedDecode = true;
    private boolean m_bStopPlayback = false;
    private Thread thread;
    private boolean isShow = true;
    private Button btnUp;
    private Button btnDown;
    private Button btnLeft;
    private Button btnRight;
    private Button btnZoomIn;
    private Button btnZoomOut;


    private Button btn_setConfig_hik;


    private Button btn_setPreset_hik;//打开设置预置点的界面
    private Button btn_carnum_hik;//打开设置预置点的界面

    /**
     * 定义摄像机管理类
     */
    private CameraManager h1;

    /**
     *
     * */
//    private AppData app;

    public final String ADDRESS = "192.168.1.64";//摄像头IP地址
    public final int PORT = 8000;//摄像头端口号
    public final String USER = "admin";//摄像头用户名
    public final String PSD = "admin123";//摄像头密码


//    public static final int DEVICE_CONNECTING = 1;//有设备正在连接热点
//    public static final int DEVICE_CONNECTED = 2;//有设备连上热点
//    public static final int SEND_MSG_SUCCSEE = 3;//发送消息成功
//    public static final int SEND_MSG_ERROR = 4;//发送消息失败
//    public static final int GET_MSG = 6;//获取新消息
//
//    private TextView text_state;
//    /**
//     * 连接线程
//     */
//    private ConnectThread connectThread;
//
//
//    /**
//     * 监听线程
//     */
//    private ListenerThread listenerThread;
//
//    /**
//     * 热点名称
//     */
//    private static final String WIFI_HOTSPOT_SSID = "TEST";
//    /**
//     * 端口号
//     */
//    private static final int PORT_wifi = 54321;
//    private WifiManager wifiManager;
//    private TextView status_init;
//    private PresetAdapter adapter;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        app = (AppData) getApplication();

        /**
         * 加载布局
         * */
        setContentView(R.layout.ly_hik);

        /**
         * 摄像机错误工具类
         * */
        CrashUtil crashUtil = CrashUtil.getInstance();
        crashUtil.init(this);

        /**
         * 加载海康SDK 加载失败退出活动页面
         * */
        if (!initeSdk()) {
            this.finish();
            return;
        }

        if (!initeActivity()) {
            this.finish();
            return;
        }

        /**
         *获取登录设备ID
         * */
        // login on the device
        m_iLogID = loginDevice();
        /**
         * 提示是否登录设备成功
         * */
        if (m_iLogID < 0) {
            Log.e(TAG, "This device logins failed!");
            return;
        } else {
            //打印设备登录ID
            System.out.println("m_iLogID=" + m_iLogID);
        }


        /**
         * 获取异常回调实例并设置
         * */
        // get instance of exception callback and set
        ExceptionCallBack oexceptionCbf = getExceptiongCbf();
        if (oexceptionCbf == null) {
            Log.e(TAG, "ExceptionCallBack object is failed!");
            return;
        }
        if (!HCNetSDK.getInstance().NET_DVR_SetExceptionCallBack(
                oexceptionCbf)) {
            Log.e(TAG, "NET_DVR_SetExceptionCallBack is failed!");
            return;
        }

        //预览
        final NET_DVR_PREVIEWINFO ClientInfo = new NET_DVR_PREVIEWINFO();
        ClientInfo.lChannel = 0;
        ClientInfo.dwStreamType = 0; // substream
        ClientInfo.bBlocked = 1;

        //设置默认点
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {

                    SystemClock.sleep(1000);//睡眠一秒

                    /**
                     * 更新UI
                     * */
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isShow)
                                //开始单个预览
                                startSinglePreview();
                        }
                    });
                }
            }
        });
        thread.start();


        /**
         * 设置预置点
         * */
        btn_setPreset_hik.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Tools.Toast(HiKCameraActivity.this, "点击了设置预置点页面");
                Tools.IntentDataBack(HiKCameraActivity.this, DialogPerset.class, "m_iPlayID", m_iPlayID + "");
                //SetPresetOnclick();
            }
        });


        /**
         * 配置信息设置
         * */
        btn_setConfig_hik.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Tools.Toast(HiKCameraActivity.this, "点击了配置页面");
                Tools.IntentBack(HiKCameraActivity.this, ConfigSetActivity.class);
            }
        });


        /**
         * 通信开始识别车牌
         * */
        btn_carnum_hik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Tools.Toast(HiKCameraActivity.this, "点击了开始识别车牌");
                Tools.IntentBack(HiKCameraActivity.this, LprDialog.class);

            }
        });



    }
//
//    /**
//     *
//     */
//    private void initViewWifi() {
//        findViewById(R.id.send).setOnClickListener(this);
////        findViewById(R.id.connect).setOnClickListener(this);
////        findViewById(R.id.fileButton).setOnClickListener(this);
//        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//
//        //检查Wifi状态
//        if (!wifiManager.isWifiEnabled())
//            wifiManager.setWifiEnabled(true);
//        text_state = (TextView) findViewById(R.id.status_info);
//        status_init = (TextView) findViewById(R.id.status_init);
//
//
//        status_init.setText("已连接到：" + wifiManager.getConnectionInfo().getSSID() +
//                "\nIP:" + getIp()
//                + "\n路由：" + getWifiRouteIPAddress(HiKCameraActivity.this));
//
//        initBroadcastReceiver();
//        //开启连接线程
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Socket socket = new Socket(getWifiRouteIPAddress(HiKCameraActivity.this), PORT_wifi);
//                    connectThread = new ConnectThread(socket, handler);
//                    connectThread.start();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            text_state.setText("通信连接失败");
//                        }
//                    });
//
//                }
//            }
//        }).start();
//
//        listenerThread = new ListenerThread(PORT_wifi, handler);
//        listenerThread.start();
//    }


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
            if (!Player.getInstance()
                    .setVideoWindow(m_iPort, 0, holder)) {
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
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
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
     * 2、每次用户旋转屏幕时，您的Activity将被破坏并重新创建。当屏幕改变方向时，系统会破坏并重新创建前台Activity，因为屏幕配置已更改，您的Activity可能需要加载替代资源（例如布局）。即会执行onSaveInstanceState()和onRestoreInstanceState()的。
     * =============================================================================================
     * */

    /**
     * 保存你的Activity状态
     * 当您的Activity开始停止时，系统会调用，onSaveInstanceState()以便您的Activity可以使用一组键值对来保存状态信息。此方法的默认实现保存有关Activity视图层次结构状态的信息，例如EditText小部件中的文本或ListView的滚动位置。
     * 为了保存Activity的附加状态信息，您必须实现onSaveInstanceState()并向对象添加键值对Bundle
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("m_iPort", m_iPort);
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
    }

    /**
     * 恢复您的Activity状态
     * 当您的Activity在之前被破坏后重新创建时，您可以从Bundle系统通过您的Activity中恢复您的保存状态。这两个方法onCreate()和onRestoreInstanceState()回调方法都会收到Bundle包含实例状态信息的相同方法。
     * 因为onCreate()调用该方法是否系统正在创建一个新的Activity实例或重新创建一个以前的实例，所以您必须Bundle在尝试读取之前检查该状态是否为空。如果它为空，那么系统正在创建一个Activity的新实例，而不是恢复之前被销毁的实例。
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        m_iPort = savedInstanceState.getInt("m_iPort");
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * =============================================================================================
     * 加载摄像头海康SDK
     *
     * @return true - success;false - fail
     * @fn initeSdk
     * @author zhuzhenlei
     * @brief SDK init
     */
    private boolean initeSdk() {
        // init net sdk
        if (!HCNetSDK.getInstance().NET_DVR_Init()) {
            //加载海康SDK失败
            Log.e(TAG, "HCNetSDK init is failed!");
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
    private boolean initeActivity() {
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

        this.btn_setPreset_hik = (Button) findViewById(R.id.btn_setPreset_hik);
        this.btn_setConfig_hik = (Button) findViewById(R.id.btn_setConfig_hik);
        this.btn_carnum_hik = (Button) findViewById(R.id.btn_carnum_hik);

        btnUp.setOnTouchListener(this);
        btnDown.setOnTouchListener(this);
        btnLeft.setOnTouchListener(this);
        btnRight.setOnTouchListener(this);
        btnZoomIn.setOnTouchListener(this);
        btnZoomOut.setOnTouchListener(this);
        this.m_osurfaceView = (SurfaceView) findViewById(R.id.sf_VideoMonitor);

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
                            h1.startMove(8, m_iLogID);
                        }
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            h1.stopMove(8, m_iLogID);
                        }
                        break;
                    /**
                     * 向上移动
                     * */
                    case R.id.btn_Left:
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            h1.startMove(4, m_iLogID);
                        }
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            h1.stopMove(4, m_iLogID);
                        }
                        break;
                    /**
                     * 向上移动
                     * */
                    case R.id.btn_Right:
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            h1.startMove(6, m_iLogID);
                        }
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            h1.stopMove(6, m_iLogID);
                        }
                        break;
                    /**
                     * 向上移动
                     * */
                    case R.id.btn_Down:
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            h1.startMove(2, m_iLogID);
                        }
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            h1.stopMove(2, m_iLogID);
                        }
                        break;
                    /**
                     * 向上移动
                     * */
                    case R.id.btn_ZoomIn:
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            h1.startZoom(1, m_iLogID);
                        }
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            h1.stopZoom(1, m_iLogID);
                        }
                        break;
                    /**
                     * 向上移动
                     * */
                    case R.id.btn_ZoomOut:
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            h1.startZoom(-1, m_iLogID);
                        }
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            h1.stopZoom(-1, m_iLogID);
                        }
                        break;
                    default:
                        break;
                }
            }
        }.start();
        return false;
    }


    private AlertDialog getDialongView(View view) {
        final AlertDialog.Builder builder6 = new AlertDialog.Builder(HiKCameraActivity.this);
        builder6.setView(view);
        builder6.create();
        AlertDialog dialog = builder6.show();
        //设置背景半透明
        dialog.getWindow().setBackgroundDrawableResource(R.color.translucent);
        Window window = dialog.getWindow();
        //全屏设置
        WindowManager.LayoutParams lp = window.getAttributes();
        //lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;//弹出框居中显示
        window.setAttributes(lp);


        return dialog;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        LogUtil.showLog("HicActivity", "准备关闭 DES");

        //释放资源 播放资源和SDK资源
        Cleanup();
        m_iLogID = -1;
        //注销当前用户
        // whether we have logout
        if (!HCNetSDK.getInstance().NET_DVR_Logout_V30(m_iLogID)) {
            Log.e(TAG, " NET_DVR_Logout is failed!");
            return;
        }
        //停止单个预览
        stopSinglePreview();
        System.exit(0);//直接结束程序



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

            //停止单个预览
            stopSinglePreview();

            //注销当前用户
            // whether we have logout
            if (!HCNetSDK.getInstance().NET_DVR_Logout_V30(m_iLogID)) {
                Log.e(TAG, " NET_DVR_Logout is failed!");
                return;
            }
            m_iLogID = -1;

            //释放资源 播放资源和SDK资源
            Cleanup();

            System.exit(0);//直接结束程序

        }
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
            Log.e(TAG, "NET_DVR_RealPlay is failed!Err:"
                    + HCNetSDK.getInstance().NET_DVR_GetLastError());
            return;
        }
        isShow = false;
        if (NotNull.isNotNull(thread)) {
            thread.interrupt();
        }
        h1 = new CameraManager();
        h1.setLoginId(m_iLogID);
        Intent intent = getIntent();
        if (NotNull.isNotNull(intent) && intent.getIntExtra("INDEX", -1) != -1) {

//            int point = app.preferences.getInt("POINT", 0);


            int point = TsSharePreferences.getIntValue(this, "POINT", 0);

            boolean b = HCNetSDK.getInstance().NET_DVR_PTZPreset(m_iPlayID, PTZCommand.GOTO_PRESET, point);
        }
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
            Log.e(TAG, "StopRealPlay is failed!Err:"
                    + HCNetSDK.getInstance().NET_DVR_GetLastError());
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
        int iLogID = HCNetSDK.getInstance().NET_DVR_Login_V30(ADDRESS, PORT,
                USER, PSD, m_oNetDvrDeviceInfoV30);
        if (iLogID < 0) {
            Log.e(TAG, "NET_DVR_Login is failed!Err:"
                    + HCNetSDK.getInstance().NET_DVR_GetLastError());
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
            public void fRealDataCallBack(int iRealHandle, int iDataType,
                                          byte[] pDataBuffer, int iDataSize) {
                //播放器频道1
                // player channel 1
                HiKCameraActivity.this.processRealData(iDataType, pDataBuffer,
                        iDataSize, Player.STREAM_REALTIME);
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
    public void processRealData(int iDataType,
                                byte[] pDataBuffer, int iDataSize, int iStreamMode) {
        if (!m_bNeedDecode) {
            // Log.i(TAG, "iPlayViewNo:" + iPlayViewNo + ",iDataType:" +
            // iDataType + ",iDataSize:" + iDataSize);
        } else {
            if (HCNetSDK.NET_DVR_SYSHEAD == iDataType) {
                if (m_iPort >= 0) {
                    return;
                }
                m_iPort = Player.getInstance().getPort();
                if (m_iPort == -1) {
                    Log.e(TAG, "getPort is failed with: "
                            + Player.getInstance().getLastError(m_iPort));
                    return;
                }
                Log.i(TAG, "getPort succ with: " + m_iPort);
                if (iDataSize > 0) {
                    if (!Player.getInstance().setStreamOpenMode(m_iPort,
                            iStreamMode)) // set stream mode
                    {
                        Log.e(TAG, "setStreamOpenMode failed");
                        return;
                    }
                    if (!Player.getInstance().openStream(m_iPort, pDataBuffer,
                            iDataSize, 2 * 1024 * 1024)) // open stream
                    {
                        Log.e(TAG, "openStream failed");
                        return;
                    }
                    if (!Player.getInstance().play(m_iPort,
                            m_osurfaceView.getHolder())) {
                        Log.e(TAG, "play failed");
                        return;
                    }
                    if (!Player.getInstance().playSound(m_iPort)) {
                        Log.e(TAG, "playSound failed with error code:"
                                + Player.getInstance().getLastError(m_iPort));
                        return;
                    }
                }
            } else {
                if (!Player.getInstance().inputData(m_iPort, pDataBuffer,
                        iDataSize)) {
                    // Log.e(TAG, "inputData failed with: " +
                    // Player.getInstance().getLastError(m_iPort));
                    for (int i = 0; i < 4000 && m_iPlaybackID >= 0
                            && !m_bStopPlayback; i++) {
                        if (Player.getInstance().inputData(m_iPort,
                                pDataBuffer, iDataSize)) {
                            break;

                        }

                        if (i % 100 == 0) {
                            Log.e(TAG, "inputData failed with: "
                                    + Player.getInstance()
                                    .getLastError(m_iPort) + ", i:" + i);
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

//    /**
//     * 预置点列表
//     * 定义Bean类型的数组
//     */
//    private List<PresetBean> presetBeansList = new ArrayList<PresetBean>();

//    private void initFruits() {
//        for (int i = 1; i < 255; i++) {
//            PresetBean itemPresetBean = new PresetBean(i + "", "预置点" + i);
//            presetBeansList.add(itemPresetBean);
//        }
//    }

//    /**
//     * =============================================================================================
//     * 设置预置点监听事件
//     */
//    public void SetPresetOnclick() {
//
//        //向终端发送获取所有预置点的命令
//        //connectThread.sendData(SendOrder.getPersetData());
//
//    }

//    public void initPersetDiloag() {
//        //加载预置点设置页面
//        View linearLayout = getLayoutInflater().inflate(R.layout.ly_set_preset, null);
//
//        //实例化设置预置点
//        Button btnSetPreset = (Button) linearLayout.findViewById(R.id.btn_setPreset_setPreset);
//        //实例化清除预置点
//        Button btnClearPreset = (Button) linearLayout.findViewById(R.id.btn_clearPreset_setPreset);
//        //实例化转到预置点
//        Button btnGoPreset = (Button) linearLayout.findViewById(R.id.btn_goTo_setPreset);
//        //设置获取预置点的序号
//        final EditText edtPreset = (EditText) linearLayout.findViewById(R.id.edt_putPreset_setPreset);
//        final EditText edt_putGeomagnetismAddressNumber_setPreset = (EditText) linearLayout.findViewById(R.id.edt_putGeomagnetismAddressNumber_setPreset);
//        final AlertDialog dialog = getDialongView(linearLayout);
//
//
//        /**
//         *
//         * =========================================================================================
//         * 获取预置点列表
//         *
//         * */
//        RecyclerView recyclerView = (RecyclerView) linearLayout.findViewById(R.id.recv_item_setPreset);
//        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(layoutManager);
//        adapter = new PresetAdapter(presetBeansList);
//        recyclerView.setAdapter(adapter);
//
//
//        btnSetPreset.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //获取输入框的预置点序号 转化为INT类型
//                Integer integer = Integer.valueOf(edtPreset.getText().toString());
//                String GeomagnetismAddressNumber = edt_putGeomagnetismAddressNumber_setPreset.getText().toString();
//
//                //判断预置点的范围
//                if (integer > 255 || integer < 0) {
//                    Toast.makeText(HiKCameraActivity.this, "请设置预置点1-255之间", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                //判断预置点的范围
//                if (GeomagnetismAddressNumber == null || GeomagnetismAddressNumber.equals("")) {
//                    Toast.makeText(HiKCameraActivity.this, "请输入地磁预置点", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                //发送数据
//                connectThread.sendData(SendOrder.setPersetData(GeomagnetismAddressNumber, integer.toString()));
//
//                //调用SDK设置预置点 返回结果为false设置失败 true设置成功
////                boolean b = HCNetSDK.getInstance().NET_DVR_PTZPreset(m_iPlayID, SET_PRESET, integer);
////                if (b) {
////                    Toast.makeText(HiKCameraActivity.this, "预置点设置成功", Toast.LENGTH_SHORT).show();
////
////                    TsSharePreferences.putIntValue(HiKCameraActivity.this, "POINT", integer);
////
//////                    app.editor.putInt("POINT", integer).commit();
////
////
////                } else {
////                    Toast.makeText(HiKCameraActivity.this, "预置点设置失败", Toast.LENGTH_SHORT).show();
////                }
//
//
//                //打印预置点返回信息
//                //Log.d(TAG, "设置预置点返回信息 : " + b);
//            }
//        });//设置预置点
//
//        //清除预置点，是清除的某一个预置点
//        btnClearPreset.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                /*
//                 *
//                 * */
//                Integer integer = Integer.valueOf(edtPreset.getText().toString());
//
//                if (integer > 255 || integer < 0) {
//                    Toast.makeText(HiKCameraActivity.this, "请设置1-255之间", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                connectThread.sendData(SendOrder.deletePersetData(integer.toString()));
//
//
////                boolean b = HCNetSDK.getInstance().NET_DVR_PTZPreset(m_iPlayID, CLE_PRESET, integer);
////
////                if (b) {
////
////
////                    //app.editor.remove("POINT").commit();
////
////                    TsSharePreferences.putIntValue(HiKCameraActivity.this, "POINT", integer);
////
////
////                    Toast.makeText(HiKCameraActivity.this, "清除成功", Toast.LENGTH_SHORT).show();
////                } else {
////                    Toast.makeText(HiKCameraActivity.this, "清除失败", Toast.LENGTH_SHORT).show();
////                }
////                //Log.d(TAG, "onClick: " + b);
////                Log.d(TAG, "清除预置点返回信息 : " + b);
//                dialog.dismiss();
//            }
//        });
//        //转到预置点
//        btnGoPreset.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Integer integer = Integer.valueOf(edtPreset.getText().toString());
//                if (integer > 255 || integer < 0) {
//
//                    Toast.makeText(HiKCameraActivity.this, "请设置0-255之间", Toast.LENGTH_SHORT).show();
//
//                    return;
//                }
//
//                //int point = app.preferences.getInt("POINT", 0);
//                //if (point == 0) {
//                //    Toast.makeText(app, "请先设置预设点", Toast.LENGTH_SHORT).show();
//                //    return;
//                //}
//                //boolean b = HCNetSDK.getInstance().NET_DVR_PTZPreset(m_iPlayID, PTZCommand.GOTO_PRESET, point);
//
//
//                if (integer == 0) {
//                    Toast.makeText(HiKCameraActivity.this, "请先设置预设点", Toast.LENGTH_SHORT).show();
//
//                    //Toast.makeText(app, "请先设置预设点", Toast.LENGTH_SHORT).show();
//
//                    return;
//                }
//
//                boolean b = HCNetSDK.getInstance().NET_DVR_PTZPreset(m_iPlayID, PTZCommand.GOTO_PRESET, integer);
//
//                Log.d(TAG, "转到预置点返回信息 : " + b);
//
//                //Log.d(TAG, "onClick: " + b);
//            }
//        });
//    }
//
//
//    /**
//     *
//     *
//     *
//     *
//     *
//     *
//     *
//     * */
//
//    /**
//     * 获取已连接的热点路由
//     *
//     * @return
//     */
//    private String getIp() {
//        //检查Wifi状态
//        if (!wifiManager.isWifiEnabled())
//            wifiManager.setWifiEnabled(true);
//        WifiInfo wi = wifiManager.getConnectionInfo();
//        //获取32位整型IP地址
//        int ipAdd = wi.getIpAddress();
//        //把整型地址转换成“*.*.*.*”地址
//        String ip = intToIp(ipAdd);
//        return ip;
//    }
//
//    /**
//     * 获取路由
//     *
//     * @return
//     */
//
//    private String getRouterIp() {
//        //检查Wifi状态
//        if (!wifiManager.isWifiEnabled())
//            wifiManager.setWifiEnabled(true);
//        WifiInfo wi = wifiManager.getConnectionInfo();
//        //获取32位整型IP地址
//        int ipAdd = wi.getIpAddress();
//        //把整型地址转换成“*.*.*.*”地址
//        String ip = intToRouterIp(ipAdd);
//        return ip;
//    }
//
//    private String intToIp(int i) {
//        return (i & 0xFF) + "." +
//                ((i >> 8) & 0xFF) + "." +
//                ((i >> 16) & 0xFF) + "." +
//                (i >> 24 & 0xFF);
//    }
//
//    private String intToRouterIp(int i) {
//        return (i & 0xFF) + "." +
//                ((i >> 8) & 0xFF) + "." +
//                ((i >> 16) & 0xFF) + "." +
//                1;
//    }
//
//    /**
//     * wifi获取 已连接网络路由  路由ip地址---方法同上
//     *
//     * @param context
//     * @return
//     */
//    private static String getWifiRouteIPAddress(Context context) {
//        WifiManager wifi_service = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//        DhcpInfo dhcpInfo = wifi_service.getDhcpInfo();
//        //        WifiInfo wifiinfo = wifi_service.getConnectionInfo();
//        //        System.out.println("Wifi info----->" + wifiinfo.getIpAddress());
//        //        System.out.println("DHCP info gateway----->" + Formatter.formatIpAddress(dhcpInfo.gateway));
//        //        System.out.println("DHCP info netmask----->" + Formatter.formatIpAddress(dhcpInfo.netmask));
//        //DhcpInfo中的ipAddress是一个int型的变量，通过Formatter将其转化为字符串IP地址
//        String routeIp = Formatter.formatIpAddress(dhcpInfo.gateway);
//        Log.i("route ip", "wifi route ip：" + routeIp);
//
//        return routeIp;
//    }
//
//    int CHOOSE_FILE_RESULT_CODE = 1001;
//
//    int FILE_CODE = 1002;
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//
////            case R.id.fileButton:
////                /**
////                 * 相册
////                 */
////                //                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
////                //                intent.setType("image/*");
////                //                startActivityForResult(intent, CHOOSE_FILE_RESULT_CODE);
////                /**文件库*/
////                // This always works
////                Intent i = new Intent(MainActivity.this, FilePickerActivity.class);
////                // This works if you defined the intent filter
////                // Intent i = new Intent(Intent.ACTION_GET_CONTENT);
////
////                // Set these depending on your use case. These are the defaults.
////                i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
////                i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
////                i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);
////
////                // Configure initial directory by specifying a String.
////                // You could specify a String like "/storage/emulated/0/", but that can
////                // dangerous. Always use Android's API calls to get paths to the SD-card or
////                // internal memory.
////                i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());
////
////                startActivityForResult(i, FILE_CODE);
////
////                break;
//            case R.id.send:
//                if (connectThread != null) {
//                    connectThread.sendData("这是来自Wifi-client热点的消息");
//                } else {
//                    Log.i("AAA", "connectThread == null");
//                }
//                break;
//
//
////            case R.id.connect:
////                status_init.setText("已连接到：" + wifiManager.getConnectionInfo().getSSID() +
////                        "\nIP:" + getIp()
////                        + "\n路由：" + getWifiRouteIPAddress(MainActivity.this));
////
////
////                //                //        initBroadcastReceiver();
////                //                //        开启连接线程
////                //                new Thread(new Runnable() {
////                //                    @Override
////                //                    public void run() {
////                //                        try {
////                //                            Socket socket = new Socket(getRouterIp(), PORT);
////                //                            connectThread = new ConnectThread(socket, handler);
////                //                            connectThread.start();
////                //                        } catch (IOException e) {
////                //                            e.printStackTrace();
////                //                            runOnUiThread(new Runnable() {
////                //                                @Override
////                //                                public void run() {
////                //                                    text_state.setText("通信连接失败");
////                //                                }
////                //                            });
////                //
////                //                        }
////                //                    }
////                //                }).start();
////                //                listenerThread = new ListenerThread(PORT, handler);
////                //                listenerThread.start();
////                break;
//        }
//    }
//
////    @Override
////    public void onActivityResult(int requestCode, int resultCode, Intent data) {
////
////        //        if (resultCode == RESULT_OK) {
////        //            Uri uri = data.getData();
////        //            //            Intent serviceIntent = new Intent(this, FileTransferService.class);
////        //            //            serviceIntent.setAction(FileTransferService.ACTION_SEND_FILE);
////        //            //            serviceIntent.putExtra(FileTransferService.EXTRAS_FILE_PATH, uri.toString());
////        //            //            serviceIntent.putExtra(FileTransferService.EXTRAS_GROUP_OWNER_ADDRESS,
////        //            //                    getWifiRouteIPAddress(MainActivity.this));
////        //            //            serviceIntent.putExtra(FileTransferService.EXTRAS_GROUP_OWNER_PORT, PORT);
////        //            //            startService(serviceIntent);
////        //
////        //            if (connectThread != null) {
////        //                connectThread.sendData(MainActivity.this, uri);
////        //            }
////        //        }
////
////        if (requestCode == FILE_CODE && resultCode == Activity.RESULT_OK) {
////            // Use the provided utility method to parse the result
////            List<Uri> files = Utils.getSelectedFilesFromResult(data);
////            for (Uri uri : files) {
////                File file = Utils.getFileForUri(uri);
////                // Do something with the result...
////
////                if (connectThread != null) {
////                    connectThread.sendData(file);
////                }
////            }
////        }
////
////
////    }
//    //文件传输
//    //    https://blog.csdn.net/yuankundong/article/details/51489823
//
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
//
//    @SuppressLint("HandlerLeak")
//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case DEVICE_CONNECTING:
//                    connectThread = new ConnectThread(listenerThread.getSocket(), handler);
//                    connectThread.start();
//                    break;
//                case DEVICE_CONNECTED:
//                    text_state.setText("设备连接成功");
//                    break;
//                case SEND_MSG_SUCCSEE:
//                    text_state.setText("发送消息成功:" + msg.getData().getString("MSG"));
//                    break;
//                case SEND_MSG_ERROR:
//                    text_state.setText("发送消息失败:" + msg.getData().getString("MSG"));
//                    break;
//                case GET_MSG:
//
//                    String BackMsgData = msg.getData().getString("MSG");
//
//                    System.out.println("返回的数据>>>>>>>>>>>>>" + BackMsgData);
//
//                    try {
//                        JSONObject jsonObject = new JSONObject(BackMsgData);
//                        String Order = jsonObject.getString("Order");
//
//                        switch (Order) {
//
//                            //返回查询所有预置点的消息
//                            case "Order0":
//
//                                JSONArray jsonArrayPrset = jsonObject.getJSONArray("data");
//
//                                for (int i = 0; i < jsonArrayPrset.length(); i++) {//内部不锁定，效率最高，但在多线程要考虑并发操作的问题。
//                                    JSONObject jsonArrayPrsetObj = jsonArrayPrset.getJSONObject(i);
//                                    PresetBean itemPresetBean = new PresetBean(jsonArrayPrsetObj.getString("GeomagnetismAddressNumber"), i + "", jsonArrayPrsetObj.getString("PersetNumber"));
//                                    presetBeansList.add(itemPresetBean);
//                                }
//                                //弹出预置点Diloag
//                                initPersetDiloag();
//                                break;
//
//                            //新增预置点
//                            case "Order1":
//                                String errcode = jsonObject.getString("errcode");
//                                String errmsg = jsonObject.getString("errmsg");
//                                if (errcode.equals("0")) {
//                                    /**
//                                     * 重新设置数据
//                                     * */
//                                    //adapter.notifyDataSetChanged();
//                                    Tools.Toast(HiKCameraActivity.this, "预置点设置成功");
//                                } else {
//                                    Tools.Toast(HiKCameraActivity.this, errmsg);
//                                }
//                                break;
//
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//
//                    //text_state.setText("收到消息:" + msg.getData().getString("MSG"));
//
//
//                    break;
//            }
//        }
//    };
//
//    /**
//     * 获取连接到热点上的手机ip
//     *
//     * @return
//     */
//    private ArrayList<String> getConnectedIP() {
//        ArrayList<String> connectedIP = new ArrayList<String>();
//        try {
//            BufferedReader br = new BufferedReader(new FileReader(
//                    "/proc/net/arp"));
//            String line;
//            while ((line = br.readLine()) != null) {
//                String[] splitted = line.split(" +");
//                if (splitted != null && splitted.length >= 4) {
//                    String ip = splitted[0];
//                    connectedIP.add(ip);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        //        Log.i("connectIp:", connectedIP);
//        return connectedIP;
//    }
}
