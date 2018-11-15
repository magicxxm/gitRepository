package com.mushiny.wms.schedule.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.schedule.domin.StorageLocationType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StorageLocationTypeRepository extends BaseRepository<StorageLocationType,String> {

    @Query("select s from StorageLocationType s where s.storageType=:name")
    List<StorageLocationType> getByName(@Param("name") String name);

}
