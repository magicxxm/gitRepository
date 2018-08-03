package com.mushiny.wms.masterdata.obbasics.service.impl;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.obbasics.crud.dto.ReBinCellTypeDTO;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.ReBinCellTypeMapper;
import com.mushiny.wms.masterdata.obbasics.domain.ReBinCellType;
import com.mushiny.wms.masterdata.obbasics.exception.OutBoundException;
import com.mushiny.wms.masterdata.obbasics.repository.ReBinCellTypeRepository;
import com.mushiny.wms.masterdata.obbasics.service.ReBinCellTypeService;
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
public class ReBinCellTypeServiceImpl implements ReBinCellTypeService {

    private final ReBinCellTypeRepository reBinCellTypeRepository;
    private final ReBinCellTypeMapper reBinCellTypeMapper;


    public ReBinCellTypeServiceImpl(ReBinCellTypeRepository reBinCellTypeRepository,
                                    ReBinCellTypeMapper reBinCellTypeMapper) {
        this.reBinCellTypeRepository = reBinCellTypeRepository;
        this.reBinCellTypeMapper = reBinCellTypeMapper;
    }

    @Override
    public ReBinCellTypeDTO create(ReBinCellTypeDTO dto) {
        ReBinCellType entity = reBinCellTypeMapper.toEntity(dto);
        checkReBinCellTypeName(entity.getWarehouseId(), entity.getName());
        return reBinCellTypeMapper.toDTO(reBinCellTypeRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        ReBinCellType entity = reBinCellTypeRepository.retrieve(id);
        reBinCellTypeRepository.delete(entity);
    }

    @Override
    public ReBinCellTypeDTO update(ReBinCellTypeDTO dto) {
        ReBinCellType entity = reBinCellTypeRepository.retrieve(dto.getId());
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkReBinCellTypeName(entity.getWarehouseId(), dto.getName());
        }
        reBinCellTypeMapper.updateEntityFromDTO(dto, entity);
        return reBinCellTypeMapper.toDTO(reBinCellTypeRepository.save(entity));
    }

    @Override
    public ReBinCellTypeDTO retrieve(String id) {
        return reBinCellTypeMapper.toDTO(reBinCellTypeRepository.retrieve(id));
    }

    @Override
    public List<ReBinCellTypeDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        List<ReBinCellType> entities = reBinCellTypeRepository.getNotLockList(null, sort);
        return reBinCellTypeMapper.toDTOList(entities);
    }

    @Override
    public List<ReBinCellTypeDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<ReBinCellType> entities = reBinCellTypeRepository.getBySearchTerm(searchTerm, sort);
        return reBinCellTypeMapper.toDTOList(entities);
    }

    @Override
    public Page<ReBinCellTypeDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if(pageable.getSort()==null){
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<ReBinCellType> entities = reBinCellTypeRepository.getBySearchTerm(searchTerm, pageable);
        return reBinCellTypeMapper.toDTOPage(pageable, entities);
    }

    private void checkReBinCellTypeName(String warehouse, String name) {
        ReBinCellType reBinCellType = reBinCellTypeRepository.getByName(warehouse, name);
        if (reBinCellType != null) {
            throw new ApiException(OutBoundException.EX_MD_OB_REBIN_CELL_TYPE_NAME_UNIQUE.toString(), name);
        }
    }
}
