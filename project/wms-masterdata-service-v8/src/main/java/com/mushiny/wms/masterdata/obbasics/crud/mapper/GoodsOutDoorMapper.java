package com.mushiny.wms.masterdata.obbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.obbasics.crud.dto.GoodsOutDoorDTO;
import com.mushiny.wms.masterdata.obbasics.domain.GoodsOutDoor;
import com.mushiny.wms.masterdata.obbasics.repository.LabelControllerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GoodsOutDoorMapper implements BaseMapper<GoodsOutDoorDTO, GoodsOutDoor> {

    private final LabelControllerRepository labelControllerRepository;
    private final LabelControllerMapper labelControllerMapper;
    private final ApplicationContext applicationContext;

    @Autowired
    public GoodsOutDoorMapper(LabelControllerRepository labelControllerRepository,
                              LabelControllerMapper labelControllerMapper,
                              ApplicationContext applicationContext) {
        this.labelControllerRepository = labelControllerRepository;
        this.labelControllerMapper = labelControllerMapper;
        this.applicationContext = applicationContext;
    }

    @Override
    public GoodsOutDoorDTO toDTO(GoodsOutDoor entity) {
        if (entity == null) {
            return null;
        }

        GoodsOutDoorDTO dto = new GoodsOutDoorDTO(entity);

        dto.setName(entity.getName());
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setSortCode(entity.getSortCode());
        return dto;
    }

    @Override
    public GoodsOutDoor toEntity(GoodsOutDoorDTO dto) {
        if (dto == null) {
            return null;
        }

        GoodsOutDoor entity = new GoodsOutDoor();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setSortCode(dto.getSortCode());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());

        return entity;
    }

    @Override
    public void updateEntityFromDTO(GoodsOutDoorDTO dto, GoodsOutDoor entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setSortCode(dto.getSortCode());
    }
}

