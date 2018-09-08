package com.mushiny.wms.application.repository;

import com.mushiny.wms.application.domain.SystemProperty;
import com.mushiny.wms.application.domain.WmsInvStockunit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by Administrator on 2018/7/9.
 */
public interface WmsInvStockunitRepository extends JpaRepository<WmsInvStockunit, String> {
    @Query(" select c from WmsInvStockunit c where c.unitloadId=:unitloadId and c.entityLock=:entityLock")
    WmsInvStockunit getStockUnitByUnitLoadId(@Param("unitloadId") String unitloadId,@Param("entityLock") Integer entityLock);
}
