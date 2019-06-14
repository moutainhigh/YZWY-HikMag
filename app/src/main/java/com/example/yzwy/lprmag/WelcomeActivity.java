package com.example.yzwy.lprmag;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.example.yzwy.lprmag.control.activityStackExtends.util.ActivityStackManager;
import com.example.yzwy.lprmag.guide.animation.guide.GuideActivity;
import com.example.yzwy.lprmag.myConstant.ApiHttpURL;
import com.example.yzwy.lprmag.myConstant.UserInfoConstant;
import com.example.yzwy.lprmag.util.AESUtil;
import com.example.yzwy.lprmag.util.LogUtil;
import com.example.yzwy.lprmag.util.OkHttpUtil;
import com.example.yzwy.lprmag.util.SharePreferencesUtil;
import com.example.yzwy.lprmag.util.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Response;

/**
 * #################################################################################################
 * Copyright: Copyright (c) 2018
 * Created on 2019-04-03
 * Author: 仲超(zhongchao)
 * Version 1.0
 * Describe: 欢迎页
 * #################################################################################################
 */
public class WelcomeActivity extends AppCompatActivity {

    public static SharedPreferences sp;
    SharedPreferences.Editor ed;
    boolean bl;
    private Timer timer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消任务栏
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.ly_welcome);


        //==========================================================================================
        ActivityStackManager.getInstance().addActivity(this);

        //Tools.Intent(WelcomeActivity.this, GuideActivity.class);

        /**
         * 先申请权限
         * */
        PerMissionDynamic();


    }

    private void WelcomeDB() {
        sp = getPreferences(Activity.MODE_PRIVATE);
        ed = sp.edit();
        bl = sp.getBoolean("FIRSTINSTALLAPP", false);
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (!bl) {
                    //程序首次安装启动
                    ed.putBoolean("FIRSTINSTALLAPP", true);
                    ed.commit();
                    Tools.Intent(WelcomeActivity.this, GuideActivity.class);
                } else {

                    /**
                     * 取出用户名和密码和登录标识，判定用户是否登录
                     * */

                    boolean userIsLogin = SharePreferencesUtil.getBooleanValue(WelcomeActivity.this, UserInfoConstant.Flag, false);

                    if (userIsLogin) {
                        //接口判断是否更换账号密码
                        final String userName = SharePreferencesUtil.getStringValue(WelcomeActivity.this, UserInfoConstant.userName, "0");
                        final String passWord = SharePreferencesUtil.getStringValue(WelcomeActivity.this, UserInfoConstant.passWord, "0");

                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                Map<String, String> LoginStringMap = new HashMap<>();
                                try {
                                    LoginStringMap.put("userName", AESUtil.getInstance().JiaEncrypt(userName));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    LogUtil.showLog("AESJIA", "加密失败");
                                    return;
                                }
                                LoginStringMap.put("passWord", passWord);
                                OkHttpUtil.getInstance().postDataAsyn(ApiHttpURL.LoginVerification, LoginStringMap, new OkHttpUtil.MyNetCall() {
                                    @Override
                                    public void success(Call call, Response response) throws IOException {
                                        String rs = response.body().string();
                                        HanderMsgSend(handler, 100, rs);
                                        LogUtil.showLog("LoginActivity success --->", rs);
                                    }

                                    @Override
                                    public void failed(Call call, IOException e) {
                                        HanderMsgSend(handler, 101, e.toString());
                                        LogUtil.showLog("LoginActivity failed --->", e.toString());
                                    }
                                });


                            }
                        }).start();

