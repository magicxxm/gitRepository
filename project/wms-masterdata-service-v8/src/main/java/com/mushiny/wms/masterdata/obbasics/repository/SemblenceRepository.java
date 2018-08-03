package com.mushiny.wms.masterdata.obbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.obbasics.domain.Semblence;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by prestonmax on 2017/6/1.
 */
public interface SemblenceRepository extends BaseRepository<Semblence,String> {
    @Query("select s from Semblence s where s.entityLock=:entityLock")
    List<Semblence> getAll(@Param("entityLock")int entityLock);
    @Query("select s from Semblence s where s.clientId=:search ")
    Semblence getBySearch(@Param("search")String search);

    @Query("select s from Semblence s where s.id=:id ")
    Semblence getById(@Param("id")String id);

    @Query("select s from Semblence s where s.clientId=:clientId and s.semblence=:semblence ")
    Semblence getByClientIdAndSemblence(@Param("clientId")String clientId,@Param("semblence")int semblence);

    @Query("select s from Semblence s where s.clientId=:clientId")
    Semblence getByClientId(@Param("clientId")String clientId);
}
