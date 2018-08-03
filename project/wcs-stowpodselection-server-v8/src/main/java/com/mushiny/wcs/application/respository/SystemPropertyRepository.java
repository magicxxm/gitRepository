package com.mushiny.wcs.application.respository;

import com.mushiny.wcs.application.domain.SystemProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SystemPropertyRepository extends JpaRepository<SystemProperty,String> {


    @Query(" select sp from SystemProperty sp " +
            " where sp.systemKey = :systemKey and sp.warehouseId = :warehouseId")
    SystemProperty getBySystemKey(@Param("systemKey")String systemKey,
                                  @Param("warehouseId")String warehouseId);
}
