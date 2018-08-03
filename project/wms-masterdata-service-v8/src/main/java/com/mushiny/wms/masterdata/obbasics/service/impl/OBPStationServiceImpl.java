package com.mushiny.wms.masterdata.obbasics.service.impl;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.mdbasics.domain.WorkStation;
import com.mushiny.wms.masterdata.mdbasics.repository.WorkStationRepository;
import com.mushiny.wms.masterdata.obbasics.business.OBPStationBusiness;
import com.mushiny.wms.masterdata.obbasics.crud.dto.OBPStationDTO;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.OBPStationMapper;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.OBPStationPositionMapper;
import com.mushiny.wms.masterdata.obbasics.domain.OBPStation;
import com.mushiny.wms.masterdata.obbasics.exception.OutBoundException;
import com.mushiny.wms.masterdata.obbasics.repository.OBPStationRepository;
import com.mushiny.wms.masterdata.obbasics.service.OBPStationService;
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
public class OBPStationServiceImpl implements OBPStationService {

    private final OBPStationRepository obpStationRepository;
    private final OBPStationMapper obpStationMapper;
    private final OBPStationPositionMapper obpStationPositionMapper;
    private final OBPStationBusiness obpStationBusiness;
    private final WorkStationRepository workStationRepository;

    @Autowired
    public OBPStationServiceImpl(OBPStationRepository obpStationRepository,
                                 OBPStationMapper obpStationMapper,
                                 OBPStationPositionMapper obpStationPositionMapper,
                                 OBPStationBusiness obpStationBusiness, WorkStationRepository workStationRepository) {
        this.obpStationRepository = obpStationRepository;
        this.obpStationMapper = obpStationMapper;
        this.obpStationPositionMapper = obpStationPositionMapper;
        this.obpStationBusiness = obpStationBusiness;
        this.workStationRepository = workStationRepository;
    }

    @Override
    public void createMore(OBPStationDTO dto) {
        checkPackingStationName(dto.getWarehouseId(), dto.getName());
        obpStationBusiness.createMore(dto);
    }

    @Override
    public OBPStationDTO create(OBPStationDTO dto) {
        return null;
    }

    @Override
    public void delete(String id) {
        OBPStation entity = obpStationRepository.retrieve(id);
        if (entity.getOperatorId() != null) {
            throw new ApiException("该状态的工作站不能删除");
        }
        obpStationRepository.delete(entity);
    }

    @Override
    public OBPStationDTO update(OBPStationDTO dto) {
        OBPStation entity = obpStationRepository.retrieve(dto.getId());
        if (entity.getOperatorId()!=null) {
            throw new ApiException("该状态的工作站不能更新");
        }
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkPackingStationName(entity.getWarehouseId(), dto.getName());
        }
        obpStationBusiness.updateMore(dto);
        return obpStationMapper.toDTO(entity);
    }

    @Override
    public OBPStationDTO retrieve(String id) {
        OBPStation entity = obpStationRepository.retrieve(id);
        OBPStationDTO dto = obpStationMapper.toDTO(entity);
        dto.setPositions(obpStationPositionMapper.toDTOList(entity.getPositions()));
        return dto;
    }

    @Override
    public List<OBPStationDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        List<OBPStation> entities = obpStationRepository.getNotLockList(null, sort);
        return obpStationMapper.toDTOList(entities);
    }

    @Override
    public List<OBPStationDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<OBPStation> entities = obpStationRepository.getBySearchTerm(searchTerm, sort);
        return obpStationMapper.toDTOList(entities);
    }

    @Override
    public Page<OBPStationDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if (pageable.getSort() == null) {
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<OBPStation> entities = obpStationRepository.getBySearchTerm(searchTerm, pageable);
        return obpStationMapper.toDTOPage(pageable, entities);
    }

    private void checkPackingStationName(String warehouse, String name) {
        OBPStation packingStation = obpStationRepository.getByName(warehouse, name);
        if (packingStation != null) {
            throw new ApiException(OutBoundException.EX_MD_OB_PACKING_STATION_NAME_UNIQUE.toString(), name);
        }
    }
}
