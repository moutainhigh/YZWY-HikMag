package com.example.yzwy.lprmag.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.text.format.Time;
import android.util.TypedValue;
import android.widget.Toast;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * =========================================================================
 * 1.Toast 短提示 2.Intent跳转 3.Intent 带数据跳转 4.Intent 带数据返回跳转 5.手机号码验证
 */
public class Tools {

    /**
     * ======================================================================
     * Toast打印提示信息
     *
     * @param con （当前Activity）
     * @param str （打印的内容） 示例代码：D_GuideActivity类第76行
     */
    public static void Toast(Context con, String str) {
        Toast.makeText(con, str, Toast.LENGTH_SHORT).show();
    }

    /**
     * ======================================================================
     * Activity跳转
     *
     * @param con （当前Activity）
     * @param cls （目的Activity） 示例代码：D_GuideActivity类第79行
     */
    public static void Intent(Context con, Class<?> cls) {
        Intent in = new Intent(con, cls);
        con.startActivity(in);
        ((Activity) con).finish();
    }

    /**
     * ======================================================================
     * Activity跳转带String通信
     *
     * @param con  （当前Activity）
     * @param cls  （目的Activity）
     * @param pag  （通信检索）
     * @param data （通信内容） 示例代码：D_SellActivity类第46行
     */
    public static void IntentData(Context con, Class<?> cls, String pag,
                                  String data) {
        Intent in = new Intent(con, cls);
        in.putExtra(pag, data);
        con.startActivity(in);
        ((Activity) con).finish();
    }

    /**
     * ======================================================================
     * Activity跳转带String通信
     *
     * @param con  （当前Activity）
     * @param cls  （目的Activity）
     * @param pag  （通信检索）
     * @param data （通信内容） 示例代码：D_MainActivity类第108行
     */
    public static void IntentDataBack(Context con, Class<?> cls, String pag,
                                      String data) {
        Intent in = new Intent(con, cls);
        in.putExtra(pag, data);
        con.startActivity(in);
    }

    /**
     * ======================================================================
     * Activity返回上层
     *
     * @param con （当前Activity）
     * @param cls （目的Activity） 示例代码：B_SettingActivity类第140行
     */
    public static void IntentBack(Context con, Class<?> cls) {
        Intent in = new Intent(con, cls);
        con.startActivity(in);

    }

