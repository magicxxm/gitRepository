/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aricojf.platform.mina.common;

import java.beans.PropertyChangeSupport;

/**
 *  客户端
 *  @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class Client extends SimpleMachine{ 
   private final PropertyChangeSupport changeSupport;  

   
   public Client() {
       changeSupport = new PropertyChangeSupport(this);
   }
    
    public String toString() {     
      return "client:"+getIP()+":"+getPort();
    }
}
