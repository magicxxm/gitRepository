/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.kiva.path.astart;

import java.util.LinkedList;

/**
 *
 * @author aricochen
 */
public interface Map {
    public short [][] getMap();
    public LinkedList<Node> toMapNodes();
}
