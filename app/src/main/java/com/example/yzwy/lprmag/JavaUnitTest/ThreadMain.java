package com.example.yzwy.lprmag.JavaUnitTest;

//import com.example.yzwy.lprmag.util.crypto.AesUtil;

import com.example.yzwy.lprmag.util.crypto.Des3Util;
import com.example.yzwy.lprmag.util.crypto.MD5Util;
import com.example.yzwy.lprmag.util.crypto.RSAUtil;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;


public class ThreadMain {
    private static Thread mThread;
    private static boolean isStopThread = false;

    public static void main(String[] args) {


        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    //执行操作
                    if (isStopThread) {

                        try {
                            Thread.sleep(1000 * 1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        System.out.println("***************************");

                        break;
                    }
                }
            }
        });
        mThread.start();

        try {
            Thread.sleep(1000 * 2);
            isStopThread = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
