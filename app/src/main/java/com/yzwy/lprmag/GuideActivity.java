package com.yzwy.lprmag;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yzwy.lprmag.adapter.GuideViewPagerAdapter;
import com.yzwy.lprmag.util.ExitApplication;
import com.yzwy.lprmag.util.Tools;
import com.yzwy.lprmag.util.TsSharePreferences;

import java.util.ArrayList;

/**
 * 引导页
 */
public class GuideActivity extends AppCompatActivity implements View.OnClickListener,ViewPager.OnPageChangeListener, CompoundButton.OnCheckedChangeListener {
    //定义ViewPager对象
    private ViewPager viewPager;

    //定义ViewPager适配器
    private GuideViewPagerAdapter guideViewPagerAdapter;

    //定义一个ArrayList来存放View
    private ArrayList<View> views;

    // 定义各个界面View对象
    private View view1, view2, view3;

    //底部小点的图片
    private ImageView[] points;

    //记录当前选中位置
    private int currentIndex;

    private long exitTime = 0;

    private boolean tfJoin = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.ly_guide);
        ExitApplication.getInstance().addActivity(this);

        initView();
        initData();

        guideViewPagerAdapter.notifyDataSetChanged();//ADT22以后，严格刷新线程
    }

    /**
     * 初始化组件
     */
    private void initView() {
        //实例化各个界面的布局对象
        LayoutInflater mLi = LayoutInflater.from(this);
        view1 = mLi.inflate(R.layout.ly_guide_page1, null);
        view2 = mLi.inflate(R.layout.ly_guide_page2, null);
        view3 = mLi.inflate(R.layout.ly_guide_page3, null);

        // 实例化ViewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        // 实例化ArrayList对象
        views = new ArrayList<View>();
        // 实例化ViewPager适配器
        guideViewPagerAdapter = new GuideViewPagerAdapter(this, views);
        /**
         * ===============================================================================================
         * 开始按钮 -- 跳转到主界面
         */
        Button btn_start_gdpg3;
        btn_start_gdpg3 = (Button) view3.findViewById(R.id.btn_start_gdpg3);
        btn_start_gdpg3.findViewById(R.id.btn_start_gdpg3).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(tfJoin){
                    /**
                     * 跳转到主界面
                     */
                    Tools.Intent(GuideActivity.this, MainActivity.class);
                }else{
                    Tools.Toast(GuideActivity.this, "请查看相关协议");
                }
            }
        });


        /**
         * ======================================================================================
         * 监听是否选中协议
         */
        CheckBox ckb_agmt_gdpg3;
        ckb_agmt_gdpg3 = (CheckBox) view3.findViewById(R.id.ckb_agmt_gdpg3);
        ckb_agmt_gdpg3.setOnCheckedChangeListener(this);

        /**
         * ======================================================================================
         * 跳转到协议界面
         */
        TextView tv_agmt_gdpg3;
        tv_agmt_gdpg3 = (TextView) view3.findViewById(R.id.tv_agmt_gdpg3);

//        tv_agmt_gdpg3.findViewById(R.id.tv_agmt_gdpg3).setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                Tools.IntentBack(GuideActivity.this, AgreementActivity.class);
//            }
//        });

    }
    /**
     * 初始化数据
     */
    private void initData() {
        // 设置监听
        viewPager.setOnPageChangeListener(this);
        // 设置适配器数据
        viewPager.setAdapter(guideViewPagerAdapter);

        //将要分页显示的View装入数组中
        views.add(view1);
        views.add(view2);
        views.add(view3);

        //初始化底部小点
        initPoint(views.size());
    }
    /**
     * 初始化底部小点
     */
    private void initPoint(int views){
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.point);

        points = new ImageView[views];

        //循环取得小点图片
        for (int i = 0; i < views; i++) {
            //得到一个LinearLayout下面的每一个子元素
            points[i] = (ImageView) linearLayout.getChildAt(i);
            //默认都设为灰色
            // points[i].setEnabled(true);
            //给每个小点设置监听
            points[i].setOnClickListener(this);
            //设置位置tag，方便取出与当前位置对应
            points[i].setTag(i);
        }

        //设置当面默认的位置
        currentIndex = 0;
        //设置为白色，即选中状态
        points[currentIndex].setEnabled(false);
    }

    /**
     * 当滑动状态改变时调用
     */
    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    /**
     * 当当前页面被滑动时调用
     */
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    /**
     * 当新的页面被选中时调用
     */
    @Override
    public void onPageSelected(int position) {
        //设置底部小点选中状态
        setCurDot(position);
    }

    /**
     * 通过点击事件来切换当前的页面
     */
    @Override
    public void onClick(View v) {
        int position = (Integer)v.getTag();
        setCurView(position);
        setCurDot(position);
    }

    /**
     * 设置当前页面的位置
     */
    private void setCurView(int position){
        if (position < 0 || position >= 3) {
            return;
        }
        viewPager.setCurrentItem(position);
    }

    /**
     * 设置当前的小点的位置
     */
    private void setCurDot(int positon){
        if (positon < 0 || positon > 3 || currentIndex == positon) {
            return;
        }
        points[positon].setEnabled(false);
        points[currentIndex].setEnabled(true);

        currentIndex = positon;
    }

    /**
     * ===================================================================
     * 双击退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            exitAPP();
        }
        return false;
    }

    private void exitAPP() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Tools.Toast(GuideActivity.this, "再按一次退出屏幕");
            exitTime = System.currentTimeMillis();
        } else {
            //退出程序，下次还会进入引导页
            TsSharePreferences.putBooleanValue(GuideActivity.this, "FIRST", false);
            System.exit(0);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton button, boolean b) {
        tfJoin = b;
    }

}