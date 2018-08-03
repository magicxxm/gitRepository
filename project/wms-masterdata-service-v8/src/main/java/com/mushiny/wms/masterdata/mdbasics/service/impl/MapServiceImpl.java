package com.mushiny.wms.masterdata.mdbasics.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.MapDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.MapMapper;
import com.mushiny.wms.masterdata.mdbasics.domain.ChargingPile;
import com.mushiny.wms.masterdata.mdbasics.domain.Map;
import com.mushiny.wms.masterdata.mdbasics.repository.MapRepository;
import com.mushiny.wms.masterdata.mdbasics.service.MapService;
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
public class MapServiceImpl implements MapService {

    private final MapRepository mapRepository;
    private final ApplicationContext applicationContext;
    private final MapMapper mapMapper;

    @Autowired
    public MapServiceImpl(MapRepository mapRepository,
                          ApplicationContext applicationContext,
                          MapMapper mapMapper) {
        this.mapRepository = mapRepository;
        this.applicationContext = applicationContext;
        this.mapMapper = mapMapper;
    }

    @Override
    public MapDTO create(MapDTO dto) {
        Map entity = mapMapper.toEntity(dto);
        checkMapName(dto.getName(), entity.getWarehouseId());
        return mapMapper.toDTO(mapRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        Map entity = mapRepository.retrieve(id);
        mapRepository.delete(entity);
    }

    @Override
    public MapDTO update(MapDTO dto) {
        Map entity = mapRepository.retrieve(dto.getId());
        if (!entity.getName().equalsIgnoreCase(dto.getName())) {
            checkMapName(dto.getName(), entity.getWarehouseId());
        }
        mapMapper.updateEntityFromDTO(dto, entity);
        return mapMapper.toDTO(mapRepository.save(entity));
    }

    @Override
    public MapDTO retrieve(String id) {
        return mapMapper.toDTO(mapRepository.retrieve(id));
    }

    @Override
    public List<MapDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        List<Map> entities = mapRepository.getList(null, sort);
        return mapMapper.toDTOList(entities);
    }

//    @Override
//    public List<MapDTO> getBySectionId(String sectionId) {
//        String warehouse = applicationContext.getCurrentWarehouse();
//        List<Map> entities = mapRepository.getBySectionId(warehouse, sectionId);
//        return mapMapper.toDTOList(entities);
//    }

    @Override
    public List<MapDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<Map> entities = mapRepository.getBySearchTerm(searchTerm, sort);
        return mapMapper.toDTOList(entities);
    }

    @Override
    public Page<MapDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if (pageable.getSort() == null) {
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<Map> entities = mapRepository.getBySearchTerm(searchTerm, pageable);
        return mapMapper.toDTOPage(pageable, entities);
    }

    private void checkMapName(String name, String warehouse) {
        Map map = mapRepository.getByName(name,warehouse);
        if (map != null) {
            throw new ApiException("同一仓库中地图名不能重复！");
        }
    }
}