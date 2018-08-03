package com.mushiny.wms.masterdata.obbasics.service.impl;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.obbasics.business.PackingStationBusiness;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PackingStationDTO;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PackingStationPositionDTO;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.PackingStationMapper;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.PackingStationPositionMapper;
import com.mushiny.wms.masterdata.obbasics.domain.PackingStation;
import com.mushiny.wms.masterdata.obbasics.domain.PackingStationPosition;
import com.mushiny.wms.masterdata.obbasics.exception.OutBoundException;
import com.mushiny.wms.masterdata.obbasics.repository.PackingStationRepository;
import com.mushiny.wms.masterdata.obbasics.service.PackingStationService;
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
public class PackingStationServiceImpl implements PackingStationService {

    private final PackingStationRepository packingStationRepository;
    private final PackingStationMapper packingStationMapper;
    private final PackingStationPositionMapper packingStationPositionMapper;
    private final PackingStationBusiness packingStationBusiness;

    @Autowired
    public PackingStationServiceImpl(PackingStationRepository packingStationRepository,
                                     PackingStationMapper packingStationMapper,
                                     PackingStationPositionMapper packingStationPositionMapper,
                                     PackingStationBusiness packingStationBusiness) {
        this.packingStationRepository = packingStationRepository;
        this.packingStationMapper = packingStationMapper;
        this.packingStationPositionMapper = packingStationPositionMapper;
        this.packingStationBusiness = packingStationBusiness;
    }

    @Override
    public void createMore(PackingStationDTO dto) {
        checkPackingStationName(dto.getWarehouseId(),dto.getName());
        packingStationBusiness.createMore(dto);
    }

    @Override
    public PackingStationDTO create(PackingStationDTO dto) {
        return null;
    }

    @Override
    public void delete(String id) {
        PackingStation entity = packingStationRepository.retrieve(id);
        if (entity.getOperator()!=null) {
            throw new ApiException("该状态的工作站不能删除");
        }
        packingStationRepository.delete(entity);
    }

    @Override
    public PackingStationDTO update(PackingStationDTO dto) {
        PackingStation entity = packingStationRepository.retrieve(dto.getId());
        if (entity.getOperator()!=null) {
            throw new ApiException("该状态的工作站不能更新");
        }
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkPackingStationName(entity.getWarehouseId(), dto.getName());
        }
        packingStationBusiness.upDateMore(dto);
        return packingStationMapper.toDTO(entity);
    }

    @Override
    public PackingStationDTO retrieve(String id) {
        PackingStation entity = packingStationRepository.retrieve(id);
        PackingStationDTO dto = packingStationMapper.toDTO(entity);
        dto.setPositions(packingStationPositionMapper.toDTOList(entity.getPositions()));
        return dto;
    }

    @Override
    public List<PackingStationDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        List<PackingStation> entities = packingStationRepository.getNotLockList(null, sort);
        return packingStationMapper.toDTOList(entities);
    }

    @Override
    public List<PackingStationDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<PackingStation> entities = packingStationRepository.getBySearchTerm(searchTerm, sort);
        return packingStationMapper.toDTOList(entities);
    }

    @Override
    public Page<PackingStationDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if(pageable.getSort()==null){
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<PackingStation> entities = packingStationRepository.getBySearchTerm(searchTerm, pageable);
        return packingStationMapper.toDTOPage(pageable, entities);
    }

    private void checkPackingStationName(String warehouse, String name) {
        PackingStation packingStation = packingStationRepository.getByName(warehouse, name);
        if (packingStation != null) {
            throw new ApiException(OutBoundException.EX_MD_OB_PACKING_STATION_NAME_UNIQUE.toString(), name);
        }
    }
}
