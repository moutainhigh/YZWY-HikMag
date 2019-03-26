package com.example.yzwy.lprmag;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.yzwy.lprmag.util.Tools;

/**
 * 登录程序
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edt_username_lgn;
    private EditText edt_pwd_lgn;

    private Button btn_clode_lgn;
    private Button btn_forget_lgn;
    private Button btn_login_lgn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_login);


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

                String edtUsername = edt_username_lgn.getText().toString();
                String edtPwd = edt_pwd_lgn.getText().toString();

                if (edtUsername.equals("")) {
                    Tools.Toast(LoginActivity.this, "用户名不能为空");
                } else if (edtPwd.equals("")) {
                    Tools.Toast(LoginActivity.this, "密码不能为空");
                } else if (edtUsername.equals("admin") && edtPwd.equals("admin123")) {
                    //登陆成功
                    Tools.Toast(LoginActivity.this, "登陆成功");
                } else {
                    //登录失败
                    Tools.Toast(LoginActivity.this, "登陆失败");
                }


                break;

        }
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

}
