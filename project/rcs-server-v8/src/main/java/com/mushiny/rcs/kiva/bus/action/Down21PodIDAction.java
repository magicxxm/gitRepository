/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.rcs.kiva.bus.action;

/**
 *下降
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class Down21PodIDAction extends AbstractRobotAction{
    private final byte actionCode=(byte)0x21;
    public byte getActionCode(){
        return actionCode;
    }
    public String toString(){
        return "actionCode=0x21，动作=下降， POD_ID="+actionParameter;
    }

    public Down21PodIDAction(int actionParameter) {
        this.actionParameter = actionParameter;
    }
}
