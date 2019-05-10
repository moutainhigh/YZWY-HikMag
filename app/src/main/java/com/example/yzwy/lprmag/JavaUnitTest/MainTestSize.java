package com.example.yzwy.lprmag.JavaUnitTest;


import com.example.yzwy.lprmag.util.crypto.aes.AES_ECB_PKCS7Padding_128_Key32_Util;

import java.util.Arrays;

public class MainTestSize {
    public static void main(String[] args) {


        StringBuffer stringBuffer = new StringBuffer();
        int MAX_SIZE = 720;
        for (int i = 0; i <= MAX_SIZE; i++) {
            stringBuffer.append("<dimen name=\"fs_px_"+i+"\">@dimen/qb_px_"+i+"</dimen>\r\n");
        }

        System.out.println(stringBuffer.toString());

    }


}
