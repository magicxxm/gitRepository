package com.mushiny.kiva.map;

import com.mushiny.rcs.listener.MapIOListener;
import com.mushiny.rcs.listener.MapViewChangedListener;
import com.mushiny.rcs.global.MapConfig;
import java.awt.geom.AffineTransform;
import java.util.LinkedList;
import javax.swing.JPanel;

/**
 *
 * 注册地图监听器，及监听地图相关信息
 *
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class MapContainer extends JPanel {
    //模式
    public final static int VIEW_MODEL = 0; // 视图模式
    public final static int EDIT_MODEL = 1; // 编辑模式
    private int mapModel; // 地图模式 -- 变量
    private boolean viewCell;//显示网格
    private boolean viewRobotInfo;//显示信息

    private LinkedList<MapViewChangedListener> listeners = new LinkedList();

    public MapContainer() {
        super();
        mapModel = MapConfig.MAP_VIEW_MODEL;
        viewCell = MapConfig.MAP_VIEW_CELL;
        viewRobotInfo = MapConfig.MAP_VIEW_ROBOT_INFO;
    }

    /**
     * 鼠标移动事件添加方法
     * @param mapIOListener
     */
    public void installMapIOListener(MapIOListener mapIOListener) {
        addMouseListener(mapIOListener);
    }

    /**
     * 注册地图改变事件的监听器
     * @param listener
     */
    public synchronized void registerMapViewChangedListener(MapViewChangedListener listener) {
        if (!listeners.contains(listener)) {
            listeners.addLast(listener);
        }
    }

    /**
     * 移除地图改变事件的监听器
     * @param listener
     */
    public synchronized void removeMapViewChanagedListener(MapViewChangedListener listener) {
        if (listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }

    /**
     * 地图有数据加载事件触发 --> 地图改变监听器
     */
    public void fireLoadingMapData() {
        for (MapViewChangedListener listener : listeners) {
            listener.onLoadingMapData();
        }
    }

    /**
     * 地图旋转触发 --> 地图改变监听器
     * @param x
     * @param y
     * @param theta
     */
    public void fireMapViewChangedListenerFlip(final int x, final int y, final double theta) {
        new Thread() {
            public void run() {
                for (MapViewChangedListener listener : listeners) {
                    listener.onFlip(x, y, theta);
                }
            }
        }.start();
    }

    /**
     * 地图缩放触发 --> 地图改变监听器
     * @param transform
     */
    public void fireMapViewChangedListenerZoom(final AffineTransform transform) {
        new Thread() {
            public void run() {
                for (MapViewChangedListener listener : listeners) {
                    listener.onZoom(transform);
                }
            }
        }.start();
    }

    public int getMapModel() {
        return mapModel;
    }
    public void setMapModel(int mapModel) {
        this.mapModel = mapModel;
    }
    public boolean isViewCell() {
        return viewCell;
    }
    public void setViewCell(boolean viewCell) {
        this.viewCell = viewCell;
    }
    public boolean isViewRobotInfo() {
        return viewRobotInfo;
    }
    public void setViewRobotInfo(boolean viewRobotInfo) {
        this.viewRobotInfo = viewRobotInfo;
    }
}
