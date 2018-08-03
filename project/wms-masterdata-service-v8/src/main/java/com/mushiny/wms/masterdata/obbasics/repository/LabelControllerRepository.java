package com.mushiny.wms.masterdata.obbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.obbasics.domain.LabelController;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LabelControllerRepository extends BaseRepository<LabelController, String> {

    @Query("select b from LabelController b " +
            " where b.warehouseId = :warehouse and b.name = :name")
    LabelController getByName(@Param("warehouse") String warehouse,
                              @Param("name") String name);

    @Query("select b.id from LabelController b  where b.id = :id")
    String getById(@Param("id") String id);
}
