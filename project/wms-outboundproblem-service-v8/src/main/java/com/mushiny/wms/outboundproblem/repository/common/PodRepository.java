package com.mushiny.wms.outboundproblem.repository.common;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.outboundproblem.domain.common.Pod;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PodRepository extends BaseRepository<Pod,String>{

    @Query("select p from Pod p where p.warehouseId=:warehouseId and p.name=:name")
    Pod getByName(@Param("warehouseId")String warehouseId,@Param("name") String name);
}
