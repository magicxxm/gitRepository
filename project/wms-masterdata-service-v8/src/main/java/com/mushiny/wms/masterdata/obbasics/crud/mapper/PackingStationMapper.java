package com.mushiny.wms.masterdata.obbasics.crud.mapper;

import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.masterdata.general.crud.mapper.UserMapper;
import com.mushiny.wms.masterdata.general.repository.UserRepository;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.WorkStationMapper;
import com.mushiny.wms.masterdata.mdbasics.repository.WorkStationRepository;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PackingStationDTO;
import com.mushiny.wms.masterdata.obbasics.domain.PackingStation;
import com.mushiny.wms.masterdata.obbasics.repository.PackingStationTypeRepository;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import org.springframework.stereotype.Component;

@Component
public class PackingStationMapper implements BaseMapper<PackingStationDTO, PackingStation> {

    private final PackingStationTypeRepository packingStationTypeRepository;
    private final PackingStationTypeMapper packingStationTypeMapper;
    private final ApplicationContext applicationContext;
    private final WarehouseMapper warehouseMapper;
    private final WorkStationMapper workStationMapper;
    private final WorkStationRepository workStationRepository;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public PackingStationMapper(PackingStationTypeRepository packingStationTypeRepository,
                                PackingStationTypeMapper packingStationTypeMapper,
                                ApplicationContext applicationContext,
                                WarehouseMapper warehouseMapper,
                                WorkStationMapper workStationMapper,
                                WorkStationRepository workStationRepository,
                                UserMapper userMapper,
                                UserRepository userRepository) {
        this.packingStationTypeRepository = packingStationTypeRepository;
        this.packingStationTypeMapper = packingStationTypeMapper;
        this.applicationContext = applicationContext;
        this.warehouseMapper = warehouseMapper;
        this.workStationMapper = workStationMapper;
        this.workStationRepository = workStationRepository;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    @Override
    public PackingStationDTO toDTO(PackingStation entity) {
        if (entity == null) {
            return null;
        }
        PackingStationDTO dto = new PackingStationDTO(entity);

        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());

        dto.setWarehouseId(entity.getWarehouseId());
        dto.setPackingStationType(packingStationTypeMapper.toDTO(entity.getPackingStationType()));
        dto.setUser(userMapper.toDTO(entity.getOperator()));
        dto.setWorkstation(workStationMapper.toDTO(entity.getWorkStation()));

        return dto;
    }

    @Override
    public PackingStation toEntity(PackingStationDTO dto) {
        if (dto == null) {
            return null;
        }
        PackingStation entity = new PackingStation();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());

        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        if (dto.getTypeId() != null) {
            entity.setPackingStationType(packingStationTypeRepository.retrieve(dto.getTypeId()));
        }
        if (dto.getOperatorId() != null) {
            entity.setOperator(userRepository.retrieve(dto.getOperatorId()));
        } else {
            entity.setOperator(null);
        }
        if (dto.getWorkstationId() != null) {
            entity.setWorkStation(workStationRepository.retrieve(dto.getWorkstationId()));
        }

        return entity;
    }

    @Override
    public void updateEntityFromDTO(PackingStationDTO dto, PackingStation entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }

        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());

        if (dto.getTypeId() != null) {
            entity.setPackingStationType(packingStationTypeRepository.retrieve(dto.getTypeId()));
        } else {
            entity.setPackingStationType(null);
        }
        if (dto.getOperatorId() != null) {
            entity.setOperator(userRepository.retrieve(dto.getOperatorId()));
        } else {
            entity.setOperator(null);
        }
        if (dto.getWorkstationId() != null) {
            entity.setWorkStation(workStationRepository.retrieve(dto.getWorkstationId()));
        } else {
            entity.setWorkStation(null);
        }
    }
}
