package com.example.yzwy.lprmag;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.example.yzwy.lprmag.util.LogUtil;
import com.example.yzwy.lprmag.util.Tools;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * #################################################################################################
 * Copyright: Copyright (c) 2018
 * Created on 2019-04-03
 * Author: 仲超(zhongchao)
 * Version 1.0
 * Describe: 我的详细信息页面
 * #################################################################################################
 */
public class MineInfoActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消任务栏
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.ly_mine_info);


        initView();


    }

    /**
     * =============================================================================================
     * 初始化组件
     */
    private void initView() {

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
                            //登陆成功
                            //Tools.Toast(WelcomeActivity.this, "登陆成功");
                            Tools.Intent(MineInfoActivity.this, MainActivity.class);
                        } else {
                            //登陆失败
                            Tools.Toast(MineInfoActivity.this, errmsg);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        LogUtil.showLog("LoginActivity JSON failed --->", e.toString());
                        Tools.Toast(MineInfoActivity.this, "数据解析异常");
                        //Tools.Toast(WelcomeActivity.this, "数据解析异常，异常Log：\n" + data);
                        //去登录界面
                        Tools.Intent(MineInfoActivity.this, LoginActivity.class);
                        //resrtlogin();
                    }
                    break;

                case 101:
                    Tools.Toast(MineInfoActivity.this, "网络异常，请检查网络");
                    //Tools.Toast(WelcomeActivity.this, "登陆失败，异常Log：\n" + data);
                    //resrtlogin();
                    //去登录界面
                    Tools.Intent(MineInfoActivity.this, LoginActivity.class);
                    break;


                default:
                    break;

            }


        }


    };
}
