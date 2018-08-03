package com.mushiny.wms.masterdata.mdbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.PodTypePositionDTO;
import com.mushiny.wms.masterdata.mdbasics.domain.PodTypePosition;
import com.mushiny.wms.masterdata.mdbasics.repository.PodTypeRepository;
import com.mushiny.wms.masterdata.mdbasics.repository.DropZoneRepository;
import com.mushiny.wms.masterdata.mdbasics.repository.StorageLocationTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PodTypePositionMapper implements BaseMapper<PodTypePositionDTO, PodTypePosition> {

    private final StorageLocationTypeRepository storageLocationTypeRepository;
    private final DropZoneRepository dropZoneRepository;
    private final PodTypeRepository podTypeRepository;
    private final ApplicationContext applicationContext;
    private final StorageLocationTypeMapper storageLocationTypeMapper;
    private final DropZoneMapper dropZoneMapper;
    private final PodTypeMapper podTypeMapper;

    @Autowired
    public PodTypePositionMapper(DropZoneMapper dropZoneMapper,
                                 StorageLocationTypeMapper storageLocationTypeMapper,
                                 ApplicationContext applicationContext,
                                 PodTypeRepository podTypeRepository,
                                 StorageLocationTypeRepository storageLocationTypeRepository,
                                 DropZoneRepository dropZoneRepository,
                                 PodTypeMapper podTypeMapper) {
        this.dropZoneMapper = dropZoneMapper;
        this.storageLocationTypeMapper = storageLocationTypeMapper;
        this.applicationContext = applicationContext;
        this.podTypeRepository = podTypeRepository;
        this.storageLocationTypeRepository = storageLocationTypeRepository;
        this.dropZoneRepository = dropZoneRepository;
        this.podTypeMapper = podTypeMapper;
    }

    @Override
    public PodTypePositionDTO toDTO(PodTypePosition entity) {
        if (entity == null) {
            return null;
        }

        PodTypePositionDTO dto = new PodTypePositionDTO(entity);

        dto.setPositionNo(entity.getPositionNo());
        dto.setLevel(entity.getLevel());
        dto.setNumberOfColumns(entity.getNumberOfColumns());
        dto.setFace(entity.getFace());
        dto.setColor(entity.getColor());

        dto.setStorageLocationType(storageLocationTypeMapper.toDTO(entity.getStorageLocationType()));
        dto.setDropZone(dropZoneMapper.toDTO(entity.getDropZone()));
//        dto.setLocationCluster(locationClusterMapper.toDTO(entity.getLocationCluster()));
        dto.setPodType(podTypeMapper.toDTO(entity.getPodType()));
//        dto.setWarehouse(warehouseMapper.toDTO(entity.getWarehouseId()));
        dto.setWarehouseId(entity.getWarehouseId());

        return dto;
    }

    @Override
    public PodTypePosition toEntity(PodTypePositionDTO dto) {

        if (dto == null) {
            return null;
        }

        PodTypePosition entity = new PodTypePosition();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setPositionNo(dto.getPositionNo());
        entity.setNumberOfColumns(dto.getNumberOfColumns());
        entity.setLevel(dto.getLevel());
        entity.setFace(dto.getFace());
        entity.setColor(dto.getColor());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        if (dto.getStorageLocationTypeId() != null) {
            entity.setStorageLocationType(storageLocationTypeRepository.retrieve(dto.getStorageLocationTypeId()));
        }
        if (dto.getDropZoneId() != null) {
            entity.setDropZone(dropZoneRepository.retrieve(dto.getDropZoneId()));
        }
        if (dto.getPodTypeId() != null) {
            entity.setPodType(podTypeRepository.retrieve(dto.getPodTypeId()));
        }

        return entity;
    }

    @Override
    public void updateEntityFromDTO(PodTypePositionDTO dto, PodTypePosition entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setPositionNo(dto.getPositionNo());
        entity.setNumberOfColumns(dto.getNumberOfColumns());
        entity.setColor(dto.getColor());

        if (dto.getDropZoneId() != null) {
            entity.setDropZone(dropZoneRepository.retrieve(dto.getDropZoneId()));
        } else {
            entity.setDropZone(null);
        }

    }
}
