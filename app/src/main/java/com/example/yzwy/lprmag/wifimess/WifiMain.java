//package com.example.yzwy.lprmag.wifimess;
//
//import com.example.yzwy.lprmag.util.InetAddressUtil;
//
//import java.util.Map;
//
//import static com.example.yzwy.lprmag.wifimess.util.SocketUtil.sendTCPRequest;
//
//public class WifiMain {
//
//    public static void main(String[] args) {
//        String reqData = "0003721000510110199201209222240000020120922000069347814303000700000813``中国联通交费充值`为号码18655228826交费充值100.00元`UDP1209222238312219411`10000```20120922`chinaunicom-payFeeOnline`UTF-8`20120922223831`MD5`20120922020103806276`1`02`10000`20120922223954`20120922`BOCO_B2C```http://192.168.20.2:5545/ecpay/pay/elecChnlFrontPayRspBackAction.action`1`立即支付,交易成功`";
//        String IP = InetAddressUtil.getIP();
//        String port = "9901";
//        String reqCharset = "GB18030";
//        Map<String, String> respMap = sendTCPRequest(IP, port, reqData, reqCharset);
//        System.out.println("=============================================================================");
//        System.out.println("请求报文如下");
//        System.out.println(respMap.get("reqData"));
//        System.out.println("=============================================================================");
//        System.out.println("响应报文如下");
//        System.out.println(respMap.get("respData"));
//        System.out.println("=============================================================================");
//        System.out.println("响应十六进制如下");
//        System.out.println(respMap.get("respDataHex"));
//        System.out.println("=============================================================================");
//    }
//
//}
