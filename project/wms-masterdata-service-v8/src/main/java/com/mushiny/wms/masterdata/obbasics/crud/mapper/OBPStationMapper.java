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
import com.mushiny.wms.masterdata.obbasics.crud.dto.OBPStationDTO;
import com.mushiny.wms.masterdata.obbasics.domain.OBPStation;
import com.mushiny.wms.masterdata.obbasics.repository.OBPStationTypeRepository;
import org.springframework.stereotype.Component;

@Component
public class OBPStationMapper implements BaseMapper<OBPStationDTO, OBPStation> {

    private final ApplicationContext applicationContext;
    private final WarehouseMapper warehouseMapper;
    private final OBPStationTypeMapper obpStationTypeMapper;
    private final OBPStationTypeRepository obpStationTypeRepository;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final WorkStationMapper workStationMapper;
    private final WorkStationRepository workStationRepository;

    public OBPStationMapper(ApplicationContext applicationContext,
                            WarehouseMapper warehouseMapper,
                            OBPStationTypeMapper obpStationTypeMapper,
                            OBPStationTypeRepository obpStationTypeRepository,
                            UserMapper userMapper,
                            UserRepository userRepository,
                            WorkStationMapper workStationMapper,
                            WorkStationRepository workStationRepository) {
        this.applicationContext = applicationContext;
        this.warehouseMapper = warehouseMapper;
        this.obpStationTypeMapper = obpStationTypeMapper;
        this.obpStationTypeRepository = obpStationTypeRepository;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.workStationMapper = workStationMapper;
        this.workStationRepository = workStationRepository;
    }

    @Override
    public OBPStationDTO toDTO(OBPStation entity) {
        if (entity == null) {
            return null;
        }
        OBPStationDTO dto = new OBPStationDTO(entity);

        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setState(entity.getState());
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setObpStationType(obpStationTypeMapper.toDTO(entity.getObpStationType()));
        dto.setUser(userMapper.toDTO(entity.getOperatorId()));
        dto.setWorkstation(workStationMapper.toDTO(entity.getWorkStation()));

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
        entity.setState(dto.getState());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        if (dto.getTypeId() != null) {
            entity.setObpStationType(obpStationTypeRepository.retrieve(dto.getTypeId()));
        }
        if (dto.getOperatorId() != null) {
            entity.setOperatorId(userRepository.retrieve(dto.getOperatorId()));
        }
        if (dto.getWorkstationId() != null) {
            entity.setWorkStation(workStationRepository.retrieve(dto.getWorkstationId()));
        }

        return entity;
    }

    @Override
    public void updateEntityFromDTO(OBPStationDTO dto, OBPStation entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }

        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setState(dto.getState());
        if (dto.getTypeId() != null) {
            entity.setObpStationType(obpStationTypeRepository.retrieve(dto.getTypeId()));
        } else {
            entity.setObpStationType(null);
        }
        if (dto.getOperatorId() != null) {
            entity.setOperatorId(userRepository.retrieve(dto.getOperatorId()));
        } else {
            entity.setOperatorId(null);
        }
        if (dto.getWorkstationId() != null) {
            entity.setWorkStation(workStationRepository.retrieve(dto.getWorkstationId()));
        } else {
            entity.setWorkStation(null);
        }
    }
}
