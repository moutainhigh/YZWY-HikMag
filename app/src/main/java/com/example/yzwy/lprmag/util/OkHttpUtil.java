package com.example.yzwy.lprmag.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * #################################################################################################
 * Copyright: Copyright (c) 2018
 * Created on 2019-04-03
 * Author: 仲超(zhongchao)
 * Version 1.0
 * Describe: OKHTTP工具类
 * #################################################################################################
 */
public class OkHttpUtil {

    private String TAG = "OkHttpUtil";

    private final static int READ_TIMEOUT = 100;
    private final static int CONNECT_TIMEOUT = 60;
    private final static int WRITE_TIMEOUT = 60;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final byte[] LOCKER = new byte[0];
    private static OkHttpUtil mInstance;
    private OkHttpClient mOkHttpClient;

    private OkHttpUtil() {
        okhttp3.OkHttpClient.Builder ClientBuilder = new okhttp3.OkHttpClient.Builder();
        ClientBuilder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);//读取超时
        ClientBuilder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);//连接超时
        ClientBuilder.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);//写入超时
        //支持HTTPS请求，跳过证书验证
        ClientBuilder.sslSocketFactory(createSSLSocketFactory());
        ClientBuilder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        mOkHttpClient = ClientBuilder.build();
    }

    /**
     * 单例模式获取NetUtils
     *
     * @return
     */
    public static OkHttpUtil getInstance() {
        if (mInstance == null) {
            synchronized (LOCKER) {
                if (mInstance == null) {
                    mInstance = new OkHttpUtil();
                }
            }
        }
        return mInstance;
    }

    /**
     * 判断网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
        } else {
            //如果仅仅是用来判断网络连接
            //则可以使用cm.getActiveNetworkInfo().isAvailable();
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * get请求，同步方式，获取网络数据，是在主线程中执行的，需要新起线程，将其放到子线程中执行
     *
     * @param url
     * @return
     */
    public Response getData(String url) {
        //1 构造Request
        Request.Builder builder = new Request.Builder();
        Request request = builder.get().url(url).build();
        //2 将Request封装为Call
        Call call = mOkHttpClient.newCall(request);
        //3 执行Call，得到response
        Response response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * post请求，同步方式，提交数据，是在主线程中执行的，需要新起线程，将其放到子线程中执行
     *
     * @param url
     * @param bodyParams
     * @return
     */
    public Response postData(String url, Map<String, String> bodyParams) {
        //1构造RequestBody
        RequestBody body = setRequestBody(bodyParams);
        //2 构造Request
        Request.Builder requestBuilder = new Request.Builder();
        Request request = requestBuilder.post(body).url(url).build();
        //3 将Request封装为Call
        Call call = mOkHttpClient.newCall(request);
        //4 执行Call，得到response
        Response response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * get请求，异步方式，获取网络数据，是在子线程中执行的，需要切换到主线程才能更新UI
     *
     * @param url
     * @param myNetCall
     * @return
     */
    public void getDataAsyn(String url, Map<String, String> params, final MyNetCall myNetCall) {


        if (params == null) {
            params = new HashMap<>();
        }
        // 请求url（baseUrl+参数）
        final String doUrl = urlJoint(url, params);

        //1 构造Request
        Request.Builder builder = new Request.Builder();
        Request request = builder.get().url(doUrl).build();
        //2 将Request封装为Call
        Call call = mOkHttpClient.newCall(request);
        //3 执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                myNetCall.failed(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                myNetCall.success(call, response);

            }
        });
    }

    /**
     * 拼接url和请求参数
     *
     * @param url    请求地址
     * @param params Map值
     * @return
     */
    private static String urlJoint(String url, Map<String, String> params) {
        StringBuilder endUrl = new StringBuilder(url);
        boolean isFirst = true;
        Set<Map.Entry<String, String>> entrySet = params.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            if (isFirst && !url.contains("?")) {
                isFirst = false;
                endUrl.append("?");
            } else {
                endUrl.append("&");
            }
            endUrl.append(entry.getKey());
            endUrl.append("=");
            endUrl.append(entry.getValue());
        }
        return endUrl.toString();
    }

    /**
     * post请求，异步方式，提交数据，是在子线程中执行的，需要切换到主线程才能更新UI
     *
     * @param url
     * @param bodyParams
     * @param myNetCall
     */
    public void postDataAsyn(String url, Map<String, String> bodyParams, final MyNetCall myNetCall) {
        //1构造RequestBody
        RequestBody body = setRequestBody(bodyParams);

        //2 构造Request
        Request.Builder requestBuilder = new Request.Builder();
        Request request = requestBuilder.post(body).url(url).build();
        //3 将Request封装为Call
        Call call = mOkHttpClient.newCall(request);
        //4 执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                myNetCall.failed(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                myNetCall.success(call, response);

            }
        });
    }

    /**
     * post的请求参数，构造RequestBody
     *
     * @param BodyParams
     * @return
     */
    private RequestBody setRequestBody(Map<String, String> BodyParams) {
        RequestBody body = null;
        okhttp3.FormBody.Builder formEncodingBuilder = new okhttp3.FormBody.Builder();
        if (BodyParams != null) {
            Iterator<String> iterator = BodyParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                formEncodingBuilder.add(key, BodyParams.get(key));
                Log.d("post http", "post_Params===" + key + "====" + BodyParams.get(key));
            }
        }
        body = formEncodingBuilder.build();
        return body;

    }

    /**
     * 生成安全套接字工厂，用于https请求的证书跳过
     *
     * @return
     */
    public SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }
        return ssfFactory;
    }

    public String postJson(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = mOkHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    /**
     * @param url
     * @param json
     * @param myNetCall
     * @throws IOException
     */
    public void postJsonAsyn(String url, String json, final MyNetCall myNetCall) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        //2 构造Request
        Request.Builder requestBuilder = new Request.Builder();
        Request request = requestBuilder.post(body).url(url).build();
        //3 将Request封装为Call
        Call call = mOkHttpClient.newCall(request);
        //4 执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                myNetCall.failed(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                myNetCall.success(call, response);
            }
        });
    }

    /**
     * 自定义网络回调接口
     */
    public interface MyNetCall {
        void success(Call call, Response response) throws IOException;

        void failed(Call call, IOException e);
    }

    /**
     * 用于信任所有证书
     */
    class TrustAllCerts implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }


