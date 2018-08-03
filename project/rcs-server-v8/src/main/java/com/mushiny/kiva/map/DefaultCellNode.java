package com.mushiny.kiva.map;

import com.mushiny.kiva.path.RotateAreaManager;
import com.mushiny.kiva.path.astart.Node;
import java.awt.Point;
import java.awt.Rectangle;

/**
 *
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public abstract class DefaultCellNode implements CellNode {

    protected long addressCodeID;

    // 格子所拥有的货架码
    protected long podCodeID; //格子当前的货架码
    /*
    业务 -- 锁格超时，修改本身和未锁住的格子cost值，解锁时还原，获取到长路径时若有cost值修改的格子，需要重新请求路径
     */
    protected boolean isChangingCost; // 是否更改cost值

    //cell区域
    protected Rectangle rectangle; //原始数据
    protected Rectangle cellViewRectangle; //视图数据
    protected int cellColor;
    protected int cellMarginColor;
    protected int cellPreviousColor;
    protected int cellPreviousMarginColor;
    //节点坐标
    protected Point point;
    //节点的父节点
    protected Node parentNode;
    //上，右，下，左相邻点
    protected Node upNode;
    protected Node rightNode;
    protected Node downNode;
    protected Node leftNode;
    //向上，右，下，左相邻坐标点移动的成本
    protected short upConst;
    protected short rightConst;
    protected short downConst;
    protected short leftConst;
    protected int G;
    protected int H;
    //此节点是否可通过
    protected boolean walkable;
    //
    RotateAreaManager rotateAreaManager;
    //跟车直行标志
    private boolean followCellNode=false;

    private boolean unlockedTimeout; // 是否不用于锁格超时（工位上长时间等待的点）
    private boolean tempUnwalkable; // 是否临时不可走点（位置不改变点  锁格超时点）


    public DefaultCellNode() {
        rotateAreaManager = RotateAreaManager.getInstance();
       
    }
  //获得格子地址码ID
    @Override
    public long getAddressCodeID() {
        return addressCodeID;
    }

    //对格子赋予地址码ID
    @Override
    public void setAddressCode(long addressCodeID) {
        this.addressCodeID = addressCodeID;
    }
    @Override
    public void setRectangle(Rectangle R) {
        this.rectangle = R;
    }
    @Override
    public Rectangle getRectangle() {
        return rectangle;
    }
    public Point getPoint() {
        return point;
    }
    public void setPoint(Point point) {
        this.point = point;
    }
    public boolean isWalkable() {
        return walkable;
    }
    public Node getParentNode() {
        return parentNode;
    }
    public void setParentNode(Node parentNode) {
        this.parentNode = parentNode;
    }
    public Node getUpNode() {
        return upNode;
    }
    public void setUpNode(Node upNode) {
        this.upNode = upNode;
    }
    public Node getRightNode() {
        return rightNode;
    }
    public void setRightNode(Node rightNode) {
        this.rightNode = rightNode;
    }
    @Override
    public Node getDownNode() {
        return downNode;
    }
    public void setDownNode(Node downNode) {
        this.downNode = downNode;
    }
    public Node getLeftNode() {
        return leftNode;
    }
    public void setLeftNode(Node leftNode) {
        this.leftNode = leftNode;
    }
    public short getUpConst() {
        return upConst;
    }
    public void setUpConst(short upConst) {
        this.upConst = upConst;
    }
    public short getRightConst() {
        return rightConst;
    }
    public void setRightConst(short rightConst) {
        this.rightConst = rightConst;
    }
    public short getDownConst() {
        return downConst;
    }
    public void setDownConst(short downConst) {
        this.downConst = downConst;
    }
    public short getLeftConst() {
        return leftConst;
    }
    public void setLeftConst(short leftConst) {
        this.leftConst = leftConst;
    }
    public int getG() {
        return G;
    }
    public void setG(int G) {
        this.G = G;
    }
    public int getH() {
        return H;
    }

    public void setH(int h) {
        this.H = h;
    }

    @Override
    public boolean isUpNode(Node node) {
        return node.getPoint().x == getPoint().x && node.getPoint().y == getPoint().y - 1;
    }
    @Override
    public boolean isDownNode(Node node) {
        return node.getPoint().x == getPoint().x
                && node.getPoint().y == getPoint().y + 1;
    }

    @Override
    public boolean isRightNode(Node node) {
        return node.getPoint().x == getPoint().x + 1 && node.getPoint().y == getPoint().y;
    }

    @Override
    public boolean isLeftNode(Node node) {
        return node.getPoint().x == getPoint().x - 1 && node.getPoint().y == getPoint().y;
    }

    /*
    设定指定点为邻居（自动判断）,如果符合邻居特征则设定，且返回TRUE,否则false
    @return true成功，否则false
     */
    @Override
    public boolean setNeighhour(Node node) {
        if (isUpNode(node)) {
            setUpNode(node);
            return true;
        }
        if (isRightNode(node)) {
            setRightNode(node);
            return true;
        }
        if (isDownNode(node)) {
            setDownNode(node);
            return true;
        }
        if (isLeftNode(node)) {
            setLeftNode(node);
            return true;
        }
        return false;
    }

    /*
    测试指定的节点是否为本节点的邻居
     */
    @Override
    public boolean isNeighbour(Node node) {
        return upNode.equals(node) || rightNode.equals(node) || downNode.equals(node) || leftNode.equals(node);
    }

    /**
     * H值带成本的估算方法
     *
     * @param node 为目标节点，估算本节点到node节点H值
     */
    @Override
    public void calculateH(Node node) {
        int distance = Math.abs(getPoint().x - node.getPoint().x) + Math.abs(getPoint().y - node.getPoint().y);
        setH(distance);
    }

    public int getF() {
        return G + H;
    }

    /**
     * 坐标相等， 两个点相等
     * @param node
     * @return
     */
    public boolean equals(Node node) {
        if(node==null) {
            return false;
        }
        return getPoint().x == node.getPoint().x && getPoint().y == node.getPoint().y;
    }
    /**
     *  比较本节点与指定节点大小 -- a* 算法的f值比较
     * @param node
     * @return
     */
    @Override
    public int compareTo(Object node) {
        Node n = (Node) node;
        int a = getF();
        int b = n.getF();
        if (a < b) {
            return -1;
        } else {
            if (a == b) {
                return 0;
            } else {
                return 1;
            }
        }
    }

    /**
     * 返回此节点相邻的四个顶点
     * @return
     */
    @Override
    public Node[]  getSurroudNodes() {
        Node[] nodes = new Node[4];
        nodes[0] = getUpNode();
        nodes[1] = getRightNode();
        nodes[2] = getDownNode();
        nodes[3] = getLeftNode();
        return nodes;
    }
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("node(");
        builder.append(getAddressCodeID());
       //-- builder.append(getRobotAction());
        builder.append(")");
        return builder.toString();
    }
    public Rectangle getCellViewRectangle() {
        return cellViewRectangle;
    }
    public void setCellViewRectangle(Rectangle cellViewRectangle) {
        this.cellViewRectangle = cellViewRectangle;
    }
    public int getCellColor() {
        return cellColor;
    }
    public void setCellColor(int cellColor) {
        this.cellColor = cellColor;
    }
    public int getCellMarginColor() {
        return cellMarginColor;
    }
    public void setCellMarginColor(int cellMarginColor) {
        this.cellMarginColor = cellMarginColor;
    }
    @Override
    public int getCellPreviousColor() {
        return cellPreviousColor;
    }
    @Override
    public void setCellPreviousColor(int cellPreviousColor) {
        this.cellPreviousColor = cellPreviousColor;
    }
    @Override
    public int getCellPreviousMarginColor() {
        return cellPreviousMarginColor;
    }
    @Override
    public void setCellPreviousMarginColor(int cellPreviousMarginColor) {
        this.cellPreviousMarginColor = cellPreviousMarginColor;
    }
    public void setFollowCellNode(boolean isFollowCellNode){//设置为跟车点
         this.followCellNode = isFollowCellNode;
     }
    public boolean isFollowCellNode(){
       return this.followCellNode;
   }
    @Override
    public boolean isUnlockedTimeout() {
        return unlockedTimeout;
    }
    @Override
    public void setUnlockedTimeout(boolean unlockedTimeout) {
        this.unlockedTimeout = unlockedTimeout;
    }
    @Override
    public boolean isTempUnwalkable() {
        return tempUnwalkable;
    }
    @Override
    public void setTempUnwalkable(boolean tempUnwalkable) {
        this.tempUnwalkable = tempUnwalkable;
    }
    public long getPodCodeID() {
        return podCodeID;
    }
    public void setPodCodeID(long podCodeID) {
        this.podCodeID = podCodeID;
    }

    public boolean isChangingCost() {
        return isChangingCost;
    }

    public void setChangingCost(boolean changingCost) {
        isChangingCost = changingCost;
    }
}
