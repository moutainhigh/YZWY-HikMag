package com.example.yzwy.lprmag;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.yzwy.lprmag.myConstant.HttpURL;
import com.example.yzwy.lprmag.control.activityStackExtends.util.ActivityStackManager;
import com.example.yzwy.lprmag.util.HanderMsg;
import com.example.yzwy.lprmag.util.LogUtil;
import com.example.yzwy.lprmag.util.OkHttpUtil;
import com.example.yzwy.lprmag.util.Tools;
import com.example.yzwy.lprmag.util.WebViewLifecycleUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * #################################################################################################
 * Copyright: Copyright (c) 2018
 * Created on 2019-04-09
 * Author: 仲超(zhongchao)
 * Version 1.0
 * Describe: 界面
 * #################################################################################################
 */
@SuppressLint("Registered")
public class UseCourseDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private String MycusTitle = "";
    private String MycusLoadingTitle = "加载中...";
    private String StrContentHtml = "";

    //String url = "https://github.com/getActivity/";
    //private String url = WebViewUrl.AubotUsUrl;
    private String url = "file:///android_asset/index.html";

    private ImageButton imgbtn_back;
    private WebView mWebView;
    private ProgressBar mProgressBar;
    private TextView tv_titlehead;
    private ImageView img_overload_webview;
    private ImageView img_showfaile_webview;
    private TextView tc_showload_webview;
    private LinearLayout li_load_webview;
    private int ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_webview);


        //==========================================================================================
        ActivityStackManager.getInstance().addActivity(this);

        initView();


        initHttpData();


        initOnClick();
    }

    private void initHttpData() {

        /**
         * =============================================================================================
         * 获取Intent中的值（）
         */

        //ID = Integer.valueOf(getIntent().getStringExtra("ID"));
        Bundle receive = getIntent().getExtras();
        //得到随Intent传递过来的Bundle对象
        assert receive != null;
        MycusTitle = receive.getString("MycusTitle");
        ID = Integer.valueOf(receive.getString("ID"));


        new Thread(new Runnable() {
            @Override
            public void run() {

                Map<String, String> LoginStringMap = new HashMap<>();
                LoginStringMap.put("ID", String.valueOf(ID));
                //LoginStringMap.put("passWord", edtPwd);
                OkHttpUtil.getInstance().postDataAsyn(HttpURL.LoginVerification, LoginStringMap, new OkHttpUtil.MyNetCall() {
                    @Override
                    public void success(Call call, Response response) throws IOException {
                        String rs = response.body().string();
                        HanderMsg.HanderMsgSend(handler, 100, rs);
                        LogUtil.showLog("UseCourseDetailsActivity success --->", rs);
                    }

                    @Override
                    public void failed(Call call, IOException e) {
                        HanderMsg.HanderMsgSend(handler, 101, e.toString());
                        LogUtil.showLog("UseCourseDetailsActivity failed --->", e.toString());
                    }
                });


            }
        }).start();
    }

    /**
     * =============================================================================================
     * 海康登录UI更新和Log打印和m_iLogID设置
     */
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String data = msg.getData().getString("data");
            switch (msg.what) {

                case 100:
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        String errcode = jsonObject.getString("errcode");
                        String errmsg = jsonObject.getString("errmsg");
                        String dataHtml = jsonObject.getString("data");

                        StrContentHtml = dataHtml;
                        initWebViewData();


                    } catch (JSONException e) {
                        e.printStackTrace();
                        LogUtil.showLog("UseCourseDetailsActivity JSON failed --->", e.toString());
                        Tools.Toast(UseCourseDetailsActivity.this, "数据解析异常");
                    }


                    break;

                case 101:
                    Tools.Toast(UseCourseDetailsActivity.this, "网络异常，请检查网络");
                    break;


                default:
                    break;

            }


        }


    };

    /**
     * =============================================================================================
     * 初始化组件
     */
    private void initView() {
        imgbtn_back = (ImageButton) findViewById(R.id.imgbtn_back);
        tv_titlehead = (TextView) findViewById(R.id.tv_titlehead);
        mWebView = (WebView) findViewById(R.id.wv_web_view);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_web_progress);
        img_overload_webview = (ImageView) findViewById(R.id.img_overload_webview);
        img_showfaile_webview = (ImageView) findViewById(R.id.img_showfaile_webview);
        tc_showload_webview = (TextView) findViewById(R.id.tc_showload_webview);
        li_load_webview = (LinearLayout) findViewById(R.id.li_load_webview);

        li_load_webview.setVisibility(View.GONE);


        // 不显示滚动条
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);

        WebSettings settings = mWebView.getSettings();
        // 允许文件访问
        settings.setAllowFileAccess(true);
        // 支持javaScript
        settings.setJavaScriptEnabled(true);
        // 允许网页定位
        settings.setGeolocationEnabled(true);
        // 允许保存密码
        settings.setSavePassword(true);

        // 解决Android 5.0上WebView默认不允许加载Http与Https混合内容
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //两者都可以
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        // 加快HTML网页加载完成的速度，等页面finish再加载图片
        if (Build.VERSION.SDK_INT >= 19) {
            settings.setLoadsImagesAutomatically(true);
        } else {
            settings.setLoadsImagesAutomatically(false);
        }

    }

    /**
     * =============================================================================================
     * 数据加载
     */
    private void initWebViewData() {
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());
        mWebView.loadUrl(url);
        MySetTitle(MycusLoadingTitle);
    }

    /**
     * =============================================================================================
     * 初始化组件监听事件
     */
    private void initOnClick() {
        imgbtn_back.setOnClickListener(this);
        img_overload_webview.setOnClickListener(this);
    }

    /**
     * =============================================================================================
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            /**
             * 关闭页面
             * */
            case R.id.imgbtn_back:

                if (mWebView.canGoBack()) {
                    // 后退网页并且拦截该事件
                    mWebView.goBack();
                    return;
                } else {
                    this.finish();
                }

                break;

            /**
             * 重新加载
             * */
            case R.id.img_overload_webview:

                initWebViewData();

                break;

        }
    }


    /**
     * =============================================================================================
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            // 后退网页并且拦截该事件
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * =============================================================================================
     */
    @Override
    protected void onResume() {
        WebViewLifecycleUtils.onResume(mWebView);
        super.onResume();
    }

    /**
     * =============================================================================================
     */
    @Override
    protected void onPause() {
        WebViewLifecycleUtils.onPause(mWebView);
        super.onPause();
    }

    /**
     * =============================================================================================
     */
    @Override
    protected void onDestroy() {
        WebViewLifecycleUtils.onDestroy(mWebView);
        super.onDestroy();
    }

    /**
     * =============================================================================================
     */
    private class MyWebViewClient extends WebViewClient {

        // 网页加载错误时回调，这个方法会在 onPageFinished 之前调用
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, final String failingUrl) {
            li_load_webview.setVisibility(View.VISIBLE);
        }

        // 开始加载网页
        @Override
        public void onPageStarted(final WebView view, final String url, Bitmap favicon) {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        // 完成加载网页
        @Override
        public void onPageFinished(WebView view, String url) {
            mProgressBar.setVisibility(View.GONE);
            li_load_webview.setVisibility(View.GONE);

            //java中调用JS中的方法
            mWebView.loadUrl("javascript:do_view(" + "'" + StrContentHtml + "'" + ")");
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            //super.onReceivedSslError(view, handler, error);注意一定要去除这行代码，否则设置无效。
            // handler.cancel();// Android默认的处理方式
            handler.proceed();// 接受所有网站的证书
            // handleMessage(Message msg);// 进行其他处理
        }

        // 跳转到其他链接
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, final String url) {

            String scheme = Uri.parse(url).getScheme();
            if (scheme != null) {
                scheme = scheme.toLowerCase();
            }
            if ("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme)) {
                mWebView.loadUrl(url);
            }
            // 已经处理该链接请求
            return true;
        }
    }

    /**
     * =============================================================================================
     */
    private class MyWebChromeClient extends WebChromeClient {

        // 收到网页标题
        @Override
        public void onReceivedTitle(WebView view, String title) {
            //if (title != null) {
            //    MySetTitle(title);
            //}
            MySetTitle(MycusTitle);
        }

        // 收到加载进度变化
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            mProgressBar.setProgress(newProgress);
        }
    }

    /**
     * =============================================================================================
     * 设置标题
     */
    private void MySetTitle(String Title) {
        if (Title != null) {
            tv_titlehead.setText(Title);
        }
    }

}
