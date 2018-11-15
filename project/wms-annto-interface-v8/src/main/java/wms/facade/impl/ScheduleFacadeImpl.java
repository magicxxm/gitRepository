package wms.facade.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import wms.business.ScheduleBusiness;
import wms.constants.State;
import wms.domain.common.CustomerShipment;
import wms.facade.ScheduleFacade;
import wms.service.Shipment;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by 123 on 2017/12/21.
 */
@Component
public class ScheduleFacadeImpl implements ScheduleFacade {

    private final Logger log = LoggerFactory.getLogger(ScheduleFacadeImpl.class);

    private final ScheduleBusiness scheduleBusiness;
    private final EntityManager manager;
    private final Shipment shipmentService;

    @Autowired
    public ScheduleFacadeImpl(ScheduleBusiness scheduleBusiness,
                              EntityManager manager,
                              Shipment shipmentService){
        this.scheduleBusiness = scheduleBusiness;
        this.manager = manager;
        this.shipmentService = shipmentService;
    }


    @Override
//    @Scheduled(fixedDelay = 5000)
    public void splitOrder() {
        try {
            scheduleBusiness.splitCustomerOrder();
        } catch (Exception e) {
            log.error("拆单出现异常： startSplit >>>>>>>>>" + e.toString());
        }
    }


    /**
     * 定时查询已经完成拣货的订单
     */
    @Override
    public void confirmShipment() {
        CustomerShipment shipment = getShipmentList(State.PICKED,1);
        if(shipment == null){
            return;
        }
        shipmentService.confirmShipment(shipment);
    }

    private CustomerShipment getShipmentList(int state, int i) {
        Query query = manager.createQuery("select s from " +
                CustomerShipment.class.getSimpleName() +
                " s where s.state = :state order by s.deliveryDate");
        query.setMaxResults(i);
        query.setParameter("state",state);
        List<CustomerShipment> shipments = query.getResultList();
        if(shipments.isEmpty()){
            return null;
        }
        return shipments.get(0);
    }


}
