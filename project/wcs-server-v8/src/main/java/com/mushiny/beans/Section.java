package com.mushiny.beans;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Tank.li on 2017/7/20.
 */
public class Section implements java.io.Serializable{

    private String activeMapId;

    public String getActiveMapId() {
        return activeMapId;
    }

    public void setActiveMapId(String activeMapId) {
        this.activeMapId = activeMapId;
    }

    private String section_id;

    private String section_name;

    private String wareHouse_id;

    private long rcs_sectionId;

    public Map<String,AddressGroup> addressGroupMap = new HashMap<>();

    /**
     * 当前section的调度单组 key是小车的robot_id
     */

    private List<Address> storageAddrs;

    public List<Address> getStorageAddrs() {
        return storageAddrs;
    }

    public void setStorageAddrs(List<Address> storageAddrs) {
        this.storageAddrs = storageAddrs;
    }

    //所有的格子列表
    public List<Address> addressList = new ArrayList<>();
    //所有格子索引
    public Map<String,Address> addressMap = new ConcurrentHashMap<>();
    //所有充电桩的地址 key是chargerId
    public Map<String,Charger> chargers = new ConcurrentHashMap<>();
    //所有工作站
    public Map<String,WorkStation> workStationMap = new ConcurrentHashMap<>();
    //自动移动扫描的位置
    public Map<Address,WorkStation> scanPoionts = new ConcurrentHashMap<>();
    //工位工作停止点
    public Map<Address, WorkStation> stopPoionts = new ConcurrentHashMap<>();
    //POD回程缓冲点
    public Map<Address, WorkStation> bufferPoionts = new ConcurrentHashMap<>();

    public long getRcs_sectionId() {
        return rcs_sectionId;
    }

    public void setRcs_sectionId(long rcs_sectionId) {
        this.rcs_sectionId = rcs_sectionId;
    }

    public String getSection_name() {
        return section_name;
    }

    public void setSection_name(String section_name) {
        this.section_name = section_name;
    }

    public String getSection_id() {
        return section_id;
    }

    public void setSection_id(String section_id) {
        this.section_id = section_id;
    }

    public String getWareHouse_id() {
        return wareHouse_id;
    }

    public void setWareHouse_id(String wareHouse_id) {
        this.wareHouse_id = wareHouse_id;
    }

    @Override
    public String toString() {
        return "Section{" +
                "section_id='" + section_id + '\'' +
                ", section_name='" + section_name + '\'' +
                '}';
    }
}
