package com.example.yzwy.lprmag;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yzwy.lprmag.bean.GeomDataBean;
import com.example.yzwy.lprmag.control.activityStackExtends.util.ActivityStackManager;
import com.example.yzwy.lprmag.myConstant.OrderConstant;
import com.example.yzwy.lprmag.myConstant.WifiMsgConstant;
import com.example.yzwy.lprmag.util.DisplayUtil;
import com.example.yzwy.lprmag.util.HanderUtil;
import com.example.yzwy.lprmag.util.InetAddressUtil;
import com.example.yzwy.lprmag.util.LogUtil;
import com.example.yzwy.lprmag.util.NetUtils;
import com.example.yzwy.lprmag.util.Tools;
import com.example.yzwy.lprmag.view.LoadingDialog;
import com.example.yzwy.lprmag.wifimess.model.SendOrder;
import com.example.yzwy.lprmag.wifimess.util.SocketUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.yzwy.lprmag.util.Tools.getWifiRouteIPAddress;

//import net.sf.json.JSONArray;

/**
 * #################################################################################################
 * Copyright: Copyright (c) 2018
 * Created on 2019-04-03
 * Author: 仲超(zhongchao)
 * Version 1.0
 * Describe: 地磁管理界面
 * #################################################################################################
 */
public class GeomagneticManageActivity extends AppCompatActivity {

    /**
     *
     * */
    private EditText edt_locIP_tremconn;
    /**
     *
     * */
    private EditText edt_wwwnet_tremconn;
    /**
     *
     * */
    private EditText edt_nettype_tremconn;
    /**
     *
     * */
    private EditText edt_conntern_tremconn;
    /**
     *
     * */
    //private EditText edt_termaddress_tremdatamag;
    /**
     *
     * */
    private Button btn_enter_tremdatamag;
    private ImageButton imgbtn_back_tremdatamag;
    private Button btn_f5data_tremdatamag;
    //private EditText edt_termpriority_tremdatamag;


    /**
     * 定义适配器
     */
    private GeomRestAdapter adapter;


    /**
     * 定义Bean类型的数组
     */
    private List<GeomDataBean> adapterBeanList = new ArrayList<>();
    private int spanCount = 1;
    private LinearLayout li_rcv_geom_rest;

    private boolean isSelectAll = false;
    private CheckBox ckb_selectall_gemorest;


    SparseBooleanArray mSelectedPositions = new SparseBooleanArray();
    private LoadingDialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ly_trem_geom_mag);


        //==========================================================================================
        ActivityStackManager.getInstance().addActivity(this);

        //加载View
        initView();

        //设置输入框不可编辑
        setEnabled();

        //设置显示的字符
        setEditText();

        //向终端获取热点信息
        GetTerminalDataInfo();

        //按钮监听事件
        initOnClick();


//        //加载Bean数据
//        initBeanData();
        //加载适配器
        initAdapter();
    }

    /**
     * =============================================================================================
     * 加載View
     */
    private void initView() {
        edt_locIP_tremconn = (EditText) findViewById(R.id.edt_locIP_tremconn);
        edt_wwwnet_tremconn = (EditText) findViewById(R.id.edt_wwwnet_tremconn);
        edt_nettype_tremconn = (EditText) findViewById(R.id.edt_nettype_tremconn);
        edt_conntern_tremconn = (EditText) findViewById(R.id.edt_conntern_tremconn);

        //edt_termaddress_tremdatamag = (EditText) findViewById(R.id.//edt_termaddress_tremdatamag);
        //edt_termpriority_tremdatamag = (EditText) findViewById(R.id.edt_termpriority_tremdatamag);

        btn_enter_tremdatamag = (Button) findViewById(R.id.btn_enter_tremdatamag);
        imgbtn_back_tremdatamag = (ImageButton) findViewById(R.id.imgbtn_back_tremdatamag);
        btn_f5data_tremdatamag = (Button) findViewById(R.id.btn_f5data_tremdatamag);


        li_rcv_geom_rest = (LinearLayout) findViewById(R.id.li_rcv_geom_rest);


        ckb_selectall_gemorest = (CheckBox) findViewById(R.id.ckb_selectall_gemorest);
    }

    /**
     * =============================================================================================
     * 按钮监听事件
     */
    private void initOnClick() {

        /**
         * 确认修改
         * */
        btn_enter_tremdatamag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //数据修改
                GeomDadaRest();
            }
        });

        /**
         * 返回
         * */
        imgbtn_back_tremdatamag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeomagneticManageActivity.this.finish();
            }
        });

        /**
         * 刷新
         * */
        btn_f5data_tremdatamag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEditText();

                ckb_selectall_gemorest.setChecked(false);
                initSetWelectAllList(false);

                //向终端获取热点信息
                GetTerminalDataInfo();
                Tools.Toast(GeomagneticManageActivity.this, "刷新成功~");
            }
        });


        ckb_selectall_gemorest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Tools.Toast(GeomagneticManageActivity.this, ckb_selectall_gemorest.isChecked() + "");
                if (ckb_selectall_gemorest.isChecked()) {
                    ckb_selectall_gemorest.setChecked(true);
                    initSetWelectAllList(true);
                } else {
                    ckb_selectall_gemorest.setChecked(false);
                    initSetWelectAllList(false);
                }
                adapter.notifyDataSetChanged();
                //Tools.Toast(GeomagneticManageActivity.this, "已选择" + getmSelectedItemSize() + "项");
            }
        });
