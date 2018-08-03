package com.mushiny.wms.masterdata.ibbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.StowStationDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.StowStation;
import com.mushiny.wms.masterdata.ibbasics.repository.StowStationTypeRepository;
import com.mushiny.wms.masterdata.general.crud.mapper.UserMapper;
import com.mushiny.wms.masterdata.general.repository.ClientRepository;
import com.mushiny.wms.masterdata.general.repository.UserRepository;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.WorkStationMapper;
import com.mushiny.wms.masterdata.mdbasics.repository.WorkStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StowStationMapper implements BaseMapper<StowStationDTO, StowStation> {

    private final ApplicationContext applicationContext;

    private final UserMapper userMapper;
    private final StowStationTypeMapper stowStationTypeMapper;
    private final UserRepository userRepository;
    private final StowStationTypeRepository stowStationTypeRepository;
    private final ClientRepository clientRepository;
    private final WorkStationMapper workStationMapper;
    private final WorkStationRepository workStationRepository;

    @Autowired
    public StowStationMapper(ApplicationContext applicationContext,
                             UserMapper userMapper,
                             StowStationTypeMapper stowStationTypeMapper,
                             UserRepository userRepository,
                             StowStationTypeRepository stowStationTypeRepository,
                             ClientRepository clientRepository,
                             WorkStationMapper workStationMapper,
                             WorkStationRepository workStationRepository) {
        this.userMapper = userMapper;
        this.stowStationTypeMapper = stowStationTypeMapper;
        this.applicationContext = applicationContext;
        this.userRepository = userRepository;
        this.stowStationTypeRepository = stowStationTypeRepository;
        this.clientRepository = clientRepository;
        this.workStationMapper = workStationMapper;
        this.workStationRepository = workStationRepository;
    }

    @Override
    public StowStationDTO toDTO(StowStation entity) {
        if (entity == null) {
            return null;
        }

        StowStationDTO dto = new StowStationDTO(entity);

        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setStowStationType(stowStationTypeMapper.toDTO(entity.getType()));
        dto.setUser(userMapper.toDTO(entity.getOperator()));
        dto.setWorkstation(workStationMapper.toDTO(entity.getWorkstation()));
        dto.setWarehouseId(entity.getWarehouseId());

        return dto;
    }

    @Override
    public StowStation toEntity(StowStationDTO dto) {
        if (dto == null) {
            return null;
        }

        StowStation entity = new StowStation();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        if (dto.getTypeId() != null) {
            entity.setType(stowStationTypeRepository.retrieve(dto.getTypeId()));
        }
        if (dto.getUser() != null){
            entity.setOperator(userRepository.retrieve(dto.getOperatorId()));
        }
        if (dto.getWorkstationId() != null) {
            entity.setWorkstation(workStationRepository.retrieve(dto.getWorkstationId()));
        }
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        return entity;
    }

    @Override
    public void updateEntityFromDTO(StowStationDTO dto, StowStation entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        if (dto.getTypeId() != null) {
            entity.setType(stowStationTypeRepository.retrieve(dto.getTypeId()));
        }
        if (dto.getUser() != null){
            entity.setOperator(userRepository.retrieve(dto.getOperatorId()));
        }
        if (dto.getWorkstationId() != null) {
            entity.setWorkstation(workStationRepository.retrieve(dto.getWorkstationId()));
        }
    }
}

