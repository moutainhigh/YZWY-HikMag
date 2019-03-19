package com.yzwy.lprmag;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.yzwy.lprmag.util.InetAddressUtil;
import com.yzwy.lprmag.util.NetUtils;

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

        setEnabled();


        setEditText();

    }
    /**
     * =============================================================================================
     * 设置 EditText 不可编辑
     */
    private void setEnabled() {
        edt_locIP_cfgset.setEnabled(false);
        edt_wwwnet_cfgset.setEnabled(false);
        edt_nettype_cfgset.setEnabled(false);
        edt_hkIp_cfgset.setEnabled(false);
        edt_hkport_cfgset.setEnabled(false);
        edt_hikusername_cfgset.setEnabled(false);
        edt_hikpwd_cfgset.setEnabled(false);
    }
    /**
     * =============================================================================================
     * 初始化值
     */
    private void setEditText() {
        edt_locIP_cfgset.setText(InetAddressUtil.getIP());
        edt_wwwnet_cfgset.setText(NetUtils.isNetConnected(ConfigSetActivity.this)+"");
        edt_nettype_cfgset.setText(NetUtils.getNetworkState(ConfigSetActivity.this)+"");
        edt_hkIp_cfgset.setText("192.168.1.64");
        edt_hkport_cfgset.setText("8000");
        edt_hikusername_cfgset.setText("admin");
        edt_hikpwd_cfgset.setText("admin123");
    }
}
