package com.mushiny.wcs.application.respository;

import com.mushiny.wcs.application.domain.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip,String> {


    @Query(" select count (t) from Trip t " +
            " where t.workStationId = :workStationId " +
            " and t.entityLock = 0 " +
            " and t.tripState in :tripStates")
    long countByWorkStationId(@Param("workStationId") String workStationId,
                              @Param("tripStates") List<String> tripStates);
    @Query("select tp from  Trip tp" +
            " where tp.workStationId = :workStationId " +
            " and tp.entityLock = 0 "
           )
    List<Trip> getAllTripByStation(@Param("workStationId") String workStationId);
    @Query("select tp from  Trip tp" +
            " where tp.podId = :podId and tp.tripType=:tripType " +
            " and tp.tripState<> 'Finish' "
    )
    Trip getTripByPodId(@Param("podId") String podId,@Param("tripType") String tripType);

    @Query("select tp from  Trip tp" +
            " where tp.podId = :podId" +
            " and tp.tripState<> 'Finish' "
    )
    List<Trip> getTripByPodAndNotFinish(@Param("podId") String podId);

}
