package com.mingchun.mu.mushiny.kiva.pod;

import com.mushiny.kiva.map.CellNode;

/**
 * Created by Laptop-6 on 2017/10/17.
 */
public interface IPod {
    long getPodCodeID();
    void setPodCodeID(long podCodeID);
    CellNode getCellNode();
    void setCellNode(CellNode cellNode);
    int getPodWeight();
    void setPodWeight(int podWeight);
}
