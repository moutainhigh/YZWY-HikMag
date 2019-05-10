package com.example.yzwy.lprmag;


import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.widget.LinearLayout;

import com.example.yzwy.lprmag.adapter.TabFragmentAdapter;
import com.example.yzwy.lprmag.broadcast.NetWorkChangReceiver;
import com.example.yzwy.lprmag.control.activityStackExtends.util.ActivityStackManager;
import com.example.yzwy.lprmag.fragment.HomeFragment;
import com.example.yzwy.lprmag.fragment.MineFragment;
import com.example.yzwy.lprmag.myinterface.NetBroadcastListener;
import com.example.yzwy.lprmag.util.LogUtil;
import com.example.yzwy.lprmag.util.SystemUtil;
import com.example.yzwy.lprmag.util.Tools;
import com.example.yzwy.lprmag.view.TabContainerView;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

/**
 * #################################################################################################
 * Copyright: Copyright (c) 2018
 * Created on 2019-04-03
 * Author: 仲超(zhongchao)
 * Version 1.0
 * Describe: 主界面
 * #################################################################################################
 */
public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, NetBroadcastListener {


    private long exitTime;
    private NetWorkChangReceiver netWorkStateReceiver;


    /**
     * =============================================================================================
     * 试图管理 页面跳转
     *
     * @param activity
     * @param tab
     */
    public static void startMainActivity(Activity activity, int tab) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra("tab", tab);
        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);

    }

    /**
     * =============================================================================================
     * tab图标集合
     */
    private final int ICONS_RES[][] = {
            {
                    R.mipmap.ic_home_normal,
                    R.mipmap.ic_home_focus
            }
//            ,
//            {
//                    R.mipmap.ic_message_normal,
//                    R.mipmap.ic_message_focus
//            }
            ,

            {
                    R.mipmap.ic_mine_normal,
                    R.mipmap.ic_mine_focus
            }
    };

    /**
     * =============================================================================================
     * tab 颜色值
     */
    private final int[] TAB_COLORS = new int[]
            {
                    R.color.main_bottom_tab_textcolor_normal,
                    R.color.main_bottom_tab_textcolor_selected
            };

    /**
     * =============================================================================================
     * 实例化fragment
     */
    private Fragment[] fragments = {
            new HomeFragment(),
//            new MessageFragment(),
            new MineFragment()
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ly_main);


        //==========================================================================================
        ActivityStackManager.getInstance().addActivity(this);

        //初始化组件
        initViews();


        StringBuffer stringBufferswLog = new StringBuffer();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = Math.min(dm.widthPixels, dm.heightPixels);
        stringBufferswLog.append("--->\n");
        stringBufferswLog.append("密度Dpi: " + dm.densityDpi + "\n");
        stringBufferswLog.append("最小宽度像素: " + width + "\n");
        stringBufferswLog.append("计算出来的smallestWidth : " + width / (dm.densityDpi / 160.0) + "dp" + "\n");
        stringBufferswLog.append("实际使用的smallestWidth :  " + getResources().getString(R.string.base_dpi) + "\n");
        stringBufferswLog.append("当前手机： " + SystemUtil.getDeviceBrand() + "  " + SystemUtil.getSystemModel() + " \n" + "当前系统： " + SystemUtil.getSystemVersion() + " ");

        LogUtil.showLog("swSystem", stringBufferswLog.toString());


//        try {
//
//            String Key = "A1zFlux77a99X1be";
//            String src = "admin";
//            String Base64Str = AesUtil.encryptBase64("A1zFlux77a99X1be", "admin", AesUtil.TRANSFORM_ECB_PKCS5PADDING);
//            String HexStr = AesUtil.encryptHexStr("A1zFlux77a99X1be", "admin", AesUtil.TRANSFORM_ECB_PKCS5PADDING);
//
//
//            System.out.println("mainactivity --->AES加密结果是 Base64 ：" + Base64Str);
//            System.out.println("mainactivity --->AES加密结果是 HexStr ：" + AesUtil.encryptHexStr(Key, src, AesUtil.TRANSFORM_ECB_PKCS5PADDING));
//
//            System.out.println("mainactivity --->AES解密结果是 Str ：" + AesUtil.decryptStr(Key, HexStr, AesUtil.TRANSFORM_ECB_PKCS5PADDING));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


