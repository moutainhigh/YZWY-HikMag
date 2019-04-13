package com.example.yzwy.lprmag.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yzwy.lprmag.AboutUsActivity;
import com.example.yzwy.lprmag.CustomServiceActivity;
import com.example.yzwy.lprmag.LoginActivity;
import com.example.yzwy.lprmag.PrivacyAgreementActivity;
import com.example.yzwy.lprmag.R;
import com.example.yzwy.lprmag.UseCourseListActivity;
import com.example.yzwy.lprmag.myConstant.HttpUrl;
import com.example.yzwy.lprmag.myConstant.UserInfoConstant;
import com.example.yzwy.lprmag.myConstant.VersionNumber;
import com.example.yzwy.lprmag.util.ActivityStackManager;
import com.example.yzwy.lprmag.util.HanderMsg;
import com.example.yzwy.lprmag.util.LogUtil;
import com.example.yzwy.lprmag.util.OkHttpUtil;
import com.example.yzwy.lprmag.util.SharePreferencesUtil;
import com.example.yzwy.lprmag.util.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 *
 */
public class MineFragment extends Fragment implements View.OnClickListener {

    private View view;

    private LinearLayout li_aboutus_mine;
    private Button btn_loginOut_mine;
    private LinearLayout li_mineinfo_mine;
    private LinearLayout li_privacyProtocol_mine;
    private LinearLayout li_update_mine;


    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;
    private Notification notification;


    private String apkName = "";//apk名字
    private String openApkUrl = "";//下载完成以后安装apk


    ProgressDialog progressDlg;//定义下载提示框
    private TextView tv_verionnum_mine;
    private LinearLayout li_useCourse_mine;
    private LinearLayout li_customService_mine;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ActivityStackManager.getInstance().addActivity(getActivity());

        //初始化组件
        initView();

        //加载数据
        initData();

    }

    /**
     * =============================================================================================
     * 加载网络数据
     */
    private void initData() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                Map<String, String> LoginStringMap = new HashMap<>();
//                LoginStringMap.put("userName", userName);
//                LoginStringMap.put("passWord", passWord);
                OkHttpUtil.getInstance().postDataAsyn(HttpUrl.LoginUrl, LoginStringMap, new OkHttpUtil.MyNetCall() {
                    @Override
                    public void success(Call call, Response response) throws IOException {
                        String rs = response.body().string();
                        HanderMsg.HanderMsgSend(handler, 100100, rs);
                        LogUtil.showLog("NetAPi success --->", rs);
                    }

                    @Override
                    public void failed(Call call, IOException e) {
                        HanderMsg.HanderMsgSend(handler, 101101, e.toString());
                        LogUtil.showLog("NetAPi failed --->", e.toString());
                    }
                });


            }
        }).start();


        String userName = SharePreferencesUtil.getStringValue(getActivity(), UserInfoConstant.userName, "你还没有登录哦~");
        String pwd = SharePreferencesUtil.getStringValue(getActivity(), UserInfoConstant.passWord, "0");
        Boolean aBoolean = SharePreferencesUtil.getBooleanValue(getActivity(), UserInfoConstant.Flag, true);


    }

    /**
     * =============================================================================================
     * 初始化组件
     */
    private void initView() {
        li_aboutus_mine = (LinearLayout) view.findViewById(R.id.li_aboutus_mine);
        li_mineinfo_mine = (LinearLayout) view.findViewById(R.id.li_mineinfo_mine);
        li_privacyProtocol_mine = (LinearLayout) view.findViewById(R.id.li_privacyProtocol_mine);
        li_useCourse_mine = (LinearLayout) view.findViewById(R.id.li_useCourse_mine);
        li_customService_mine = (LinearLayout) view.findViewById(R.id.li_customService_mine);
        li_update_mine = (LinearLayout) view.findViewById(R.id.li_update_mine);
        btn_loginOut_mine = (Button) view.findViewById(R.id.btn_loginOut_mine);
        tv_verionnum_mine = (TextView) view.findViewById(R.id.tv_verionnum_mine);


        initOnClick();


    }


    /**
     * =============================================================================================
     * 事件监听
     */
    private void initOnClick() {
        li_aboutus_mine.setOnClickListener(this);
        btn_loginOut_mine.setOnClickListener(this);
        li_mineinfo_mine.setOnClickListener(this);
        li_privacyProtocol_mine.setOnClickListener(this);
        li_update_mine.setOnClickListener(this);
        li_useCourse_mine.setOnClickListener(this);
        li_customService_mine.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            //======================================================================================
            //我的信息
            case R.id.li_mineinfo_mine:
                //AboutUsOnClick();
                break;

            //======================================================================================
            //使用教程
            case R.id.li_useCourse_mine:
                UseCourseOnClick();
                break;

            //======================================================================================
            //客服中心
            case R.id.li_customService_mine:
                CustomServiceOnClick();
                break;

            //======================================================================================
            //关于我们
            case R.id.li_aboutus_mine:
                AboutUsOnClick();
                break;

            //======================================================================================
            //隐私协议
            case R.id.li_privacyProtocol_mine:
                PrivacyAgreementOnClick();
                break;

            //======================================================================================
            //检查更新
            case R.id.li_update_mine:
                APPUpdateOnClick();
                break;

            //======================================================================================
            //退出登录
            case R.id.btn_loginOut_mine:
                LoginOutOnClick();
                break;

            default:
                break;

        }
    }

    /**
     * =============================================================================================
     * 使用教程
     */
    private void UseCourseOnClick() {
        Tools.IntentBack(getActivity(), UseCourseListActivity.class);
    }

    /**
     * =============================================================================================
     * 客服中心
     */
    private void CustomServiceOnClick() {
        //Tools.Toast(getActivity(), "亲亲，单身狗们正在加倍努力喔~");
        Tools.IntentBack(getActivity(), CustomServiceActivity.class);
    }

    /**
     * =============================================================================================
     * 关于我们
     */
    private void AboutUsOnClick() {
        //Tools.Toast(getActivity(), "亲亲，单身狗们正在加倍努力喔~");
        Tools.IntentBack(getActivity(), AboutUsActivity.class);
    }

    /**
     * =============================================================================================
     * 隐私协议
     */
    private void PrivacyAgreementOnClick() {
        //Tools.Toast(getActivity(), "亲亲，单身狗们正在加倍努力喔~");
        Tools.IntentBack(getActivity(), PrivacyAgreementActivity.class);
    }

    /**
     * =============================================================================================
     * 程序更新
     */
    private void APPUpdateOnClick() {
        //Tools.Toast(getActivity(), "亲亲，单身狗们正在加倍努力喔~");
//        Tools.IntentBack(getActivity(), PrivacyAgreementActivity.class);

        InstallApp();

    }


    /**
     * =============================================================================================
     * 退出登录
     */
    private void LoginOutOnClick() {
        SharePreferencesUtil.putStringValue(getActivity(), UserInfoConstant.userName, "0");
        SharePreferencesUtil.putStringValue(getActivity(), UserInfoConstant.passWord, "0");
        SharePreferencesUtil.putBooleanValue(getActivity(), UserInfoConstant.Flag, false);

        Tools.Toast(getActivity(), "成功退出账号");

        Tools.Intent(getActivity(), LoginActivity.class);

    }


    private void InstallApp() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                Map<String, String> LoginStringMap = new HashMap<>();
