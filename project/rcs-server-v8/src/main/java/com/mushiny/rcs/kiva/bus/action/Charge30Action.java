/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.rcs.kiva.bus.action;

/**
 *动作=充电
 * @author 陈庆余 <13469592826@163.com>
 */
public class Charge30Action extends AbstractRobotAction{
     private final byte actionCode=(byte)0x30;
    
    public byte getActionCode(){
        return actionCode;
    }
    public String toString(){
        return "actionCode=0x30，动作=充电)";
    }
}
