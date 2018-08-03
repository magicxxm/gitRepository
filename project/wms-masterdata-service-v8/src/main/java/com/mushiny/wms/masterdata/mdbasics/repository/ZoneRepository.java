package com.mushiny.wms.masterdata.mdbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.mdbasics.domain.Zone;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ZoneRepository extends BaseRepository<Zone, String> {

    @Query(" select z from Zone z " +
            " where z.warehouseId = :warehouse and z.clientId = :client and z.name =:name")
    Zone getByName(@Param("warehouse") String warehouse,
                   @Param("client") String client,
                   @Param("name") String name);

    @Query(" select z from Zone z " +
            " where z.warehouseId = :warehouse and z.clientId = :client and  z.sectionId =:sectionId order by z.name ")
    List<Zone> getByClientIdAndSectionId(@Param("warehouse") String warehouse,
                                         @Param("client") String client,
                                         @Param("sectionId") String sectionId);
}
