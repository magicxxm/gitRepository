/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aricojf.platform.mina.message.robot;

import com.aricojf.platform.common.Global;
import com.mushiny.rcs.global.KivaBusConfig;
import com.mushiny.rcs.global.Robot2RCSMessageTypeConfig;

/**
 * 机器人周期性状态数据包
 *
 *  @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class RobotStatusMessage extends RobotMessage {

    private int shengYuDianLiang; // 剩余电量

    private int leftWheelMotorTemperature; // 左轮电机温度
    private int rightWheelMotorTemperature; // 右轮电机温度
    private int rotationMotorTemperature; // 旋转电机温度
    private int upMotorTemperature; // 顶升电机温度
    private int batteryTemperatureOne; // 电池温度1
    private int batteryTemperatureTwo; // 电池温度2
    private int batteryVoltage; // 电池电压

    public RobotStatusMessage() {
        this(-1);
    }

    public RobotStatusMessage(long robotID) {
        super();
        this.robotID = robotID;
        this.messageBodyLength = 10;
        this.frameDataLength = 19;
        this.functionWordCode = Robot2RCSMessageTypeConfig.ROBOT_STATUS_MESSAGE;
         this.head = KivaBusConfig.ROBOT2RCS_HEAD;
    }


    public void toObject() {
        byte[] messageBytes = (byte[]) getMessage();
        super.toObject();
        //剩余电量
        shengYuDianLiang = messageBytes[13];
        shengYuDianLiang = (int) ((shengYuDianLiang << 8) | (messageBytes[12] & 0xff));
        //左轮电机温度
        leftWheelMotorTemperature = messageBytes[14];
        //右轮电机温度
        rightWheelMotorTemperature = messageBytes[15];
        //旋转电机温度
        rotationMotorTemperature = messageBytes[16];
        //顶升电机温度
        upMotorTemperature = messageBytes[17];
        //电池温度1
        batteryTemperatureOne = messageBytes[18];
        //电池温度2
        batteryTemperatureTwo = messageBytes[19];
        //电池电压
        batteryVoltage = messageBytes[21];
        batteryVoltage = (int) ((batteryVoltage << 8) | (messageBytes[20] & 0xff));

    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("车辆状态消息:");
        builder.append(super.toString());
        builder.append(Global.NR);
        builder.append("剩余电量:");
        builder.append(getShengYuDianLiang());
        builder.append("，电池电压:");
        builder.append(getBatteryVoltage());
        return builder.toString();
    }

    public int getShengYuDianLiang() {
        return shengYuDianLiang;
    }

    public void setShengYuDianLiang(int shengYuDianLiang) {
        this.shengYuDianLiang = shengYuDianLiang;
    }

    public int getLeftWheelMotorTemperature() {
        return leftWheelMotorTemperature;
    }

    public void setLeftWheelMotorTemperature(int leftWheelMotorTemperature) {
        this.leftWheelMotorTemperature = leftWheelMotorTemperature;
    }

    public int getRightWheelMotorTemperature() {
        return rightWheelMotorTemperature;
    }

    public void setRightWheelMotorTemperature(int rightWheelMotorTemperature) {
        this.rightWheelMotorTemperature = rightWheelMotorTemperature;
    }

    public int getRotationMotorTemperature() {
        return rotationMotorTemperature;
    }

    public void setRotationMotorTemperature(int rotationMotorTemperature) {
        this.rotationMotorTemperature = rotationMotorTemperature;
    }

    public int getUpMotorTemperature() {
        return upMotorTemperature;
    }

    public void setUpMotorTemperature(int upMotorTemperature) {
        this.upMotorTemperature = upMotorTemperature;
    }

    public int getBatteryTemperatureOne() {
        return batteryTemperatureOne;
    }

    public void setBatteryTemperatureOne(int batteryTemperatureOne) {
        this.batteryTemperatureOne = batteryTemperatureOne;
    }

    public int getBatteryTemperatureTwo() {
        return batteryTemperatureTwo;
    }

    public void setBatteryTemperatureTwo(int batteryTemperatureTwo) {
        this.batteryTemperatureTwo = batteryTemperatureTwo;
    }

    public int getBatteryVoltage() {
        return batteryVoltage;
    }

    public void setBatteryVoltage(int batteryVoltage) {
        this.batteryVoltage = batteryVoltage;
    }
}
