package test.com.mushiny.wms.application.test.timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import test.com.mushiny.wms.application.common.utils.JSONUtil;
import test.com.mushiny.wms.application.test.TestConstant;
import test.com.mushiny.wms.application.test.bean.WorkOrder;
import test.com.mushiny.wms.application.test.business.common.StationNodeBusiness;
import test.com.mushiny.wms.application.test.container.PodContainer;
import test.com.mushiny.wms.application.test.container.WorkOrderContainer;
import test.com.mushiny.wms.application.test.domain.Stationnode;
import test.com.mushiny.wms.application.test.repository.PodRepository;
import test.com.mushiny.wms.application.test.service.InBoundService;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/***
*
* @Author: mingchun.mu@mushiy.com
*/
@Component
public class InBound {

    private Logger LOG = LoggerFactory.getLogger(InBound.class.getName());


    @Autowired
    private PodContainer podContainer;
    @Autowired
    private WorkOrderContainer workOrderContainer;

    @Autowired
    private StationNodeBusiness stationNodeBusiness;
    @Autowired
    private PodRepository podRepository;
    @Autowired
    private InBoundService inBoundService;
    private RestTemplate restTemplate;
    public InBound() {
        this.restTemplate = new RestTemplate();
    }



    /***
     * 新货架进入入库工作站
     * @Author: mingchun.mu@mushiy.com
     */
    @Scheduled(fixedDelay=4000,initialDelay = 60000)
    @RequestMapping("getForEntity/carryPod")
    public void newPodEnterInboundStation(){
        List<Stationnode> stationnodeList = stationNodeBusiness.getAllStationNode();
        if(CollectionUtils.isEmpty(stationnodeList)){
            stationnodeList.forEach(new Consumer<Stationnode>() {
                @Override
                public void accept(Stationnode stationnode) {
                    try {
                        int podId = podRepository.getMaxIndexOfPod() + 1;
                        ResponseEntity<String> responseEntity = restTemplate.getForEntity(TestConstant.TEST_URL_PREFIX + "/carryPod?podId="+podId+"&stationName="+stationnode.getName(), String.class);
                        String res = responseEntity.getBody();
                        Thread.sleep(30 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }


    /***
    * 自动呼叫空料车
    * @Author: mingchun.mu@mushiy.com
    */
    @Scheduled(fixedDelay=4000,initialDelay = 30000)
    @RequestMapping("getForEntity/callEmptyPod")
    public void callEmptyPod(){
        List<Stationnode> stationnodeList = inBoundService.getInBoundStationNames();
        if(!CollectionUtils.isEmpty(stationnodeList)){
            stationnodeList.forEach(new Consumer<Stationnode>() {
                @Override
                public void accept(Stationnode stationnode) {
                    try {
                        ResponseEntity<String> responseEntity = restTemplate.getForEntity(TestConstant.TEST_URL_PREFIX + "/callEmptyPod", String.class);
                        String res = responseEntity.getBody();
                        if("1".equals(res.trim())){
                            LOG.info(" 呼叫空料车成功。。。 ");
                        }else{
                            LOG.info(" 呼叫空料车失败。。。 ");
                        }
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        LOG.error(" 呼叫空料车异常。。。 ", e);
                    }
                }
            });
        }
    }


    /***
     * 呼叫agv将半成品入库
     * @Author: mingchun.mu@mushiy.com
     */
    @Scheduled(fixedDelay=4000,initialDelay = 30000)
    @RequestMapping("getForEntity/carryPod")
    public void callAGVLoadPod() {


        Map<String, WorkOrder> container = workOrderContainer.getContainer();

        if(!workOrderContainer.isEmpty()){
            workOrderContainer.getContainer().forEach(new BiConsumer<String, WorkOrder>() {
                @Override
                public void accept(String workOrderId, WorkOrder workOrder) {
                    try {
                        if(!workOrder.isStock()
                                && workOrder.getPod().isInBoundStation()){
                            String paramJson = JSONUtil.toJSon(workOrder.getInboundInstruct());
                            ResponseEntity<String> responseEntity = restTemplate.getForEntity(TestConstant.TEST_URL_PREFIX + "/carryPod?podId="+workOrder.getPod().getId()+"&wordOrderId="+workOrderId, String.class, paramJson);
                            String res = responseEntity.getBody();
                            if("1".equals(res.trim())){
                                LOG.info(" 呼叫agv将半成品入库成功。。。 ");
                                workOrder.setStock(true); // 入库成功
                                workOrder.getPod().setInBoundStation(false); // 离开上架工作站
                            }else{
                                LOG.info(" 呼叫agv将半成品入库失败。。。 ");
                            }
                            Thread.sleep(5000);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        LOG.error(" 呼叫agv将半成品入库异常。。。 ", e);
                    }
                }
            });
        }
    }







}



