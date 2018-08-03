/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.kiva.pathviewtool;

import com.mushiny.kiva.map.KivaMap;
import com.mushiny.kiva.map.MapManager;
import com.mushiny.kiva.pathviewtool.remotemonitor.RemoteMonitorServer;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

/**
 *
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class PathViewApplicationForRCS {

    private JFrame mainFrame;
    private MapManager mapManager;
    private AGVListView agvListView; // agv 上线个数及状态显示
    private AGVDataView agvDataView; // agv 收到数据包显示
    private JPanel leftPanel;
    private KivaMap kivaMap;

    public PathViewApplicationForRCS(KivaMap map) {
        mainFrame = new JFrame();
        mainFrame.setTitle("AGV监控工具V0.2");
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); // 默认最大化
        mainFrame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        this.kivaMap = map;
    }

    /**
     * 显示主控制面板
     */
    public void visible() {
        mapManager = MapManager.getInstance();

        agvListView = new AGVListView();
        agvDataView = new AGVDataView();

        leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.add(agvListView, BorderLayout.NORTH);
        leftPanel.add(agvDataView, BorderLayout.CENTER);
        
        JSplitPane splitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,true,leftPanel,mapManager.getMapView());
        splitPanel.setOneTouchExpandable(true);
        splitPanel.setDividerLocation(400);
        mainFrame.getContentPane().setLayout(new BorderLayout());
        mainFrame.getContentPane().add(splitPanel, BorderLayout.CENTER);
        mainFrame.setVisible(true);
        
        mapManager.loadingMap();
        /*new Thread() {
            public void run() {
                RemoteMonitorServer server = RemoteMonitorServer.getInstance();
                server.startServer();
            }
        }.start();*/
    }

}
