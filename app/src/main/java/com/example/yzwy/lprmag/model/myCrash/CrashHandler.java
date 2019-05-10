package com.example.yzwy.lprmag.model.myCrash;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.example.yzwy.lprmag.control.activityStackExtends.util.ActivityStackManager;
import com.example.yzwy.lprmag.myConstant.HttpURL;
import com.example.yzwy.lprmag.util.ExecRootCmd;
import com.example.yzwy.lprmag.util.HanderUtil;
import com.example.yzwy.lprmag.util.LogUtil;
import com.example.yzwy.lprmag.util.OkHttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;


/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 *
 * @author user
 */
public class CrashHandler implements UncaughtExceptionHandler {

    public static final String TAG = "CrashHandler";

    private Throwable exsss;

    //系统默认的UncaughtException处理类
    private UncaughtExceptionHandler mDefaultHandler;
    //CrashHandler实例
    private static CrashHandler INSTANCE = new CrashHandler();
    //程序的Context对象
    private Context mContext;
    //用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<String, String>();

    //用于格式化日期,作为日志文件名的一部分
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashHandler() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Log.e(TAG, "error : ", e);
            }

            ActivityStackManager.getInstance().finishAllActivity();
            android.os.Process.killProcess(android.os.Process.myPid());

            //mDefaultHandler.uncaughtException(thread, ex);
           // android.os.Process.killProcess(android.os.Process.myPid());


            /**
             * 由于是系统签名，所以必须适应root权限杀死进程
             * */
//            try {
//                Runtime.getRuntime().exec("am force-stop com.example.yzwy.lprmag");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            String killAPP = "am force-stop com.example.yzwy.lprmag";
//            ExecRootCmd.execRootCmd(killAPP);

//            ActivityStackManager.getInstance().finishAllActivity();
//            //System.exit(0);
//            //退出程序
//            android.os.Process.killProcess(android.os.Process.myPid());
//            System.exit(1);


        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }

        exsss = ex;

        //使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                //Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
        //收集设备参数信息
        collectDeviceInfo(mContext);
        //保存日志文件
        //saveCrashInfo2File(ex);

        //
        initSaveType();

        return true;
    }

    /**
     * =============================================================================================
     * 保存方式
     */
    private void initSaveType() {

        //上传到服务器
        UpdateWWW();

        //保存日志文件
        //saveCrashInfo2File(ex);
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info", e);
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private void saveCrashInfo2File(Throwable ex) {

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            long timestamp = System.currentTimeMillis();
            String time = formatter.format(new Date());
            String fileName = "crash-" + time + "-" + timestamp + ".log";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                @SuppressLint("SdCardPath") String path = "/sdcard/lprcrash/";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + fileName);
                fos.write(sb.toString().getBytes());
                fos.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
        }

    }

    private void UpdateWWW() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                Map<String, String> LoginStringMap = new HashMap<>();
//                LoginStringMap.put("userName", edtUsername);
//                LoginStringMap.put("passWord", edtPwd);
                OkHttpUtil.getInstance().postDataAsyn(HttpURL.LoginVerification, LoginStringMap, new OkHttpUtil.MyNetCall() {
                    @Override
                    public void success(Call call, Response response) throws IOException {
                        String rs = response.body().string();
                        HanderUtil.HanderMsgSend(handler, 100, rs);
                        LogUtil.showLog("LoginActivity success --->", rs);
                    }

                    @Override
                    public void failed(Call call, IOException e) {
                        HanderUtil.HanderMsgSend(handler, 101, e.toString());
                        LogUtil.showLog("LoginActivity failed --->", e.toString());
                    }
                });


            }
        }).start();

    }


    /**
     * =============================================================================================
     * 海康登录UI更新和Log打印和m_iLogID设置
     */
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);


            String data = msg.getData().getString("data");
            switch (msg.what) {

                case 100:
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        String errcode = jsonObject.getString("errcode");
                        String errmsg = jsonObject.getString("errmsg");


                        if (errcode.equals("0")) {

                            exsss = null;

                        } else {
                            //保存到文件中
                            saveCrashInfo2File(exsss);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        saveCrashInfo2File(exsss);
                        LogUtil.showLog("Crash JSON failed --->", e.toString());
                    }
                    break;

                case 101:
                    saveCrashInfo2File(exsss);
                    break;


                default:
                    break;

            }


        }


    };
}
