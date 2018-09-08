package test.com.mushiny.wms.application.test.business.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import test.com.mushiny.wms.application.common.utils.CommonUtil;
import test.com.mushiny.wms.application.test.domain.Stationnode;
import test.com.mushiny.wms.application.test.repository.StationnodeRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created by Administrator on 2018/7/6.
 */
@Component
public class StationNodeBusiness {

    private Logger LOG = LoggerFactory.getLogger(StationNodeBusiness.class.getName());

    private final StationnodeRepository stationnodeRepository;

    private List<Stationnode> allStationNode;
    private final List<Stationnode> inboundStationNodeList = new ArrayList<>();
    private final List<Stationnode> outboundStationNodeList = new ArrayList<>();
    private final Map<String, Stationnode> nameMappingStation = new HashMap<>();
    private final Map<Integer, Stationnode> addressMappingStation = new HashMap<>();


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
        setAllStationNode(this.stationnodeRepository.getAllStationNode());
        initMappingStation();
    }
    public void initMappingStation(){

        if(CollectionUtils.isEmpty(allStationNode)){
            return;
        }

        inboundStationNodeList.clear();
        outboundStationNodeList.clear();

        allStationNode.forEach(new Consumer<Stationnode>() {
            @Override
            public void accept(Stationnode stationnode) {
                nameMappingStation.put(stationnode.getName(), stationnode);

                List<Integer> addrList = CommonUtil.string2List(stationnode.getAddrs());
                if(addrList != null){
                    addrList.forEach(new Consumer<Integer>() {
                        @Override
                        public void accept(Integer addr) {
                            addressMappingStation.put(addr, stationnode);
                        }
                    });
                }

                if(stationnode.getType() == 1){
                    inboundStationNodeList.add(stationnode);
                }

                if(stationnode.getType() == 2){
                    outboundStationNodeList.add(stationnode);
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

    public List<Stationnode> getInboundStationNodeList() {
        return inboundStationNodeList;
    }

    public List<Stationnode> getOutboundStationNodeList() {
        return outboundStationNodeList;
    }
}
