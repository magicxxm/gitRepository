package com.mushiny.wms.tot.attendance.crud.mapper;

import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.tot.attendance.crud.dto.AttendanceDTO;
import com.mushiny.wms.tot.attendance.domain.Attendance;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class AttendanceMapper implements BaseMapper<AttendanceDTO,Attendance> {

    @Override
    public AttendanceDTO toDTO(Attendance entity) {
        if (entity == null) {
            return null;
        }
        AttendanceDTO dto =new AttendanceDTO(entity);
        dto.setEmployeeCode(entity.getEmployeeCode());
        dto.setEmployeeName(entity.getEmployeeName());
        dto.setClockType(entity.getClockType());
        dto.setClockTime(entity.getClockTime());
        dto.setClockMethod(entity.getClockMethod());
        dto.setClientId(entity.getClientId());
        dto.setWarehouseId(entity.getWarehouseId());
        return dto;
    }
    public List<AttendanceDTO> toDTOList(List<Attendance> entitys) {
        List<AttendanceDTO> result=null;
        if (CollectionUtils.isEmpty(entitys)) {

            result=new ArrayList<AttendanceDTO>();

            for(Attendance entity:entitys)
            {
                AttendanceDTO dto =new AttendanceDTO(entity);
                dto.setEmployeeCode(entity.getEmployeeCode());
                dto.setEmployeeName(entity.getEmployeeName());
                dto.setClockType(entity.getClockType());
                dto.setClockTime(entity.getClockTime());
                dto.setClockMethod(entity.getClockMethod());
                dto.setClientId(entity.getClientId());
                dto.setWarehouseId(entity.getWarehouseId());
            }
        }

        return result;
    }

    @Override
    public Attendance toEntity(AttendanceDTO dto) {
        if (dto == null) {
            return null;
        }
        Attendance entity = new Attendance();
        entity.setEmployeeCode(dto.getEmployeeCode());
        entity.setEmployeeName(dto.getEmployeeName());
        entity.setClockType(dto.getClockType());
        entity.setClockTime(dto.getClockTime());
        entity.setClockMethod(dto.getClockMethod());
        entity.setClientId(dto.getClientId());
        entity.setWarehouseId(dto.getWarehouseId());
        return entity;
    }

    @Override
    public void updateEntityFromDTO(AttendanceDTO dto, Attendance entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setEmployeeCode(dto.getEmployeeCode());
        entity.setEmployeeName(dto.getEmployeeName());
        entity.setClockType(dto.getClockType());
        entity.setClockTime(dto.getClockTime());
        entity.setClockMethod(dto.getClockMethod());
        entity.setClientId(dto.getClientId());
        entity.setWarehouseId(dto.getWarehouseId());
    }
}
