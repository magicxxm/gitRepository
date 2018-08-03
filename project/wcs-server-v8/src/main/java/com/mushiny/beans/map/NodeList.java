package com.mushiny.beans.map;

import com.mushiny.beans.Address;
import com.mushiny.beans.enums.AddressType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Tank.li on 2017/9/11.
 */
public class NodeList {
    private final static Logger logger = LoggerFactory.getLogger(NodeList.class);
    private Integer x;//x轴包含的列表
    private List<Address> addressList;
    private boolean revert;

    private List<List<Long>> addrsList = new ArrayList<>();//输出该轴 多条存储区的路径

    public List<List<Long>> getAddrsList() {
        return addrsList;
    }

    public boolean isRevert() {
        return revert;
    }

    public void setRevert(boolean revert) {
        this.revert = revert;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public List<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }

    //正向反向排序
    public void sort() {
        if (revert) {
            Collections.sort(this.addressList, new NodeAddrComparator());
        } else {
            Collections.sort(this.addressList, new RevertNodeAddrComparator());
        }
    }

    private List<Long> sNodes = new ArrayList<>();

    public List<Long> getsNodes() {
        return sNodes;
    }

    public void export() {
        Address last = null;
        List<Long> list = null;
        for (int i = 0; i < addressList.size(); i++) {
            Address address = addressList.get(i);
            if (last == null || last.getType() != AddressType.STORAGE) {
                if (address.getType() == AddressType.STORAGE) {
                    list = new ArrayList<>();
                    addrsList.add(list);
                    list.add(Long.parseLong(address.getId()));
                }
            }else{
                if (address.getType() == AddressType.STORAGE) {
                    if (list != null) {
                        list.add(Long.parseLong(address.getId()));
                    }
                }
            }
            last = address;
        }

        logger.debug(this.getX()+"生成的路径有:"+addrsList.size());
        for (int i = 0; i < addrsList.size(); i++) {
            List<Long> aLong =  addrsList.get(i);
            StringBuilder sb = new StringBuilder("[");
            for (int j = 0; j < aLong.size(); j++) {
                Long addr =  aLong.get(j);
                if (j<aLong.size()-1) {
                    sb.append(addr).append(",");
                } else {
                    sb.append(addr);
                }
            }
            sb.append("]");
            logger.debug("路径:"+i+":"+sb);
        }

        //生成首尾
        for (int i = 0; i < addrsList.size(); i++) {
            List<Long> longs = addrsList.get(i);
            if(longs.size()>1){
                sNodes.add(longs.get(0));
                sNodes.add(longs.get(longs.size()-1));
            }else{
                sNodes.add(longs.get(0));
            }
        }
        if (sNodes.size() > 2) {
            //去掉首尾
            sNodes.remove(0);
            sNodes.remove(sNodes.size() - 1);
        }

        StringBuilder sb = new StringBuilder("[");
        for (int j = 0; j < sNodes.size(); j++) {
            Long addr = sNodes.get(j);
            if (j < sNodes.size() - 1) {
                sb.append(addr).append(",");
            } else {
                sb.append(addr);
            }
        }
        sb.append("]");

        logger.debug("sNodes:"+sb.toString());
    }

    private class NodeAddrComparator implements Comparator<Address> {
        @Override
        public int compare(Address o1, Address o2) {
            if (o1.getxPosition() - o2.getxPosition() > 0) return 1;
            if (o1.getxPosition() - o2.getxPosition() == 0) return 0;
            return -1;
        }
    }

    private class RevertNodeAddrComparator implements Comparator<Address> {
        @Override
        public int compare(Address o1, Address o2) {
            if (o1.getxPosition() - o2.getxPosition() > 0) return -1;
            if (o1.getxPosition() - o2.getxPosition() == 0) return 0;
            return 1;
        }
    }
}
