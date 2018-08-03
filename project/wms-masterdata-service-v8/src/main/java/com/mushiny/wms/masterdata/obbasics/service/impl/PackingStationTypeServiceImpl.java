package com.mushiny.wms.masterdata.obbasics.service.impl;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PackingStationTypeDTO;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PackingStationTypePositionDTO;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.PackingStationMapper;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.PackingStationTypeMapper;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.PackingStationTypePositionMapper;
import com.mushiny.wms.masterdata.obbasics.domain.PackingStation;
import com.mushiny.wms.masterdata.obbasics.domain.PackingStationType;
import com.mushiny.wms.masterdata.obbasics.domain.PackingStationTypePosition;
import com.mushiny.wms.masterdata.obbasics.exception.OutBoundException;
import com.mushiny.wms.masterdata.obbasics.repository.PackingStationRepository;
import com.mushiny.wms.masterdata.obbasics.repository.PackingStationTypeRepository;
import com.mushiny.wms.masterdata.obbasics.service.PackingStationTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PackingStationTypeServiceImpl implements PackingStationTypeService {

    private final PackingStationTypeRepository packingStationTypeRepository;
    private final PackingStationRepository packingStationRepository;
    private final PackingStationMapper packingStationMapper;
    private final PackingStationTypeMapper packingStationTypeMapper;
    private final PackingStationTypePositionMapper packingStationTypePositionMapper;

    @Autowired
    public PackingStationTypeServiceImpl(PackingStationTypeRepository packingStationTypeRepository,
                                         PackingStationTypeMapper packingStationTypeMapper,
                                         PackingStationRepository packingStationRepository,
                                         PackingStationMapper packingStationMapper,
                                         PackingStationTypePositionMapper packingStationTypePositionMapper) {
        this.packingStationTypeRepository = packingStationTypeRepository;
        this.packingStationTypeMapper = packingStationTypeMapper;
        this.packingStationRepository = packingStationRepository;
        this.packingStationMapper = packingStationMapper;
        this.packingStationTypePositionMapper = packingStationTypePositionMapper;
    }

    @Override
    public PackingStationTypeDTO create(PackingStationTypeDTO dto) {
        PackingStationType entity = packingStationTypeMapper.toEntity(dto);
        checkPackingStationTypeName(entity.getWarehouseId(), entity.getName());
        for (PackingStationTypePositionDTO positionDTO : dto.getPositions()) {
            entity.addPosition(packingStationTypePositionMapper.toEntity(positionDTO));
        }
        return packingStationTypeMapper.toDTO(packingStationTypeRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        PackingStationType entity = packingStationTypeRepository.retrieve(id);
        packingStationTypeRepository.delete(entity);
    }

    @Override
    public PackingStationTypeDTO update(PackingStationTypeDTO dto) {
        PackingStationType entity = packingStationTypeRepository.retrieve(dto.getId());
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkPackingStationTypeName(entity.getWarehouseId(), dto.getName());
        }
        packingStationTypeMapper.updateEntityFromDTO(dto, entity);
        entity.getPositions().clear();
        List<PackingStationTypePosition> positions = packingStationTypePositionMapper.toEntityList(dto.getPositions());
        for (PackingStationTypePosition position : positions) {
            entity.addPosition(position);
        }
        return packingStationTypeMapper.toDTO(packingStationTypeRepository.save(entity));
    }

    @Override
    public PackingStationTypeDTO retrieve(String id) {
        PackingStationType entity = packingStationTypeRepository.retrieve(id);
        PackingStationTypeDTO dto = packingStationTypeMapper.toDTO(entity);
        dto.setPositions(packingStationTypePositionMapper.toDTOList(entity.getPositions()));
        return dto;
    }

    @Override
    public List<PackingStationTypeDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        List<PackingStationType> entities = packingStationTypeRepository.getNotLockList(null, sort);
        return packingStationTypeMapper.toDTOList(entities);
    }

    @Override
    public List<PackingStationTypeDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<PackingStationType> entities = packingStationTypeRepository.getBySearchTerm(searchTerm, sort);
        return packingStationTypeMapper.toDTOList(entities);
    }

    @Override
    public Page<PackingStationTypeDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if(pageable.getSort()==null){
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<PackingStationType> entityPage = packingStationTypeRepository.getBySearchTerm(searchTerm,pageable);
        Page<PackingStationTypeDTO> dtoPage = packingStationTypeMapper.toDTOPage(pageable, entityPage);
        if (dtoPage.getContent() != null) {
            for (PackingStationTypeDTO dto : dtoPage.getContent()) {
                List<PackingStation> packingStations = packingStationRepository.getByTypeId(dto.getId());
                dto.setPackingStations(packingStationMapper.toDTOList(packingStations));
            }
        }
        return dtoPage;
    }

    private void checkPackingStationTypeName(String warehouse, String name) {
        PackingStationType packingStationType = packingStationTypeRepository.getByName(warehouse, name);
        if (packingStationType != null) {
            throw new ApiException(OutBoundException.EX_MD_OB_PACKING_STATION_TYPE_NAME_UNIQUE.toString(), name);
        }
    }
}