//        ckb_selectall_gemorest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Tools.Toast(GeomagneticManageActivity.this, isChecked + "");
//                initSetWelectAllList(isChecked);
//                adapter.notifyDataSetChanged();
//                Tools.Toast(GeomagneticManageActivity.this, "已选择" + getmSelectedItemSize() + "项");
//            }
//        });
    }

    /**
     * @param isSelectBool
     */
    private void initSetWelectAllList(boolean isSelectBool) {
        if (adapterBeanList.size() <= 0) return;
        for (int i = 0; i < adapterBeanList.size(); i++) {
            adapterBeanList.get(i).setSelectboll(isSelectBool);
            setItemChecked(i, isSelectBool);
        }
    }

    /**
     * =============================================================================================
     * 存储配置信息
     */
    private boolean GeomDadaRest() {


        /**
         * 获取所有的数据框的数据，去除首位空格
         * */
        String termpriority = "";
        String termaddress = "";
//        String termpriority = edt_termpriority_tremdatamag.getText().toString().trim();
//        String termaddress = edt_termaddress_tremdatamag.getText().toString().trim();
//
//        //==========================================================================================
//        //
//        if (termaddress.equals("")) {
//            Tools.Toast(GeomagneticManageActivity.this, "终端地址不能为空，请重新输入");
//            return false;
//        }

        loadingDialog = new LoadingDialog(this, "正在提交数据...", R.mipmap.ic_dialog_loading);
        loadingDialog.show();

        JSONArray jsonGeom = new JSONArray();
        ArrayList<GeomDataBean> selectedItemData = getSelectedItemData();
        for (int i = 0; i < selectedItemData.size(); i++) {
            try {
                JSONObject jsonObjArray = new JSONObject();
                String geomID = selectedItemData.get(i).getGeomID();
                jsonObjArray.put("geomID", geomID);
                jsonGeom.put(jsonObjArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        LogUtil.showLog("#--->", jsonGeom.toString());

        //向终端发送设置终端热点的命令
        SetTerminalDataInfo(jsonGeom);

        return true;
    }

    /**
     * =============================================================================================
     * 向终端发送获取终端热点的命令
     */
    private void GetTerminalDataInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String socketServerMsg = SocketUtil.getInstance().SocketRequest(getWifiRouteIPAddress(GeomagneticManageActivity.this), WifiMsgConstant.PORT_wifi, SendOrder.Get_GeomRest());
                    LogUtil.showLog("SocketUtilRs /...", socketServerMsg);
                    HanderUtil.HanderMsgSend(handler, 100, "data", socketServerMsg);
                } catch (final IOException e) {
                    e.printStackTrace();
                    LogUtil.showLog("SocketUtilRs /***", e.toString());
                    HanderUtil.HanderMsgSend(handler, 101, "data", e.toString());
                }
            }
        }).start();
    }

    /**
     * =============================================================================================
     * 向终端发送设置终端热点的命令
     */
    private void SetTerminalDataInfo(final JSONArray jsonGeom) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String socketServerMsg = SocketUtil.getInstance().SocketRequest(getWifiRouteIPAddress(GeomagneticManageActivity.this), WifiMsgConstant.PORT_wifi, SendOrder.Set_GeomRest(jsonGeom));
                    LogUtil.showLog("socketServerMsg /...", socketServerMsg);
                    HanderUtil.HanderMsgSend(handler, 100, "data", socketServerMsg);
                } catch (final IOException e) {
                    e.printStackTrace();
                    LogUtil.showLog("socketServerMsg /***", e.toString());
                    HanderUtil.HanderMsgSend(handler, 101, "data", e.toString());
                }
            }
        }).start();
    }

    /**
     * =============================================================================================
     * 设置 EditText 不可编辑
     */
    private void setEnabled() {
        edt_locIP_tremconn.setEnabled(false);
        edt_wwwnet_tremconn.setEnabled(false);
        edt_nettype_tremconn.setEnabled(false);
        edt_conntern_tremconn.setEnabled(false);
    }

    /**
     * =============================================================================================
     * 初始化值
     */
    private void setEditText() {

        String InetAddress = InetAddressUtil.getIP();
        if (InetAddress == null || InetAddress.equals("")) {
            edt_locIP_tremconn.setText("0.0.0.0");
        } else {
            edt_locIP_tremconn.setText(InetAddressUtil.getIP());
        }


        boolean netConnected = NetUtils.isNetConnected(GeomagneticManageActivity.this);
        if (netConnected) {
            edt_wwwnet_tremconn.setText("Internet网访问");
        } else {
            edt_wwwnet_tremconn.setText("网络未连接");
        }

        //java数组初始化
        String[] networkStateTypeArray = {"没有网络连接", "wifi连接", "2G", "3G", "4G", "手机流量"};
        int networkStateType = NetUtils.getNetworkState(GeomagneticManageActivity.this);
        edt_nettype_tremconn.setText(networkStateTypeArray[networkStateType]);


    }


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String dataMsg = msg.getData().getString("data");
            System.out.println("返回的数据>>>>>>>>>>>>>" + dataMsg);
            if (loadingDialog != null) loadingDialog.cancel();
            switch (msg.what) {

                case 100:
                    try {
                        JSONObject jsonObject = new JSONObject(dataMsg);
                        int Order = Integer.valueOf(jsonObject.getString("Order"));
                        String errmsg = jsonObject.getString("errmsg");
                        String errcode = jsonObject.getString("errcode");

                        switch (Order) {

                            //获取终端上的数据
                            case OrderConstant.ORDER_GET_GeomList:
                                String data = jsonObject.getString("data");
                                edt_conntern_tremconn.setText("是");


                                //加载Bean数据
                                initBeanData(dataMsg);

                                adapter.notifyDataSetChanged(); //更新界面

                                //加载适配器
                                //initAdapter();

                                break;

                            //设置终端上的数据
                            case OrderConstant.ORDER_SET_GeomListRest:
                                if (errcode.equals("0")) {
                                    Tools.Toast(GeomagneticManageActivity.this, "数据设置成功");
                                } else {
                                    Tools.Toast(GeomagneticManageActivity.this, errmsg);
                                }
                                break;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Tools.Toast(GeomagneticManageActivity.this, "终端数据获取异常");

                    }
                    break;

                case 101:
                    Tools.Toast(GeomagneticManageActivity.this, "终端连接异常，请检查~");
                    LogUtil.showLog("ResSocket >>>", dataMsg);
                    edt_conntern_tremconn.setText("否");
                    break;
            }

        }
    };

    /**
     * =============================================================================================
     * 加载数据
     *
     * @param data
     */
    private void initBeanData(String data) {


        if (adapterBeanList.size() > 0) {
            adapterBeanList.clear();
        }
        try {
            JSONObject jsonObjectALL = new JSONObject(data);
            JSONArray jsonArray = jsonObjectALL.getJSONArray("data");
            //JSONArray jsonArray = jsonObjectALL.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                // JSON数组里面的具体-JSON对象
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String geomID = jsonObject.optString("geomID", null);
                String PersetNumber = jsonObject.optString("PersetNumber", null);
                adapterBeanList.add(new GeomDataBean(i, i + 1, false, geomID, PersetNumber));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //adapterBeanList.add(new GeomDataBean(1, 2, false, "01010101"));
        //adapterBeanList.add(new GeomDataBean(2, 3, false, "01000011"));
        //adapterBeanList.add(new GeomDataBean(3, 4, false, "01001001"));
        //adapterBeanList.add(new GeomDataBean(4, 5, false, "01101010"));
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
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recv_item_geom_rest);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new GeomRestAdapter(adapterBeanList);
        recyclerView.setAdapter(adapter);


        //设置边距
        recyclerView.addItemDecoration(new SpaceItemDecoration());


    }

    /**
     * =============================================================================================
     * 适配器 列表
     */
    private class GeomRestAdapter extends RecyclerView.Adapter<GeomRestAdapter.ViewHolder> {


        private List<GeomDataBean> dataBeanList;

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView tv_persetNumber_item_gemorest;
            private View mAdapterView;
            private TextView tv_xuhao_item_gemorest;
            private TextView tv_geomnum_item_gemorest;
            private CheckBox ckb_fuxuan_item_gemorest;
            //private Button btn_enter_tremdatamag;
            private LinearLayout li_geom_rest_item_gemorest;

            ViewHolder(View view) {
                super(view);
                mAdapterView = view;
                tv_xuhao_item_gemorest = (TextView) view.findViewById(R.id.tv_xuhao_item_gemorest);
                tv_geomnum_item_gemorest = (TextView) view.findViewById(R.id.tv_geomnum_item_gemorest);
                //btn_enter_tremdatamag = (Button) view.findViewById(R.id.btn_enter_tremdatamag);
                ckb_fuxuan_item_gemorest = (CheckBox) view.findViewById(R.id.ckb_fuxuan_item_gemorest);
                li_geom_rest_item_gemorest = (LinearLayout) view.findViewById(R.id.li_geom_rest_item_gemorest);
                tv_persetNumber_item_gemorest = (TextView) view.findViewById(R.id.tv_persetNumber_item_gemorest);
            }
        }


        GeomRestAdapter(List<GeomDataBean> dataBeans) {
            this.dataBeanList = dataBeans;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_geom_rest, parent, false);
            final ViewHolder holder = new ViewHolder(view);

