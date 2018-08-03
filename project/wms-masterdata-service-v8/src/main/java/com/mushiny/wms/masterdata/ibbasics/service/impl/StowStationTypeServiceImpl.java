package com.mushiny.wms.masterdata.ibbasics.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.StowStationTypeDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.StowStationTypePositionDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.mapper.StowStationMapper;
import com.mushiny.wms.masterdata.ibbasics.crud.mapper.StowStationTypeMapper;
import com.mushiny.wms.masterdata.ibbasics.crud.mapper.StowStationTypePositionMapper;
import com.mushiny.wms.masterdata.ibbasics.domain.StowStation;
import com.mushiny.wms.masterdata.ibbasics.domain.StowStationType;
import com.mushiny.wms.masterdata.ibbasics.domain.StowStationTypePosition;
import com.mushiny.wms.masterdata.ibbasics.exception.InBoundException;
import com.mushiny.wms.masterdata.ibbasics.repository.StowStationRepository;
import com.mushiny.wms.masterdata.ibbasics.repository.StowStationTypeRepository;
import com.mushiny.wms.masterdata.ibbasics.service.StowStationTypeService;
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
public class StowStationTypeServiceImpl implements StowStationTypeService {

    private final StowStationTypeRepository stowStationTypeRepository;
    private final ApplicationContext applicationContext;
    private final StowStationTypeMapper stowStationTypeMapper;
    private final StowStationTypePositionMapper stowStationTypePositionMapper;
    private final StowStationRepository stowStationRepository;
    private final StowStationMapper stowStationMapper;

    @Autowired
    public StowStationTypeServiceImpl(StowStationTypeRepository stowStationTypeRepository,
                                      ApplicationContext applicationContext,
                                      StowStationTypeMapper stowStationTypeMapper,
                                      StowStationTypePositionMapper stowStationTypePositionMapper, StowStationRepository stowStationRepository, StowStationMapper stowStationMapper) {
        this.stowStationTypeRepository = stowStationTypeRepository;
        this.applicationContext = applicationContext;
        this.stowStationTypeMapper = stowStationTypeMapper;
        this.stowStationTypePositionMapper = stowStationTypePositionMapper;
        this.stowStationRepository = stowStationRepository;
        this.stowStationMapper = stowStationMapper;
    }

    @Override
    public StowStationTypeDTO create(StowStationTypeDTO dto) {
        StowStationType entity = stowStationTypeMapper.toEntity(dto);
        checkStowStationName(entity.getWarehouseId(), entity.getName());
        for (StowStationTypePositionDTO stowStationTypePositionDTO : dto.getPositions()) {
            entity.addPosition(stowStationTypePositionMapper.toEntity(stowStationTypePositionDTO));
        }
        return stowStationTypeMapper.toDTO(stowStationTypeRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        StowStationType entity = stowStationTypeRepository.retrieve(id);
        stowStationTypeRepository.delete(entity);
    }

    @Override
    public StowStationTypeDTO update(StowStationTypeDTO dto) {
        StowStationType entity = stowStationTypeRepository.retrieve(dto.getId());
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkStowStationName(entity.getWarehouseId(), dto.getName());
        }
        stowStationTypeMapper.updateEntityFromDTO(dto, entity);
        entity.getStowStationType().clear();
        List<StowStationTypePosition> positions = stowStationTypePositionMapper.toEntityList(dto.getPositions());
        for (StowStationTypePosition position : positions) {
            entity.addPosition(position);
        }
        return stowStationTypeMapper.toDTO(stowStationTypeRepository.save(entity));
    }

    @Override
    public StowStationTypeDTO retrieve(String id) {
        StowStationType entity = stowStationTypeRepository.retrieve(id);
        StowStationTypeDTO dto = stowStationTypeMapper.toDTO(entity);
        dto.setPositions(stowStationTypePositionMapper.toDTOList(entity.getStowStationType()));
        return dto;
    }

    @Override
    public List<StowStationTypeDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<StowStationType> entities = stowStationTypeRepository.getBySearchTerm(searchTerm, sort);
        return stowStationTypeMapper.toDTOList(entities);
    }

    @Override
    public Page<StowStationTypeDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if(pageable.getSort()==null){
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<StowStationType> entities = stowStationTypeRepository.getBySearchTerm(searchTerm, pageable);
        Page<StowStationTypeDTO> dtoPage=stowStationTypeMapper.toDTOPage(pageable, entities);
        if(dtoPage.getContent()!=null){
            for(StowStationTypeDTO dto:dtoPage){
             List<StowStation> stowStations= stowStationRepository.getByStowStationTypeId(dto.getId());
             dto.setStowStations(stowStationMapper.toDTOList(stowStations));
            }
        }
        return dtoPage;
    }

    @Override
    public List<StowStationTypeDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        List<StowStationType> entities = stowStationTypeRepository.getList(null, sort);
        return stowStationTypeMapper.toDTOList(entities);
    }

    private void checkStowStationName(String warehouse, String name) {
        StowStationType stowStation = stowStationTypeRepository.getByName(warehouse, name);
        if (stowStation != null) {
            throw new ApiException(InBoundException.EX_MD_IN_STOW_STATION_NAME_UNIQUE.toString(), name);
        }
    }
}