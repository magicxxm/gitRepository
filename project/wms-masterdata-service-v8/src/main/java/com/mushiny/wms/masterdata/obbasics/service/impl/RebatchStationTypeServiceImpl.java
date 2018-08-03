package com.mushiny.wms.masterdata.obbasics.service.impl;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.obbasics.crud.dto.RebatchStationTypeDTO;
import com.mushiny.wms.masterdata.obbasics.crud.dto.RebatchStationTypePositionDTO;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.RebatchStationMapper;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.RebatchStationTypeMapper;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.RebatchStationTypePositionMapper;
import com.mushiny.wms.masterdata.obbasics.domain.RebatchStation;
import com.mushiny.wms.masterdata.obbasics.domain.RebatchStationType;
import com.mushiny.wms.masterdata.obbasics.domain.RebatchStationTypePosition;
import com.mushiny.wms.masterdata.obbasics.exception.OutBoundException;
import com.mushiny.wms.masterdata.obbasics.repository.RebatchStationRepository;
import com.mushiny.wms.masterdata.obbasics.repository.RebatchStationTypeRepository;
import com.mushiny.wms.masterdata.obbasics.service.RebatchStationTypeService;
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
public class RebatchStationTypeServiceImpl implements RebatchStationTypeService {

    private final RebatchStationTypeRepository rebatchStationTypeRepository;
    private final RebatchStationTypeMapper rebatchStationTypeMapper;
    private final RebatchStationTypePositionMapper rebatchStationTypePositionMapper;
    private final RebatchStationRepository rebatchStationRepository;
    private final RebatchStationMapper rebatchStationMapper;

    @Autowired
    public RebatchStationTypeServiceImpl(RebatchStationTypeRepository rebatchStationTypeRepository,
                                         RebatchStationTypeMapper rebatchStationTypeMapper,
                                         RebatchStationTypePositionMapper rebatchStationTypePositionMapper, RebatchStationRepository rebatchStationRepository, RebatchStationMapper rebatchStationMapper) {
        this.rebatchStationTypeRepository = rebatchStationTypeRepository;
        this.rebatchStationTypeMapper = rebatchStationTypeMapper;
        this.rebatchStationTypePositionMapper = rebatchStationTypePositionMapper;
        this.rebatchStationRepository = rebatchStationRepository;
        this.rebatchStationMapper = rebatchStationMapper;
    }

    @Override
    public RebatchStationTypeDTO create(RebatchStationTypeDTO dto) {
        RebatchStationType entity = rebatchStationTypeMapper.toEntity(dto);
        checkRebatchStationTypeName(entity.getWarehouseId(), entity.getName());
        for (RebatchStationTypePositionDTO positionDTO : dto.getPositions()) {
            entity.addPosition(rebatchStationTypePositionMapper.toEntity(positionDTO));
        }
        return rebatchStationTypeMapper.toDTO(rebatchStationTypeRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        RebatchStationType entity = rebatchStationTypeRepository.retrieve(id);
        rebatchStationTypeRepository.delete(entity);
    }

    @Override
    public RebatchStationTypeDTO update(RebatchStationTypeDTO dto) {
        RebatchStationType entity = rebatchStationTypeRepository.retrieve(dto.getId());
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkRebatchStationTypeName(entity.getWarehouseId(), dto.getName());
        }
        rebatchStationTypeMapper.updateEntityFromDTO(dto, entity);
        entity.getPositions().clear();
        List<RebatchStationTypePosition> positions = rebatchStationTypePositionMapper.toEntityList(dto.getPositions());
        for (RebatchStationTypePosition position : positions) {
            entity.addPosition(position);
        }
        return rebatchStationTypeMapper.toDTO(rebatchStationTypeRepository.save(entity));
    }

    @Override
    public RebatchStationTypeDTO retrieve(String id) {
        RebatchStationType entity = rebatchStationTypeRepository.retrieve(id);
        RebatchStationTypeDTO dto = rebatchStationTypeMapper.toDTO(entity);
        dto.setPositions(rebatchStationTypePositionMapper.toDTOList(entity.getPositions()));
        return dto;
    }

    @Override
    public List<RebatchStationTypeDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        List<RebatchStationType> entities = rebatchStationTypeRepository.getNotLockList(null, sort);
        return rebatchStationTypeMapper.toDTOList(entities);
    }

    @Override
    public List<RebatchStationTypeDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<RebatchStationType> entities = rebatchStationTypeRepository.getBySearchTerm(searchTerm, sort);
        return rebatchStationTypeMapper.toDTOList(entities);
    }

    @Override
    public Page<RebatchStationTypeDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if(pageable.getSort()==null){
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<RebatchStationType> entityPage = rebatchStationTypeRepository.getBySearchTerm(searchTerm, pageable);
        Page<RebatchStationTypeDTO> dtoPage=rebatchStationTypeMapper.toDTOPage(pageable, entityPage);
        if(dtoPage.getContent()!=null){
           for(RebatchStationTypeDTO dto :dtoPage){
              List<RebatchStation> rebatchStations= rebatchStationRepository.getByRebatchStationTypeId(dto.getId());
              dto.setRebatchStations(rebatchStationMapper.toDTOList(rebatchStations));
           }
        }
        return dtoPage;
    }

    private void checkRebatchStationTypeName(String warehouse, String name) {
        RebatchStationType rebatchStationType = rebatchStationTypeRepository.getByName(warehouse, name);
        if (rebatchStationType != null) {
            throw new ApiException(OutBoundException.EX_MD_OB_REBATCH_STATION_TYPE_NAME_UNIQUE.toString(), name);
        }
    }
}
