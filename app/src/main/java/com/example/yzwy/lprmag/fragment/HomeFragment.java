package com.example.yzwy.lprmag.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yzwy.lprmag.ConfigSetActivity;
import com.example.yzwy.lprmag.HiKCameraActivity;
import com.example.yzwy.lprmag.R;
import com.example.yzwy.lprmag.WifiHotMagActivity;
import com.example.yzwy.lprmag.util.ExitApplication;
import com.example.yzwy.lprmag.util.Tools;

/**
 * 　　　　┏┓　　　┏┓
 * 　　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　 　┃　　　━　　　┃
 * 　　 ████━████     ┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃
 * 　　　　┃　　　┃
 * 　　　　　┃　　　┃
 * 　 　　┃　　　┃
 * 　　　 ┃　　　┃
 * 　　 　┃　　　┃
 * 　　 　┃　 　 ┗━━━┓
 * 　　　 ┃ 神兽保佑　　 ┣┓
 * 　　　 ┃ 代码无BUG   ┏┛
 * 　  　┗┓┓┏━┳┓┏┛
 * 　　 　┃┫┫　┃┫┫
 * 　　 　┗┻┛　┗┻┛
 * Created by 仲超(zhongchao) on 2019/03/26.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private View view;
    private ImageView img_hik_frgmthome;
    private TextView tv_hik_frgmthome;
    private ImageView img_config_frgmthome;
    private TextView tv_config_frgmthome;
    private ImageView img_other_frgmthome;
    private TextView tv_other_frgmthome;
    private ImageView img_hotConfig_frgmthome;
    private TextView tv_hotConfig_frgmthome;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ExitApplication.getInstance().addActivity(getActivity());

        //初始化组件
        initView();

    }

    /**
     * =============================================================================================
     * 初始化组件
     */
    private void initView() {
        img_hik_frgmthome = (ImageView) view.findViewById(R.id.img_hik_frgmthome);
        tv_hik_frgmthome = (TextView) view.findViewById(R.id.tv_hik_frgmthome);
        img_config_frgmthome = (ImageView) view.findViewById(R.id.img_config_frgmthome);
        tv_config_frgmthome = (TextView) view.findViewById(R.id.tv_config_frgmthome);

        img_hotConfig_frgmthome = (ImageView) view.findViewById(R.id.img_hotConfig_frgmthome);
        tv_hotConfig_frgmthome = (TextView) view.findViewById(R.id.tv_hotConfig_frgmthome);

//        img_other_frgmthome = (ImageButton) view.findViewById(R.id.img_other_frgmthome);
//        tv_other_frgmthome = (TextView) view.findViewById(R.id.tv_other_frgmthome);


        initOnClick();


    }


    /**
     * =============================================================================================
     * 事件监听
     */
    private void initOnClick() {
        img_hik_frgmthome.setOnClickListener(this);
        tv_hik_frgmthome.setOnClickListener(this);
        img_config_frgmthome.setOnClickListener(this);
        tv_config_frgmthome.setOnClickListener(this);
        img_hotConfig_frgmthome.setOnClickListener(this);
        tv_hotConfig_frgmthome.setOnClickListener(this);

//        img_other_frgmthome.setOnClickListener(this);
//        tv_other_frgmthome.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            //======================================================================================
            //海康设备管理和预览
            case R.id.img_hik_frgmthome:
                BtnHiKOnClick();
                break;
            case R.id.tv_hik_frgmthome:
                BtnHiKOnClick();
                break;

            //======================================================================================
            //配置管理
            case R.id.img_config_frgmthome:
                BtnConfigOnClick();
                break;
            case R.id.tv_config_frgmthome:
                BtnConfigOnClick();
                break;

            //======================================================================================
            //热点管理
            case R.id.img_hotConfig_frgmthome:
                WifiHotMagOnClick();
                break;
            case R.id.tv_hotConfig_frgmthome:
                WifiHotMagOnClick();
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
     * 终端热点管理
     * */
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
}
