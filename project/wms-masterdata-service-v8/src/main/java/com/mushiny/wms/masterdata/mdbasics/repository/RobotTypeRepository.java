package com.mushiny.wms.masterdata.mdbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.mdbasics.domain.BatterConfig;
import com.mushiny.wms.masterdata.mdbasics.domain.RobotType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RobotTypeRepository extends BaseRepository<RobotType, String> {

}
