/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.kiva.map;

import com.mushiny.rcs.server.KivaAGV;

/**
 *
 * @author aricochen
 */
public class LockCellNode {
    
     public synchronized boolean checkAndLocked_MapLock(KivaAGV agv) {
         
         return true;
     }
}
