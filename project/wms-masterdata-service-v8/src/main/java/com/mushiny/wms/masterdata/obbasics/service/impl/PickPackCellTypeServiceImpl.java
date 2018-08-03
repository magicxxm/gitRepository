package com.mushiny.wms.masterdata.obbasics.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PickPackCellTypeDTO;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.PickPackCellTypeMapper;
import com.mushiny.wms.masterdata.obbasics.domain.PickPackCellType;
import com.mushiny.wms.masterdata.obbasics.exception.OutBoundException;
import com.mushiny.wms.masterdata.obbasics.repository.PickPackCellTypeRepository;
import com.mushiny.wms.masterdata.obbasics.service.PickPackCellTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class PickPackCellTypeServiceImpl implements PickPackCellTypeService {

    private final PickPackCellTypeRepository pickPackCellTypeRepository;
    private final ApplicationContext applicationContext;
    private final PickPackCellTypeMapper pickPackCellTypeMapper;

    @Autowired
    public PickPackCellTypeServiceImpl(PickPackCellTypeRepository pickPackCellTypeRepository,
                                       ApplicationContext applicationContext,
                                       PickPackCellTypeMapper pickPackCellTypeMapper) {
        this.pickPackCellTypeRepository = pickPackCellTypeRepository;
        this.applicationContext = applicationContext;
        this.pickPackCellTypeMapper = pickPackCellTypeMapper;
    }

    @Override
    public PickPackCellTypeDTO create(PickPackCellTypeDTO dto) {
        PickPackCellType entity = pickPackCellTypeMapper.toEntity(dto);
        checkName(entity.getWarehouseId(), entity.getName());
        return pickPackCellTypeMapper.toDTO(pickPackCellTypeRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        PickPackCellType entity = pickPackCellTypeRepository.retrieve(id);
        pickPackCellTypeRepository.delete(entity);
    }

    @Override
    public PickPackCellTypeDTO update(PickPackCellTypeDTO dto) {
        PickPackCellType entity = pickPackCellTypeRepository.retrieve(dto.getId());
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkName(entity.getWarehouseId(), dto.getName());
        }
        pickPackCellTypeMapper.updateEntityFromDTO(dto, entity);
        return pickPackCellTypeMapper.toDTO(pickPackCellTypeRepository.save(entity));
    }

    @Override
    public PickPackCellTypeDTO retrieve(String id) {
        return pickPackCellTypeMapper.toDTO(pickPackCellTypeRepository.retrieve(id));
    }

    @Override
    public List<PickPackCellTypeDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        List<PickPackCellType> entities = pickPackCellTypeRepository.getList(null, sort);
        return pickPackCellTypeMapper.toDTOList(entities);
    }

    @Override
    public List<PickPackCellTypeDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<PickPackCellType> entities = pickPackCellTypeRepository.getBySearchTerm(searchTerm, sort);
        return pickPackCellTypeMapper.toDTOList(entities);
    }

    @Override
    public Page<PickPackCellTypeDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if(pageable.getSort()==null){
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<PickPackCellType> entities = pickPackCellTypeRepository.getBySearchTerm(searchTerm,pageable);
        return pickPackCellTypeMapper.toDTOPage(pageable, entities);
    }

    private void checkName(String warehouse, String areaName) {
        PickPackCellType workStationType = pickPackCellTypeRepository.getByName(warehouse, areaName);
        if (workStationType != null) {
            throw new ApiException(OutBoundException.EX_MD_OB_PICKPACK_CELL_TYPE_NAME_UNIQUE.toString(), areaName);
        }
    }
}