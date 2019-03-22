package com.example.yzwy.lprmag;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

                break;

            /**
             * 登录按钮
             * */
            case R.id.btn_login_lgn:

                break;

        }
    }


}
