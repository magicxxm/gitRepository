package com.mushiny.wcs.application.respository;

import com.mushiny.wcs.application.domain.StowStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StowStationRepository extends JpaRepository<StowStation, String> {

    @Query(" select s from StowStation s where s.operatorId is not null and s.operatorId <> ''")
    List<StowStation> getAll();
}
