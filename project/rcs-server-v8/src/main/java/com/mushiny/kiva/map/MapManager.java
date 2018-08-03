/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.kiva.map;

import com.mushiny.rcs.listener.MapViewChangedListener;
import com.mushiny.rcs.server.AGV;
import com.mushiny.rcs.server.AGVManager;
import com.mushiny.rcs.server.KivaAGV;

/**
 *
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class MapManager {

    private static MapManager instance;
    private MapView mapView;
    private AGVManager robotManager;

    private KivaMap kivaMap;

    private MapManager() {
        // 单机测试。。。
//        mapView = new MapView();// 创建并显示地图


        robotManager = AGVManager.getInstance();
    }

    private static synchronized void initInstance(){
        if(instance == null){
            instance = new MapManager();
        }
    }

    public static MapManager getInstance() {
        if (instance == null) {
            initInstance();
        }
        return instance;
    }

    /*public static MapManager getInstance() {
        if (instance == null) {
            instance = new MapManager();
        }
        return instance;
    }*/

    public void installMap(KivaMap map) {
        this.kivaMap = map;
        // 单机测试。。。
//        getMapView().installMap(map);
    }
    public void  loadingMap() {
        getMapView().loadingMap();
    }
    public KivaMap getMap() {
        return this.kivaMap;
        // 单机测试。。。
//        return getMapView().getMapWindow().getMap();
    }
    public void showTip(int x, int y, String tip) {
        getMapWindow().showTip(x, y, tip);
    }
    public void registerMapViewChangedListener(MapViewChangedListener listener) {
        getMapWindow().registerMapViewChangedListener(listener);
    }
    public MapView getMapView() {
        return mapView;
    }
    public MapWindow getMapWindow() {
        return mapView.getMapWindow();
    }
}
