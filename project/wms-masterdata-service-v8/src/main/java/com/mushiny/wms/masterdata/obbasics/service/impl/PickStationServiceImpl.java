package com.mushiny.wms.masterdata.obbasics.service.impl;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.obbasics.business.PickStationBusiness;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PickStationDTO;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.PickStationMapper;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.PickStationPositionMapper;
import com.mushiny.wms.masterdata.obbasics.domain.PickStation;
import com.mushiny.wms.masterdata.obbasics.exception.OutBoundException;
import com.mushiny.wms.masterdata.obbasics.repository.PickStationRepository;
import com.mushiny.wms.masterdata.obbasics.service.PickStationService;
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
public class PickStationServiceImpl implements PickStationService {

    private final PickStationRepository pickStationRepository;
    private final PickStationMapper pickStationMapper;
    private final PickStationPositionMapper pickStationPositionMapper;
    private final PickStationBusiness pickStationBusiness;

    @Autowired
    public PickStationServiceImpl(PickStationRepository pickStationRepository,
                                  PickStationMapper pickStationMapper,
                                  PickStationPositionMapper pickStationPositionMapper,
                                  PickStationBusiness pickStationBusiness) {
        this.pickStationRepository = pickStationRepository;
        this.pickStationMapper = pickStationMapper;
        this.pickStationPositionMapper = pickStationPositionMapper;
        this.pickStationBusiness = pickStationBusiness;
    }

    @Override
    public void createMore(PickStationDTO dto) {
        checkPackingStationName(dto.getWarehouseId(),dto.getName());
        pickStationBusiness.createMore(dto);
    }

    @Override
    public PickStationDTO create(PickStationDTO dto) {
        return null;
    }

    @Override
    public void delete(String id) {
        PickStation entity = pickStationRepository.retrieve(id);
        if (entity.getOperator()!=null) {
            throw new ApiException("该状态的工作站不能刪除");
        }
        pickStationRepository.delete(entity);
    }

    @Override
    public PickStationDTO update(PickStationDTO dto) {
        PickStation entity = pickStationRepository.retrieve(dto.getId());
        if (entity.getOperator()!=null) {
            throw new ApiException("该状态的工作站不能更新");
        }
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkPackingStationName(entity.getWarehouseId(),dto.getName());
        }
       pickStationBusiness.updateMore(dto);
        return pickStationMapper.toDTO(entity);
    }

    @Override
    public PickStationDTO retrieve(String id) {
        PickStation entity = pickStationRepository.retrieve(id);
        PickStationDTO dto = pickStationMapper.toDTO(entity);
        dto.setPositions(pickStationPositionMapper.toDTOList(entity.getPositions()));
        return dto;
    }

    @Override
    public List<PickStationDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        List<PickStation> entities = pickStationRepository.getNotLockList(null, sort);
        return pickStationMapper.toDTOList(entities);
    }

    @Override
    public List<PickStationDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<PickStation> entities = pickStationRepository.getBySearchTerm(searchTerm, sort);
        return pickStationMapper.toDTOList(entities);
    }

    @Override
    public Page<PickStationDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if(pageable.getSort()==null){
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<PickStation> entities = pickStationRepository.getBySearchTerm(searchTerm, pageable);
        return pickStationMapper.toDTOPage(pageable, entities);
    }

    private void checkPackingStationName(String warehouse, String name) {
        PickStation packingStation = pickStationRepository.getByName(warehouse, name);
        if (packingStation != null) {
            throw new ApiException(OutBoundException.EX_MD_OB_PACKING_STATION_NAME_UNIQUE.toString(), name);
        }
    }
}
