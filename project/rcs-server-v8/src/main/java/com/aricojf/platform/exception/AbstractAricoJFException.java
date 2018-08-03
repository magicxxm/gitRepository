/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aricojf.platform.exception;

/**
 *
 * @author aricochen
 */
public abstract  class AbstractAricoJFException implements AricoJFException{
    private Object dataSource;
    private int ID;
    private int typeID;
    private long time;
    public AbstractAricoJFException() {
        
    }
    public AbstractAricoJFException(int ID,int typeID,long time,Object dataSource) {
        this.ID = ID;
        this.typeID = typeID;
        this.time = time;
        this.dataSource = dataSource;
    }
}
