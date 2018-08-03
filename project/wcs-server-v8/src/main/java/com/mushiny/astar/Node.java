package com.mushiny.astar;

import java.util.Map;

/**
 * Created by Tank.li on 2017/7/4.
 */
public class Node implements java.io.Serializable{

    private String nodeId;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    private Node parentNode;//用于回溯
    private int f;
    private int g;
    private int h;
    private int x;
    private int y;

    private Map<Node,Integer> nodes;//用于初始化和计算 key是node value是cost值

    public Node getParentNode() {
        return parentNode;
    }

    public void setParentNode(Node parentNode) {
        this.parentNode = parentNode;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Map<Node, Integer> getNodes() {
        return nodes;
    }

    public void setNodes(Map<Node, Integer> nodes) {
        this.nodes = nodes;
    }

    public void calcF() {
        this.f = this.g + this.h;
    }

    @Override
    public String toString() {
        return "Node{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
