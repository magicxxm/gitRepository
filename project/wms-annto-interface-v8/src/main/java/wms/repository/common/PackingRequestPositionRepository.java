package wms.repository.common;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wms.common.respository.BaseRepository;
import wms.domain.ItemData;
import wms.domain.common.CustomerShipmentPosition;
import wms.domain.common.PackingRequest;
import wms.domain.common.PackingRequestPosition;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 123 on 2017/5/17.
 */
public interface PackingRequestPositionRepository extends BaseRepository<PackingRequestPosition,String> {

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
