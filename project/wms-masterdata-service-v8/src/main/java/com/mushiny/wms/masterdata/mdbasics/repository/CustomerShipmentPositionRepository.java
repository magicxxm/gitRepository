package com.mushiny.wms.masterdata.mdbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.mdbasics.domain.CustomerShipmentPosition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomerShipmentPositionRepository extends BaseRepository<CustomerShipmentPosition, String> {

    @Query(" select a from CustomerShipmentPosition a " +
            " where a.warehouseId=:warehouseId  and a.clientId=:clientId and " +
            "a.itemDataId = :itemDataId and a.createdDate <=:time and a.createdDate>:time1")
    List<CustomerShipmentPosition> getByItemDataId(@Param("warehouseId") String warehouseId,
                                                   @Param("clientId") String clientId,
                                                   @Param("itemDataId") String itemDataId,
                                                   @Param("time") LocalDateTime time1,
                                                   @Param("time1") LocalDateTime time2);

    @Query(" select a from CustomerShipmentPosition a " +
            " where a.createdDate <= :time and a.createdDate>:time1")
    List<CustomerShipmentPosition> getByTime(@Param("time") LocalDateTime time1,
                                             @Param("time1") LocalDateTime time2);

    @Query(" select a from CustomerShipmentPosition a " +
            " where a.clientId=:clientId and a.createdDate <= :time and a.createdDate>:time1")
    List<CustomerShipmentPosition> getByClient(@Param("clientId") String clientId,
                                               @Param("time") LocalDateTime time1,
                                               @Param("time1") LocalDateTime time2);

    @Query(" select a from CustomerShipmentPosition a " +
            " where a.warehouseId=:warehouseId and a.clientId=:clientId and a.createdDate <= :time and a.createdDate>:time1")
    List<CustomerShipmentPosition> getByTimeClientAndWarehouse(@Param("warehouseId") String warehouseId,
                                                               @Param("clientId") String clientId,
                                                               @Param("time") LocalDateTime time1,
                                                               @Param("time1") LocalDateTime time2);


}
