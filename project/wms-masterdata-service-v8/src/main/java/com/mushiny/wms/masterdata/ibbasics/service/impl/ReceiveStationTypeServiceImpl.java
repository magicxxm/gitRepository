package com.mushiny.wms.masterdata.ibbasics.service.impl;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceiveStationTypeDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceiveStationTypePositionDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.mapper.ReceiveStationMapper;
import com.mushiny.wms.masterdata.ibbasics.crud.mapper.ReceiveStationTypeMapper;
import com.mushiny.wms.masterdata.ibbasics.crud.mapper.ReceiveStationTypePositionMapper;
import com.mushiny.wms.masterdata.ibbasics.domain.ReceiveStation;
import com.mushiny.wms.masterdata.ibbasics.domain.ReceiveStationType;
import com.mushiny.wms.masterdata.ibbasics.domain.ReceiveStationTypePosition;
import com.mushiny.wms.masterdata.ibbasics.exception.InBoundException;
import com.mushiny.wms.masterdata.ibbasics.repository.ReceiveStationRepository;
import com.mushiny.wms.masterdata.ibbasics.repository.ReceiveStationTypeRepository;
import com.mushiny.wms.masterdata.ibbasics.service.ReceiveStationTypeService;
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
public class ReceiveStationTypeServiceImpl implements ReceiveStationTypeService {

    private final ReceiveStationTypeRepository receivingStationTypeRepository;
    private final ReceiveStationTypeMapper receivingStationTypeMapper;
    private final ReceiveStationTypePositionMapper receivingStationTypePositionMapper;
    private final ReceiveStationRepository receiveStationRepository;
    private final ReceiveStationMapper receiveStationMapper;

    @Autowired
    public ReceiveStationTypeServiceImpl(ReceiveStationTypeRepository receivingStationTypeRepository,
                                         ReceiveStationTypeMapper receivingStationTypeMapper,
                                         ReceiveStationTypePositionMapper receivingStationTypePositionMapper, ReceiveStationRepository receiveStationRepository, ReceiveStationMapper receiveStationMapper) {
        this.receivingStationTypeRepository = receivingStationTypeRepository;
        this.receivingStationTypeMapper = receivingStationTypeMapper;
        this.receivingStationTypePositionMapper = receivingStationTypePositionMapper;
        this.receiveStationRepository = receiveStationRepository;
        this.receiveStationMapper = receiveStationMapper;
    }

    @Override
    public ReceiveStationTypeDTO create(ReceiveStationTypeDTO dto) {
        ReceiveStationType entity = receivingStationTypeMapper.toEntity(dto);
        checkReceivingStationTypeName(entity.getWarehouseId(), entity.getName());
        for (ReceiveStationTypePositionDTO positionDTO : dto.getPositions()) {
            entity.addPosition(receivingStationTypePositionMapper.toEntity(positionDTO));
        }
        return receivingStationTypeMapper.toDTO(receivingStationTypeRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        ReceiveStationType entity = receivingStationTypeRepository.retrieve(id);
        receivingStationTypeRepository.delete(entity);
    }

    @Override
    public ReceiveStationTypeDTO update(ReceiveStationTypeDTO dto) {
        ReceiveStationType entity = receivingStationTypeRepository.retrieve(dto.getId());
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkReceivingStationTypeName(entity.getWarehouseId(), dto.getName());
        }
        receivingStationTypeMapper.updateEntityFromDTO(dto, entity);
        entity.getPositions().clear();
        List<ReceiveStationTypePosition> positions = receivingStationTypePositionMapper.toEntityList(dto.getPositions());
        for (ReceiveStationTypePosition position : positions) {
            entity.addPosition(position);
        }
        return receivingStationTypeMapper.toDTO(receivingStationTypeRepository.save(entity));
    }

    @Override
    public ReceiveStationTypeDTO retrieve(String id) {
        ReceiveStationType entity = receivingStationTypeRepository.retrieve(id);
        ReceiveStationTypeDTO dto = receivingStationTypeMapper.toDTO(entity);
        dto.setPositions(receivingStationTypePositionMapper.toDTOList(entity.getPositions()));
        return dto;
    }

    @Override
    public List<ReceiveStationTypeDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<ReceiveStationType> entities = receivingStationTypeRepository.getBySearchTerm(searchTerm, sort);
        return receivingStationTypeMapper.toDTOList(entities);
    }

    @Override
    public Page<ReceiveStationTypeDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if(pageable.getSort()==null){
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<ReceiveStationType> entities = receivingStationTypeRepository.getBySearchTerm(searchTerm, pageable);
        Page<ReceiveStationTypeDTO> dtoPage=receivingStationTypeMapper.toDTOPage(pageable, entities);
        if(dtoPage.getContent()!=null){
            for(ReceiveStationTypeDTO dto:dtoPage){
               List<ReceiveStation> receiveStations=receiveStationRepository.getByReceiveStationTypeId(dto.getId());
               dto.setReceiveStations(receiveStationMapper.toDTOList(receiveStations));
            }
        }
        return dtoPage;
    }

    @Override
    public List<ReceiveStationTypeDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        List<ReceiveStationType> entities = receivingStationTypeRepository.getList(null, sort);
        return receivingStationTypeMapper.toDTOList(entities);
    }

    private void checkReceivingStationTypeName(String warehouse, String name) {
        ReceiveStationType receivingStationType = receivingStationTypeRepository.getByName(warehouse, name);
        if (receivingStationType != null) {
            throw new ApiException(InBoundException.EX_MD_IN_RECEIVE_STATION_TYPE_NAME_UNIQUE.toString(), name);
        }
    }
}
