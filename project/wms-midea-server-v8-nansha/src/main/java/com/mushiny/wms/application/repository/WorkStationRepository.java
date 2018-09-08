package com.mushiny.wms.application.repository;


import com.mushiny.wms.application.domain.WorkStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WorkStationRepository extends JpaRepository<WorkStation, String> {


    /**
     * @param sectionId
     * @param typeId
     * @return
     */
    @Query(" select w from WorkStation w where w.typeId=:typeId and w.sectionId=:sectionId"
    )

    List<WorkStation> getAllWorkStation(@Param("sectionId") String sectionId,@Param("typeId") String typeId);

    @Query(" select w from WorkStation w where w.name=:workstationName"
    )
   WorkStation getWorkStationByName(@Param("workstationName") String workstationName);



}
