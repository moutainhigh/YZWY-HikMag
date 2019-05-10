package com.example.yzwy.lprmag.JavaUnitTest;

//import com.example.yzwy.lprmag.util.crypto.AesUtil;
import com.example.yzwy.lprmag.util.crypto.Des3Util;
import com.example.yzwy.lprmag.util.crypto.MD5Util;
import com.example.yzwy.lprmag.util.crypto.RSAUtil;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;


public class ApiSign {
    public static void main(String[] args) {
        String Str = "admin123";
        String Public_salt = "-RRVMGIiMU6！8&UaN0—=2uhEaxz@0Ybb";
        String Private_Salt = "coeOI0or—od@&n&！e4+￥Ch5pNVxhVV）0";
        System.out.println("对字符串md5加密(小写+字母) 32位：" + MD5Util.MD5_Lowercase_letter_32(Str));
        System.out.println("对字符串md5加密(大写+数字) 32位：" + MD5Util.MD5_Capital_letter_32(Str));
        System.out.println("====================================================");
        System.out.println("对字符串md5加密(小写+字母) 16位：" + MD5Util.MD5_Lowercase_letter_16(Str));
        System.out.println("对字符串md5加密(大写+数字) 16位：" + MD5Util.MD5_Capital_letter_16(Str));

        System.out.println("****************************************************");

        System.out.println("对字符串md5加密(小写+字母) 32位(加盐)：" + MD5Util.Salt_MD5_Lowercase_letter_32(Public_salt, Str, Private_Salt));
        System.out.println("对字符串md5加密(大写+数字) 32位(加盐)：" + MD5Util.Salt_MD5_Capital_letter_32(Public_salt, Str, Private_Salt));
        System.out.println("====================================================");
        System.out.println("对字符串md5加密(小写+字母) 16位(加盐)：" + MD5Util.Salt_MD5_Lowercase_letter_16(Public_salt, Str, Private_Salt));
        System.out.println("对字符串md5加密(大写+数字) 16位(加盐)：" + MD5Util.Salt_MD5_Capital_letter_16(Public_salt, Str, Private_Salt));


        System.out.println("****************************************************");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("****************************************************");


//        String raw = "{ \"errcode\": 0, \"errmsg\": \"ok\", \"time\": \"2019:03:08 02:03:03\" }";
        String raw = "admin";
//        String raw = "Android安卓";
//        String raw = "111111";
        System.out.println("RSA加密结果是：" + RSAUtil.encodeRSA(null, raw));

//        try {
//            /**
//             * 04-09 16:56:54.274 3765-3765/com.example.yzwy.lprmag I/System.out: mainactivity --->null
//             * 04-09 16:56:54.278 3765-3765/com.example.yzwy.lprmag I/System.out: mainactivity --->7X9S5G1nVLeEXV7UptgHew==
//             * 04-09 16:56:54.278 3765-3765/com.example.yzwy.lprmag I/System.out: mainactivity Str--->ed7f52e46d6754b7845d5ed4a6d8077b
//             * */
////            String seed = "knk%OPq7F…Nk8pzmoUjcb7LeL&q@+flihy8S=qJG8M8+s—R+=bNm%7￥X）L…#YD#G43yh";
////            String seed = "A1zFlux77a99X1be";
//            String seed = "A1zFlux77a99X1be";
//            String enStr = AesUtil.encryptHexStr(seed, raw, AesUtil.TRANSFORM_ECB_PKCS5PADDING);//1D4F8CA100ED403ACD7FDB0588CC8A0D
//            String deStr = AesUtil.decryptStr(seed, enStr, AesUtil.TRANSFORM_ECB_PKCS5PADDING);
//            String desc = String.format("AES加密结果是:%s\nAES解密结果是:%s", enStr, deStr);
//            System.out.println(desc);
//            String seedtest = "123";
//            System.out.println("AES 1234 获取128位的加密密钥：" + new String(AesUtil.getRawKey(seedtest.getBytes(), 128)));
//            System.out.println("AES 1234 获取192位的加密密钥：" + new String(AesUtil.getRawKey(seedtest.getBytes(), 192)));
//            System.out.println("AES 1234 获取256位的加密密钥：" + new String(AesUtil.getRawKey(seedtest.getBytes(), 256)));
//            //System.out.println("AES加密结果是 Base64 ：" + AesUtil.encryptBase64(seed, raw));
//            System.out.println("AES 生成密钥：" + getAESKey());
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("AES加密/解密失败");
//        }


        String key = "a";
        String enStr = Des3Util.encrypt(key, raw);
        String deStr = Des3Util.decrypt(key, enStr);
        String desc = String.format("3DES加密结果是:%s\n3DES解密结果是:%s", enStr, new String(deStr));
        System.out.println(desc);


    }


    /**
     * 生成密钥
     * 自动生成base64 编码后的AES128位密钥
     *
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String getAESKey() throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(128);//要生成多少位，只需要修改这里即可128, 192或256
        SecretKey sk = kg.generateKey();
        byte[] b = sk.getEncoded();

//        return new String(b);
        return parseByte2HexStr(b);
    }


    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
}
