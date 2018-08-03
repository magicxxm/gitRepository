package com.mushiny.beans.order;

import com.mushiny.beans.BaseObject;

/**
 * 用于异步删除的对象
 * Created by Tank.li on 2017/10/16.
 */
public class EnroutePod extends BaseObject{
    @Override
    public Object getId() {
        return this.uuid;
    }

    private String uuid;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    private String table;

    public void setTable(String table){
        this.table = table;
    }

    @Override
    public String getTable() {
        return this.table;
    }

    @Override
    public String getIdName() {
        return "ID";
    }
}
