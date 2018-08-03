package com.mushiny.wms.masterdata.ibbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.ibbasics.domain.ReceiveDestination;
import com.mushiny.wms.masterdata.ibbasics.domain.ReceiveStationType;
import com.mushiny.wms.masterdata.ibbasics.domain.ReceiveStationTypePosition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReceiveStationTypePositionRepository extends BaseRepository<ReceiveStationTypePosition, String> {

    long countByReceivingStationType(ReceiveStationType receivingStationType);

    @Query("select r from ReceiveStationTypePosition r " +
            " where r.receivingStationType = :receivingStationType and r.receivingDestination = :receivingDestination")
    ReceiveStationTypePosition getPosition(@Param("receivingStationType") ReceiveStationType receivingStationType,
                                           @Param("receivingDestination") ReceiveDestination receivingDestination);

    @Query("select r from ReceiveStationTypePosition r " +
            " where r.receivingStationType = :receivingStationType")
    List<ReceiveStationTypePosition> getPositions(
            @Param("receivingStationType") ReceiveStationType receivingStationType);

    @Query(" select b from ReceiveStationTypePosition b " +
            " where b.receivingStationType = :receiveStationType  order by b.positionIndex")
    List<ReceiveStationTypePosition> getByStowStationType(@Param("receiveStationType") ReceiveStationType receiveStationType);

    @Query(" select b from ReceiveStationTypePosition b " +
            " where b.receivingStationType= :receiveStationType  order by b.positionIndex")
    ReceiveStationTypePosition getByStationType(@Param("receiveStationType") ReceiveStationType receiveStationType);
}
