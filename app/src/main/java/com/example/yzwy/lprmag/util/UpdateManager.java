package com.example.yzwy.lprmag.util;

import android.content.Context;

public class UpdateManager {

    private static UpdateManager manager = null;
    //private static String packageName = "com.example.yzwy.lprmag";

    private UpdateManager() {
    }

    public static UpdateManager getInstance() {
        manager = new UpdateManager();
        return manager;
    }

    //获取版本号
    public int getVersion(Context context) {
        int version = 0;
        try {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            System.out.println("获取版本号异常！");
        }
        return version;
    }

    //获取版本名
    public String getVersionName(Context context) {
        String versionName = null;
        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            System.out.println("获取版本名异常！");
        }
        return versionName;
    }

}
