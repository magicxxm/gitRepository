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
import com.mushiny.wms.masterdata.obbasics.crud.dto.RebatchStationDTO;
import com.mushiny.wms.masterdata.obbasics.domain.RebatchStation;
import com.mushiny.wms.masterdata.obbasics.repository.RebatchStationTypeRepository;
import org.springframework.stereotype.Component;

@Component
public class RebatchStationMapper implements BaseMapper<RebatchStationDTO, RebatchStation> {

    private final ApplicationContext applicationContext;
    private final WarehouseMapper warehouseMapper;
    private final RebatchStationTypeMapper rebatchStationTypeMapper;
    private final RebatchStationTypeRepository rebatchStationTypeRepository;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final WorkStationMapper workStationMapper;
    private final WorkStationRepository workStationRepository;

    public RebatchStationMapper(ApplicationContext applicationContext,
                                WarehouseMapper warehouseMapper,
                                RebatchStationTypeMapper rebatchStationTypeMapper,
                                RebatchStationTypeRepository rebatchStationTypeRepository,
                                UserMapper userMapper,
                                UserRepository userRepository,
                                WorkStationMapper workStationMapper,
                                WorkStationRepository workStationRepository) {
        this.applicationContext = applicationContext;
        this.warehouseMapper = warehouseMapper;
        this.rebatchStationTypeMapper = rebatchStationTypeMapper;
        this.rebatchStationTypeRepository = rebatchStationTypeRepository;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.workStationMapper = workStationMapper;
        this.workStationRepository = workStationRepository;
    }

    @Override
    public RebatchStationDTO toDTO(RebatchStation entity) {
        if (entity == null) {
            return null;
        }
        RebatchStationDTO dto = new RebatchStationDTO(entity);

        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());

        dto.setWarehouseId(entity.getWarehouseId());
        dto.setRebatchStationType(rebatchStationTypeMapper.toDTO(entity.getRebatchStationType()));
        dto.setUser(userMapper.toDTO(entity.getOperator()));
        dto.setWorkstation(workStationMapper.toDTO(entity.getWorkStation()));

        return dto;
    }

    @Override
    public RebatchStation toEntity(RebatchStationDTO dto) {
        if (dto == null) {
            return null;
        }
        RebatchStation entity = new RebatchStation();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());

        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        if (dto.getTypeId() != null) {
            entity.setRebatchStationType(rebatchStationTypeRepository.retrieve(dto.getTypeId()));
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
    public void updateEntityFromDTO(RebatchStationDTO dto, RebatchStation entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }

        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());

        if (dto.getTypeId() != null) {
            entity.setRebatchStationType(rebatchStationTypeRepository.retrieve(dto.getTypeId()));
        } else {
            entity.setRebatchStationType(null);
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
