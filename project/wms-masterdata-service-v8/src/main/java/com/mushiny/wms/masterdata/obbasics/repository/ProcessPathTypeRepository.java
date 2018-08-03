package com.mushiny.wms.masterdata.obbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.obbasics.domain.ProcessPathType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProcessPathTypeRepository extends BaseRepository<ProcessPathType, String> {

    @Query("select p from ProcessPathType p where p.name = :name")
    ProcessPathType getByName(@Param("name") String name);
}
