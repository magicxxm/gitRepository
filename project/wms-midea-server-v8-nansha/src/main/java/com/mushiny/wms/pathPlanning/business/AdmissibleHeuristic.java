package com.mushiny.wms.pathPlanning.business;

import com.mushiny.wms.application.domain.MapNode;
import org.jgrapht.alg.interfaces.AStarAdmissibleHeuristic;

public class AdmissibleHeuristic<V> implements AStarAdmissibleHeuristic<V> {

    @Override
    public double getCostEstimate(V sourceVertex, V targetVertex) {
        return Math.abs(((MapNode) sourceVertex).getxPosition() - ((MapNode) targetVertex).getxPosition())
                + Math.abs(((MapNode) sourceVertex).getyPosition() - ((MapNode) targetVertex).getyPosition());
    }
}