//            holder.ckb_fuxuan_item_gemorest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                    int position = holder.getAdapterPosition();
//
//                    if (isChecked) {
//                        if (isItemChecked(position)) {
//                            setItemChecked(position, false);
//                        } else {
//                            setItemChecked(position, true);
//                        }
//                        Tools.Toast(GeomagneticManageActivity.this, "position:" + position + "已选择" + getmSelectedItemSize() + "项");
//
//                    } else {
//                        if (isItemChecked(position)) {
//                            setItemChecked(position, false);
//                        } else {
//                            setItemChecked(position, true);
//                        }
//                        Tools.Toast(GeomagneticManageActivity.this, "position:" + position + "已选择" + getmSelectedItemSize() + "项");
//
//                    }
//                }
//            });


            holder.ckb_fuxuan_item_gemorest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    GeomDataBean customDataBean = dataBeanList.get(position);
                    if (isItemChecked(position)) {
                        setItemChecked(position, false);
                        holder.ckb_fuxuan_item_gemorest.setChecked(false);
                    } else {
                        setItemChecked(position, true);
                        holder.ckb_fuxuan_item_gemorest.setChecked(true);
                    }
                    if (getmSelectedItemSize() == dataBeanList.size()) {
                        ckb_selectall_gemorest.setChecked(true);
                    } else {
                        ckb_selectall_gemorest.setChecked(false);
                    }
                    //Tools.Toast(GeomagneticManageActivity.this, "position:" + position + "已选择" + getmSelectedItemSize() + "项");
                }
            });
            holder.mAdapterView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    GeomDataBean customDataBean = dataBeanList.get(position);
                    if (isItemChecked(position)) {
                        setItemChecked(position, false);
                        holder.ckb_fuxuan_item_gemorest.setChecked(false);
                    } else {
                        setItemChecked(position, true);
                        holder.ckb_fuxuan_item_gemorest.setChecked(true);
                    }
                    if (getmSelectedItemSize() == dataBeanList.size()) {
                        ckb_selectall_gemorest.setChecked(true);
                    } else {
                        ckb_selectall_gemorest.setChecked(false);
                    }
                    //Tools.Toast(GeomagneticManageActivity.this, "position:" + position + "已选择" + getmSelectedItemSize() + "项");
                }
            });


            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            GeomDataBean dataBean = dataBeanList.get(position);
            holder.tv_xuhao_item_gemorest.setText(String.valueOf(dataBean.getKeyID()));
            holder.tv_geomnum_item_gemorest.setText(dataBean.getGeomID());
            //holder.ckb_fuxuan_item_gemorest.setText(dataBean.getGeomID());
            holder.ckb_fuxuan_item_gemorest.setChecked(dataBean.isSelectboll());
            holder.tv_persetNumber_item_gemorest.setText(dataBean.getPersetNumber());


            if (position % 2 != 0) {
                holder.li_geom_rest_item_gemorest.setBackgroundResource(R.color.white_e);
            }

        }


        /**
         * =========================================================
         * 返回多少个布局
         */
        @Override
        public int getItemCount() {
            return dataBeanList.size();
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
            return position;
        }


    }

    //通过条目位置得到该条目的Boolean值，就可以知道条目有没有选中这时可以在onbindview里设置chekbox的状态了，同时设置chekbox的监听
    //根据位置判断条目是否选中
    private boolean isItemChecked(int position) {
        return mSelectedPositions.get(position);
    }

    //用来为Adapter 里的数据item设置标记，默认每个条目为false，选中的话就设置为true
    private void setItemChecked(int position, boolean isChecked) {
        mSelectedPositions.put(position, isChecked);
    }


    //用来为Adapter 里的数据item设置标记，默认每个条目为false，选中的话就设置为true
    private int getmSelectedItemSize() {
        int mSize = 0;
        for (int i = 0; i < mSelectedPositions.size(); i++) {
            if (mSelectedPositions.get(i)) {
                mSize++;
                continue;
            }
        }
        return mSize;
    }


    //获得选中条目的结果
    public ArrayList<GeomDataBean> getSelectedItemData() {
        ArrayList<GeomDataBean> selectList = new ArrayList<>();
        for (int i = 0; i < adapterBeanList.size(); i++) {
            if (isItemChecked(i)) {
                selectList.add(adapterBeanList.get(i));
            }
        }
        return selectList;
    }


    //设置边距
    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int normal = 0;
        private int margin = DisplayUtil.dip2px(GeomagneticManageActivity.this, 5);
        private int RcVPaTop = DisplayUtil.dip2px(GeomagneticManageActivity.this, 10);
        private int RcVPaBottom = DisplayUtil.dip2px(GeomagneticManageActivity.this, 10);


