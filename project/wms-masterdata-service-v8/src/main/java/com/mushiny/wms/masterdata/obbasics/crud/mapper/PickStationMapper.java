package com.mushiny.wms.masterdata.obbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.general.crud.mapper.UserMapper;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import com.mushiny.wms.masterdata.general.repository.UserRepository;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.WorkStationMapper;
import com.mushiny.wms.masterdata.mdbasics.repository.WorkStationRepository;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PickStationDTO;
import com.mushiny.wms.masterdata.obbasics.domain.PickStation;
import com.mushiny.wms.masterdata.obbasics.repository.PickStationTypeRepository;
import org.springframework.stereotype.Component;

@Component
public class PickStationMapper implements BaseMapper<PickStationDTO, PickStation> {

    private final ApplicationContext applicationContext;
    private final WarehouseMapper warehouseMapper;
    private final PickStationTypeMapper pickStationTypeMapper;
    private final PickStationTypeRepository pickStationTypeRepository;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final WorkStationMapper workStationMapper;
    private final WorkStationRepository workStationRepository;

    public PickStationMapper(ApplicationContext applicationContext,
                             WarehouseMapper warehouseMapper,
                             PickStationTypeMapper pickStationTypeMapper,
                             PickStationTypeRepository pickStationTypeRepository,
                             UserMapper userMapper,
                             UserRepository userRepository,
                             WorkStationMapper workStationMapper,
                             WorkStationRepository workStationRepository) {
        this.applicationContext = applicationContext;
        this.warehouseMapper = warehouseMapper;
        this.pickStationTypeMapper = pickStationTypeMapper;
        this.pickStationTypeRepository = pickStationTypeRepository;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.workStationMapper = workStationMapper;
        this.workStationRepository = workStationRepository;
    }

    @Override
    public PickStationDTO toDTO(PickStation entity) {
        if (entity == null) {
            return null;
        }
        PickStationDTO dto = new PickStationDTO(entity);

        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());

        dto.setWarehouseId(entity.getWarehouseId());
        dto.setPickStationType(pickStationTypeMapper.toDTO(entity.getPickStationType()));
        dto.setUser(userMapper.toDTO(entity.getOperator()));
        dto.setWorkstation(workStationMapper.toDTO(entity.getWorkStation()));

        return dto;
    }

    @Override
    public PickStation toEntity(PickStationDTO dto) {
        if (dto == null) {
            return null;
        }
        PickStation entity = new PickStation();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());

        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        if (dto.getTypeId() != null) {
            entity.setPickStationType(pickStationTypeRepository.retrieve(dto.getTypeId()));
        }
        if (dto.getOperatorId() != null) {
            entity.setOperator(userRepository.retrieve(dto.getOperatorId()));
        }
        if (dto.getWorkstationId() != null) {
            entity.setWorkStation(workStationRepository.retrieve(dto.getWorkstationId()));
        }

        return entity;
    }

    @Override
    public void updateEntityFromDTO(PickStationDTO dto, PickStation entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }

        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());

        if (dto.getTypeId() != null) {
            entity.setPickStationType(pickStationTypeRepository.retrieve(dto.getTypeId()));
        } else {
            entity.setPickStationType(null);
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
