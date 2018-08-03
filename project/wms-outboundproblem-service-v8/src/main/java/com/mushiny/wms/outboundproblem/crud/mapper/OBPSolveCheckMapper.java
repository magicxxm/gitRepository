package com.mushiny.wms.outboundproblem.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.outboundproblem.crud.dto.OBPSolveCheckDTO;
import com.mushiny.wms.outboundproblem.domain.OBPSolveCheck;
import com.mushiny.wms.outboundproblem.repository.OBPSolveCheckRepository;
import com.mushiny.wms.outboundproblem.crud.common.mapper.StorageLocationMapper;
import com.mushiny.wms.outboundproblem.repository.OBProblemCheckRepository;
import com.mushiny.wms.outboundproblem.repository.common.StorageLocationRepository;
import com.mushiny.wms.outboundproblem.repository.OBProblemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OBPSolveCheckMapper implements BaseMapper<OBPSolveCheckDTO, OBPSolveCheck> {

    private final StorageLocationRepository storageLocationRepository;
    private final StorageLocationMapper storageLocationMapper;
    private final ApplicationContext applicationContext;
    private final OBProblemCheckMapper obProblemCheckMapper;
    private final OBProblemCheckRepository obProblemCheckRepository;

    @Autowired
    public OBPSolveCheckMapper(StorageLocationRepository storageLocationRepository,
                               StorageLocationMapper storageLocationMapper,
                               ApplicationContext applicationContext,
                               OBProblemCheckMapper obProblemCheckMapper,
                               OBProblemCheckRepository obProblemCheckRepository) {
        this.storageLocationRepository = storageLocationRepository;
        this.storageLocationMapper = storageLocationMapper;
        this.applicationContext = applicationContext;
        this.obProblemCheckMapper = obProblemCheckMapper;
        this.obProblemCheckRepository = obProblemCheckRepository;
    }
    @Override
    public OBPSolveCheckDTO toDTO(OBPSolveCheck entity) {
        if (entity == null) {
            return null;
        }
        OBPSolveCheckDTO dto = new OBPSolveCheckDTO(entity);

        dto.setObProblem(obProblemCheckMapper.toDTO(entity.getObProblem()));
        dto.setStorageLocation(storageLocationMapper.toDTO(entity.getStorageLocation()));
        dto.setAmount(entity.getAmount());
        dto.setProblemAmount(entity.getProblemAmount());
        dto.setItemDataAmount(entity.getItemDataAmount());
        dto.setState(entity.getState());
        dto.setCheckBy(entity.getCheckBy());
        dto.setCheckDate(entity.getCheckDate());
        dto.setClient(entity.getClientId());
        dto.setWarehouse(entity.getWarehouseId());
        return dto;
    }

    @Override
    public OBPSolveCheck toEntity(OBPSolveCheckDTO dto) {
        if (dto == null) {
            return null;
        }
        OBPSolveCheck entity = new OBPSolveCheck();
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());

        if (dto.getProblemId() != null) {
            entity.setObProblem(obProblemCheckRepository.retrieve(dto.getProblemId()));
        }
        if (dto.getStorageLocationId() != null) {
            entity.setStorageLocation(storageLocationRepository.retrieve(dto.getStorageLocationId()));
        }
        entity.setAmount(dto.getAmount());
        entity.setProblemAmount(dto.getProblemAmount());
        entity.setItemDataAmount(dto.getItemDataAmount());
        entity.setStorageLocationAmount(dto.getStorageLocationAmount());
        entity.setState(dto.getState());
        entity.setCheckBy(dto.getCheckBy());
        entity.setCheckDate(dto.getCheckDate());
        entity.setClientId(dto.getClientId());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());

        return entity;
    }

    @Override
    public void updateEntityFromDTO(OBPSolveCheckDTO dto, OBPSolveCheck entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());

        if (dto.getProblemId() != null) {
            entity.setObProblem(obProblemCheckRepository.retrieve(dto.getProblemId()));
        }
        if (dto.getStorageLocationId() != null) {
            entity.setStorageLocation(storageLocationRepository.retrieve(dto.getStorageLocationId()));
        }
        entity.setAmount(dto.getAmount());
        entity.setProblemAmount(dto.getProblemAmount());
        entity.setItemDataAmount(dto.getItemDataAmount());
        entity.setStorageLocationAmount(dto.getStorageLocationAmount());
        entity.setState(dto.getState());
        entity.setCheckBy(dto.getCheckBy());
        entity.setCheckDate(dto.getCheckDate());

    }
}
