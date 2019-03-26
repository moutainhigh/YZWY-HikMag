package com.example.yzwy.lprmag;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.yzwy.lprmag.myConstant.ConfigDataConstant;
import com.example.yzwy.lprmag.util.InetAddressUtil;
import com.example.yzwy.lprmag.util.NetUtils;
import com.example.yzwy.lprmag.util.Tools;
import com.example.yzwy.lprmag.util.SharePreferencesUtil;

public class ConfigSetActivity extends AppCompatActivity {

    /**
     *
     * */
    private EditText edt_locIP_cfgset;
    /**
     *
     * */
    private EditText edt_wwwnet_cfgset;
    /**
     *
     * */
    private EditText edt_nettype_cfgset;
    /**
     *
     * */
    private EditText edt_hkIp_cfgset;
    /**
     *
     * */
    private EditText edt_hkport_cfgset;
    /**
     *
     * */
    private EditText edt_hikusername_cfgset;
    /**
     *
     * */
    private EditText edt_hikpwd_cfgset;
    /**
     *
     * */
    private Button btn_enter_cfgset;
    private Button btn_back_cfgset;
    private Button btn_f5data_cfgset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ly_config_set);

        /**
         * 加载View
         * */
        initView();
    }

    /**
     * =============================================================================================
     * 加載View
     */
    private void initView() {
        edt_locIP_cfgset = (EditText) findViewById(R.id.edt_locIP_cfgset);
        edt_wwwnet_cfgset = (EditText) findViewById(R.id.edt_wwwnet_cfgset);
        edt_nettype_cfgset = (EditText) findViewById(R.id.edt_nettype_cfgset);
        edt_hkIp_cfgset = (EditText) findViewById(R.id.edt_hkIp_cfgset);
        edt_hkport_cfgset = (EditText) findViewById(R.id.edt_hkport_cfgset);
        edt_hikusername_cfgset = (EditText) findViewById(R.id.edt_hikusername_cfgset);
        edt_hikpwd_cfgset = (EditText) findViewById(R.id.edt_hikpwd_cfgset);


        btn_enter_cfgset = (Button) findViewById(R.id.btn_enter_cfgset);
        btn_back_cfgset = (Button) findViewById(R.id.btn_back_cfgset);
        btn_f5data_cfgset = (Button) findViewById(R.id.btn_f5data_cfgset);

        //设置输入框不可编辑
        setEnabled();

        //设置显示的字符
        setEditText();

        //按钮监听事件
        initOnClick();

    }

    /**
     * =============================================================================================
     * 按钮监听事件
     */
    private void initOnClick() {
        btn_enter_cfgset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //RestartAPPTool.restartAPP(ConfigSetActivity.this,1000);

                //数据修改
                if (SharedPreferencesConfigData()) {
                    final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });

        btn_back_cfgset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfigSetActivity.this.finish();
            }
        });

        btn_f5data_cfgset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setEditText();

                Tools.Toast(ConfigSetActivity.this, "刷新成功~");


            }
        });

    }

    /**
     * =============================================================================================
     * 存储配置信息
     */
    private boolean SharedPreferencesConfigData() {

        /**
         * 获取所有的数据框的数据，去除首位空格
         * */
        //String edt_locIP_cfgset_str = edt_locIP_cfgset.getText().toString().trim();
        //String edt_wwwnet_cfgset_str = edt_wwwnet_cfgset.getText().toString().trim();
        //String edt_nettype_cfgset_str = edt_nettype_cfgset.getText().toString().trim();
        String edt_hkIp_cfgset_str = edt_hkIp_cfgset.getText().toString().trim();
        String edt_hkport_cfgset_str = edt_hkport_cfgset.getText().toString().trim();
        String edt_hikusername_cfgset_str = edt_hikusername_cfgset.getText().toString().trim();
        String edt_hikpwd_cfgset_str = edt_hikpwd_cfgset.getText().toString().trim();

        //==========================================================================================
        //海康IP地址校验
        if (!Tools.isIP(edt_hkIp_cfgset_str)) {
            Tools.Toast(ConfigSetActivity.this, "海康IP地址格式不正确，请重新输入");
            return false;
        }

        //==========================================================================================
        //海康端口号校验
        if (edt_hkport_cfgset_str.equals("")) {
            Tools.Toast(ConfigSetActivity.this, "海康端口号不能为空，请重新输入");
            return false;
        }
        if (edt_hkport_cfgset_str.substring(0, 1).equals("0")) {
            Tools.Toast(ConfigSetActivity.this, "海康端口号首字符不能为0，请重新输入");
            return false;
        }

        //==========================================================================================
        //海康用户名校验
        if (edt_hikusername_cfgset_str.equals("")) {
            Tools.Toast(ConfigSetActivity.this, "海康用户名不能为空，请重新输入");
            return false;
        }
        if (edt_hikusername_cfgset_str.length() <= 4 || edt_hikusername_cfgset_str.length() >= 11) {
            Tools.Toast(ConfigSetActivity.this, "海康用户名必须大于4位小于11位，请重新输入");
            return false;
        }


        //==========================================================================================
        //海康密码校验
        if (edt_hikpwd_cfgset_str.equals("")) {
            Tools.Toast(ConfigSetActivity.this, "海康密码不能为空，请重新输入");
            return false;
        }
        if (edt_hikpwd_cfgset_str.length() <= 4 || edt_hikpwd_cfgset_str.length() >= 21) {
            Tools.Toast(ConfigSetActivity.this, "海康密码必须大于4位小于21位，请重新输入");
            return false;
        }


        SharePreferencesUtil.putStringValue(ConfigSetActivity.this, ConfigDataConstant.hkIp_cfgset_str, edt_hkIp_cfgset_str);
        SharePreferencesUtil.putStringValue(ConfigSetActivity.this, ConfigDataConstant.hkport_cfgset_str, edt_hkport_cfgset_str);
        SharePreferencesUtil.putStringValue(ConfigSetActivity.this, ConfigDataConstant.hikusername_cfgset_str, edt_hikusername_cfgset_str);
        SharePreferencesUtil.putStringValue(ConfigSetActivity.this, ConfigDataConstant.hikpwd_cfgset_str, edt_hikpwd_cfgset_str);

        return true;
    }

    /**
     * =============================================================================================
     * 设置 EditText 不可编辑
     */
    private void setEnabled() {
        edt_locIP_cfgset.setEnabled(false);
        edt_wwwnet_cfgset.setEnabled(false);
        edt_nettype_cfgset.setEnabled(false);
        //edt_hkIp_cfgset.setEnabled(false);
        //edt_hkport_cfgset.setEnabled(false);
        //edt_hikusername_cfgset.setEnabled(false);
        //edt_hikpwd_cfgset.setEnabled(false);
    }

    /**
     * =============================================================================================
     * 初始化值
     */
    private void setEditText() {
        edt_locIP_cfgset.setText(InetAddressUtil.getIP());


        boolean netConnected = NetUtils.isNetConnected(ConfigSetActivity.this);
        if (netConnected) {
            edt_wwwnet_cfgset.setText("Internet网访问");
        } else {
            edt_wwwnet_cfgset.setText("网络未连接");
        }

        //java数组初始化
        String[] networkStateTypeArray = {"没有网络连接", "wifi连接", "2G", "3G", "4G", "手机流量"};
        int networkStateType = NetUtils.getNetworkState(ConfigSetActivity.this);
        edt_nettype_cfgset.setText(networkStateTypeArray[networkStateType]);

        String hkIp_cfgset_str = SharePreferencesUtil.getStringValue(ConfigSetActivity.this, ConfigDataConstant.hkIp_cfgset_str, ConfigDataConstant.hkIp_cfgset_str_default);
        String hkport_cfgset = SharePreferencesUtil.getStringValue(ConfigSetActivity.this, ConfigDataConstant.hkport_cfgset_str, ConfigDataConstant.hkport_cfgset_str_default);
        String hikusername_cfgset = SharePreferencesUtil.getStringValue(ConfigSetActivity.this, ConfigDataConstant.hikusername_cfgset_str, ConfigDataConstant.hikusername_cfgset_str_default);
        String hikpwd_cfgset = SharePreferencesUtil.getStringValue(ConfigSetActivity.this, ConfigDataConstant.hikpwd_cfgset_str, ConfigDataConstant.hikpwd_cfgset_str_default);

        edt_hkIp_cfgset.setText(hkIp_cfgset_str);
        edt_hkport_cfgset.setText(hkport_cfgset);
        edt_hikusername_cfgset.setText(hikusername_cfgset);
        edt_hikpwd_cfgset.setText(hikpwd_cfgset);

    }
}
