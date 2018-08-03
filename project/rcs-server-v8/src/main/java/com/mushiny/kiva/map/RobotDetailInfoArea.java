/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.kiva.map;

import java.awt.geom.Area;

/**
 *机器详细信息区域
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class RobotDetailInfoArea extends Area{
    private int x;
    private int y;
    private int width;
    private int height;
    public RobotDetailInfoArea(int x,int y,int width,int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
