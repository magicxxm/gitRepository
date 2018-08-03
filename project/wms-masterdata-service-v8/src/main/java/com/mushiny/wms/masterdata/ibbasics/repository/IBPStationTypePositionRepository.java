package com.mushiny.wms.masterdata.ibbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.ibbasics.domain.IBPStationType;
import com.mushiny.wms.masterdata.ibbasics.domain.IBPStationTypePosition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IBPStationTypePositionRepository extends BaseRepository<IBPStationTypePosition, String> {

    @Query(" select b from IBPStationTypePosition b " +
            " where b.ibpStationType = :ibpStationType  order by b.positionIndex")
    List<IBPStationTypePosition> getByIBPStationType(@Param("ibpStationType") IBPStationType ibpStationType);

}
