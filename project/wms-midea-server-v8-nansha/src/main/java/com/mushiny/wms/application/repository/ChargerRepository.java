package com.mushiny.wms.application.repository;

import com.mushiny.wms.application.domain.Charger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChargerRepository extends JpaRepository<Charger, String> {

    @Query(" select c from Charger c where c.entityLock = 0 and  (c.chargerType=1 or c.chargerType=2 or c.chargerType=3 ) and c.sectionId=:sectionId and (c.state = 'Available' or c.state = 'Charging')")
    List<Charger> getAvailableChargers(@Param("sectionId") String sectionId);

    @Query(" select c from Charger c where c.entityLock = 0 and  (c.chargerType=1 or c.chargerType=2 or c.chargerType=3) and c.sectionId=:sectionId and c.placeMark=:plackMark")
    Charger getChargersByPlaceMark(@Param("sectionId") String sectionId, @Param("plackMark") int plackMark);
}
