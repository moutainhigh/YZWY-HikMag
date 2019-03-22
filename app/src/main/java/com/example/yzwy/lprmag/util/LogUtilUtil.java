package com.example.yzwy.lprmag.util;

import android.util.Log;

public class LogUtilUtil {

    private static final boolean mIsShow=true;

    public static void showLog(String tag,String log){
        if(mIsShow==true){
            Log.e(tag,log);
        }
    }
}
