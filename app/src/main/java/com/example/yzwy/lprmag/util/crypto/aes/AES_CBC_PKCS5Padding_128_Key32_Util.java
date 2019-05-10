package com.example.yzwy.lprmag.util.crypto.aes;


import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import javax.crypto.spec.IvParameterSpec;

import java.io.UnsupportedEncodingException;

/**
 * AES加解密工具
 * Created by steadyjack on 2018/2/9.
 * <p>
 * ---------------------
 * 作者：debug-steadyjack
 * 来源：CSDN
 * 原文：https://blog.csdn.net/u013871100/article/details/80100992
 * 版权声明：本文为博主原创文章，转载请附上博文链接！
 */
public class AES_CBC_PKCS5Padding_128_Key32_Util {

    private static final String CipherMode = "AES/CBC/PKCS5Padding";

    private static final String SecretKey = "debug";

    private static final Integer IVSize = 16;

    private static final String EncryptAlg = "AES";

    private static final String Encode = "UTF-8";

    private static final int SecretKeySize = 32;

    private static final String Key_Encode = "UTF-8";

    /**
     * 创建密钥
     *
     * @return
     */
    private static SecretKeySpec createKey() {
        StringBuilder sb = new StringBuilder(SecretKeySize);
        sb.append(SecretKey);
        if (sb.length() > SecretKeySize) {
            sb.setLength(SecretKeySize);
        }
        if (sb.length() < SecretKeySize) {
            while (sb.length() < SecretKeySize) {
                sb.append(" ");
            }
        }
        try {
            byte[] data = sb.toString().getBytes(Encode);
            return new SecretKeySpec(data, EncryptAlg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建16位向量: 不够则用0填充
     *
     * @return
     */
    private static IvParameterSpec createIV() {
        StringBuffer sb = new StringBuffer(IVSize);
        sb.append(SecretKey);
        if (sb.length() > IVSize) {
            sb.setLength(IVSize);
        }
        if (sb.length() < IVSize) {
            while (sb.length() < IVSize) {
                sb.append("0");
            }
        }
        byte[] data = null;
        try {
            data = sb.toString().getBytes(Encode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new IvParameterSpec(data);
    }

    /**
     * 加密：有向量16位，结果转base64
     *
     * @param context
     * @return
     */
    public static String encrypt(String context) {
        try {
            byte[] content = context.getBytes(Encode);
            SecretKeySpec key = createKey();
            Cipher cipher = Cipher.getInstance(CipherMode);
            cipher.init(Cipher.ENCRYPT_MODE, key, createIV());
            byte[] data = cipher.doFinal(content);
            String result = Base64.encodeBase64String(data);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     *
     * @param context
     * @return
     */
    public static String decrypt(String context) {
        try {
            byte[] data = Base64.decodeBase64(context);
            SecretKeySpec key = createKey();
            Cipher cipher = Cipher.getInstance(CipherMode);
            cipher.init(Cipher.DECRYPT_MODE, key, createIV());
            byte[] content = cipher.doFinal(data);
            String result = new String(content, Encode);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        //密钥 加密内容(对象序列化后的内容-json格式字符串)
        String content = "{\"domain\":{\"method\":\"getDetails\",\"url\":\"http://www.baidu.com\"},\"name\":\"steadyjack_age\",\"age\":\"23\",\"address\":\"Canada\",\"id\":\"12\",\"phone\":\"15627284601\"}";

        String encryptText = encrypt(content);
        String decryptText = decrypt(encryptText);
        System.out.println(String.format("明文：%s \n加密结果：%s \n解密结果：%s ", content, encryptText, decryptText));

    }
}