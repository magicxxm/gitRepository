package com.mushiny.wcs.application.respository;


import com.mushiny.wcs.application.domain.StorageLocationType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StorageLocationTypeRepository extends JpaRepository<StorageLocationType, String> {

}
