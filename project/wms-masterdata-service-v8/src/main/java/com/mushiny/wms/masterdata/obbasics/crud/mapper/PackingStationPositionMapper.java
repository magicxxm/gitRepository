package com.mushiny.wms.masterdata.obbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import com.mushiny.wms.masterdata.ibbasics.crud.mapper.ReceiveDestinationMapper;
import com.mushiny.wms.masterdata.ibbasics.crud.mapper.ReceiveStationTypeMapper;
import com.mushiny.wms.masterdata.ibbasics.repository.ReceiveDestinationRepository;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.WorkStationPositionMapper;
import com.mushiny.wms.masterdata.mdbasics.repository.WorkStationPositionRepository;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PackingStationPositionDTO;
import com.mushiny.wms.masterdata.obbasics.domain.PackingStationPosition;
import com.mushiny.wms.masterdata.obbasics.repository.PackingStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PackingStationPositionMapper implements BaseMapper<PackingStationPositionDTO, PackingStationPosition> {

    private final ApplicationContext applicationContext;
    private final PackingStationRepository packingStationRepository;
    private final PackingStationMapper packingStationMapper;
    private final WorkStationPositionMapper workStationPositionMapper;
    private final WorkStationPositionRepository workStationPositionRepository;
    private final WarehouseMapper warehouseMapper;

    @Autowired
    public PackingStationPositionMapper(ApplicationContext applicationContext,
                                        WarehouseMapper warehouseMapper,
                                        PackingStationRepository packingStationRepository,
                                        PackingStationMapper packingStationMapper,
                                        WorkStationPositionMapper workStationPositionMapper,
                                        WorkStationPositionRepository workStationPositionRepository) {
        this.applicationContext = applicationContext;
        this.warehouseMapper = warehouseMapper;
        this.packingStationRepository = packingStationRepository;
        this.packingStationMapper = packingStationMapper;
        this.workStationPositionMapper = workStationPositionMapper;
        this.workStationPositionRepository = workStationPositionRepository;
    }

    @Override
    public PackingStationPositionDTO toDTO(PackingStationPosition entity) {
        if (entity == null) {
            return null;
        }
        PackingStationPositionDTO dto = new PackingStationPositionDTO(entity);
        dto.setPositionState(entity.getPositionState());
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setWorkStationPosition(workStationPositionMapper.toDTO(entity.getWorkStationPosition()));
        dto.setPackingStation(packingStationMapper.toDTO(entity.getPackingStation()));

        return dto;
    }

    @Override
    public PackingStationPosition toEntity(PackingStationPositionDTO dto) {
        if (dto == null) {
            return null;
        }
        PackingStationPosition entity = new PackingStationPosition();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setPositionState(dto.getPositionState());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        if (dto.getWorkStationPositionId() != null) {
            entity.setWorkStationPosition(workStationPositionRepository.retrieve(dto.getWorkStationPositionId()));
        }
        if (dto.getPackingStationId() != null) {
            entity.setPackingStation(packingStationRepository.retrieve(dto.getPackingStationId()));
        }

        return entity;
    }

    @Override
    public void updateEntityFromDTO(PackingStationPositionDTO dto, PackingStationPosition entity) {
    }
}
