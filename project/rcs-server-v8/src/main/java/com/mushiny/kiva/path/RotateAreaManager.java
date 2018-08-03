/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.kiva.path;

import com.mushiny.kiva.map.CellNode;
import com.mushiny.kiva.map.MapCellNode;
import com.mushiny.rcs.global.RotateAreaConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Point;
import java.util.LinkedList;

/**
 * 管理一组旋转区域
 *
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class RotateAreaManager {

    private static Logger LOG = LoggerFactory.getLogger(RotateAreaManager.class.getName());
    private static RotateAreaManager instane;
    private final static String ROTATE_AREA_LOCKER = "LOCKER";
    private final LinkedList<RotateArea> rotateAreaLinkedList = new LinkedList();

    private RotateAreaManager() {
    }

    private static synchronized void initInstance(){
        if(instane == null){
            instane = new RotateAreaManager();
        }
    }
    public static RotateAreaManager getInstance() {
        if (instane == null) {
            initInstance();
        }
        return instane;
    }
/*
    public static RotateAreaManager getInstance() {
        if (instane == null) {
            instane = new RotateAreaManager();
        }
        return instane;
    }
*/

    public CellNode getR1() {
        CellNode R1 = new MapCellNode(RotateAreaConfig.R1_ADDRESS_CODE_ID){
            public String toString() {
                return "R1";
            }
        };
        R1.setPoint(new Point(RotateAreaConfig.R1X, RotateAreaConfig.R1Y));
        return R1;
    }

    public CellNode getR2() {
        CellNode R2 = new MapCellNode(RotateAreaConfig.R2_ADDRESS_CODE_ID){
            public String toString() {
                return "R2";
            }
        };
        R2.setPoint(new Point(RotateAreaConfig.R2X, RotateAreaConfig.R2Y));
        return R2;
    }

    public CellNode getR3() {
        CellNode R3 = new MapCellNode(RotateAreaConfig.R3_ADDRESS_CODE_ID){
            public String toString() {
                return "R3";
            }
        };
        R3.setPoint(new Point(RotateAreaConfig.R3X, RotateAreaConfig.R3Y));
        return R3;
    }

    public CellNode getR4() {
        CellNode R4 = new MapCellNode(RotateAreaConfig.R4_ADDRESS_CODE_ID){
            public String toString() {
                return "R4";
            }
        };
        R4.setPoint(new Point(RotateAreaConfig.R4X, RotateAreaConfig.R4Y));
        return R4;
    }

    public CellNode getR() {
        CellNode R = new MapCellNode(RotateAreaConfig.R_ADDRESS_CODE_ID){
            public String toString() {
                return "R";
            }
        };
        R.setPoint(new Point(RotateAreaConfig.RX, RotateAreaConfig.RY));
        return R;
    }

    public CellNode getInventedTurnCellNode() {
        CellNode turnCellNode = new MapCellNode(RotateAreaConfig.TURN_ADDRESS_CODE_ID);
        turnCellNode.setPoint(new Point(RotateAreaConfig.TURN_X, RotateAreaConfig.TURN_Y));
        return turnCellNode;
    }

    /*
     增加旋转区域
     */
    public boolean addRotateArea(RotateArea ra) {
        if (!ra.isValidate()) {
            return false;
        }
        synchronized (this) {
            for (RotateArea tmpRotateArea : rotateAreaLinkedList) {
                if (ra.equals(tmpRotateArea)) {
                    LOG.error("####增加旋转区域错误，旋转区域重复");
                    return false;
                }
            }
            ra.getLeftUpCellNode().setInRotateArea(ra);
            ra.getRightUpCellNode().setInRotateArea(ra);
            ra.getRightDownCellNode().setInRotateArea(ra);
            ra.getLeftDownCellNode().setInRotateArea(ra);
            rotateAreaLinkedList.addLast(ra);
            return true;
        }
    }

    public boolean addRotateArea(long podID, CellNode leftUpCellNode, CellNode rightUpCellNode, CellNode rightDownCellNode, CellNode leftDownCellNode) {
        RotateArea ra = new RotateArea(podID, leftUpCellNode, rightUpCellNode, rightDownCellNode, leftDownCellNode);
        if (!ra.isValidate()) {
            return false;
        }
        synchronized (this) {
            for (RotateArea tmpRotateArea : rotateAreaLinkedList) {
                if (ra.equals(tmpRotateArea)) {
                    LOG.error("####增加旋转区域错误，旋转区域重复");
                    return false;
                }
            }
            ra.getLeftUpCellNode().setInRotateArea(ra);
            ra.getRightUpCellNode().setInRotateArea(ra);
            ra.getRightDownCellNode().setInRotateArea(ra);
            ra.getLeftDownCellNode().setInRotateArea(ra);
            rotateAreaLinkedList.addLast(ra);
            return true;
        }
    }

    /*
     删除旋转区
     */
    public void removeRotateArea(RotateArea ra) {
        rotateAreaLinkedList.remove(ra);
        ra.getLeftUpCellNode().setInRotateArea(null);
        ra.getRightUpCellNode().setInRotateArea(null);
        ra.getRightDownCellNode().setInRotateArea(null);
        ra.getLeftDownCellNode().setInRotateArea(null);
    }

    /*
     通过旋转区的四个点中的任何一个获取旋转区域
    @param cellNode
     */
    public RotateArea getRotateAreaByCellNode(CellNode cellNode) {
        synchronized (this) {
            for (RotateArea ra : rotateAreaLinkedList) {
                if (ra.isCellNodeInRotateArea(cellNode)) {
                    return ra;
                }
            }
            return null;
        }
    }
    /*
     判断一个点是否在旋转区
    */
    public boolean isInRotateArea(CellNode cellNode) {
        if(getRotateAreaByCellNode(cellNode)==null) {
            return false;
        }else {
            return true;
        }
    }

    /*
     通过旋转区的四个点中的任何一个获取旋转区域
    @param cellNode
     */
    public RotateArea getRotateAreaByCellNode(long addressCodeID) {
        synchronized (this) {
            for (RotateArea ra : rotateAreaLinkedList) {
                if (ra.isCellNodeInRotateAreaByAddressCodeID(addressCodeID)) {
                    return ra;
                }
            }
            return null;
        }
    }
    /*
     判断两个CELL是否在同一个旋转区
    */
    public boolean isInSameRotateArea(long a,long b) {
        RotateArea raA = getRotateAreaByCellNode(a);
        RotateArea raB = getRotateAreaByCellNode(b);
        if(raA == null || raB == null) {
            return false;
        }
        if(raA.equals(raB)) {
            return true;
        }else {
            return false;
        }
    }

    public boolean isInSameRotateArea(CellNode a,CellNode b) {
        RotateArea raA = getRotateAreaByCellNode(a);
        RotateArea raB = getRotateAreaByCellNode(b);
        if(raA == null || raB == null) {
            return false;
        }
        if(raA.equals(raB)) {
            return true;
        }else {
            return false;
        }
    }

}
