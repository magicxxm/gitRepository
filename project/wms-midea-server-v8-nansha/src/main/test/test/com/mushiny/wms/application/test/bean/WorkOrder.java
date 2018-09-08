package test.com.mushiny.wms.application.test.bean;


import test.com.mushiny.wms.application.test.domain.InboundInstruct;
import test.com.mushiny.wms.application.test.domain.OutboundInstruct;

import java.util.UUID;

/**
 * @program: wms-midea-server
 * @description: 工单
 * @author: mingchun.mu@mushiny.com
 * @create: 2018-07-10 10:41
 **/
public class WorkOrder {
    private String id;
    private boolean isStock; // 是否库存中
    private Pod pod; // 工单绑定的物料在哪个货架上

    private final String uuid = UUID.randomUUID().toString();
    private InboundInstruct inboundInstruct;
    private OutboundInstruct outboundInstruct;

    public WorkOrder(InboundInstruct inboundInstruct, OutboundInstruct outboundInstruct) {
        if(inboundInstruct != null){
            this.inboundInstruct = inboundInstruct;
        }else {
            this.inboundInstruct  = new InboundInstruct();
            setInboundInstruct();
        }
        if(outboundInstruct != null){
            this.outboundInstruct = outboundInstruct;
        }else {
            this.outboundInstruct  = new OutboundInstruct();
            setOutboundInstruct();
        }
    }

    private void setInboundInstruct() {
        inboundInstruct.setINV_ORG_ID(uuid);
        inboundInstruct.setBILL_TYPE(uuid);
        inboundInstruct.setBILL_NO(uuid);
        inboundInstruct.setLABEL_NO(uuid);
        inboundInstruct.setINV_CODE(uuid);
        inboundInstruct.setMO_NAME(uuid);
        inboundInstruct.setASS_MO_NAME(uuid);
    }
    private void setOutboundInstruct() {
        outboundInstruct.setINV_ORG_ID(uuid);
        outboundInstruct.setBILL_TYPE(uuid);
        outboundInstruct.setBILL_NO(uuid);
        outboundInstruct.setLABEL_NO(uuid);
        outboundInstruct.setINV_CODE(uuid);
        outboundInstruct.setMO_NAME(uuid);
        outboundInstruct.setASS_MO_NAME(uuid);
    }

    public InboundInstruct getInboundInstruct() {
        return inboundInstruct;
    }

    public OutboundInstruct getOutboundInstruct() {
        return outboundInstruct;
    }

    public WorkOrder(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isStock() {
        return isStock;
    }

    public void setStock(boolean stock) {
        isStock = stock;
    }

    public Pod getPod() {
        return pod;
    }

    public void setPod(Pod pod) {
        this.pod = pod;
    }

    @Override
    public String toString() {
        return "WorkOrder{" +
                "id='" + id + '\'' +
                ", isStock=" + isStock +
                '}';
    }

    public boolean equals(WorkOrder workOrder) {
        if(workOrder == null){
            return false;
        }
        if(this.id.equals(workOrder.getId())){
            return true;
        }
        return false;
    }



}
