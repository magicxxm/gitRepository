package com.mushiny.beans;

/**
 * Created by Tank.li on 2017/7/20.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 仓库对象 一个系统初始化可能包含多个仓库
 */
public class WareHouse implements java.io.Serializable {
    private String wareHouseId;
    private String wareHouseName;

    public Map<String,Section> sectionMap = new ConcurrentHashMap<>();


    public String getWareHouseId() {
        return wareHouseId;
    }

    public void setWareHouseId(String wareHouseId) {
        this.wareHouseId = wareHouseId;
    }

    public String getWareHouseName() {
        return wareHouseName;
    }

    public void setWareHouseName(String wareHouseName) {
        this.wareHouseName = wareHouseName;
    }
}
