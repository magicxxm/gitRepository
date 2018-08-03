package com.mushiny.business;

import com.mushiny.beans.Pod;

import java.util.Comparator;

/**
 * Created by Tank.li on 2017/6/28.
 */
public class PodComparator implements Comparator<Pod> {

    @Override
    public int compare(Pod o1, Pod o2) {
        //return (o1.getHot()-o2.getHot()>0)? 1:-1;
        if (o1.getHot() - o2.getHot()>0) return 1;
        if (o1.getHot() - o2.getHot()==0) return 0;
        return -1;
    }
}
