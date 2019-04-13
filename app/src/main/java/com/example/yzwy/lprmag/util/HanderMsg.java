package com.example.yzwy.lprmag.util;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class HanderMsg {

    /**
     * =============================================================================================
     * 发消息
     *
     * @param handler
     * @param what
     * @param Val
     */
    public static void HanderMsgSend(Handler handler, int what, String Val) {
        Message message_Login_Success = new Message();
        message_Login_Success.what = what;
        Bundle bundle = new Bundle();
        bundle.putString("data", Val);
        message_Login_Success.setData(bundle);
        handler.sendMessage(message_Login_Success);
    }

}
