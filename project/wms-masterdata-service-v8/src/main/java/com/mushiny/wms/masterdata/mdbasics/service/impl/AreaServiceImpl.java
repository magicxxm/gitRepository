package com.mushiny.wms.masterdata.mdbasics.service.impl;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.AreaDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.DropZoneDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.AreaMapper;
import com.mushiny.wms.masterdata.mdbasics.domain.Area;
import com.mushiny.wms.masterdata.mdbasics.domain.DropZone;
import com.mushiny.wms.masterdata.mdbasics.exception.MasterDataException;
import com.mushiny.wms.masterdata.mdbasics.repository.AreaRepository;
import com.mushiny.wms.masterdata.mdbasics.service.AreaService;
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
public class AreaServiceImpl implements AreaService {

    private final AreaRepository areaRepository;
    private final ApplicationContext applicationContext;
    private final AreaMapper areaMapper;

    @Autowired
    public AreaServiceImpl(AreaRepository areaRepository,
                           ApplicationContext applicationContext,
                           AreaMapper areaMapper) {
        this.areaRepository = areaRepository;
        this.applicationContext = applicationContext;
        this.areaMapper = areaMapper;
    }

    @Override
    public AreaDTO create(AreaDTO dto) {
        Area entity = areaMapper.toEntity(dto);
        checkAreaName(entity.getWarehouseId(), entity.getClientId(), entity.getName());
        return areaMapper.toDTO(areaRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        Area entity = areaRepository.retrieve(id);
        areaRepository.delete(entity);
    }

    @Override
    public AreaDTO update(AreaDTO dto) {
        Area entity = areaRepository.retrieve(dto.getId());
        if (!(entity.getName().equalsIgnoreCase(dto.getName())
                && entity.getClientId().equalsIgnoreCase(dto.getClientId()))) {
            String client = dto.getClientId();
            checkAreaName(entity.getWarehouseId(), client, dto.getName());
        }
        areaMapper.updateEntityFromDTO(dto, entity);
        return areaMapper.toDTO(areaRepository.save(entity));
    }

    @Override
    public AreaDTO retrieve(String id) {
        return areaMapper.toDTO(areaRepository.retrieve(id));
    }

    @Override
    public List<AreaDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<Area> entities = areaRepository.getBySearchTerm(searchTerm, sort);
        return areaMapper.toDTOList(entities);
    }

    @Override
    public Page<AreaDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if (pageable.getSort() == null) {
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<Area> entities = areaRepository.getBySearchTerm(searchTerm, pageable);
        return areaMapper.toDTOPage(pageable, entities);
    }

    @Override
    public List<AreaDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        String warehouse = applicationContext.getCurrentWarehouse();
        List<Area> entities = areaRepository.getList(warehouse, null, sort);
        return areaMapper.toDTOList(entities);
    }

    @Override
    public List<AreaDTO> getByClientId(String clientId) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        applicationContext.isCurrentClient(clientId);
        List<Area> entities = areaRepository.getList(clientId, sort);
        return areaMapper.toDTOList(entities);
    }

    private void checkAreaName(String warehouse, String client, String areaName) {
        Area area = areaRepository.getByName(warehouse, client, areaName);
        if (area != null) {
            throw new ApiException(MasterDataException.EX_MD_AREA_NAME_UNIQUE.toString(), areaName);
        }
    }
}