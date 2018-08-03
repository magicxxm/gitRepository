/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aricojf.platform.mina.message.robot;

/**
 *
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public interface OnReceiveAGVActionCommandResponseMessageListener {
    public void onReceivedAGVActionCommandResponseMessage(Robot2RCSActionCommandResponseMessage data);
}
