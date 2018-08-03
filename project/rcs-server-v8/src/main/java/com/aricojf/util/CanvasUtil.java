/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aricojf.util;

import java.awt.Graphics2D;

/**
 *画板工具
 * @author aricochen
 */
public class CanvasUtil {
    static int width = 0;
    static int height = 0;
    
    //以当前坐标点为矩形中心,画指定高度和宽度的矩形
    public static void drawRect(Graphics2D g,int x,int y,int width,int height) {
        int beingX = x-width/2;
        int beginY = y-height/2;
        g.drawRect(beingX, beginY, width, height);
    }
    //以当前坐标为中心,画正在进行动作的圆圈
    public static void drawWaiting(Graphics2D g,int x,int y) {
        if(width==150) {
            width = 0;
            height = 0;
        }
        width += 30;
        height += 30;
        g.drawOval(x, y, width, height);
    }
}
