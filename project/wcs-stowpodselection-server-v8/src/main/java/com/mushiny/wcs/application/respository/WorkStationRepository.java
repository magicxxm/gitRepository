package com.mushiny.wcs.application.respository;


import com.mushiny.wcs.application.domain.WorkStation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkStationRepository extends JpaRepository<WorkStation, String> {

}
