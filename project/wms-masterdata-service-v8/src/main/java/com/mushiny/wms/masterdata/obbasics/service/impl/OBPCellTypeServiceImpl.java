package com.mushiny.wms.masterdata.obbasics.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.obbasics.crud.dto.OBPCellTypeDTO;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.OBPCellTypeMapper;
import com.mushiny.wms.masterdata.obbasics.domain.OBPCellType;
import com.mushiny.wms.masterdata.obbasics.exception.OutBoundException;
import com.mushiny.wms.masterdata.obbasics.repository.OBPCellTypeRepository;
import com.mushiny.wms.masterdata.obbasics.service.OBPCellTypeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OBPCellTypeServiceImpl implements OBPCellTypeService {

    private final OBPCellTypeRepository obpCellTypeRepository;
    private final ApplicationContext applicationContext;
    private final OBPCellTypeMapper obpCellTypeMapper;

    public OBPCellTypeServiceImpl(OBPCellTypeRepository obpCellTypeRepository,
                                  ApplicationContext applicationContext,
                                  OBPCellTypeMapper obpCellTypeMapper) {
        this.obpCellTypeRepository = obpCellTypeRepository;
        this.applicationContext = applicationContext;
        this.obpCellTypeMapper = obpCellTypeMapper;
    }

    @Override
    public OBPCellTypeDTO create(OBPCellTypeDTO dto) {
        OBPCellType entity = obpCellTypeMapper.toEntity(dto);
        checkOBPCellTypeName(entity.getWarehouseId(), entity.getName());
        return obpCellTypeMapper.toDTO(obpCellTypeRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        OBPCellType entity = obpCellTypeRepository.retrieve(id);
        obpCellTypeRepository.delete(entity);
    }

    @Override
    public OBPCellTypeDTO update(OBPCellTypeDTO dto) {
        OBPCellType entity = obpCellTypeRepository.retrieve(dto.getId());
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkOBPCellTypeName(entity.getWarehouseId(), dto.getName());
        }
        obpCellTypeMapper.updateEntityFromDTO(dto, entity);
        return obpCellTypeMapper.toDTO(obpCellTypeRepository.save(entity));
    }


    @Override
    public OBPCellTypeDTO retrieve(String id) {
        return obpCellTypeMapper.toDTO(obpCellTypeRepository.retrieve(id));
    }

    @Override
    public List<OBPCellTypeDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        List<OBPCellType> entities = obpCellTypeRepository.getNotLockList(null, sort);
        return obpCellTypeMapper.toDTOList(entities);
    }

    @Override
    public List<OBPCellTypeDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<OBPCellType> entities = obpCellTypeRepository.getBySearchTerm(searchTerm, sort);
        return obpCellTypeMapper.toDTOList(entities);
    }

    @Override
    public Page<OBPCellTypeDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if(pageable.getSort()==null){
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<OBPCellType> entities = obpCellTypeRepository.getBySearchTerm(searchTerm, pageable);
        return obpCellTypeMapper.toDTOPage(pageable, entities);
    }

    private void checkOBPCellTypeName(String warehouse, String name) {
        OBPCellType obpCellType = obpCellTypeRepository.getByName(warehouse, name);
        if (obpCellType != null) {
            throw new ApiException(OutBoundException.EX_MD_OB_LABEL_CONTROLLER_NAME_UNIQUE.toString(), name);
        }
    }
}
