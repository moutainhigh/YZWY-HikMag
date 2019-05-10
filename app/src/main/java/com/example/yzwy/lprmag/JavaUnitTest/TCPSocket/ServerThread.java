package com.example.yzwy.lprmag.JavaUnitTest.TCPSocket;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/*
 * 服务器线程处理类
 */
public class ServerThread extends Thread {
    // 和本线程相关的Socket
    Socket socket = null;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    //线程执行的操作，响应客户端的请求
    public void run(){
        InputStream is=null;
        InputStreamReader isr=null;
        BufferedReader br=null;
        OutputStream os=null;
        PrintWriter pw=null;
        try {
            //获取输入流，并读取客户端信息
            is = socket.getInputStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            String info=null;
            while((info=br.readLine())!=null){//循环读取客户端的信息
                System.out.println("我是服务器，客户端说："+info);
            }
            socket.shutdownInput();//关闭输入流
            //获取输出流，响应客户端的请求
            os = socket.getOutputStream();
            pw = new PrintWriter(os);
            pw.write("\"{\\\"errcode\\\":\\\"0\\\",\\\"errmsg\\\":\\\"0\\\",\\\"Order\\\":\\\"0\\\",\\\"time\\\":\\\"2019-05-03 18:55:57\\\",\\\"TerminalPriority\\\":\\\"\\\",\\\"data\\\":[{\\\"GeomagnetismAddressNumber\\\":\\\"01000001\\\",\\\"RK3399AddressNumber\\\":\\\"\\\",\\\"PersetNumber\\\":\\\"1\\\",\\\"ScaleWidth\\\":\\\"7.68\\\",\\\"ScaleHeight\\\":\\\"9.0\\\"},{\\\"GeomagnetismAddressNumber\\\":\\\"01000001\\\",\\\"RK3399AddressNumber\\\":\\\"\\\",\\\"PersetNumber\\\":\\\"2\\\",\\\"ScaleWidth\\\":\\\"7.68\\\",\\\"ScaleHeight\\\":\\\"9.0\\\"},{\\\"GeomagnetismAddressNumber\\\":\\\"82288228\\\",\\\"RK3399AddressNumber\\\":\\\"\\\",\\\"PersetNumber\\\":\\\"3\\\",\\\"ScaleWidth\\\":\\\"7.68\\\",\\\"ScaleHeight\\\":\\\"9.0\\\"},{\\\"GeomagnetismAddressNumber\\\":\\\"82288228\\\",\\\"RK3399AddressNumber\\\":\\\"\\\",\\\"PersetNumber\\\":\\\"6\\\",\\\"ScaleWidth\\\":\\\"7.68\\\",\\\"ScaleHeight\\\":\\\"9.0\\\"},{\\\"GeomagnetismAddressNumber\\\":\\\"82255228\\\",\\\"RK3399AddressNumber\\\":\\\"\\\",\\\"PersetNumber\\\":\\\"4\\\",\\\"ScaleWidth\\\":\\\"7.68\\\",\\\"ScaleHeight\\\":\\\"9.0\\\"},{\\\"GeomagnetismAddressNumber\\\":\\\"82556228\\\",\\\"RK3399AddressNumber\\\":\\\"\\\",\\\"PersetNumber\\\":\\\"8\\\",\\\"ScaleWidth\\\":\\\"7.68\\\",\\\"ScaleHeight\\\":\\\"9.0\\\"},{\\\"GeomagnetismAddressNumber\\\":\\\"82512228\\\",\\\"RK3399AddressNumber\\\":\\\"\\\",\\\"PersetNumber\\\":\\\"7\\\",\\\"ScaleWidth\\\":\\\"7.68\\\",\\\"ScaleHeight\\\":\\\"9.0\\\"},{\\\"GeomagnetismAddressNumber\\\":\\\"82545866\\\",\\\"RK3399AddressNumber\\\":\\\"\\\",\\\"PersetNumber\\\":\\\"11\\\",\\\"ScaleWidth\\\":\\\"7.68\\\",\\\"ScaleHeight\\\":\\\"9.0\\\"},{\\\"GeomagnetismAddressNumber\\\":\\\"82545866\\\",\\\"RK3399AddressNumber\\\":\\\"\\\",\\\"PersetNumber\\\":\\\"9\\\",\\\"ScaleWidth\\\":\\\"7.68\\\",\\\"ScaleHeight\\\":\\\"9.0\\\"},{\\\"GeomagnetismAddressNumber\\\":\\\"82545866\\\",\\\"RK3399AddressNumber\\\":\\\"\\\",\\\"PersetNumber\\\":\\\"10\\\",\\\"ScaleWidth\\\":\\\"7.68\\\",\\\"ScaleHeight\\\":\\\"9.0\\\"},{\\\"GeomagnetismAddressNumber\\\":\\\"82545866\\\",\\\"PersetNumber\\\":\\\"11\\\",\\\"ScaleWidth\\\":\\\"1\\\",\\\"ScaleHeight\\\":\\\"1\\\"},{\\\"GeomagnetismAddressNumber\\\":\\\"77747445\\\",\\\"RK3399AddressNumber\\\":\\\"\\\",\\\"PersetNumber\\\":\\\"12\\\",\\\"ScaleWidth\\\":\\\"7.68\\\",\\\"ScaleHeight\\\":\\\"9.0\\\"},{\\\"GeomagnetismAddressNumber\\\":\\\"77747445\\\",\\\"RK3399AddressNumber\\\":\\\"\\\",\\\"PersetNumber\\\":\\\"13\\\",\\\"ScaleWidth\\\":\\\"7.68\\\",\\\"ScaleHeight\\\":\\\"9.0\\\"},{\\\"GeomagnetismAddressNumber\\\":\\\"77747445\\\",\\\"RK3399AddressNumber\\\":\\\"\\\",\\\"PersetNumber\\\":\\\"14\\\",\\\"ScaleWidth\\\":\\\"7.68\\\",\\\"ScaleHeight\\\":\\\"9.0\\\"},{\\\"GeomagnetismAddressNumber\\\":\\\"77747445\\\",\\\"RK3399AddressNumber\\\":\\\"\\\",\\\"PersetNumber\\\":\\\"15\\\",\\\"ScaleWidth\\\":\\\"7.68\\\",\\\"ScaleHeight\\\":\\\"9.0\\\"},{\\\"GeomagnetismAddressNumber\\\":\\\"77747445\\\",\\\"RK3399AddressNumber\\\":\\\"\\\",\\\"PersetNumber\\\":\\\"16\\\",\\\"ScaleWidth\\\":\\\"7.68\\\",\\\"ScaleHeight\\\":\\\"9.0\\\"},{\\\"GeomagnetismAddressNumber\\\":\\\"77747445\\\",\\\"RK3399AddressNumber\\\":\\\"\\\",\\\"PersetNumber\\\":\\\"17\\\",\\\"ScaleWidth\\\":\\\"7.68\\\",\\\"ScaleHeight\\\":\\\"9.0\\\"},{\\\"GeomagnetismAddressNumber\\\":\\\"77747445\\\",\\\"RK3399AddressNumber\\\":\\\"\\\",\\\"PersetNumber\\\":\\\"18\\\",\\\"ScaleWidth\\\":\\\"7.68\\\",\\\"ScaleHeight\\\":\\\"9.0\\\"},{\\\"GeomagnetismAddressNumber\\\":\\\"77747445\\\",\\\"RK3399AddressNumber\\\":\\\"\\\",\\\"PersetNumber\\\":\\\"19\\\",\\\"ScaleWidth\\\":\\\"7.68\\\",\\\"ScaleHeight\\\":\\\"9.0\\\"},{\\\"GeomagnetismAddressNumber\\\":\\\"77747445\\\",\\\"RK3399AddressNumber\\\":\\\"\\\",\\\"PersetNumber\\\":\\\"20\\\",\\\"ScaleWidth\\\":\\\"7.68\\\",\\\"ScaleHeight\\\":\\\"9.0\\\"},{\\\"GeomagnetismAddressNumber\\\":\\\"77747445\\\",\\\"RK3399AddressNumber\\\":\\\"\\\",\\\"PersetNumber\\\":\\\"21\\\",\\\"ScaleWidth\\\":\\\"7.68\\\",\\\"ScaleHeight\\\":\\\"9.0\\\"},{\\\"GeomagnetismAddressNumber\\\":\\\"77747445\\\",\\\"RK3399AddressNumber\\\":\\\"\\\",\\\"PersetNumber\\\":\\\"22\\\",\\\"ScaleWidth\\\":\\\"7.68\\\",\\\"ScaleHeight\\\":\\\"9.0\\\"},{\\\"GeomagnetismAddressNumber\\\":\\\"77747445\\\",\\\"RK3399AddressNumber\\\":\\\"\\\",\\\"PersetNumber\\\":\\\"23\\\",\\\"ScaleWidth\\\":\\\"7.68\\\",\\\"ScaleHeight\\\":\\\"9.0\\\"},{\\\"GeomagnetismAddressNumber\\\":\\\"77747445\\\",\\\"RK3399AddressNumber\\\":\\\"\\\",\\\"PersetNumber\\\":\\\"24\\\",\\\"ScaleWidth\\\":\\\"7.68\\\",\\\"ScaleHeight\\\":\\\"9.0\\\"},{\\\"GeomagnetismAddressNumber\\\":\\\"77747445\\\",\\\"RK3399AddressNumber\\\":\\\"\\\",\\\"PersetNumber\\\":\\\"25\\\",\\\"ScaleWidth\\\":\\\"7.68\\\",\\\"ScaleHeight\\\":\\\"9.0\\\"},{\\\"GeomagnetismAddressNumber\\\":\\\"45565563\\\",\\\"RK3399AddressNumber\\\":\\\"\\\",\\\"PersetNumber\\\":\\\"26\\\",\\\"ScaleWidth\\\":\\\"7.68\\\",\\\"ScaleHeight\\\":\\\"9.0\\\"}]}\"");
            pw.flush();//调用flush()方法将缓冲输出
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            //关闭资源
            try {
                if(pw!=null)
                    pw.close();
                if(os!=null)
                    os.close();
                if(br!=null)
                    br.close();
                if(isr!=null)
                    isr.close();
                if(is!=null)
                    is.close();
                if(socket!=null)
                    socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

