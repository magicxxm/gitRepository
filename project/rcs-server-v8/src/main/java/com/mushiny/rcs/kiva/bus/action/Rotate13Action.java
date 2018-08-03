/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.rcs.kiva.bus.action;

/**
 *
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class Rotate13Action extends RotateAction {
 private final byte actionCode=(byte)0x13;
   
 public Rotate13Action(int actionParameter) {
     this.actionParameter = actionParameter;
 }
    public byte getActionCode() {
        return actionCode;
    }

    public String toString() {
        return "actionCode=0x13，动作=旋转(托盘固定，且附带降落),旋转角度="+actionParameter;
    }
}
