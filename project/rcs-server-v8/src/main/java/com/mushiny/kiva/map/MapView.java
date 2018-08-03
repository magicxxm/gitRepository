package com.mushiny.kiva.map;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * 后台显示窗口
 */
public class MapView extends JPanel {

    private MapWindow mapWindow;
    private JButton zoomInButton;
    private JButton zoomOutButton;
    private JButton originButton;
    private JButton rightRotateButton;
    private JButton leftRotateButton;

    public MapView() {
        super();
        this.mapWindow = new MapWindow();
        setLayout(new BorderLayout());
        initUI();
    }

    public void installMap(KivaMap map) {
        mapWindow.setMap(map);
    }

    public void loadingMap() {
//        mapWindow.fireLoadingMapData();
        mapWindow.createBaseMapImage();
        mapWindow.createMapBuffer();
    }

    /**
     * 添加按钮和显示地图窗口
     */
    public void initUI() {
        JPanel optionPanel = new JPanel(new FlowLayout());
        zoomInButton = new JButton(new ZoomInAction());
        zoomOutButton = new JButton(new ZoomOutAction());
        originButton = new JButton(new OriginAction());
        rightRotateButton = new JButton(new RightRotateAction());
        leftRotateButton = new JButton(new LeftRotateAction());
        optionPanel.add(zoomInButton);
        optionPanel.add(zoomOutButton);
        optionPanel.add(originButton);
        optionPanel.add(leftRotateButton);
        optionPanel.add(rightRotateButton);
        add(optionPanel, BorderLayout.NORTH);
        ScrollPane sp = new ScrollPane();
        sp.add(getMapWindow());
        add(sp, BorderLayout.CENTER);
    }
    class ZoomInAction extends AbstractAction {
        public ZoomInAction() {
            putValue(Action.NAME, "放大");
        }
        public void actionPerformed(ActionEvent e) {
            getMapWindow().zoomIn();
        }
    }
    class ZoomOutAction extends AbstractAction {
        public ZoomOutAction() {
            putValue(Action.NAME, "缩小");
        }
        public void actionPerformed(ActionEvent e) {
            getMapWindow().zoomOut();
        }
    }
    class OriginAction extends AbstractAction {
        public OriginAction() {
            putValue(Action.NAME, "原始");
        }
        public void actionPerformed(ActionEvent e) {
            getMapWindow().setOriginalSize();
        }
    }
    class RightRotateAction extends AbstractAction {
        public RightRotateAction() {
            putValue(Action.NAME, "右90");
        }
        public void actionPerformed(ActionEvent e) {
            getMapWindow().flipMap(90);
        }
    }
    class LeftRotateAction extends AbstractAction {
        public LeftRotateAction() {
            putValue(Action.NAME, "左90");
        }
        public void actionPerformed(ActionEvent e) {
            getMapWindow().flipMap(-90);
        }
    }

    public MapWindow getMapWindow() {
        return mapWindow;
    }
    public void setMapWindow(MapWindow mapWindow) {
        this.mapWindow = mapWindow;
    }
}
