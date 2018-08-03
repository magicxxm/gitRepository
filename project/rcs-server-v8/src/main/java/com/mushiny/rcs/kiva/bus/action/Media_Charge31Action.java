/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.rcs.kiva.bus.action;

/**
 *动作=到达充电桩
 * @author 陈庆余 <13469592826@163.com>
 */
public class Media_Charge31Action extends AbstractRobotAction{
    private final byte actionCode=(byte)0x31;

    public Media_Charge31Action(int chongDianZhuangId) {

        this.actionParameter = chongDianZhuangId;
    }

    public byte getActionCode(){
        return actionCode;
    }
    public String toString(){
        return "actionCode=0x31，动作=到达充电桩，动作参数="+getActionParameter();
    }
}
