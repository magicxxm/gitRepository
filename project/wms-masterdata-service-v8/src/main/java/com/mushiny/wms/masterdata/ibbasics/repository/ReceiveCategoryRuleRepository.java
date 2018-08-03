package com.mushiny.wms.masterdata.ibbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.ibbasics.domain.ReceiveCategoryRule;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReceiveCategoryRuleRepository extends BaseRepository<ReceiveCategoryRule, String> {

    @Query("select r from ReceiveCategoryRule r where r.name = :name")
    ReceiveCategoryRule getByName(@Param("name") String name);
}
