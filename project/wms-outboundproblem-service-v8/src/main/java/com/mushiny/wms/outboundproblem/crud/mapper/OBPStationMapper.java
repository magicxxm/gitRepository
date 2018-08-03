package com.mushiny.wms.outboundproblem.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.outboundproblem.crud.common.mapper.WarehouseMapper;
import com.mushiny.wms.outboundproblem.crud.dto.OBPStationDTO;
import com.mushiny.wms.outboundproblem.domain.OBPStation;
import com.mushiny.wms.outboundproblem.repository.OBPStationTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OBPStationMapper implements BaseMapper<OBPStationDTO, OBPStation> {

    private final OBPStationTypeRepository obpStationTypeRepository;
    private final ApplicationContext applicationContext;
    private final OBPStationTypeMapper obpStationTypeMapper;
    private final WarehouseMapper warehouseMapper;

    @Autowired
    public OBPStationMapper(OBPStationTypeRepository obpStationTypeRepository,
                            ApplicationContext applicationContext,
                            OBPStationTypeMapper obpStationTypeMapper,
                            WarehouseMapper warehouseMapper) {
        this.obpStationTypeRepository = obpStationTypeRepository;
        this.applicationContext = applicationContext;
        this.obpStationTypeMapper = obpStationTypeMapper;
        this.warehouseMapper = warehouseMapper;
    }

    @Override
    public OBPStationDTO toDTO(OBPStation entity) {
        if (entity == null) {
            return null;
        }
        OBPStationDTO dto = new OBPStationDTO(entity);

        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());

        dto.setWarehouse(entity.getWarehouseId());
        dto.setWorkStation(entity.getWorkStation());
        dto.setObpStationType(obpStationTypeMapper.toDTO(entity.getObpStationType()));

        return dto;
    }

    @Override
    public OBPStation toEntity(OBPStationDTO dto) {
        if (dto == null) {
            return null;
        }
        OBPStation entity = new OBPStation();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setWorkStation(dto.getWorkStation());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        if (dto.getTypeId() != null) {
            entity.setObpStationType(obpStationTypeRepository.retrieve(dto.getTypeId()));
        }

        return entity;
    }

    @Override
    public void updateEntityFromDTO(OBPStationDTO dto, OBPStation entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());

        if (dto.getTypeId() != null) {
            entity.setObpStationType(obpStationTypeRepository.retrieve(dto.getTypeId()));
        } else {
            entity.setObpStationType(null);
        }
    }
}
