package com.example.yzwy.lprmag.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yzwy.lprmag.ConfigSetActivity;
import com.example.yzwy.lprmag.HiKCameraActivity;
import com.example.yzwy.lprmag.R;
import com.example.yzwy.lprmag.TerminalDataManageActivity;
import com.example.yzwy.lprmag.WifiHotMagActivity;
import com.example.yzwy.lprmag.bean.HomeMenuBean;
import com.example.yzwy.lprmag.control.activityStackExtends.util.ActivityStackManager;
import com.example.yzwy.lprmag.util.Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 仲超(zhongchao) on 2019/03/26.
 */
public class HomeFragment extends Fragment
//        implements View.OnClickListener
{

    private View view;
//    private LinearLayout li_hik_frgmthome;
//    private LinearLayout li_config_frgmthome;
//    private LinearLayout li_hotConfig_frgmthome;
//    private LinearLayout li_termdatamag_frgmthome;


    /**
     * 定义适配器
     */
    private FgemtHomeServiceAdapter adapter;


    /**
     * 定义Bean类型的数组
     */
    private List<HomeMenuBean> adapterBeanList = new ArrayList<>();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ActivityStackManager.getInstance().addActivity(getActivity());

        //初始化组件
        //initView();


        //加载Bean数据
        initBeanData();
        //加载适配器
        initAdapter();

    }

    /**
     * =============================================================================================
     * 加载数据
     */
    private void initBeanData() {

        if (adapterBeanList.size() > 0) {
            adapterBeanList.clear();
        }

        adapterBeanList.add(new HomeMenuBean(1, R.drawable.ic_tiaoshi, "设备调试"));
        adapterBeanList.add(new HomeMenuBean(2, R.drawable.ic_config_app, "设备连接配置"));
        adapterBeanList.add(new HomeMenuBean(3, R.drawable.ic_hot_block, "热点管理"));
        adapterBeanList.add(new HomeMenuBean(4, R.drawable.ic_term_data_mag, "终端数据管理"));
    }

//    /**
//     * =============================================================================================
//     * 初始化组件
//     */
//    private void initView() {
//
//        li_hik_frgmthome = (LinearLayout) view.findViewById(R.id.li_hik_frgmthome);
//        li_config_frgmthome = (LinearLayout) view.findViewById(R.id.li_config_frgmthome);
//        li_hotConfig_frgmthome = (LinearLayout) view.findViewById(R.id.li_hotConfig_frgmthome);
//        li_termdatamag_frgmthome = (LinearLayout) view.findViewById(R.id.li_termdatamag_frgmthome);
//
//
//        initOnClick();
//
//
//    }
//
//
//    /**
//     * =============================================================================================
//     * 事件监听
//     */
//    private void initOnClick() {
//        li_hik_frgmthome.setOnClickListener(this);
//        li_config_frgmthome.setOnClickListener(this);
//        li_hotConfig_frgmthome.setOnClickListener(this);
//        li_termdatamag_frgmthome.setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//
//
//            //======================================================================================
//            //海康设备管理和预览
//            case R.id.li_hik_frgmthome:
//                BtnHiKOnClick();
//                break;
//
//            //======================================================================================
//            //配置管理
//            case R.id.li_config_frgmthome:
//                BtnConfigOnClick();
//                break;
//
//            //======================================================================================
//            //热点管理
//            case R.id.li_hotConfig_frgmthome:
//                WifiHotMagOnClick();
//                break;
//
//
//            //======================================================================================
//            //终端数据管理
//            case R.id.li_termdatamag_frgmthome:
//                TermDataMagOnClick();
//                break;
//            default:
//                break;
//
//        }
//    }


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
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FgemtHomeServiceAdapter(adapterBeanList);
        recyclerView.setAdapter(adapter);

    }

    /**
     * =============================================================================================
     * 适配器 列表
     */
    private class FgemtHomeServiceAdapter extends RecyclerView.Adapter<FgemtHomeServiceAdapter.ViewHolder> {


        private List<HomeMenuBean> dataBeanList;

        class ViewHolder extends RecyclerView.ViewHolder {
            private View mAdapterView;
            private TextView tv_keyid_item_frgmthome;
            private TextView tv_homemenu_item_frgmthome;
            private ImageView img_homemenu_item_frgmthome;
            private LinearLayout li_homemenu_item_frgmthome;

            ViewHolder(View view) {
                super(view);
                mAdapterView = view;
                tv_keyid_item_frgmthome = (TextView) view.findViewById(R.id.tv_keyid_item_frgmthome);
                tv_homemenu_item_frgmthome = (TextView) view.findViewById(R.id.tv_homemenu_item_frgmthome);
                img_homemenu_item_frgmthome = (ImageView) view.findViewById(R.id.img_homemenu_item_frgmthome);
                li_homemenu_item_frgmthome = (LinearLayout) view.findViewById(R.id.li_homemenu_item_frgmthome);
            }
        }


        FgemtHomeServiceAdapter(List<HomeMenuBean> dataBeans) {
            this.dataBeanList = dataBeans;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_menu, parent, false);
            final ViewHolder holder = new ViewHolder(view);


            holder.li_homemenu_item_frgmthome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    HomeMenuBean customDataBean = dataBeanList.get(position);

                    switch (customDataBean.getKeyID()) {

                        //======================================================================================
                        //海康设备管理和预览
                        case 1:
                            BtnHiKOnClick();
                            break;

                        //======================================================================================
                        //配置管理
                        case 2:
                            BtnConfigOnClick();
                            break;

                        //======================================================================================
                        //热点管理
                        case 3:
                            WifiHotMagOnClick();
                            break;


                        //======================================================================================
                        //终端数据管理
                        case 4:
                            TermDataMagOnClick();
                            break;
                        default:
                            break;

                    }

                }
            });


            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            HomeMenuBean dataBean = dataBeanList.get(position);
            holder.tv_keyid_item_frgmthome.setText(String.valueOf(dataBean.getKeyID()));
            holder.tv_homemenu_item_frgmthome.setText(dataBean.getMenuText());
            //holder.img_homemenu_item_frgmthome.setBackgroundResource(dataBean.getImgID());
            holder.img_homemenu_item_frgmthome.setImageResource(dataBean.getImgID());
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
