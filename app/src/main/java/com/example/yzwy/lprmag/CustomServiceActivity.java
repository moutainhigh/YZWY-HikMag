package com.example.yzwy.lprmag;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;

/**
 * #################################################################################################
 * Copyright: Copyright (c) 2018
 * Created on 2019-04-03
 * Author: 仲超(zhongchao)
 * Version 1.0
 * Describe: 客服中心页面
 * #################################################################################################
 */
public class CustomServiceActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 关闭页面
     */
    private ImageButton imgbtn_back_close;


//    private TextView tv_customerService_cusservice;
//    private TextView tv_technicalSupport_cusservice;
//    private TextView tv_troubleShooting_cusservice;
//
//    private TextView tv_customerServiceType_cusservice;
//    private TextView tv_technicalSupportType_cusservice;
//    private TextView tv_troubleShootingType_cusservice;


//    /**
//     * 定义适配器
//     */
//    private CustomServiceAdapter adapter;
//
//
//    /**
//     * 定义Bean类型的数组
//     */
//    private List<CustomServiceDataBean> adapterBeanList = new ArrayList<CustomServiceDataBean>();

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ly_cusservice);

        /**
         * 初始化组件
         * */
        initView();


    }

    /**
     * =============================================================================================
     * 初始化组件
     */
    public void initView() {

        imgbtn_back_close = (ImageButton) findViewById(R.id.imgbtn_left_cusservice);
//        tv_customerService_cusservice = (TextView) findViewById(R.id.tv_customerService_cusservice);
//        tv_technicalSupport_cusservice = (TextView) findViewById(R.id.tv_technicalSupport_cusservice);
//        tv_troubleShooting_cusservice = (TextView) findViewById(R.id.tv_troubleShooting_cusservice);
//
//        tv_customerServiceType_cusservice = (TextView) findViewById(R.id.tv_customerServiceType_cusservice);
//        tv_technicalSupportType_cusservice = (TextView) findViewById(R.id.tv_technicalSupportType_cusservice);
//        tv_troubleShootingType_cusservice = (TextView) findViewById(R.id.tv_troubleShootingType_cusservice);

        InitSpData();

        /**
         * 按钮事件
         * */
        initClick();
    }

    /**
     * =============================================================================================
     * 设置缓存值
     */
    private void InitSpData() {

//        if (SharePreferencesUtil.getBooleanValue(CustomServiceActivity.this, CustomServiceConstant.CustomService_MODEL, CustomServiceConstant.CustomService_MODEL_bool_default)) {
//            tv_customerService_cusservice.setText(SharePreferencesUtil.getStringValue(CustomServiceActivity.this, CustomServiceConstant.CustomService_customerService_str, CustomServiceConstant.CustomService_customerService_str_default));
//            tv_technicalSupport_cusservice.setText(SharePreferencesUtil.getStringValue(CustomServiceActivity.this, CustomServiceConstant.CustomService_technicalSupport_str, CustomServiceConstant.CustomService_technicalSupport_str_default));
//            tv_troubleShooting_cusservice.setText(SharePreferencesUtil.getStringValue(CustomServiceActivity.this, CustomServiceConstant.CustomService_troubleShooting_str, CustomServiceConstant.CustomService_troubleShooting_str_default));
//        }


    }


    /**
     * =============================================================================================
     * 加载事件监听器
     */
    public void initClick() {

//        imgbtn_back_close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                exit();
//            }
//        });


        imgbtn_back_close.setOnClickListener(this);
//        tv_customerService_cusservice.setOnClickListener(this);
//        tv_technicalSupport_cusservice.setOnClickListener(this);
//        tv_troubleShooting_cusservice.setOnClickListener(this);


    }


    /**
     * =============================================================================================
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }


    /**
     * =============================================================================================
     * 拨打电话（直接拨打电话）
     *
     * @param phoneNum 电话号码
     */
    public void callPhoneDirect(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent);
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


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.imgbtn_left_cusservice:
                exit();
                break;


