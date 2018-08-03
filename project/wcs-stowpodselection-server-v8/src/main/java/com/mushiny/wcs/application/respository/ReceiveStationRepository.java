package com.mushiny.wcs.application.respository;


import com.mushiny.wcs.application.domain.ReceiveStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReceiveStationRepository extends JpaRepository<ReceiveStation, String> {

    @Query(" select r from ReceiveStation r where r.operatorId is not null and r.operatorId <> ''")
    List<ReceiveStation> getAll();
}
