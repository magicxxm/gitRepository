package com.mushiny.wms.masterdata.mdbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.general.repository.ClientRepository;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.WorkStationTypeDTO;
import com.mushiny.wms.masterdata.mdbasics.domain.WorkStationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WorkStationTypeMapper implements BaseMapper<WorkStationTypeDTO, WorkStationType> {

    private final ApplicationContext applicationContext;
    private final ClientRepository clientRepository;

    @Autowired
    public WorkStationTypeMapper(ApplicationContext applicationContext,
                                 ClientRepository clientRepository) {
        this.applicationContext = applicationContext;
        this.clientRepository = clientRepository;
    }

    @Override
    public WorkStationTypeDTO toDTO(WorkStationType entity) {
        if (entity == null) {
            return null;
        }

        WorkStationTypeDTO dto = new WorkStationTypeDTO(entity);

        dto.setName(entity.getName());

        dto.setWarehouseId(entity.getWarehouseId());

        return dto;
    }


    @Override
    public WorkStationType toEntity(WorkStationTypeDTO dto) {
        if (dto == null) {
            return null;
        }

        WorkStationType entity = new WorkStationType();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());

        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        return entity;
    }

    @Override
    public void updateEntityFromDTO(WorkStationTypeDTO dto, WorkStationType entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
    }
}

