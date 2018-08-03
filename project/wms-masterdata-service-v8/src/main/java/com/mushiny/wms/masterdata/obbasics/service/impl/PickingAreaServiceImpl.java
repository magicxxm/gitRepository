package com.mushiny.wms.masterdata.obbasics.service.impl;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PickingAreaDTO;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PickingAreaPositionDTO;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.PickingAreaMapper;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.PickingAreaPositionMapper;
import com.mushiny.wms.masterdata.obbasics.domain.PickingArea;
import com.mushiny.wms.masterdata.obbasics.domain.PickingAreaPosition;
import com.mushiny.wms.masterdata.obbasics.exception.OutBoundException;
import com.mushiny.wms.masterdata.obbasics.repository.PickingAreaRepository;
import com.mushiny.wms.masterdata.obbasics.service.PickingAreaService;
import com.mushiny.wms.masterdata.general.domain.Client;
import com.mushiny.wms.masterdata.general.domain.Warehouse;
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
public class PickingAreaServiceImpl implements PickingAreaService {

    private final PickingAreaRepository pickingAreaRepository;
    private final ApplicationContext applicationContext;
    private final PickingAreaMapper pickingAreaMapper;
    private final PickingAreaPositionMapper pickingAreaPositionMapper;

    @Autowired
    public PickingAreaServiceImpl(PickingAreaRepository pickingAreaRepository,
                                  ApplicationContext applicationContext,
                                  PickingAreaMapper pickingAreaMapper,
                                  PickingAreaPositionMapper pickingAreaPositionMapper) {
        this.pickingAreaRepository = pickingAreaRepository;
        this.applicationContext = applicationContext;
        this.pickingAreaMapper = pickingAreaMapper;
        this.pickingAreaPositionMapper = pickingAreaPositionMapper;
    }

    @Override
    public PickingAreaDTO create(PickingAreaDTO dto) {
        PickingArea entity = pickingAreaMapper.toEntity(dto);
        checkPickingAreaName(entity.getWarehouseId(), entity.getClientId(), entity.getName());
        for (PickingAreaPositionDTO positionDTO : dto.getPositions()) {
            entity.addPosition(pickingAreaPositionMapper.toEntity(positionDTO));
        }
        return pickingAreaMapper.toDTO(pickingAreaRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        PickingArea entity = pickingAreaRepository.retrieve(id);
        pickingAreaRepository.delete(entity);
    }

    @Override
    public PickingAreaDTO update(PickingAreaDTO dto) {
        PickingArea entity = pickingAreaRepository.retrieve(dto.getId());
        if (!entity.getName().equalsIgnoreCase(dto.getName())) {
            checkPickingAreaName(entity.getWarehouseId(), entity.getClientId(), dto.getName());
        }
        pickingAreaMapper.updateEntityFromDTO(dto, entity);
        entity.getPositions().clear();
        List<PickingAreaPosition> positions = pickingAreaPositionMapper.toEntityList(dto.getPositions());
        for (PickingAreaPosition position : positions) {
            entity.addPosition(position);
        }
        return pickingAreaMapper.toDTO(pickingAreaRepository.save(entity));
    }

    @Override
    public PickingAreaDTO retrieve(String id) {
        PickingArea entity = pickingAreaRepository.retrieve(id);
        PickingAreaDTO dto = pickingAreaMapper.toDTO(entity);
        dto.setPositions(pickingAreaPositionMapper.toDTOList(entity.getPositions()));
        return dto;
    }

    @Override
    public List<PickingAreaDTO> getByClientId(String clientId) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        applicationContext.isCurrentClient(clientId);
        List<PickingArea> entities = pickingAreaRepository.getNotLockList(clientId, sort);
        return pickingAreaMapper.toDTOList(entities);
    }

    @Override
    public List<PickingAreaDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<PickingArea> entities = pickingAreaRepository.getBySearchTerm(searchTerm, sort);
        return pickingAreaMapper.toDTOList(entities);
    }

    @Override
    public Page<PickingAreaDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if(pageable.getSort()==null){
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<PickingArea> entities = pickingAreaRepository.getBySearchTerm(searchTerm,pageable);
        return pickingAreaMapper.toDTOPage(pageable, entities);
    }

    private void checkPickingAreaName(String warehouse, String client, String name) {
        PickingArea pickingArea = pickingAreaRepository.getByName(warehouse, client, name);
        if (pickingArea != null) {
            throw new ApiException(OutBoundException.EX_MD_OB_PICKING_AREA_NAME_UNIQUE.toString(), name);
        }
    }
}
