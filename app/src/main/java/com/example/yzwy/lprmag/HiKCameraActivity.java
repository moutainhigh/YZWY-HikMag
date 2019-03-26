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

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
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
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.yzwy.lprmag.application.MyApp;
import com.example.yzwy.lprmag.dialog.DialogPerset;
import com.example.yzwy.lprmag.dialog.LprDialog;
import com.example.yzwy.lprmag.hik.model.CameraManager;
import com.example.yzwy.lprmag.hik.util.CrashUtil;
import com.example.yzwy.lprmag.hik.util.NotNull;
import com.example.yzwy.lprmag.myConstant.CarRectLintScreen;
import com.example.yzwy.lprmag.myConstant.ConfigDataConstant;
import com.example.yzwy.lprmag.util.LogUtil;
import com.example.yzwy.lprmag.util.Tools;
import com.example.yzwy.lprmag.util.SharePreferencesUtil;
import com.hikvision.netsdk.ExceptionCallBack;
import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.NET_DVR_DEVICEINFO_V30;
import com.hikvision.netsdk.NET_DVR_PREVIEWINFO;
import com.hikvision.netsdk.PTZCommand;
import com.hikvision.netsdk.RealPlayCallBack;

import org.MediaPlayer.PlayM4.Player;

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

    private String ADDRESS = "192.168.1.64";//摄像头IP地址
    private int PORT = 8000;//摄像头端口号
    private String USER = "admin";//摄像头用户名
    private String PSD = "admin123";//摄像头密码


    private View rectLine;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        //去掉标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        /**
         * 加载布局
         * */
        setContentView(R.layout.ly_hik);

        /**
         * 摄像机错误工具类
         * */
        CrashUtil crashUtil = CrashUtil.getInstance();
        crashUtil.init(this);

        initHiKConnectConfig();

        /**
         * 加载海康SDK 加载失败退出活动页面
         * */
        if (!initeSdk()) {
            this.finish();
            //return;
        }

        if (!initeActivity()) {
            this.finish();
            //return;
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
            //return;
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
            //return;
        }
        if (!HCNetSDK.getInstance().NET_DVR_SetExceptionCallBack(oexceptionCbf)) {
            Log.e(TAG, "NET_DVR_SetExceptionCallBack is failed!");
            //return;
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

    /**
     * =============================================================================================
     * 初始化海康连接诶信息
     */
    private void initHiKConnectConfig() {

        ADDRESS = SharePreferencesUtil.getStringValue(HiKCameraActivity.this, ConfigDataConstant.hkIp_cfgset_str, ConfigDataConstant.hkIp_cfgset_str_default);
        PORT = Integer.valueOf(SharePreferencesUtil.getStringValue(HiKCameraActivity.this, ConfigDataConstant.hkport_cfgset_str, ConfigDataConstant.hkport_cfgset_str_default));
        USER = SharePreferencesUtil.getStringValue(HiKCameraActivity.this, ConfigDataConstant.hikusername_cfgset_str, ConfigDataConstant.hikusername_cfgset_str_default);
        PSD = SharePreferencesUtil.getStringValue(HiKCameraActivity.this, ConfigDataConstant.hikpwd_cfgset_str, ConfigDataConstant.hikpwd_cfgset_str_default);

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


        /**
         * 设置中间需要裁剪的车牌宽度的红框
         * */
        rectLine = (View) findViewById(R.id.rectLine);

        initRectLineWH();

    }


    /**
     * =============================================================================================
     * 设置中间需要裁剪的车牌宽度的红框
     */
    private void initRectLineWH() {
        int HikSurfaceViewWidth = m_osurfaceView.getWidth();
        int HikSurfaceViewHeight = m_osurfaceView.getHeight();
        //设置固定大小


//        FrameLayout.LayoutParams params=(FrameLayout.LayoutParams) rectLine.getLayoutParams();
//params.width=500;
//params.height=500;
//// params.setMargins(dip2px(MyMainActivity.this, 1), 0, 0, 0); // 可以实现设置位置信息，如居左距离，其它类推  
//// params.leftMargin = dip2px(MyMainActivity.this, 1);  
//        rectLine.setLayoutParams(params);


        /**
         * 注册一个ViewTreeObserver的监听回调，这个监听回调，就是专门监听绘图的，既然是监听绘图，
         * 那么我们自然可以获取测量值了，同时，我们在每次监听前remove前一次的监听，避免重复监听。
         * */
        ViewTreeObserver vto = m_osurfaceView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                /**
                 * 获取屏幕高宽度
                 * */
                int WinScreenWidth = MyApp.getInstance().getScreenWidth();//1920
                int WinScreenHeight = MyApp.getInstance().getScreenHeight();//1080

                //
                int WSc = (int) ((int) MyApp.getInstance().getScreenWidth() / CarRectLintScreen.WidthProportion);//125
                int HSc = (int) ((int) MyApp.getInstance().getScreenHeight() / CarRectLintScreen.HeightProportion);//125


                m_osurfaceView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int HikSurfaceViewHeight = m_osurfaceView.getHeight();
                int HikSurfaceViewWidth = m_osurfaceView.getWidth();

                //父级比列宽度和高度
                int ParentWidth_S = WinScreenWidth;//1920
                int ParentHeight_S = WinScreenHeight;//1080

                //父级比列设置宽度和高度
                int ParentWidth_s = WSc;//250
                int ParentHeight_s = HSc;//120

                //父级宽度和高度
                int ParentWidthView = HikSurfaceViewWidth;
                int ParentHeightView = HikSurfaceViewHeight;

                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) rectLine.getLayoutParams();
                params.width = (int) (ParentWidthView * ParentWidth_s) / ParentWidth_S;
                params.height = (int) (ParentHeightView * ParentHeight_s) / ParentHeight_S;
                rectLine.setLayoutParams(params);

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
            //return;
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
                //return;
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
            Log.e(TAG, "NET_DVR_RealPlay is failed!Err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
            return;
        }
        isShow = false;
        if (NotNull.isNotNull(thread)) {
            thread.interrupt();
        }
        h1 = new CameraManager();
        h1.setLoginId(m_iLogID);

        //设置预置点
        Intent intent = getIntent();
        if (NotNull.isNotNull(intent) && intent.getIntExtra("INDEX", -1) != -1) {
            int point = SharePreferencesUtil.getIntValue(this, "POINT", 0);
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
        int iLogID = HCNetSDK.getInstance().NET_DVR_Login_V30(ADDRESS, PORT,
                USER, PSD, m_oNetDvrDeviceInfoV30);
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
    public void processRealData(int iDataType, byte[] pDataBuffer, int iDataSize, int iStreamMode) {
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
}
