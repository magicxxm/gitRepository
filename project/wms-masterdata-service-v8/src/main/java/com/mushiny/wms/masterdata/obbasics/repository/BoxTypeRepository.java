package com.mushiny.wms.masterdata.obbasics.repository;


import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.obbasics.domain.BoxType;
import com.mushiny.wms.masterdata.general.domain.Client;
import com.mushiny.wms.masterdata.general.domain.Warehouse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoxTypeRepository extends BaseRepository<BoxType, String> {

    @Query("select b from BoxType b " +
            " where b.warehouseId = :warehouse and b.clientId = :client and b.name = :name")
    BoxType getByName(@Param("warehouse") String warehouse,
                      @Param("client") String client,
                      @Param("name") String name);

    @Query("select b from BoxType b " +
            " where b.warehouseId = :warehouse and b.clientId = :client and b.group= 'BOX'" +
            " order by b.volume asc")
    List<BoxType> getAllBoxAsc(@Param("warehouse") Warehouse warehouse,
                               @Param("client") Client client);

    @Query("select b from BoxType b " +
            " where b.warehouseId = :warehouse and b.clientId = :client and b.group= 'BAG'" +
            " order by b.volume asc")
    List<BoxType> getAllBagAsc(@Param("warehouse") Warehouse warehouse,
                               @Param("client") Client client);


    @Query(" select w from BoxType w " +
        " where w.entityLock = :entityLock " +
        " and not exists (" +
        " select 1 from PickPackCellType c " +
        " left join c.boxType cw " +
        " where cw.id = w.id " +
        " and c.id = :pickPackCellTypeId) " +
        " order by w.name")
    List<BoxType> getUnassignedReBinCellTypes(@Param("pickPackCellTypeId") String pickPackCellTypeId,
                                                       @Param("entityLock") Integer entityLock);
//    @Query(" select w from BoxType w " +
//            " where w.entityLock = :entityLock " +
//            " and not exists (" +
//            " select 1 from ReBinCellType c " +
//            " left join c.boxTypes cw " +
//            " where cw.id = w.id " +
//            " and c.id = :reBinCellTypeId) " +
//            " order by w.name")

//    @Query(" select u from BoxType u " +
//            " where u.entityLock = :entityLock " +
//            " and exists (" +
//            " select 1 from Warehouse w " +
//            " left join w.users wu " +
//            " where wu.id = u.id " +
//            " and w.id = :warehouseId " +
//            " and not exists (" +
//            " select 1 from PickPackCellType p " +
//            " left join p.boxTypes pu" +
//            " where pu.id = wu.id " +
//            " and p.id = :reBinCellTypeId )" +
//            " )" +
//            " order by u.name ")
//    List<BoxType> getUnassignedReBinCellTypes(@Param("warehouseId") String warehouseId,
//                                              @Param("reBinCellTypeId") String reBinCellTypeId,
//                                              @Param("entityLock") Integer entityLock);
}
