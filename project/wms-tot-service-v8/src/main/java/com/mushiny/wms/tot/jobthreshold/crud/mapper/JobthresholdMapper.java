package com.mushiny.wms.tot.jobthreshold.crud.mapper;

import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.tot.jobthreshold.crud.dto.JobthresholdDTO;
import com.mushiny.wms.tot.jobthreshold.domain.Jobthreshold;
import org.springframework.stereotype.Component;

@Component
public class JobthresholdMapper implements BaseMapper<JobthresholdDTO, Jobthreshold> {
    @Override
    public JobthresholdDTO toDTO(Jobthreshold entity) {
        if (entity == null) {
            return null;
        }
        JobthresholdDTO dto = new JobthresholdDTO(entity);
        dto.setThresholdA(entity.getThresholdA());
        dto.setThresholdB(entity.getThresholdB());
        dto.setWarehouseId(entity.getWarehouseId());
        return dto;
    }

    @Override
    public Jobthreshold toEntity(JobthresholdDTO dto) {
        if (dto == null) {
            return null;
        }
        Jobthreshold entity =new Jobthreshold();
        entity.setThresholdA(dto.getThresholdA());
        entity.setThresholdB(dto.getThresholdB());
        entity.setWarehouseId(dto.getWarehouseId());
        return entity;
    }

    @Override
    public void updateEntityFromDTO(JobthresholdDTO dto, Jobthreshold entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setThresholdA(dto.getThresholdA());
        entity.setThresholdB(dto.getThresholdB());
//        entity.setWarehouseId(dto.getWarehouseId());
    }
}
