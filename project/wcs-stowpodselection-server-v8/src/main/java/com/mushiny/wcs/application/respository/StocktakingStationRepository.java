package com.mushiny.wcs.application.respository;

import com.mushiny.wcs.application.domain.StocktakingStation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StocktakingStationRepository extends JpaRepository<StocktakingStation,String> {


}
