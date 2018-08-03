/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.aricojf.platform.common.HexBinaryUtil;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author aricochen
 */
public class TimeTest {

    /*public static void main(String[] args) {
        int n = 1492584645;
        System.out.println("n="+n);

        byte[] m = new byte[4];
        m[0] = (byte) (n & 0xff);
        m[1] = (byte) ((n >> 8) & 0xff);
        m[2] = (byte) ((n >> 16) & 0xff);
        m[3] = (byte) ((n >> 24) & 0xff);
        System.out.println("m:" + HexBinaryUtil.byteArrayToHexString2(m));
        int x = 0;
//        x = (int)m[3];
//        x = (int)((x << 8) | (m[2] & 0xff));
//        x = (int)((x << 16) | (m[1] & 0xff));
//        x = (int)((x << 24) | (m[0] & 0xff));
        x = (m[3] & 0x000000ff) << 24;
        x += (m[2] & 0x000000ff) << 16 ;
        x += (m[1] & 0x000000ff) << 8 ;
        x += (m[0] & 0x000000ff) ;
        System.out.println("x="+x);

        byte[] a = new byte[4];
        a[0] = (byte) 0xc5;
        a[1] = (byte) 0x08;
        a[2] = (byte) 0xf7;
        a[3] = (byte) 0x58;
        int b = 0;
        b =  a[3];
        b =  ((b << 8) | (a[2] & 0xff));
        b =  ((b << 8) | (a[1] & 0xff));
        b =  ((b << 8) | (a[0] & 0xff));
        System.out.println("b:" + b);
    }*/
}
