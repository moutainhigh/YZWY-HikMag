package com.example.yzwy.lprmag.util.crypto.aes;

public class AESBaseUtil {


    /**
     * =============================================================================================
     * 字节数组转成16进制表示格式的字符串
     *
     * @param byteArray 要转换的字节数组
     * @return 16进制表示格式的字符串
     **/
    public static String ByteToHexString(byte[] byteArray) {
        if (byteArray == null || byteArray.length < 1)
            //此byte数组不能为空
            throw new IllegalArgumentException("this byteArray must not be null or empty");

        final StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < byteArray.length; i++) {
            if ((byteArray[i] & 0xff) < 0x10)//0~F前面不零
                hexString.append("0");
            hexString.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return hexString.toString().toLowerCase();
    }


    /**
     * =============================================================================================
     * 十六进制串转化为byte数组
     *
     * @return the array of byte
     */
    public static byte[] HexStringToByte(String hex)
            throws IllegalArgumentException {
        if (hex.length() % 2 != 0) {
            throw new IllegalArgumentException();
        }
        char[] arr = hex.toCharArray();
        byte[] b = new byte[hex.length() / 2];
        for (int i = 0, j = 0, l = hex.length(); i < l; i++, j++) {
            String swap = "" + arr[i++] + arr[i];
            int byteint = Integer.parseInt(swap, 16) & 0xFF;
            b[j] = Integer.valueOf(byteint).byteValue();
        }
        return b;
    }

    /**
     * 对密钥key进行处理：如密钥长度不够位数的则 以指定paddingChar 进行填充；
     * 此处用空格字符填充，也可以 0 填充，具体可根据实际项目需求做变更
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] getSecretKey(String key, int Secret_Key_Size, String Key_Encode) throws Exception {


        return (AESBaseUtil.FillInLeft(key, Secret_Key_Size, "0")).getBytes();


//        final byte paddingChar = ' ';
//        //final byte paddingChar = '0';
//
//        byte[] realKey = new byte[Secret_Key_Size];
//        byte[] byteKey = key.getBytes(Key_Encode);
//        for (int i = 0; i < realKey.length; i++) {
//            if (i < byteKey.length) {
//                realKey[i] = byteKey[i];
//            } else {
//                realKey[i] = paddingChar;
//            }
//        }
//
//        //System.out.println("realKey：" + ByteToHexString(realKey));
//
//        return realKey;
    }

    /**
     * =============================================================================================
     * 长度不足左边补位
     *
     * @param str
     * @param strLength
     * @param addStr
     * @return
     */
    public static String FillInLeft(String str, int strLength, String addStr) {

        int strLen = str.length();
        if (strLength <= strLen) {
            return str;
        }

        if (addStr.equals("") || addStr == null) {
            return str;
        }


        if (strLen < strLength) {
            while (strLen < strLength) {
                StringBuffer sb = new StringBuffer();
                sb.append(addStr).append(str);// 左补0
                // sb.append(str).append("0");//右补0
                str = sb.toString();
                strLen = str.length();
            }
        }

        return str;
    }


    /**
     * =============================================================================================
     * 长度不足左边补位
     *
     * @param str       原数据
     * @param strLength 总长度
     * @param addStr    需要补位的数据
     * @return
     */
    public static String FillInRight(String str, int strLength, String addStr) {

        int strLen = str.length();

        if (strLength <= strLen) {
            return str;
        }

        if (addStr.equals("") || addStr == null) {
            return str;
        }


        if (strLen < strLength) {
            while (strLen < strLength) {
                StringBuffer sb = new StringBuffer();
                //sb.append(addStr).append(str);// 左补0
                sb.append(str).append(addStr);//右补0
                str = sb.toString();
                strLen = str.length();
            }
        }

        return str;
    }

}
