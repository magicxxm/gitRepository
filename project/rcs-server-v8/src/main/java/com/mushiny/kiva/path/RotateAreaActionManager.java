/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.kiva.path;

import com.mushiny.kiva.map.CellNode;
import com.mushiny.rcs.kiva.bus.action.Rotate11Action;
import com.mushiny.rcs.kiva.bus.action.AGVActionContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;


/**
 * 旋转区的动作管理
 *
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class RotateAreaActionManager extends AGVActionContainer {

    private static Logger LOG = LoggerFactory.getLogger(RotateAreaActionManager.class.getName());
    private static RotateAreaActionManager instance;
    private RotateArea rotateArea;
    private RotateAreaManager rotateAreaManager;

    private RotateAreaActionManager() {
        super();
        rotateAreaManager = RotateAreaManager.getInstance();
    }

    private static synchronized void initInstance() {
        if (instance == null) {
            instance = new RotateAreaActionManager();
        }
    }

    public static RotateAreaActionManager getInstance() {
        if (instance == null) {
            initInstance();
        }
        return instance;
    }
/*
    public static RotateAreaActionManager getInstance() {
        if (instance == null) {
            instance = new RotateAreaActionManager();
        }
        return instance;
    }
*/


    public LinkedList<SeriesPath> modifyGetRotateAreaEnterSeriesPath(CellNode previousEnterCellNode, CellNode enterCellNode, CellNode exitCellNode, CellNode nextExitCellNode, int rotateTheta) {
        RotateArea raEnter = rotateAreaManager.getRotateAreaByCellNode(enterCellNode);
        RotateArea raExit = rotateAreaManager.getRotateAreaByCellNode(exitCellNode);
        assert raEnter != null;
        assert raExit != null;
        LinkedList<SeriesPath> rotateAreaSeriesPathList = new LinkedList();
//================================1.进入旋转区.旋转================================
        SeriesPath enterSeriesPath_A = new SeriesPath();
        SeriesPath enterSeriesPath_B = new SeriesPath();
        //1.1.左上角
        if (enterCellNode.equals(raEnter.getLeftUpCellNode())) {
            enterCellNode.setRobotAction(straightLine02Divide2Action);
            enterSeriesPath_A.addPathCell(enterCellNode);
            //1.1.1左方
            if (enterCellNode.isLeftNode(previousEnterCellNode)) {
                //R1_1-e
                CellNode R1_1 = rotateAreaManager.getR1();
                R1_1.setRobotAction(rotate10Action_180);
                enterSeriesPath_A.addPathCell(R1_1);
                //R1_1-b
                CellNode R1_2 = rotateAreaManager.getR1();
                R1_2.setRobotAction(rotate10Action_180);
                CellNode R1_3 = rotateAreaManager.getR1();
                R1_3.setRobotAction(straightLine02Divide2Action);
                enterSeriesPath_B.addPathCell(R1_2);
                enterSeriesPath_B.addPathCell(R1_3);
            }
            //1.1.2上方
            if (enterCellNode.isUpNode(previousEnterCellNode)) {
                //R4_1-e
                CellNode R4_1 = rotateAreaManager.getR4();
                R4_1.setRobotAction(rotate10Action_90);
                enterSeriesPath_A.addPathCell(R4_1);
                //R4_1-b
                CellNode R4_2 = rotateAreaManager.getR4();
                R4_2.setRobotAction(rotate10Action_90);
                CellNode R4_3 = rotateAreaManager.getR4();
                R4_3.setRobotAction(straightLine02Divide2Action);
                enterSeriesPath_B.addPathCell(R4_2);
                enterSeriesPath_B.addPathCell(R4_3);
            }

            //1.1.3旋转区旋转
            CellNode R = rotateAreaManager.getR();
            Rotate11Action rotate11ActionTheta = new Rotate11Action(rotateTheta);
            R.setRobotAction(rotate11ActionTheta);
            enterSeriesPath_B.addPathCell(R);
        }
        //1.2.右上角
        if (enterCellNode.equals(raEnter.getRightUpCellNode())) {
            enterCellNode.setRobotAction(straightLine02Divide2Action);
            enterSeriesPath_A.addPathCell(enterCellNode);
            //1.2.1右方
            if (enterCellNode.isRightNode(previousEnterCellNode)) {
                //R1_1-e
                CellNode R1_1 = rotateAreaManager.getR1();
                R1_1.setRobotAction(rotate10Action_180);
                enterSeriesPath_A.addPathCell(R1_1);
                //R1_1-b
                CellNode R1_2 = rotateAreaManager.getR1();
                R1_2.setRobotAction(rotate10Action_180);
                CellNode R1_3 = rotateAreaManager.getR1();
                R1_3.setRobotAction(straightLine02Divide2Action);
                enterSeriesPath_B.addPathCell(R1_2);
                enterSeriesPath_B.addPathCell(R1_3);
            }
            //1.2.2上方
            if (enterCellNode.isUpNode(previousEnterCellNode)) {
                //R2_1-e
                CellNode R2_1 = rotateAreaManager.getR2();
                R2_1.setRobotAction(rotate10Action_270);
                enterSeriesPath_A.addPathCell(R2_1);
                //R2_1-b
                CellNode R2_2 = rotateAreaManager.getR2();
                R2_2.setRobotAction(rotate10Action_270);
                CellNode R2_3 = rotateAreaManager.getR2();
                R2_3.setRobotAction(straightLine02Divide2Action);
                enterSeriesPath_B.addPathCell(R2_2);
                enterSeriesPath_B.addPathCell(R2_3);
            }

            //1.2.3旋转区旋转
            CellNode R = rotateAreaManager.getR();
            Rotate11Action rotate11ActionTheta = new Rotate11Action(rotateTheta);
            R.setRobotAction(rotate11ActionTheta);
            enterSeriesPath_B.addPathCell(R);
        }
        //1.3.右下角
        if (enterCellNode.equals(raEnter.getRightDownCellNode())) {
            enterCellNode.setRobotAction(straightLine02Divide2Action);
            enterSeriesPath_A.addPathCell(enterCellNode);
            //1.3.1右方
            if (enterCellNode.isRightNode(previousEnterCellNode)) {
                //R3_1-e
                CellNode R3_1 = rotateAreaManager.getR3();
                R3_1.setRobotAction(rotate10Action_0);
                enterSeriesPath_A.addPathCell(R3_1);
                //R3_1-b
                CellNode R3_2 = rotateAreaManager.getR3();
                R3_2.setRobotAction(rotate10Action_0);
                CellNode R3_3 = rotateAreaManager.getR3();
                R3_3.setRobotAction(straightLine02Divide2Action);
                enterSeriesPath_B.addPathCell(R3_2);
                enterSeriesPath_B.addPathCell(R3_3);
            }
            //1.3.2下方
            if (enterCellNode.isDownNode(previousEnterCellNode)) {
                //R2_1-e
                CellNode R2_1 = rotateAreaManager.getR2();
                R2_1.setRobotAction(rotate10Action_270);
                enterSeriesPath_A.addPathCell(R2_1);
                //R2_1-b
                CellNode R2_2 = rotateAreaManager.getR2();
                R2_2.setRobotAction(rotate10Action_270);
                CellNode R2_3 = rotateAreaManager.getR2();
                R2_3.setRobotAction(straightLine02Divide2Action);
                enterSeriesPath_B.addPathCell(R2_2);
                enterSeriesPath_B.addPathCell(R2_3);
            }
            //1.3.3旋转区旋转
            CellNode R = rotateAreaManager.getR();
            Rotate11Action rotate11ActionTheta = new Rotate11Action(rotateTheta);
            R.setRobotAction(rotate11ActionTheta);
            enterSeriesPath_B.addPathCell(R);
        }
        //1.4.左下角
        if (enterCellNode.equals(raEnter.getLeftDownCellNode())) {
            enterCellNode.setRobotAction(straightLine02Divide2Action);
            enterSeriesPath_A.addPathCell(enterCellNode);
            //1.4.1左方
            if (enterCellNode.isLeftNode(previousEnterCellNode)) {
                //R3_1-e
                CellNode R3_1 = rotateAreaManager.getR3();
                R3_1.setRobotAction(rotate10Action_0);
                enterSeriesPath_A.addPathCell(R3_1);
                //R3_1-b
                CellNode R3_2 = rotateAreaManager.getR3();
                R3_2.setRobotAction(rotate10Action_0);
                CellNode R3_3 = rotateAreaManager.getR3();
                R3_3.setRobotAction(straightLine02Divide2Action);
                enterSeriesPath_B.addPathCell(R3_2);
                enterSeriesPath_B.addPathCell(R3_3);
            }
            //1.4.2下方
            if (enterCellNode.isDownNode(previousEnterCellNode)) {
                //R4_1-e
                CellNode R4_1 = rotateAreaManager.getR4();
                R4_1.setRobotAction(rotate10Action_90);
                enterSeriesPath_A.addPathCell(R4_1);
                //R4_1-b
                CellNode R4_2 = rotateAreaManager.getR4();
                R4_2.setRobotAction(rotate10Action_90);
                CellNode R4_3 = rotateAreaManager.getR4();
                R4_3.setRobotAction(straightLine02Divide2Action);
                enterSeriesPath_B.addPathCell(R4_2);
                enterSeriesPath_B.addPathCell(R4_3);
            }

            //1.4.3旋转区旋转
            CellNode R = rotateAreaManager.getR();
            Rotate11Action rotate11ActionTheta = new Rotate11Action(rotateTheta);
            R.setRobotAction(rotate11ActionTheta);
            enterSeriesPath_B.addPathCell(R);
        }
//==========================2.走出旋转区.接入普通路径===========================
        SeriesPath exitSeriesPath_A = new SeriesPath();
        SeriesPath exitSeriesPath_B = new SeriesPath();
        //2.1.出口（左上角）
        if (exitCellNode.equals(raExit.getLeftUpCellNode())) {
            //2.1.1.左边
            if (exitCellNode.isLeftNode(nextExitCellNode)) {
                //R点动作
                CellNode R_1 = rotateAreaManager.getR();
                R_1.setRobotAction(rotate10Action_0);

                // mingchun.mu@mushiny.com -- 兼容新三角旋转区
                CellNode R_0 = rotateAreaManager.getR();
                R_0.setRobotAction(new Rotate11Action(rotateTheta));
                // mingchun.mu@mushiny.com ---------

                CellNode R_2 = rotateAreaManager.getR();
                R_2.setRobotAction(straightLine02Divide2Action);
                exitSeriesPath_A.addPathCell(R_1);

                // mingchun.mu@mushiny.com -- 兼容新三角旋转区
                exitSeriesPath_A.addPathCell(R_0);
                // mingchun.mu@mushiny.com ---------

                exitSeriesPath_A.addPathCell(R_2);
                //R1点-e
                CellNode R1_1 = rotateAreaManager.getR1();
                R1_1.setRobotAction(rotate10Action_270);
                exitSeriesPath_A.addPathCell(R1_1);
                //R1点-b
                CellNode R1_2 = rotateAreaManager.getR1();
                R1_2.setRobotAction(rotate10Action_270);
                CellNode R1_3 = rotateAreaManager.getR1();
                R1_3.setRobotAction(straightLine02Divide2Action);
                exitCellNode.setRobotAction(lineAction);
                exitSeriesPath_B.addPathCell(R1_2);
                exitSeriesPath_B.addPathCell(R1_3);
                exitSeriesPath_B.addPathCell(exitCellNode);
            }
            //2.1.2上边
            if (exitCellNode.isUpNode(nextExitCellNode)) {
                //R点动作
                CellNode R_1 = rotateAreaManager.getR();
                R_1.setRobotAction(rotate10Action_270);

                // mingchun.mu@mushiny.com -- 兼容新三角旋转区
                CellNode R_0 = rotateAreaManager.getR();
                R_0.setRobotAction(new Rotate11Action(rotateTheta));
                // mingchun.mu@mushiny.com ---------

                CellNode R_2 = rotateAreaManager.getR();
                R_2.setRobotAction(straightLine02Divide2Action);
                exitSeriesPath_A.addPathCell(R_1);

                // mingchun.mu@mushiny.com -- 兼容新三角旋转区
                exitSeriesPath_A.addPathCell(R_0);
                // mingchun.mu@mushiny.com ---------

                exitSeriesPath_A.addPathCell(R_2);

                //R4点-e
                CellNode R4_1 = rotateAreaManager.getR4();
                R4_1.setRobotAction(rotate10Action_0);
                exitSeriesPath_A.addPathCell(R4_1);
                //R4点-b
                CellNode R4_2 = rotateAreaManager.getR4();
                R4_2.setRobotAction(rotate10Action_0);
                CellNode R4_3 = rotateAreaManager.getR4();
                R4_3.setRobotAction(straightLine02Divide2Action);
                exitCellNode.setRobotAction(lineAction);
                exitSeriesPath_B.addPathCell(R4_2);
                exitSeriesPath_B.addPathCell(R4_3);
                exitSeriesPath_B.addPathCell(exitCellNode);
            }
        }
        //2.2.出口（右上）
        if (exitCellNode.equals(raExit.getRightUpCellNode())) {
            //2.2.1.右边
            if (exitCellNode.isRightNode(nextExitCellNode)) {
                //R点动作
                CellNode R_1 = rotateAreaManager.getR();
                R_1.setRobotAction(rotate10Action_0);

                // mingchun.mu@mushiny.com -- 兼容新三角旋转区
                CellNode R_0 = rotateAreaManager.getR();
                R_0.setRobotAction(new Rotate11Action(rotateTheta));
                // mingchun.mu@mushiny.com ---------

                CellNode R_2 = rotateAreaManager.getR();
                R_2.setRobotAction(straightLine02Divide2Action);
                exitSeriesPath_A.addPathCell(R_1);

                // mingchun.mu@mushiny.com -- 兼容新三角旋转区
                exitSeriesPath_A.addPathCell(R_0);
                // mingchun.mu@mushiny.com ---------

                exitSeriesPath_A.addPathCell(R_2);
                //R1点-e
                CellNode R1_1 = rotateAreaManager.getR1();
                R1_1.setRobotAction(rotate10Action_90);
                exitSeriesPath_A.addPathCell(R1_1);
                //R1点-b
                CellNode R1_2 = rotateAreaManager.getR1();
                R1_2.setRobotAction(rotate10Action_90);
                CellNode R1_3 = rotateAreaManager.getR1();
                R1_3.setRobotAction(straightLine02Divide2Action);
                exitCellNode.setRobotAction(lineAction);
                exitSeriesPath_B.addPathCell(R1_2);
                exitSeriesPath_B.addPathCell(R1_3);
                exitSeriesPath_B.addPathCell(exitCellNode);
            }
            //2.2.2上边
            if (exitCellNode.isUpNode(nextExitCellNode)) {
                //R点动作
                CellNode R_1 = rotateAreaManager.getR();
                R_1.setRobotAction(rotate10Action_90);

                // mingchun.mu@mushiny.com -- 兼容新三角旋转区
                CellNode R_0 = rotateAreaManager.getR();
                R_0.setRobotAction(new Rotate11Action(rotateTheta));
                // mingchun.mu@mushiny.com ---------

                CellNode R_2 = rotateAreaManager.getR();
                R_2.setRobotAction(straightLine02Divide2Action);
                exitSeriesPath_A.addPathCell(R_1);

                // mingchun.mu@mushiny.com -- 兼容新三角旋转区
                exitSeriesPath_A.addPathCell(R_0);
                // mingchun.mu@mushiny.com ---------

                exitSeriesPath_A.addPathCell(R_2);

                //R2点-e
                CellNode R2_1 = rotateAreaManager.getR2();
                R2_1.setRobotAction(rotate10Action_0);
                exitSeriesPath_A.addPathCell(R2_1);
                //R2点-b
                CellNode R2_2 = rotateAreaManager.getR2();
                R2_2.setRobotAction(rotate10Action_0);
                CellNode R2_3 = rotateAreaManager.getR2();
                R2_3.setRobotAction(straightLine02Divide2Action);
                exitCellNode.setRobotAction(lineAction);
                exitSeriesPath_B.addPathCell(R2_2);
                exitSeriesPath_B.addPathCell(R2_3);
                exitSeriesPath_B.addPathCell(exitCellNode);
            }
        }
        //2.3.出口（右下）
        if (exitCellNode.equals(raExit.getRightDownCellNode())) {
            //2.3.1.右边
            if (exitCellNode.isRightNode(nextExitCellNode)) {
                //R点动作
                CellNode R_1 = rotateAreaManager.getR();
                R_1.setRobotAction(rotate10Action_180);

                // mingchun.mu@mushiny.com -- 兼容新三角旋转区
                CellNode R_0 = rotateAreaManager.getR();
                R_0.setRobotAction(new Rotate11Action(rotateTheta));
                // mingchun.mu@mushiny.com ---------

                CellNode R_2 = rotateAreaManager.getR();
                R_2.setRobotAction(straightLine02Divide2Action);
                exitSeriesPath_A.addPathCell(R_1);

                // mingchun.mu@mushiny.com -- 兼容新三角旋转区
                exitSeriesPath_A.addPathCell(R_0);
                // mingchun.mu@mushiny.com ---------

                exitSeriesPath_A.addPathCell(R_2);
                //R3点-e
                CellNode R3_1 = rotateAreaManager.getR3();
                R3_1.setRobotAction(rotate10Action_90);
                exitSeriesPath_A.addPathCell(R3_1);
                //R3点-b
                CellNode R3_2 = rotateAreaManager.getR3();
                R3_2.setRobotAction(rotate10Action_90);
                CellNode R3_3 = rotateAreaManager.getR3();
                R3_3.setRobotAction(straightLine02Divide2Action);
                exitCellNode.setRobotAction(lineAction);
                exitSeriesPath_B.addPathCell(R3_2);
                exitSeriesPath_B.addPathCell(R3_3);
                exitSeriesPath_B.addPathCell(exitCellNode);
            }
            //2.3.2下边
            if (exitCellNode.isDownNode(nextExitCellNode)) {
                //R点动作
                CellNode R_1 = rotateAreaManager.getR();
                R_1.setRobotAction(rotate10Action_90);

                // mingchun.mu@mushiny.com -- 兼容新三角旋转区
                CellNode R_0 = rotateAreaManager.getR();
                R_0.setRobotAction(new Rotate11Action(rotateTheta));
                // mingchun.mu@mushiny.com ---------

                CellNode R_2 = rotateAreaManager.getR();
                R_2.setRobotAction(straightLine02Divide2Action);
                exitSeriesPath_A.addPathCell(R_1);

                // mingchun.mu@mushiny.com -- 兼容新三角旋转区
                exitSeriesPath_A.addPathCell(R_0);
                // mingchun.mu@mushiny.com ---------

                exitSeriesPath_A.addPathCell(R_2);

                //R2点-e
                CellNode R2_1 = rotateAreaManager.getR2();
                R2_1.setRobotAction(rotate10Action_180);
                exitSeriesPath_A.addPathCell(R2_1);
                //R2点-b
                CellNode R2_2 = rotateAreaManager.getR2();
                R2_2.setRobotAction(rotate10Action_180);
                CellNode R2_3 = rotateAreaManager.getR2();
                R2_3.setRobotAction(straightLine02Divide2Action);
                exitCellNode.setRobotAction(lineAction);
                exitSeriesPath_B.addPathCell(R2_2);
                exitSeriesPath_B.addPathCell(R2_3);
                exitSeriesPath_B.addPathCell(exitCellNode);
            }
        }
        //2.4.出口（左下）
        if (exitCellNode.equals(raExit.getLeftDownCellNode())) {
            //2.4.1.左边
            if (exitCellNode.isLeftNode(nextExitCellNode)) {
                //R点动作
                CellNode R_1 = rotateAreaManager.getR();
                R_1.setRobotAction(rotate10Action_180);

                // mingchun.mu@mushiny.com -- 兼容新三角旋转区
                CellNode R_0 = rotateAreaManager.getR();
                R_0.setRobotAction(new Rotate11Action(rotateTheta));
                // mingchun.mu@mushiny.com ---------

                CellNode R_2 = rotateAreaManager.getR();
                R_2.setRobotAction(straightLine02Divide2Action);
                exitSeriesPath_A.addPathCell(R_1);

                // mingchun.mu@mushiny.com -- 兼容新三角旋转区
                exitSeriesPath_A.addPathCell(R_0);
                // mingchun.mu@mushiny.com ---------

                exitSeriesPath_A.addPathCell(R_2);
                //R3点-e
                CellNode R3_1 = rotateAreaManager.getR3();
                R3_1.setRobotAction(rotate10Action_270);
                exitSeriesPath_A.addPathCell(R3_1);
                //R3点-b
                CellNode R3_2 = rotateAreaManager.getR3();
                R3_2.setRobotAction(rotate10Action_270);
                CellNode R3_3 = rotateAreaManager.getR3();
                R3_3.setRobotAction(straightLine02Divide2Action);
                exitCellNode.setRobotAction(lineAction);
                exitSeriesPath_B.addPathCell(R3_2);
                exitSeriesPath_B.addPathCell(R3_3);
                exitSeriesPath_B.addPathCell(exitCellNode);
            }
            //2.4.2下边
            if (exitCellNode.isDownNode(nextExitCellNode)) {
                //R点动作
                CellNode R_1 = rotateAreaManager.getR();
                R_1.setRobotAction(rotate10Action_270);

                // mingchun.mu@mushiny.com -- 兼容新三角旋转区
                CellNode R_0 = rotateAreaManager.getR();
                R_0.setRobotAction(new Rotate11Action(rotateTheta));
                // mingchun.mu@mushiny.com ---------

                CellNode R_2 = rotateAreaManager.getR();
                R_2.setRobotAction(straightLine02Divide2Action);
                exitSeriesPath_A.addPathCell(R_1);

                // mingchun.mu@mushiny.com -- 兼容新三角旋转区
                exitSeriesPath_A.addPathCell(R_0);
                // mingchun.mu@mushiny.com ---------

                exitSeriesPath_A.addPathCell(R_2);
                //R4点-e
                CellNode R4_1 = rotateAreaManager.getR4();
                R4_1.setRobotAction(rotate10Action_180);
                exitSeriesPath_A.addPathCell(R4_1);
                //R4点-b
                CellNode R4_2 = rotateAreaManager.getR4();
                R4_2.setRobotAction(rotate10Action_180);
                CellNode R4_3 = rotateAreaManager.getR4();
                R4_3.setRobotAction(straightLine02Divide2Action);
                exitCellNode.setRobotAction(lineAction);
                exitSeriesPath_B.addPathCell(R4_2);
                exitSeriesPath_B.addPathCell(R4_3);
                exitSeriesPath_B.addPathCell(exitCellNode);
            }
        }
        rotateAreaSeriesPathList.addLast(enterSeriesPath_A);
        rotateAreaSeriesPathList.addLast(enterSeriesPath_B);
        rotateAreaSeriesPathList.addLast(exitSeriesPath_A);
        rotateAreaSeriesPathList.addLast(exitSeriesPath_B);
        return rotateAreaSeriesPathList;
    }
    private boolean modifyFlag = true;
    public LinkedList<SeriesPath> getRotateAreaEnterSeriesPath(CellNode previousEnterCellNode, CellNode enterCellNode, CellNode exitCellNode, CellNode nextExitCellNode, int rotateTheta) {
        if(!modifyFlag){
            return getRotateAreaEnterSeriesPath_(previousEnterCellNode, enterCellNode, exitCellNode, nextExitCellNode, rotateTheta);
        }else {
            return modifyGetRotateAreaEnterSeriesPath(previousEnterCellNode, enterCellNode, exitCellNode, nextExitCellNode, rotateTheta);
        }
    }
    /*
     通过旋转区域入口点和旋转角度获取旋转区域内的路径串
    @param enterCellNode 入口点
    @param rotateTheta 旋转角度
    @return 返回旋转区域内的路径串，
     */
    public LinkedList<SeriesPath> getRotateAreaEnterSeriesPath_(CellNode previousEnterCellNode, CellNode enterCellNode, CellNode exitCellNode, CellNode nextExitCellNode, int rotateTheta) {
        RotateArea raEnter = rotateAreaManager.getRotateAreaByCellNode(enterCellNode);
        RotateArea raExit = rotateAreaManager.getRotateAreaByCellNode(exitCellNode);
        assert raEnter != null;
        assert raExit != null;
        LinkedList<SeriesPath> rotateAreaSeriesPathList = new LinkedList();
//================================1.进入旋转区.旋转================================
        SeriesPath enterSeriesPath_A = new SeriesPath();
        SeriesPath enterSeriesPath_B = new SeriesPath();
        //1.1.左上角
        if (enterCellNode.equals(raEnter.getLeftUpCellNode())) {
            enterCellNode.setRobotAction(straightLine02Divide2Action);
            enterSeriesPath_A.addPathCell(enterCellNode);
            //1.1.1左方
            if (enterCellNode.isLeftNode(previousEnterCellNode)) {
                //R1_1-e
                CellNode R1_1 = rotateAreaManager.getR1();
                R1_1.setRobotAction(rotate10Action_180);
                enterSeriesPath_A.addPathCell(R1_1);
                //R1_1-b
                CellNode R1_2 = rotateAreaManager.getR1();
                R1_2.setRobotAction(rotate10Action_180);
                CellNode R1_3 = rotateAreaManager.getR1();
                R1_3.setRobotAction(straightLine02Divide2Action);
                enterSeriesPath_B.addPathCell(R1_2);
                enterSeriesPath_B.addPathCell(R1_3);
            }
            //1.1.2上方
            if (enterCellNode.isUpNode(previousEnterCellNode)) {
                //R4_1-e
                CellNode R4_1 = rotateAreaManager.getR4();
                R4_1.setRobotAction(rotate10Action_90);
                enterSeriesPath_A.addPathCell(R4_1);
                //R4_1-b
                CellNode R4_2 = rotateAreaManager.getR4();
                R4_2.setRobotAction(rotate10Action_90);
                CellNode R4_3 = rotateAreaManager.getR4();
                R4_3.setRobotAction(straightLine02Divide2Action);
                enterSeriesPath_B.addPathCell(R4_2);
                enterSeriesPath_B.addPathCell(R4_3);
            }

            //1.1.3旋转区旋转
            CellNode R = rotateAreaManager.getR();
            Rotate11Action rotate11ActionTheta = new Rotate11Action(rotateTheta);
            R.setRobotAction(rotate11ActionTheta);
            enterSeriesPath_B.addPathCell(R);
        }
        //1.2.右上角
        if (enterCellNode.equals(raEnter.getRightUpCellNode())) {
            enterCellNode.setRobotAction(straightLine02Divide2Action);
            enterSeriesPath_A.addPathCell(enterCellNode);
            //1.2.1右方
            if (enterCellNode.isRightNode(previousEnterCellNode)) {
                //R1_1-e
                CellNode R1_1 = rotateAreaManager.getR1();
                R1_1.setRobotAction(rotate10Action_180);
                enterSeriesPath_A.addPathCell(R1_1);
                //R1_1-b
                CellNode R1_2 = rotateAreaManager.getR1();
                R1_2.setRobotAction(rotate10Action_180);
                CellNode R1_3 = rotateAreaManager.getR1();
                R1_3.setRobotAction(straightLine02Divide2Action);
                enterSeriesPath_B.addPathCell(R1_2);
                enterSeriesPath_B.addPathCell(R1_3);
            }
            //1.2.2上方
            if (enterCellNode.isUpNode(previousEnterCellNode)) {
                //R2_1-e
                CellNode R2_1 = rotateAreaManager.getR2();
                R2_1.setRobotAction(rotate10Action_270);
                enterSeriesPath_A.addPathCell(R2_1);
                //R2_1-b
                CellNode R2_2 = rotateAreaManager.getR2();
                R2_2.setRobotAction(rotate10Action_270);
                CellNode R2_3 = rotateAreaManager.getR2();
                R2_3.setRobotAction(straightLine02Divide2Action);
                enterSeriesPath_B.addPathCell(R2_2);
                enterSeriesPath_B.addPathCell(R2_3);
            }

            //1.2.3旋转区旋转
            CellNode R = rotateAreaManager.getR();
            Rotate11Action rotate11ActionTheta = new Rotate11Action(rotateTheta);
            R.setRobotAction(rotate11ActionTheta);
            enterSeriesPath_B.addPathCell(R);
        }
        //1.3.右下角
        if (enterCellNode.equals(raEnter.getRightDownCellNode())) {
            enterCellNode.setRobotAction(straightLine02Divide2Action);
            enterSeriesPath_A.addPathCell(enterCellNode);
            //1.3.1右方
            if (enterCellNode.isRightNode(previousEnterCellNode)) {
                //R3_1-e
                CellNode R3_1 = rotateAreaManager.getR3();
                R3_1.setRobotAction(rotate10Action_0);
                enterSeriesPath_A.addPathCell(R3_1);
                //R3_1-b
                CellNode R3_2 = rotateAreaManager.getR3();
                R3_2.setRobotAction(rotate10Action_0);
                CellNode R3_3 = rotateAreaManager.getR3();
                R3_3.setRobotAction(straightLine02Divide2Action);
                enterSeriesPath_B.addPathCell(R3_2);
                enterSeriesPath_B.addPathCell(R3_3);
            }
            //1.3.2下方
            if (enterCellNode.isDownNode(previousEnterCellNode)) {
                //R2_1-e
                CellNode R2_1 = rotateAreaManager.getR2();
                R2_1.setRobotAction(rotate10Action_270);
                enterSeriesPath_A.addPathCell(R2_1);
                //R2_1-b
                CellNode R2_2 = rotateAreaManager.getR2();
                R2_2.setRobotAction(rotate10Action_270);
                CellNode R2_3 = rotateAreaManager.getR2();
                R2_3.setRobotAction(straightLine02Divide2Action);
                enterSeriesPath_B.addPathCell(R2_2);
                enterSeriesPath_B.addPathCell(R2_3);
            }
            //1.3.3旋转区旋转
            CellNode R = rotateAreaManager.getR();
            Rotate11Action rotate11ActionTheta = new Rotate11Action(rotateTheta);
            R.setRobotAction(rotate11ActionTheta);
            enterSeriesPath_B.addPathCell(R);
        }
        //1.4.左下角
        if (enterCellNode.equals(raEnter.getLeftDownCellNode())) {
            enterCellNode.setRobotAction(straightLine02Divide2Action);
            enterSeriesPath_A.addPathCell(enterCellNode);
            //1.4.1左方
            if (enterCellNode.isLeftNode(previousEnterCellNode)) {
                //R3_1-e
                CellNode R3_1 = rotateAreaManager.getR3();
                R3_1.setRobotAction(rotate10Action_0);
                enterSeriesPath_A.addPathCell(R3_1);
                //R3_1-b
                CellNode R3_2 = rotateAreaManager.getR3();
                R3_2.setRobotAction(rotate10Action_0);
                CellNode R3_3 = rotateAreaManager.getR3();
                R3_3.setRobotAction(straightLine02Divide2Action);
                enterSeriesPath_B.addPathCell(R3_2);
                enterSeriesPath_B.addPathCell(R3_3);
            }
            //1.4.2下方
            if (enterCellNode.isDownNode(previousEnterCellNode)) {
                //R4_1-e
                CellNode R4_1 = rotateAreaManager.getR4();
                R4_1.setRobotAction(rotate10Action_90);
                enterSeriesPath_A.addPathCell(R4_1);
                //R4_1-b
                CellNode R4_2 = rotateAreaManager.getR4();
                R4_2.setRobotAction(rotate10Action_90);
                CellNode R4_3 = rotateAreaManager.getR4();
                R4_3.setRobotAction(straightLine02Divide2Action);
                enterSeriesPath_B.addPathCell(R4_2);
                enterSeriesPath_B.addPathCell(R4_3);
            }

            //1.4.3旋转区旋转
            CellNode R = rotateAreaManager.getR();
            Rotate11Action rotate11ActionTheta = new Rotate11Action(rotateTheta);
            R.setRobotAction(rotate11ActionTheta);
            enterSeriesPath_B.addPathCell(R);
        }
//==========================2.走出旋转区.接入普通路径===========================
        SeriesPath exitSeriesPath_A = new SeriesPath();
        SeriesPath exitSeriesPath_B = new SeriesPath();
        //2.1.出口（左上角）
        if (exitCellNode.equals(raExit.getLeftUpCellNode())) {
            //2.1.1.左边
            if (exitCellNode.isLeftNode(nextExitCellNode)) {
                //R点动作
                CellNode R_1 = rotateAreaManager.getR();
                R_1.setRobotAction(rotate10Action_0);
                CellNode R_2 = rotateAreaManager.getR();
                R_2.setRobotAction(straightLine02Divide2Action);
                exitSeriesPath_A.addPathCell(R_1);
                exitSeriesPath_A.addPathCell(R_2);
                //R1点-e
                CellNode R1_1 = rotateAreaManager.getR1();
                R1_1.setRobotAction(rotate10Action_270);
                exitSeriesPath_A.addPathCell(R1_1);
                //R1点-b
                CellNode R1_2 = rotateAreaManager.getR1();
                R1_2.setRobotAction(rotate10Action_270);
                CellNode R1_3 = rotateAreaManager.getR1();
                R1_3.setRobotAction(straightLine02Divide2Action);
                exitCellNode.setRobotAction(lineAction);
                exitSeriesPath_B.addPathCell(R1_2);
                exitSeriesPath_B.addPathCell(R1_3);
                exitSeriesPath_B.addPathCell(exitCellNode);
            }
            //2.1.2上边
            if (exitCellNode.isUpNode(nextExitCellNode)) {
                //R点动作
                CellNode R_1 = rotateAreaManager.getR();
                R_1.setRobotAction(rotate10Action_270);
                CellNode R_2 = rotateAreaManager.getR();
                R_2.setRobotAction(straightLine02Divide2Action);
                exitSeriesPath_A.addPathCell(R_1);
                exitSeriesPath_A.addPathCell(R_2);

                //R4点-e
                CellNode R4_1 = rotateAreaManager.getR4();
                R4_1.setRobotAction(rotate10Action_0);
                exitSeriesPath_A.addPathCell(R4_1);
                //R4点-b
                CellNode R4_2 = rotateAreaManager.getR4();
                R4_2.setRobotAction(rotate10Action_0);
                CellNode R4_3 = rotateAreaManager.getR4();
                R4_3.setRobotAction(straightLine02Divide2Action);
                exitCellNode.setRobotAction(lineAction);
                exitSeriesPath_B.addPathCell(R4_2);
                exitSeriesPath_B.addPathCell(R4_3);
                exitSeriesPath_B.addPathCell(exitCellNode);
            }
        }
        //2.2.出口（右上）
        if (exitCellNode.equals(raExit.getRightUpCellNode())) {
            //2.2.1.右边
            if (exitCellNode.isRightNode(nextExitCellNode)) {
                //R点动作
                CellNode R_1 = rotateAreaManager.getR();
                R_1.setRobotAction(rotate10Action_0);
                CellNode R_2 = rotateAreaManager.getR();
                R_2.setRobotAction(straightLine02Divide2Action);
                exitSeriesPath_A.addPathCell(R_1);
                exitSeriesPath_A.addPathCell(R_2);
                //R1点-e
                CellNode R1_1 = rotateAreaManager.getR1();
                R1_1.setRobotAction(rotate10Action_90);
                exitSeriesPath_A.addPathCell(R1_1);
                //R1点-b
                CellNode R1_2 = rotateAreaManager.getR1();
                R1_2.setRobotAction(rotate10Action_90);
                CellNode R1_3 = rotateAreaManager.getR1();
                R1_3.setRobotAction(straightLine02Divide2Action);
                exitCellNode.setRobotAction(lineAction);
                exitSeriesPath_B.addPathCell(R1_2);
                exitSeriesPath_B.addPathCell(R1_3);
                exitSeriesPath_B.addPathCell(exitCellNode);
            }
            //2.2.2上边
            if (exitCellNode.isUpNode(nextExitCellNode)) {
                //R点动作
                CellNode R_1 = rotateAreaManager.getR();
                R_1.setRobotAction(rotate10Action_90);
                CellNode R_2 = rotateAreaManager.getR();
                R_2.setRobotAction(straightLine02Divide2Action);
                exitSeriesPath_A.addPathCell(R_1);
                exitSeriesPath_A.addPathCell(R_2);

                //R2点-e
                CellNode R2_1 = rotateAreaManager.getR2();
                R2_1.setRobotAction(rotate10Action_0);
                exitSeriesPath_A.addPathCell(R2_1);
                //R2点-b
                CellNode R2_2 = rotateAreaManager.getR2();
                R2_2.setRobotAction(rotate10Action_0);
                CellNode R2_3 = rotateAreaManager.getR2();
                R2_3.setRobotAction(straightLine02Divide2Action);
                exitCellNode.setRobotAction(lineAction);
                exitSeriesPath_B.addPathCell(R2_2);
                exitSeriesPath_B.addPathCell(R2_3);
                exitSeriesPath_B.addPathCell(exitCellNode);
            }
        }
        //2.3.出口（右下）
        if (exitCellNode.equals(raExit.getRightDownCellNode())) {
            //2.3.1.右边
            if (exitCellNode.isRightNode(nextExitCellNode)) {
                //R点动作
                CellNode R_1 = rotateAreaManager.getR();
                R_1.setRobotAction(rotate10Action_180);
                CellNode R_2 = rotateAreaManager.getR();
                R_2.setRobotAction(straightLine02Divide2Action);
                exitSeriesPath_A.addPathCell(R_1);
                exitSeriesPath_A.addPathCell(R_2);
                //R3点-e
                CellNode R3_1 = rotateAreaManager.getR3();
                R3_1.setRobotAction(rotate10Action_90);
                exitSeriesPath_A.addPathCell(R3_1);
                //R3点-b
                CellNode R3_2 = rotateAreaManager.getR3();
                R3_2.setRobotAction(rotate10Action_90);
                CellNode R3_3 = rotateAreaManager.getR3();
                R3_3.setRobotAction(straightLine02Divide2Action);
                exitCellNode.setRobotAction(lineAction);
                exitSeriesPath_B.addPathCell(R3_2);
                exitSeriesPath_B.addPathCell(R3_3);
                exitSeriesPath_B.addPathCell(exitCellNode);
            }
            //2.3.2下边
            if (exitCellNode.isDownNode(nextExitCellNode)) {
                //R点动作
                CellNode R_1 = rotateAreaManager.getR();
                R_1.setRobotAction(rotate10Action_90);
                CellNode R_2 = rotateAreaManager.getR();
                R_2.setRobotAction(straightLine02Divide2Action);
                exitSeriesPath_A.addPathCell(R_1);
                exitSeriesPath_A.addPathCell(R_2);

                //R2点-e
                CellNode R2_1 = rotateAreaManager.getR2();
                R2_1.setRobotAction(rotate10Action_180);
                exitSeriesPath_A.addPathCell(R2_1);
                //R2点-b
                CellNode R2_2 = rotateAreaManager.getR2();
                R2_2.setRobotAction(rotate10Action_180);
                CellNode R2_3 = rotateAreaManager.getR2();
                R2_3.setRobotAction(straightLine02Divide2Action);
                exitCellNode.setRobotAction(lineAction);
                exitSeriesPath_B.addPathCell(R2_2);
                exitSeriesPath_B.addPathCell(R2_3);
                exitSeriesPath_B.addPathCell(exitCellNode);
            }
        }
        //2.4.出口（左下）
        if (exitCellNode.equals(raExit.getLeftDownCellNode())) {
            //2.4.1.左边
            if (exitCellNode.isLeftNode(nextExitCellNode)) {
                //R点动作
                CellNode R_1 = rotateAreaManager.getR();
                R_1.setRobotAction(rotate10Action_180);
                CellNode R_2 = rotateAreaManager.getR();
                R_2.setRobotAction(straightLine02Divide2Action);
                exitSeriesPath_A.addPathCell(R_1);
                exitSeriesPath_A.addPathCell(R_2);
                //R3点-e
                CellNode R3_1 = rotateAreaManager.getR3();
                R3_1.setRobotAction(rotate10Action_270);
                exitSeriesPath_A.addPathCell(R3_1);
                //R3点-b
                CellNode R3_2 = rotateAreaManager.getR3();
                R3_2.setRobotAction(rotate10Action_270);
                CellNode R3_3 = rotateAreaManager.getR3();
                R3_3.setRobotAction(straightLine02Divide2Action);
                exitCellNode.setRobotAction(lineAction);
                exitSeriesPath_B.addPathCell(R3_2);
                exitSeriesPath_B.addPathCell(R3_3);
                exitSeriesPath_B.addPathCell(exitCellNode);
            }
            //2.4.2下边
            if (exitCellNode.isDownNode(nextExitCellNode)) {
                //R点动作
                CellNode R_1 = rotateAreaManager.getR();
                R_1.setRobotAction(rotate10Action_270);
                CellNode R_2 = rotateAreaManager.getR();
                R_2.setRobotAction(straightLine02Divide2Action);
                exitSeriesPath_A.addPathCell(R_1);
                exitSeriesPath_A.addPathCell(R_2);
                //R4点-e
                CellNode R4_1 = rotateAreaManager.getR4();
                R4_1.setRobotAction(rotate10Action_180);
                exitSeriesPath_A.addPathCell(R4_1);
                //R4点-b
                CellNode R4_2 = rotateAreaManager.getR4();
                R4_2.setRobotAction(rotate10Action_180);
                CellNode R4_3 = rotateAreaManager.getR4();
                R4_3.setRobotAction(straightLine02Divide2Action);
                exitCellNode.setRobotAction(lineAction);
                exitSeriesPath_B.addPathCell(R4_2);
                exitSeriesPath_B.addPathCell(R4_3);
                exitSeriesPath_B.addPathCell(exitCellNode);
            }
        }
        rotateAreaSeriesPathList.addLast(enterSeriesPath_A);
        rotateAreaSeriesPathList.addLast(enterSeriesPath_B);
        rotateAreaSeriesPathList.addLast(exitSeriesPath_A);
        rotateAreaSeriesPathList.addLast(exitSeriesPath_B);
        return rotateAreaSeriesPathList;
    }
}
