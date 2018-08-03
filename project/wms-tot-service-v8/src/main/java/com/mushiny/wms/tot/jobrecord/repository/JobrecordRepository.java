package com.mushiny.wms.tot.jobrecord.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.tot.jobrecord.domain.Jobrecord;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Created by Laptop-8 on 2017/6/8.
 */
public interface JobrecordRepository extends BaseRepository<Jobrecord,String> {

    @Query(" select r from Jobrecord r  " +
            " where (r.employeeCode = :employee or :employee='null') and (r.warehouseId = :warehouse or :warehouse='null') " +
            " and r.jobType = 'DIRECT' " +
            " and (r.recordTime between :startTime and :endTime or :startTime='null' or :endTime='null')" +
            " order by r.recordTime" )
    List<Jobrecord> getDetail(@Param("employee") String employeeCode, @Param("warehouse") String warehouseId,
                              @Param("startTime") String startTime, @Param("endTime") String endTime);

    @Query(" select r.tool,r.jobAction,r.unitType,r.size,sum (r.quantity),r.entityLock from Jobrecord r " +
            " where (r.employeeCode = :employee or :employee='null') and (r.warehouseId = :warehouse or :warehouse='null')" +
            " and r.jobType = 'DIRECT' " +
            " and (r.recordTime between :startTime and :endTime or :startTime='null' or :endTime='null')" +
            " and (r.entityLock = 0 or r.entityLock = 1)" +
            " group by r.tool,r.jobAction,r.unitType,r.size,r.entityLock" )
    List<Object[]> getTotal(@Param("employee") String employeeCode, @Param("warehouse") String warehouseId,
                            @Param("startTime") String startTime, @Param("endTime") String endTime);
}
