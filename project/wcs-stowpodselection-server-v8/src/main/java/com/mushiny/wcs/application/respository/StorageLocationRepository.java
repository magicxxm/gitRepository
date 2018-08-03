package com.mushiny.wcs.application.respository;

import com.mushiny.wcs.application.domain.StorageLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface StorageLocationRepository extends JpaRepository<StorageLocation, String> {


}
