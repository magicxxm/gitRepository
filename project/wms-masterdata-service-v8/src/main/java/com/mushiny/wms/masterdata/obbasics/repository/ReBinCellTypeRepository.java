package com.mushiny.wms.masterdata.obbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.obbasics.domain.ReBinCellType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReBinCellTypeRepository extends BaseRepository<ReBinCellType, String> {

    List<ReBinCellType> findByEntityLockOrderByName(Integer entityLock);

    @Query("select r from ReBinCellType r where r.warehouseId = :warehouse and r.name = :name")
    ReBinCellType getByName(@Param("warehouse") String warehouse, @Param("name") String name);
}
