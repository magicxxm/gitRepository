package com.mushiny.business;

import com.mushiny.beans.Address;

import java.util.Comparator;

/**
 * Created by Tank.li on 2017/6/26.
 */
public class AddressComparator implements Comparator<Address> {
    @Override
    public int compare(Address o1, Address o2) {
        //return o1.getHot() - o2.getHot();
        if (o1.getHot() - o2.getHot()>0) return 1;
        if (o1.getHot() - o2.getHot()==0) return 0;
        return -1;
    }
}
