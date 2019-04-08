package com.example.yzwy.lprmag.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yzwy.lprmag.ConfigSetActivity;
import com.example.yzwy.lprmag.HiKCameraActivity;
import com.example.yzwy.lprmag.LoginActivity;
import com.example.yzwy.lprmag.R;
import com.example.yzwy.lprmag.WelcomeActivity;
import com.example.yzwy.lprmag.myConstant.UserInfoConstant;
import com.example.yzwy.lprmag.util.ExitApplication;
import com.example.yzwy.lprmag.util.SharePreferencesUtil;
import com.example.yzwy.lprmag.util.Tools;

/**
 * 　　　　　　　　┏┓　　　┏┓
 * 　　　　　　　┏┛┻━━━┛┻┓
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃　　　━　　　┃
 * 　　　　　　 ████━████     ┃
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃　　　┻　　　┃
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┗━┓　　　┏━┛
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　 　 ┗━━━┓
 * 　　　　　　　　　┃ 神兽保佑　　 ┣┓
 * 　　　　　　　　　┃ 代码无BUG   ┏┛
 * 　　　　　　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　　　　　　┃┫┫　┃┫┫
 * 　　　　　　　　　　┗┻┛　┗┻┛
 * Created by 仲超(zhongchao) on 2019/03/26.
 */
public class MineFragment extends Fragment implements View.OnClickListener {

    private View view;

    private LinearLayout li_aboutus_mine;
    private Button btn_loginOut_mine;
    private LinearLayout li_mineinfo_mine;
    private LinearLayout li_privacyProtocol_mine;
    private LinearLayout li_update_mine;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine, container, false);
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
        li_aboutus_mine = (LinearLayout) view.findViewById(R.id.li_aboutus_mine);
        li_mineinfo_mine = (LinearLayout) view.findViewById(R.id.li_mineinfo_mine);
        li_privacyProtocol_mine = (LinearLayout) view.findViewById(R.id.li_privacyProtocol_mine);
        li_update_mine = (LinearLayout) view.findViewById(R.id.li_update_mine);
        btn_loginOut_mine = (Button) view.findViewById(R.id.btn_loginOut_mine);


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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            //======================================================================================
            //我的信息
            case R.id.li_mineinfo_mine:
                AboutUsOnClick();
                break;

            //======================================================================================
            //关于我们
            case R.id.li_aboutus_mine:
                AboutUsOnClick();
                break;

            //======================================================================================
            //隐私协议
            case R.id.li_privacyProtocol_mine:
                AboutUsOnClick();
                break;

            //======================================================================================
            //检查更新
            case R.id.li_update_mine:
                AboutUsOnClick();
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
     * 关于我们
     */
    private void AboutUsOnClick() {
        Tools.Toast(getActivity(), "亲亲，单身狗们正在加倍努力喔~");
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
}