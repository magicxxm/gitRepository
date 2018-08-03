package com.mushiny.wcs.application.respository;

import com.mushiny.wcs.application.domain.TripPosition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripPositionRepository extends JpaRepository<TripPosition,String> {
}
