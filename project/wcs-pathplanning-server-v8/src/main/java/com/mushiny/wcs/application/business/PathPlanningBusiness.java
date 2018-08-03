package com.mushiny.wcs.application.business;

import com.mushiny.wcs.application.business.path.AdmissibleHeuristic;
import com.mushiny.wcs.application.domain.Map;
import com.mushiny.wcs.application.domain.MapNeighbor;
import com.mushiny.wcs.application.domain.MapNode;
import com.mushiny.wcs.application.respository.MapNeighborRepository;
import com.mushiny.wcs.application.respository.MapNodeRepository;
import com.mushiny.wcs.application.respository.MapRepository;
import com.mushiny.wcs.application.utils.MapNodeUtils;
import com.mushiny.wcs.common.exception.ApiException;
import com.mushiny.wcs.common.exception.ExceptionEnum;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.AStarShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedPseudograph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PathPlanningBusiness {

    private final MapRepository mapRepository;
    private final MapNodeRepository mapNodeRepository;
    private final MapNeighborRepository mapNeighborRepository;

    @Autowired
    public MapNodeUtils mapNodeUtils;

    @Autowired
    public PathPlanningBusiness(MapNeighborRepository mapNeighborRepository,
                                MapRepository mapRepository,
                                MapNodeRepository mapNodeRepository) {
        this.mapNeighborRepository = mapNeighborRepository;
        this.mapRepository = mapRepository;
        this.mapNodeRepository = mapNodeRepository;

    }

    /**
     * @param warehouseId  仓库ID
     * @param sectionId    SECTION的ID
     * @param sourceVertex 起始点
     * @param targetVertex 结束点
     * @return 路径点顺序列表
     */
    public List<Integer> getEmptyDrivePath(String warehouseId, String sectionId,
                                           int sourceVertex, int targetVertex) {
        // 获取地图上可以使用的点和边

        return mapNodeUtils.getDrivePathBySection(sectionId, sourceVertex, targetVertex, 0);
    }

    /**
     * @param warehouseId  仓库ID
     * @param sectionId    SECTION的ID
     * @param sourceVertex 起始点
     * @param targetVertex 结束点
     * @return 路径点顺序列表
     */
    public List<Integer> getHeavyDrivePath(String warehouseId, String sectionId,
                                           int sourceVertex, int targetVertex) {
        return mapNodeUtils.getDrivePathBySection(sectionId, sourceVertex, targetVertex, 1);
    }

    /**
     * @param warehouseId  仓库ID
     * @param sectionId    SECTION的ID
     * @param sourceVertex 起始点
     * @param targetVertex 结束点
     * @return 路径点顺序列表
     */
    public List<Integer> getEmptyDrivePath2(String warehouseId, String sectionId,
                                            int sourceVertex, int targetVertex) {
        // 获取地图上可以使用的点和边
        Map map = mapRepository.getBySectionIdAndWarehouseId(sectionId, warehouseId);
        if (map == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        List<MapNode> mapNodes = mapNodeRepository.getEmptyNode(map.getId());
        List<MapNeighbor> mapNeighbors = mapNeighborRepository.getByMapId(map.getId());
        if (mapNodes.isEmpty() || mapNeighbors.isEmpty()) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        // 组建空车地图
        Graph<MapNode, DefaultWeightedEdge> graph = new DirectedWeightedPseudograph<>(DefaultWeightedEdge.class);

        for (MapNode mapNode : mapNodes) {
            graph.addVertex(mapNode);
        }
        for (MapNeighbor mapNeighbor : mapNeighbors) {
            Integer newCost = mapNeighbor.getNewCost();
            int cost = mapNeighbor.getCost();
            if (newCost != null && newCost >= 0 && cost != -1) {
                Graphs.addEdge(graph, mapNeighbor.getInNode(), mapNeighbor.getOutNode(), newCost);
            } else {
                cost = mapNeighbor.getCost();
                if (cost >= 0) {
                    Graphs.addEdge(graph, mapNeighbor.getInNode(), mapNeighbor.getOutNode(), cost);

                }
            }
        }
        // 计算最短路径
        MapNode sourceNode = mapNodeRepository.getByMapIdAndAddressCodeId(map.getId(), sourceVertex);
        MapNode targetNode = mapNodeRepository.getByMapIdAndAddressCodeId(map.getId(), targetVertex);
        AStarShortestPath<MapNode, DefaultWeightedEdge> starShortestPath =
                new AStarShortestPath<>(graph, new AdmissibleHeuristic<>());
        GraphPath<MapNode, DefaultWeightedEdge> pathGraph = starShortestPath.getPath(sourceNode, targetNode);
        if (pathGraph == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        return pathGraph.getVertexList().stream().map(MapNode::getAddressCodeId).collect(Collectors.toList());
    }


    /**
     * @param warehouseId  仓库ID
     * @param sectionId    SECTION的ID
     * @param sourceVertex 起始点
     * @param targetVertex 结束点
     * @return 路径点顺序列表
     */
    public List<Integer> getHeavyDrivePath2(String warehouseId, String sectionId,
                                            int sourceVertex, int targetVertex) {
        // 获取地图上可以使用的点和边
        Map map = mapRepository.getBySectionIdAndWarehouseId(sectionId, warehouseId);
        if (map == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        List<MapNode> mapNodes = mapNodeRepository.getHeavyNode(map.getId());
        List<MapNeighbor> mapNeighbors = mapNeighborRepository.getByMapId(map.getId());
        if (mapNodes.isEmpty() || mapNeighbors.isEmpty()) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        // 组建重车地图
        Graph<MapNode, DefaultWeightedEdge> graph = new DirectedWeightedPseudograph<>(DefaultWeightedEdge.class);
        for (MapNode mapNode : mapNodes) {
            graph.addVertex(mapNode);
        }
        for (MapNeighbor mapNeighbor : mapNeighbors) {
            int cost = mapNeighbor.getCarryingCost();
            Integer newCost = mapNeighbor.getNewCost();
            if (newCost != null && newCost >= 0 && cost != -1) {
                Graphs.addEdge(graph, mapNeighbor.getInNode(), mapNeighbor.getOutNode(), newCost);
            } else {
                cost = mapNeighbor.getCarryingCost();
                if (cost >= 0) {
                    Graphs.addEdge(graph, mapNeighbor.getInNode(), mapNeighbor.getOutNode(), cost);
                }
            }
        }
        // 计算最短路径
        MapNode sourceNode = mapNodeRepository.getByMapIdAndAddressCodeId(map.getId(), sourceVertex);
        MapNode targetNode = mapNodeRepository.getByMapIdAndAddressCodeId(map.getId(), targetVertex);
        AStarShortestPath<MapNode, DefaultWeightedEdge> starShortestPath =
                new AStarShortestPath<>(graph, new AdmissibleHeuristic<>());
        GraphPath<MapNode, DefaultWeightedEdge> pathGraph = starShortestPath.getPath(sourceNode, targetNode);
        if (pathGraph == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        return pathGraph.getVertexList().stream().map(MapNode::getAddressCodeId).collect(Collectors.toList());
    }

}
