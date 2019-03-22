package com.example.yzwy.lprmag;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.yzwy.lprmag.guide.animation.guide.GuideActivity;
import com.example.yzwy.lprmag.util.Tools;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends AppCompatActivity {

    public static SharedPreferences sp;
    SharedPreferences.Editor ed;
    boolean bl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_welcome);

        sp = getPreferences(Activity.MODE_PRIVATE);
        ed = sp.edit();
        bl = sp.getBoolean("FIRSTINSTALLAPP", false);
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (!bl) {
                    ed.putBoolean("FIRSTINSTALLAPP", true);
                    ed.commit();
                    Tools.Intent(WelcomeActivity.this, GuideActivity.class);
                } else {
                    Tools.Intent(WelcomeActivity.this, HiKCameraActivity.class);
                }
            }
        };
        timer.schedule(task, 1000 * 3);
    }
}
