package com.mushiny.wcs.application.business.path;

import com.mushiny.wcs.application.domain.MapNode;
import org.jgrapht.alg.interfaces.AStarAdmissibleHeuristic;

public class AdmissibleHeuristic<V> implements AStarAdmissibleHeuristic<V> {

    @Override
    public double getCostEstimate(V sourceVertex, V targetVertex) {
        return Math.abs(((MapNode) sourceVertex).getxPosition() - ((MapNode) targetVertex).getxPosition())
                + Math.abs(((MapNode) sourceVertex).getyPosition() - ((MapNode) targetVertex).getyPosition());
    }
}
