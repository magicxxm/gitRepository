package com.mushiny.wms.tot.jobthreshold.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.tot.jobrecord.domain.Jobrecord;
import com.mushiny.wms.tot.jobthreshold.domain.Jobthreshold;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Laptop-8 on 2017/6/8.
 */
public interface JobthresholdRepository extends BaseRepository<Jobthreshold,String> {

    @Query(" select t from Jobthreshold t where t.warehouseId = :warehouseId")
    Jobthreshold getJobthreshold(@Param("warehouseId") String warehouseId);

    @Query(" select t from Jobthreshold t where t.warehouseId = :warehouseId")
    Jobthreshold getbyWarehouseId(@Param("warehouseId") String warehouseId);

}

