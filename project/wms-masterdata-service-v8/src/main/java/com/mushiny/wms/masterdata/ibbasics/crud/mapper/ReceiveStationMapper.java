package com.mushiny.wms.masterdata.ibbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.general.crud.mapper.UserMapper;
import com.mushiny.wms.masterdata.general.repository.UserRepository;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceiveStationDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.ReceiveStation;
import com.mushiny.wms.masterdata.ibbasics.repository.ReceiveStationTypeRepository;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.WorkStationMapper;
import com.mushiny.wms.masterdata.mdbasics.repository.WorkStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReceiveStationMapper implements BaseMapper<ReceiveStationDTO, ReceiveStation> {

    private final ApplicationContext applicationContext;
    private final ReceiveStationTypeMapper receivingStationTypeMapper;
    private final ReceiveStationTypeRepository receivingStationTypeRepository;
    private final WorkStationMapper workStationMapper;
    private final WorkStationRepository workStationRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public ReceiveStationMapper(ApplicationContext applicationContext,
                                ReceiveStationTypeRepository receivingStationTypeRepository,
                                ReceiveStationTypeMapper receivingStationTypeMapper,
                                WorkStationMapper workStationMapper,
                                WorkStationRepository workStationRepository,
                                UserRepository userRepository,
                                UserMapper userMapper) {
        this.applicationContext = applicationContext;
        this.receivingStationTypeRepository = receivingStationTypeRepository;
        this.receivingStationTypeMapper = receivingStationTypeMapper;
        this.workStationMapper = workStationMapper;
        this.workStationRepository = workStationRepository;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public ReceiveStationDTO toDTO(ReceiveStation entity) {
        if (entity == null) {
            return null;
        }
        ReceiveStationDTO dto = new ReceiveStationDTO(entity);

        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        if(entity.getOperator()!=null){
        dto.setUser(userMapper.toDTO(userRepository.retrieve(entity.getOperator())));
        }else{
            dto.setUser(null);
        }
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setReceiveStationType(receivingStationTypeMapper.toDTO(entity.getReceivingStationType()));
        dto.setWorkstation(workStationMapper.toDTO(entity.getWorkStation()));
        return dto;
    }

    @Override
    public ReceiveStation toEntity(ReceiveStationDTO dto) {
        if (dto == null) {
            return null;
        }
        ReceiveStation entity = new ReceiveStation();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setOperator(dto.getOperatorId());

        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        if (dto.getTypeId() != null) {
            entity.setReceivingStationType(receivingStationTypeRepository.retrieve(dto.getTypeId()));
        }
        if (dto.getWorkstationId() != null) {
            entity.setWorkStation(workStationRepository.retrieve(dto.getWorkstationId()));
        }

        return entity;
    }

    @Override
    public void updateEntityFromDTO(ReceiveStationDTO dto, ReceiveStation entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        if (dto.getWorkstationId() != null) {
            entity.setWorkStation(workStationRepository.retrieve(dto.getWorkstationId()));
        }
        if (dto.getTypeId() != null) {
            entity.setReceivingStationType(receivingStationTypeRepository.retrieve(dto.getTypeId()));
        }

    }
}
