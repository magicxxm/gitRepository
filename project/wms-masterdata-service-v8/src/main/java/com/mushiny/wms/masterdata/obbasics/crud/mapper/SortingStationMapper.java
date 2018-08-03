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
import com.mushiny.wms.masterdata.obbasics.crud.dto.SortingStationDTO;
import com.mushiny.wms.masterdata.obbasics.domain.SortingStation;
import com.mushiny.wms.masterdata.obbasics.repository.SortingStationTypeRepository;
import org.springframework.stereotype.Component;

@Component
public class SortingStationMapper implements BaseMapper<SortingStationDTO, SortingStation> {

    private final ApplicationContext applicationContext;
    private final WarehouseMapper warehouseMapper;
    private final SortingStationTypeMapper sortingStationTypeMapper;
    private final SortingStationTypeRepository sortingStationTypeRepository;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final WorkStationMapper workStationMapper;
    private final WorkStationRepository workStationRepository;

    public SortingStationMapper(ApplicationContext applicationContext,
                                WarehouseMapper warehouseMapper,
                                SortingStationTypeMapper sortingStationTypeMapper,
                                SortingStationTypeRepository sortingStationTypeRepository,
                                UserMapper userMapper,
                                UserRepository userRepository,
                                WorkStationMapper workStationMapper,
                                WorkStationRepository workStationRepository) {
        this.applicationContext = applicationContext;
        this.warehouseMapper = warehouseMapper;
        this.sortingStationTypeMapper = sortingStationTypeMapper;
        this.sortingStationTypeRepository = sortingStationTypeRepository;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.workStationMapper = workStationMapper;
        this.workStationRepository = workStationRepository;
    }

    @Override
    public SortingStationDTO toDTO(SortingStation entity) {
        if (entity == null) {
            return null;
        }
        SortingStationDTO dto = new SortingStationDTO(entity);

        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());

        dto.setWarehouseId(entity.getWarehouseId());
        dto.setSortingStationType(sortingStationTypeMapper.toDTO(entity.getSortingStationType()));
        dto.setUser(userMapper.toDTO(entity.getOperator()));
        dto.setWorkstation(workStationMapper.toDTO(entity.getWorkStation()));

        return dto;
    }

    @Override
    public SortingStation toEntity(SortingStationDTO dto) {
        if (dto == null) {
            return null;
        }
        SortingStation entity = new SortingStation();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());

        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        if (dto.getTypeId() != null) {
            entity.setSortingStationType(sortingStationTypeRepository.retrieve(dto.getTypeId()));
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
    public void updateEntityFromDTO(SortingStationDTO dto, SortingStation entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }

        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());

        if (dto.getTypeId() != null) {
            entity.setSortingStationType(sortingStationTypeRepository.retrieve(dto.getTypeId()));
        } else {
            entity.setSortingStationType(null);
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
