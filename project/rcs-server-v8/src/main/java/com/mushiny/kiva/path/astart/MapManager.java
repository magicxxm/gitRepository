/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.kiva.path.astart;

/**
 *
 * @author aricochen
 */
public class MapManager {
    public static MapManager instance;
    private Map map;

    public MapManager() {
        instance = this;
    }
    public static MapManager getInstance() {
        if(instance==null) {
            instance = new MapManager();
        }
        return instance;
    }

    /**
     * @return the map
     */
    public Map getMap() {
        return map;
    }

    /**
     * @param map the map to set
     */
    public void setMap(Map map) {
        this.map = map;
    }
   
    
    
}
