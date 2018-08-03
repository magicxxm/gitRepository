package com.mushiny.wms.masterdata.ibbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.ibbasics.domain.ReceiveStation;
import com.mushiny.wms.masterdata.ibbasics.domain.ReceiveStationPosition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReceiveStationPositionRepository extends BaseRepository<ReceiveStationPosition, String> {
    @Query(" select r from ReceiveStationPosition r where r.receiveStation.id = :id ")
    List<ReceiveStationPosition> getByReceiveStationId(@Param("id") String id);

}
