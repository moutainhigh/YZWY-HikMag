package com.example.yzwy.lprmag;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.yzwy.lprmag.myConstant.CustomServiceConstant;
import com.example.yzwy.lprmag.util.SharePreferencesUtil;

/**
 * #################################################################################################
 * Copyright: Copyright (c) 2018
 * Created on 2019-04-03
 * Author: 仲超(zhongchao)
 * Version 1.0
 * Describe: 客服中心页面
 * #################################################################################################
 */
public class CustomServiceActivity extends AppCompatActivity {

    /**
     * 关闭页面
     */
    private ImageButton imgbtn_back_close;
    private TextView tv_customerService_cusservice;
    private TextView tv_technicalSupport_cusservice;
    private TextView tv_troubleShooting_cusservice;


    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ly_cusservice);

        /**
         * 初始化组件
         * */
        initView();


    }

    /**
     * =============================================================================================
     * 初始化组件
     */
    public void initView() {

        imgbtn_back_close = (ImageButton) findViewById(R.id.imgbtn_left_cusservice);
        tv_customerService_cusservice = (TextView) findViewById(R.id.tv_customerService_cusservice);
        tv_technicalSupport_cusservice = (TextView) findViewById(R.id.tv_technicalSupport_cusservice);
        tv_troubleShooting_cusservice = (TextView) findViewById(R.id.tv_troubleShooting_cusservice);

        InitSpData();

        /**
         * 按钮事件
         * */
        initClick();
    }

    /**
     * =============================================================================================
     * 设置缓存值
     */
    private void InitSpData() {

        if (SharePreferencesUtil.getBooleanValue(CustomServiceActivity.this, CustomServiceConstant.CustomService_MODEL, CustomServiceConstant.CustomService_MODEL_bool_default)) {
            tv_customerService_cusservice.setText(SharePreferencesUtil.getStringValue(CustomServiceActivity.this, CustomServiceConstant.CustomService_customerService_str, CustomServiceConstant.CustomService_customerService_str_default));
            tv_technicalSupport_cusservice.setText(SharePreferencesUtil.getStringValue(CustomServiceActivity.this, CustomServiceConstant.CustomService_technicalSupport_str, CustomServiceConstant.CustomService_technicalSupport_str_default));
            tv_troubleShooting_cusservice.setText(SharePreferencesUtil.getStringValue(CustomServiceActivity.this, CustomServiceConstant.CustomService_troubleShooting_str, CustomServiceConstant.CustomService_troubleShooting_str_default));
        }


    }


    /**
     * =============================================================================================
     * 加载事件监听器
     */
    public void initClick() {

        imgbtn_back_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });

    }

    /**
     * =============================================================================================
     * 活动销毁活动生命周期
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //结束当前活动
        this.finish();
    }

    /**
     * =============================================================================================
     * 单击退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
        }
        return false;
    }

    private void exit() {
        //结束当前活动
        finish();
    }


}
