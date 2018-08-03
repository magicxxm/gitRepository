/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.rcs.kiva.bus.action;

import com.mushiny.rcs.global.RCSConfig;

/**
 *跟车直行
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class StraightLineFollow03Action extends AbstractRobotAction{
     protected int straightDistance=RCSConfig.CELL_SIZE;
     public StraightLineFollow03Action() {
          actionCode=(byte)0x03;
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
        return "actionCode=0x03，动作=跟车直行,直行距离(动作参数)="+getStraightDistance();
    }
}
