package com.mushiny.wms.masterdata.mdbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.mdbasics.domain.Map;
import com.mushiny.wms.masterdata.mdbasics.domain.Section;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SectionRepository extends BaseRepository<Section, String> {

    @Query(" select b from Section b " +
            " where b.warehouseId = :warehouse")
    List<Section> getByWarehouse(@Param("warehouse") String warehouse);

    @Query(" select a from Section a " +
            " where a.warehouseId = :warehouse  and a.name = :name")
    Section getByName(@Param("name") String name, @Param("warehouse") String warehouse);

}
