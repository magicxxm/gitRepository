/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.rcs.listener;

import java.awt.geom.AffineTransform;

/**
 * 地图视图改变监听
 *  @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public interface MapViewChangedListener {
    void onLoadingMapData();
    void onZoom(AffineTransform transform);
    void onFlip(int x,int y,double theta);
}
