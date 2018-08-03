package com.mushiny.wms.masterdata.ibbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceiveDestinationDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.ReceiveDestination;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.AreaMapper;
import com.mushiny.wms.masterdata.mdbasics.repository.AreaRepository;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReceiveDestinationMapper implements BaseMapper<ReceiveDestinationDTO, ReceiveDestination> {

    private final AreaRepository areaRepository;
    private final ApplicationContext applicationContext;
    private final AreaMapper areaMapper;
    private final WarehouseMapper warehouseMapper;

    @Autowired
    public ReceiveDestinationMapper(ApplicationContext applicationContext,
                                    WarehouseMapper warehouseMapper,
                                    AreaRepository areaRepository,
                                    AreaMapper areaMapper) {
        this.applicationContext = applicationContext;
        this.warehouseMapper = warehouseMapper;
        this.areaRepository = areaRepository;
        this.areaMapper = areaMapper;
    }

    @Override
    public ReceiveDestinationDTO toDTO(ReceiveDestination entity) {
        if (entity == null) {
            return null;
        }
        ReceiveDestinationDTO dto = new ReceiveDestinationDTO(entity);

        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());

//        dto.setWarehouse(warehouseMapper.toDTO(entity.getWarehouse()));
        dto.setArea(areaMapper.toDTO(entity.getArea()));
        dto.setWarehouseId(entity.getWarehouseId());

        return dto;
    }

    @Override
    public ReceiveDestination toEntity(ReceiveDestinationDTO dto) {
        if (dto == null) {
            return null;
        }
        ReceiveDestination entity = new ReceiveDestination();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        if (dto.getAreaId() != null) {
            entity.setArea(areaRepository.retrieve(dto.getAreaId()));
        }

        return entity;
    }

    @Override
    @SuppressWarnings("Duplicates")
    public void updateEntityFromDTO(ReceiveDestinationDTO dto, ReceiveDestination entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());

        if (dto.getAreaId() != null) {
            entity.setArea(areaRepository.retrieve(dto.getAreaId()));
        } else {
            entity.setArea(null);
        }
    }
}
