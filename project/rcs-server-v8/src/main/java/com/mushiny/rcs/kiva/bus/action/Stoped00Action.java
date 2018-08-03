package com.mushiny.rcs.kiva.bus.action;

/**
 * AGV行驶路径最后一个参数动作
 */
public class Stoped00Action extends AbstractRobotAction {

    public Stoped00Action() {
        actionCode = (byte) 0x00;
    }
    public String toString(){
        return "actionCode=0x00，动作=暂停小车动作   ";
    }
}
