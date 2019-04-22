package com.example.yzwy.lprmag.control.activityStackExtends.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.yzwy.lprmag.control.activityStackExtends.util.ActivityStackManager;

public class BaseAppCompatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStackManager.getInstance().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /**
         *
         * */
        ActivityStackManager.getInstance().finishActivity(this);
    }
}