    public static int dip2px(Context context, float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                value, context.getResources().getDisplayMetrics());
    }

    public static int sp2px(Context context, float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                value, context.getResources().getDisplayMetrics());
    }

    /**
     * ======================================================================
     * 验证号码 手机号 固话均可
     *
     * @param phoneNumber （需要验证的手机号码）
     *                    <p>
     *                    示例代码：D_LoginActivity类第140行
     *                    <p>
     *                    说明：移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
     *                    联通：130、131、132、152、155、156、185、186 电信：133、153、180、189
     *                    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9 -----已添加最新17号码段
     */
    public static boolean isPhoneNumberValid(String phoneNumber) {
        boolean isValid = false;

        String expression = "((^(13|15|18|17)[0-9]{9}$)|(^0[1,2]{1}\\d{1}-?\\d{8}$)|(^0[3-9] {1}"
                + "\\d{2}-?\\d{7,8}$)|(^0[1,2]{1}\\d{1}-?\\d{8}-(\\d{1,4})$)|(^0[3-9]{1}\\d{2}-? "
                + "\\d{7,8}-(\\d{1,4})$))";
        CharSequence inputStr = phoneNumber;

        Pattern pattern = Pattern.compile(expression);

        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches()) {
            isValid = true;
        }

        return isValid;

    }

    /**
     * ======================================================================
     * 在SD卡上创建一个文件夹
     *
     * @param context
     * @param folderUrl
     * 文件夹
     * @return
     */
    private static String path;

    public static boolean createSDCardDir(Context context, String folderUrl) {
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            // 创建一个文件夹对象，赋值为外部存储器的目录
            File sdcardDir = Environment.getExternalStorageDirectory();
            // 得到一个路径，内容是sdcard的文件夹路径和名字
            path = sdcardDir.getPath() + folderUrl;
            File path1 = new File(path);
            if (!path1.exists()) {
                // 若不存在，创建目录，可以在应用启动的时候创建
                path1.mkdirs();

                // /////////////////////////////////////////////////
                // System.out.println("若不存在，创建目录--->"+path);
                // ///////////////////////////////////////////////////
                return true;
            } else {

                // /////////////////////////////////////////////////
                // System.out.println("--->"+path);
                // ///////////////////////////////////////////////////
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * ========================================================================
     *
     * @param date 日期 （"xxxx年xx月xx日）
     */
    public static int[] DataStringInt(String date) {
        int year = Integer.valueOf(date.substring(0, 4));
        int month = Integer.valueOf(date.substring(5, 7));
        int day = Integer.valueOf(date.substring(8, 10));
        int[] reDate = {year, month, day};
        return reDate;
    }

    /**
     * ========================================================================
     *
     * @param date 字符串时间
     * @return int[] 0、2、4
     */
    public static int[] DataStringGetInt(String date) {
        int reDate[] = new int[6];
        Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]+|\\d+");
        Matcher m = p.matcher(date);
        int i = 0;
        while (m.find()) {
            if (i != 0) {
                if (i % 2 == 0) {
                    reDate[i] = Integer.valueOf(m.group());
                    //System.out.println(i+"$$$$$$"+reDate[i]);
                }
            } else {
                reDate[i] = Integer.valueOf(m.group());
            }
            i++;
        }
        return reDate;
    }

    /**
     * =================================================================================
     * @param context
     * @return -1未连接网络  ---> 0 移动网络 --->1无线网络
     */
//	public static int NetWorkState(Context context) {
//		/**
//		 * 没有连接网络
//		 */
//		final int NETWORK_NONE = -1;
//		/**
//		 * 移动网络
//		 */
//		final int NETWORK_MOBILE = 0;
//		/**
//		 * 无线网络
//		 */
//		final int NETWORK_WIFI = 1;
//
//		// 得到连接管理器对象
//		ConnectivityManager connectivityManager = (ConnectivityManager) context
//				.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//		if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
//
//			if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI)) {
//				return NETWORK_WIFI;
//			} else if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_MOBILE)) {
//				return NETWORK_MOBILE;
//			}
//		} else {
//			return NETWORK_NONE;
//		}
//		return NETWORK_NONE;
//	}


    /**
     * ==================================================================================
     *
     * @param YMD 年或者月作者日
     */
    public static String twoAddZero(String YMD) {
        if (YMD.length() == 1) {
            return "0" + YMD;
        } else {
            return YMD;
        }
    }


    /**
     * ======================================================================
     * 获取系统时间
     * <p>
     * （目的Activity） 示例代码：B_SettingActivity类第140行
     */
    public static String nowDate() {
        Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
        t.setToNow(); // 取得系统时间。
        int yyyy = t.year;
        int MM = t.month + 1;
        int dd = t.monthDay;
	    /*int hh = t.hour; // 0-23
	    int mm = t.minute;
	    int ss = t.second;*/
        String time = yyyy + "-" + Tools.twoAddZero(String.valueOf(MM)) + "-" + Tools.twoAddZero(String.valueOf(dd))/*+" "+hh+":"+mm+":"+ss*/;

        return time;
    }

    /**
     * =======================================================================
     * 日期大小判断 <br>
     * false 大于今天 <br>
     * true 小于今天 <br>
     *
     * @param YMD 2017-05-16
     */
    public static boolean timeSize(String YMD) {
        Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
        t.setToNow(); // 取得系统时间。
        int yyyy = t.year;  //2017
        int MM = t.month + 1;  //5
        int dd = t.monthDay;  //15

        String nowYMD = yyyy + "-" + Tools.twoAddZero(String.valueOf(MM)) + "-" + Tools.twoAddZero(String.valueOf(dd));
        int res = YMD.compareTo(nowYMD);
        if (res > 0) {
            return false;
        } else if (res == 0) {
            return true;
        } else {
            return true;
        }
    }

    /**
     * ===============================================================================
     * 将农历数字转化成汉字
     *
     * @param GreYear  公历 年 2017
     * @param LunYear  农历 年 2017
     * @param LunMonth 农历 月 5
     * @param LunDay   农历  日 06
     * @return
     */
    public static String SetLunar(int GreYear, int LunYear, int LunMonth, int LunDay) {
        final String[] zeroToTen = new String[]{"零", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十"};
        final String[] Animals = new String[]{"鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"};
        String reYear = zeroToTen[Integer.valueOf(String.valueOf(LunYear).substring(0, 1))] + zeroToTen[Integer.valueOf(String.valueOf(LunYear).substring(1, 2))] + zeroToTen[Integer.valueOf(String.valueOf(LunYear).substring(2, 3))] + zeroToTen[Integer.valueOf(String.valueOf(LunYear).substring(3, 4))];
        String reMonth = "";
        if (String.valueOf(LunMonth).length() == 1) {//<10
            reMonth = zeroToTen[LunMonth];
        } else {
            if (String.valueOf(LunMonth).substring(0, 1).equals("-")) {
                reMonth = zeroToTen[Integer.valueOf(String.valueOf(LunMonth).substring(1, 2))];
            } else if (String.valueOf(LunMonth).equals("10")) {
                reMonth = "十";
            } else if (String.valueOf(LunMonth).equals("11")) {
                reMonth = "冬";
            } else if (String.valueOf(LunMonth).equals("12")) {
                reMonth = "腊";
            }


        }
        String reDay = "";
        if (LunDay <= 10) {
            reDay = "初" + zeroToTen[LunDay];
        } else {
            if (String.valueOf(LunDay).substring(0, 1).equals("1")) {
                reDay = "十" + zeroToTen[Integer.valueOf(String.valueOf(LunDay).substring(1, 2))];
            } else if (String.valueOf(LunDay).substring(0, 1).equals("2")) {
                if (String.valueOf(LunDay).substring(1, 2).equals("0")) {
                    reDay = "甘" + zeroToTen[10];
                } else {
                    reDay = "甘" + zeroToTen[Integer.valueOf(String.valueOf(LunDay).substring(1, 2))];
                }
            } else if (String.valueOf(LunDay).substring(0, 1).equals("3")) {
                if (String.valueOf(LunDay).substring(1, 2).equals("0")) {
                    reDay = "三十";
                } else if (String.valueOf(LunDay).substring(1, 2).equals("1")) {
                    reDay = "三十一";
                }
            }
        }
        String reAnimals = Animals[(GreYear - 4) % 12];

        return reYear + "(" + reAnimals + ")" + "年" + reMonth + "月" + reDay;
    }


    /**
     * =============================================================================================
     * IP4判断
     * @param addr
     * @return
     */
    public static boolean isIP(String addr) {
        if (addr.length() < 7 || addr.length() > 15 || "".equals(addr)) {
            return false;
        }
        /**
         * 判断IP格式和范围
         * */
        String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pat = Pattern.compile(rexp);
        Matcher mat = pat.matcher(addr);
        boolean ipAddress = mat.find();

        //============对之前的ip判断的bug在进行判断
        if (ipAddress == true) {
            String ips[] = addr.split("\\.");
            if (ips.length == 4) {
                try {
                    for (String ip : ips) {
                        if (Integer.parseInt(ip) < 0 || Integer.parseInt(ip) > 255) {
                            return false;
                        }
                    }
                } catch (Exception e) {
                    return false;
                }
                return true;
            } else {
                return false;
            }
        }
        return ipAddress;
    }


}
