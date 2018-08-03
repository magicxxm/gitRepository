package com.mushiny.kiva.map;

import com.aricojf.platform.mina.message.robot.DefaultAGVReceiveMessageProcessor;
import com.mingchun.mu.util.ExceptionUtil;
import com.mushiny.kiva.path.astart.Node;
import com.mushiny.rcs.global.MapConfig;
import com.mushiny.rcs.server.AGV;
import com.mushiny.rcs.server.KivaAGV;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import java.util.LinkedList;

import com.mushiny.rcs.listener.AGV2CellListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public abstract class Map extends DefaultAGVReceiveMessageProcessor {
    private static Logger LOG = LoggerFactory.getLogger(Map.class.getName());
    private int id;
    private String mapName;
    private int areaID;
    private int rotateAngle = 0;
    protected int mapRow; //地图行
    protected int mapCol; //地图列
    protected MapCell[][] mapNodes;
    protected ArrayList<CellNode> unWalkedCellNodeList = new ArrayList();
    protected AffineTransform transform;
    //protected ArrayList<CellNode> lockedCellList = new ArrayList();
    private final ArrayList<AGV2CellListener> robotMoveListenerList = new ArrayList();
   // private final ArrayList<MapCellLockedListener> mapLockedListenerList = new ArrayList();
  //  private final ArrayList<MapCellSeriesPathChangeListener> mapCellSeriesPathChangeListenerList = new ArrayList();
    public Map(int row, int col) {
        super();
        this.mapRow = row;
        this.mapCol = col;
        mapNodes = new MapCell[row][col];
    }

    /*
     初始化地图
     */
    public void initMap() {
        long addressCodeID = 1;
        for (int row = 0; row < getMapRow(); row++) {
            for (int col = 0; col < getMapCol(); col++) {
                mapNodes[row][col] = new MapCell(addressCodeID++);
                mapNodes[row][col].setPoint(new Point(col, row));
                mapNodes[row][col].setWalkable(true);
               //-- mapNodes[row][col].setUnLocked();
                mapNodes[row][col].setCellMarginColor(MapConfig.CELL_MARGIN_COMMON_COLOR.getRGB());
                mapNodes[row][col].setCellPreviousMarginColor(MapConfig.CELL_MARGIN_COMMON_COLOR.getRGB());
                mapNodes[row][col].setCellColor(MapConfig.CELL_COMMON_COLOR.getRGB());
                mapNodes[row][col].setCellPreviousColor(MapConfig.CELL_COMMON_COLOR.getRGB());
            }
        }
    }

    public void createNodeRelation() {
        //第1行没有upNode,最后1行没有downNode,第1列没有leftNode,最后1列没有rightNode
        for (int row = 0; row < getMapRow(); row++) {
            for (int col = 0; col < getMapCol(); col++) {
                Node upNode = null;
                Node rightNode = null;
                Node downNode = null;
                Node leftNode = null;
                //第一行，无upNode
                if (row != 0) {
                    upNode = mapNodes[row - 1][col];
                    mapNodes[row][col].setUpConst((short) 0);
                }
                //最后一列，无rightNode
                if (col != mapCol - 1) {
                    rightNode = mapNodes[row][col + 1];
                    mapNodes[row][col].setRightConst((short) 0);
                }
                //最后一行，无downNode
                if (row != mapRow - 1) {
                    downNode = mapNodes[row + 1][col];
                    mapNodes[row][col].setDownConst((short) 0);
                }
                //第一列，无leftNode
                if (col != 0) {
                    leftNode = mapNodes[row][col - 1];
                    mapNodes[row][col].setLeftConst((short) 0);
                }
                mapNodes[row][col].setUpNode(upNode);
                mapNodes[row][col].setRightNode(rightNode);
                mapNodes[row][col].setDownNode(downNode);
                mapNodes[row][col].setLeftNode(leftNode);
            }
        }
    }
    /*
     @param addressCodeID 指定的地址码
     */
    MapCell findTmpCell;

    public MapCell getMapCellByAddressCodeID(long addressCodeID) {
       if(addressCodeID > getMapRow()*getMapCol()) {
           return null;
       }
        int indexRow = (int) ((addressCodeID-1) / getMapCol());
        int indexCol = (int) ((addressCodeID-1) % getMapCol());
        findTmpCell = getMapCell(indexRow, indexCol);
        return findTmpCell;
    }
    
    public boolean isInMap(long addressCodeID) {
        if(addressCodeID <= 0 || addressCodeID  > getMaxAddressCodeID()) {
            return false;
        }
        return true;
    }

    public MapCell getMapCellByScreenPoint(Point p) {
        Point sourceP = new Point();
        try {
            if (transform != null) {
                transform.inverseTransform(p, sourceP);
            } else {
                sourceP = p;
            }
        } catch (NoninvertibleTransformException E) {
            System.out.println("坐标转换出错:\n" + ExceptionUtil.getMessage(E));
        }
        return getMapCell(sourceP);
    }

    /*
      获得包含指定点的CELL
      @param p原始坐标
     */
    public MapCell getMapCell(Point p) {
        MapCell cell;
        for (int row = 0; row < getMapRow(); row++) {
            for (int col = 0; col < getMapCol(); col++) {
                cell = getMapCell(row, col);
                if (cell.getCellViewRectangle() != null) {
                    if (cell.getCellViewRectangle().contains(p)) {
                        return cell;
                    }
                }
            }
        }
        return null;
    }

    public MapCell getMapCell(int row, int col) {
        if (row < 0 || row >= mapRow || col < 0 || col >= mapCol) {
            return null;
        }
        return mapNodes[row][col];
    }

    /**
     * @param mapRow the mapRow to set
     */
    public void setMapRow(short mapRow) {
        this.mapRow = mapRow;
    }

    /**
     * @param mapCol the mapCol to set
     */
    public void setMapCol(short mapCol) {
        this.mapCol = mapCol;
    }

    /*
     根据坐标得到一个点
     */
    public Node getNode(Point point) {
        if (point.x > mapRow || point.y > mapCol) {
            return null;
        }
        return getMapNodes()[point.x][point.y];
    }

    public Node getNode(int x, int y) {
        if (x > mapRow || y > mapCol) {
            return null;
        }
        return getMapNodes()[x][y];
    }

    /**
     * @return the mapNodes
     */
    public Node[][] getMapNodes() {
        return mapNodes;
    }

    /**
     * @param mapNodes the mapNodes to set
     */
    public void setMapNodes(MapCell[][] mapNodes) {
        this.mapNodes = mapNodes;
    }

    public int getMaxAddressCodeID() {
        return mapRow * mapCol;
    }

    public void setCellNodeUnWalked(CellNode unWaCellNode) {
        if (unWaCellNode != null) {
            unWaCellNode.setWalkable(false);
            unWalkedCellNodeList.add(unWaCellNode);
        }
    }

    public void setCellNodeCanWalked(CellNode cellNode) {
        if (cellNode != null) {
            unWalkedCellNodeList.remove(cellNode);
            cellNode.setWalkable(true);
        }
    }


   
    public void changeCellViewRectangleByFlipTheta(int x0, int y0, double theta) {
        AffineTransform transform = new AffineTransform();
        transform.translate(y0, x0);
        transform.rotate(theta);
        transform.translate(-x0, -y0);
        Rectangle r;
        for (int row = 0; row < getMapRow(); row++) {
            for (int col = 0; col < getMapCol(); col++) {
                r = transform.createTransformedShape(getMapCell(row, col).getCellViewRectangle()).getBounds();
                getMapCell(row, col).getCellViewRectangle().x = r.x;
                getMapCell(row, col).getCellViewRectangle().y = r.y;
            }
        }
    }


    /**
     * @return the mapRow
     */
    public int getMapRow() {
        return mapRow;
    }

    /**
     * @return the mapCol
     */
    public int getMapCol() {
        return mapCol;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the mapName
     */
    public String getMapName() {
        return mapName;
    }

    /**
     * @param mapName the mapName to set
     */
    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    /**
     * @return the areaID
     */
    public int getAreaID() {
        return areaID;
    }

    /**
     * @param areaID the areaID to set
     */
    public void setAreaID(int areaID) {
        this.areaID = areaID;
    }

    public void increaseRotateAngle(int angle) {
        this.rotateAngle += angle;
    }

    public void decreaseRotateAngle(int angle) {
        this.rotateAngle -= angle;
    }

    public  void regsiteAGV2CellListener(AGV2CellListener listener) {
        if (!robotMoveListenerList.contains(listener)) {
            robotMoveListenerList.add(listener);
        }
    }

    public  void removeAGV2CellListener(AGV2CellListener listener) {
        if (robotMoveListenerList.contains(listener)) {
            robotMoveListenerList.remove(listener);
        }
    }

    public  void fireOnAGVLeaveMapCell(KivaAGV agv,CellNode leaveCellNode) {
        for (AGV2CellListener listener : robotMoveListenerList) {
            listener.OnAGVLeaveMapCell(agv,leaveCellNode);
        }
    }

    public  void fireOnAGVEnterMapCell(KivaAGV agv,CellNode enterCell) {
        for (AGV2CellListener listener : robotMoveListenerList) {
            listener.OnAGVEnterMapCell(agv,enterCell);
        }
    }

    public abstract boolean installUnlockedCellTimeoutCellNodes(LinkedList<Long> longs);
}
