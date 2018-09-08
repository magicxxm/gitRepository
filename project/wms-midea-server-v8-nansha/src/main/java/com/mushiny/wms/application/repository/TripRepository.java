package com.mushiny.wms.application.repository;

import com.mushiny.wms.application.domain.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip, String> {


    @Query("select r from Trip r where r.id=:tripId")
    Trip getTripByID(@Param("tripId") String tripId);


    @Query("select r from Trip r where r.tripType='EmptyRun' and r.tripState<> 'Finish' and r.sectionId = :sectionId and  r.endAddress=:addressCodeId  ")
    List<Trip> getSleepAddrRobots(@Param("addressCodeId") int addressCodeId,
                                      @Param("sectionId") String sectionId);


    @Query(" select r from Trip r " +
            " where r.id =:tripId " +
            " and r.podId  in (" +
            "   select r2.podId from Trip r2" +
            "   where r2.tripState in :tripStates )")
    Trip getAvailableTrip(@Param("tripStates") List<String> tripStates,
                          @Param("tripId") String tripId);


    @Query(" select r from Trip r " +
            " where r.sectionId=:sectionId and r.warehouseId=:warehouseId" +
            " and r.tripType=:tripType and r.tripState in :tripStates"
           )
   List<Trip>  getWorkStationTrip(@Param("tripStates") List<String> tripStates,
                          @Param("sectionId") String sectionId, @Param("tripType") String tripType,
                                  @Param("warehouseId") String warehouseId
                                  );

    @Query(" select r from Trip r " +
            " where r.sectionId=:sectionId and r.warehouseId=:warehouseId and r.mdNodePosition=:mdNodePosition" +
            "  and r.tripState not in :tripStates"
    )
    List<Trip>  getWorkStationNotFinishTrip(@Param("tripStates") List<String> tripStates, @Param("sectionId") String sectionId,
    @Param("mdNodePosition") String    mdNodePosition  , @Param("warehouseId") String warehouseId
    );

    @Query(" select t from Trip t " +
            " where t.entityLock = 0 " +
            " and t.tripType in :tripTypes " +
            " and t.tripState = :tripState " +
            " and t.sectionId = :sectionId order by t.createdDate ")
    List<Trip> getAllNewTrips(@Param("tripTypes") List<String> tripTypes,
                              @Param("tripState") String tripState,
                              @Param("sectionId") String sectionId);
   /* @Query(" select t from Trip t " +
            " where t.entityLock = 0 " +
            " and t.tripType not in :tripTypes " +
            " and t.tripState = :tripState " +
            " and t.sectionId = :sectionId order by t.createdDate ")*/
   @Query(" select t from Trip t " +
           " where t.entityLock = 0 " +
           " and t.tripType not in :tripTypes " +
           " and t.tripState <>:tripState " +
           " and t.sectionId = :sectionId and t.driveId=:driveId")
    List<Trip> getAllNotChargeTrips(@Param("tripTypes") List<String> tripTypes,
                                    @Param("tripState") String tripState,
                                    @Param("sectionId") String sectionId,
                                    @Param("driveId") String driveId
                                    );
    @Query(" select t from Trip t " +
            " where t.entityLock = 0 " +
            " and t.tripType not in :tripTypes " +
            " and t.tripState = :tripState " +
            " and t.sectionId = :sectionId")
    List<Trip> getAllNotChargenNewTrips(@Param("tripTypes") List<String> tripTypes,
                                    @Param("tripState") String tripState,
                                    @Param("sectionId") String sectionId
    );
    @Query(" select t from Trip t " +
            " where t.entityLock = 0 " +
            " and t.tripType ='EmptyRun' and t.driveId=:driveId " +
            " and t.tripState in :tripState " +
            " and t.sectionId = :sectionId order by t.createdDate ")
    List<Trip> getAllEmptyRunTrips(@Param("driveId") String driveId,
                                    @Param("tripState") List<String> tripState,
                                    @Param("sectionId") String sectionId);

    @Query(" select t from Trip t " +
            " where t.entityLock = 0 " +
            " and t.tripState in :tripState and t.podId=:podId" +
            " and t.sectionId = :sectionId")
    List<Trip> getTripsByPodId(@Param("tripState") List<String> tripState, @Param("podId") String podId,
                               @Param("sectionId") String sectionId);

    @Query(" select t from Trip t where t.podId  in  " +
            "(select p.id from  Pod p where p.tarAddrcodeId=:addrcodeId and p.sectionId=:sectionId  )" +
            " and t.entityLock = 0 " +
            " and t.tripState = :tripState  " +
            " and t.sectionId = :sectionId ")
    List<Trip> getTripsAddrcodeId(@Param("tripState") List<String> tripState, @Param("addrcodeId") String addrcodeId,
                                  @Param("sectionId") String sectionId);

    @Query(" select t from Trip t " +
            " where t.chargerId = :chargerId and t.tripState in :tripStates")
    Trip getByChargerId(@Param("chargerId") String chargerId,
                        @Param("tripStates") List<String> tripStates);

    @Query(" select t from Trip t " +
            " where t.driveId = :driveId and  t.tripState in :tripStates")
    Trip getByRobotId(@Param("driveId") String driveId,
                      @Param("tripStates") List<String> tripStates);

    @Query(" select t from Trip t " +
            " where t.sectionId = :sectionId and t.driveId is not null and t.tripState in :tripStates")
    List<Trip> getAllocatedTrip(@Param("sectionId") String sectionId, @Param("tripStates") List<String> tripStates);

    @Query(" select t from Trip t " +
            " where t.sectionId = :sectionId and t.driveId=:driveId and t.tripState in :tripStates and t.tripType='ChargerDrive'")
    List<Trip> getRobotChargeTrip(@Param("sectionId") String sectionId,@Param("driveId") String driveId, @Param("tripStates") List<String> tripStates);
}
