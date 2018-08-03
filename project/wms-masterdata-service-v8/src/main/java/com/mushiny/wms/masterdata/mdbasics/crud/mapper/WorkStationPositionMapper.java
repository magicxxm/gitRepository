package com.mushiny.wms.masterdata.mdbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.general.repository.ClientRepository;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.WorkStationPositionDTO;
import com.mushiny.wms.masterdata.mdbasics.domain.WorkStationPosition;
import com.mushiny.wms.masterdata.mdbasics.repository.WorkStationRepository;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.DigitalLabelMapper;
import com.mushiny.wms.masterdata.obbasics.repository.DigitalLabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WorkStationPositionMapper implements BaseMapper<WorkStationPositionDTO, WorkStationPosition> {

    private final ApplicationContext applicationContext;
    private final ClientRepository clientRepository;
    private final WorkStationMapper workStationMapper;
    private final WorkStationRepository workStationRepository;
    private final DigitalLabelMapper digitalLabelMapper;
    private final DigitalLabelRepository digitalLabelRepository;

    @Autowired
    public WorkStationPositionMapper(ApplicationContext applicationContext,
                                     ClientRepository clientRepository,
                                     WorkStationMapper workStationMapper,
                                     WorkStationRepository workStationRepository,
                                     DigitalLabelMapper digitalLabelMapper,
                                     DigitalLabelRepository digitalLabelRepository) {
        this.applicationContext = applicationContext;
        this.clientRepository = clientRepository;
        this.workStationMapper = workStationMapper;
        this.workStationRepository = workStationRepository;
        this.digitalLabelMapper = digitalLabelMapper;
        this.digitalLabelRepository = digitalLabelRepository;
    }

    @Override
    public WorkStationPositionDTO toDTO(WorkStationPosition entity) {
        if (entity == null) {
            return null;
        }
        WorkStationPositionDTO dto = new WorkStationPositionDTO(entity);
        dto.setPositionNo(entity.getPositionNo());
        dto.setPositionIndex(entity.getPositionIndex());
        dto.setDigitalLabel(digitalLabelMapper.toDTO(entity.getDigitalLabel()));
        dto.setWorkstation(workStationMapper.toDTO(entity.getWorkStation()));

        dto.setWarehouseId(entity.getWarehouseId());

        return dto;
    }


    @Override
    public WorkStationPosition toEntity(WorkStationPositionDTO dto) {
        if (dto == null) {
            return null;
        }

        WorkStationPosition entity = new WorkStationPosition();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setPositionIndex(dto.getPositionIndex());
        if(dto.getWorkstationId() != null) {
            entity.setWorkStation(workStationRepository.retrieve(dto.getWorkstationId()));
        }
        if(dto.getDigitalLabelId().length() > 0) {
            entity.setDigitalLabel(digitalLabelRepository.retrieve(dto.getDigitalLabelId()));
        }
        entity.setPositionNo(dto.getPositionNo());

        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        return entity;
    }

    @Override
    public void updateEntityFromDTO(WorkStationPositionDTO dto, WorkStationPosition entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setPositionIndex(dto.getPositionIndex());
        if(dto.getWorkstationId() != null) {
            entity.setWorkStation(workStationRepository.retrieve(dto.getWorkstationId()));
        }
        if(dto.getDigitalLabelId().length() > 0) {
            entity.setDigitalLabel(digitalLabelRepository.retrieve(dto.getDigitalLabelId()));
        }
        entity.setPositionNo(dto.getPositionNo());

        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
    }
}