//                LoginStringMap.put("userName", userName);
//                LoginStringMap.put("passWord", passWord);
                OkHttpUtil.getInstance().postDataAsyn(HttpUrl.LoginUrl, LoginStringMap, new OkHttpUtil.MyNetCall() {
                    @Override
                    public void success(Call call, Response response) throws IOException {
                        String rs = response.body().string();
                        HanderMsg.HanderMsgSend(handler, 100, rs);
                        LogUtil.showLog("LoginActivity success --->", rs);
                    }

                    @Override
                    public void failed(Call call, IOException e) {
                        HanderMsg.HanderMsgSend(handler, 101, e.toString());
                        LogUtil.showLog("LoginActivity failed --->", e.toString());
                    }
                });


            }
        }).start();
    }

    /**
     * =============================================================================================
     */
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            String data = msg.getData().getString("data");
            try {
                JSONObject jsonObject = new JSONObject(data);
                String errcode = jsonObject.getString("errcode");
                String errmsg = jsonObject.getString("errmsg");
                switch (msg.what) {

                    case 100:
                        if (errcode.equals("0")) {

                            String NetApkUrl = jsonObject.getString("NetApkUrl");
                            String NetApkName = jsonObject.getString("NetApkName");

                            //开始执行下载任务
                            ExeInstallApp(NetApkUrl, NetApkName);

                        } else {
                            Tools.Toast(getActivity(), errmsg);
                        }
                        break;

                    case 101:
                        Tools.Toast(getActivity(), "失败，异常Log：\n" + data);
                        break;


                    case 100100:
                        if (errcode.equals("0")) {
                            //String VersionNumber = jsonObject.getString("VersionNumber");
                            String StrVersionNumber = "0.0.1";
                            if ((VersionNumber.versionNum).equals(StrVersionNumber)) {
                                tv_verionnum_mine.setText("版本号 v" + StrVersionNumber + " " + "最新版");
                            } else {
                                tv_verionnum_mine.setText("可更新至 " + "版本号 v" + StrVersionNumber);
                            }

                        } else {
                            Tools.Toast(getActivity(), errmsg);
                        }
                        break;

                    case 101101:
                        tv_verionnum_mine.setText("版本号 获取失败");
                        Tools.Toast(getActivity(), "失败，异常Log：\n" + data);
                        break;

                    default:
                        break;

                }
            } catch (JSONException e) {
                e.printStackTrace();
                LogUtil.showLog(" JSON failed --->", e.toString());
                Tools.Toast(getActivity(), "失败，JSON解析异常，异常Log：\n" + data);
            }

        }

    };
    public static String updateAPP = "/paiguanxia/files/app/";

    private void ExeInstallApp(String NetApkUrl, String NetApkName) {
        /*|||||||||||----------(这部分内容最好是在安装时就创建文件目录)------------||||||||||*/

        /////////////////////////////////////////////////////////////////////////////
        try {
            if (Tools.createSDCardDir(getActivity(), updateAPP)) {
                // --->文件夹创建成功
                progressDlg = new ProgressDialog(getActivity());
                notificationManager = (NotificationManager) getActivity().getSystemService(Activity.NOTIFICATION_SERVICE);

                /////////////////////////////////////////////////////////////
                try {
                    openApkUrl = "file:///sdcard/" + updateAPP + NetApkName;//下载完成以后安装apk
                    //开始下载
                    new MyDownloadAnsy().execute(NetApkUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else {
                Tools.Toast(getActivity(), "没有SD卡,无法完成升级");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Tools.Toast(getActivity(), "没有权限，请开启权限");
        }

        /////////////////////////////////////////////////////////////////////////////
    }




    /*|||||||||||||||||||||||||||||---下载开始--|||||||||||||||||||||||||||||||||*/

    public class MyDownloadAnsy extends AsyncTask<String, Integer, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            builder = new NotificationCompat.Builder(getContext())
                    .setSmallIcon(R.drawable.app_logo).setContentInfo("下载中...").setContentTitle("正在下载").setContentText("拍管侠");
            notification = builder.build();
        }

        @Override
        protected Integer doInBackground(String... params) {
            HttpURLConnection con = null;
            InputStream is = null;
            OutputStream os = null;
            try {
                URL url = new URL(params[0]);
                con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(5 * 1000);  //设置超时时间
                if (con.getResponseCode() == 200) { //判断是否连接成功
                    int fileLength = con.getContentLength();
                    is = con.getInputStream();    //获取输入
                    os = new FileOutputStream("/sdcard/" + updateAPP + apkName);
                    byte[] buffer = new byte[1024 * 1024 * 10];
                    long total = 0;
                    int count;
                    int pro1 = 0;
                    int pro2 = 0;
                    while ((count = is.read(buffer)) != -1) {
                        total += count;
                        if (fileLength > 0)
                            pro1 = (int) (total * 100 / fileLength);  //传递进度（注意顺序）
                        if (pro1 != pro2)
                            publishProgress(pro2 = pro1);
                        os.write(buffer, 0, count);
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                    if (os != null) {
                        os.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (con != null) {
                    con.disconnect();
                }
            }
            return 1;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);

            if (result == 1) {
                Toast.makeText(getActivity(), "下载完成", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            builder.setProgress(100, values[0], false);
            notification = builder.build();
            notificationManager.notify(0, notification);
            if (values[0] == 100) {    //下载完成后点击安装
                Intent it = new Intent(Intent.ACTION_VIEW);
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                it.setDataAndType(Uri.parse(openApkUrl), "application/vnd.android.package-archive");
                PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, it, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentInfo("点击安装").setContentTitle("下载完成").setContentText("拍管侠").setContentIntent(pendingIntent);
                notification = builder.build();
                notificationManager.notify(0, notification);
            }

            /**
             * -------------------------------------------------------->
             */
            progressDlg.setTitle("更新进度");
            progressDlg.setMessage("正在下载...");
            progressDlg.setIcon(R.drawable.app_logo);
            progressDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDlg.setCancelable(false);
            progressDlg.show();
            progressDlg.setMax(100);
            //设置ProgressDialog的当前进度 
            progressDlg.setProgress(values[0]);
            if (values[0] == 100) {
                progressDlg.setMessage("下载完成");
                // 通过Intent安装APK文件
                /*
                 * 如果没有android.os.Process.killProcess(android.os.Process.myPid());最后不会提示完成、打开。
                 * 如果没有i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);这一步的话，最后安装好了，点打开，是不会打开新版本应用的
                 */
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setDataAndType(Uri.parse(openApkUrl), "application/vnd.android.package-archive");
                startActivity(i);
                android.os.Process.killProcess(android.os.Process.myPid());//杀死软件进程
            }
        }
    }
    /*|||||||||||||||||||||||||||||---下载结束--|||||||||||||||||||||||||||||||||*/
}