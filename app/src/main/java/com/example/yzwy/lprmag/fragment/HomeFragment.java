package com.example.yzwy.lprmag.fragment;

import android.graphics.Rect;
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
import com.example.yzwy.lprmag.ErrorLogActivity;
import com.example.yzwy.lprmag.GeomagneticManageActivity;
import com.example.yzwy.lprmag.HiKCameraActivity;
import com.example.yzwy.lprmag.R;
import com.example.yzwy.lprmag.TerminalDataManageActivity;
import com.example.yzwy.lprmag.WifiHotMagActivity;
import com.example.yzwy.lprmag.bean.HomeMenuBean;
import com.example.yzwy.lprmag.control.activityStackExtends.util.ActivityStackManager;
import com.example.yzwy.lprmag.util.DisplayUtil;
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
    private int spanCount = 3;


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
        //Load defaults from resources


//        Resources res = getResources();
//
//        float defaultStrokeWidth = res.getDimension(R.dimen.default_circle_indicator_stroke_width);
//        float defaultRadius = res.getDimension(R.dimen.default_circle_indicator_radius);
//        int dimension = (int)res.getDimension(R.dimen.qb_px_5);
//        float qb_px_5 = res.getDimension(R.dimen.qb_px_5);
//        float qb_px_20 = res.getDimension(R.dimen.qb_px_20);
//
//        LogUtil.showLog("qb_px",qb_px_5+"=="+qb_px_20+"");

    }

    /**
     * =============================================================================================
     * 加载数据
     */
    private void initBeanData() {

        if (adapterBeanList.size() > 0) {
            adapterBeanList.clear();
        }

        adapterBeanList.add(new HomeMenuBean(1, R.drawable.ic_menu_tiaoshi, "设备调试"));
        adapterBeanList.add(new HomeMenuBean(2, R.drawable.ic_menu_config_app, "设备连接配置"));
        adapterBeanList.add(new HomeMenuBean(3, R.drawable.ic_menu_hot_block, "热点管理"));
        adapterBeanList.add(new HomeMenuBean(4, R.drawable.ic_menu_term_data_mag, "终端数据管理"));
        adapterBeanList.add(new HomeMenuBean(5, R.drawable.ic_menu_geom_rest, "地磁复位管理"));
        //adapterBeanList.add(new HomeMenuBean(6, R.drawable.ic_menu_errorlog, "终端异常数据管理"));
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
     * 终端异常数据管理
     */
    private void ErrorLogOnClick() {
        Tools.IntentBack(getActivity(), ErrorLogActivity.class);
    }

    /**
     * 地磁复位管理
     */
    private void GeomRestOnClick() {
        Tools.IntentBack(getActivity(), GeomagneticManageActivity.class);
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
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FgemtHomeServiceAdapter(adapterBeanList);
        recyclerView.setAdapter(adapter);


        //设置边距
        recyclerView.addItemDecoration(new SpaceItemDecoration());


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


                        //======================================================================================
                        //地磁复位管理
                        case 5:
                            GeomRestOnClick();
                            break;


                        //======================================================================================
                        //地磁复位管理
                        case 6:
                            ErrorLogOnClick();
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


    //设置边距
    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int normal = 0;
        private int margin = DisplayUtil.dip2px(getActivity(), 5);
        private int RcVPaTop = DisplayUtil.dip2px(getActivity(), 20);
        private int RcVPaBottom = DisplayUtil.dip2px(getActivity(), 20);


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

            outRect.top = margin;
            outRect.bottom = margin;
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

}
