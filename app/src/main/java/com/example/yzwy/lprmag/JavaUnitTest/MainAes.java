package com.example.yzwy.lprmag.JavaUnitTest;


import com.example.yzwy.lprmag.util.crypto.aes.AES_ECB_PKCS7Padding_128_Key32_Util;

import java.util.Arrays;

public class MainAes {
    public static void main(String[] args) {


        //密钥 加密内容(对象序列化后的内容-json格式字符串)
        //String key = "0000000000000000A1zFlux77a99X1be";
        String key = "A1zFlux77a99X1be";
        String content = "admin";
        try {

            System.out.println("Byte[]加密结果：" + Arrays.toString(AES_ECB_PKCS7Padding_128_Key32_Util.aesEncryptByte(content, key)));
            System.out.println("Byte[]解密结果：" + AES_ECB_PKCS7Padding_128_Key32_Util.aesDecryptByte(AES_ECB_PKCS7Padding_128_Key32_Util.aesEncryptByte(content, key), key));

            System.out.println("Base64加密结果：" + AES_ECB_PKCS7Padding_128_Key32_Util.aesEncryptBase64(content, key));
            System.out.println("Base64解密结果：" + AES_ECB_PKCS7Padding_128_Key32_Util.aesDecryptBase64(AES_ECB_PKCS7Padding_128_Key32_Util.aesEncryptBase64(content, key), key));


            System.out.println("HexStr加密结果：" + AES_ECB_PKCS7Padding_128_Key32_Util.aesEncryptHexStr(content, key));
            System.out.println("HexStr解密结果：" + AES_ECB_PKCS7Padding_128_Key32_Util.aesDecryptHexStr(AES_ECB_PKCS7Padding_128_Key32_Util.aesEncryptHexStr(content, key), key));


//            //String json = "{ \"errmsg\": \"用户登陆验证正确！\", \"errcode\": 0, \"time\": \"2019/5/9 15:11:27\", \"data\": { \"Id\": \"021bc83a-9aef-413b-80a9-af8a0d82ec32\", \"UserName\": \"admin\", \"PassWord\": null, \"Sex\": \"男\", \"Tel\": \"18608211717\", \"Address\": \"高新区\", \"Email\": null, \"_LoginTime\": \"2019/5/9 15:11:09\", \"Role_Id\": \"021bc83a-9aef-413b-80a9-af8a0d82ec34\" } }";
//            String json = "{\"errmsg\":\"用户登陆验证正确！\",\"errcode\":0,\"time\":\"2019/5/9 15:31:41\",\"data\":{\"Id\":\"021bc83a-9aef-413b-80a9-af8a0d82ec32\",\"UserName\":\"admin\",\"PassWord\":null,\"Sex\":\"男\",\"Tel\":\"18608211717\",\"Address\":\"高新区\",\"Email\":null,\"_LoginTime\":\"2019/5/9 15:24:20\",\"Role_Id\":\"021bc83a-9aef-413b-80a9-af8a0d82ec34\"}}";
////            String json = "{\n" +
////                    "    \"errmsg\": \"用户登陆验证正确！\",\n" +
////                    "    \"errcode\": 0,\n" +
////                    "    \"time\": \"2019/5/9 15:31:41\",\n" +
////                    "    \"data\": {\n" +
////                    "        \"Id\": \"021bc83a-9aef-413b-80a9-af8a0d82ec32\",\n" +
////                    "        \"UserName\": \"admin\",\n" +
////                    "        \"PassWord\": null,\n" +
////                    "        \"Sex\": \"男\",\n" +
////                    "        \"Tel\": \"18608211717\",\n" +
////                    "        \"Address\": \"高新区\",\n" +
////                    "        \"Email\": null,\n" +
////                    "        \"_LoginTime\": \"2019/5/9 15:24:20\",\n" +
////                    "        \"Role_Id\": \"021bc83a-9aef-413b-80a9-af8a0d82ec34\"\n" +
////                    "    }\n" +
////                    "}";
//                    System.out.println("HexStr加密结果JSON：" + AES_ECB_PKCS7Padding_128_Key32_Util.aesEncryptHexStr(json, key));
//            System.out.println("HexStr解密结果JSON：" + AES_ECB_PKCS7Padding_128_Key32_Util.aesDecryptHexStr(AES_ECB_PKCS7Padding_128_Key32_Util.aesEncryptHexStr(json, key), key));


            String json = "{\"errcode\":0,\"errmsg\":\"未能根据秘钥解析参数！\",\"time\":\"2019/5/9 15:46:27\",\"data\":null}";
            System.out.println("HexStr加密结果JSON：" + AES_ECB_PKCS7Padding_128_Key32_Util.aesEncryptHexStr(json, key));
            System.out.println("HexStr解密结果JSON：" + AES_ECB_PKCS7Padding_128_Key32_Util.aesDecryptHexStr("D17B99BCFF8EED4A48C80128FD8D35EB712603C9E94F5E9387AF4100AA1C7D724C32B8F6146A576CDDE5E6E6F6094F6450C73CBF14637D86F0DE5ADDAFDA5EF5CBA735BD9893C6C3D85CF8404AE6023EA593E887FCFB6A98CC0855491CDCF3E122C3E335C3BDE8F0CA5D4870806F8F61", key));




            System.out.println("HexStr解密结果JSON：" + AES_ECB_PKCS7Padding_128_Key32_Util.aesDecryptHexStr("2905C630DE20C3B358B6FFAA82CF45C1712603C9E94F5E9387AF4100AA1C7D724C32B8F6146A576CDDE5E6E6F6094F6450C73CBF14637D86F0DE5ADDAFDA5EF532531AB35A133E8014A39BFA3DAFB75BE8908CB1A4BFFA66878193F84DBE769222C3E335C3BDE8F0CA5D4870806F8F61", key));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
