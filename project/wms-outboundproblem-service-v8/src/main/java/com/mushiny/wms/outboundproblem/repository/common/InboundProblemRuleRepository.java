package com.mushiny.wms.outboundproblem.repository.common;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.outboundproblem.domain.common.InboundProblemRule;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InboundProblemRuleRepository extends BaseRepository<InboundProblemRule, String> {

    @Query("select a from InboundProblemRule a where a.name =:name ")
    InboundProblemRule getByName(@Param("name") String name);

}
