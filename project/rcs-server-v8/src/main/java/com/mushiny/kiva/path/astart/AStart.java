/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.kiva.path.astart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

/**
 * A START 算法
 *
 * @author aricochen
 */
public class AStart {
    private static Logger LOG = LoggerFactory.getLogger(AStart.class.getName());
    private SortLinkedList openList = new SortLinkedList();
    private LinkedList<Node> closeList = new LinkedList();

    public LinkedList findMinPath(Node start, Node targetNode) {
        Node currentNode, upNode, rightNode, downNode, leftNode;
        openList.add(start);
        long startTime = System.currentTimeMillis();
        LOG.info("开始计算最短路径...");
        while (!openList.isEmpty()) {
            currentNode = (Node) openList.removeFirst();
            if (currentNode.equals(targetNode)) {
                long endTime = System.currentTimeMillis();
                LOG.info("计算最短路径完成，耗时=" + (endTime - startTime) + "毫秒");
                LinkedList pathRSList = makePath(currentNode);
                openList.clear();
                closeList.clear();
                return pathRSList;
            } else {
                closeList.add(currentNode);
                upNode = currentNode.getUpNode();
                rightNode = currentNode.getRightNode();
                downNode = currentNode.getDownNode();
                leftNode = currentNode.getLeftNode();
                if (upNode != null) {
                    if ((!openList.contains(upNode) & (!closeList.contains(upNode)) & upNode.isWalkable())) {
                        upNode.setG(currentNode.getG() + currentNode.getUpConst());
                        upNode.calculateH(targetNode);
                        upNode.setParentNode(currentNode);
                        openList.add(upNode);
                    }
                }
                if (rightNode != null) {
                    if ((!openList.contains(rightNode) & (!closeList.contains(rightNode)) & rightNode.isWalkable())) {
                        rightNode.setG(currentNode.getG() + currentNode.getRightConst());
                        rightNode.calculateH(targetNode);
                        rightNode.setParentNode(currentNode);
                        openList.add(rightNode);
                    }
                }
                if (downNode != null) {
                    if ((!openList.contains(downNode) & (!closeList.contains(downNode)) & downNode.isWalkable())) {
                        downNode.setG(currentNode.getG() + currentNode.getDownConst());
                        downNode.calculateH(targetNode);
                        downNode.setParentNode(currentNode);
                        openList.add(downNode);
                    }
                }
                if (leftNode != null) {
                    if ((!openList.contains(leftNode) & (!closeList.contains(leftNode)) & leftNode.isWalkable())) {
                        leftNode.setG(currentNode.getG() + currentNode.getLeftConst());
                        leftNode.calculateH(targetNode);
                        leftNode.setParentNode(currentNode);
                        openList.add(leftNode);
                    }
                }
            }
        }
        openList.clear();
        closeList.clear();
        return null;
    }

    /*
     通过指定node,返回从开始节点到此node 的路径
     */
    public LinkedList makePath(Node node) {
        LinkedList linkedPath = new LinkedList();
        while (node.getParentNode() != null) {
            linkedPath.addFirst(node);
            node = node.getParentNode();
        }
        linkedPath.addFirst(node);

        return linkedPath;
    }

    /*
     此链表实现了在增加时就进行排序的功能，升序
     */
    private class SortLinkedList extends LinkedList {

        public void add(Node node) {
            for (int i = 0; i < size(); i++) {
                if (node.compareTo(get(i)) < 0) {
                    set(i, node);
                    return;
                }
            }
            addLast(node);
        }
    }

}