//            case R.id.tv_customerService_cusservice:
//
//                String PhoneNum = tv_customerService_cusservice.getText().toString().trim();
//
//                callPhone(PhoneNum);
//
//                break;
//

//            case R.id.tv_technicalSupport_cusservice:
//
//                String PhoneNum1 = tv_customerService_cusservice.getText().toString().trim();
//
//                callPhone(PhoneNum1);
//
//                break;
//
//
//            case R.id.tv_troubleShooting_cusservice:
//
//                String PhoneNum2 = tv_customerService_cusservice.getText().toString().trim();
//
//                callPhone(PhoneNum2);
//
//                break;
        }

    }


//    /**
//     * =============================================================================================
//     * 加载RecyclerView适配器
//     */
//    public void initAdapter() {
//
//        /**
//         * -----------------------------------------------------------------------------------------
//         * 获取预置点列表
//         * */
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recv_item_cusservice);
//        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(layoutManager);
//        adapter = new CustomServiceAdapter(adapterBeanList);
//        recyclerView.setAdapter(adapter);
//
//    }
//
//    /**
//     * =============================================================================================
//     * 适配器 列表
//     */
//    private class CustomServiceAdapter extends RecyclerView.Adapter<CustomServiceAdapter.ViewHolder> {
//
//        private List<CustomServiceDataBean> dataBeanList;
//
//        class ViewHolder extends RecyclerView.ViewHolder {
//            private View mAdapterView;
//            private TextView tv_id_cusservice;
//            private TextView tv_customerServiceType_cusservice;
//            private TextView tv_customerService_cusservice;
//            private XCRoundRectImageView xcrimg_bg_cusservice;
//
//            ViewHolder(View view) {
//                super(view);
//                mAdapterView = view;
//                tv_id_cusservice = (TextView) view.findViewById(R.id.tv_id_cusservice);
//                tv_customerServiceType_cusservice = (TextView) view.findViewById(R.id.tv_customerServiceType_cusservice);
//                tv_customerService_cusservice = (TextView) view.findViewById(R.id.tv_customerService_cusservice);
//                xcrimg_bg_cusservice = (XCRoundRectImageView) view.findViewById(R.id.xcrimg_bg_cusservice);
//            }
//        }
//
//
//        CustomServiceAdapter(List<CustomServiceDataBean> dataBeans) {
//            this.dataBeanList = dataBeans;
//        }
//
//        @Override
//        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cusservice, parent, false);
//            final ViewHolder holder = new ViewHolder(view);
//            holder.tv_customerService_cusservice.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = holder.getAdapterPosition();
//                    CustomServiceDataBean customServiceDataBean = dataBeanList.get(position);
//                    callPhone(customServiceDataBean.getVal().trim());
//                }
//            });
//
//            return holder;
//        }
//
//        @Override
//        public void onBindViewHolder(ViewHolder holder, int position) {
//            CustomServiceDataBean dataBean = dataBeanList.get(position);
//
//
//            holder.tv_id_cusservice.setText(dataBean.getID());
//            holder.tv_customerServiceType_cusservice.setText(dataBean.getKey());
//            holder.tv_customerService_cusservice.setText(dataBean.getVal());
//
//            if (dataBean.getBgUrl().length() > 0) {
//                new MyAsyXcr(holder.xcrimg_bg_cusservice).execute(dataBean.getBgUrl());
//            }
//
//
//        }
//
//
//        /**
//         * =========================================================
//         * 返回多少个布局
//         */
//        @Override
//        public int getItemCount() {
//            return dataBeanList.size();
//        }
//
//        /**
//         * =========================================================
//         * 返回具体item 的 item ID 号
//         */
//        @Override
//        public long getItemId(int id) {
//
//            return id;
//        }
//
//        @Override
//        public int getItemViewType(int position) {
//            return (position % 2);
//        }
//
//    }

}