package com.mushiny.wms.outboundproblem.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.outboundproblem.domain.OBPShipmentSerialNo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OBPShipmentSerialNoRepository extends BaseRepository<OBPShipmentSerialNo, String> {

    @Query("select sn from OBPShipmentSerialNo sn where sn.warehouseId = :warehouseId and sn.shipmentId = :shipmentId " +
            "and sn.itemDataId = :itemDataId and sn.serialNo = :serialNo ")
    OBPShipmentSerialNo getByShipment(@Param("warehouseId") String warehouseId, @Param("shipmentId") String shipmentId,
                                      @Param("itemDataId") String itemDataId, @Param("serialNo") String serialNo);

    @Query("select sn from OBPShipmentSerialNo sn where sn.warehouseId = :warehouseId and sn.shipmentId = :shipmentId ")
    List<OBPShipmentSerialNo> getAllByShipment(@Param("warehouseId") String warehouseId, @Param("shipmentId") String shipmentId);

    @Query("select sn,cs from OBPShipmentSerialNo sn,CustomerShipment cs where sn.warehouseId = :warehouseId and sn.serialNo = :serialNo and sn.shipmentId=cs.id and cs.shipmentNo=:shipmentNo")
    OBPShipmentSerialNo getSerialNo(@Param("warehouseId") String warehouseId, @Param("serialNo") String serialNo,@Param("shipmentNo") String shipmentNo);

    @Query("select sn from OBPShipmentSerialNo sn where sn.warehouseId = :warehouseId and sn.serialNo = :serialNo")
    OBPShipmentSerialNo getSerialNoByName(@Param("warehouseId") String warehouseId, @Param("serialNo") String serialNo);

}
