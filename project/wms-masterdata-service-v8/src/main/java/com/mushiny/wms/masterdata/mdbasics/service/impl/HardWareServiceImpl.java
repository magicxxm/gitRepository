package com.mushiny.wms.masterdata.mdbasics.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.HardWareDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.HardWareMapper;
import com.mushiny.wms.masterdata.mdbasics.domain.HardWare;
import com.mushiny.wms.masterdata.mdbasics.exception.MasterDataException;
import com.mushiny.wms.masterdata.mdbasics.repository.HardWareRepository;
import com.mushiny.wms.masterdata.mdbasics.service.HardWareService;
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
public class HardWareServiceImpl implements HardWareService {

    private final HardWareRepository hardWareRepository;
    private final ApplicationContext applicationContext;
    private final HardWareMapper hardWareMapper;

    @Autowired
    public HardWareServiceImpl(HardWareRepository hardWareRepository,
                               ApplicationContext applicationContext,
                               HardWareMapper hardWareMapper) {
        this.hardWareRepository = hardWareRepository;
        this.applicationContext = applicationContext;
        this.hardWareMapper = hardWareMapper;
    }

    @Override
    public HardWareDTO create(HardWareDTO dto) {
        HardWare entity = hardWareMapper.toEntity(dto);
        checkName(entity.getWarehouseId(), dto.getName());
        return hardWareMapper.toDTO(hardWareRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        HardWare entity = hardWareRepository.retrieve(id);
        hardWareRepository.delete(entity);
    }

    @Override
    public HardWareDTO update(HardWareDTO dto) {
        HardWare entity = hardWareRepository.retrieve(dto.getId());
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkName(entity.getWarehouseId(), dto.getName());
        }
        hardWareMapper.updateEntityFromDTO(dto, entity);
        return hardWareMapper.toDTO(hardWareRepository.save(entity));
    }

    @Override
    public HardWareDTO retrieve(String id) {
        return hardWareMapper.toDTO(hardWareRepository.retrieve(id));
    }

    @Override
    public List<HardWareDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        List<HardWare> entities = hardWareRepository.getList(null, sort);
        return hardWareMapper.toDTOList(entities);
    }

    @Override
    public List<HardWareDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<HardWare> entities = hardWareRepository.getBySearchTerm(searchTerm, sort);
        return hardWareMapper.toDTOList(entities);
    }

    @Override
    public Page<HardWareDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if(pageable.getSort()==null){
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<HardWare> entities = hardWareRepository.getBySearchTerm(searchTerm, pageable);
        return hardWareMapper.toDTOPage(pageable, entities);
    }

    private void checkName(String warehouse, String areaName) {
        HardWare hardWare = hardWareRepository.getByName(warehouse, areaName);
        if (hardWare != null) {
            throw new ApiException(MasterDataException.EX_MD_WORK_STATION_TYPE_NAME_UNIQUE.toString(), areaName);
        }
    }
}