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
public interface AricoJFException {
    public int getID();
    public int getTypeID();
    public void setTypeID();
    public long getTime();
    public Object getDataSource();
}
