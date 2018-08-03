package com.mushiny.wms.masterdata.ibbasics.service.impl;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.IBPStationTypeDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.IBPStationTypePositionDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.mapper.IBPStationMapper;
import com.mushiny.wms.masterdata.ibbasics.crud.mapper.IBPStationTypeMapper;
import com.mushiny.wms.masterdata.ibbasics.crud.mapper.IBPStationTypePositionMapper;
import com.mushiny.wms.masterdata.ibbasics.domain.IBPStation;
import com.mushiny.wms.masterdata.ibbasics.domain.IBPStationType;
import com.mushiny.wms.masterdata.ibbasics.domain.IBPStationTypePosition;
import com.mushiny.wms.masterdata.ibbasics.exception.InBoundException;
import com.mushiny.wms.masterdata.ibbasics.repository.IBPStationRepository;
import com.mushiny.wms.masterdata.ibbasics.repository.IBPStationTypeRepository;
import com.mushiny.wms.masterdata.ibbasics.service.IBPStationTypeService;
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
public class IBPStationTypeServiceImpl implements IBPStationTypeService {

    private final IBPStationTypeRepository ibpStationTypeRepository;
    private final IBPStationTypeMapper ibpStationTypeMapper;
    private final IBPStationTypePositionMapper ibpStationTypePositionMapper;
    private final IBPStationRepository ibpStationRepository;
    private final IBPStationMapper ibpStationMapper;

    @Autowired
    public IBPStationTypeServiceImpl(IBPStationTypeRepository ibpStationTypeRepository,
                                     IBPStationTypeMapper ibpStationTypeMapper,
                                     IBPStationTypePositionMapper ibpStationTypePositionMapper, IBPStationRepository ibpStationRepository, IBPStationMapper ibpStationMapper) {
        this.ibpStationTypeRepository = ibpStationTypeRepository;
        this.ibpStationTypeMapper = ibpStationTypeMapper;
        this.ibpStationTypePositionMapper = ibpStationTypePositionMapper;
        this.ibpStationRepository = ibpStationRepository;
        this.ibpStationMapper = ibpStationMapper;
    }

    @Override
    public IBPStationTypeDTO create(IBPStationTypeDTO dto) {
        IBPStationType entity = ibpStationTypeMapper.toEntity(dto);
        checkPackingStationTypeName(entity.getWarehouseId(), entity.getName());
        for (IBPStationTypePositionDTO positionDTO : dto.getPositions()) {
            entity.addPosition(ibpStationTypePositionMapper.toEntity(positionDTO));
        }
        return ibpStationTypeMapper.toDTO(ibpStationTypeRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        IBPStationType entity = ibpStationTypeRepository.retrieve(id);
        ibpStationTypeRepository.delete(entity);
    }

    @Override
    public IBPStationTypeDTO update(IBPStationTypeDTO dto) {
        IBPStationType entity = ibpStationTypeRepository.retrieve(dto.getId());
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkPackingStationTypeName(entity.getWarehouseId(), dto.getName());
        }
        ibpStationTypeMapper.updateEntityFromDTO(dto, entity);
        entity.getPositions().clear();
        List<IBPStationTypePosition> positions = ibpStationTypePositionMapper.toEntityList(dto.getPositions());
        for (IBPStationTypePosition position : positions) {
            entity.addPosition(position);
        }
        return ibpStationTypeMapper.toDTO(ibpStationTypeRepository.save(entity));
    }

    @Override
    public IBPStationTypeDTO retrieve(String id) {
        IBPStationType entity = ibpStationTypeRepository.retrieve(id);
        IBPStationTypeDTO dto = ibpStationTypeMapper.toDTO(entity);
        dto.setPositions(ibpStationTypePositionMapper.toDTOList(entity.getPositions()));
        return dto;
    }

    @Override
    public List<IBPStationTypeDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        List<IBPStationType> entities = ibpStationTypeRepository.getNotLockList(null, sort);
        return ibpStationTypeMapper.toDTOList(entities);
    }

    @Override
    public List<IBPStationTypeDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<IBPStationType> entities = ibpStationTypeRepository.getBySearchTerm(searchTerm, sort);
        return ibpStationTypeMapper.toDTOList(entities);
    }

    @Override
    public Page<IBPStationTypeDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if(pageable.getSort()==null){
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<IBPStationType> entities = ibpStationTypeRepository.getBySearchTerm(searchTerm, pageable);
        Page<IBPStationTypeDTO> dtoPage=ibpStationTypeMapper.toDTOPage(pageable, entities);
        if(dtoPage.getContent()!=null){
             for(IBPStationTypeDTO dto:dtoPage){
                 List<IBPStation> ibpStations= ibpStationRepository.getByTypeId(dto.getId());
                 dto.setIbpStations(ibpStationMapper.toDTOList(ibpStations));
             }
        }
        return dtoPage;
    }

    private void checkPackingStationTypeName(String warehouse, String name) {
        IBPStationType ibpStationType = ibpStationTypeRepository.getByName(warehouse, name);
        if (ibpStationType != null) {
            throw new ApiException(InBoundException.EX_MD_IN_IBP_STATION_TYPE_NAME_UNIQUE.toString(), name);
        }
    }
}
