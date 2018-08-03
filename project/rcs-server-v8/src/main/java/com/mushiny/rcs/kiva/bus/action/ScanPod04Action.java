/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.rcs.kiva.bus.action;

/**
 *AGV行驶路径最后一个参数动作
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class ScanPod04Action extends AbstractRobotAction {

    public ScanPod04Action() {
        actionCode = (byte) 0x04;
    }
    public String toString(){
        return "actionCode=0x04，扫描POD_ID";
    }
}
