package com.mushiny.repository;

import com.mushiny.common.repository.BaseRepository;
import com.mushiny.model.WdSection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by 123 on 2017/8/11.
 */
public interface SectionRepository extends BaseRepository<WdSection,String> {

    @Query("select s from WdSection s where s.warehouseId=:warehouseId")
    List<WdSection> getByWarehouseId(@Param("warehouseId") String warehouseId);
}
