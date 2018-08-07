package com.mushiny.repository;

import com.mushiny.common.repository.BaseRepository;
import com.mushiny.model.BoxType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by 123 on 2018/2/5.
 */
public interface BoxTypeRepository extends BaseRepository<BoxType,String> {
    @Query("select b from BoxType b where b.name=:boxName and b.warehouseId=:warehouseId and b.clientId=:clientId")
    BoxType getByName(@Param("boxName")String name, @Param("warehouseId")String warehouseId, @Param("clientId")String clientId);

    @Query("select b from BoxType b where b.warehouseId = :warehouse and b.name = 'Ownbox'")
    BoxType getOwnBox(@Param("warehouse") String warehouse);

    @Query("select b from BoxType b " +
            " where b.warehouseId = :warehouse and b.clientId = :client and b.group= 'BOX'" +
            " order by b.volume asc")
    List<BoxType> getAllBoxAsc(@Param("warehouse") String warehouse,
                               @Param("client") String client);

    @Query("select b from BoxType b " +
            " where b.warehouseId = :warehouse and b.clientId = :client and b.group= 'BAG'" +
            " order by b.volume asc")
    List<BoxType> getAllBagAsc(@Param("warehouse") String warehouse,
                               @Param("client") String client);
}
