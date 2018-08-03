package com.mushiny.wms.masterdata.ibbasics.service.impl;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.ibbasics.business.IBPStationBusiness;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.IBPStationDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.mapper.IBPStationMapper;
import com.mushiny.wms.masterdata.ibbasics.crud.mapper.IBPStationPositionMapper;
import com.mushiny.wms.masterdata.ibbasics.domain.IBPStation;
import com.mushiny.wms.masterdata.ibbasics.exception.InBoundException;
import com.mushiny.wms.masterdata.ibbasics.repository.IBPStationRepository;
import com.mushiny.wms.masterdata.ibbasics.service.IBPStationService;
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
public class IBPStationServiceImpl implements IBPStationService {

    private final IBPStationRepository ibpStationRepository;
    private final IBPStationMapper ibpStationMapper;
    private final IBPStationPositionMapper ibpStationPositionMapper;
    private final IBPStationBusiness ibpStationBusiness;

    @Autowired
    public IBPStationServiceImpl(IBPStationRepository ibpStationRepository,
                                 IBPStationMapper ibpStationMapper,
                                 IBPStationPositionMapper ibpStationPositionMapper,
                                 IBPStationBusiness ibpStationBusiness) {
        this.ibpStationRepository = ibpStationRepository;
        this.ibpStationMapper = ibpStationMapper;
        this.ibpStationPositionMapper = ibpStationPositionMapper;
        this.ibpStationBusiness = ibpStationBusiness;
    }

    @Override
    public void createMore(IBPStationDTO dto) {
        checkPackingStationName(dto.getWarehouseId(), dto.getName());
        ibpStationBusiness.createMore(dto);
    }

    @Override
    public IBPStationDTO create(IBPStationDTO dto) {
        return null;
    }

    @Override
    public void delete(String id) {
        IBPStation entity = ibpStationRepository.retrieve(id);
        if (entity.getOperatorId() != null) {
            throw new ApiException("该状态的工作站不能删除");
        }
        ibpStationRepository.delete(entity);
    }

    @Override
    public IBPStationDTO update(IBPStationDTO dto) {
        IBPStation entity = ibpStationRepository.retrieve(dto.getId());
        if (entity.getOperatorId() != null) {
            throw new ApiException("该状态的工作站不能更新");
        }
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkPackingStationName(entity.getWarehouseId(), dto.getName());
        }
        ibpStationBusiness.unDateMore(dto);
        return ibpStationMapper.toDTO(entity);
    }

    @Override
    public IBPStationDTO retrieve(String id) {
        IBPStation entity = ibpStationRepository.retrieve(id);
        IBPStationDTO dto = ibpStationMapper.toDTO(entity);
        dto.setPositions(ibpStationPositionMapper.toDTOList(entity.getPositions()));
        return dto;
    }

    @Override
    public List<IBPStationDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        List<IBPStation> entities = ibpStationRepository.getNotLockList(null, sort);
        return ibpStationMapper.toDTOList(entities);
    }

    @Override
    public List<IBPStationDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<IBPStation> entities = ibpStationRepository.getBySearchTerm(searchTerm, sort);
        return ibpStationMapper.toDTOList(entities);
    }

    @Override
    public Page<IBPStationDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if (pageable.getSort() == null) {
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<IBPStation> entities = ibpStationRepository.getBySearchTerm(searchTerm, pageable);
        return ibpStationMapper.toDTOPage(pageable, entities);
    }

    private void checkPackingStationName(String warehouse, String name) {
        IBPStation packingStation = ibpStationRepository.getByName(warehouse, name);
        if (packingStation != null) {
            throw new ApiException(InBoundException.EX_MD_IN_IBP_STATION_NAME_UNIQUE.toString(), name);
        }
    }
}
