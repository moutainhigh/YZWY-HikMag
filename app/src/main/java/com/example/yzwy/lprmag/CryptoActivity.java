//package com.example.yzwy.lprmag;
//
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.example.yzwy.lprmag.util.crypto.AesUtil;
//import com.example.yzwy.lprmag.util.crypto.Des3Util;
//import com.example.yzwy.lprmag.util.crypto.MD5Util;
//import com.example.yzwy.lprmag.util.crypto.RSAUtil;
//
///**
// * #################################################################################################
// * Copyright: Copyright (c) 2018
// * Created on 2019-04-03
// * Author: 仲超(zhongchao)
// * Version 1.0
// * Describe: 欢迎页
// * #################################################################################################
// */
//public class CryptoActivity extends AppCompatActivity implements View.OnClickListener {
//
//    private final static String TAG = "MainActivity";
//    private EditText et_raw;
//    private TextView tv_des;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.ly_crypto);
//
//        et_raw = (EditText) findViewById(R.id.et_raw);
//        tv_des = (TextView) findViewById(R.id.tv_des);
//        findViewById(R.id.btn_md5).setOnClickListener(this);
//        findViewById(R.id.btn_rsa).setOnClickListener(this);
//        findViewById(R.id.btn_aes).setOnClickListener(this);
//        findViewById(R.id.btn_3des).setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View v) {
//        String raw = et_raw.getText().toString();
//        if (raw == null || raw.length() <= 0) {
//            Toast.makeText(this, "请输入待加密字符串", Toast.LENGTH_LONG).show();
//            return;
//        }
//        if (v.getId() == R.id.btn_md5) {
//            String enStr = MD5Util.encrypBy(raw);
//            tv_des.setText("MD5的加密结果是:" + enStr);
//        } else if (v.getId() == R.id.btn_rsa) {
//            String enStr = RSAUtil.encodeRSA(null, raw);
//            tv_des.setText("RSA加密结果是:" + enStr);
//        } else if (v.getId() == R.id.btn_aes) {
//            try {
//                String seed = "a";
//                String enStr = AesUtil.encryptHexStr(seed, raw,AesUtil.TRANSFORM_ECB_PKCS5PADDING);
//                String deStr = AesUtil.decryptStr(seed, enStr,AesUtil.TRANSFORM_ECB_PKCS5PADDING);
//                String desc = String.format("AES加密结果是:%s\nAES解密结果是:%s", enStr, deStr);
//                tv_des.setText(desc);
//            } catch (Exception e) {
//                e.printStackTrace();
//                tv_des.setText("AES加密/解密失败");
//            }
//        } else if (v.getId() == R.id.btn_3des) {
//            String key = "a";
//            String enStr = Des3Util.encrypt(key, raw);
//            String deStr = Des3Util.decrypt(key, enStr);
//            String desc = String.format("3DES加密结果是:%s\n3DES解密结果是:%s", enStr, new String(deStr));
//            tv_des.setText(desc);
//        }
//    }
//
//}
