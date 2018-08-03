package com.mushiny.wms.masterdata.mdbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.mdbasics.domain.Robot;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RobotRepository extends BaseRepository<Robot, String> {

    @Query(" select a from Robot a " +
            " where a.warehouseId = :warehouse  and a.robot = :robotId")
    Robot getByName(@Param("warehouse") String warehouse,
                   @Param("robotId") String robotId);

    @Query(" select a from Robot a " +
            " where a.robot = :robotId and a.password = :password")
    Robot getByEnter(@Param("robotId") String robotId,
                    @Param("password") String password);

}
