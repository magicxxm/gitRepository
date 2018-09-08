package com.mushiny.wms.application.repository;

import com.mushiny.wms.application.domain.WmsInvUnitload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by Administrator on 2018/7/9.
 */
public interface WmsInvUnitLoadRepository  extends JpaRepository<WmsInvUnitload, String>  {
    @Query(" select c from WmsInvUnitload c where c.podIndex=:podIndex and c.entityLock=:entityLock")
    WmsInvUnitload getUnitLoadByPodIndex(@Param("podIndex") Integer podIndex,@Param("entityLock") Integer entityLock);

}
