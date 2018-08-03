package com.mushiny.wms.masterdata.obbasics.service.impl;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.obbasics.business.SortingStationBusiness;
import com.mushiny.wms.masterdata.obbasics.crud.dto.SortingStationDTO;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.SortingStationMapper;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.SortingStationPositionMapper;
import com.mushiny.wms.masterdata.obbasics.domain.SortingStation;
import com.mushiny.wms.masterdata.obbasics.exception.OutBoundException;
import com.mushiny.wms.masterdata.obbasics.repository.SortingStationRepository;
import com.mushiny.wms.masterdata.obbasics.service.SortingStationService;
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
public class SortingStationServiceImpl implements SortingStationService {

    private final SortingStationRepository sortingStationRepository;
    private final SortingStationMapper sortingStationMapper;
    private final SortingStationPositionMapper sortingStationPositionMapper;
    private final SortingStationBusiness sortingStationBusiness;

    @Autowired
    public SortingStationServiceImpl(SortingStationRepository sortingStationRepository,
                                     SortingStationMapper sortingStationMapper,
                                     SortingStationPositionMapper sortingStationPositionMapper,
                                     SortingStationBusiness sortingStationBusiness) {
        this.sortingStationRepository = sortingStationRepository;
        this.sortingStationMapper = sortingStationMapper;
        this.sortingStationPositionMapper = sortingStationPositionMapper;
        this.sortingStationBusiness = sortingStationBusiness;
    }

    @Override
    public void createMore(SortingStationDTO dto) {
        checkPackingStationName(dto.getWarehouseId(), dto.getName());
        sortingStationBusiness.createMore(dto);
    }

    @Override
    public SortingStationDTO create(SortingStationDTO dto) {
        return null;
    }

    @Override
    public void delete(String id) {
        SortingStation entity = sortingStationRepository.retrieve(id);
        if (entity.getOperator()!=null) {
            throw new ApiException("该状态的工作站不能删除");
        }
        sortingStationRepository.delete(entity);
    }

    @Override
    public SortingStationDTO update(SortingStationDTO dto) {
        SortingStation entity = sortingStationRepository.retrieve(dto.getId());
        if (entity.getOperator() != null) {
            throw new ApiException("该状态的工作站不能更新");
        }
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkPackingStationName(entity.getWarehouseId(), dto.getName());
        }
            sortingStationBusiness.updateMore(dto);
            return sortingStationMapper.toDTO(entity);
        }

    @Override
    public SortingStationDTO retrieve(String id) {
        SortingStation entity = sortingStationRepository.retrieve(id);
        SortingStationDTO dto = sortingStationMapper.toDTO(entity);
        dto.setPositions(sortingStationPositionMapper.toDTOList(entity.getPositions()));
        return dto;
    }

    @Override
    public List<SortingStationDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        List<SortingStation> entities = sortingStationRepository.getNotLockList(null, sort);
        return sortingStationMapper.toDTOList(entities);
    }

    @Override
    public List<SortingStationDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<SortingStation> entities = sortingStationRepository.getBySearchTerm(searchTerm, sort);
        return sortingStationMapper.toDTOList(entities);
    }

    @Override
    public Page<SortingStationDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if (pageable.getSort() == null) {
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<SortingStation> entities = sortingStationRepository.getBySearchTerm(searchTerm, pageable);
        return sortingStationMapper.toDTOPage(pageable, entities);
    }

    private void checkPackingStationName(String warehouse, String name) {
        SortingStation packingStation = sortingStationRepository.getByName(warehouse, name);
        if (packingStation != null) {
            throw new ApiException(OutBoundException.EX_MD_OB_PACKING_STATION_NAME_UNIQUE.toString(), name);
        }
    }
}
