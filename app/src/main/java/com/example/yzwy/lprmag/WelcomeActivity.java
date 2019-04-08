package com.example.yzwy.lprmag;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.example.yzwy.lprmag.guide.animation.guide.GuideActivity;
import com.example.yzwy.lprmag.myConstant.HttpUrl;
import com.example.yzwy.lprmag.myConstant.UserInfoConstant;
import com.example.yzwy.lprmag.util.ExitApplication;
import com.example.yzwy.lprmag.util.LogUtil;
import com.example.yzwy.lprmag.util.OkHttpUtils;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消任务栏
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.ly_welcome);


        //==========================================================================================
        ExitApplication.getInstance().addActivity(this);

        //Tools.Intent(WelcomeActivity.this, GuideActivity.class);

        WelcomeDB();
    }

    private void WelcomeDB() {
        sp = getPreferences(Activity.MODE_PRIVATE);
        ed = sp.edit();
        bl = sp.getBoolean("FIRSTINSTALLAPP", false);
        Timer timer = new Timer();
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
                                LoginStringMap.put("userName", userName);
                                LoginStringMap.put("passWord", passWord);
                                OkHttpUtils.getInstance().postDataAsyn(HttpUrl.LoginUrl, LoginStringMap, new OkHttpUtils.MyNetCall() {
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


//                        if (userName.equals("admin") && passWord.equals("admin123")) {
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
                        Tools.Intent(WelcomeActivity.this, WelcomeActivity.class);
                    }


                }
            }
        };
        timer.schedule(task, 1000 * 2);
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
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            String data = msg.getData().getString("data");
            try {
                JSONObject jsonObject = new JSONObject(data);
                String errcode = jsonObject.getString("errcode");
                //String errmsg = jsonObject.getString("errmsg");
                String message = jsonObject.getString("message");

                switch (msg.what) {

                    case 100:
                        if (errcode.equals("0")) {
                            //登陆成功
                            //Tools.Toast(WelcomeActivity.this, "登陆成功");
                            Tools.Intent(WelcomeActivity.this, MainActivity.class);
                        } else {
                            //登陆失败
                            Tools.Toast(WelcomeActivity.this, message);
                            resrtlogin();
                        }


                        break;

                    case 101:
                        Tools.Toast(WelcomeActivity.this, "登陆失败，异常Log：\n" + data);
                        resrtlogin();
                        break;


                    default:
                        break;

                }
            } catch (JSONException e) {
                e.printStackTrace();
                LogUtil.showLog("LoginActivity JSON failed --->", e.toString());
                Tools.Toast(WelcomeActivity.this, "失败，JSON解析异常，异常Log：\n" + data);

                resrtlogin();
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
        SharePreferencesUtil.putBooleanValue(WelcomeActivity.this, UserInfoConstant.Flag, false);
        //去登录界面
        Tools.Intent(WelcomeActivity.this, LoginActivity.class);

    }
}
