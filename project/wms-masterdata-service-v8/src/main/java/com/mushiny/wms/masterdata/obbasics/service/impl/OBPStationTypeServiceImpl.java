package com.mushiny.wms.masterdata.obbasics.service.impl;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.obbasics.crud.dto.OBPStationTypeDTO;
import com.mushiny.wms.masterdata.obbasics.crud.dto.OBPStationTypePositionDTO;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.OBPStationMapper;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.OBPStationTypeMapper;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.OBPStationTypePositionMapper;
import com.mushiny.wms.masterdata.obbasics.domain.OBPStation;
import com.mushiny.wms.masterdata.obbasics.domain.OBPStationType;
import com.mushiny.wms.masterdata.obbasics.domain.OBPStationTypePosition;
import com.mushiny.wms.masterdata.obbasics.exception.OutBoundException;
import com.mushiny.wms.masterdata.obbasics.repository.OBPStationRepository;
import com.mushiny.wms.masterdata.obbasics.repository.PackingStationRepository;
import com.mushiny.wms.masterdata.obbasics.repository.OBPStationTypeRepository;
import com.mushiny.wms.masterdata.obbasics.service.OBPStationTypeService;
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
public class OBPStationTypeServiceImpl implements OBPStationTypeService {

    private final OBPStationTypeRepository obpStationTypeRepository;
    private final PackingStationRepository packingStationRepository;
    private final OBPStationTypeMapper obpStationTypeMapper;
    private final OBPStationTypePositionMapper obpStationTypePositionMapper;
    private final OBPStationRepository obpStationRepository;
    private final OBPStationMapper obpStationMapper;

    @Autowired
    public OBPStationTypeServiceImpl(OBPStationTypeRepository obpStationTypeRepository,
                                     OBPStationTypeMapper obpStationTypeMapper,
                                     PackingStationRepository packingStationRepository,
                                     OBPStationTypePositionMapper obpStationTypePositionMapper, OBPStationRepository obpStationRepository, OBPStationMapper obpStationMapper) {
        this.obpStationTypeRepository = obpStationTypeRepository;
        this.obpStationTypeMapper = obpStationTypeMapper;
        this.packingStationRepository = packingStationRepository;
        this.obpStationTypePositionMapper = obpStationTypePositionMapper;
        this.obpStationRepository = obpStationRepository;
        this.obpStationMapper = obpStationMapper;
    }

    @Override
    public OBPStationTypeDTO create(OBPStationTypeDTO dto) {
        OBPStationType entity = obpStationTypeMapper.toEntity(dto);
        checkPackingStationTypeName(entity.getWarehouseId(), entity.getName());
        for (OBPStationTypePositionDTO positionDTO : dto.getPositions()) {
            entity.addPosition(obpStationTypePositionMapper.toEntity(positionDTO));
        }
        return obpStationTypeMapper.toDTO(obpStationTypeRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        OBPStationType entity = obpStationTypeRepository.retrieve(id);
        obpStationTypeRepository.delete(entity);
    }

    @Override
    public OBPStationTypeDTO update(OBPStationTypeDTO dto) {
        OBPStationType entity = obpStationTypeRepository.retrieve(dto.getId());
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkPackingStationTypeName(entity.getWarehouseId(), dto.getName());
        }
        obpStationTypeMapper.updateEntityFromDTO(dto, entity);
        entity.getPositions().clear();
        List<OBPStationTypePosition> positions = obpStationTypePositionMapper.toEntityList(dto.getPositions());
        for (OBPStationTypePosition position : positions) {
            entity.addPosition(position);
        }
        return obpStationTypeMapper.toDTO(obpStationTypeRepository.save(entity));
    }

    @Override
    public OBPStationTypeDTO retrieve(String id) {
        OBPStationType entity = obpStationTypeRepository.retrieve(id);
        OBPStationTypeDTO dto = obpStationTypeMapper.toDTO(entity);
        dto.setPositions(obpStationTypePositionMapper.toDTOList(entity.getPositions()));
        return dto;
    }

    @Override
    public List<OBPStationTypeDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        List<OBPStationType> entities = obpStationTypeRepository.getNotLockList(null, sort);
        return obpStationTypeMapper.toDTOList(entities);
    }

    @Override
    public List<OBPStationTypeDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<OBPStationType> entities = obpStationTypeRepository.getBySearchTerm(searchTerm, sort);
        return obpStationTypeMapper.toDTOList(entities);
    }

    @Override
    public Page<OBPStationTypeDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if (pageable.getSort() == null) {
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<OBPStationType> entities = obpStationTypeRepository.getBySearchTerm(searchTerm, pageable);
        Page<OBPStationTypeDTO> dtoPage=obpStationTypeMapper.toDTOPage(pageable, entities);
        if(dtoPage.getContent()!=null){
            for(OBPStationTypeDTO dto:dtoPage){
             List<OBPStation> obpStations= obpStationRepository.getByTypeId(dto.getId());
             dto.setObpStations(obpStationMapper.toDTOList(obpStations));
            }
        }
        return dtoPage;
    }

    private void checkPackingStationTypeName(String warehouse, String name) {
        OBPStationType obpStationType = obpStationTypeRepository.getByName(warehouse, name);
        if (obpStationType != null) {
            throw new ApiException(OutBoundException.EX_MD_OB_PICK_STATION_NAME_UNIQUE.toString(), name);
        }
    }
}
