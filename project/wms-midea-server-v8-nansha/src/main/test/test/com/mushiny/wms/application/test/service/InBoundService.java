package test.com.mushiny.wms.application.test.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import test.com.mushiny.wms.application.common.Constant;
import test.com.mushiny.wms.application.common.utils.JSONUtil;
import test.com.mushiny.wms.application.test.TestConstant;
import test.com.mushiny.wms.application.test.bean.Pod;
import test.com.mushiny.wms.application.test.bean.WorkOrder;
import test.com.mushiny.wms.application.test.business.common.StationNodeBusiness;
import test.com.mushiny.wms.application.test.container.PodContainer;
import test.com.mushiny.wms.application.test.domain.Stationnode;

import java.util.List;
import java.util.UUID;

/***
*
* @Author: mingchun.mu@mushiy.com
* @Date:   2018/7/9
*/
@Component
public class InBoundService {

    private Logger LOG = LoggerFactory.getLogger(InBoundService.class.getName());

    @Autowired
    private PodContainer podContainer;

    private RestTemplate restTemplate;
    public InBoundService() {
        restTemplate = new RestTemplate();
    }

    @Autowired
    private StationNodeBusiness stationNodeBusiness;


    public List<Stationnode> getInBoundStationNames(){
        return stationNodeBusiness.getInboundStationNodeList();
    }

    @RequestMapping("getForEntity/carryPod")
    public void newPodEnter(String newPod){
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(TestConstant.TEST_URL_PREFIX + "/carryPod", String.class);
        String res = responseEntity.getBody();
        if("1".equals(res.trim())){
            LOG.info(" 新货架交接入库成功。。。 ");
        }else{
            LOG.info(" 新货架交接入库失败。。。 ");
        }

    }

    /**
     * 添加到工作站驮料的料车，用于呼叫agv将料车入库
     * @param podId
     * @return
     */
    public int addEmptyPodOfInboundStation(String podId){
        try {
            int podCodeId = Integer.parseInt(podId.trim());
            Pod pod = podContainer.get(podCodeId);
            if(pod == null){
                pod = new Pod(podCodeId);
                podContainer.add(pod);
            }
            pod.setInBoundStation(true);
            return Constant.SUCCESS_FLAG;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            try {
                podContainer.remove(Integer.parseInt(podId.trim()));
            } catch (NumberFormatException e1) {
                e1.printStackTrace();
            }
            return Constant.FAIL_FLAG;
        }
    }


    @RequestMapping("getForEntity/InboundInstruct")
    public void sendInboundCommand(String podId) {

        WorkOrder workOrder = new WorkOrder(UUID.randomUUID().toString());
        workOrder.setPod(podContainer.get(Integer.parseInt(podId))); // 半成品即将放在此货架上
        String jsonParam = JSONUtil.toJSon(workOrder.getInboundInstruct());

        restTemplate.getForEntity(TestConstant.TEST_URL_PREFIX + "/InboundInstruct", String.class, jsonParam);
    }


}
