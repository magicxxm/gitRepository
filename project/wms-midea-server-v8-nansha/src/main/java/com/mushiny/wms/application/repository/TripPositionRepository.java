package com.mushiny.wms.application.repository;

import com.mushiny.wms.application.domain.TripPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TripPositionRepository extends JpaRepository<TripPosition, String> {


}
