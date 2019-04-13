package com.example.yzwy.lprmag.control;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.yzwy.lprmag.util.ActivityStackManager;

public class BaseActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStackManager.getInstance().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityStackManager.getInstance().finishActivity(this);
    }
}
