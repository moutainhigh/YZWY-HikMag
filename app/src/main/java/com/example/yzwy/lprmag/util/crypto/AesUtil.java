//package com.example.yzwy.lprmag.util.crypto;
//
//import android.annotation.SuppressLint;
//import android.util.Base64;
//
//import java.io.UnsupportedEncodingException;
//import java.security.InvalidKeyException;
//import java.security.NoSuchAlgorithmException;
//import java.security.SecureRandom;
//
//import javax.crypto.BadPaddingException;
//import javax.crypto.Cipher;
//import javax.crypto.IllegalBlockSizeException;
//import javax.crypto.KeyGenerator;
//import javax.crypto.NoSuchPaddingException;
//import javax.crypto.SecretKey;
//import javax.crypto.spec.SecretKeySpec;
//
///**
// * #################################################################################################
// * Copyright: Copyright (c) 2018
// * Created on 2019-04-08
// * Author: 仲超(zhongchao)
// * Version 1.0
// * Describe: Aes工具类
// * $$数据的安全是非常重要的，现在无论干什么都要账号和密码，一旦账号与密码泄露出去必将造成财产的损失，所以做好数据保密是非常重要的。
// * Android加密算法有多种多样，常见的有MD5、RSA、AES、3DES四种。
// * $$AES是设计用来替换DES的高级加密算法
// * $$AES算法是可逆算法，支持对加密字符串进行解密，前提是解密时密钥必须与加密时一致。
// * #################################################################################################
// */
//@SuppressLint("TrulyRandom")
//public class AesUtil {
//
//    private final static String HEX = "0123456789ABCDEF";
//
//
//    public static final String BASE_ALGORITHM = "AES";
//    public static final String BASE_ENCODING = "UTF-8";
//    public static final String TRANSFORM_ECB_NO_PADDING = "AES/ECB/NoPadding";
//    public static final String TRANSFORM_ECB_PKCS5PADDING = "AES/ECB/PKCS5Padding";
//    public static final String TRANSFORM_CBC_NO_PADDING = "AES/CBC/NoPadding";
//    public static final String TRANSFORM_CBC_PKCS5PADDING = "AES/CBC/PKCS5Padding";
//    private static final String TAG_ECB_ECRYPT = "ECB ENCRYPT";
//    private static final String TAG_ECB_DECRYPT = "ECB DECRYPT";
//    private static final String TAG_CBC_ENCRYPT = "CBC ENCRYPT";
//    private static final String TAG_CBC_DECRYPT = "CBC DECRYPT";
//
//    /**
//     * =============================================================================================
//     * 加密
//     *
//     * @param key 密钥
//     * @param src 加密文本
//     * @return
//     * @throws Exception
//     */
//    //加密函数，key为密钥
//    public static String encryptHexStr(String key, String src, String transform) throws Exception {
//        //byte[] rawKey = getRawKey(key.getBytes());
//        byte[] result = encryptByte(key.getBytes(BASE_ENCODING), src.getBytes(BASE_ENCODING), transform);
//        return toHex(result);
//    }
//
//    /**
//     * =============================================================================================
//     * 加密
//     *
//     * @param key 密钥
//     * @param src 加密文本
//     * @return
//     * @throws Exception
//     */
//    //加密函数，key为密钥
//    public static String encryptBase64(String key, String src, String transform) throws Exception {
//        //byte[] rawKey = getRawKey(key.getBytes());
//        byte[] encrypted = encryptByte(key.getBytes(BASE_ENCODING), src.getBytes(BASE_ENCODING), transform);
//        byte[] encoded = Base64.encode(encrypted, Base64.DEFAULT);
//        return new String(encoded);
//    }
//
//    /**
//     * =============================================================================================
//     * 解密
//     *
//     * @param key       密钥
//     * @param encrypted 待揭秘文本
//     * @return
//     * @throws Exception
//     */
//    //解密函数。key值必须和加密时的key一致
//    public static String decryptStr(String key, String encrypted, String transform) throws Exception {
//        //byte[] rawKey = getRawKey(key.getBytes());
//        byte[] enc = toByte(encrypted);
//        byte[] result = decryptByte(key.getBytes(BASE_ENCODING), enc, transform);
//        return new String(result);
//    }
//
//
//    /**
//     * =============================================================================================
//     * 获取256位的加密密钥
//     *
//     * @param seed
//     * @return
//     * @throws Exception
//     */
//    @SuppressLint("TrulyRandom")
//    private static byte[] getRawKey(byte[] seed) throws Exception {
//        KeyGenerator kgen = KeyGenerator.getInstance(BASE_ALGORITHM);
//        // SHA1PRNG 强随机种子算法, 要区别Android 4.2.2以上版本的调用方法
//        SecureRandom sr = null;
//        if (android.os.Build.VERSION.SDK_INT >= 17) {
//            sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
//        } else {
//            sr = SecureRandom.getInstance("SHA1PRNG");
//        }
//
//        sr.setSeed(seed);
//        kgen.init(128, sr); // 256位或128位或192位
//        SecretKey skey = kgen.generateKey();
//        byte[] raw = skey.getEncoded();
//        return raw;
//    }
//
//    /**
//     * =============================================================================================
//     * 获取256位的加密密钥
//     *
//     * @param seed
//     * @return
//     * @throws Exception
//     */
//    @SuppressLint("TrulyRandom")
//    public static byte[] getRawKey(byte[] seed, int keysize) throws Exception {
//        KeyGenerator kgen = KeyGenerator.getInstance(BASE_ALGORITHM);
//        // SHA1PRNG 强随机种子算法, 要区别Android 4.2.2以上版本的调用方法
//        SecureRandom sr = null;
//        if (android.os.Build.VERSION.SDK_INT >= 17) {
//            sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
//        } else {
//            sr = SecureRandom.getInstance("SHA1PRNG");
//        }
//
//        sr.setSeed(seed);
//        kgen.init(keysize, sr); // 256位或128位或192位
//        SecretKey skey = kgen.generateKey();
//        byte[] raw = skey.getEncoded();
//        return raw;
//    }
//
//
//    /**
//     * =============================================================================================
//     * 真正的加密过程
//     *
//     * @param key
//     * @param src
//     * @return
//     * @throws Exception
//     */
//    private static byte[] encryptByte(byte[] key, byte[] src, String transform) throws Exception {
//        SecretKeySpec skeySpec = new SecretKeySpec(key, BASE_ALGORITHM);
//        // 创建密码器
//        //Cipher cipher = Cipher.getInstance(BASE_ALGORITHM);
//        Cipher cipher = Cipher.getInstance(transform);
//        // 初始化为加密模式的密码器
//        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
//        // 加密
//        byte[] encrypted = cipher.doFinal(src);
//        return encrypted;
//    }
//
//    /**
//     * =============================================================================================
//     * 真正的解密过程
//     *
//     * @param key
//     * @param encrypted
//     * @return
//     * @throws Exception
//     */
//    private static byte[] decryptByte(byte[] key, byte[] encrypted, String transform) throws Exception {
//        SecretKeySpec skeySpec = new SecretKeySpec(key, BASE_ALGORITHM);
//        Cipher cipher = Cipher.getInstance(transform);
//        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
//        byte[] decrypted = cipher.doFinal(encrypted);
//        return decrypted;
//    }
//
//
//    /**
//     * $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
//     * 工具类开始
//     * =============================================================================================
//     */
//
//    public static String toHex(String txt) {
//        return toHex(txt.getBytes());
//    }
//
//    public static String fromHex(String hex) {
//        return new String(toByte(hex));
//    }
//
//    private static byte[] toByte(String hexString) {
//        int len = hexString.length() / 2;
//        byte[] result = new byte[len];
//        for (int i = 0; i < len; i++) {
//            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
//        }
//        return result;
//    }
//
//    private static String toHex(byte[] buf) {
//        if (buf == null) {
//            return "";
//        }
//        StringBuffer result = new StringBuffer(2 * buf.length);
//        for (int i = 0; i < buf.length; i++) {
//            appendHex(result, buf[i]);
//        }
//        return result.toString();
//    }
//
//
//    private static void appendHex(StringBuffer sb, byte b) {
//        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
//    }
//
//
////
////
////    /**
////     * @param key       {@link String}
////     * @param value     {@link String}
////     * @param transform Select one {@link String} form above
////     * @return ECB encrypted {@link String}
////     * @throws UnsupportedEncodingException
////     * @throws NoSuchPaddingException
////     * @throws NoSuchAlgorithmException
////     * @throws InvalidKeyException
////     * @throws BadPaddingException
////     * @throws IllegalBlockSizeException
////     */
////    public static byte[] encryptECB_Byte(String key, String value, String transform) throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
////
////        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), BASE_ALGORITHM);
////        // 创建密码器
////        //Cipher cipher = Cipher.getInstance(BASE_ALGORITHM);
////        Cipher cipher = Cipher.getInstance(BASE_ALGORITHM);
////        // 初始化为加密模式的密码器
////        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
////        // 加密
////        byte[] encrypted = cipher.doFinal(value.getBytes());
////        return encrypted;
////
////
////    }
////
////    /**
////     * @param key       {@link String}
////     * @param value     {@link String}
////     * @param transform Select one {@link String} form above
////     * @return ECB encrypted {@link String}
////     * @throws UnsupportedEncodingException
////     * @throws NoSuchPaddingException
////     * @throws NoSuchAlgorithmException
////     * @throws InvalidKeyException
////     * @throws BadPaddingException
////     * @throws IllegalBlockSizeException
////     */
////    public static String encryptECBBase64(String key, String value, String transform) throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
////        byte[] encrypted = encryptECB_Byte(key, value, transform);
////        byte[] encoded = Base64.encode(encrypted, Base64.DEFAULT);
////        System.out.println("ENCRYPTED: " + new String(encoded));
////        return new String(encoded);
////    }
////
////
////    /**
////     * @param key       {@link String}
////     * @param value     {@link String}
////     * @param transform Select one {@link String} form above
////     * @return ECB encrypted {@link String}
////     * @throws UnsupportedEncodingException
////     * @throws NoSuchPaddingException
////     * @throws NoSuchAlgorithmException
////     * @throws InvalidKeyException
////     * @throws BadPaddingException
////     * @throws IllegalBlockSizeException
////     */
////    public static String encryptECB_HexStr(String key, String value, String transform) throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
////        byte[] encrypted = encryptECB_Byte(key, value, transform);
////        return ByteToHexString(encrypted);
////    }
////
////
////    /**
////     * =============================================================================================
////     * 字节数组转成16进制表示格式的字符串
////     *
////     * @param byteArray 要转换的字节数组
////     * @return 16进制表示格式的字符串
////     **/
////    public static String ByteToHexString(byte[] byteArray) {
////        if (byteArray == null || byteArray.length < 1)
////            throw new IllegalArgumentException("this byteArray must not be null or empty");
////
////        final StringBuilder hexString = new StringBuilder();
////        for (int i = 0; i < byteArray.length; i++) {
////            if ((byteArray[i] & 0xff) < 0x10)//0~F前面不零
////                hexString.append("0");
////            hexString.append(Integer.ByteToHexString(0xFF & byteArray[i]));
////        }
////        return hexString.toString().toLowerCase();
////    }
//
///**
// * $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
// * 工具类结束
// * =============================================================================================
// * */
//}
