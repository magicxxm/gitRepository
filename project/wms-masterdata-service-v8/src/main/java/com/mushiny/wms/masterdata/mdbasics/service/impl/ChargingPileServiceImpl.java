package com.mushiny.wms.masterdata.mdbasics.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.ChargingPileDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.NodeDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.ChargingPileMapper;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.NodeMapper;
import com.mushiny.wms.masterdata.mdbasics.domain.ChargingPile;
import com.mushiny.wms.masterdata.mdbasics.domain.Node;
import com.mushiny.wms.masterdata.mdbasics.repository.ChargingPileRepository;
import com.mushiny.wms.masterdata.mdbasics.repository.NodeRepository;
import com.mushiny.wms.masterdata.mdbasics.service.ChargingPileService;
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
public class ChargingPileServiceImpl implements ChargingPileService {

    private final ChargingPileRepository chargingPileRepository;
    private final ChargingPileMapper chargingPileMapper;
    private final ApplicationContext applicationContext;
    private final NodeRepository nodeRepository;
    private final NodeMapper nodeMapper;

    @Autowired
    public ChargingPileServiceImpl(ChargingPileRepository chargingPileRepository, ChargingPileMapper chargingPileMapper, ApplicationContext applicationContext, NodeRepository nodeRepository, NodeMapper nodeMapper) {
        this.chargingPileRepository = chargingPileRepository;
        this.chargingPileMapper = chargingPileMapper;
        this.applicationContext = applicationContext;
        this.nodeRepository = nodeRepository;
        this.nodeMapper = nodeMapper;
    }

    @Override
    public ChargingPileDTO create(ChargingPileDTO dto) {
        ChargingPile entity = chargingPileMapper.toEntity(dto);
        checkChargingPileName(entity.getWarehouseId(), dto.getName());
        checkChargingPileId(dto.getChargerId(),dto.getChargerType());
        return chargingPileMapper.toDTO(chargingPileRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        chargingPileRepository.delete(chargingPileRepository.retrieve(id));
    }

    @Override
    public ChargingPileDTO update(ChargingPileDTO dto) {
        ChargingPile entity = chargingPileRepository.retrieve(dto.getId());
        if (!entity.getName().equalsIgnoreCase(dto.getName())) {
            checkChargingPileName(entity.getWarehouseId(), dto.getName());
        }
        if (entity.getChargerId()!=dto.getChargerId()||entity.getChargerType()!=dto.getChargerType()) {
            checkChargingPileId(dto.getChargerId(),dto.getChargerType());
        }
        chargingPileMapper.updateEntityFromDTO(dto, entity);
        return chargingPileMapper.toDTO(chargingPileRepository.save(entity));
    }

    @Override
    public ChargingPileDTO retrieve(String id) {
        return chargingPileMapper.toDTO(chargingPileRepository.retrieve(id));
    }

    @Override
    public List<ChargingPileDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<ChargingPile> entities = chargingPileRepository.getBySearchTerm(searchTerm, sort);
        return chargingPileMapper.toDTOList(entities);
    }

    @Override
    public Page<ChargingPileDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if (pageable.getSort() == null) {
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<ChargingPile> entities = chargingPileRepository.getBySearchTerm(searchTerm, pageable);
        return chargingPileMapper.toDTOPage(pageable, entities);
    }

    private void checkChargingPileName(String warehouse, String name) {
        ChargingPile chargingPile = chargingPileRepository.getByName(warehouse, name);
        if (chargingPile != null) {
            throw new ApiException("同仓库下名称唯一");
        }
    }private void checkChargingPileId(int id, int type) {
        ChargingPile chargingPile = chargingPileRepository.getById(id, type);
        if (chargingPile != null) {
            throw new ApiException("类型为"+type+"id为"+id+"的充电桩已经存在");
        }
    }

//    @Override
//    public List<NodeDTO> getPlaceMark(String id) {
//        List<Node> nodeList = nodeRepository.getByMapId(applicationContext.getCurrentWarehouse(),id);
//        return nodeMapper.toDTOList(nodeList);
//    }
}