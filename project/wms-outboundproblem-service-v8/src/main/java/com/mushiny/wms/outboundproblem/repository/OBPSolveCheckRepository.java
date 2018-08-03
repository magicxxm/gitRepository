package com.mushiny.wms.outboundproblem.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.outboundproblem.domain.OBPSolveCheck;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OBPSolveCheckRepository extends BaseRepository<OBPSolveCheck, String> {

    @Query("SELECT a.storageLocation.id FROM OBPSolveCheck a where a.obProblem.id = :inboundProblemId ")
    List<String> getByStorageLocationId(@Param("inboundProblemId") String anDonInboundId);

    @Query("SELECT a.state FROM OBPSolveCheck a " +
            "where a.obProblem.id=:inboundProblemId " +
            "and a.storageLocation.id=:storageLocationId ")
    String getByState(@Param("inboundProblemId") String inboundProblemId,
                      @Param("storageLocationId") String storageLocationId);

//    @Query("FROM StorageLocation S WHERE S.id =:storageLocationId ")
//    StorageLocation getById(@Param("storageLocationId") String storageLocationId);
}
