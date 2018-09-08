package com.mushiny.wms.application.business.common;

import com.mushiny.wms.application.domain.Stationnode;
import com.mushiny.wms.application.repository.StationnodeRepository;
import com.mushiny.wms.common.utils.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Consumer;

/**
 * Created by Administrator on 2018/7/6.
 */
@Component
public class StationNodeBusiness {

    private Logger LOG = LoggerFactory.getLogger(StationNodeBusiness.class.getName());

    private final StationnodeRepository stationnodeRepository;

    private List<Stationnode> allStationNode;
    private final Map<String, Stationnode> nameMappingStation = new HashMap<>();
    private final Map<Integer, Stationnode> addressMappingStation = new HashMap<>();
    private final Map<Integer,LinkedHashMap<Integer, Stationnode>> typeMappingStation = new HashMap<>();

    @Autowired
    public StationNodeBusiness(StationnodeRepository stationnodeRepository) {
        this.stationnodeRepository = stationnodeRepository;
        initStationNode();
    }

    /***
    * 有数据库更改，调用此接口更新系统缓存
    * @Author: mingchun.mu@mushiy.com
    */
    public void initStationNode(){
      //  setAllStationNode(this.stationnodeRepository.getAllStationNode());
       // initMappingStation();
    }
    public void initMappingStation(){
        allStationNode.forEach(new Consumer<Stationnode>() {
            @Override
            public void accept(Stationnode stationnode) {
                nameMappingStation.put(stationnode.getName(), stationnode);

                List<Integer> addrList = CommonUtil.string2List("");

                if(typeMappingStation.containsKey(stationnode.getType()))
                {

                    if(addrList != null){
                        addrList.forEach(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer addr) {
                                addressMappingStation.put(addr, stationnode);
                                typeMappingStation.get(stationnode.getType()).put(addr, stationnode);
                            }
                        });
                    }

                }else{

                    typeMappingStation.put(stationnode.getType(),new LinkedHashMap<>());
                    if(addrList != null){
                        addrList.forEach(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer addr) {
                                addressMappingStation.put(addr, stationnode);
                                typeMappingStation.get(stationnode.getType()).put(addr, stationnode);
                            }
                        });
                    }

                }

            }
        });
    }

    public List<Stationnode> getAllStationNode() {
        return allStationNode;
    }

    public void setAllStationNode(List<Stationnode> allStationNode) {
        this.allStationNode = allStationNode;
    }

    public LinkedHashMap getTypeStation(Integer type)
    {
        return typeMappingStation.get(type);
    }
    public Stationnode getStationNode(String name){
        return nameMappingStation.get(name);
    }
    public Stationnode getStationNode(Integer addr){
        return nameMappingStation.get(addr);
    }

    public Map<String, Stationnode> getNameMappingStation() {
        return nameMappingStation;
    }

    public Map<Integer, Stationnode> getAddressMappingStation() {
        return addressMappingStation;
    }
}
