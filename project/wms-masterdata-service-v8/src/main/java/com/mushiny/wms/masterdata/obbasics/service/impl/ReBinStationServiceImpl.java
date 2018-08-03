package com.mushiny.wms.masterdata.obbasics.service.impl;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.obbasics.crud.dto.ReBinStationDTO;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.ReBinStationMapper;
import com.mushiny.wms.masterdata.obbasics.domain.ReBinStation;
import com.mushiny.wms.masterdata.obbasics.exception.OutBoundException;
import com.mushiny.wms.masterdata.obbasics.repository.ReBinStationRepository;
import com.mushiny.wms.masterdata.obbasics.service.ReBinStationService;
import com.mushiny.wms.masterdata.general.domain.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReBinStationServiceImpl implements ReBinStationService {

    private final ReBinStationRepository reBinStationRepository;
    private final ReBinStationMapper reBinStationMapper;


    public ReBinStationServiceImpl(ReBinStationRepository reBinStationRepository,
                                   ReBinStationMapper reBinStationMapper) {
        this.reBinStationRepository = reBinStationRepository;
        this.reBinStationMapper = reBinStationMapper;
    }

    @Override
    public ReBinStationDTO create(ReBinStationDTO dto) {
        ReBinStation entity = reBinStationMapper.toEntity(dto);
        checkReBinStationName(entity.getWarehouseId(), entity.getName());
        return reBinStationMapper.toDTO(reBinStationRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        ReBinStation entity = reBinStationRepository.retrieve(id);
        if (entity.getOperator()!=null) {
            throw new ApiException("该状态的工作站不能删除");
        }
        reBinStationRepository.delete(entity);
    }

    @Override
    public ReBinStationDTO update(ReBinStationDTO dto) {
        ReBinStation entity = reBinStationRepository.retrieve(dto.getId());
        if (entity.getOperator()!=null) {
            throw new ApiException("该状态的工作站不能更新");
        }
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkReBinStationName(entity.getWarehouseId(), dto.getName());
        }
        reBinStationMapper.updateEntityFromDTO(dto, entity);
        return reBinStationMapper.toDTO(reBinStationRepository.save(entity));
    }

    @Override
    public ReBinStationDTO retrieve(String id) {
        return reBinStationMapper.toDTO(reBinStationRepository.retrieve(id));
    }

    @Override
    public List<ReBinStationDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        List<ReBinStation> entities = reBinStationRepository.getNotLockList(null, sort);
        return reBinStationMapper.toDTOList(entities);
    }

    @Override
    public List<ReBinStationDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<ReBinStation> entities = reBinStationRepository.getBySearchTerm(searchTerm, sort);
        return reBinStationMapper.toDTOList(entities);
    }

    @Override
    public Page<ReBinStationDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if(pageable.getSort()==null){
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<ReBinStation> entities = reBinStationRepository.getBySearchTerm(searchTerm, pageable);
        return reBinStationMapper.toDTOPage(pageable, entities);
    }

    private void checkReBinStationName(String warehouse, String name) {
        ReBinStation reBinStation = reBinStationRepository.getByName(warehouse, name);
        if (reBinStation != null) {
            throw new ApiException(OutBoundException.EX_MD_OB_REBIN_STATION_NAME_UNIQUE.toString(), name);
        }
    }
}
