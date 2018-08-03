package com.mushiny.wms.masterdata.obbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.obbasics.crud.dto.DigitalLabelDTO;
import com.mushiny.wms.masterdata.obbasics.domain.DigitalLabel;
import com.mushiny.wms.masterdata.obbasics.repository.LabelControllerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DigitalLabelMapper implements BaseMapper<DigitalLabelDTO, DigitalLabel> {

    private final LabelControllerRepository labelControllerRepository;
    private final LabelControllerMapper labelControllerMapper;
    private final ApplicationContext applicationContext;

    @Autowired
    public DigitalLabelMapper(LabelControllerRepository labelControllerRepository,
                              LabelControllerMapper labelControllerMapper,
                              ApplicationContext applicationContext) {
        this.labelControllerRepository = labelControllerRepository;
        this.labelControllerMapper = labelControllerMapper;
        this.applicationContext = applicationContext;
    }

    @Override
    public DigitalLabelDTO toDTO(DigitalLabel entity) {
        if (entity == null) {
            return null;
        }

        DigitalLabelDTO dto = new DigitalLabelDTO(entity);

        dto.setName(entity.getName());
        dto.setNumOrder(entity.getNumOrder());
        dto.setLabelController(labelControllerMapper.toDTO(entity.getLabelController()));
        dto.setWarehouseId(entity.getWarehouseId());

        return dto;
    }

    @Override
    public DigitalLabel toEntity(DigitalLabelDTO dto) {
        if (dto == null) {
            return null;
        }

        DigitalLabel entity = new DigitalLabel();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setNumOrder(dto.getNumOrder());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        if (dto.getLabelControllerId() != null) {
            entity.setLabelController(labelControllerRepository.retrieve(dto.getLabelControllerId()));
        }
        return entity;
    }

    @Override
    public void updateEntityFromDTO(DigitalLabelDTO dto, DigitalLabel entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setNumOrder(dto.getNumOrder());
        if (dto.getLabelControllerId() != null) {
            entity.setLabelController(labelControllerRepository.retrieve(dto.getLabelControllerId()));
        }
    }
}

