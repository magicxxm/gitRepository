package com.mushiny.beans;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Tank.li on 2017/7/25.
 */
public abstract class BaseObject implements java.io.Serializable{

    private String activeThread;//activeThread

    public String getActiveThread() {
        return activeThread;
    }

    public void setActiveThread(String activeThread) {
        this.activeThread = activeThread;
    }

    private Map<String,Object> kv = new HashMap<>();

    private Map<String,Object> con;
    private Map<String,Object> delCon;//删除条件

    public Map<String, Object> getDelCon() {
        return delCon;
    }

    public void setDelCon(Map<String, Object> delCon) {
        this.delCon = delCon;
    }

    public Map<String, Object> getCon() {
        return con;
    }

    public void setCon(Map<String, Object> con) {
        this.con = con;
    }

    public Map getNewValue(){
        return kv;
    }

    public Map<String, Object> getKv() {
        return kv;
    }

    public void setKv(Map<String, Object> kv) {
        this.kv = kv;
    }

    /**
     * 做多次操作
     * @param key
     * @param value
     * @return
     */
    public BaseObject addKV(String key, Object value){
        kv.put(key,value);
        return this;
    }

    public void clearKV(){
        kv.clear();
    }

    public abstract Object getId();

    public abstract String getTable();
    public abstract String getIdName();
}
