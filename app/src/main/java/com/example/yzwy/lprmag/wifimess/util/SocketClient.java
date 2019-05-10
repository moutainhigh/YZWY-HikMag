package com.example.yzwy.lprmag.wifimess.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class SocketClient {

    private Socket m_socket;
    private InputStream m_inputstream;
    private OutputStream m_outputstream;
    private BufferedInputStream m_bufferedinputstream;
    private BufferedOutputStream m_bufferedoutputstream;
    private boolean connected;

    public int connect(String host, int port) {
        try {
            SocketAddress socketAddress = new InetSocketAddress(host, port);
            m_socket = new Socket();
            m_socket.connect(socketAddress, 5000);
            m_socket.setSoTimeout(60000);

            m_inputstream = m_socket.getInputStream();
            m_bufferedinputstream = new BufferedInputStream(m_inputstream);
            m_outputstream = m_socket.getOutputStream();
            m_bufferedoutputstream = new BufferedOutputStream(m_outputstream);
        } catch (Exception e) {
            return -1;
        }
        connected = true;
        return 0;
    }

    /**
     * 发送请求数据
     *
     * @param data
     * @return
     */
    public int write(byte data[]) {
        if (data == null || data.length == 0 || !connected) {
            return 0;
        }
        try {
            m_bufferedoutputstream.write(data, 0, data.length);
            m_bufferedoutputstream.flush();
        } catch (Exception e) {
            return -1;
        }
        return 0;
    }

    /**
     * 读取返回数据
     *
     * @return
     */
    public byte[] read() {
        if (!connected) {
            return null;
        }
        try {
            return readStream(m_bufferedinputstream);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param inStream
     * @return 字节数组
     * @throws Exception
     * @功能 读取流
     */
    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        return outSteam.toByteArray();
    }
//
//    public static void test() {
//        SocketClient client = new SocketClient();
//        // 建立socket对象
//        int iret = client.connect("192.168.1.105", 1234);
//        if (iret == 0) {
//            // 发送数据
//            client.write("helloworld".getBytes());
//            // 接收数据
//            byte data[] = client.read();
//            if ((data != null) && (data.length != 0)) {
//                // 处理接收结果
//                System.out.println() ("响应报文字节数组---->" + Arrays.toString(data));
//            }
//        }
//
//    }
}
