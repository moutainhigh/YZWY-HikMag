package com.example.yzwy.lprmag.wifimess.client;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import android.util.Log;

public class MinaClientHandler extends IoHandlerAdapter {

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		Log.i("TEST", "客户端发生异常");
		super.exceptionCaught(session, cause);
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		String msg = message.toString();
		Log.i("TEST", "i客户端接收到的信息为:" + msg);
		super.messageReceived(session, message);
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		// TODO Auto-generated method stub
		super.messageSent(session, message);
	}
}
