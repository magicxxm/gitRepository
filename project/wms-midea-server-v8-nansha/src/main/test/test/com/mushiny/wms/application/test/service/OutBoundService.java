package test.com.mushiny.wms.application.test.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import test.com.mushiny.wms.application.test.business.common.StationNodeBusiness;
import test.com.mushiny.wms.application.test.container.PodContainer;
import test.com.mushiny.wms.application.test.domain.Stationnode;

import java.util.List;

/***
*
* @Author: mingchun.mu@mushiy.com
* @Date:   2018/7/9
*/
@Component
public class OutBoundService {

    private Logger LOG = LoggerFactory.getLogger(OutBoundService.class.getName());

    @Autowired
    private PodContainer podContainer;

    private RestTemplate restTemplate;
    public OutBoundService() {
        restTemplate = new RestTemplate();
    }

    @Autowired
    private StationNodeBusiness stationNodeBusiness;

    public List<Stationnode> getStationNodeBusiness() {
        return stationNodeBusiness.getOutboundStationNodeList();
    }



}
