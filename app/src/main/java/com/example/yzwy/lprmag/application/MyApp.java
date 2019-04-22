package com.example.yzwy.lprmag.application;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.example.yzwy.lprmag.model.myCrash.CrashHandler;
import com.example.yzwy.lprmag.myConstant.HiKLineWHRectLintScreen;
import com.example.yzwy.lprmag.util.LogUtil;


/**
 * 应用程序的入口
 */

/**
 * 存储app全局数据，实现全局共享数据
 */
public class MyApp extends Application {

    private static MyApp myApp = null;
    private final String TAG = "MyLprApp";

    //屏幕宽高 密度
    private int screenHeight = 0;
    private int screenWidth = 0;
    private float density;//密度

    //识别区域宽高
    private int mRectHeight = 150;
    private int mRectWidth = 250;


    private static Context mContext;

    /**
     * 判断程序生命周期结束后是否首次启动 默认为false
     */
    private boolean firstInAppLife = false;

    public int getScreenHeight() {
        return screenHeight;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public float getDensity() {
        return density;
    }


    //创建一个静态的方法，以便获取context对象
    public static Context getContext() {
        return mContext;
    }


    public static MyApp getInstance() {
        return myApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        LogUtil.showLog(TAG, "程序创建的时候执行");

        //获取context
        mContext = getApplicationContext();
        myApp = this;


        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;
        density = getResources().getDisplayMetrics().density;
        //
        mRectWidth = (int) ((int) myApp.getScreenWidth() / HiKLineWHRectLintScreen.WidthProportion);//125
        mRectHeight = (int) ((int) myApp.getScreenHeight() / HiKLineWHRectLintScreen.HeightProportion);//125


        ///**
        // * 开启服务
        // * */
        //Intent startAppServiceIntent = new Intent(this, AppService.class);
        //startService(startAppServiceIntent);



        //收集异常信息
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());

    }

    /**
     * =============================================================================================
     * 程序终止的时候执行
     * 在模拟环境下执行。当终止应用程序对象时调用，不保证一定被调用，
     * 当程序是被内核终止以便为其他应用程序释放资源，那么将不会提醒，
     * 并且不调用应用程序Application对象的onTerminate方法而直接终止进程。
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        LogUtil.showLog(TAG, "程序被终止");
        /**
         * 关闭服务
         * */
        //Intent stopAppServiceIntent = new Intent(this, AppService.class);
        //stopService(stopAppServiceIntent);
    }


    /**
     * =============================================================================================
     * 低内存的时候执行
     * <p>
     * 好的应用程序一般会在这个方法里面释放一些不必要的资源来应付当后台程序已经终止，
     * 前台应用程序内存还不够时的情况。
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        LogUtil.showLog(TAG, "低内存的时候执行");
    }


    /**
     * =============================================================================================
     * 程序在进行内存清理时执行
     */
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        LogUtil.showLog(TAG, "程序在进行内存清理时执行");
        /**
         * 关闭服务
         * */
        //Intent stopAppServiceIntent = new Intent(this, AppService.class);
        //stopService(stopAppServiceIntent);
    }


    /**
     * =============================================================================================
     * 配置改变时触发这个方法
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LogUtil.showLog(TAG, "配置改变时触发这个方法");
    }


}
