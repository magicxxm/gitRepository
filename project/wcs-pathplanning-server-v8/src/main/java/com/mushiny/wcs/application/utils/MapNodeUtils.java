package com.mushiny.wcs.application.utils;

import com.mushiny.wcs.application.business.CommonBusiness;
import com.mushiny.wcs.application.business.path.AdmissibleHeuristic;
import com.mushiny.wcs.application.domain.*;
import com.mushiny.wcs.application.event.MapNeighborFlushEvent;
import com.mushiny.wcs.application.jgrapht.MyDefaultEdge;
import com.mushiny.wcs.application.jgrapht.MyDefaultWeightedEdge;
import com.mushiny.wcs.common.context.ApplicationBeanContextAware;
import com.mushiny.wcs.common.utils.DateTimeUtil;
import org.apache.commons.collections4.map.ReferenceMap;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.AStarShortestPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedPseudograph;
import org.jgrapht.traverse.CrossComponentIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.*;

import java.util.*;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author:
 * @Description: Created by wangjianwei on 2017/11/11.
 */
@Component
@Order(1)
@Transactional
public class MapNodeUtils implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(MapNodeUtils.class);
    private static final int EMPTYDRIVEPATH = 0;
    private static final int HEAVYDRIVEPATH = 1;
    private static final String SEPAR = System.getProperty("line.separator", "/n");
    private final Object lock = new Object();
    private Map<String, List<MapNeighbor>> mapNeighborsTemp =Collections.synchronizedMap( new HashMap());

    private Map<String, List<MapNeighbor>> mapNeighbors =Collections.synchronizedMap( new HashMap());

    private Map<String, Map<Integer, MapNode>> mapNodes = new HashMap();

    private List<UpdateCost> updateNodeCost=new ArrayList<>();
    private List<UpdateCost> recoverNodeCost=new ArrayList<>();
    private final MapNode defaultMapNode = new MapNode();
    private Map<String, com.mushiny.wcs.application.domain.Map> maps = new HashMap();
    @Autowired
    private CommonBusiness commonBusiness;

    public List<NodeCosteValue> getMinCost(String sectionId,Integer start,Integer end)
    {
        com.mushiny.wcs.application.domain.Map mapTemp = maps.get(sectionId);
        List<NodeCosteValue> finalResult=new ArrayList<>();
        Map<Integer,NodeCosteValue> result=new HashMap();
        if(!ObjectUtils.isEmpty(mapTemp))
        {
            String mapId=mapTemp.getId();
            List<MapNeighbor> temp=mapNeighbors.get(mapId);

            for(MapNeighbor mn:temp)
            {
                Integer newCost=mn.getNewCost();
                Integer addr=mn.getInNode().getAddressCodeId();
                Integer carryCost=mn.getCarryingCost();
                Integer costValue=-1;
                if(carryCost==-1)
                {
                    costValue=Integer.MAX_VALUE;
                }else{
                    costValue=ObjectUtils.isEmpty(newCost)?carryCost:newCost;
                }

                if(!result.containsKey(addr))
                {
                    NodeCosteValue cv=new NodeCosteValue();
                    cv.setInNode(addr);
                    cv.setCostType(mn.getCostType());
                    cv.setOutNode(mn.getOutNode().getAddressCodeId());
                    cv.setCostValue(costValue);
                    result.put(addr,cv);
                }else{
                    NodeCosteValue ncv=result.get(addr);
                    if(ncv.getCostValue()>costValue)
                    {
                        ncv.setCostValue(costValue);
                        ncv.setOutNode(mn.getOutNode().getAddressCodeId());
                        ncv.setCostType(mn.getCostType());
                    }
                }
            }

            if(!CollectionUtils.isEmpty(result))
            {
                if(ObjectUtils.isEmpty(end))
                {
                    finalResult.add(result.get(start));
                }else
                {
                    for(int k=start;k<=end;k++)
                    {
                        finalResult.add(result.get(k));
                    }
                }

            }
        }

      return  finalResult;
    }

    public synchronized Map<String,Object> getPathPairDetail(String sectionId,String paths,int driverType)
    {
        Map<String,Object> result=new HashMap<>();
        List<PathPair> pathPairResult=null;
        Integer sumCost=0;
        com.mushiny.wcs.application.domain.Map mapTemp = maps.get(sectionId);
        if(!ObjectUtils.isEmpty(mapTemp))
        {
            pathPairResult=findPathPairs(mapNeighbors.get(mapTemp.getId()),paths,driverType);

        }
        if(!CollectionUtils.isEmpty(pathPairResult))
        {
            for(PathPair temp:pathPairResult)
            {
                sumCost+=temp.getWeight();
            }
        }
        result.put("details",pathPairResult);
        result.put("costSum",sumCost);

        return result;
    }

    private List<PathPair> findPathPairs(List<MapNeighbor> mapNeighbors,String paths,int driverType){
        List<PathPair> result=new ArrayList<>();
        List<PathPair> pathPairs=null;
        if(!CollectionUtils.isEmpty(mapNeighbors))
        {
            pathPairs=new ArrayList<>();
            for(MapNeighbor mapNeighbor:mapNeighbors)
            {
                PathPair pathPair=new PathPair(mapNeighbor.getInNode().getAddressCodeId(),
                        mapNeighbor.getOutNode().getAddressCodeId(),
                        mapNeighbor.getCarryingCost(),mapNeighbor.getCost(),mapNeighbor.getNewCost());
                pathPair.setWeight(driverType);
                pathPairs.add(pathPair);

            }
        }
        if(!CollectionUtils.isEmpty(pathPairs))
        {
           String[] pathTemp=paths.split(",");
           int len=pathTemp.length-2;
           if(len>=1)
           {
               for(int k=0;k<pathTemp.length-1;k++)
               {
                   PathPair pathPairTemp=findPathPair(pathPairs,pathTemp[k],pathTemp[k+1]);
                   if(!ObjectUtils.isEmpty(pathPairTemp))
                   {
                       result.add(pathPairTemp);
                   }
               }
           }

        }
        return  result;

    }
    private PathPair findPathPair(List<PathPair> pathPairs,String start,String end){
        PathPair result=null;
        for(PathPair temp:pathPairs)
        {
            if(temp.getStart()==Integer.parseInt(start)&&temp.getEnd()==Integer.parseInt(end))
            {
                result=temp;
                break;
            }
        }
        return result;

    }
    public synchronized List<Integer>  getChangedCostValue(String sectionId,Integer changeValue)
    {
        com.mushiny.wcs.application.domain.Map mapTemp = maps.get(sectionId);
        List<Integer> finalResult=new ArrayList<>();
        Set<Integer> changed=new HashSet<>();
        Set<Integer> unChanged=new HashSet<>();
        Integer costValue=ObjectUtils.isEmpty(changeValue)?1000:changeValue;
        if(!ObjectUtils.isEmpty(mapTemp))
        {
            String mapId=mapTemp.getId();
            List<MapNeighbor> temp=mapNeighbors.get(mapId);
            for(MapNeighbor mn:temp)
            {
                if(!ObjectUtils.isEmpty(mn.getNewCost()))
                {
                    if(mn.getNewCost().equals(costValue))
                    {
                      //  changed.add(mn.getInNode().getAddressCodeId());
                        changed.add(mn.getOutNode().getAddressCodeId());
                    }
                }else{
                   // unChanged.add(mn.getInNode().getAddressCodeId());
                    unChanged.add(mn.getOutNode().getAddressCodeId());
                }
            }


            for(Integer addrTemp:changed)
            {
                if(!unChanged.contains(addrTemp))
                {
                    finalResult.add(addrTemp);
                }
            }



        }

        return  finalResult;
    }



    public List<UpdateCost> getUpdateNodeCost() {
        return updateNodeCost;
    }

    public void setUpdateNodeCost(List<UpdateCost> updateNodeCost) {
        this.updateNodeCost = updateNodeCost;
    }

    public List<UpdateCost> getRecoverNodeCost() {
        return recoverNodeCost;
    }

    public void setRecoverNodeCost(List<UpdateCost> recoverNodeCost) {
        this.recoverNodeCost = recoverNodeCost;
    }

    public MapNode findMapNode(String mapId, int addr) {
        MapNode result = mapNodes.get(mapId).get(addr);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("内存中地图{}信息如下" + SEPAR + "{}", mapId, printMapNodeByMapId(mapId,null));
        }
        Assert.notNull(result, "获取地图 " + mapId + " 节点" + addr + " 失败");

        return result;

    }

    public MapNodeUtils() {

    }

    private void initMapNodes() {
        if (!CollectionUtils.isEmpty(maps)) {
            for (Map.Entry<String, com.mushiny.wcs.application.domain.Map> temp : maps.entrySet()) {
                initMapNode(temp.getValue().getId());
            }
        }
    }

    private void initMapNode(String mapId) {
        List<MapNode> search = commonBusiness.getMapNodeRepository().getEmptyNode(mapId);
        Map<Integer, MapNode> temp = new HashMap();
        for (MapNode mn : search) {
            temp.put(mn.getAddressCodeId(), mn);
        }
        mapNodes.put(mapId, temp);

    }

    private void initMapNeighbor() {
        if (!CollectionUtils.isEmpty(maps)) {
            for (Map.Entry<String, com.mushiny.wcs.application.domain.Map> temp : maps.entrySet()) {
                List<MapNeighbor> search = commonBusiness.getMapNeighborRepository().getByMapId(temp.getValue().getId());
                mapNeighbors.put(temp.getValue().getId(), search);
            }
            mapNeighborsTemp=JSONUtil.deepClone(mapNeighbors);
        }
    }

    private void initMap() {
        List<com.mushiny.wcs.application.domain.Map> mapsTemp = commonBusiness.getMapRepository().getAllActiveMap();
        if (!CollectionUtils.isEmpty(mapsTemp)) {

            for (com.mushiny.wcs.application.domain.Map temp : mapsTemp) {
                maps.put(temp.getSectionId(), temp);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("section {} mapId {} name {}", temp.getSectionId(), temp.getId(), temp.getName());
                }
            }
        }


    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, com.mushiny.wcs.application.domain.Map> temp : maps.entrySet()) {
            final com.mushiny.wcs.application.domain.Map m = temp.getValue();
            sb.append("mapId=" + m.getId());
            sb.append(" mapName=" + m.getName());
            sb.append(" SectionId=" + m.getSectionId());
            sb.append(" WarehouseId=" + m.getWarehouseId());
            sb.append(SEPAR);
        }
        sb.append(printMapNodes());
        sb.append(printMapNeighbors());
        return sb.toString();
    }

    public String printMapNodes() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Map<Integer, MapNode>> temp : mapNodes.entrySet()) {
            sb.append(printMapNodeByMapId(temp.getKey(),null));
        }
        return sb.toString();
    }


    public  String printMapNodeByMapId(String mapId,Integer add) {
        StringBuilder sb = new StringBuilder();
        sb.append("地图"+mapId + "信息"+"----------");
        if(!ObjectUtils.isEmpty(add))
        {
            MapNode temp2 =mapNodes.get(mapId).get(add);
            if(!ObjectUtils.isEmpty(temp2))
            {
                sb.append(SEPAR);
                sb.append(" MapNodeId=" +temp2.getId());
                sb.append(" AddressCode=" + temp2.getAddressCodeId());
            }


        }else{
            for (Map.Entry<Integer, MapNode> temp : mapNodes.get(mapId).entrySet()) {


                sb.append(SEPAR);
                MapNode temp2 = temp.getValue();
                sb.append(" MapNodeId=" + temp2.getId());
                sb.append(" AddressCode=" + temp2.getAddressCodeId());
                sb.append(SEPAR);


            }
        }

        return sb.toString();
    }
    public  String printMapNodeBySectionId(String sectionId,Integer add) {
        String result="";
        com.mushiny.wcs.application.domain.Map mapTemp = maps.get(sectionId);
        if (!ObjectUtils.isEmpty(mapTemp)) {
            result = printMapNodeByMapId(mapTemp.getId(),add);
        }
        return result;
    }

   public String printMapNeighborByMapId(String mapId,Integer addr)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(mapId +"cost 信息"+ "###########");
        sb.append(SEPAR);
        List<MapNeighbor> mn = mapNeighbors.get(mapId);
        for (MapNeighbor temp2 : mn) {
            if(temp2.getInNode().getAddressCodeId()==addr || temp2.getOutNode().getAddressCodeId()==addr)
            {
                sb.append(" mapNeighborId=" + temp2.getId());
                sb.append(" inMapNode=" + temp2.getInNode().getAddressCodeId());
                sb.append(" outMapNode=" + temp2.getOutNode().getAddressCodeId());
                sb.append(" newCost=" + temp2.getNewCost());
                sb.append(" cost=" + temp2.getCost());
                sb.append(" costType=" + temp2.getCostType());
                sb.append(" carryCostType=" + temp2.getCarryingCost());
                sb.append(SEPAR);
            }
        }
        return  sb.toString();

    }

    public String printMapNeighbors() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Map<Integer, MapNode>> temp : mapNodes.entrySet()) {
            sb.append(printMapNeighborByMapId(temp.getKey()));

        }
        return sb.toString();
    }

    public List<Map<String,Object>> getMapNeighborByMapId(String sectionId,Integer addr){
        List<Map<String,Object>> result=new ArrayList<>();

        com.mushiny.wcs.application.domain.Map mapTemp = maps.get(sectionId);
        if (!ObjectUtils.isEmpty(mapTemp)) {
            List<MapNeighbor> mn = mapNeighbors.get(mapTemp.getId());
            for (MapNeighbor temp2 : mn) {
                if(ObjectUtils.isEmpty(addr))
                {
                    Map<String,Object> tt=new HashMap<>();
                    tt.put("mapNeighborId",temp2.getId());
                    tt.put("inMapNode",temp2.getInNode().getAddressCodeId());
                    tt.put("outMapNode",temp2.getOutNode().getAddressCodeId());
                    tt.put("newCost",temp2.getNewCost());
                    tt.put("cost",temp2.getCost());
                    tt.put("costType",temp2.getCostType());
                    tt.put("carryCostValue",temp2.getCarryingCost());
                    result.add(tt);
                }else{
                    if(temp2.getInNode().getAddressCodeId()==addr || temp2.getOutNode().getAddressCodeId()==addr)
                    {
                        Map<String,Object> tt=new HashMap<>();
                        tt.put("mapNeighborId",temp2.getId());
                        tt.put("inMapNode",temp2.getInNode().getAddressCodeId());
                        tt.put("outMapNode",temp2.getOutNode().getAddressCodeId());
                        tt.put("newCost",temp2.getNewCost());
                        tt.put("cost",temp2.getCost());
                        tt.put("costType",temp2.getCostType());
                        tt.put("carryCostValue",temp2.getCarryingCost());
                        result.add(tt);
                    }
                }
        }
        }
        return result;

    }

    public  String printMapNeighborByMapId(String mapId) {
        StringBuilder sb = new StringBuilder();
        sb.append("地图"+mapId +"cost 信息"+ "###########");
        sb.append(SEPAR);
        List<MapNeighbor> mn = mapNeighbors.get(mapId);
        for (MapNeighbor temp2 : mn) {
            sb.append(" mapNeighborId=" + temp2.getId());
            sb.append(" inMapNode=" + temp2.getInNode().getAddressCodeId());
            sb.append(" outMapNode=" + temp2.getOutNode().getAddressCodeId());
            sb.append(" newCost=" + temp2.getNewCost());
            sb.append(" cost=" + temp2.getCost());
            sb.append(" costType=" + temp2.getCostType());
            sb.append(" carryCostType=" + temp2.getCarryingCost());
            sb.append(SEPAR);
        }
        return sb.toString();
    }
    public  String printMapNeighbor(List<MapNeighbor> mn ) {
        StringBuilder sb = new StringBuilder();
        for (MapNeighbor temp2 : mn) {
            sb.append(" mapNeighborId=" + temp2.getId());
            sb.append(" inMapNode=" + temp2.getInNode().getAddressCodeId());
            sb.append(" outMapNode=" + temp2.getOutNode().getAddressCodeId());
            sb.append(" newCost=" + temp2.getNewCost());
            sb.append(" cost=" + temp2.getCost());
            sb.append(" costType=" + temp2.getCostType());
            sb.append(" carryCostType=" + temp2.getCarryingCost());
            sb.append(SEPAR);
        }
        return sb.toString();
    }

    public  String printMapNeighborBySectionId(String sectionId,Integer addr) {
        String result=null;
        com.mushiny.wcs.application.domain.Map mapTemp = maps.get(sectionId);
        if (!ObjectUtils.isEmpty(mapTemp)) {
            if(ObjectUtils.isEmpty(addr))
            {
                result = printMapNeighborByMapId(mapTemp.getId());
            }else{
                result = printMapNeighborByMapId(mapTemp.getId(),addr);
            }

        }
        return result;
    }

    public void init() {
        synchronized (lock) {
            initMap();
            initMapNodes();
            initMapNeighbor();
            LOGGER.info(this.toString());
        }

    }

    public int updateMapNeighborByMapId(String mapId, String addressList, String costValue) {

        synchronized (lock) {
            int result = 1;
            List<MapNeighbor> mapNeighbor = new ArrayList<>();
            List<String> addressTemp = Arrays.asList(addressList.split(","));
            List<Integer> address = new ArrayList<>();
            Integer cost = StringUtils.isEmpty(costValue) ? null : Integer.parseInt(costValue);

            for (String temp : addressTemp) {
                address.add(Integer.parseInt(temp));
            }
            LOGGER.info("接收到{}个 ---{}更改cost值 {} ",address.size(),addressList,costValue);
            LOGGER.info("更改以前---{} ",printMapNeighborByMapId(mapId));

            for (MapNeighbor temp : mapNeighbors.get(mapId)) {

                if (address.contains(temp.getOutNode().getAddressCodeId())) {
                    temp.setNewCost(cost);
                    mapNeighbor.add(temp);

                }
            }


            //LOGGER.info("更改了---{} ",printMapNeighbor(mapNeighbor));
           // LOGGER.info("更改以后---{} ",printMapNeighborByMapId(mapId));
            if (!CollectionUtils.isEmpty(mapNeighbor)) {
                result=1;
               /* ApplicationContext context = ApplicationBeanContextAware.getApplicationContext();
                context.publishEvent(new MapNeighborFlushEvent(context, mapNeighbor));*/
            } else {
                LOGGER.error("未找到{} 要修改的cost",costValue);
                result = 0;
            }
            UpdateCost uc=new UpdateCost();
            uc.setAddressList(addressList);
            uc.setCostValue(costValue);
            uc.setTime(DateTimeUtil.getNowFormat());
            if(StringUtils.isEmpty(costValue))
            {
                recoverNodeCost.add(uc);
            }else{
                updateNodeCost.add(uc);
            }
            return result;
        }


    }


    private MapNeighbor findMapNeighbor(List<MapNeighbor> mapNeighbors ,MapNeighbor mapNeighbor){


        for(MapNeighbor temp:mapNeighbors)
        {
            if(temp.getId().equals(mapNeighbor.getId()))
            {
                return temp;
            }
        }
        return null;
    }

    public int updateHeavyDriveMapNeighborByMapId(String mapId, String addressList, String costValue) {

        synchronized (lock) {
            int result = 1;
            List<MapNeighbor> mapNeighbor = new ArrayList<>();
            List<String> addressTemp = Arrays.asList(addressList.split(","));
            List<Integer> address = new ArrayList<>();
            Integer cost = StringUtils.isEmpty(costValue) ? 0 : Integer.parseInt(costValue);

            for (String temp : addressTemp) {
                address.add(Integer.parseInt(temp));
            }
            LOGGER.info("接收{}个 ---{}更改重车cost值 {} ",address.size(),addressList,costValue);

            for (MapNeighbor temp : mapNeighborsTemp.get(mapId)) {

                if (address.contains(temp.getOutNode().getAddressCodeId())) {
                    MapNeighbor findMapNeighbor=findMapNeighbor(mapNeighbors.get(mapId),temp);
                    findMapNeighbor.setCarryingCost(temp.getCarryingCost()+cost);


                    mapNeighbor.add(findMapNeighbor);

                }
            }


            //LOGGER.info("更改了---{} ",printMapNeighbor(mapNeighbor));
            // LOGGER.info("更改以后---{} ",printMapNeighborByMapId(mapId));
            if (!CollectionUtils.isEmpty(mapNeighbor)) {
                result=1;
               /* ApplicationContext context = ApplicationBeanContextAware.getApplicationContext();
                context.publishEvent(new MapNeighborFlushEvent(context, mapNeighbor));*/
            } else {
                LOGGER.error("未找到{} 要修改的cost",costValue);
                result = 0;
            }
            UpdateCost uc=new UpdateCost();
            uc.setAddressList(addressList);
            uc.setCostValue(costValue);
            uc.setTime(DateTimeUtil.getNowFormat());
            uc.setCostType("HeavyDrive");
            if(cost<0)
            {
                recoverNodeCost.add(uc);
            }else{
                updateNodeCost.add(uc);
            }
            return result;
        }


    }

    public int updateMapNeighborBySectionId(String sectionId, String addressList, String costValue) {
        int result = 0;
        com.mushiny.wcs.application.domain.Map mapTemp = maps.get(sectionId);
        if (!ObjectUtils.isEmpty(mapTemp)) {
            result = updateMapNeighborByMapId(mapTemp.getId(), addressList, costValue);
        }

        return result;
    }


    public int updateHeavyDriveMapNeighborBySectionId(String sectionId, String addressList, String costValue) {
        int result = 0;
        com.mushiny.wcs.application.domain.Map mapTemp = maps.get(sectionId);
        if (!ObjectUtils.isEmpty(mapTemp)) {
            result = updateHeavyDriveMapNeighborByMapId(mapTemp.getId(), addressList, costValue);
        }

        return result;
    }

    @Override
    public void run(String... args) throws Exception {
        LOGGER.info("开始加载地图....");
        init();

        LOGGER.info("加载地图结束");

    }

    public List<Integer> getDrivePathBySection(String sectionId, int sourceVertex, int targetVertex, int pathType) {
        List<Integer> result = null;
        com.mushiny.wcs.application.domain.Map mapTemp = maps.get(sectionId);
        if (!ObjectUtils.isEmpty(mapTemp)) {
            result = getDrivePathByMapId(mapTemp.getId(), sourceVertex, targetVertex, pathType);
        }else{
            LOGGER.error("计算{} 到 {} 的路径 未找到section {} 对应的地图----\r\n{}",sourceVertex,targetVertex,sectionId,JSONUtil.toJSon(maps));
        }
        return result;

    }
    public List<Integer> getDrivePathByMapId(String mapId, int sourceVertex, int targetVertex, int pathType) {
        synchronized (lock) {
            MapNode sourceNode = findMapNode(mapId, sourceVertex);
            MapNode targetNode = findMapNode(mapId, targetVertex);
            AStarShortestPath<MapNode, MyDefaultWeightedEdge> starShortestPath = getAStarShortestPath(mapId, pathType);
            GraphPath<MapNode, MyDefaultWeightedEdge> pathGraph = starShortestPath.getPath(sourceNode, targetNode);
            List<Integer> result= pathGraph.getVertexList().stream().map(MapNode::getAddressCodeId).collect(Collectors.toList());
            List<String> wes=new ArrayList();
            List<MyDefaultWeightedEdge> weights= pathGraph.getEdgeList();
            for(DefaultWeightedEdge de:weights)
            {
                wes.add(de.toString());
            }
            LOGGER.info("{} 到 {} 的路径为{}",sourceVertex,targetVertex,JSONUtil.toJSon(result));
            LOGGER.info("{} 到 {} 的cost为{}",sourceVertex,targetVertex,JSONUtil.toJSon(wes));
            return  result;
        }
    }

    public Map<String,Object> getDrivePathDetail(String sectionId, int sourceVertex, int targetVertex, int pathType) {
        synchronized (lock) {
            com.mushiny.wcs.application.domain.Map mapTemp = maps.get(sectionId);
            Map<String,Object> details=new HashMap<>();
            if (!ObjectUtils.isEmpty(mapTemp)) {
                MapNode sourceNode = findMapNode(mapTemp.getId(), sourceVertex);
                MapNode targetNode = findMapNode(mapTemp.getId(), targetVertex);
              //  buildPath(sectionId,sourceVertex,targetVertex,1);
                AStarShortestPath<MapNode, MyDefaultWeightedEdge> starShortestPath = getAStarShortestPath(mapTemp.getId(), pathType);
                GraphPath<MapNode, MyDefaultWeightedEdge> pathGraph = starShortestPath.getPath(sourceNode, targetNode);
                List<Integer> result= pathGraph.getVertexList().stream().map(MapNode::getAddressCodeId).collect(Collectors.toList());
                List<String> wes=new ArrayList();
                List<MyDefaultWeightedEdge> weights= pathGraph.getEdgeList();
                for(DefaultWeightedEdge de:weights)
                {
                    wes.add(de.toString());
                }
                details.put("paths:",result);
                details.put("costsDetail",wes);
                details.put("costsSum",pathGraph.getWeight());
                LOGGER.info("{} 到 {} 的路径为{}",sourceVertex,targetVertex,JSONUtil.toJSon(result));
                LOGGER.info("{} 到 {} 的cost为{}",sourceVertex,targetVertex,JSONUtil.toJSon(wes));
            }else{
                LOGGER.error("计算{} 到 {} 的路径 未找到section {} 对应的地图----\r\n{}",sourceVertex,targetVertex,sectionId,JSONUtil.toJSon(maps));
            }

            return  details;
        }
    }

    private Path buildPath(String sectionId, int sourceVertex, int targetVertex,int pathType)
    {
        com.mushiny.wcs.application.domain.Map mapTemp = maps.get(sectionId);
        Map<String,Object> details=new HashMap<>();
        Path root=null;
        List<MapNode> ll;
        List<String> resu;
        Map<Integer,Integer> keys=new HashMap<>();
        if (!ObjectUtils.isEmpty(mapTemp)) {
            MapNode sourceNode = findMapNode(mapTemp.getId(), sourceVertex);
            MapNode targetNode = findMapNode(mapTemp.getId(), targetVertex);
            root=new Path(sourceNode);
            DirectedWeightedPseudograph<MapNode, MyDefaultWeightedEdge> graph = new DirectedWeightedPseudograph<>(MyDefaultWeightedEdge.class);
            int cost = -1;
            //String path=getPath(mapId);
            for (Map.Entry<Integer, MapNode> mapNode : mapNodes.get(mapTemp.getId()).entrySet()) {
                graph.addVertex(mapNode.getValue());
            }
            for (MapNeighbor mapNeighbor : mapNeighbors.get(mapTemp.getId())) {
                Integer newCost = mapNeighbor.getNewCost();
                if (pathType == HEAVYDRIVEPATH) {
                    cost = mapNeighbor.getCarryingCost();
                } else {
                    cost = mapNeighbor.getCost();
                }

                if (newCost != null && newCost >= 0 && cost != -1) {
                    Graphs.addEdge(graph, mapNeighbor.getInNode(), mapNeighbor.getOutNode(), newCost+cost);
                } else {
                    if (cost >= 0) {
                        Graphs.addEdge(graph, mapNeighbor.getInNode(), mapNeighbor.getOutNode(), cost);
                    }
                }

            }
            build(keys,graph.outgoingEdgesOf(sourceNode),graph,root,sourceNode);
          ll=new ArrayList<>();
           resu=new ArrayList<>();
            buildPath(ll,root,resu);
        }
        return root;
    }

    private Path build(Map<Integer,Integer> paths, Set<MyDefaultWeightedEdge>mdw, DirectedWeightedPseudograph<MapNode, MyDefaultWeightedEdge> graph,Path root,MapNode sourceNode){

          Path result=root;

            for(MyDefaultWeightedEdge me:mdw)
            {
                MapNode source=(MapNode)me.getSource();
                MapNode target=(MapNode)me.getTarget();

                if(!paths.containsKey(target.getAddressCodeId()))
                {
                    paths.put(target.getAddressCodeId(),source.getAddressCodeId());
                    Path rootTemp=new  Path(target);
                    Set<MyDefaultWeightedEdge> temp= graph.outgoingEdgesOf(target);
                    if(!CollectionUtils.isEmpty(temp)) {
                        build(paths,temp,graph,rootTemp,target);
                    }else {
                        result.getNext().add(rootTemp);
                    }
                }


                }




        return result;

    }
    public void buildPath(List<MapNode> stack, Path root, List<String> pathList) {

        if (root != null) {
            stack.add(root.getCurrent());
            if (root.getNext().size() == 0) {
                changeToPath(stack, pathList); // 把值栈中的值转化为路径
            } else {
                List<Path> items = root.getNext();
                for (int i = 0; i < items.size(); i++) {
                    buildPath(stack, items.get(i), pathList);
                }
            }
            stack.remove(stack.size() - 1);
        }
    }





    /**
     * @param path
     * @param pathList
     */
    public void changeToPath(List<MapNode> path, List<String> pathList) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < path.size(); i++) {
            if (path.get(i) != null) {
                sb.append(path.get(i).getAddressCodeId() + " ");
            }

        }
        pathList.add(sb.toString().trim());


    }

    private AStarShortestPath<MapNode, MyDefaultWeightedEdge> getAStarShortestPath(String mapId, int pathType) {
        AStarShortestPath<MapNode, MyDefaultWeightedEdge> starShortestPath = null;

        try {
            Graph<MapNode, MyDefaultWeightedEdge> graph = new DirectedWeightedPseudograph<>(MyDefaultWeightedEdge.class);
            int cost = -1;
            //String path=getPath(mapId);
            for (Map.Entry<Integer, MapNode> mapNode : mapNodes.get(mapId).entrySet()) {
                graph.addVertex(mapNode.getValue());
            }
            for (MapNeighbor mapNeighbor : mapNeighbors.get(mapId)) {
                Integer newCost = mapNeighbor.getNewCost();
                if (pathType == HEAVYDRIVEPATH) {
                    cost = mapNeighbor.getCarryingCost();
                } else {
                    cost = mapNeighbor.getCost();
                }

                if (newCost != null && newCost >= 0 && cost != -1) {
                    Graphs.addEdge(graph, mapNeighbor.getInNode(), mapNeighbor.getOutNode(), newCost+cost);
                } else {
                    if (cost >= 0) {
                        Graphs.addEdge(graph, mapNeighbor.getInNode(), mapNeighbor.getOutNode(), cost);
                    }
                }
               /* if (newCost != null && newCost >= 0 && cost != -1) {
                    Graphs.addEdge(graph, mapNeighbor.getInNode(), mapNeighbor.getOutNode(), newCost);
                } else {
                    if (cost >= 0) {
                        Graphs.addEdge(graph, mapNeighbor.getInNode(), mapNeighbor.getOutNode(), cost);
                    }
                }*/
            }
            starShortestPath = new AStarShortestPath<>(graph, new AdmissibleHeuristic<>());

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        return starShortestPath;

    }
    private String getPath(String mapId){
        String result="";
        Integer[] it=new Integer[]{1,2,3,4,5,6,7,8};

        DefaultDirectedGraph<Integer, DefaultEdge> g = new DefaultDirectedGraph<>(DefaultEdge.class);
         for (Integer temp:it) {
            g.addVertex(temp);
         }

        Graphs.addEdgeWithVertices(g,1,2);
        Graphs.addEdgeWithVertices(g,2,3);
        Graphs.addEdgeWithVertices(g,3,4);
        Graphs.addEdgeWithVertices(g,1,5);
        Graphs.addEdgeWithVertices(g,2,6);
        Graphs.addEdgeWithVertices(g,3,7);
        Graphs.addEdgeWithVertices(g,4,8);
        Graphs.addEdgeWithVertices(g,5,6);
        Graphs.addEdgeWithVertices(g,6,7);
        Graphs.addEdgeWithVertices(g,7,8);
          //  Set<DefaultEdge> edges=g.getAllEdges();
        result=g.toString();
            return result;

    }


    private static class MapNodeComparator implements Comparator<MapNode> {
        public static final MapNodeComparator DEFAULT = new MapNodeComparator();

        @Override
        public int compare(MapNode mapNode1, MapNode mapNode2) {
            int address1 = findAddr(mapNode1);
            int address2 = findAddr(mapNode2);
            return (address1 < address2) ? -1 : (address1 > address2) ? 1 : 0;
        }

        private int findAddr(MapNode mapNode) {
            return mapNode.getAddressCodeId();
        }
    }

}

