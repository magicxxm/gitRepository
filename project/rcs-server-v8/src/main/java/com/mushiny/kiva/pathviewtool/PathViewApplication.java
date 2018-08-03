package com.mushiny.kiva.pathviewtool;

import com.aricojf.platform.mina.server.ServerManager;
import com.aricojf.platform.mina.server.ServerMessageService;
import com.mushiny.kiva.map.KivaMap;
import com.mushiny.kiva.map.MapManager;
import com.mushiny.kiva.pathviewtool.remotemonitor.RemoteMonitorServer;
import com.mushiny.rcs.server.RCSTimer;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

/**
 *
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class PathViewApplication {

    private JFrame mainFrame;
    private MapManager mapManager;
    private AGVListView agvListView;
    private AGVDataView agvDataView;
    private JPanel leftPanel;
    private KivaMap kivaMap;

    public PathViewApplication(KivaMap map) {
        mainFrame = new JFrame();
        mainFrame.setTitle("AGV监控工具V0.1");
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        this.kivaMap = map;
    }

    public void visible() {
        kivaMap.initKivaMap();
        mapManager = MapManager.getInstance();
        mapManager.installMap(kivaMap);
        agvListView = new AGVListView();
        agvDataView = new AGVDataView();

        leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.add(agvListView, BorderLayout.NORTH);
        leftPanel.add(agvDataView, BorderLayout.CENTER);

        JSplitPane splitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, leftPanel, mapManager.getMapView());
        splitPanel.setOneTouchExpandable(true);
        splitPanel.setDividerLocation(400);
        mainFrame.getContentPane().setLayout(new BorderLayout());
        mainFrame.getContentPane().add(splitPanel, BorderLayout.CENTER);
        mainFrame.setVisible(true);

        mapManager.loadingMap();
        new Thread() {
            public void run() {
                RemoteMonitorServer server = RemoteMonitorServer.getInstance();
                server.startServer();
            }
        }.start();

    }

    /*public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                KivaMap kivaMap = new KivaMap(8, 5);
                PathViewApplication pathViewAppliation = new PathViewApplication(kivaMap);

                pathViewAppliation.visible();

                ServerMessageService kivaServer = ServerManager.getMessageServerInstance();
                kivaServer.Begin();
                RCSTimer.getInstance().start();

            }
        });

    }*/
}
