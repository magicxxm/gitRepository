package com.mushiny.wcs.application.jgrapht;

import org.jgrapht.graph.DefaultWeightedEdge;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/12/29.
 */
public class MyDefaultEdge extends DefaultWeightedEdge {


    @Override
    public Object getSource()
    {
        return super.getSource();
    }

    /**
     * Retrieves the target of this edge. This is protected, for use by subclasses only (e.g. for
     * implementing toString).
     *
     * @return target of this edge
     */
    @Override
    public Object getTarget()
    {
        return super.getTarget();
    }

}