//        private int RcVPaTop;
//        private int RcVPaBottom;
//        private int RcVPaLeft;
//        private int RcVPaRight;


//        public SpaceItemDecoration(int normal, int margin) {
//            this.normal = normal;
//            this.margin = margin;
//        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);

            int itemCount = adapter.getItemCount();
            int viewPosition = parent.getChildAdapterPosition(view) + 1;

            //LogUtil.showLog("#--->","itemCount:"+itemCount +" === "+"viewPosition:"+parent.getChildAdapterPosition(view));

            //outRect.top = margin;
            //outRect.bottom = margin;
            outRect.left = margin * 2;


            //LogUtil.showLog("#--->3", "itemCount % spanCount:" + (viewPosition % spanCount));
            //LogUtil.showLog("#--->2", "itemCount % spanCount:" + (viewPosition % 2));
            //LogUtil.showLog("#--->1", "itemCount % spanCount:" + (viewPosition % 1));

            /**
             * 设置最右边的距离，查询最右边的item
             * */
            if (viewPosition % spanCount == 0) {
                outRect.right = margin * 2;
            }

//            /**
//             * 设置最最左边的距离，查询最右边的item
//             * */
//            if (spanCount > 1) {
//                if (viewPosition % spanCount == 1) {
//                    outRect.right = margin * 2;
//                }
//            } else {
//                if (viewPosition % spanCount == 0) {
//                    outRect.right = margin * 2;
//                }
//            }

            /**
             * itemCount 从1开始标记
             * viewPosition 从1开始标记
             * */
            if (itemCount > 0) {
                /**
                 * 第一行
                 * */
                if (viewPosition <= spanCount) {
                    outRect.top = RcVPaTop;
                }

                /**
                 * 最后一行
                 * */
                if (viewPosition + spanCount > itemCount) {
                    outRect.bottom = RcVPaBottom;
                }

            }


        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loadingDialog != null) loadingDialog.show();
    }
}

