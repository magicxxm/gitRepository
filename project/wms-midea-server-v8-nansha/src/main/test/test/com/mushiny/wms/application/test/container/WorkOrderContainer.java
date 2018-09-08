package test.com.mushiny.wms.application.test.container;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import test.com.mushiny.wms.application.test.bean.WorkOrder;
import test.com.mushiny.wms.application.test.domain.OutboundInstruct;
import test.com.mushiny.wms.application.test.repository.OutboundInstructRepository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * @program: wms-midea-server
 * @description: 工单号 管理器
 * @author: mingchun.mu@mushiny.com
 * @create: 2018-07-09 20:35
 **/
@Component
@Scope("singleton")
public class WorkOrderContainer {

    private final OutboundInstructRepository outboundInstructRepository;

    private Map<String, WorkOrder> container = new ConcurrentHashMap<>();

    @Autowired
    public WorkOrderContainer(OutboundInstructRepository outboundInstructRepository) {
        this.outboundInstructRepository = outboundInstructRepository;
        initInbound();
    }

    public WorkOrder removeFirst(){
        if(!CollectionUtils.isEmpty(container)){
            return container.remove(0);
        }
        return null;
    }
    public WorkOrder get(String id){
        return container.get(id);
    }
    public boolean contains(WorkOrder workOrder){
        if(workOrder == null){
            return false;
        }
        if(container.get(workOrder.getId()) != null){
            return true;
        }
        return false;
    }
    public void add(WorkOrder workOrder){
        if(workOrder == null){
            return;
        }
        if(contains(workOrder)){
            return;
        }
        container.put(workOrder.getId(), workOrder);
    }

    public boolean isEmpty(){
        return CollectionUtils.isEmpty(container);
    }

    public Map<String, WorkOrder> getContainer() {
        return container;
    }



    public void initInbound(){
        List<OutboundInstruct> outboundInstructList = outboundInstructRepository.getCreated();
        if(!CollectionUtils.isEmpty(outboundInstructList)){
            outboundInstructList.forEach(new Consumer<OutboundInstruct>() {
                @Override
                public void accept(OutboundInstruct outboundInstruct) {
                    WorkOrder workOrder = new WorkOrder(null, outboundInstruct);
                    add(workOrder);
                }
            });
        }
    }


}
