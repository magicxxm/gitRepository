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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;


/**
 * MAP显示通用CELLNODE操作实现
 *
 * @author 陈庆余 <18292019681 13469592826@163.com>
 */
public class MapViewCommonOperation extends MapOperation {

    private static Logger LOG = LoggerFactory.getLogger(MapViewCommonOperation.class.getName());

    /*
     CELLNode显示
     */
    public void updateCellNodeView(CellNode cellNode) {
        if (getMapWindow().getMapBufferedImage() == null || cellNode == null) {
            return;
        }
        if (!MapManager.getInstance().getMap().isInMap(cellNode.getAddressCodeID())) {
            return;
        }
        Graphics2D g2 = getMapWindow().getMapBufferedImage().createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        Rectangle cellRectangle_Big = cellNode.getCellViewRectangle();
        Rectangle cellRectangle1_Small = new Rectangle();
        cellRectangle1_Small.x = (int) (cellRectangle_Big.x + (cellRectangle_Big.getWidth() * 0.5) * 0.6);
        cellRectangle1_Small.y = (int) (cellRectangle_Big.y + (cellRectangle_Big.getWidth() * 0.5) * 0.6);
        cellRectangle1_Small.width = (int) (cellRectangle_Big.width * 0.4);
        cellRectangle1_Small.height = (int) (cellRectangle_Big.height * 0.4);
        if (!cellNode.isWalkable()) {
            g2.setColor(MapConfig.CELL_UNWALKED_MARGIN_COLOR);
            g2.draw(cellRectangle_Big);
            g2.setColor(MapConfig.CELL_UNWALKED_COLOR);
            g2.fillRect(cellRectangle_Big.x + 1, cellRectangle_Big.y + 1, cellRectangle_Big.width - 1, cellRectangle_Big.height - 1);
            return;
        }
        if (getMapWindow().isViewCell()) {
            if (cellNode.isAGVIn()) { // 有agv进入此格子
                g2.setColor(MapConfig.AGV_MARGIN_POSITION_COLOR);
                g2.draw(cellRectangle_Big);
                g2.setColor(MapConfig.AGV_POSITION_COLOR);
                g2.fillRect(cellRectangle_Big.x + 1, cellRectangle_Big.y + 1, cellRectangle_Big.width - 1, cellRectangle_Big.height - 1);
                if (cellNode.isSelected()) { // 鼠标单击选中格子
                    g2.setColor(MapConfig.CELL_SELECTED_MARGIN_COLOR);
                    g2.draw(cellRectangle1_Small);
                    g2.setColor(MapConfig.CELL_SELECTED_COLOR);
                    g2.fillRect(cellRectangle1_Small.x + 1, cellRectangle1_Small.y + 1, cellRectangle1_Small.width - 1, cellRectangle1_Small.height - 1);
                }
                if (!cellNode.getSeriesPath().isEmpty()) { // 格子所在的所有路径不为空
                    g2.setColor(MapConfig.CELL_MARGIN_PATH_COLOR);
                    g2.draw(cellRectangle1_Small);
                    g2.setColor(MapConfig.CELL_PATH_COLOR);
                    g2.fillRect(cellRectangle1_Small.x + 1, cellRectangle1_Small.y + 1, cellRectangle1_Small.width - 1, cellRectangle1_Small.height - 1);
                }
               //-- if (cellNode.isLocked_MapLock()) {
                if (cellNode.isLocked()) {
                    g2.setColor(MapConfig.CELL_LOCKED_MARGIN_PATH_COLOR);
                    g2.draw(cellRectangle1_Small);
                    g2.setColor(MapConfig.CELL_LOCKED_PATH_COLOR);
                    g2.fillRect(cellRectangle1_Small.x + 1, cellRectangle1_Small.y + 1, cellRectangle1_Small.width - 1, cellRectangle1_Small.height - 1);
                }
                g2.drawString(cellNode.getAGV().getID() + "," + cellNode.getAddressCodeID(), cellRectangle_Big.x + 5, cellRectangle_Big.y + 10);

                g2.dispose();
                getMapWindow().updateMap();
                return;
            }
            if (!cellNode.isAGVIn()) {
                if (cellNode.isSelected()) {
                    g2.setColor(MapConfig.CELL_SELECTED_MARGIN_COLOR);
                    g2.draw(cellRectangle_Big);
                    g2.setColor(MapConfig.CELL_SELECTED_COLOR);
                    g2.fillRect(cellRectangle_Big.x + 1, cellRectangle_Big.y + 1, cellRectangle_Big.width - 1, cellRectangle_Big.height - 1);
                } else {
                    if (cellNode.isInRotateArea() != null) {
                        g2.setColor(MapConfig.CELL_ROTATE_AREA_COLOR);
                        g2.draw(cellRectangle_Big);
                        g2.setColor(MapConfig.CELL_ROTATE_AREA_MARGIN_COLOR);
                        g2.fillRect(cellRectangle_Big.x + 1, cellRectangle_Big.y + 1, cellRectangle_Big.width - 1, cellRectangle_Big.height - 1);
                    }else if(TriangleRotateAreaManager.getInstance().getTriangleRotateAreaByCellNode(cellNode) != null){ // 三角旋转区颜色显示（小车锁格离开后复原）
                        g2.setColor(MapConfig.CELL_ROTATE_AREA_COLOR);
                        g2.draw(cellRectangle_Big);
                        g2.setColor(MapConfig.CELL_ROTATE_AREA_MARGIN_COLOR);
                        g2.fillRect(cellRectangle_Big.x + 1, cellRectangle_Big.y + 1, cellRectangle_Big.width - 1, cellRectangle_Big.height - 1);
                    }else if(TriangleRotateAreaNewManager.getInstance().getTriangleRotateAreaNewByCellNode(cellNode) != null){ // 新三角旋转区颜色显示（小车锁格离开后复原）
                        g2.setColor(MapConfig.CELL_ROTATE_AREA_COLOR);
                        g2.draw(cellRectangle_Big);
                        g2.setColor(MapConfig.CELL_ROTATE_AREA_MARGIN_COLOR);
                        g2.fillRect(cellRectangle_Big.x + 1, cellRectangle_Big.y + 1, cellRectangle_Big.width - 1, cellRectangle_Big.height - 1);
                    }else if(IndividualCellNodeManager.getInstance().getIndividualCellNodeByCellNode(cellNode) != null){// 孤立点颜色显示（小车锁格离开后复原）
                        g2.setColor(MapConfig.CELL_MARGIN_COMMON_COLOR);
                        g2.draw(cellRectangle_Big);
                        g2.setColor(MapConfig.INDIVIDUAL_CELL_COLOR);
                        g2.fillRect(cellRectangle_Big.x + 1, cellRectangle_Big.y + 1, cellRectangle_Big.width - 1, cellRectangle_Big.height - 1);
                    } else {
                        g2.setColor(MapConfig.CELL_MARGIN_COMMON_COLOR);
                        g2.draw(cellRectangle_Big);
                        g2.setColor(MapConfig.CELL_COMMON_COLOR);
                        g2.fillRect(cellRectangle_Big.x + 1, cellRectangle_Big.y + 1, cellRectangle_Big.width - 1, cellRectangle_Big.height - 1);
                    }
                }
                if (!cellNode.getSeriesPath().isEmpty()) { // 格子所在的所有路径不为空
                    g2.setColor(MapConfig.CELL_MARGIN_PATH_COLOR);
                    g2.draw(cellRectangle_Big);
                    g2.setColor(MapConfig.CELL_PATH_COLOR);
                    g2.fillRect(cellRectangle_Big.x + 1, cellRectangle_Big.y + 1, cellRectangle_Big.width - 1, cellRectangle_Big.height - 1);
                }
                //-- if (cellNode.isLocked_MapLock()) {
                if (cellNode.isLocked()) {
                    g2.setColor(MapConfig.CELL_LOCKED_MARGIN_PATH_COLOR);
                    g2.draw(cellRectangle_Big);
                    g2.setColor(MapConfig.CELL_LOCKED_PATH_COLOR);
                    g2.fillRect(cellRectangle_Big.x + 1, cellRectangle_Big.y + 1, cellRectangle_Big.width - 1, cellRectangle_Big.height - 1);
                }
                g2.dispose();
                getMapWindow().updateMap();
                return;
            }
        }
        if (!getMapWindow().isViewCell()) {
            if (getMapWindow().isViewRobotInfo()) {
            } else {
            }
        }
    }
}
