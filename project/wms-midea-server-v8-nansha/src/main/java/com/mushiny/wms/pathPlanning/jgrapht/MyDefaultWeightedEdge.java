package com.mushiny.wms.pathPlanning.jgrapht;

import com.mushiny.wms.application.domain.MapNode;
import org.jgrapht.graph.DefaultWeightedEdge;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/12/27.
 */
public class MyDefaultWeightedEdge extends DefaultWeightedEdge {
    @Override
    public String toString() {
        return ((MapNode)super.getSource()).getAddressCodeId() +":"+((MapNode)super.getTarget()).getAddressCodeId()+"---->"+super.getWeight();
    }
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
