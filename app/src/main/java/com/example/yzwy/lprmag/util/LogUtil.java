package com.example.yzwy.lprmag.util;

import android.util.Log;

/**
 * Created by LY on 2017/3/7.
 * 管理Log的工具类
 */
public class LogUtil {
    /**
     * 日志输出级别NONE
     */
    public static final int LEVEL_NONE = 0;
    /**
     * 日志输出级别E
     */
    public static final int LEVEL_ERROR = 1;
    /**
     * 日志输出级别W
     */
    public static final int LEVEL_WARN = 2;
    /**
     * 日志输出级别I
     */
    public static final int LEVEL_INFO = 3;
    /**
     * 日志输出级别D
     */
    public static final int LEVEL_DEBUG = 4;
    /**
     * 日志输出级别V
     */
    public static final int LEVEL_VERBOSE = 5;

    /**
     * 日志输出时的TAG
     */
    private static String mTag = "LogUtil";
    /**
     * 是否允许输出log
     */
    private static int mDebuggable = LEVEL_VERBOSE;

    /**
     * 以级别为 d 的形式输出LOG
     */
    public static void v(String tag, String msg) {
        if (mDebuggable >= LEVEL_VERBOSE) {
            Log.v(tag, msg);
        }
    }

    /**
     * 以级别为 d 的形式输出LOG
     */
    public static void d(String tag, String msg) {
        if (mDebuggable >= LEVEL_DEBUG) {
            Log.d(tag, msg);
        }
    }

    /**
     * 以级别为 i 的形式输出LOG
     */
    public static void i(String tag, String msg) {
        if (mDebuggable >= LEVEL_INFO) {
            Log.i(tag, msg);
        }
    }

    /**
     * 以级别为 w 的形式输出LOG
     */
    public static void w(String tag, String msg) {
        if (mDebuggable >= LEVEL_WARN) {
            Log.w(tag, msg);
        }
    }

    /**
     * 以级别为 w 的形式输出Throwable
     */
    public static void w(Throwable tr) {
        if (mDebuggable >= LEVEL_WARN) {
            Log.w(mTag, "", tr);
        }
    }

    /**
     * 以级别为 w 的形式输出LOG信息和Throwable
     */
    public static void w(String msg, Throwable tr) {
        if (mDebuggable >= LEVEL_WARN && null != msg) {
            Log.w(mTag, msg, tr);
        }
    }

    /**
     * 以级别为 e 的形式输出LOG
     */
    public static void e(String tag, String msg) {
        if (mDebuggable >= LEVEL_ERROR) {
            Log.e(tag, msg);
        }
    }

    /**
     * 以级别为 e 的形式输出Throwable
     */
    public static void e(Throwable tr) {
        if (mDebuggable >= LEVEL_ERROR) {
            Log.e(mTag, "", tr);
        }
    }

    /**
     * 以级别为 e 的形式输出LOG信息和Throwable
     */
    public static void e(String msg, Throwable tr) {
        if (mDebuggable >= LEVEL_ERROR && null != msg) {
            Log.e(mTag, msg, tr);
        }
    }


    /**
     * 以级别为 e 的形式输出LOG信息和Throwable
     */
    private static final boolean mIsShow = true;

    public static void showLog(String tag, String log) {
        if (mIsShow == true) {
            Log.e(tag, log);
        }
    }



    /**
     * 分段打印出较长log文本
     * @param logContent  打印文本
     * @param showLength  规定每段显示的长度（AndroidStudio控制台打印log的最大信息量大小为4k）
     * @param tag         打印log的标记
     */
    public static void showLargeLog(String logContent, int showLength, String tag){
        if(logContent.length() > showLength){
            String show = logContent.substring(0, showLength);
            showLog(tag, show);
            /*剩余的字符串如果大于规定显示的长度，截取剩余字符串进行递归，否则打印结果*/
            if((logContent.length() - showLength) > showLength){
                String partLog = logContent.substring(showLength,logContent.length());
                showLargeLog(partLog, showLength, tag);
            }else{
                String printLog = logContent.substring(showLength, logContent.length());
                showLog(tag, printLog);
            }

        }else{
            showLog(tag, logContent);
        }
    }


}
