/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.rcs.global;

/**
 * 旋转区域，旋转点定义管理
 *
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class RotateAreaConfig {

    public  static int R1X = 5301;
    public  static int R1Y = 5301;
    public  static long R1_ADDRESS_CODE_ID = 5301;//测试旋转区R1地址码
    public  static int R2X = R1X + 1;
    public  static int R2Y = R1Y + 1;
    public  static long R2_ADDRESS_CODE_ID = R1_ADDRESS_CODE_ID + 1;
    public  static int R3X = R1X;
    public  static int R3Y = R1Y + 2;
    public  static long R3_ADDRESS_CODE_ID = R2_ADDRESS_CODE_ID + 1;
    public  static int R4X = R1X - 1;
    public  static int R4Y = R1Y + 1;
    public  static long R4_ADDRESS_CODE_ID = R3_ADDRESS_CODE_ID + 1;
    public  static int RX = R1X;
    public  static int RY = R1Y + 1;
    public  static long R_ADDRESS_CODE_ID = R4_ADDRESS_CODE_ID + 1;
    
    public  static int TURN_X = R1X-1;
    public  static int TURN_Y = R1Y-1;
    public  static long TURN_ADDRESS_CODE_ID = R1_ADDRESS_CODE_ID-1;
    
   
}
