package com.mushiny.wms.schedule.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.schedule.domin.Pod;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PodRepository extends BaseRepository<Pod,String> {

    @Query("select p from Pod p where p.name=:name and p.warehouseId=:warehouseId")
    Pod getByName(@Param("warehouseId") String warehouseId, @Param("name") String name);

    @Query("select p from Pod p where p.warehouseId=:warehouseId and p.sectionId=:sectionId and p.placeMark>0")
    List<Pod> getByWarehouse(@Param("warehouseId") String warehouseId, @Param("sectionId") String sectionId);

}
