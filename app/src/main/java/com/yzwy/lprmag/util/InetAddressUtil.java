package com.yzwy.lprmag.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * InetAddressUtil
 */
public class InetAddressUtil {


    /**
     * =============================================================================================
     * 获取计算名
     *
     * @return InetAddressUtil
     */
    public static String getHostName() {
        String strAddress = "";
        try {
            InetAddress address = InetAddress.getLocalHost();
            // 获取本机的InetAddress实例
            strAddress = address.getHostName().toString();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return strAddress;
    }

    /**
     * =============================================================================================
     * 获取计IP地址
     *
     * @return InetAddressUtil
     */
    public static String getHostIP() {
        String strAddress = "";
        try {
            InetAddress address = InetAddress.getLocalHost();
            // 获取本机的InetAddress实例
            strAddress = address.getHostAddress().toString();
        } catch (UnknownHostException e) {
            e.printStackTrace();

            return e.toString();
        }
        return strAddress;
    }

    /**
     * =============================================================================================
     * 获取计算名
     *
     * @return InetAddressUtil
     */
    public static byte[] getHostByteIP() {
        byte[] bytes = null;
        try {
            InetAddress address = InetAddress.getLocalHost();
            byte[] bytes2 = address.getAddress();// 获取字节数组形式的IP地址
            // 获取本机的InetAddress实例
            bytes = bytes2;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public static String getIP(){

        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
                {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address))
                    {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        }
        catch (SocketException ex){
            ex.printStackTrace();
        }
        return null;
    }
}
