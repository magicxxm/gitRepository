package com.mushiny.wms.schedule.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.schedule.domin.Semblence;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SemblenceRepository extends BaseRepository<Semblence,String> {

    @Query("select s from Semblence s where s.clientId=:clientId")
    Semblence getByClientId(@Param("clientId") String clientId);
}
