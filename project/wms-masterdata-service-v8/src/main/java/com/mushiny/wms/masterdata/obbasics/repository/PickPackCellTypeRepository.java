package com.mushiny.wms.masterdata.obbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.obbasics.domain.PickPackCellType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PickPackCellTypeRepository extends BaseRepository<PickPackCellType, String> {

    List<PickPackCellType> findByEntityLockOrderByName(Integer entityLock);

    @Query(" select a from WorkStationType a " +
            " where a.warehouseId = :warehouse and a.name = :name")
    PickPackCellType getByName(@Param("warehouse") String warehouse,
                              @Param("name") String name);

//    @Query(" select u from PickPackCellType u " +
//            " where u.entityLock = :entityLock " +
//            " and exists (" +
//            " select 1 from Warehouse w " +
//            " left join w.users wu " +
//            " where wu.id = u.id " +
//            " and w.id = :warehouseId " +
//            " and not exists (" +
//            " select 1 from BoxType p " +
//            " left join p.pickPackCellTypes pu" +
//            " where pu.id = wu.id " +
//            " and p.id = :reBinCellTypeId )" +
//            " )" +
//            " order by u.name ")
//
}
