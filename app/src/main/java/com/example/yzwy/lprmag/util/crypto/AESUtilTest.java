package com.example.yzwy.lprmag.util.crypto;

//public class AESUtilTest {
//}


//import java.io.UnsupportedEncodingException;
//        import java.security.InvalidAlgorithmParameterException;
//        import java.security.InvalidKeyException;
//        import java.security.NoSuchAlgorithmException;
//
//        import javax.crypto.BadPaddingException;
//        import javax.crypto.Cipher;
//        import javax.crypto.IllegalBlockSizeException;
//        import javax.crypto.NoSuchPaddingException;
//        import javax.crypto.spec.IvParameterSpec;
//        import javax.crypto.spec.SecretKeySpec;


import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * by y on 22/11/2017.
 */

public class AESUtilTest {
//    public static final String VIPARA = "9769475569322011";
//    public static final String bm = "utf-8";
//
//    /**
//     * 字节数组转化为大写16进制字符串
//     *
//     * @param b
//     * @return
//     */
//    private static String byte2HexStr(byte[] b) {
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < b.length; i++) {
//            String s = Integer.toHexString(b[i] & 0xFF);
//            if (s.length() == 1) {
//                sb.append("0");
//            }
//
//            sb.append(s.toUpperCase());
//        }
//
//        return sb.toString();
//    }
//
//    /**
//     * 16进制字符串转字节数组
//     *
//     * @param s
//     * @return
//     */
//    private static byte[] str2ByteArray(String s) {
//        int byteArrayLength = s.length() / 2;
//        byte[] b = new byte[byteArrayLength];
//        for (int i = 0; i < byteArrayLength; i++) {
//            byte b0 = (byte) Integer.valueOf(s.substring(i * 2, i * 2 + 2), 16)
//                    .intValue();
//            b[i] = b0;
//        }
//
//        return b;
//    }
//
//
//    /**
//     * AES 加密
//     *
//     * @param content
//     *            明文
//     * @param password
//     *            生成秘钥的关键字
//     * @return
//     */
//
//    public static String aesEncrypt(String content, String password) {
//        try {
//            IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes());
//            SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");
//            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//            cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
//            byte[] encryptedData = cipher.doFinal(content.getBytes(bm));
//
//            return byte2HexStr(encryptedData);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    /**
//     * AES 解密
//     *
//     * @param content
//     *            密文
//     * @param password
//     *            生成秘钥的关键字
//     * @return
//     */
//
//    public static String aesDecrypt(String content, String password) {
//        try {
//            byte[] byteMi=  str2ByteArray(content);
//            IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes());
//            SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");
//            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//            cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
//            byte[] decryptedData = cipher.doFinal(byteMi);
//            return new String(decryptedData, "utf-8");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//}


    private final static String SHA1_PRNG = "SHA1PRNG";
    private static final int KEY_SIZE = 32;

    /**
     * Aes加密/解密
     *
     * @param content  字符串
     * @param password 密钥
     * @param type     加密：{@link Cipher#ENCRYPT_MODE}，解密：{@link Cipher#DECRYPT_MODE}
     * @return 加密/解密结果字符串
     */
    @SuppressLint({"DeletedProvider", "GetInstance"})
    public static String des(String content, String password, @AESType int type) {
        if (TextUtils.isEmpty(content) || TextUtils.isEmpty(password)) {
            return null;
        }
        try {
            SecretKeySpec secretKeySpec;
            if (Build.VERSION.SDK_INT >= 28) {
                secretKeySpec = deriveKeyInsecurely(password);
            } else {
                secretKeySpec = fixSmallVersion(password);
            }
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(type, secretKeySpec);
            if (type == Cipher.ENCRYPT_MODE) {
                byte[] byteContent = content.getBytes("utf-8");
                return parseByte2HexStr(cipher.doFinal(byteContent));
            } else {
                byte[] byteContent = parseHexStr2Byte(content);
                return new String(cipher.doFinal(byteContent));
            }
        } catch (NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException |
                UnsupportedEncodingException | InvalidKeyException | NoSuchPaddingException |
                NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressLint("DeletedProvider")
    private static SecretKeySpec fixSmallVersion(String password) throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            secureRandom = SecureRandom.getInstance(SHA1_PRNG, new CryptoProvider());
        } else {
            secureRandom = SecureRandom.getInstance(SHA1_PRNG, "Crypto");
        }
        secureRandom.setSeed(password.getBytes());
        generator.init(128, secureRandom);
        byte[] enCodeFormat = generator.generateKey().getEncoded();
        return new SecretKeySpec(enCodeFormat, "AES");
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static SecretKeySpec deriveKeyInsecurely(String password) {
        byte[] passwordBytes = password.getBytes(StandardCharsets.US_ASCII);
        return new SecretKeySpec(InsecureSHA1PRNGKeyDerivator.deriveInsecureKey(passwordBytes, AESUtilTest.KEY_SIZE), "AES");
    }

    private static String parseByte2HexStr(byte buf[]) {
        StringBuilder sb = new StringBuilder();
        for (byte b : buf) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    private static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    @IntDef({Cipher.ENCRYPT_MODE, Cipher.DECRYPT_MODE})
    @interface AESType {
    }

    private static final class CryptoProvider extends Provider {
        CryptoProvider() {
            super("Crypto", 1.0, "HARMONY (SHA1 digest; SecureRandom; SHA1withDSA signature)");
            put("SecureRandom.SHA1PRNG", "org.apache.harmony.security.provider.crypto.SHA1PRNG_SecureRandomImpl");
            put("SecureRandom.SHA1PRNG ImplementedIn", "Software");
        }
    }




}

