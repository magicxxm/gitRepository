/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.rcs.listener;

import com.mushiny.rcs.server.KivaAGV;

import java.util.Map;

/**
 *AGV超时监听
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public interface AGVTimeoutListener {
    //心跳超时
    public void onAGVBeatTimeout(KivaAGV agv) ;
    //实时数据包超时
    public void onAGVRTTimeout(KivaAGV agv);
    //心跳或实时数据包超时
    public void onAGVBeatOrRTTimeout(KivaAGV agv);
    //位置不改变超时
//    public void onAGVPositionNoChanageTimeout(KivaAGV agv);
    //位置不改变超时
    public void onAGVPositionNoChanageTimeout(KivaAGV agv, Map<String, Object> paramMap);
    //锁格超时
//    public void onAGVLockCellTimeout(KivaAGV agv);
    //锁格超时
    public void onAGVLockCellTimeout(KivaAGV agv, Map<String, Object> paramMap);
}
