package com.mushiny.wms.masterdata.mdbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.mdbasics.domain.Area;
import com.mushiny.wms.masterdata.mdbasics.domain.ChargingPile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChargingPileRepository extends BaseRepository<ChargingPile, String> {

    @Query(" select c from ChargingPile c " +
            " where c.warehouseId = :warehouse  and c.name = :name ")
    ChargingPile getByName(@Param("warehouse") String warehouse,
                           @Param("name") String name);
    @Query(" select c from ChargingPile c " +
            " where c.chargerId = :chargerId  and c.chargerType = :chargerType ")
    ChargingPile getById(@Param("chargerId") int chargerId,
                           @Param("chargerType") int chargerType);
}
