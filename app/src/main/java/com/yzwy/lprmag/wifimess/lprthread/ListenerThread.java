package com.yzwy.lprmag.wifimess.lprthread;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.yzwy.lprmag.dialog.DialogPerset;
import com.yzwy.lprmag.dialog.LprDialog;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 监听线程
 * Created by syhuang on 2016/9/7.
 */
public class ListenerThread extends Thread {

    private ServerSocket serverSocket = null;
    private Handler handler;
    private int port;
    private Socket socket;

    public ListenerThread(int port, Handler handler) {
        setName("ListenerThread");
        this.port = port;
        this.handler = handler;
        try {
            serverSocket = new ServerSocket(port);//监听本机的12345端口
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        while (true) {
            try {
                //中断处理逻辑
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("DiaLogPersetActivity   111" + "The thread is interrupted!");
                    break;
                }
                Log.i("ListennerThread", "阻塞");
                //阻塞，等待设备连接
                if (serverSocket != null)
                    socket = serverSocket.accept();
                Message message = Message.obtain();
                message.what = LprDialog.DEVICE_CONNECTING;
                handler.sendMessage(message);
            } catch (IOException e) {
                Log.i("ListennerThread", "error:" + e.getMessage());
                e.printStackTrace();
                //中断状态在抛出异常前，被清除掉，因此在此处重置中断状态
                Thread.currentThread().interrupt();
            }
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public void close() {
        try {
            if (serverSocket != null)
                serverSocket.close();
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
