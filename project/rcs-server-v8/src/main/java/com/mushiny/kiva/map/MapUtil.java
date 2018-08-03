/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.kiva.map;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * 与地图有关的一些工具
 *
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class MapUtil {

    /*
     画箭头
     */
    public static void drawAL(int sx, int sy, int ex, int ey, Graphics2D g2) {
        double H = 10; // 箭头高度
        double L = 4; // 底边的一半
        int x3 = 0;
        int y3 = 0;
        int x4 = 0;
        int y4 = 0;
        double awrad = Math.atan(L / H); // 箭头角度  
        double arraow_len = Math.sqrt(L * L + H * H); // 箭头的长度  
        double[] arrXY_1 = rotateVec(ex - sx, ey - sy, awrad, true, arraow_len);
        double[] arrXY_2 = rotateVec(ex - sx, ey - sy, -awrad, true, arraow_len);
        double x_3 = ex - arrXY_1[0]; // (x3,y3)是第一端点  
        double y_3 = ey - arrXY_1[1];
        double x_4 = ex - arrXY_2[0]; // (x4,y4)是第二端点  
        double y_4 = ey - arrXY_2[1];

        Double X3 = new Double(x_3);
        x3 = X3.intValue();
        Double Y3 = new Double(y_3);
        y3 = Y3.intValue();
        Double X4 = new Double(x_4);
        x4 = X4.intValue();
        Double Y4 = new Double(y_4);
        y4 = Y4.intValue();
        // 画线  
        g2.drawLine(sx, sy, ex, ey);
        //  
        GeneralPath triangle = new GeneralPath();
        triangle.moveTo(ex, ey);
        triangle.lineTo(x3, y3);
        triangle.lineTo(x4, y4);
        triangle.closePath();
        //实心箭头  
        g2.fill(triangle);
        //非实心箭头  
        //g2.draw(triangle);  

    }

    // 计算  
    public static double[] rotateVec(int px, int py, double ang,
            boolean isChLen, double newLen) {

        double mathstr[] = new double[2];
        // 矢量旋转函数，参数含义分别是x分量、y分量、旋转角、是否改变长度、新长度  
        double vx = px * Math.cos(ang) - py * Math.sin(ang);
        double vy = px * Math.sin(ang) + py * Math.cos(ang);
        if (isChLen) {
            double d = Math.sqrt(vx * vx + vy * vy);
            vx = vx / d * newLen;
            vy = vy / d * newLen;
            mathstr[0] = vx;
            mathstr[1] = vy;
        }
        return mathstr;
    }

    /*
     拷贝一个新图像
     */
    public static BufferedImage cloneImageBuffer(BufferedImage source) {
        BufferedImage dest = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        dest.setData(source.getData());
        return dest;
    }
   public static BufferedImage mapZoom(BufferedImage image,float radio) {
       //radio = 1.0f;
        AffineTransform transform = new AffineTransform();
        transform.scale(radio,radio);
        AffineTransformOp op = new AffineTransformOp(transform,null);
        return op.filter(image, null);
   }
   
    public static void mapFlipTheta(KivaMap map, int x0, int y0, double theta) {
        float thetaR = new Float(Math.toRadians(theta));
        AffineTransform transform = new AffineTransform();
        transform.translate(x0, y0);
        transform.rotate(theta);
        transform.translate(-y0, -x0);

        for (int row = 0; row < map.getMapRow(); row++) {
            for (int col = 0; col < map.getMapCol(); col++) {
               // System.out.println("-----------坐标转换-------");
                Point p3 = new Point();
                //System.out.println("转换前r="+map.getMapCell(row, col).getRectangle());
                transform.transform(map.getMapCell(row, col).getRectangle().getLocation(), p3);
                map.getMapCell(row, col).getCellViewRectangle().setLocation(p3);
                //System.out.println("转换后r=" + map.getMapCell(row, col).getRectangle());
                System.out.println("转换后cell.r=" + map.getMapCell(row, col).getCellViewRectangle());
            }
        }
    }
   
}
