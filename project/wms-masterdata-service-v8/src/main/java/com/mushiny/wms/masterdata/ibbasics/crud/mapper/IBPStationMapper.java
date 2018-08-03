package com.mushiny.wms.masterdata.ibbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.general.crud.mapper.UserMapper;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import com.mushiny.wms.masterdata.general.repository.UserRepository;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.WorkStationMapper;
import com.mushiny.wms.masterdata.mdbasics.repository.WorkStationRepository;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.IBPStationDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.mapper.IBPStationTypeMapper;
import com.mushiny.wms.masterdata.ibbasics.domain.IBPStation;
import com.mushiny.wms.masterdata.ibbasics.repository.IBPStationTypeRepository;
import org.springframework.stereotype.Component;

@Component
public class IBPStationMapper implements BaseMapper<IBPStationDTO, IBPStation> {

    private final ApplicationContext applicationContext;
    private final WarehouseMapper warehouseMapper;
    private final IBPStationTypeMapper ibpStationTypeMapper;
    private final IBPStationTypeRepository ibpStationTypeRepository;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final WorkStationMapper workStationMapper;
    private final WorkStationRepository workStationRepository;

    public IBPStationMapper(ApplicationContext applicationContext,
                            WarehouseMapper warehouseMapper,
                            IBPStationTypeMapper ibpStationTypeMapper,
                            IBPStationTypeRepository ibpStationTypeRepository,
                            UserMapper userMapper,
                            UserRepository userRepository,
                            WorkStationMapper workStationMapper,
                            WorkStationRepository workStationRepository) {
        this.applicationContext = applicationContext;
        this.warehouseMapper = warehouseMapper;
        this.ibpStationTypeMapper = ibpStationTypeMapper;
        this.ibpStationTypeRepository = ibpStationTypeRepository;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.workStationMapper = workStationMapper;
        this.workStationRepository = workStationRepository;
    }

    @Override
    public IBPStationDTO toDTO(IBPStation entity) {
        if (entity == null) {
            return null;
        }
        IBPStationDTO dto = new IBPStationDTO(entity);

        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setWarehouseId(entity.getWarehouseId());
//        dto.setIbpStationType(ibpStationTypeMapper.toDTO(entity.getIbpStationType()));
        dto.setIbpStationType(ibpStationTypeMapper.toDTO(entity.getIbpStationType()));
        dto.setUser(userMapper.toDTO(entity.getOperatorId()));
        dto.setWorkstation(workStationMapper.toDTO(entity.getWorkStation()));

        return dto;
    }

    @Override
    public IBPStation toEntity(IBPStationDTO dto) {
        if (dto == null) {
            return null;
        }
        IBPStation entity = new IBPStation();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());

        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        if (dto.getTypeId() != null) {
            entity.setIbpStationType(ibpStationTypeRepository.retrieve(dto.getTypeId()));
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
    public void updateEntityFromDTO(IBPStationDTO dto, IBPStation entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }

        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());

        if (dto.getTypeId() != null) {
            entity.setIbpStationType(ibpStationTypeRepository.retrieve(dto.getTypeId()));
        } else {
            entity.setIbpStationType(null);
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