//                        if (userName.equals("admin") && passWord.equals("123")) {
//                            //去登录界面
//                            Tools.Intent(WelcomeActivity.this, MainActivity.class);
//                        } else {
//
//                            SharePreferencesUtil.putStringValue(WelcomeActivity.this, UserInfoConstant.userName, "0");
//                            SharePreferencesUtil.putStringValue(WelcomeActivity.this, UserInfoConstant.passWord, "0");
//                            SharePreferencesUtil.putBooleanValue(WelcomeActivity.this, UserInfoConstant.Flag, false);
//
//                            //去登录界面
//                            Tools.Intent(WelcomeActivity.this, WelcomeActivity.class);
//                        }


                    } else {
                        //去登录界面
                        Tools.Intent(WelcomeActivity.this, LoginActivity.class);
                    }


                }
            }
        };
        timer.schedule(task, 1000 * 2);
    }


    /**
     * =============================================================================================
     * 停止定时器
     */
    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            // 一定设置为null，否则定时器不会被回收
            timer = null;
        }
    }


    /**
     * =============================================================================================
     * 发消息
     *
     * @param handler
     * @param what
     * @param Val
     */
    private void HanderMsgSend(Handler handler, int what, String Val) {
        Message message_Login_Success = new Message();
        message_Login_Success.what = what;
        Bundle bundle = new Bundle();
        bundle.putString("data", Val);
        message_Login_Success.setData(bundle);
        handler.sendMessage(message_Login_Success);
    }


    /**
     * =============================================================================================
     * 海康登录UI更新和Log打印和m_iLogID设置
     */
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            String data = msg.getData().getString("data");

            switch (msg.what) {

                case 100:

                    try {

                        String decryptData;
                        try {
                            decryptData = AESUtil.getInstance().JieDecrypt(data);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Tools.Toast(WelcomeActivity.this, "数据异常，解密失败");
                            break;
                        }
                        JSONObject jsonObject = new JSONObject(decryptData);

                        //JSONObject jsonObject = new JSONObject(data);
                        String errcode = jsonObject.getString("errcode");
                        String errmsg = jsonObject.getString("errmsg");


                        if (errcode.equals("0")) {
                            //登陆成功
                            //Tools.Toast(WelcomeActivity.this, "登陆成功");
                            Tools.Intent(WelcomeActivity.this, MainActivity.class);
                        } else {
                            //登陆失败
                            Tools.Toast(WelcomeActivity.this, errmsg);
                            resrtlogin();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        LogUtil.showLog("LoginActivity JSON failed --->", e.toString());
                        Tools.Toast(WelcomeActivity.this, "数据解析异常");
                        //Tools.Toast(WelcomeActivity.this, "数据解析异常，异常Log：\n" + data);
                        //去登录界面
                        Tools.Intent(WelcomeActivity.this, LoginActivity.class);
                        //resrtlogin();
                    }
                    break;

                case 101:
                    Tools.Toast(WelcomeActivity.this, "网络异常，请检查网络");
                    //Tools.Toast(WelcomeActivity.this, "登陆失败，异常Log：\n" + data);
                    //resrtlogin();
                    //去登录界面
                    Tools.Intent(WelcomeActivity.this, LoginActivity.class);
                    break;


                default:
                    break;

            }


        }


    };

    /**
     * =============================================================================================
     * 从新登陆
     */
    private void resrtlogin() {

        SharePreferencesUtil.putStringValue(WelcomeActivity.this, UserInfoConstant.userName, "0");
        SharePreferencesUtil.putStringValue(WelcomeActivity.this, UserInfoConstant.passWord, "0");
        SharePreferencesUtil.putStringValue(WelcomeActivity.this, UserInfoConstant.userID, "0");
        SharePreferencesUtil.putBooleanValue(WelcomeActivity.this, UserInfoConstant.Flag, false);
        //去登录界面
        Tools.Intent(WelcomeActivity.this, LoginActivity.class);

    }


    private static final int MY_PERMISSION_REQUEST_CODE = 10000;

    private void PerMissionDynamic() {
        /**
         * 第 1 步: 检查是否有相应的权限
         */
        boolean isAllGranted = checkPermissionAllGranted(
                new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CALL_PHONE
                }
        );
        // 如果这3个权限全都拥有, 则直接执行备份代码
        if (isAllGranted) {
            initLocation();
            return;
        }

        /**
         * 第 2 步: 请求权限
         */
        // 一次请求多个权限, 如果其他有权限是已经授予的将会自动忽略掉
        ActivityCompat.requestPermissions(
                this,
                new String[]{
//
//                        //位置
//                        Manifest.permission.ACCESS_FINE_LOCATION,
//                        Manifest.permission.ACCESS_COARSE_LOCATION,

                        //存储
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,

//                        //联系人（读，写，获取）
//                        Manifest.permission.WRITE_CONTACTS,
//                        Manifest.permission.GET_ACCOUNTS,
//                        Manifest.permission.READ_CONTACTS,
//
//                        //电话
//                        Manifest.permission.READ_CALL_LOG,
//                        Manifest.permission.READ_PHONE_STATE,
//                        Manifest.permission.CALL_PHONE,
//                        Manifest.permission.WRITE_CALL_LOG,
//                        Manifest.permission.USE_SIP,
//                        Manifest.permission.PROCESS_OUTGOING_CALLS,
//                        Manifest.permission.ADD_VOICEMAIL,
//
//                        //日历
//                        Manifest.permission.READ_CALENDAR,
//                        Manifest.permission.WRITE_CALENDAR,
//
//                        //相机
//                        Manifest.permission.CAMERA,
//
//                        //麦克风
//                        Manifest.permission.RECORD_AUDIO,
//
//                        //SMS
//                        Manifest.permission.READ_SMS,
//                        Manifest.permission.RECEIVE_WAP_PUSH,
//                        Manifest.permission.RECEIVE_MMS,
//                        Manifest.permission.WRITE_CALL_LOG,
//                        Manifest.permission.RECEIVE_SMS,
//                        Manifest.permission.SEND_SMS,

                },
                MY_PERMISSION_REQUEST_CODE
        );

    }

    /**
     * =============================================================================================
     * 所有权限都已经开启
     */
    private void initLocation() {
        WelcomeDB();
    }

    /**
     * 检查是否拥有指定的所有权限
     */
    private boolean checkPermissionAllGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }

    /**
     * 第 3 步: 申请权限结果返回处理
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSION_REQUEST_CODE) {
            boolean isAllGranted = true;

            // 判断是否所有的权限都已经授予了
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false;
                    break;
                }
            }

            if (isAllGranted) {
                // 如果所有的权限都授予了, 则执行备份代码
                initLocation();

            } else {
                // 弹出对话框告诉用户需要权限的原因, 并引导用户去应用权限管理中手动打开权限按钮
                openAppDetails();
            }
        }
    }

    /**
     * 打开 APP 的详情设置
     */
    private void openAppDetails() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("使用此应用需要开启权限授权，请到 “应用信息 -> 权限” 中授予！");
        builder.setPositiveButton("去手动授权", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }
}
