package com.example.yzwy.lprmag;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yzwy.lprmag.bean.UseCourseBean;
import com.example.yzwy.lprmag.broadcast.NetWorkChangReceiver;
import com.example.yzwy.lprmag.myConstant.HttpUrl;
import com.example.yzwy.lprmag.myinterface.NetBroadcastListener;
import com.example.yzwy.lprmag.util.HanderMsg;
import com.example.yzwy.lprmag.util.LogUtil;
import com.example.yzwy.lprmag.util.OkHttpUtil;
import com.example.yzwy.lprmag.util.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;


/**
 * #################################################################################################
 * Copyright: Copyright (c) 2018
 * Created on 2019-04-03
 * Author: 仲超(zhongchao)
 * Version 1.0
 * Describe: 使用教程列表页
 * #################################################################################################
 */
public class UseCourseListActivity extends Activity implements NetBroadcastListener {


    /**
     * 定义适配器
     */
    private UseCourseAdapter adapter;

    /**
     * 定义Bean类型的数组
     */
    private List<UseCourseBean> useCourseBeanList = new ArrayList<UseCourseBean>();

    /**
     * 关闭页面
     */
    private ImageButton imgbtn_back_usecourse;


    /**
     * 网络实时监听
     */
    private NetWorkChangReceiver netWorkStateReceiver;


    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ly_usecourse);


        /**
         * 获取Intent值
         * */
        initIntentData();


        /**
         * 初始化组件
         * */
        initView();


        /**
         * 服务器上获取使用教程列表
         * */
        GetAllUseCourseListRequest();

    }

    @Override
    public void netBroadcastReceiver(int netStatus, int netMobile, boolean isAvailable) {
        Tools.Toast(UseCourseListActivity.this, "网络状态:" + netStatus + "  网络类型:" + netMobile + "  网络是否可用:" + isAvailable);
        LogUtil.showLog("NetBroadcast --->", "网络状态:" + netStatus + "  网络类型:" + netMobile + "  网络是否可用:" + isAvailable);
    }


    //在onResume()方法注册
    @Override
    protected void onResume() {

        if (netWorkStateReceiver == null) {
            netWorkStateReceiver = new NetWorkChangReceiver(this);
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkStateReceiver, filter);
        System.out.println("实时监测网络注册");
        super.onResume();
    }

    //onPause()方法注销
    @Override
    protected void onPause() {
        unregisterReceiver(netWorkStateReceiver);
        System.out.println("实时监测网络注销");
        super.onPause();
    }

    /**
     * =============================================================================================
     * 获取Intent中的值（视频播放ID）
     */
    private void initIntentData() {

//        m_iPlayID = Integer.valueOf(getIntent().getStringExtra("m_iPlayID"));
//        Bundle receive = getIntent().getExtras();
//        //得到随Intent传递过来的Bundle对象
//        scwidth_hik = receive.getString("scwidth_hik");
//        scheight_hik = receive.getString("scheight_hik");
//        m_iPlayID = Integer.valueOf(receive.getString("m_iPlayID"));

    }


    /**
     * =============================================================================================
     * 初始化组件
     */
    public void initView() {

        imgbtn_back_usecourse = (ImageButton) findViewById(R.id.imgbtn_back_usecourse);

        /**
         * 按钮事件
         * */
        initClick();
    }

    /**
     * =============================================================================================
     * 加载RecyclerView适配器
     */
    public void initAdapter() {

        /**
         * -----------------------------------------------------------------------------------------
         * 获取预置点列表
         * */
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recv_item_setPreset);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new UseCourseAdapter(useCourseBeanList);
        recyclerView.setAdapter(adapter);

    }


    /**
     * =============================================================================================
     * 服务器上获取使用教程列表
     */
    private void GetAllUseCourseListRequest() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                Map<String, String> LoginStringMap = new HashMap<>();
                LoginStringMap.put("ID", "");
                //LoginStringMap.put("passWord", edtPwd);
                OkHttpUtil.getInstance().postDataAsyn(HttpUrl.LoginUrl, LoginStringMap, new OkHttpUtil.MyNetCall() {
                    @Override
                    public void success(Call call, Response response) throws IOException {
                        String rs = response.body().string();
                        HanderMsg.HanderMsgSend(handler, 100, rs);
                        LogUtil.showLog("UseCourseListActivity success --->", rs);
                    }

                    @Override
                    public void failed(Call call, IOException e) {
                        HanderMsg.HanderMsgSend(handler, 101, e.toString());
                        LogUtil.showLog("UseCourseListActivity failed --->", e.toString());
                    }
                });


            }
        }).start();
    }


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {


            final String dataMsg = msg.getData().getString("data");

            switch (msg.what) {

                case 100:

                    try {
                        JSONObject jsonObject = new JSONObject(dataMsg);
                        JSONArray jsonArrayPrset = jsonObject.getJSONArray("data");
                        if (useCourseBeanList.size() > 0) {
                            useCourseBeanList.clear();//清除之前的数据
                        }
                        for (int i = 0; i < jsonArrayPrset.length(); i++) {//内部不锁定，效率最高，但在多线程要考虑并发操作的问题。
                            JSONObject jsonArrayPrsetObj = jsonArrayPrset.getJSONObject(i);
                            UseCourseBean useCourseBean = new UseCourseBean(jsonArrayPrsetObj.getString("name"), jsonArrayPrsetObj.getString("id"));
                            useCourseBeanList.add(useCourseBean);
                        }
                        /**
                         * 初始化适配器
                         * */
                        initAdapter();
                        adapter.notifyDataSetChanged(); //更新界面
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;

                case 101:
                    Tools.Toast(UseCourseListActivity.this, "网络异常，请检查~");
                    LogUtil.showLog("UseCourseListActivity >>>", dataMsg);
                    break;
            }
        }
    };


    /**
     * =============================================================================================
     * 加载事件监听器
     */
    public void initClick() {

        imgbtn_back_usecourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });

    }

    /**
     * =============================================================================================
     * 活动销毁活动生命周期
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //结束当前活动
        this.finish();
    }

    /**
     * =============================================================================================
     * 单击退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
        }
        return false;
    }

    private void exit() {
        //结束当前活动
        finish();
    }


    /**
     * =============================================================================================
     * 适配器 列表
     */
    private class UseCourseAdapter extends RecyclerView.Adapter<UseCourseAdapter.ViewHolder> {

        private List<UseCourseBean> useCourseBeanList;

        class ViewHolder extends RecyclerView.ViewHolder {
            private View mUseCourseView;
            private TextView tv_word_item_usecourse;
            private TextView tv_keyid_item_usecourse;

            ViewHolder(View view) {
                super(view);
                mUseCourseView = view;
                tv_word_item_usecourse = (TextView) view.findViewById(R.id.tv_word_item_usecourse);
                tv_keyid_item_usecourse = (TextView) view.findViewById(R.id.tv_keyid_item_usecourse);
            }
        }

        UseCourseAdapter(List<UseCourseBean> useCourseBeanList) {
            this.useCourseBeanList = useCourseBeanList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usecourse, parent, false);
            final ViewHolder holder = new ViewHolder(view);
            holder.mUseCourseView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    UseCourseBean useCourseBean = useCourseBeanList.get(position);


                    Intent in = new Intent(UseCourseListActivity.this, UseCourseDetailsActivity.class);
                    in.putExtra("ID", useCourseBean.getUseCourseKeyID());
                    in.putExtra("MycusTitle", useCourseBean.getUseCourseName());
                    UseCourseListActivity.this.startActivity(in);

                    //Tools.IntentDataBack(UseCourseListActivity.this, UseCourseDetailsActivity.class, "ID", useCourseBean.getUseCourseKeyID());

                    Toast.makeText(v.getContext(), "you clicked view  列表  " + useCourseBean.getUseCourseName(), Toast.LENGTH_SHORT).show();
                }
            });

            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            UseCourseBean useCourseBean = useCourseBeanList.get(position);


            holder.tv_word_item_usecourse.setText(useCourseBean.getUseCourseName());
            holder.tv_keyid_item_usecourse.setText(useCourseBean.getUseCourseKeyID());
        }


        /**
         * =========================================================
         * 返回多少个布局
         */
        @Override
        public int getItemCount() {
            return useCourseBeanList.size();
        }

        /**
         * =========================================================
         * 返回具体item 的 item ID 号
         */
        @Override
        public long getItemId(int id) {

            return id;
        }

        @Override
        public int getItemViewType(int position) {
            return (position % 2);
        }

    }
}