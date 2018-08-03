/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.rcs.kiva.bus.action;

/**
 *旋转(托盘跟着转动)
 *  @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class Rotate11Action extends RotateAction{
    private final byte actionCode=(byte)0x11;
   
    public Rotate11Action(int actionParameter) {
        this.actionParameter = actionParameter;
    }
    public byte getActionCode(){
        return actionCode;
    }
    public String toString(){
        return "actionCode=0x11，动作=旋转(托盘跟着转动),旋转角度="+actionParameter;
    }
}
