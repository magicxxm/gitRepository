package com.mushiny.wms.schedule.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.schedule.domin.CustomerShipmentPosition;
import com.mushiny.wms.schedule.domin.ItemData;
import com.mushiny.wms.schedule.domin.PackingRequest;
import com.mushiny.wms.schedule.domin.PackingRequestPosition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 123 on 2017/5/17.
 */
public interface PackingRequestPositionRepository extends BaseRepository<PackingRequestPosition, String> {

    @Query(" select coalesce(sum (p.amount),0) from PackingRequestPosition p " +
            " left join p.customerShipmentPosition cp " +
            " left join cp.customerShipment c " +
            " where c.shipmentNo = :shipmentNo and p.itemData = :itemData")
    BigDecimal sumByShipmentNoAndItemData(@Param("shipmentNo") String shipmentNo,
                                          @Param("itemData") ItemData itemData);

    @Query("select coalesce(sum (p.amountPacked),0) from PackingRequestPosition p " +
            " where p.customerShipmentPosition = :shipmentPosition")
    BigDecimal sumByShipmentPosition(@Param("shipmentPosition") CustomerShipmentPosition shipmentPosition);

    @Query("select p from PackingRequestPosition p where p.packingRequest=:request")
    List<PackingRequestPosition> getByPackingRequest(@Param("request") PackingRequest request);

    @Query("select prp from PackingRequestPosition prp where prp.customerShipmentPosition=:shipmentPosition and prp.itemData=:itemData")
    List<PackingRequestPosition> getByCustomerShipmentPositionAndItemData(@Param("shipmentPosition") CustomerShipmentPosition shipmentPosition, @Param("itemData") ItemData itemData);
}
