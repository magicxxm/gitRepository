package com.mushiny.wms.outboundproblem.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.config.security.SecurityUtils;
import com.mushiny.wms.outboundproblem.crud.common.mapper.WarehouseMapper;
import com.mushiny.wms.outboundproblem.crud.dto.OBPSolvePositionDTO;
import com.mushiny.wms.outboundproblem.domain.OBPSolvePosition;
import com.mushiny.wms.outboundproblem.domain.enums.OBPSolveState;
import com.mushiny.wms.outboundproblem.repository.OBPSolveRepository;
import com.mushiny.wms.outboundproblem.repository.OBPStationTypeRepository;
import com.mushiny.wms.outboundproblem.repository.common.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class OBPSolvePositionMapper implements BaseMapper<OBPSolvePositionDTO, OBPSolvePosition> {

    private final OBPStationTypeRepository obpStationTypeRepository;
    private final ApplicationContext applicationContext;
    private final OBPStationTypeMapper obpStationTypeMapper;
    private final WarehouseMapper warehouseMapper;
    private final OBPSolveRepository obpSolveRepository;
    private final ResourceRepository resourceRepository;

    @Autowired
    public OBPSolvePositionMapper(OBPStationTypeRepository obpStationTypeRepository,
                                  ApplicationContext applicationContext,
                                  OBPStationTypeMapper obpStationTypeMapper,
                                  WarehouseMapper warehouseMapper,
                                  OBPSolveRepository obpSolveRepository,
                                  ResourceRepository resourceRepository) {
        this.obpStationTypeRepository = obpStationTypeRepository;
        this.applicationContext = applicationContext;
        this.obpStationTypeMapper = obpStationTypeMapper;
        this.warehouseMapper = warehouseMapper;
        this.obpSolveRepository = obpSolveRepository;
        this.resourceRepository = resourceRepository;
    }

    @Override
    public OBPSolvePositionDTO toDTO(OBPSolvePosition entity) {
        if (entity == null) {
            return null;
        }
        OBPSolvePositionDTO dto = new OBPSolvePositionDTO();

        dto.setId(entity.getId());
        if (entity.getObpSolve() != null)
            dto.setSolveId(entity.getObpSolve().getId());
        dto.setDescription(entity.getDescription());
        dto.setSolveKey(entity.getSolveKey());
        dto.setLocation(entity.getLocation());
        dto.setLocationContainer(entity.getLocationContainer());
        dto.setState(entity.getState());
        dto.setSolveBy(entity.getSolveBy());
        dto.setSolveDate(entity.getSolveDate());
        dto.setShipmentNo(entity.getShipmentNo());
        dto.setItemNo(entity.getItemDataNo());
        dto.setWarehouseId(applicationContext.getCurrentWarehouse());

        return dto;
    }

    @Override
    public OBPSolvePosition toEntity(OBPSolvePositionDTO dto) {
        if (dto == null) {
            return null;
        }
        OBPSolvePosition entity = new OBPSolvePosition();

        entity.setObpSolve(null);
        if (dto.getSolveId() != null) {
            entity.setObpSolve(obpSolveRepository.retrieve(dto.getSolveId()));
        }

        if (dto.getId() != null) {
            entity.setId(dto.getId());
        }

        entity.setDescription(dto.getDescription());
        entity.setSolveKey(dto.getSolveKey());
        entity.setLocation(dto.getLocation());
        entity.setLocationContainer(dto.getLocationContainer());
        entity.setItemDataNo(dto.getItemNo());
        entity.setState(dto.getState());
        entity.setAmountScaned(dto.getAmountScaned());
        entity.setSolveBy(SecurityUtils.getCurrentUsername());
        entity.setSolveDate(LocalDateTime.now());
        entity.setShipmentNo(dto.getShipmentNo());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        if(dto.getEntityLock()!=null){
            entity.setEntityLock(dto.getEntityLock());
        }
        return entity;
    }

    @Override
    public void updateEntityFromDTO(OBPSolvePositionDTO dto, OBPSolvePosition entity) {
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
        if (dto.getSolveKey() != null) {
            entity.setSolveKey(dto.getSolveKey());
        }
        if (dto.getLocation() != null) {
            entity.setLocation(dto.getLocation());
        }
        if (dto.getLocationContainer() != null) {
            entity.setLocationContainer(dto.getLocationContainer());
        }
        if (dto.getItemNo() != null) {
            entity.setItemDataNo(dto.getItemNo());
        }
        if (dto.getState() != null) {
            entity.setState(dto.getState());
        }
        if (dto.getAmountScaned() != null) {
            entity.setAmountScaned(dto.getAmountScaned());
        }
        if (dto.getSolveBy() != null) {
            entity.setSolveBy(dto.getSolveBy());
        }
        if (dto.getSolveDate() != null) {
            entity.setSolveDate(dto.getSolveDate());
        }
    }

}
