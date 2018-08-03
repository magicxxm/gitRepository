/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.kiva.map;

import com.mingchun.mu.mushiny.kiva.individual.IndividualCellNodeManager;
import com.mingchun.mu.mushiny.kiva.path.TriangleRotateAreaManager;
import com.mingchun.mu.mushiny.kiva.path.TriangleRotateAreaNewManager;
import com.mushiny.rcs.global.MapConfig;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 *
 *  地图显示类
 *
 *@author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class MapWindow extends MapContainer {

    private KivaMap map; // 地图
    private AGVViewController robotViewController;
    private AffineTransform transform; // 缩放  旋转 -- 仿射变换
    private final int marginTop = 50;
    private final int marginBotton = 50;
    private final int marginLeft = 50;
    private final int marginRight = 50;
    private float scaleRadio = 1.0f;
    private int mapWidth;
    private int mapHeight;
    private BufferedImage sourceImage; // 原图
    private BufferedImage mapBufferedImage; // 缓冲图
    private boolean original = true;

    private MapFlipFilter mapFlip;
    private int flip;

    public MapWindow() {
        super();
        mapFlip = new MapFlipFilter(); // 仿射变换主方法
        robotViewController = AGVViewController.getInstance();
        robotViewController.installMapWindow(this);
    }

    /**
     * 初始化地图显示大小
     * @return
     */
    @Override
    public Dimension getPreferredSize() {
        Dimension dimension = new Dimension();
        if (getMap() == null) {
            return new Dimension(200, 200);
        }
        dimension.setSize(getMapWidth(), getMapHeight());
        return dimension;
    }

    /**
     * 画地图
     * @param g
     */
    @Override
    protected synchronized void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        showMap(g2);
    }

    /**
     * 画地图
     * @param g2
     */
    private void showMap(Graphics2D g2) {
        if (getMapBufferedImage() != null) {
            g2.scale(getScaleRadio(), getScaleRadio());
            g2.drawImage(getMapBufferedImage(), 0, 0, null);
        }
    }

    /*
     形成地图
     */
    public void createMapBuffer() {
        if (getSourceImage() == null) {
            return;
        }
        Graphics2D g2 = getSourceImage().createGraphics();
        createMapData(g2);
        g2.dispose();
        //--setScaleMapBufferedImage(MapUtil.cloneImageBuffer(getMapBufferImage()));
        mapBufferedImage = getSourceImage();
        createAL();
        setOriginalSize();
    }

    /**
     *  形成地图数据 -- 不可通过，可通过，旋转区
     */
    public void createMapData(Graphics2D g) {
        if (getMap() == null) {
            return;
        }
        g.setBackground(MapConfig.MAP_BACKGROUD_COLOR);
        g.clearRect(0, 0, getMapWidth(), getMapHeight());

        int x = getMarginLeft(), y = getMarginTop();
        MapCell mapCell;
        for (int row = 0; row < getMap().getMapRow(); row++) {
            for (int col = 0; col < getMap().getMapCol(); col++) {
                mapCell = getMap().getMapCell(row, col);
                Rectangle r = new Rectangle(x, y, MapConfig.CELL_WIDTH, MapConfig.CELL_HEIGHT); // 设置格子显示大小
                Rectangle cellR = new Rectangle(x, y, MapConfig.CELL_WIDTH, MapConfig.CELL_HEIGHT); // 设置格子显示区域大小
                mapCell.setRectangle(r);
                mapCell.setCellViewRectangle(cellR);
                if (isViewCell()) {
                    if (!mapCell.isWalkable()) {//不可通过
                        g.setColor(MapConfig.CELL_UNWALKED_COLOR);
                        g.draw(cellR);
                        g.setColor(MapConfig.CELL_UNWALKED_MARGIN_COLOR);
                        g.fillRect(cellR.x + 1, cellR.y + 1, cellR.width - 1, cellR.height - 1);
                    } else {
                        if (mapCell.isInRotateArea() != null) {//旋转区
                            g.setColor(MapConfig.CELL_ROTATE_AREA_COLOR);
                            g.draw(cellR);
                            g.setColor(MapConfig.CELL_ROTATE_AREA_MARGIN_COLOR);
                            g.fillRect(cellR.x + 1, cellR.y + 1, cellR.width - 1, cellR.height - 1);
                        } else if(TriangleRotateAreaManager.getInstance().getTriangleRotateAreaByCellNode(mapCell) != null){ // 三角旋转区
                            g.setColor(MapConfig.CELL_ROTATE_AREA_COLOR);
                            g.draw(cellR);
                            g.setColor(MapConfig.CELL_ROTATE_AREA_MARGIN_COLOR);
                            g.fillRect(cellR.x + 1, cellR.y + 1, cellR.width - 1, cellR.height - 1);
                        } else if(TriangleRotateAreaNewManager.getInstance().getTriangleRotateAreaNewByCellNode(mapCell) != null){ // 新三角旋转区
                            g.setColor(MapConfig.CELL_ROTATE_AREA_COLOR);
                            g.draw(cellR);
                            g.setColor(MapConfig.CELL_ROTATE_AREA_MARGIN_COLOR);
                            g.fillRect(cellR.x + 1, cellR.y + 1, cellR.width - 1, cellR.height - 1);
                        } else if(IndividualCellNodeManager.getInstance().getIndividualCellNodeByCellNode(mapCell) != null){ // 孤立点
                            g.setColor(MapConfig.CELL_MARGIN_COMMON_COLOR);
                            g.draw(cellR);
                            g.setColor(MapConfig.INDIVIDUAL_CELL_COLOR);
                            g.fillRect(cellR.x + 1, cellR.y + 1, cellR.width - 1, cellR.height - 1);
                        } else {
                            g.setColor(new Color(mapCell.getCellMarginColor()));
                            g.draw(cellR);
                            g.setColor(new Color(mapCell.getCellColor()));
                            g.fillRect(cellR.x + 1, cellR.y + 1, cellR.width - 1, cellR.height - 1);
                        }
                    }
                }
                x += MapConfig.CELL_WIDTH;
            }
            x = getMarginLeft();
            y += MapConfig.CELL_HEIGHT;
        }
    }

    /*
     建立本地化适宜的图像
     */
    public void createBaseMapImage() {
        if (getMap() == null) {
            return;
        }
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
        GraphicsConfiguration gc = device.getDefaultConfiguration();
        setScaleRadio(1.0f);
        setOriginal(true);
        setFlip(0);
        sourceImage = gc.createCompatibleImage(getMapWidth(), getMapHeight(), Transparency.OPAQUE);
    }

    //原始大小
    public void setOriginalSize() {
        mapWidth = MapConfig.MAP_MARGIN_LEFT + MapConfig.CELL_WIDTH * map.getMapCol() + MapConfig.MAP_MARGIN_RIGHT;
        mapHeight = MapConfig.MAP_MARGIN_TOP + MapConfig.CELL_HEIGHT * map.getMapRow() + MapConfig.MAP_MARGIN_BOTTON;
        setScaleRadio(1.0f);
        setOriginal(true);
        setFlip(0);
        updateMap();
        Graphics2D g2 = (Graphics2D) getGraphics();
        g2.scale(getScaleRadio(), getScaleRadio());
        transform = g2.getTransform();
        fireMapViewChangedListenerZoom(transform);
    }

    //放大
    public void zoomIn() {
        setScaleRadio(getScaleRadio() + 0.1f);
        setOriginal(false);
        updateMap();
        Graphics2D g2 = (Graphics2D) getGraphics();
        g2.scale(getScaleRadio(), getScaleRadio());
        transform = g2.getTransform();
        fireMapViewChangedListenerZoom(transform);
    }

    //缩小
    public void zoomOut() {
        if (getScaleRadio() > 0.1f) {
            setScaleRadio(getScaleRadio() - 0.1f);
        } else {
            setScaleRadio(getScaleRadio() - 0.01f);
        }
        setOriginal(false);
        //scaleMapBufferedImage=MapUtil.mapZoom(scaleMapBufferedImage, getScaleRadio());
        // setScaleRadio(MapConfig.MAP_ZOOM_IN_RADIO);
        updateMap();
        Graphics2D g2 = (Graphics2D) getGraphics();
        g2.scale(getScaleRadio(), getScaleRadio());
        transform = g2.getTransform();
        fireMapViewChangedListenerZoom(transform);
    }

    //旋转
    public void flipMap(int flip) {
        this.flip = flip;
        setOriginal(false);
        if (flip == 90) {
            map.increaseRotateAngle(90);
            fireMapViewChangedListenerFlip(mapBufferedImage.getWidth() / 2, mapBufferedImage.getHeight() / 2, Math.PI / 2);
        } else {
            if (flip == -90) {
                map.decreaseRotateAngle(90);
                fireMapViewChangedListenerFlip(mapBufferedImage.getWidth() / 2, mapBufferedImage.getHeight() / 2, -Math.PI / 2);
            }
        }
        updateMap();
    }

    public void prepareMap() {
        if (getSourceImage() == null) {
            return;
        }

        if (flip == 90) {
            getMapFlip().setOperation(MapFlipFilter.FLIP_90CW);
            mapBufferedImage = getMapFlip().filter(getMapBufferedImage(), null);
            flip = 0;
        }
        if (flip == -90) {
            getMapFlip().setOperation(MapFlipFilter.FLIP_90CCW);
            mapBufferedImage = getMapFlip().filter(getMapBufferedImage(), null);
            flip = 0;
        }

        mapWidth = (int) (getMapBufferedImage().getWidth() * getScaleRadio());
        mapHeight = (int) (getMapBufferedImage().getHeight() * getScaleRadio());
    }

    public synchronized void updateMap() {
        prepareMap();
        revalidate();
        repaint();
    }

    /**
     * 画向北的箭头
     */
    public void createAL() {
        if (mapBufferedImage == null) {
            return;
        }
        Graphics2D g2 = mapBufferedImage.createGraphics();
        MapUtil.drawAL(getMarginLeft() - 10, getMarginTop() - 10, getMarginLeft() - 10, getMarginTop() - 35, g2);
        g2.drawString("O", getMarginLeft() - 5, getMarginTop() - 12);
        g2.dispose();
    }

    public void showTip(int x, int y, String tip) {
        Graphics2D g = (Graphics2D) getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(Color.RED);
        g.setFont(new Font("宋体", Font.BOLD, 24));
        g.drawString(tip, x, y);
        g.dispose();
    }
    public KivaMap getMap() {
        return map;
    }
    public void setMap(KivaMap map) {
        this.map = map;
        mapWidth = MapConfig.MAP_MARGIN_LEFT + MapConfig.CELL_WIDTH * map.getMapCol() + MapConfig.MAP_MARGIN_RIGHT;
        mapHeight = MapConfig.MAP_MARGIN_TOP + MapConfig.CELL_HEIGHT * map.getMapRow() + MapConfig.MAP_MARGIN_BOTTON;
        registerMapViewChangedListener(map);
        map.regsiteAGV2CellListener(robotViewController);
        map.registeCellListener(robotViewController);
    }
    public int getMarginTop() {
        return marginTop;
    }
    public int getMarginBotton() {
        return marginBotton;
    }
    public int getMarginLeft() {
        return marginLeft;
    }
    public int getMarginRight() {
        return marginRight;
    }
    public int getMapWidth() {
        return mapWidth;
    }
    public void setMapWidth(int mapWidth) {
        this.mapWidth = mapWidth;
    }
    public int getMapHeight() {
        return mapHeight;
    }
    public void setMapHeight(int mapHeight) {
        this.mapHeight = mapHeight;
    }
    public MapOperation getRobotViewController() {
        return robotViewController;
    }
    public BufferedImage getSourceImage() {
        return sourceImage;
    }
    public void setSourceImage(BufferedImage sourceImage) {
        this.sourceImage = sourceImage;
    }
    public BufferedImage getMapBufferedImage() {
        return mapBufferedImage;
    }
    public void setMapBufferedImage(BufferedImage mapBufferedImage) {
        this.mapBufferedImage = mapBufferedImage;
    }
    public MapFlipFilter getMapFlip() {
        return mapFlip;
    }
    public void setMapFlip(MapFlipFilter mapFlip) {
        this.mapFlip = mapFlip;
    }
    public int getFlip() {
        return flip;
    }
    public void setFlip(int flip) {
        this.flip = flip;
    }
    public boolean isOriginal() {
        return original;
    }
    public void setOriginal(boolean original) {
        this.original = original;
    }
    public float getScaleRadio() {
        return scaleRadio;
    }
    public void setScaleRadio(float scaleRadio) {
        this.scaleRadio = scaleRadio;
    }
    public AffineTransform getTransform() {
        return transform;
    }
    public void setTransform(AffineTransform transform) {
        this.transform = transform;
    }

}
