package com.mushiny.wms.outboundproblem.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.outboundproblem.domain.OBPCheckState;
import com.mushiny.wms.outboundproblem.domain.OBPSolve;
import com.mushiny.wms.outboundproblem.domain.OBProblem;
import com.mushiny.wms.outboundproblem.domain.OBProblemCheck;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OBPCheckStateRepository extends BaseRepository<OBPCheckState, String> {



}
