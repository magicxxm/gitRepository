package test.com.mushiny.wms.application.test.timer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import test.com.mushiny.wms.application.common.utils.JSONUtil;
import test.com.mushiny.wms.application.test.TestConstant;
import test.com.mushiny.wms.application.test.bean.WorkOrder;
import test.com.mushiny.wms.application.test.container.WorkOrderContainer;
import test.com.mushiny.wms.application.test.service.OutBoundService;

/**
 * Created by Administrator on 2018/7/9 0009.
 */
@Component
public class OutBound {


    @Autowired
    private WorkOrderContainer workOrderContainer;

    @Autowired
    private OutBoundService outBoundService;

    private RestTemplate restTemplate;
    public OutBound() {
        restTemplate = new RestTemplate();
    }

    /***
     * 半成品出库
     * @Author: mingchun.mu@mushiy.com
     */
    @Scheduled(fixedDelay=4000,initialDelay = 30000)
    @RequestMapping("getForEntity/carryPod")
    public void callLoadPod() {

        while (true){
            try {
                WorkOrder workOrder = workOrderContainer.removeFirst();
                if(workOrder != null && workOrder.isStock()){
                    String json = JSONUtil.toJSon(workOrder.getOutboundInstruct());
                    ResponseEntity<String> responseEntity = restTemplate.getForEntity(TestConstant.TEST_URL_PREFIX + "/outboundInstruct", String.class, json);
                    String res = responseEntity.getBody();
                }
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


}
