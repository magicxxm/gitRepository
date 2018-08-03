package com.mushiny.beans;

import com.mushiny.beans.enums.AddressStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tank.li on 2017/8/17.
 */
public class AddressGroup implements java.io.Serializable{
    private String groupId;
    //只定义最内和最外两个节点 ，两个节点间可能存在无数个中间节点
    private Address innerAddr;
    private Address outterAddr;

    public String getGroupId() {
        return groupId;
    }

    public AddressGroup(String groupId) {
        this.groupId = groupId;
    }
    //从外到内的列表
    public List<Address> getGroupAddrs(){
        List<Address> list = new ArrayList<>();
        Address node = this.getOutterAddr();
        while (node!=null){
            list.add(node);
            node=node.getGroupInnerAddr();
        }
        return list;
    }

    /**
     * 这组成员从该节点往外的所有节点
     * @param address
     * @return
     */
    public List<Address> getOutterAddrs(Address address){
        List<Address> list = new ArrayList<>();
        Address node = address.getGroupOutterAddr();
        while (node!=null){
            list.add(node);
            node=node.getGroupOutterAddr();
        }
        return list;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Address getInnerAddr() {
        return innerAddr;
    }

    public void setInnerAddr(Address innerAddr) {
        this.innerAddr = innerAddr;
    }

    public Address getOutterAddr() {
        return outterAddr;
    }

    public void setOutterAddr(Address outterAddr) {
        this.outterAddr = outterAddr;
    }

}
