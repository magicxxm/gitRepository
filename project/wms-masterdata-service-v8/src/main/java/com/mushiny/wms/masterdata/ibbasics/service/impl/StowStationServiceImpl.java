package com.mushiny.wms.masterdata.ibbasics.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.ibbasics.business.StowStationBusiness;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.StowStationDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.StowStationPositionDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.mapper.StowStationMapper;
import com.mushiny.wms.masterdata.ibbasics.crud.mapper.StowStationPositionMapper;
import com.mushiny.wms.masterdata.ibbasics.domain.StowStation;
import com.mushiny.wms.masterdata.ibbasics.domain.StowStationPosition;
import com.mushiny.wms.masterdata.ibbasics.exception.InBoundException;
import com.mushiny.wms.masterdata.ibbasics.repository.StowStationRepository;
import com.mushiny.wms.masterdata.ibbasics.service.StowStationService;
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
public class StowStationServiceImpl implements StowStationService {

    private final StowStationRepository stowStationRepository;
    private final ApplicationContext applicationContext;
    private final StowStationMapper stowStationMapper;
    private final StowStationPositionMapper stowStationPositionMapper;
    private final StowStationBusiness stowStationBusiness;

    @Autowired
    public StowStationServiceImpl(StowStationRepository stowStationRepository,
                                  ApplicationContext applicationContext,
                                  StowStationMapper stowStationMapper,
                                  StowStationPositionMapper stowStationPositionMapper,
                                  StowStationBusiness stowStationBusiness) {
        this.stowStationRepository = stowStationRepository;
        this.applicationContext = applicationContext;
        this.stowStationMapper = stowStationMapper;
        this.stowStationPositionMapper = stowStationPositionMapper;
        this.stowStationBusiness = stowStationBusiness;
    }

    @Override
    public void createMore(StowStationDTO dto) {
        checkStowStationName(dto.getWarehouseId(), dto.getName());
        stowStationBusiness.createMore(dto);
    }

    @Override
    public StowStationDTO create(StowStationDTO dto) {
        return null;
    }

    @Override
    public void delete(String id) {
        StowStation entity = stowStationRepository.retrieve(id);
        if (entity.getOperator() != null) {
            throw new ApiException("该状态的工作站不能删除");
        }
        stowStationRepository.delete(entity);
    }

    @Override
    public StowStationDTO update(StowStationDTO dto) {
        StowStation entity = stowStationRepository.retrieve(dto.getId());
        if (entity.getOperator() != null) {
            throw new ApiException("该状态的工作站不能更新");
        }
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkStowStationName(entity.getWarehouseId(), dto.getName());
        }
        stowStationBusiness.updateMore(dto);
        return stowStationMapper.toDTO(stowStationRepository.save(entity));
    }

    @Override
    public StowStationDTO retrieve(String id) {
        StowStation entity = stowStationRepository.retrieve(id);
        StowStationDTO dto = stowStationMapper.toDTO(entity);
        dto.setPositions(stowStationPositionMapper.toDTOList(entity.getStowStation()));
        return dto;
    }

    @Override
    public List<StowStationDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<StowStation> entities = stowStationRepository.getBySearchTerm(searchTerm, sort);
        return stowStationMapper.toDTOList(entities);
    }

    @Override
    public Page<StowStationDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if (pageable.getSort() == null) {
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<StowStation> entities = stowStationRepository.getBySearchTerm(searchTerm, pageable);
        return stowStationMapper.toDTOPage(pageable, entities);
    }

    @Override
    public List<StowStationDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        List<StowStation> entities = stowStationRepository.getList(null, sort);
        return stowStationMapper.toDTOList(entities);
    }

    private void checkStowStationName(String warehouse, String name) {
        StowStation stowStation = stowStationRepository.getByName(warehouse, name);
        if (stowStation != null) {
            throw new ApiException(InBoundException.EX_MD_IN_STOW_STATION_NAME_UNIQUE.toString(), name);
        }
    }
}