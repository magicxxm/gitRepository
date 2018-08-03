package com.mushiny.wms.tot.attendance.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.tot.attendance.crud.dto.AttendanceDTO;
import com.mushiny.wms.tot.attendance.crud.mapper.AttendanceMapper;
import com.mushiny.wms.tot.attendance.domain.Attendance;
import com.mushiny.wms.tot.attendance.repository.AttendanceRepository;
import com.mushiny.wms.tot.attendance.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Service
@Transactional
public class AttendanceServiceImpl implements AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final AttendanceMapper attendanceMapper;
    private final ApplicationContext applicationContext;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public AttendanceServiceImpl(AttendanceRepository attendanceRepository, AttendanceMapper attendanceMapper,
                                 ApplicationContext applicationContext) {
        this.attendanceRepository = attendanceRepository;
        this.attendanceMapper = attendanceMapper;
        this.applicationContext = applicationContext;
    }

    @Override
    public AttendanceDTO create(AttendanceDTO dto) {
        Attendance entity = attendanceMapper.toEntity(dto);
        return attendanceMapper.toDTO(attendanceRepository.save(entity));
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public AttendanceDTO update(AttendanceDTO dto) {
        return null;
    }

    @Override
    public AttendanceDTO retrieve(String id) {
        return null;
    }

    @Override
    public List<AttendanceDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<Attendance> entities = attendanceRepository.getBySearchTerm(searchTerm, sort);
        return attendanceMapper.toDTOList(entities);
    }

    @Override
    public Page<AttendanceDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        return null;
    }

    @Override
    public List<Attendance> findAttendanceListByEmployeeCode(String employeeCode,String currentWarehouseId) {
//        String currentWarehouseId = applicationContext.getCurrentWarehouse();
//        String sql ="SELECT NAME from SYS_WAREHOUSE WHERE ID = :currentWarehouseId";
//        Query query = entityManager.createNativeQuery(sql);
//        query.setParameter("currentWarehouseId", currentWarehouseId);
//        List<String> warehouseEntity = query.getResultList();
//        String currentWarehouseName = warehouseEntity.get(0);
        List<Attendance> attendances = attendanceRepository.findAttendanceListByEmployeeCode(employeeCode,currentWarehouseId);
        return  attendances;
    }
}
