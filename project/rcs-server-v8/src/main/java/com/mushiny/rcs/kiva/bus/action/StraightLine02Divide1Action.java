/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.rcs.kiva.bus.action;

import com.mushiny.rcs.global.RCSConfig;

/**
 *动作码：直行 1000mm
 *  @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class StraightLine02Divide1Action extends StraightLine02Action{
    protected int straightDistance=RCSConfig.CELL_SIZE;
    public StraightLine02Divide1Action() {
        actionCode=(byte)0x02;
        setActionParameter(straightDistance);
    }
    /**
     * @return the strainghtDistance
     */
    public int getStraightDistance() {
        return straightDistance;
    }

    /**
     * @param straightDistance the strainghtDistance to set
     */
    public void setStraightDistance(int straightDistance) {      
        this.straightDistance = straightDistance;
        setActionParameter(straightDistance);
    }
    public String toString(){
        return "actionCode=0x02，动作=直行,直行距离(动作参数)="+getStraightDistance();
    }
}
