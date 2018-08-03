package com.mushiny.business;

import com.mushiny.beans.Address;

import java.util.Comparator;

/**
 * Created by Tank.li on 2017/6/28.
 */
public class AddrScoreComparator implements Comparator<Address> {
    /*JDK7中的Collections.Sort方法实现中，
    如果两个值是相等的，那么compare方法需要返回0，
    否则 可能 会在排序时抛错，而JDK6是没有这个限制的*/
    @Override
    public int compare(Address o1, Address o2) {
        if (o1.getScore() - o2.getScore()>0) return 1;
        if (o1.getScore() - o2.getScore()==0) return 0;
        return -1;
    }
}
