package com.example.yzwy.lprmag.util;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class HanderUtil {

    /**
     * =============================================================================================
     * 发消息
     *
     * @param handler
     * @param what
     * @param Val
     */
    public static void HanderMsgSend(Handler handler, int what, String Val) {
        Message message = new Message();
        message.what = what;
        Bundle bundle = new Bundle();
        bundle.putString("data", Val);
        message.setData(bundle);
        handler.sendMessage(message);
    }

    /**
     * =============================================================================================
     * 发消息
     *
     * @param handler
     * @param what
     * @param Val
     */
    public static void HanderMsgSend(Handler handler, int what, String key, String Val) {
        Message message = new Message();
        message.what = what;
        Bundle bundle = new Bundle();
        bundle.putString(key, Val);
        message.setData(bundle);
        handler.sendMessage(message);
    }

}
