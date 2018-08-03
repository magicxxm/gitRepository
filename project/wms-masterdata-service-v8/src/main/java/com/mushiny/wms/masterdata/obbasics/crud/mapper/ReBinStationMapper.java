package com.mushiny.wms.masterdata.obbasics.crud.mapper;

import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.masterdata.general.crud.mapper.UserMapper;
import com.mushiny.wms.masterdata.general.repository.UserRepository;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.WorkStationMapper;
import com.mushiny.wms.masterdata.mdbasics.repository.WorkStationRepository;
import com.mushiny.wms.masterdata.obbasics.crud.dto.ReBinStationDTO;
import com.mushiny.wms.masterdata.obbasics.domain.ReBinStation;
import com.mushiny.wms.masterdata.obbasics.repository.ReBinStationTypeRepository;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import org.springframework.stereotype.Component;

@Component
public class ReBinStationMapper implements BaseMapper<ReBinStationDTO, ReBinStation> {

    private final ReBinStationTypeRepository reBinStationTypeRepository;
    private final ReBinStationTypeMapper reBinStationTypeMapper;
    private final WorkStationMapper workStationMapper;
    private final WorkStationRepository workStationRepository;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final ApplicationContext applicationContext;
    private final WarehouseMapper warehouseMapper;

    public ReBinStationMapper(ReBinStationTypeRepository reBinStationTypeRepository,
                              ReBinStationTypeMapper reBinStationTypeMapper,
                              WorkStationMapper workStationMapper,
                              WorkStationRepository workStationRepository,
                              UserMapper userMapper,
                              UserRepository userRepository,
                              ApplicationContext applicationContext,
                              WarehouseMapper warehouseMapper) {
        this.reBinStationTypeRepository = reBinStationTypeRepository;
        this.reBinStationTypeMapper = reBinStationTypeMapper;
        this.workStationMapper = workStationMapper;
        this.workStationRepository = workStationRepository;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.applicationContext = applicationContext;
        this.warehouseMapper = warehouseMapper;
    }

    @Override
    public ReBinStationDTO toDTO(ReBinStation entity) {
        if (entity == null) {
            return null;
        }
        ReBinStationDTO dto = new ReBinStationDTO(entity);

        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());

        dto.setWarehouseId(entity.getWarehouseId());
        dto.setRebinStationType(reBinStationTypeMapper.toDTO(entity.getReBinStationType()));
        dto.setUser(userMapper.toDTO(entity.getOperator()));
        dto.setWorkstation(workStationMapper.toDTO(entity.getWorkStation()));

        return dto;
    }

    @Override
    public ReBinStation toEntity(ReBinStationDTO dto) {
        if (dto == null) {
            return null;
        }
        ReBinStation entity = new ReBinStation();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());

        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        if (dto.getTypeId() != null) {
            entity.setReBinStationType(reBinStationTypeRepository.retrieve(dto.getTypeId()));
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
    public void updateEntityFromDTO(ReBinStationDTO dto, ReBinStation entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());

        if (dto.getTypeId() != null) {
            entity.setReBinStationType(reBinStationTypeRepository.retrieve(dto.getTypeId()));
        } else {
            entity.setReBinStationType(null);
        }
        if (dto.getOperatorId() != null) {
            entity.setOperator(userRepository.retrieve(dto.getOperatorId()));
        } else {
            entity.setOperator(null);
        }
        if (dto.getWorkstationId() != null) {
            entity.setWorkStation(workStationRepository.retrieve(dto.getWorkstationId()));
        }
    }
}
