package com.mushiny.wms.masterdata.mdbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.mdbasics.domain.SizeFilterRule;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by PC-2 on 2017/7/22.
 */
public interface SizeFilterRuleRepository extends BaseRepository<SizeFilterRule, String> {

    @Modifying
    @Query(" select s from SizeFilterRule s" +
            " where   s.name =:name and s.rule=:rule and s.entityLock=:entityLock")
    List<SizeFilterRule> byNameAndRule(@Param("name") String name,
                                       @Param("rule") String rule,
                                       @Param("entityLock") Integer entityLock);
}