//        MinaThread mThread = new MinaThread(getWifiRouteIPAddress(MainActivity.this), WifiMsgConstant.PORT_wifi);
//        mThread.start();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//
////                SocketClient client = new SocketClient();
////                // 建立socket对象
////                int iret = client.connect(getWifiRouteIPAddress(MainActivity.this), WifiMsgConstant.PORT_wifi);
////                if (iret == 0) {
////                    // 发送数据
////                    client.write("helloworld".getBytes());
////                    // 接收数据
////                    byte data[] = client.read();
////                    if ((data != null) && (data.length != 0)) {
////                        // 处理接收结果
////                        System.out.println("响应报文字节数组---->" + Arrays.toString(data));
////                    }
////                }
//
//
//                try {
////                    String socketServerMsg = SocketUtil.getInstance().SocketRequest(getWifiRouteIPAddress(MainActivity.this), WifiMsgConstant.PORT_wifi, SendOrder.Get_PersetData());
//                    SocketUtil.getInstance().SocketRequest(mHandler,"192.168.1.110", WifiMsgConstant.PORT_wifi, SendOrder.Get_PersetData());
////                    LogUtil.showLog("ServerUtil---> /...", socketServerMsg);
////                    HandlerMsgSend(handler, 100, "data", socketServerMsg);
//                } catch (final IOException e) {
//                    e.printStackTrace();
//                    LogUtil.showLog("ServerUtil--->  /***", e.toString());
////                    HandlerMsgSend(handler, 101, "data", e.toString());
//                }
//            }
//        }).start();


    }

    /**
     * Handler
     */
    private Handler mHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0: {
                    String content = (String) msg.obj;
                    //mTxtContent.setText(content);
                    break;
                }
            }
        }
    };


    /**
     * =============================================================================================
     * 初始化组件
     */
    private void initViews() {
        TabFragmentAdapter mAdapter = new TabFragmentAdapter(getSupportFragmentManager(), fragments);
        ViewPager mPager = (ViewPager) findViewById(R.id.tab_pager);
        //设置当前可见Item左右可见page数，次范围内不会被销毁
        mPager.setOffscreenPageLimit(1);
        mPager.setAdapter(mAdapter);

        TabContainerView mTabLayout = (TabContainerView) findViewById(R.id.ll_tab_container);
        mTabLayout.setOnPageChangeListener(this);

        mTabLayout.initContainer(getResources().getStringArray(R.array.tab_main_title), ICONS_RES, TAB_COLORS, true);

        int width = getResources().getDimensionPixelSize(R.dimen.tab_icon_width);
        int height = getResources().getDimensionPixelSize(R.dimen.tab_icon_height);
        int fontSize = getResources().getDimensionPixelOffset(R.dimen.qb_px_10);
        mTabLayout.setContainerLayout(R.layout.tab_container_view, R.id.iv_tab_icon, R.id.tv_tab_text, width, height,fontSize);
//        mTabLayout.setSingleTextLayout(R.layout.tab_container_view, R.id.tv_tab_text);
//        mTabLayout.setSingleIconLayout(R.layout.tab_container_view, R.id.iv_tab_icon);

        mTabLayout.setViewPager(mPager);

        mPager.setCurrentItem(getIntent().getIntExtra("tab", 0));


    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int index = 0, len = fragments.length; index < len; index++) {
            fragments[index].onHiddenChanged(index != position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }


    //在onResume()方法注册
    @Override
    protected void onResume() {

        if (netWorkStateReceiver == null) {
            netWorkStateReceiver = new NetWorkChangReceiver(this);
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkStateReceiver, filter);
        System.out.println("实时监测网络注册");
        super.onResume();
    }

    //onPause()方法注销
    @Override
    protected void onPause() {
        unregisterReceiver(netWorkStateReceiver);
        System.out.println("实时监测网络注销");
        super.onPause();
    }


    /**
     * =============================================================================================
     * 双击退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitAPP();
        }
        return false;
    }

    private void exitAPP() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Tools.Toast(MainActivity.this, "再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            ActivityStackManager.getInstance().exitSystem();//完全退出所有Activity 活动
            System.exit(0);
        }
    }


    @Override
    public void netBroadcastReceiver(int netStatus, int netMobile, boolean isAvailable) {

        if (netStatus != 200) {
            Tools.Toast(MainActivity.this, "网络状态不可用，请检查网络");
        }

        //Tools.Toast(MainActivity.this, "网络状态:" + netStatus + "  网络类型:" + netMobile);
        LogUtil.showLog("MainNet --->", "网络状态:" + netStatus + "  网络类型:" + netMobile);
    }
}

