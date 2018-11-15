package wms.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import wms.business.dto.CydhDTO;
import wms.constants.State;
import wms.crud.dto.ShipmentToAnntoDTO;
import wms.domain.AnntoCustomerOrder;
import wms.domain.CustomerOrder;
import wms.domain.common.CustomerShipment;
import wms.repository.AnntoCustomerOrderRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by 123 on 2017/9/19.
 */
@Component
@Transactional
public class ScheduleBusiness {
    private final Logger log = LoggerFactory.getLogger(ScheduleBusiness.class);

    private final EntityManager manager;
    private final OrderBusiness orderBusiness;
    private final AnntoCustomerOrderRepository anntoCustomerOrderRepository;
    private final ShipmentRecordBusiness shipmentRecordBusiness;
    private final AnntoBusiness anntoBusiness;

    @Autowired
    public ScheduleBusiness(OrderBusiness orderBusiness,
                            EntityManager manager,
                            AnntoCustomerOrderRepository anntoCustomerOrderRepository,
                            ShipmentRecordBusiness shipmentRecordBusiness,
                            AnntoBusiness anntoBusiness){
        this.manager = manager;
        this.orderBusiness = orderBusiness;
        this.anntoCustomerOrderRepository = anntoCustomerOrderRepository;
        this.shipmentRecordBusiness = shipmentRecordBusiness;
        this.anntoBusiness = anntoBusiness;
    }

    //定时拆分订单
    public void splitCustomerOrder() {
        //获取所有未拆分的订单
        int i = 0;
        int j = 0;
        while(i < 1000 - j) {
            List<CustomerOrder> customerOrders = getOrders(State.RELEASED,i);
            if (customerOrders.isEmpty()) {
                return;
            }
            for (CustomerOrder order : customerOrders) {
                int result = split(order);
                if(result == 1){
                    j++;
                }
            }
            log.info("当前查询起始点 =========== >>>" + i);
            log.info("当前查询订单结果 拆分订单的数量 ------------>>>" + j);
            i = i+10;
        }
    }

    private List<CustomerOrder> getOrders(int released,int i) {
        String sql = "SELECT O FROM " + CustomerOrder.class.getSimpleName() + " O where O.state < :state order by O.createdDate";

        Query query = manager.createQuery(sql);
        query.setParameter("state",released);
        query.setFirstResult(i);
        query.setMaxResults(10);

        List<CustomerOrder> customerOrders = query.getResultList();

        return customerOrders;
    }

    private int split(CustomerOrder customerOrder){

        List<CustomerShipment> shipments =  orderBusiness.splitCustomerOrder(customerOrder);
        if(shipments == null || shipments.isEmpty()){
            log.error("出库单："+customerOrder.getOrderNo() +" 拆分订单失败。。。");
            return 0;
        }

        AnntoCustomerOrder dto = anntoCustomerOrderRepository.getByCode(customerOrder.getOrderNo());
        ShipmentToAnntoDTO shipmentToAnntoDTO = new ShipmentToAnntoDTO();
        shipmentToAnntoDTO.setCompanyCode(dto.getCompanyCode());
        shipmentToAnntoDTO.setCarrierCode(dto.getCarrierCode());
        shipmentToAnntoDTO.setWarehouseCode(dto.getWarehouseCode());

        String shipmentCode = "";
        for (CustomerShipment shipment:shipments) {
            shipmentCode = shipmentCode + shipment.getShipmentNo() + ",";
        }
        shipmentToAnntoDTO.setShipmentCode(shipmentCode);

        //获取承运单号
        log.info("订单:"+customerOrder.getOrderNo()+" 开始获取承运单号。。");
        List<CydhDTO> cydhDTOS = anntoBusiness.acceptPrimaryWallBillCode(shipmentToAnntoDTO);
        if(cydhDTOS.isEmpty()){
            log.error("出库单："+dto.getCode()+" 获取承运单号失败。。。");
            return 0;
        }
        log.info("订单:"+customerOrder.getOrderNo()+" 获取承运单号成功！！");
        for (CustomerShipment shipment:shipments) {
            shipment.setCarrierNo("00" + customerOrder.getOrderNo());
            for (CydhDTO c:cydhDTOS) {
                if(shipment.getShipmentNo().equalsIgnoreCase(c.getCompanyCode())){
                    shipment.setCarrierNo(c.getPrimaryWaybillCode());
                }
            }
        }
        //创建shipment历史记录
        for (CustomerShipment shipment:shipments) {
            shipmentRecordBusiness.getShipmentRecord(shipment,null,null,"RAW");
        }
        customerOrder.setState(State.RELEASED);
        return 1;
    }

}
