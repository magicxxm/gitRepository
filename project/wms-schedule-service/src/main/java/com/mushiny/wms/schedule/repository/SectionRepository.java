package com.mushiny.wms.schedule.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.schedule.domin.WdSection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SectionRepository extends BaseRepository<WdSection,String> {

    @Query("select s from WdSection s where s.warehouseId=:warehouseId")
    List<WdSection> getByWarehouseId(@Param("warehouseId") String warehouseId);
}
