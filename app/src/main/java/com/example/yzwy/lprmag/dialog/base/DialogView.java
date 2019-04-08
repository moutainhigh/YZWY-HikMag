package com.example.yzwy.lprmag.dialog.base;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class DialogView {

    /**
     * =============================================================================================
     * 设置Dialong属性
     *
     * @param view
     * @return
     */
    public static AlertDialog getDialongView(Context context, View view) {
        final AlertDialog.Builder builder6 = new AlertDialog.Builder(context);
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
