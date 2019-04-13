package com.example.yzwy.lprmag.util.crypto;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * #################################################################################################
 * Copyright: Copyright (c) 2018
 * Created on 2019-04-08
 * Author: 仲超(zhongchao)
 * Version 1.0
 * Describe: MD5工具类
 * $$数据的安全是非常重要的，现在无论干什么都要账号和密码，一旦账号与密码泄露出去必将造成财产的损失，所以做好数据保密是非常重要的。
 * Android加密算法有多种多样，常见的有MD5、RSA、AES、3DES四种。
 * $$MD5是不可逆的加密算法，也就是无法解密，主要用于客户端的用户密码加密。
 * #################################################################################################
 */
public class MD5Util {
    /**
     * =============================================================================================
     * 对字符串md5加密(小写+字母) 32位
     *
     * @param str 传入要加密的字符串
     * @return MD5加密后的字符串
     */
    public static String MD5_Lowercase_letter_32(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * =============================================================================================
     * 对字符串md5加密(大写+数字) 32位
     *
     * @param s
     * @return MD5加密后的字符串
     */

    public static String MD5_Capital_letter_32(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * =============================================================================================
     * 对字符串md5加密(小写+字母)  16位
     *
     * @param str 传入要加密的字符串
     * @return MD5加密后的字符串
     */
    public static String MD5_Lowercase_letter_16(String str) {
        if (str == null) {
            return null;
        }
        return MD5_Lowercase_letter_32(str).substring(8, 24);
    }

    /**
     * =============================================================================================
     * 对字符串md5加密(大写+字母)  16位
     *
     * @param str 传入要加密的字符串
     * @return MD5加密后的字符串
     */
    public static String MD5_Capital_letter_16(String str) {
        if (str == null) {
            return null;
        }
        return MD5_Capital_letter_32(str).substring(8, 24);
    }


    /**
     * MD5加盐加密，盐值为 ：私盐+公盐
     *
     * @param Public_salt  公盐
     * @param Str          密码
     * @param Private_Salt 私盐
     * @return MD5 大写加字母32加密字符串
     */
    public static String Salt_MD5_Capital_letter_32(String Public_salt, String Str, String Private_Salt) {
        return MD5_Capital_letter_32(Public_salt + Str + Private_Salt);
    }

    /**
     * MD5加盐加密，盐值为 ：私盐+公盐
     *
     * @param Public_salt  公盐
     * @param Str          密码
     * @param Private_Salt 私盐
     * @return MD5 小写加字母32加密字符串
     */
    public static String Salt_MD5_Lowercase_letter_32(String Public_salt, String Str, String Private_Salt) {
        return MD5_Lowercase_letter_32(Public_salt + Str + Private_Salt);
    }


    /**
     * MD5加盐加密，盐值为 ：私盐+公盐
     *
     * @param Public_salt  公盐
     * @param Str          密码
     * @param Private_Salt 私盐
     * @return MD5 大写加字母16加密字符串
     */
    public static String Salt_MD5_Capital_letter_16(String Public_salt, String Str, String Private_Salt) {
        return MD5_Capital_letter_16(Public_salt + Str + Private_Salt);
    }

    /**
     * MD5加盐加密，盐值为 ：私盐+公盐
     *
     * @param Public_salt  公盐
     * @param Str          密码
     * @param Private_Salt 私盐
     * @return MD5 小写加字母16加密字符串
     */
    public static String Salt_MD5_Lowercase_letter_16(String Public_salt, String Str, String Private_Salt) {
        return MD5_Lowercase_letter_16(Public_salt + Str + Private_Salt);
    }


    //首先初始化一个MessageDigest对象，该对象通过update方法获取原始数据，
    //并调用digest方法完成哈希计算，然后把字节数组逐位转换为十六进制数，最后拼装加密字符串
    public static String encrypBy(String raw) {
        String md5Str = raw;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(raw.getBytes());
            byte[] encryContext = md.digest();

            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < encryContext.length; offset++) {
                i = encryContext[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            md5Str = buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5Str;
    }


}
