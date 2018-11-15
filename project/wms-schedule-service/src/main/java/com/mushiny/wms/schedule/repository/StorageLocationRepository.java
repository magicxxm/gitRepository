package com.mushiny.wms.schedule.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.schedule.domin.Pod;
import com.mushiny.wms.schedule.domin.StorageLocation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StorageLocationRepository extends BaseRepository<StorageLocation,String> {
    @Query("select s from StorageLocation s where s.pod=:pod and s.face=:face order by s.xPos desc,s.yPos asc")
    List<StorageLocation> getByPod(@Param("pod") Pod pod, @Param("face") String face);


    @Query(" select s from StorageLocation s " +
            " where s.warehouseId = :warehouse and s.name =:name")
    StorageLocation getByName(@Param("warehouse") String warehouse,
                              @Param("name") String name);
}
