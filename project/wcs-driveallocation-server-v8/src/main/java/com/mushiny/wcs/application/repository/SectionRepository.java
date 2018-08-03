package com.mushiny.wcs.application.repository;

import com.mushiny.wcs.application.domain.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, String> {

    @Query(" select s from Section s where s.entityLock = 0 order by s.warehouseId")
    List<Section> getAll();
}
