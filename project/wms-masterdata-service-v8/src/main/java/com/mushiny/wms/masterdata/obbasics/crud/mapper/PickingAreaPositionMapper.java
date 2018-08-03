package com.mushiny.wms.masterdata.obbasics.crud.mapper;

import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.masterdata.general.repository.ClientRepository;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.ZoneMapper;
import com.mushiny.wms.masterdata.mdbasics.repository.ZoneRepository;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PickingAreaPositionDTO;
import com.mushiny.wms.masterdata.obbasics.domain.PickingAreaPosition;
import com.mushiny.wms.masterdata.obbasics.repository.PickingAreaRepository;
import com.mushiny.wms.masterdata.general.crud.mapper.ClientMapper;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PickingAreaPositionMapper implements BaseMapper<PickingAreaPositionDTO, PickingAreaPosition> {

    private final ZoneRepository zoneRepository;
    private final PickingAreaRepository pickingAreaRepository;
    private final ApplicationContext applicationContext;
    private final ZoneMapper zoneMapper;
    private final PickingAreaMapper pickingAreaMapper;
    private final WarehouseMapper warehouseMapper;
    private final ClientMapper clientMapper;
    private final ClientRepository clientRepository;

    @Autowired
    public PickingAreaPositionMapper(ZoneRepository zoneRepository,
                                     PickingAreaRepository pickingAreaRepository,
                                     ApplicationContext applicationContext,
                                     ZoneMapper zoneMapper,
                                     PickingAreaMapper pickingAreaMapper,
                                     WarehouseMapper warehouseMapper,
                                     ClientMapper clientMapper,
                                     ClientRepository clientRepository) {
        this.zoneRepository = zoneRepository;
        this.pickingAreaRepository = pickingAreaRepository;
        this.applicationContext = applicationContext;
        this.zoneMapper = zoneMapper;
        this.pickingAreaMapper = pickingAreaMapper;
        this.warehouseMapper = warehouseMapper;
        this.clientMapper = clientMapper;
        this.clientRepository = clientRepository;
    }

    @Override
    public PickingAreaPositionDTO toDTO(PickingAreaPosition entity) {
        if (entity == null) {
            return null;
        }
        PickingAreaPositionDTO dto = new PickingAreaPositionDTO(entity);

        dto.setPositionNo(entity.getPositionNo());

        dto.setClient(clientRepository.retrieve(entity.getClientId()).getName());
        dto.setClientId(entity.getClientId());
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setZone(zoneMapper.toDTO(entity.getZone()));
        dto.setPickingArea(pickingAreaMapper.toDTO(entity.getPickingArea()));

        return dto;
    }

    @Override
    public PickingAreaPosition toEntity(PickingAreaPositionDTO dto) {
        if (dto == null) {
            return null;
        }
        PickingAreaPosition entity = new PickingAreaPosition();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setPositionNo(dto.getPositionNo());

        applicationContext.isCurrentClient(dto.getClientId());
        entity.setClientId(dto.getClientId());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        if (dto.getZoneId() != null) {
            entity.setZone(zoneRepository.retrieve(dto.getZoneId()));
        }
        if (dto.getPickingAreaId() != null) {
            entity.setPickingArea(pickingAreaRepository.retrieve(dto.getPickingAreaId()));
        }

        return entity;
    }

    @Override
    public void updateEntityFromDTO(PickingAreaPositionDTO dto, PickingAreaPosition entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setPositionNo(dto.getPositionNo());

        if (dto.getZoneId() != null) {
            entity.setZone(zoneRepository.retrieve(dto.getZoneId()));
        } else {
            entity.setZone(null);
        }

        if (dto.getPickingAreaId() != null) {
            entity.setPickingArea(pickingAreaRepository.retrieve(dto.getPickingAreaId()));
        } else {
            entity.setPickingArea(null);
        }
    }
}
