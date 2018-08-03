package com.mushiny.test.message;

import com.aricojf.platform.common.HexBinaryUtil;

/**
 * Created by Laptop-6 on 2017/12/28.
 */
public class TestMessage2String {

    private static String testStr = "02 03 00 00 00";

    public static void main(String[] args) throws Exception {
        testStr = testStr.replace(" ", "");
        byte[] message = HexBinaryUtil.hexStringToByteArray(testStr);
        System.out.println("-----------------------------------------------");
        System.out.println(new String(message, "UTF-8"));
        System.out.println("-----------------------------------------------");




    }


}
