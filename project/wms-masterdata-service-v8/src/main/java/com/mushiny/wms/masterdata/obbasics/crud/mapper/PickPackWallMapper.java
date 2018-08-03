package com.mushiny.wms.masterdata.obbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.general.repository.ClientRepository;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PickPackWallDTO;
import com.mushiny.wms.masterdata.obbasics.domain.PickPackWall;
import com.mushiny.wms.masterdata.obbasics.repository.PickPackWallTypeRepository;
import org.springframework.stereotype.Component;

@Component
public class PickPackWallMapper implements BaseMapper<PickPackWallDTO, PickPackWall> {

    private final ApplicationContext applicationContext;
    private final ClientRepository clientRepository;
    private final PickPackWallTypeMapper pickPackWallTypeMapper;
    private final PickPackWallTypeRepository pickPackWallTypeRepository;

    public PickPackWallMapper(ApplicationContext applicationContext,
                              ClientRepository clientRepository,
                              PickPackWallTypeMapper pickPackWallTypeMapper,
                              PickPackWallTypeRepository pickPackWallTypeRepository) {
        this.applicationContext = applicationContext;
        this.clientRepository = clientRepository;
        this.pickPackWallTypeMapper = pickPackWallTypeMapper;
        this.pickPackWallTypeRepository = pickPackWallTypeRepository;
    }

    @Override
    public PickPackWallDTO toDTO(PickPackWall entity) {

        if (entity == null) {
            return null;
        }
        PickPackWallDTO dto = new PickPackWallDTO(entity);

        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setNumberOfColumns(entity.getNumberOfColumns());
        dto.setNumberOfRows(entity.getNumberOfRows());
        dto.setPickPackWallType(pickPackWallTypeMapper.toDTO(entity.getPickPackWallType()));
        dto.setWarehouseId(entity.getWarehouseId());

        return dto;
    }

    @Override
    public PickPackWall toEntity(PickPackWallDTO dto) {
        if (dto == null) {
            return null;
        }

        PickPackWall entity = new PickPackWall();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setNumberOfColumns(dto.getNumberOfColumns());
        entity.setNumberOfRows(dto.getNumberOfRows());
        if(dto.getTypeId() != null) {
            entity.setPickPackWallType(pickPackWallTypeRepository.retrieve(dto.getTypeId()));
        }
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());

        return entity;
    }

    @Override
    public void updateEntityFromDTO(PickPackWallDTO dto, PickPackWall entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setNumberOfColumns(dto.getNumberOfColumns());
        entity.setNumberOfRows(dto.getNumberOfRows());
        if(dto.getTypeId() != null) {
            entity.setPickPackWallType(pickPackWallTypeRepository.retrieve(dto.getTypeId()));
        }
    }
}
