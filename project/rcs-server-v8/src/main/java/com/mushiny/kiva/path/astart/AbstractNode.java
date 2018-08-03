/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.kiva.path.astart;

import java.awt.Point;

/**
 *
 * @author aricochen
 */
public abstract class AbstractNode implements Node{
      //节点坐标
    private Point point;
    //此节点是否可通过
    private boolean walkable;
    //节点的父节点
    private Node parentNode;
    //上，右，下，左相邻点
    private Node upNode,rightNode,downNode,leftNode;
    //向上，右，下，左相邻坐标点移动的成本
    private short upConst,rightConst,downConst,leftConst;
    //
    private int G,H;

    /**
     * @return the point
     */
    public Point getPoint() {
        return point;
    }

    /**
     * @param point the point to set
     */
    public void setPoint(Point point) {
        this.point = point;
    }

    /**
     * @return the walkable
     */
    public boolean isWalkable() {
        return walkable;
    }

    /**
     * @param walkable the walkable to set
     */
    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }

    /**
     * @return the parentNode
     */
    public Node getParentNode() {
        return parentNode;
    }

    /**
     * @param parentNode the parentNode to set
     */
    public void setParentNode(Node parentNode) {
        this.parentNode = parentNode;
    }

    /**
     * @return the upNode
     */
    public Node getUpNode() {
        return upNode;
    }

    /**
     * @param upNode the upNode to set
     */
    public void setUpNode(Node upNode) {
        this.upNode = upNode;
    }

    /**
     * @return the rightNode
     */
    public Node getRightNode() {
        return rightNode;
    }

    /**
     * @param rightNode the rightNode to set
     */
    public void setRightNode(Node rightNode) {
        this.rightNode = rightNode;
    }

    /**
     * @return the downNode
     */
    public Node getDownNode() {
        return downNode;
    }

    /**
     * @param downNode the downNode to set
     */
    public void setDownNode(Node downNode) {
        this.downNode = downNode;
    }

    /**
     * @return the rightNode1
     */
    public Node getLeftNode() {
        return leftNode;
    }

    /**
     * @param leftNode
     */
    public void setLeftNode(Node leftNode) {
        this.leftNode = leftNode;
    }

    /**
     * @return the upConst
     */
    public short getUpConst() {
        return upConst;
    }

    /**
     * @param upConst the upConst to set
     */
    public void setUpConst(short upConst) {
        this.upConst = upConst;
    }

    /**
     * @return the rightConst
     */
    public short getRightConst() {
        return rightConst;
    }

    /**
     * @param rightConst the rightConst to set
     */
    public void setRightConst(short rightConst) {
        this.rightConst = rightConst;
    }

    /**
     * @return the downConst
     */
    public short getDownConst() {
        return downConst;
    }

    /**
     * @param downConst the downConst to set
     */
    public void setDownConst(short downConst) {
        this.downConst = downConst;
    }

    /**
     * @return the leftConst
     */
    public short getLeftConst() {
        return leftConst;
    }

    /**
     * @param leftConst the leftConst to set
     */
    public void setLeftConst(short leftConst) {
        this.leftConst = leftConst;
    }

    /**
     * @return the G
     */
    public int getG() {
        return G;
    }

    /**
     * @param G the G to set
     */
    public void setG(int G) {
        this.G = G;
    }

    /**
     * @return the H
     */
    public int getH() {
        return H;
    }
    
     public void setH(int h){
         this.H = h;
     }
      /**
     * H值带成本的估算方法
     * @param node 为目标节点，估算本节点到node节点H值
     */
    @Override
    public void calculateH(Node node) {
        int distance = Math.abs(getPoint().x - node.getPoint().x) + Math.abs(getPoint().y - node.getPoint().y);
        setH(distance);
    }


    
    
    public int getF() {
        return G+H;
    }
     public boolean equals(Node node) {
        return getPoint().x == node.getPoint().x
                && getPoint().y == node.getPoint().y;
    }

    /*
     比较本节点与指定节点大小
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
               /* System.out.println("需要确定优先级! m(x="+getPoint().x+",y="+getPoint().y+"),n(x="+n.getPoint().x+",y="+n.getPoint().y+")");
                //随机
                if (MapManager.getInstance().getPriority_xy() == MapManager.PRIORITY_XY) {
                    return 0;
                }
                //X优先
                if (MapManager.getInstance().getPriority_xy() == MapManager.PRIORITY_X) {
                    //System.out.println("x优先级");
                    if (getPoint().x < n.getPoint().x) {
                        return -1;
                    }  
                    if (getPoint().x > n.getPoint().x) {
                        return 1;
                    }
                    if (getPoint().x == n.getPoint().x) {
                        return 0;
                    }
                }
                 //Y优先
                if (MapManager.getInstance().getPriority_xy() == MapManager.PRIORITY_Y) {
                    //System.out.println("y优先级");
                    if (getPoint().y < n.getPoint().y) {
                        return -1;
                    }   
                    if (getPoint().y > n.getPoint().y) {
                        return 1;
                    } 
                     if (getPoint().y == n.getPoint().y) {
                        return 0;
                    } 
                }*/
               return 0;

            } else {
                return 1;
            }
        }
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("node:");
        builder.append("x=");
        builder.append(getPoint().x);
        builder.append(",y=");
        builder.append(getPoint().y);
        builder.append(",upNode=");
        builder.append(getUpNode() == null ? "0" : "1");
        builder.append(",rightNode=");
        builder.append(getRightNode() == null ? "0" : "1");
        builder.append(",downNode=");
        builder.append(getDownNode() == null ? "0" : "1");
        builder.append(",leftNode=");
        builder.append(getLeftNode() == null ? "0" : "1");
        return builder.toString();
    }

}
