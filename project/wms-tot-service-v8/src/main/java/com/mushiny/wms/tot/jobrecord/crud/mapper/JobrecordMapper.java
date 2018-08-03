package com.mushiny.wms.tot.jobrecord.crud.mapper;

import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.tot.jobrecord.crud.dto.JobrecordDTO;
import com.mushiny.wms.tot.jobrecord.domain.Jobrecord;
import org.springframework.stereotype.Component;

@Component
public class JobrecordMapper implements BaseMapper<JobrecordDTO,Jobrecord> {

    @Override
    public JobrecordDTO toDTO(Jobrecord entity) {
        if (entity == null) {
            return null;
        }
        JobrecordDTO dto =new JobrecordDTO(entity);
        dto.setRecordTime(entity.getRecordTime());
        dto.setEmployeeCode(entity.getEmployeeCode());
        dto.setEmployeeName(entity.getEmployeeName());
        dto.setTool(entity.getTool());
        dto.setJobAction(entity.getJobAction());
        dto.setJobCode(entity.getJobCode());
        dto.setJobName(entity.getJobName());
        dto.setJobType(entity.getJobType());
        dto.setDescription(entity.getDescription());
        dto.setSkuNo(entity.getSkuNo());
        dto.setUnitType(entity.getUnitType());
        dto.setSize(entity.getSize());
        dto.setQuantity(entity.getQuantity());
        dto.setFromStoragelocation(entity.getFromStoragelocation());
        dto.setToStoragelocation(entity.getToStoragelocation());
        dto.setClientId(entity.getClientId());
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setNewBarcode(entity.getNewBarcode());
        dto.setIndirectType(entity.getIndirectType());
        dto.setOperation(entity.getOperation());
        dto.setCategoryName(entity.getCategoryName());
        dto.setShipmentNo(entity.getShipmentNo());
        return dto;
    }

    @Override
    public Jobrecord toEntity(JobrecordDTO dto) {
        if (dto == null) {
            return null;
        }
        Jobrecord entity = new Jobrecord();
        entity.setRecordTime(dto.getRecordTime());
        entity.setEmployeeCode(dto.getEmployeeCode());
        entity.setEmployeeName(dto.getEmployeeName());
        entity.setTool(dto.getTool());
        entity.setJobAction(dto.getJobAction());
        entity.setJobCode(dto.getJobCode());
        entity.setJobName(dto.getJobName());
        entity.setJobType(dto.getJobType());
        entity.setDescription(dto.getDescription());
        entity.setSkuNo(dto.getSkuNo());
        entity.setUnitType(dto.getUnitType());
        entity.setSize(dto.getSize());
        entity.setQuantity(dto.getQuantity());
        entity.setFromStoragelocation(dto.getFromStoragelocation());
        entity.setToStoragelocation(dto.getToStoragelocation());
        entity.setClientId(dto.getClientId());
        entity.setWarehouseId(dto.getWarehouseId());
        entity.setNewBarcode(dto.getNewBarcode());
        entity.setIndirectType(dto.getIndirectType());
        entity.setOperation(dto.getOperation());
        entity.setCategoryName(dto.getCategoryName());
        entity.setShipmentNo(dto.getShipmentNo());
        return entity;
    }

    @Override
    public void updateEntityFromDTO(JobrecordDTO dto, Jobrecord entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setRecordTime(dto.getRecordTime());
        entity.setEmployeeCode(dto.getEmployeeCode());
        entity.setEmployeeName(dto.getEmployeeName());
        entity.setTool(dto.getTool());
        entity.setJobAction(dto.getJobAction());
        entity.setJobCode(dto.getJobCode());
        entity.setJobName(dto.getJobName());
        entity.setJobType(dto.getJobType());
        entity.setDescription(dto.getDescription());
        entity.setSkuNo(dto.getSkuNo());
        entity.setUnitType(dto.getUnitType());
        entity.setSize(dto.getSize());
        entity.setQuantity(dto.getQuantity());
        entity.setFromStoragelocation(dto.getFromStoragelocation());
        entity.setToStoragelocation(dto.getToStoragelocation());
        entity.setClientId(dto.getClientId());
        entity.setWarehouseId(dto.getWarehouseId());
        entity.setNewBarcode(dto.getNewBarcode());
        entity.setIndirectType(dto.getIndirectType());
        entity.setOperation(dto.getOperation());
        entity.setCategoryName(dto.getCategoryName());
        entity.setShipmentNo(dto.getShipmentNo());
    }
}
