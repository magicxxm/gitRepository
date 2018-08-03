package com.mushiny.wms.tot.attendance.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.tot.attendance.domain.Attendance;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Laptop-8 on 2017/6/8.
 */
public interface AttendanceRepository extends BaseRepository<Attendance,String> {
    @Query(value = " select a from Attendance a where a.employeeCode = :employeeCode order by clockTime desc limit = :num  ",nativeQuery=true)
    Attendance  findAttendanceByEmployeeCode(@Param("employeeCode")String employeeCode,@Param("num")Integer num);
    @Query(" select a from Attendance a where a.employeeCode = :employeeCode and " +
            " a.warehouseId = :warehouseId order by a.clockTime desc " )
    List<Attendance> findAttendanceListByEmployeeCode(@Param("employeeCode")String employeeCode,
                                                      @Param("warehouseId")String warehouseId);


}
