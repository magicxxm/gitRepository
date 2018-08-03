/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.rcs.kiva.bus.action;

import com.mushiny.rcs.global.AGVConfig;

/**
 * 机器动作对象， 一个对象包括动作码，动作参数，速度档位三个属性
 *
 *  @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class AbstractRobotAction implements RobotAction {

    protected byte actionCode = 0;
    protected int actionParameter = 0;
    protected short speed = AGVConfig.AGV_STRAIGHT_SPEED_5;

    @Override
    public byte getActionCode() {
        return actionCode;
    }

    @Override
    public int getActionParameter() {
        return actionParameter;
    }

    @Override
    public void setActionParameter(int actionParameter) {
        this.actionParameter = actionParameter;
    }

    @Override
    public short getSpeed() {
        return speed;
    }

    @Override
    public void setSpeed(short speed) {
        this.speed = speed;
    }

    public byte[] toBytes() {
        byte[] bytes = new byte[8];
        bytes[0] = getActionCode();//动作码
        //直行速度档位默认5档
        if (this instanceof StraightLine02Divide1Action
                || this instanceof StraightLine02Divide2Action
                || this instanceof StraightLineFollow03Action) {
            bytes[1] = (byte) (AGVConfig.AGV_STRAIGHT_SPEED_5 & 0xff);
        } else {
            //旋转速度默认3档
            if (this instanceof Rotate10Action
                    || this instanceof Rotate11Action
                    || this instanceof Rotate12Action
                    || this instanceof Rotate13Action) {
                bytes[1] = (byte) (AGVConfig.AGV_ROTATE_SPEED_3 & 0xff);
            } else {//其他情况速度档位为0
                bytes[1] = (byte) (AGVConfig.AGV_STRAIGHT_SPEED_0 & 0xff);
            }
        }
        //动作参数
        //直行【注意动作参数是 16位无符号】
        if (this instanceof StraightLine02Divide1Action
                || this instanceof StraightLine02Divide2Action
                || this instanceof StraightLineFollow03Action) {
            bytes[2] = (byte) (getActionParameter() & 0xff);
            bytes[3] = (byte) ((getActionParameter() >> 8) & 0xff);
            return bytes;
        }
        //旋转【注意动作参数是 16位有符号】
        if (this instanceof Rotate10Action
                || this instanceof Rotate11Action
                || this instanceof Rotate12Action
                || this instanceof Rotate13Action) {
            short tmpParameter = (short) getActionParameter();
            bytes[2] = (byte) (tmpParameter & 0xff);
            bytes[3] = (byte) ((tmpParameter >> 8) & 0xff);
            return bytes;
        }

        //美的充电桩
        if (this instanceof Media_Charge31Action) {
            short tmpParameter = (short) getActionParameter();
            bytes[2] = (byte) (tmpParameter & 0xff);
            bytes[3] = (byte) ((tmpParameter >> 8) & 0xff);
            return bytes;
        }

        // mingchun.mu@dmushiny.com -- 举升 下降  需要添加参数
        if(this instanceof Up20PodIDAction
                || this instanceof Down21PodIDAction){
            short tmpParameter = (short) getActionParameter();
            bytes[2] = (byte) (tmpParameter & 0xff);
            bytes[3] = (byte) ((tmpParameter >> 8) & 0xff);
            return bytes;
        }
        // mingchun.mu@dmushiny.com ------------------------



        bytes[2] = (byte) (0x00);
        bytes[3] = (byte) (0x00);
        return bytes;
    }
}