//    public static void post3(String address, okhttp3.Callback callback, Map<String,String> map)
//    {
//        OkHttpClient client = new OkHttpClient();
//        MultipartBody.Builder builder = new MultipartBody.Builder();
//        if (map!=null)
//        {
//            for (Map.Entry<String,String> entry:map.entrySet())
//            {
//                builder.addFormDataPart(entry.getKey(),entry.getValue());
//            }
//        }
//
//        File file = new File(CommonVari.filePath);
//        if(file.exists()){
//            Log.d(TAG, "post3: 文件存在");
//            String TYPE = "application/octet-stream";
//            RequestBody fileBody = RequestBody.create(MediaType.parse(TYPE),file);
//
//            RequestBody requestBody = builder
//                    .setType(MultipartBody.FORM)
//                    .addFormDataPart("detail_image",file.getName(),fileBody)
//                    .build();
//
//            Request request = new Request.Builder()
//                    .url(address)
//                    .post(requestBody)
//                    .addHeader("Authorization","Bearer "+CommonVari.token)
//                    .build();
//            client.newCall(request).enqueue(callback);
//        }else {
//            Log.d(TAG, "post3: 文件不存在");
//        }
//
//    }


    /**
     * =============================================================================================
     * 上传文件
     *
     * @param URL
     * @param file
     * @param fileType
     * @param fileName
     * @param myNetCall
     */
    public static void postUploadFile(String URL, File file, String fileType, String fileName, final MyNetCall myNetCall) {

        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        //创建MultipartBody，给MultipartBody进行设置
        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(fileType, fileName, fileBody)
                .build();
        /**
         * GET上传的数据一般是很小的并且安全性能不高的数据，
         * 而POST上传的数据适用于数据量大，数据类型复杂，数据安全性能要求高的地方
         */
        //创建okhttp对象
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        //创建Request对象
        Request request = new Request.Builder()
                .url(URL)
                .post(multipartBody)
                .build();
        //上传完图片，得到服务器反馈数据
        Call call = okHttpClient.newCall(request);
        //4 执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                myNetCall.failed(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                myNetCall.success(call, response);

            }
        });

    }

//    /**
//     * 下载文件
//     *
//     * @param file               文件保存文件
//     * @param sdPath             保存路劲
//     * @param fileName           文件名称
//     * @param url                下载路径
//     * @param uiProgressListener
//     * @param callBack
//     * @return
//     */
//    public boolean download(final File file, final String sdPath, final String fileName, final String url, UIProgressListener uiProgressListener, final FileDownPathCallBack callBack) {
//
//        //构造请求
//        final Request request1 = new Request.Builder()
//                .addHeader("Accept-Encoding", "identity")
//                .url(url)
//                .build();
//
//        //包装Response使其支持进度回调
//        ProgressHelper.addProgressResponseListener(mOkHttpClient, uiProgressListener).newCall(request1).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) {
//                InputStream is = null;
//                byte[] buf = new byte[2048];
//                int len = 0;
//                FileOutputStream fos = null;
//                try {
//                    long total = response.body().contentLength();
//                    Log.e(TAG, "total------>" + total);
//                    long current = 0;
//                    is = response.body().byteStream();
//                    fos = new FileOutputStream(file);
//                    while ((len = is.read(buf)) != -1) {
//                        current += len;
//                        fos.write(buf, 0, len);
//                        Log.e(TAG, "current------>" + current);
//                    }
//                    fos.flush();
//                    callBack.path(sdPath + File.separator + fileName);
//                    Log.e(TAG, e.toString());
//                } catch (IOException e) {
//                } finally {
//                    try {
//                        if (is != null) {
//                            is.close();
//                        }
//                        if (fos != null) {
//                            fos.close();
//                        }
//                    } catch (IOException e) {
//                        Log.e(TAG, e.toString());
//                    }
//                }
//
//            }
//
//        });
//        return false;
//    }


}