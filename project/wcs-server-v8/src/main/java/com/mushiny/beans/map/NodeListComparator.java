package com.mushiny.beans.map;

import java.util.Comparator;

/**
 * Created by Tank.li on 2017/9/11.
 */
public class NodeListComparator implements Comparator<NodeList>{
    @Override
    public int compare(NodeList o1, NodeList o2) {
        if (o1.getX() - o2.getX()>0) return 1;
        if (o1.getX() - o2.getX()==0) return 0;
        return -1;
    }
}
