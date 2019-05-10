package com.example.yzwy.lprmag.util;


import android.app.Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

///**
// *    author : Android 轮子哥
// *    github : https://github.com/getActivity/AndroidProject
// *    time   : 2018/11/18
// *    desc   : Activity 栈管理
// */

/**
 * 实战项目中管理Activity的类，一般在BaseActivity会调用这个类，然后所有的Activity继承BaseActivity，这样管理好整个项目的Activity。
 * activity堆栈管理
 * https://www.jianshu.com/p/ed897d567b02
 */
public class ActivityStackManager2 {

    private static ActivityStackManager2 mInstance;
    private static Stack<Activity> mActivityStack;

    public static ActivityStackManager2 getInstance() {

        if (null == mInstance) {
            mInstance = new ActivityStackManager2();
        }

        return mInstance;
    }

    private ActivityStackManager2() {
        mActivityStack = new Stack<Activity>();
    }

    /**
     * 入栈
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        mActivityStack.push(activity);
    }

    /**
     * 出栈
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        mActivityStack.remove(activity);
    }

    /**
     * 彻底退出
     */
    public void finishAllActivity() {
        Activity activity;
        while (!mActivityStack.empty()) {
            activity = mActivityStack.pop();
            if (activity != null) {
                activity.finish();
            }
        }
    }

    /**
     * 结束指定类名的Activity
     *
     * @param cls
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : mActivityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 查找栈中是否存在指定的activity
     *
     * @param cls
     * @return
     */
    public boolean checkActivity(Class<?> cls) {
        for (Activity activity : mActivityStack) {
            if (activity.getClass().equals(cls)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 结束指定的Activity
     *
     * @param activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            mActivityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * finish指定的activity之上所有的activity
     *
     * @param actCls
     * @param isIncludeSelf
     * @return
     */
    public boolean finishToActivity(Class<? extends Activity> actCls, boolean isIncludeSelf) {
        List<Activity> buf = new ArrayList<Activity>();
        int size = mActivityStack.size();
        Activity activity = null;
        for (int i = size - 1; i >= 0; i--) {
            activity = mActivityStack.get(i);
            if (activity.getClass().isAssignableFrom(actCls)) {
                for (Activity a : buf) {
                    a.finish();
                }
                return true;
            } else if (i == size - 1 && isIncludeSelf) {
                buf.add(activity);
            } else if (i != size - 1) {
                buf.add(activity);
            }
        }
        return false;
    }


//    private static volatile ActivityStackManager2 sInstance;
//
//    private HashMap<String, Activity> mActivitySet = new HashMap<>();
//
//    // 当前 Activity 对象标记
//    private String mCurrentTag;
//
//    private ActivityStackManager2() {}
//
//    public static ActivityStackManager2 getInstance() {
//        // 加入双重校验锁
//        if(sInstance == null) {
//            synchronized (ActivityStackManager2.class) {
//                if(sInstance == null){
//                    sInstance = new ActivityStackManager2();
//                }
//            }
//        }
//        return sInstance;
//    }
//
//    /**
//     * 获取栈顶的 Activity
//     */
//    public Activity getTopActivity() {
//        return mActivitySet.get(mCurrentTag);
//    }
//
//    /**
//     * 销毁所有的 Activity
//     */
//    public void finishAllActivities() {
//        finishAllActivities(null);
//    }
//
//    /**
//     * 销毁所有的 Activity，除这个 Class 之外的 Activity
//     */
//    public void finishAllActivities(Class<? extends Activity> clazz) {
//        String[] keys = mActivitySet.keySet().toArray(new String[]{});
//        for (String key : keys) {
//            Activity activity = mActivitySet.get(key);
//            if (activity != null && !activity.isFinishing() &&
//                    !(activity.getClass() == clazz)) {
//                activity.finish();
//                mActivitySet.remove(key);
//            }
//        }
//    }
//
//    public void onActivityCreated(Activity activity) {
//        mCurrentTag = getObjectTag(activity);
//        mActivitySet.put(getObjectTag(activity), activity);
//    }
//
//    public void onActivityDestroyed(Activity activity) {
//        mActivitySet.remove(getObjectTag(activity));
//        // 如果当前的 Activity 是最后一个的话
//        if (getObjectTag(activity).equals(mCurrentTag)) {
//            // 清除当前标记
//            mCurrentTag = null;
//        }
//    }
//
//    /**
//     * 获取一个对象的独立无二的标记
//     */
//    private static String getObjectTag(Object object) {
//        // 对象所在的包名 + 对象的内存地址
//        return object.getClass().getName() + Integer.ByteToHexString(object.hashCode());
//    }
}
