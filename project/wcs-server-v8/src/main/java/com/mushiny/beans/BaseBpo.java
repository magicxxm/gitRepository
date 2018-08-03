package com.mushiny.beans;

import java.util.Objects;

/**
 * Created by Tank.li on 2017/10/17.
 */
public class BaseBpo extends BaseObject {


    //操作类型 TODO
    public static final int INSERT = 1;
    public static final int UPDATE = 2;
    public static final int DELETE = 3;

    private int operType = INSERT;

    public int getOperType() {
        return operType;
    }

    public void setOperType(int operType) {
        this.operType = operType;
    }

    private Object Id;

    private String table;

    private String idName;

    @Override
    public Object getId() {
        return Id;
    }

    public void setId(Object id) {
        Id = id;
    }

    @Override
    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    @Override
    public String getIdName() {
        return idName;
    }

    public void setIdName(String idName) {
        this.idName = idName;
    }
}
