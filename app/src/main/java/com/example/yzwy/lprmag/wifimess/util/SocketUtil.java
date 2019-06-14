package com.example.yzwy.lprmag.wifimess.util;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.yzwy.lprmag.util.LogUtil;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 使用JavaSocket编写发送TCP请求的工具类
 */
public class SocketUtil {


    //    private static String line;
    Socket socket = null;
//    String ip = "192.168.10.107";
//    int port = 6000;


    private InputStream inputStream;
    private OutputStream outputStream;


    //IP地址
    private String address;
    //端口
    private int port;
    //发送内容
    private String msg;
    private Handler mHandler;


    //这个就是需要被实例化的类
    private volatile static SocketUtil socketUtil = null;
    private String Res;

    //这里为全局提供访问的节点
    public static SocketUtil getInstance() {
        //涉及延时加载，当需要这个类的时候才会被实例化
        if (socketUtil == null) {
            synchronized (SocketUtil.class) {
                if (socketUtil == null) {
                    socketUtil = new SocketUtil();
                }
            }
        }
        return socketUtil;
    }

    /**
     * =============================================================================================
     * 请求TCP socket
     *
     * @param address
     * @param port    端口号
     * @param msg
     * @return
     * @throws IOException IO异常
     */
    public String SocketRequest(String address, int port, String msg) throws IOException {

        StringBuffer sb = null;
        try {
            //1.创建客户端Socket，指定服务器地址和端口
            Socket socket = new Socket(address, port);
            //2.获取输出流，向服务器端发送信息
            OutputStream os = socket.getOutputStream();//字节输出流
            PrintWriter pw = new PrintWriter(os);//将输出流包装为打印流
            pw.write(msg);
            pw.flush();
            socket.shutdownOutput();//关闭输出流
            //3.获取输入流，并读取服务器端的响应信息
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String info = null;
            sb = new StringBuffer();
            while ((info = br.readLine()) != null) {
                sb.append(info);
                //System.out.println("我是客户端，服务器说：" + info);
            }
            //4.关闭资源
            br.close();
            is.close();
            pw.close();
            os.close();
            socket.close();


//        if(sb.toString() != null){
            Log.i("SocketUtil  ASYN", "接收到的数据为---->：\n" + new String(sb.toString()));
//        }

            return sb.toString();

            //sendMsgmHandler(0, sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

//        if(sb.toString() != null){
//            Log.i("SocketUtil  ASYN", "接收到的数据为---->：\n" + new String(sb.toString()));
//        }

        return "";
    }
//    /**
//     * =============================================================================================
//     * 请求TCP socket
//     */
//
//    public String SocketRequest(final String IP, final int port, final String str) throws IOException {
//        return SocketRequestRT(IP, port, str);
//    }


    /**
     * 发送消息
     *
     * @param what
     * @param object
     */
    private void sendMsgmHandler(int what, Object object) {
        Message msg = new Message();
        msg.what = what;
        msg.obj = object;
        mHandler.sendMessage(msg);
    }


    /**
     * =============================================================================================
     * 请求TCP socket
     *
     * @param IP   IP地址
     * @param port 端口号
     * @param str  字符消息
     * @return
     * @throws IOException IO异常
     */
    public String SocketRequestRT(final String IP, final int port, final String str) throws IOException {

        byte[] res = null;
        socket = new Socket(IP, port);

        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();

        outputStream.write(str.getBytes());
        outputStream.flush();
        socket.shutdownOutput();//关闭输出流

//##################################################################################################
//方法1
//        byte[] buffer = new byte[1024 * 1024];
//
//        int bytes = 0;
//        do {
//            bytes = inputStream.read(buffer);
//            //outputStream.write(buffer, 0, bytes);
//        } while (inputStream.available() != 0);

//##################################################################################################
//方法2
//==================================================================================================
//        try {
//            byte[] bytes1 = readStream(inputStream);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//==================================================================================================
//        int bytes;
//        //读取数据
        //bytes = inputStream.read(buffer);


//##################################################################################################
//方法3
        int count = 0;
        while (count == 0) {
            count = inputStream.available();
        }
        byte[] buffer = new byte[count];
        int bytes = inputStream.read(buffer);


        if (bytes > 0) {
            final byte[] data = new byte[bytes];
            System.arraycopy(buffer, 0, data, 0, bytes);
            Res = new String(data);
            Log.i("SocketUtil  RT", "接收到的数据为---->：\n" + new String(data));

            inputStream.close();
            outputStream.close();
            socket.close();
        }


        return Res;

    }


}