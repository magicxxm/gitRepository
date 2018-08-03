/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.kiva.path.astart;

import java.awt.Point;

/**
 * 节点
 *
 * @author aricochen
 */
public interface Node extends Comparable {

    public void setPoint(Point p);

    public Point getPoint();

    public void setWalkable(boolean walkable);

    public boolean isWalkable();

    public void setParentNode(Node parentNode);

    public Node getParentNode();
    public boolean equals(Node node);

    /*
    上，右，下，左 邻近节点，及其节点成本(节点成本是使出成本）
     */
    public void setUpNode(Node upNode);

    public Node getUpNode();

    public void setUpConst(short upConst);

    public short getUpConst();

    public void setRightNode(Node rightNode);

    public Node getRightNode();

    public void setRightConst(short rightConst);

    public short getRightConst();

    public void setDownNode(Node downNode);

    public Node getDownNode();

    public void setDownConst(short downConst);

    public short getDownConst();

    public void setLeftNode(Node leftNode);

    public Node getLeftNode();

   public void setLeftConst(short leftConst);

    public short getLeftConst();
    
    public boolean isNeighbour(Node node);//判断此节点与指定的节点是否为邻居

    public Node[] getSurroudNodes();//节点相邻的方格
    
    public boolean isUpNode(Node node);
    public boolean isRightNode(Node node);
    public boolean isDownNode(Node node);
    public boolean isLeftNode(Node node);
    public boolean setNeighhour(Node node);//设定指定点为邻居（自动判断）

    public int getG();

    public void setG(int g);

    public int getH();
    
    public void setH(int h);

    public void calculateH(Node node);
    

    public int getF();
    
    public String toString();
}
