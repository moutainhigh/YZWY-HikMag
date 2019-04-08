package com.example.yzwy.lprmag;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.yzwy.lprmag.dialog.MessageDialog;
import com.example.yzwy.lprmag.myConstant.HttpUrl;
import com.example.yzwy.lprmag.myConstant.UserInfoConstant;
import com.example.yzwy.lprmag.util.ExitApplication;
import com.example.yzwy.lprmag.util.LogUtil;
import com.example.yzwy.lprmag.util.OkHttpUtils;
import com.example.yzwy.lprmag.util.SharePreferencesUtil;
import com.example.yzwy.lprmag.util.Tools;
import com.example.yzwy.lprmag.view.LoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * #################################################################################################
 * Copyright: Copyright (c) 2018
 * Created on 2019-04-03
 * Author: 仲超(zhongchao)
 * Version 1.0
 * Describe: 登录界面
 * #################################################################################################
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edt_username_lgn;
    private EditText edt_pwd_lgn;

    private Button btn_clode_lgn;
    private Button btn_forget_lgn;
    private Button btn_login_lgn;
    private String edtUsername;
    private String edtPwd;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_login);


        //==========================================================================================
        ExitApplication.getInstance().addActivity(this);

        initView();

    }

    /**
     * =============================================================================================
     * 初始化组件
     */
    private void initView() {
        edt_username_lgn = (EditText) findViewById(R.id.edt_username_lgn);
        edt_pwd_lgn = (EditText) findViewById(R.id.edt_pwd_lgn);
        btn_clode_lgn = (Button) findViewById(R.id.btn_clode_lgn);
        btn_forget_lgn = (Button) findViewById(R.id.btn_forget_lgn);
        btn_login_lgn = (Button) findViewById(R.id.btn_login_lgn);

        initOnClick();
    }

    /**
     * =============================================================================================
     * 初始化组件监听事件
     */
    private void initOnClick() {
        btn_clode_lgn.setOnClickListener(this);
        btn_forget_lgn.setOnClickListener(this);
        btn_login_lgn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            /**
             * 关闭页面
             * */
            case R.id.btn_clode_lgn:
                loadingDialog.dismiss();
                this.finish();
                break;

            /**
             * 忘记密码
             * */
            case R.id.btn_forget_lgn:
                SetDialog();
                break;

            /**
             * 登录按钮
             * */
            case R.id.btn_login_lgn:


                edtUsername = edt_username_lgn.getText().toString().trim();
                edtPwd = edt_pwd_lgn.getText().toString().trim();

                if (edtUsername.equals("")) {
                    Tools.Toast(LoginActivity.this, "用户名不能为空");
                    return;
                } else if (edtPwd.equals("")) {
                    Tools.Toast(LoginActivity.this, "密码不能为空");
                    return;
                }


                loadingDialog = new LoadingDialog(this, "正在登录...", R.mipmap.ic_dialog_loading);
                loadingDialog.show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Map<String, String> LoginStringMap = new HashMap<>();
                        LoginStringMap.put("userName", edtUsername);
                        LoginStringMap.put("passWord", edtPwd);
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


//                String edtUsername = edt_username_lgn.getText().toString().trim();
//                String edtPwd = edt_pwd_lgn.getText().toString().trim();
//
//                if (edtUsername.equals("")) {
//                    Tools.Toast(LoginActivity.this, "用户名不能为空");
//                } else if (edtPwd.equals("")) {
//                    Tools.Toast(LoginActivity.this, "密码不能为空");
//                } else if (edtUsername.equals("admin") && edtPwd.equals("admin123")) {
//                    //登陆成功
//                    Tools.Toast(LoginActivity.this, "登陆成功");
//
//
//                    SharePreferencesUtil.putStringValue(LoginActivity.this, UserInfoConstant.userName, edtUsername);
//                    SharePreferencesUtil.putStringValue(LoginActivity.this, UserInfoConstant.passWord, edtPwd);
//                    SharePreferencesUtil.putBooleanValue(LoginActivity.this, UserInfoConstant.Flag, true);
//
//
//                    Tools.Intent(LoginActivity.this, MainActivity.class);
//
//                } else {
//                    //登录失败
//                    Tools.Toast(LoginActivity.this, "登陆失败，账号或密码错误");
//                }


                break;

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
     * 设置预置点监听事件
     */
    public void SetDialog() {
        View linearLayout = getLayoutInflater().inflate(R.layout.dialog_forgetpwd, null);
        Button btnSetPreset = (Button) linearLayout.findViewById(R.id.btn_dialog_custom_ok);
        final AlertDialog dialog = getDialongView(linearLayout);
        //设置背景半透明
        //dialog.getWindow().setBackgroundDrawableResource(R.color.translucent);
        //取消
        btnSetPreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }

    /**
     * =============================================================================================
     * 设置Dialong属性
     *
     * @param view
     * @return
     */
    private AlertDialog getDialongView(View view) {
        final AlertDialog.Builder builder6 = new AlertDialog.Builder(LoginActivity.this);
        builder6.setView(view);
        builder6.create();
        AlertDialog dialog = builder6.show();

        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);


//        Window window = getWindow();
//        window.getDecorView().setPadding(0, 0, 0, 0);
//        WindowManager.LayoutParams lp = window.getAttributes();
//        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        lp.gravity = Gravity.CENTER;
//        window.setAttributes(layoutParams);


        return dialog;
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

            loadingDialog.dismiss();

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
                            Tools.Toast(LoginActivity.this, "登陆成功");


                            SharePreferencesUtil.putStringValue(LoginActivity.this, UserInfoConstant.userName, edtUsername);
                            SharePreferencesUtil.putStringValue(LoginActivity.this, UserInfoConstant.passWord, edtPwd);
                            SharePreferencesUtil.putBooleanValue(LoginActivity.this, UserInfoConstant.Flag, true);

                            Tools.Intent(LoginActivity.this, MainActivity.class);


                        } else {
                            //登陆成功
                            Tools.Toast(LoginActivity.this, message);
                        }


                        break;

                    case 101:
                        Tools.Toast(LoginActivity.this, "登陆失败，异常Log：\n" + data);
                        break;


                    default:
                        break;

                }
            } catch (JSONException e) {
                e.printStackTrace();
                LogUtil.showLog("LoginActivity JSON failed --->", e.toString());
                Tools.Toast(LoginActivity.this, "失败，JSON解析异常，异常Log：\n" + data);
            }

        }


    };

}
