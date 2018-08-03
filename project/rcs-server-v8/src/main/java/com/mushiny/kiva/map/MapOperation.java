/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.kiva.map;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Area;

/**
 *
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class MapOperation {

    private MapWindow mapWindow;
    private FontMetrics fm;

    public void installMapWindow(MapWindow mapWindow) {
        this.setMapWindow(mapWindow);
    }

//    /*
//      显示机器所在的CELL
//     */
//    public void showRobotByCell(CellNode robotCellNode) {
//        updateMap(robotCellNode, MapConfig.ROBOT_MARGIN_POSITION_COLOR, MapConfig.ROBOT_POSITION_COLOR);
//    }
//
//    //显示指定的ROBOT所在的位置
//    public void showRobotPosition(int adddressCodeID) {
//        updateMap(adddressCodeID, MapConfig.ROBOT_MARGIN_POSITION_COLOR, MapConfig.ROBOT_POSITION_COLOR);
//    }
//
//    //显示指定的PATH
//    public void showRobotPath(int[] addressCodeIDS) {
//        updateMap(addressCodeIDS, MapConfig.CELL_MARGIN_PATH_COLOR, MapConfig.CELL_PATH_COLOR);
//    }
//
//    public void showRobotPath(Rectangle[] RS) {
//        updateMapImages(RS, MapConfig.CELL_MARGIN_PATH_COLOR, MapConfig.CELL_PATH_COLOR);
//    }
//
//    public void showRobotPath(Rectangle r) {
//        updateMapImage(r, MapConfig.CELL_MARGIN_PATH_COLOR, MapConfig.CELL_PATH_COLOR);
//    }
//
//    //指定的AddressCode为非PATH
//    public void setCellNoPath(int addressCodeID) {
//        updateMap(addressCodeID, MapConfig.CELL_MARGIN_COMMON_COLOR, MapConfig.CELL_COMMON_COLOR);
//    }
//
//    public void setCellNoPath(Rectangle r) {
//        updateMapImage(r, MapConfig.CELL_MARGIN_COMMON_COLOR, MapConfig.CELL_COMMON_COLOR);
//    }
//
//    //显示指定的锁定PATH
//    public void showRobotLockedPath(int[] addressCodeIDS) {
//        updateMap(addressCodeIDS, MapConfig.CELL_MARGIN_COMMON_COLOR, MapConfig.CELL_LOCKED_PATH_COLOR);
//    }
//==============================================================================
    /*
     用指定的颜色指定的地址码ID，更新CELL
     */
    public void updateMap(int addressCodeID, Color marginColor, Color fillColor) {
        if (getMapWindow().getMap() == null) {
            return;
        }
        CellNode cellNode = getMapWindow().getMap().getMapCellByAddressCodeID(addressCodeID);
        updateMap(cellNode, marginColor, fillColor);
    }

    public void updateMap(int[] addressCodeID, Color marginColor, Color fillColor) {
        if (getMapWindow().getMap() == null) {
            return;
        }
        CellNode[] cellNodes = new CellNode[addressCodeID.length];
        for (int i = 0; i < cellNodes.length; i++) {
            CellNode cellNode = getMapWindow().getMap().getMapCellByAddressCodeID(addressCodeID[i]);
            cellNodes[i] = cellNode;
        }
        updateMap(cellNodes, marginColor, fillColor);
    }

    /*
     用指定的颜色更新指定ROW,COL的CELL
     */
    public void updateMap(int row, int col, Color marginColor, Color fillColor) {
        if (getMapWindow().getMap() == null) {
            return;
        }
        CellNode cellNode = getMapWindow().getMap().getMapCell(row, col);
        updateMap(cellNode, marginColor, fillColor);
    }

    public void updateMap(int[] row, int[] col, Color marginColor, Color fillColor) {
        if (getMapWindow().getMap() == null) {
            return;
        }
        CellNode[] cellNodes = new CellNode[row.length];
        for (int i = 0; i < cellNodes.length; i++) {
            CellNode cellNode = getMapWindow().getMap().getMapCell(row[i], col[i]);
            cellNodes[i] = cellNode;
        }
        updateMap(cellNodes, marginColor, fillColor);
    }

    /*
     
     */

 /*
     用指定的颜色更新指定的CELL
     */
    public void updateMap(CellNode cellNode) {
        updateMapImage(cellNode.getCellViewRectangle(), new Color(cellNode.getCellMarginColor()), new Color(cellNode.getCellColor()));
    }

    public void updateMap(CellNode cellNode, Color marginColor, Color fillColor) {
        updateMapImage(cellNode.getCellViewRectangle(), marginColor, fillColor);
    }

    public void updateMap(CellNode[] cellNode, Color marginColor, Color fillColor) {
        Rectangle[] RS = new Rectangle[cellNode.length];
        int i = 0;
        for (CellNode cn : cellNode) {
            RS[i++] = cn.getCellViewRectangle();
        }
        updateMapImages(RS, marginColor, fillColor);
    }

    /*
     用指定的颜色更新地图的指定区域
     */
    public void updateMapImages(Rectangle[] updateRectangles, Color marginColor, Color fillColor) {
        if (getMapWindow().getMapBufferedImage() == null) {
            return;
        }
        Graphics2D g2 = getMapWindow().getMapBufferedImage().createGraphics();
        for (Rectangle updateRectangle : updateRectangles) {
            if (getMapWindow().isViewCell()) {
                g2.setColor(marginColor);
                g2.draw(updateRectangle);
                g2.setColor(fillColor);
                g2.fillRect(updateRectangle.x + 1, updateRectangle.y + 1, updateRectangle.width - 1, updateRectangle.height - 1);
            } else {
                if (getMapWindow().isViewRobotInfo()) {

                } else {
                    g2.setColor(fillColor);
                    g2.fillRect(updateRectangle.x + 1, updateRectangle.y + 1, updateRectangle.width - 1, updateRectangle.height - 1);
                }
            }
        }
        g2.dispose();
        getMapWindow().updateMap();
    }

    public void updateMapImage(Rectangle updateRectangle, Color marginColor, Color fillColor) {
        if (getMapWindow().getMapBufferedImage() == null) {
            return;
        }
        Graphics2D g2 = getMapWindow().getMapBufferedImage().createGraphics();
        g2.setColor(marginColor);
        g2.draw(updateRectangle);
        g2.setColor(fillColor);
        g2.fillRect(updateRectangle.x + 1, updateRectangle.y + 1, updateRectangle.width - 1, updateRectangle.height - 1);
        g2.dispose();
        getMapWindow().updateMap();
    }

    /*==============================================================================
                            出走半生，归来仍是少年
===============================================================================*/
    public void showRobotPosition(CellNode cell, Color marginColor, Color fillColor) {
        if (getMapWindow().getMapBufferedImage() == null || cell == null) {
            return;
        }
        Graphics2D g2 = getMapWindow().getMapBufferedImage().createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        Rectangle cellRectangle = cell.getCellViewRectangle();
        if (getMapWindow().isViewCell()) {
            g2.setColor(marginColor);
            g2.draw(cellRectangle);
            g2.setColor(fillColor);
            g2.fillRect(cellRectangle.x + 1, cellRectangle.y + 1, cellRectangle.width - 1, cellRectangle.height - 1);
        } else {
            if (getMapWindow().isViewRobotInfo()) {
                //--showDetailCellInfo(g2, cell, marginColor, fillColor);
            } else {
                g2.setColor(fillColor);
                g2.fillRect(cellRectangle.x + 1, cellRectangle.y + 1, cellRectangle.width - 1, cellRectangle.height - 1);
            }
        }
        g2.dispose();
        getMapWindow().updateMap();
    }

    /*
    public void showDetailCellInfo(Graphics2D g, MapCell mapCell, Color marginColor, Color fillColor) {
        Rectangle R = mapCell.getCellViewRectangle();
        //初始化
        g.setColor(MapConfig.MAP_BACKGROUD_COLOR);
        g.fillRect(R.x, R.y, R.width, R.height);
        // g.clearRect(R.x, R.y, R.width, R.height);
        if (mapCell.getRobotMessage() == null) {
            System.out.println("---机器信息为空!!");
            return;
        }
        String row1 = "x=" + mapCell.getPoint().x + ",y=" + mapCell.getPoint().y;
        String row2 = "robotID=" + mapCell.getRobotMessage().getRobotID() + ",speed=" + mapCell.getRobotMessage().getSpeed();
        String row3 = "addCodeID=" + mapCell.getRobotMessage().getAddressCodeID() + ",podID=" + mapCell.getRobotMessage().getPodCodeID();

        Point beginPoint = new Point(R.x + MapConfig.ROBOT_INFO_VIEW_CIRCLE_R, R.y + R.height - MapConfig.ROBOT_INFO_VIEW_CIRCLE_R);
        Ellipse2D.Float circle = new Ellipse2D.Float(beginPoint.x, beginPoint.y, MapConfig.ROBOT_INFO_VIEW_CIRCLE_R, MapConfig.ROBOT_INFO_VIEW_CIRCLE_R);
        beginPoint.x = (int) circle.getCenterX();
        beginPoint.y = (int) circle.getCenterY();
        //
        Point middleP = new Point(beginPoint.x + MapConfig.ROBOT_INFO_VIEW_MIDDLE_P_W, beginPoint.y - MapConfig.ROBOT_INFO_VIEW_MIDDLE_P_H);
        Line2D zx1 = new Line2D.Float(beginPoint.x, beginPoint.y, middleP.x, middleP.y);
        //
        g.setFont(new Font(MapConfig.ROBOT_INFO_VIEW_FONT_NAME, Font.PLAIN, MapConfig.ROBOT_INFO_VIEW_FONT_SIZE));
        fm = g.getFontMetrics();
        int rowMaxWidth =0;
        int maxWidth = 0;
        int maxHeight = 0;
        int row1Width = (int) fm.stringWidth(row1);
        int row2Width = (int) fm.stringWidth(row2);
        int row3Width = (int) fm.stringWidth(row3);
        int row1Decent = (int)(fm.getLineMetrics(row1, g).getDescent());
        int row2Decent = (int)(fm.getLineMetrics(row2, g).getDescent());
        int row3Decent = (int)(fm.getLineMetrics(row3, g).getDescent());
        int row1Ascent = (int)(fm.getLineMetrics(row1, g).getAscent());
        int row2Ascent = (int)(fm.getLineMetrics(row2, g).getAscent());
        int row3Ascent = (int)(fm.getLineMetrics(row3, g).getAscent());
        
        rowMaxWidth = row1Width;
        if(rowMaxWidth < row2Width){
            rowMaxWidth = row2Width;
        }
        if(rowMaxWidth < row3Width){
            rowMaxWidth = row3Width;
        }
        maxWidth = rowMaxWidth+MapConfig.ROBOT_INFO_VIEW_CIRCLE_R+MapConfig.ROBOT_INFO_VIEW_MIDDLE_P_W;
        maxHeight = MapConfig.ROBOT_INFO_VIEW_CIRCLE_R + MapConfig.ROBOT_INFO_VIEW_MIDDLE_P_H;
        maxHeight+=3+row3Decent+row3Ascent+row2Decent+row2Ascent+row1Decent+row1Ascent;
        R.width=maxWidth;
        R.height=maxHeight+9;
        g.setColor(MapConfig.MAP_BACKGROUD_COLOR);
        g.fillRect(R.x, R.y-(maxHeight-R.height), maxWidth, maxHeight);
        R.y=R.y-(maxHeight-R.height)-7;
        g.setColor(marginColor);
        g.drawString(row1, middleP.x, middleP.y - (int)(row3Decent+row3Ascent+row2Decent+row2Ascent+row1Decent+3));
        g.drawString(row2, middleP.x, middleP.y - (int)(row3Decent+row3Ascent+row2Decent+2));
        g.drawString(row3, middleP.x, middleP.y - (int)(row3Decent+1));
        Line2D.Float zx2 = new Line2D.Float(middleP.x, middleP.y, (beginPoint.x + rowMaxWidth), middleP.y);
        g.draw(zx1);
        g.draw(zx2);
        g.fill(circle);
    }*/


    public MapWindow getMapWindow() {
        return mapWindow;
    }
    public void setMapWindow(MapWindow mapWindow) {
        this.mapWindow = mapWindow;
    }

}
