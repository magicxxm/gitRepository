package com.mushiny.kiva.map;

import com.mingchun.mu.mushiny.kiva.path.*;
import com.mushiny.kiva.path.CellNodeSeriesPathRobotAction;
import com.mushiny.kiva.path.RotateArea;
import com.mushiny.kiva.path.SeriesPath;
import com.mushiny.rcs.kiva.bus.action.RobotAction;
import com.mushiny.rcs.listener.CellListener;
import com.mushiny.rcs.server.KivaAGV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class MapCellNode extends DefaultCellNode {

    private static Logger LOG = LoggerFactory.getLogger(MapCellNode.class.getName());
    private KivaAGV agv;//当前CELL所在的AGV
    private KivaAGV nowLockedAGV;//被锁定的AGV
    private RotateArea ra;
    private LinkedList<SeriesPath> cellNodeInSeriesPathList;
    //-- private PriorityBlockingQueue<SeriesPath> prepareRunSeriesPathList;
    private RobotAction robotAction;
    private boolean locked = false;
    private boolean selected = false;

    //标志是否为旋转区的入口
    private boolean rotateAreaEnter = false;
    private boolean rotateAreaExit = false;

    private ArrayList<CellListener> cellListenerList = new ArrayList();

    //此CELL在所有路径串中的动作记录
    private ArrayList<CellNodeSeriesPathRobotAction> robotActionList = new ArrayList();


   // 上下左右点的距离
    private int upDistance;
    private int downDistance;
    private int leftDistance;
    private int rightDistance;


    //申请枷锁的AGV历史记录
    // private List<KivaAGV> needLockAGVList = new CopyOnWriteArrayList();
    public MapCellNode(long addressCodeID) {
        super();
        this.addressCodeID = addressCodeID;
        cellNodeInSeriesPathList = new LinkedList();
    }

    public void setRotateAreaEnter() {
        this.rotateAreaEnter = true;
    }

    public boolean isRotateAreaEnter() {
        return rotateAreaEnter;
    }

    public void setRotateAreaExit() {
        rotateAreaExit = true;
    }

    public boolean isRotateAreaExit() {
        return rotateAreaExit;
    }

    /**
     * 路径节点转换为路径数据包
     * @param sp
     * @return
     */
    public byte[] toPathActionBytes(SeriesPath sp) {
        synchronized(robotActionList){
            LinkedList<RobotAction> robotActionList = getRobotActionList(sp);
            byte[] seriesPathBytes = new byte[robotActionList.size() * 8];
            int index = 0;
            for (RobotAction ra : robotActionList) {
                byte[] tmpBytes = new byte[8];
                tmpBytes[0] = (byte) ((getAddressCodeID()) & 0xff);
                tmpBytes[1] = (byte) ((getAddressCodeID() >> 8) & 0xff);
                tmpBytes[2] = (byte) ((getAddressCodeID() >> 16) & 0xff);
                tmpBytes[3] = (byte) ((getAddressCodeID() >> 24) & 0xff);
                byte[] robotActionBytes = ra.toBytes();
                System.arraycopy(robotActionBytes, 0, tmpBytes, 4, 4);
                System.arraycopy(tmpBytes, 0, seriesPathBytes, index * 8, 8);
                index++;
            }
            return seriesPathBytes;
        }
    }

    @Override
    public RobotAction getRobotAction() {
        return robotAction;
    }
    public void setRobotAction(RobotAction robotAction) {
        this.robotAction = robotAction;
    }
    public void setRobotAction(SeriesPath sp, RobotAction robotAction) {
        boolean exist = false;
        for (CellNodeSeriesPathRobotAction cellNodeSeriesPathRobotAction : robotActionList) {
            if (cellNodeSeriesPathRobotAction != null && cellNodeSeriesPathRobotAction.getSeriesPath() != null && cellNodeSeriesPathRobotAction.getSeriesPath().equals(sp)) {
                exist = true;
                cellNodeSeriesPathRobotAction.addRobotAction(robotAction);
                break;
            }
        }
        if (!exist) {
            CellNodeSeriesPathRobotAction cellNodeSeriesPathRobotAction = new CellNodeSeriesPathRobotAction(sp, robotAction);
            robotActionList.add(cellNodeSeriesPathRobotAction);
        }
    }

    public LinkedList<RobotAction> getRobotActionList(SeriesPath sp) {
        for (CellNodeSeriesPathRobotAction cellNodeSeriesPathRobotAction : robotActionList) {
            if (cellNodeSeriesPathRobotAction.getSeriesPath().equals(sp)) {
                return cellNodeSeriesPathRobotAction.getRobotActions();
            }
        }
        return null;
    }

    public void removeRobotAction(SeriesPath sp) {
        for (CellNodeSeriesPathRobotAction cellNodeSeriesPathRobotAction : robotActionList) {
            if (cellNodeSeriesPathRobotAction.getSeriesPath().equals(sp)) {
                robotActionList.remove(cellNodeSeriesPathRobotAction);
                return;
            }
        }
    }

    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
        if (walkable) {
            fireOnCellWalkable();
        } else {
            fireOnCellUnWalkable();
        }
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public boolean isLocked_MapLock() {
        synchronized (MapLock.LOCK) {
            return locked;
        }
    }

    private TriangleRotateAreaManager triangleRotateAreaManager = TriangleRotateAreaManager.getInstance();
    private TriangleRotateAreaNewManager triangleRotateAreaNewManager = TriangleRotateAreaNewManager.getInstance();
    /**
     * 解锁  有动作 的格子 -- 第二次解锁，防止丢失实时数据包
     * @param sp 移除解锁格子的动作
     * @return
     */
    public synchronized boolean setUnLocked_MapLock(SeriesPath sp) {
        //-- synchronized (MapLock.LOCK) {

        LOG.info("解锁格子("+getAddressCodeID()+")");

        RotateArea ra = rotateAreaManager.getRotateAreaByCellNode(this);
        TriangleRotateArea tra = triangleRotateAreaManager.getTriangleRotateAreaByCellNode(this);
        TriangleRotateAreaNew traNew = triangleRotateAreaNewManager.getTriangleRotateAreaNewByCellNode(this);

        if (ra != null) {
            if (isRotateAreaExit()) {
                removeRobotAction(sp);
                ra.setUnLocked();
                return true;
            } else {
                return false;
            }
        } else if(tra != null){
            if(tra.isExitCellNode(this)){
                removeRobotAction(sp);
                tra.unlockRotationArea();
                return true;
            }else {
                return false;
            }
        } else if(traNew != null){
            if(traNew.isRotationExitCellNode(this)){
                removeRobotAction(sp);
                traNew.unlockRotationArea();
                return true;
            }else {
                return false;
            }
        }else {
            removeRobotAction(sp);
            setUnLocked();
            return true;
        }
        //-- }
    }

    /**
     *
     * @param agv
     * @return
     */
    @Override
    //检查
    public synchronized boolean checkAndLocked_MapLock(KivaAGV agv) {

//        LOG.error("当前agv="+agv.getID()+"检查格子（"+this.getAddressCodeID()+"）, locked="+locked+"，锁住的nowLockedAGV="+nowLockedAGV);

        //-- synchronized (MapLock.LOCK) {

        if(agv != null){
            LOG.info("AGV("+agv.getID()+")锁格下发正在锁定格子("+getAddressCodeID()+")");
        }else {
            LOG.info("锁格下发正在锁定格子("+getAddressCodeID()+")");
        }
        if (locked) {
            if (agv.equals(nowLockedAGV)) {
                return true;
            } else {
                return false;
            }
        } else {
            RotateArea ra = rotateAreaManager.getRotateAreaByCellNode(this);
            TriangleRotateArea tra = triangleRotateAreaManager.getTriangleRotateAreaByCellNode(this);
            TriangleRotateAreaNew traNew = triangleRotateAreaNewManager.getTriangleRotateAreaNewByCellNode(this);

            if (ra != null) {
                ra.setLocked(agv);
            } else if(tra != null){
                tra.lockRotateArea(agv);
            } else if(traNew != null){
                traNew.lockRotateArea(agv);
            }else {
                setLocked(agv);
            }
            return true;
        }
        //--  }
    }

    @Override
    public boolean isLocked() {
        return locked;
    }

    
    @Override
    public void setLocked(KivaAGV agv) {
        if(agv != null){
            LOG.info("AGV("+agv.getID()+")锁定格子("+getAddressCodeID()+")！");
        }else {
            LOG.info("格子("+getAddressCodeID()+")被锁定！");
        }
        locked = true;
        nowLockedAGV = agv;
        fireOnCellLocked();
    }

    public void setUnLocked() {
        if(nowLockedAGV != null){
            LOG.info("AGV("+nowLockedAGV.getID()+")将格子("+getAddressCodeID()+")解锁！");
        }else {
            LOG.info("格子("+getAddressCodeID()+")解锁！");
        }
        locked = false;
        nowLockedAGV = null;
        setChangingCost(false);
        fireOnCellUnLocked();
    }

    @Override
    public synchronized boolean isAGVIn() {
        if(agv == null) {
            return false;
        }else {
            return true;
        }
    }

    @Override
    public synchronized void setAGVIn(KivaAGV agv) {
        this.agv = agv;
        if (nowLockedAGV != null) {
            if (!agv.equals(nowLockedAGV)) {
                LOG.error("==============AGV进入CELL...逻辑错误！！,nowAGV:" + agv.getID() + "preAGV:" + nowLockedAGV.getID());
            }
        } 
        setLocked(agv);
    }

    public synchronized void setAGVOut_(KivaAGV agv) {
        /*if(!this.agv.equals(agv)) {
            LOG.warn("AGV LEAVE CELL逻辑错误，agv1="+nowLockedAGV.getID()+",agv2="+agv.getID()); 
        }*/

        this.agv = null;


        // mingchun.mu@mushiny.com 如果实时包当前点不在三角旋转区， 但前一点在三角旋转区， 则将三角区解锁
        if(!triangleRotateAreaManager.isInTriangleRotateArea(agv.getCurrentCellNode())){
            if(triangleRotateAreaManager.getTriangleRotateAreaByCellNode(agv.getPreviousCellNode()) != null){
                triangleRotateAreaManager.getTriangleRotateAreaByCellNode(agv.getPreviousCellNode()).unlockRotationArea();
            }
        }else{
            // 当前点在三角区中则不用解锁
            return;
        }


        if (nowLockedAGV == null) {
            RotateArea ra = rotateAreaManager.getRotateAreaByCellNode(this);
            if (ra != null) {
                if (!isRotateAreaEnter()) {
                    setUnLocked();
                }
            } else {
                setUnLocked();
            }

        } else {
            if (agv.equals(nowLockedAGV)) {
                RotateArea ra = rotateAreaManager.getRotateAreaByCellNode(this);
                if (ra != null) {
                    if (!isRotateAreaEnter()) {
                        setUnLocked();
                    }
                } else {
                    setUnLocked();
                }
            }
        }
        //  this.notifyAll();
    }

    public synchronized void setAGVOut(KivaAGV agv) {
        this.agv = null;
        RotateArea ra = rotateAreaManager.getRotateAreaByCellNode(this);
        TriangleRotateArea tra = triangleRotateAreaManager.getTriangleRotateAreaByCellNode(this);
        TriangleRotateAreaNew traNew = triangleRotateAreaNewManager.getTriangleRotateAreaNewByCellNode(this);
        if (ra != null) {
            if (!isRotateAreaEnter()) {
               ra.setUnLocked();
            }
        }else if(tra != null){
            if(tra.isExitCellNode(this)){
                tra.unlockRotationArea();
            }
        }else if(traNew != null){
            if(traNew.isRotationExitCellNode(this)){
                traNew.unlockRotationArea();
            }
        } else {
            setUnLocked();
            if(agv != null){
                LOG.info("AGV("+agv.getID()+")解锁格子("+getAddressCodeID()+")！");
            }else {
                LOG.info("格子("+getAddressCodeID()+")被离开并解锁！");
            }
        }
    }

    @Override
    public KivaAGV getAGV() {
        return agv;
    }

    @Override
    public boolean isInGlobalPath() {
        if (cellNodeInSeriesPathList.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void setInGlobalPath(SeriesPath globalPath) {
        if (globalPath != null) {
            for (SeriesPath sp : cellNodeInSeriesPathList) {
                if (sp.equals(globalPath)) {
                    return;
                }
            }
            cellNodeInSeriesPathList.addLast(globalPath);
            fireOnCellInSeriesPath(globalPath);
        }
    }

    public void setNoInGlobalPath(SeriesPath globalPath) {
        for (SeriesPath sp : cellNodeInSeriesPathList) {
            if (sp.equals(globalPath)) {
                cellNodeInSeriesPathList.remove(sp);
                fireOnCellNoInSeriesPath(sp);
                return;
            }
        }
    }

    @Override
    public LinkedList<SeriesPath> getSeriesPath() {
        return cellNodeInSeriesPathList;
    }

    @Override
    public void setInRotateArea(RotateArea ra) {
        this.ra = ra;
        if (ra != null) {
            //-- changeSupport.firePropertyChange("InRotateArea", 0, 1);
        } else {
            //-- changeSupport.firePropertyChange("NotInRotateArea", 1, 0);
        }
        fireOnCellCommonUpdate();
    }

    public RotateArea isInRotateArea() {
        return ra;
    }

    //=========================================================================
    public void registeCellListener(CellListener listener) {
        if (listener != null) {
            if (!cellListenerList.contains(listener)) {
                cellListenerList.add(listener);
            }
        }
    }

    public void removeCellListener(CellListener listener) {
        if (listener != null) {
            cellListenerList.remove(listener);
        }
    }

    public void fireOnCellWalkable() {
        for (CellListener listener : cellListenerList) {
            listener.OnCellWalkable(this);
        }
    }

    public void fireOnCellUnWalkable() {
        for (CellListener listener : cellListenerList) {
            listener.OnCellUnWalkable(this);
        }
    }

    public void fireOnCellUnLocked() {
        for (CellListener listener : cellListenerList) {
            listener.OnCellUnLocked(this);
        }
    }

    public void fireOnCellLocked() {

        for (CellListener listener : cellListenerList) {
            listener.OnCellLocked(this);
        }
    }

    public void fireOnCellInSeriesPath(SeriesPath globalSeriesPath) {
        for (CellListener listener : cellListenerList) {
            listener.OnCellInSeriesPath(this, globalSeriesPath);
        }
    }

    public void fireOnCellNoInSeriesPath(SeriesPath globalSeriesPath) {
        for (CellListener listener : cellListenerList) {
            listener.OnCellNoInSeriesPath(this, globalSeriesPath);
        }
    }

    public void fireOnCellCommonUpdate() {
        for (CellListener listener : cellListenerList) {
            listener.OnCellCommonUpdate(this);
        }
    }
    public KivaAGV getNowLockedAGV() {
        return nowLockedAGV;
    }

    @Override
    public int getUpDistance() {
        return upDistance;
    }

    @Override
    public void setUpDistance(int upDistance) {
        this.upDistance = upDistance;
    }

    @Override
    public int getDownDistance() {
        return downDistance;
    }

    @Override
    public void setDownDistance(int downDistance) {
        this.downDistance = downDistance;
    }

    @Override
    public int getLeftDistance() {
        return leftDistance;
    }

    @Override
    public void setLeftDistance(int leftDistance) {
        this.leftDistance = leftDistance;
    }

    @Override
    public int getRightDistance() {
        return rightDistance;
    }

    @Override
    public void setRightDistance(int rightDistance) {
        this.rightDistance = rightDistance;
    }


}
