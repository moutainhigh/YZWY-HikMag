package com.example.yzwy.lprmag.util;

import com.example.yzwy.lprmag.util.crypto.aes.AES_ECB_PKCS7Padding_128_Key32_Util;

public class AESUtil {


    //这个就是需要被实例化的类
    private volatile static AESUtil aesUtil = null;

    //这里为全局提供访问的节点
    public static AESUtil getInstance() {
        //涉及延时加载，当需要这个类的时候才会被实例化
        if (aesUtil == null) {
            //synchronized (AESUtil.class) {
            //    if (aesUtil == null) {
            aesUtil = new AESUtil();
            //    }
            //}
        }
        return aesUtil;
    }

    static String key = "A1zFlux77a99X1be";

    /**
     * =============================================================================================
     * 解密
     *
     * @param content
     * @return
     */
    public String JieDecrypt(String content){
        try {
        return AES_ECB_PKCS7Padding_128_Key32_Util.aesDecryptHexStr(content, key);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * =============================================================================================
     * 加密
     *
     * @param content
     * @return
     */
    public String JiaEncrypt(String content) {
        try {
            return AES_ECB_PKCS7Padding_128_Key32_Util.aesEncryptHexStr(content, key);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
