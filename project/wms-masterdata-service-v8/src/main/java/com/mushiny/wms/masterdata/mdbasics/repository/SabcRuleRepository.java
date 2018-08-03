package com.mushiny.wms.masterdata.mdbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.mdbasics.domain.SabcRule;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SabcRuleRepository extends BaseRepository<SabcRule, String> {

    @Query(" select a from SabcRule a " +
            " where  a.skuTypeName = :skuTypeName and a.warehouseId=:warehouse")
    SabcRule getByName(@Param("skuTypeName") String skuTypeName, @Param("warehouse") String warehouse);

    @Query(" select a from SabcRule a where a.warehouseId=:warehouse order by a.fromNo desc ")
    List<SabcRule> getSmRule(@Param("warehouse") String warehouse);

    @Query(" select a from SabcRule a where a.warehouseId=:warehouse order by a.fromNo asc ")
    List<SabcRule> getMaxRule(@Param("warehouse") String warehouse);
}
