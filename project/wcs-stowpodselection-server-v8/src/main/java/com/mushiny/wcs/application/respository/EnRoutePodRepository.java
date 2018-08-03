package com.mushiny.wcs.application.respository;

import com.mushiny.wcs.application.domain.EnRoutePod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface EnRoutePodRepository extends JpaRepository<EnRoutePod,String> {

    @Query(" select count (e.id) from EnRoutePod e " +
            " where e.receiveStationId = :stationId or e.stowStationId = :stationId")
    long countByStationId(@Param("stationId") String stationId);

}
