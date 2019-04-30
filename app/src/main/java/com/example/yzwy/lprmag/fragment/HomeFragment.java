package com.example.yzwy.lprmag.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yzwy.lprmag.ConfigSetActivity;
import com.example.yzwy.lprmag.HiKCameraActivity;
import com.example.yzwy.lprmag.R;
import com.example.yzwy.lprmag.TerminalDataManageActivity;
import com.example.yzwy.lprmag.WifiHotMagActivity;
import com.example.yzwy.lprmag.bean.CustomServiceDataBean;
import com.example.yzwy.lprmag.control.activityStackExtends.util.ActivityStackManager;
import com.example.yzwy.lprmag.util.Tools;
import com.example.yzwy.lprmag.view.XCRoundRectImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 仲超(zhongchao) on 2019/03/26.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private View view;
    //    private ImageView img_hik_frgmthome;
//    private TextView tv_hik_frgmthome;
//    private ImageView img_config_frgmthome;
//    private TextView tv_config_frgmthome;
//    private ImageView img_other_frgmthome;
//    private TextView tv_other_frgmthome;
//    private ImageView img_hotConfig_frgmthome;
//    private TextView tv_hotConfig_frgmthome;
    private LinearLayout li_hik_frgmthome;
    private LinearLayout li_config_frgmthome;
    private LinearLayout li_hotConfig_frgmthome;
    private LinearLayout li_termdatamag_frgmthome;


    /**
     * 定义适配器
     */
    private CustomServiceAdapter adapter;


    /**
     * 定义Bean类型的数组
     */
    private List<CustomServiceDataBean> adapterBeanList = new ArrayList<CustomServiceDataBean>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ActivityStackManager.getInstance().addActivity(getActivity());

        //初始化组件
        initView();

    }

    /**
     * =============================================================================================
     * 初始化组件
     */
    private void initView() {
//        img_hik_frgmthome = (ImageView) view.findViewById(R.id.img_hik_frgmthome);
//        tv_hik_frgmthome = (TextView) view.findViewById(R.id.tv_hik_frgmthome);
//        img_config_frgmthome = (ImageView) view.findViewById(R.id.img_config_frgmthome);
//        tv_config_frgmthome = (TextView) view.findViewById(R.id.tv_config_frgmthome);
//
//        img_hotConfig_frgmthome = (ImageView) view.findViewById(R.id.img_hotConfig_frgmthome);
//        tv_hotConfig_frgmthome = (TextView) view.findViewById(R.id.tv_hotConfig_frgmthome);


        li_hik_frgmthome = (LinearLayout) view.findViewById(R.id.li_hik_frgmthome);
        li_config_frgmthome = (LinearLayout) view.findViewById(R.id.li_config_frgmthome);
        li_hotConfig_frgmthome = (LinearLayout) view.findViewById(R.id.li_hotConfig_frgmthome);
        li_termdatamag_frgmthome = (LinearLayout) view.findViewById(R.id.li_termdatamag_frgmthome);

//        img_other_frgmthome = (ImageButton) view.findViewById(R.id.img_other_frgmthome);
//        tv_other_frgmthome = (TextView) view.findViewById(R.id.tv_other_frgmthome);


        initOnClick();


    }


    /**
     * =============================================================================================
     * 事件监听
     */
    private void initOnClick() {
//        img_hik_frgmthome.setOnClickListener(this);
//        tv_hik_frgmthome.setOnClickListener(this);
//        img_config_frgmthome.setOnClickListener(this);
//        tv_config_frgmthome.setOnClickListener(this);
//        img_hotConfig_frgmthome.setOnClickListener(this);
//        tv_hotConfig_frgmthome.setOnClickListener(this);


        li_hik_frgmthome.setOnClickListener(this);
        li_config_frgmthome.setOnClickListener(this);
        li_hotConfig_frgmthome.setOnClickListener(this);
        li_termdatamag_frgmthome.setOnClickListener(this);

//        img_other_frgmthome.setOnClickListener(this);
//        tv_other_frgmthome.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

//            //======================================================================================
//            //海康设备管理和预览
//            case R.id.img_hik_frgmthome:
//                BtnHiKOnClick();
//                break;
//            case R.id.tv_hik_frgmthome:
//                BtnHiKOnClick();
//                break;
//
//            //======================================================================================
//            //配置管理
//            case R.id.img_config_frgmthome:
//                BtnConfigOnClick();
//                break;
//            case R.id.tv_config_frgmthome:
//                BtnConfigOnClick();
//                break;
//
//            //======================================================================================
//            //热点管理
//            case R.id.img_hotConfig_frgmthome:
//                WifiHotMagOnClick();
//                break;
//            case R.id.tv_hotConfig_frgmthome:
//                WifiHotMagOnClick();
//                break;


            //======================================================================================
            //海康设备管理和预览
            case R.id.li_hik_frgmthome:
                BtnHiKOnClick();
                break;

            //======================================================================================
            //配置管理
            case R.id.li_config_frgmthome:
                BtnConfigOnClick();
                break;

            //======================================================================================
            //热点管理
            case R.id.li_hotConfig_frgmthome:
                WifiHotMagOnClick();
                break;


            //======================================================================================
            //终端数据管理
            case R.id.li_termdatamag_frgmthome:
                TermDataMagOnClick();
                break;

//            //======================================================================================
//            //其它
//            case R.id.img_other_frgmthome:
//                BtnOtherOnClick();
//                break;
//            case R.id.tv_other_frgmthome:
//                BtnOtherOnClick();
//                break;

            default:
                break;

        }
    }


    /**
     * =============================================================================================
     * 终端数据管理
     */
    private void TermDataMagOnClick() {
        Tools.IntentBack(getActivity(), TerminalDataManageActivity.class);
    }

    /**
     * =============================================================================================
     * 终端热点管理
     */
    private void WifiHotMagOnClick() {
        Tools.IntentBack(getActivity(), WifiHotMagActivity.class);
    }

    /**
     * =============================================================================================
     * 海康设备管理预览
     */
    private void BtnHiKOnClick() {
        Tools.IntentBack(getActivity(), HiKCameraActivity.class);
    }

    /**
     * =============================================================================================
     * 配置管理
     */
    private void BtnConfigOnClick() {
        Tools.IntentBack(getActivity(), ConfigSetActivity.class);
    }

    /**
     * =============================================================================================
     * 其它
     */
    private void BtnOtherOnClick() {
        Tools.Toast(getActivity(), "亲亲，攻城狮正在全力开发中~");
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
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recv_item_homefrgmt);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CustomServiceAdapter(adapterBeanList);
        recyclerView.setAdapter(adapter);

    }

    /**
     * =============================================================================================
     * 适配器 列表
     */
    private class CustomServiceAdapter extends RecyclerView.Adapter<CustomServiceAdapter.ViewHolder> {

        private List<CustomServiceDataBean> dataBeanList;

        class ViewHolder extends RecyclerView.ViewHolder {
            private View mAdapterView;
            private TextView tv_id_cusservice;
            private TextView tv_customerServiceType_cusservice;
            private TextView tv_customerService_cusservice;
            private XCRoundRectImageView xcrimg_bg_cusservice;

            ViewHolder(View view) {
                super(view);
                mAdapterView = view;
                tv_id_cusservice = (TextView) view.findViewById(R.id.tv_id_cusservice);
                tv_customerServiceType_cusservice = (TextView) view.findViewById(R.id.tv_customerServiceType_cusservice);
                tv_customerService_cusservice = (TextView) view.findViewById(R.id.tv_customerService_cusservice);
                xcrimg_bg_cusservice = (XCRoundRectImageView) view.findViewById(R.id.xcrimg_bg_cusservice);
            }
        }


        CustomServiceAdapter(List<CustomServiceDataBean> dataBeans) {
            this.dataBeanList = dataBeans;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cusservice, parent, false);
            final ViewHolder holder = new ViewHolder(view);
            holder.tv_customerService_cusservice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    CustomServiceDataBean customServiceDataBean = dataBeanList.get(position);
                }
            });

            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            CustomServiceDataBean dataBean = dataBeanList.get(position);


            holder.tv_id_cusservice.setText(dataBean.getID());
            holder.tv_customerServiceType_cusservice.setText(dataBean.getKey());
            holder.tv_customerService_cusservice.setText(dataBean.getVal());

            if (dataBean.getBgUrl().length() > 0) {

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
            return (position % 2);
        }

    }


}
