package com.mushiny.astar;

import java.util.*;

/**
 * Created by Tank.li on 2017/7/4.
 */
public class AStar {
    //步长单位
    private static final int STEP = 1;

    /**
     * 当前运算用的地图
     */
    private Map<NodeKey,Node> currMap;

    public Map<NodeKey, Node> getCurrMap() {
        return currMap;
    }

    public void setCurrMap(Map<NodeKey, Node> currMap) {
        this.currMap = currMap;
    }

    private List<Node> closeList = new ArrayList<>();
    private List<Node> openList = new ArrayList<>();

    public Node findPath(Node startNode, Node endNode) {
        // 把起点增加 open list
        startNode = currMap.get(new NodeKey(startNode.getX(),startNode.getY()));
        endNode = currMap.get(new NodeKey(endNode.getX(),endNode.getY()));
        openList.add(startNode);
        while (openList.size() > 0) {
            // 遍历 open list 。查找 F值最小的节点，把它作为当前要处理的节点
            Node currentNode = findMinFNodeInOpneList();
            // 从open list中移除
            openList.remove(currentNode);
            // 把这个节点移到 close list
            closeList.add(currentNode);

            ArrayList<Node> neighborNodes = findNeighborNodes(currentNode);
            for (Node node : neighborNodes) {
                if (exists(openList, node)) {
                    foundPoint(currentNode, node);
                } else {
                    notFoundPoint(currentNode, endNode, node);
                }
            }
            if (find(openList, endNode) != null) {
                return find(openList, endNode);
            }
        }

        return find(openList, endNode);
    }



    public ArrayList<Node> findNeighborNodes(Node currentNode) {
        //ArrayList<Node> arrayList = new ArrayList<Node>();
        // 仅仅考虑上下左右，不考虑斜对角
        ArrayList<Node> list = new ArrayList<>();
        Iterator<Node> iter = currentNode.getNodes().keySet().iterator();
        while (iter.hasNext()) {
            Node next =  iter.next();
            if(!closeList.contains(next)){
                list.add(next);
            }
        }
        return list;
    }

    public Node findMinFNodeInOpneList() {
        Node tempNode = openList.get(0);
        for (Node node : openList) {
            if (node.getF() < tempNode.getF()) {
                tempNode = node;
            }
        }
        return tempNode;
    }

    private void foundPoint(Node tempStart, Node node) {
        int G = calcG(tempStart, node);
        if (G < node.getG()) {
            node.setParentNode(tempStart);
            node.setG(G);
            node.calcF();
        }
    }

    private void notFoundPoint(Node tempStart, Node end, Node node) {
        node.setParentNode(tempStart);
        node.setG(calcG(tempStart, node));
        node.setH(calcH(end, node)); ;
        node.calcF();
        openList.add(node);
    }

    private int calcG(Node start, Node node) {
        int G = STEP;
        int parentG = node.getParentNode() != null ? node.getParentNode().getG() : 1;
        return G + parentG;
    }

    private int calcH(Node end, Node node) {
        int step = Math.abs(node.getX() - end.getX()) + Math.abs(node.getY() - end.getY());
        return step * STEP;
    }

    public static Node find(List<Node> nodes, Node point) {
        for (Node n : nodes)
            if ((n.getX() == point.getX()) && (n.getY() == point.getY())) {
                return n;
            }
        return null;
    }

    public static boolean exists(List<Node> nodes, Node node) {
        for (Node n : nodes) {
            if ((n.getX() == node.getX()) && (n.getY() == node.getY())) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        Map<NodeKey, Node> map = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                NodeKey nodeKey = new NodeKey(i+1,j+1);
                Node node = new Node();
                node.setX(i+1);
                node.setY(j+1);
                node.setG(1);
                map.put(nodeKey,node);
            }
        }

        Iterator<Node> iter = map.values().iterator();


        while (iter.hasNext()) {
            Node curr = iter.next();
            Map<Node, Integer> nextNodes = new HashMap<>();
            Iterator<Node> iter2 = map.values().iterator();
            while (iter2.hasNext()) {
                Node node = iter2.next();
                if(isNext(node,curr)){
                    nextNodes.put(node,100);
                }
            }
            curr.setNodes(nextNodes);
        }


        AStar aStar = new AStar();
        aStar.setCurrMap(map);
        Node start = new Node();
        start.setY(3);
        start.setX(3);

        Node end = new Node();
        end.setY(98);
        end.setX(100);
        long startTime = System.currentTimeMillis();
        Node nodeEnd = aStar.findPath(start,end);
        System.out.println("===>"+(System.currentTimeMillis() - startTime));
        while (nodeEnd.getParentNode()!=null){
            System.out.println(nodeEnd.toString());
            nodeEnd = nodeEnd.getParentNode();
        }
    }

    private static boolean isNext(Node node, Node curr) {
        //只有四个 相邻的
        return (Math.abs(node.getX()-curr.getX())==1 && (node.getY()==curr.getY()))
                ||(Math.abs(node.getY()-curr.getY())==1 && (node.getX()==curr.getX()));
    }

}
