package com.mushiny.wms.masterdata.obbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.general.repository.ClientRepository;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PickPackFieldTypeDTO;
import com.mushiny.wms.masterdata.obbasics.domain.PickPackFieldType;
import com.mushiny.wms.masterdata.obbasics.repository.PickPackCellTypeRepository;
import com.mushiny.wms.masterdata.obbasics.repository.PickPackWallTypeRepository;
import com.mushiny.wms.masterdata.obbasics.repository.ReBinCellTypeRepository;
import com.mushiny.wms.masterdata.obbasics.repository.ReBinWallTypeRepository;
import org.springframework.stereotype.Component;

@Component
public class PickPackFieldTypeMapper implements BaseMapper<PickPackFieldTypeDTO, PickPackFieldType> {

    private final ApplicationContext applicationContext;
    private final ClientRepository clientRepository;
    private final PickPackCellTypeMapper pickPackCellTypeMapper;
    private final PickPackCellTypeRepository pickPackCellTypeRepository;

    public PickPackFieldTypeMapper(ApplicationContext applicationContext,
                                   ClientRepository clientRepository,
                                   PickPackCellTypeMapper pickPackCellTypeMapper,
                                   PickPackCellTypeRepository pickPackCellTypeRepository) {
        this.applicationContext = applicationContext;
        this.clientRepository = clientRepository;
        this.pickPackCellTypeMapper = pickPackCellTypeMapper;
        this.pickPackCellTypeRepository = pickPackCellTypeRepository;
    }

    @Override
    public PickPackFieldTypeDTO toDTO(PickPackFieldType entity) {

        if (entity == null) {
            return null;
        }
        PickPackFieldTypeDTO dto = new PickPackFieldTypeDTO(entity);

        dto.setName(entity.getName());
        dto.setNumberOfColumns(entity.getNumberOfColumns());
        dto.setNumberOfRows(entity.getNumberOfRows());
        dto.setPickPackCellType(pickPackCellTypeMapper.toDTO(entity.getPickPackCellType()));
        dto.setWarehouseId(entity.getWarehouseId());

        return dto;
    }

    @Override
    public PickPackFieldType toEntity(PickPackFieldTypeDTO dto) {
        if (dto == null) {
            return null;
        }

        PickPackFieldType entity = new PickPackFieldType();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setNumberOfColumns(dto.getNumberOfColumns());
        entity.setNumberOfRows(dto.getNumberOfRows());

        if(dto.getPickPackCellTypeId() != null) {
            entity.setPickPackCellType(pickPackCellTypeRepository.retrieve(dto.getPickPackCellTypeId()));
        }
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());

        return entity;
    }

    @Override
    public void updateEntityFromDTO(PickPackFieldTypeDTO dto, PickPackFieldType entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setNumberOfColumns(dto.getNumberOfColumns());
        entity.setNumberOfRows(dto.getNumberOfRows());
        if(dto.getPickPackCellTypeId() != null) {
            entity.setPickPackCellType(pickPackCellTypeRepository.retrieve(dto.getPickPackCellTypeId()));
        }
    }

    public PickPackFieldTypeDTO toDTOList(PickPackFieldType entity) {

        if (entity == null) {
            return null;
        }
        PickPackFieldTypeDTO dto = new PickPackFieldTypeDTO(entity);

        dto.setName(entity.getName());
        dto.setNumberOfColumns(entity.getNumberOfColumns());
        dto.setNumberOfRows(entity.getNumberOfRows());
        dto.setWarehouseId(entity.getWarehouseId());

        return dto;
    }
}
