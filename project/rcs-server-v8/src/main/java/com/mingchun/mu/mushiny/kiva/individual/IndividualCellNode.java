package com.mingchun.mu.mushiny.kiva.individual;

import com.mushiny.kiva.map.CellNode;
import com.mushiny.rcs.server.KivaAGV;

/**
 *  向外支出的一个孤立的点的加锁与解锁
 *
 *  原则： 如果小车在孤立点处，则小车必须锁住入口点； 如果小车在孤立点处，不能解锁入口点。
 *
 */
public class IndividualCellNode {

    private CellNode enterNode;
    private CellNode individualNode;

    public IndividualCellNode(CellNode enterNode, CellNode individualNode) {
        this.enterNode = enterNode;
        this.individualNode = individualNode;
    }

    public CellNode getEnterNode() {
        return enterNode;
    }

    public void setEnterNode(CellNode enterNode) {
        this.enterNode = enterNode;
    }

    public CellNode getIndividualNode() {
        return individualNode;
    }

    public void setIndividualNode(CellNode individualNode) {
        this.individualNode = individualNode;
    }
}
