package com.mushiny.wms.masterdata.obbasics.service.impl;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.obbasics.crud.dto.OBPWallTypeDTO;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.OBPWallTypeMapper;
import com.mushiny.wms.masterdata.obbasics.domain.OBPWallType;
import com.mushiny.wms.masterdata.obbasics.exception.OutBoundException;
import com.mushiny.wms.masterdata.obbasics.repository.OBPWallTypeRepository;
import com.mushiny.wms.masterdata.obbasics.service.OBPWallTypeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OBPWallTypeServiceImpl implements OBPWallTypeService {

    private final OBPWallTypeRepository obpWallTypeRepository;
    private final OBPWallTypeMapper obpWallTypeMapper;

    public OBPWallTypeServiceImpl(OBPWallTypeRepository obpWallTypeRepository,
                                  OBPWallTypeMapper obpWallTypeMapper) {
        this.obpWallTypeRepository = obpWallTypeRepository;
        this.obpWallTypeMapper = obpWallTypeMapper;
    }

    @Override
    public OBPWallTypeDTO create(OBPWallTypeDTO dto) {
        OBPWallType entity = obpWallTypeMapper.toEntity(dto);
        checkOBPWallTypeName(entity.getWarehouseId(), entity.getName());
        return obpWallTypeMapper.toDTO(obpWallTypeRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        OBPWallType entity = obpWallTypeRepository.retrieve(id);
        obpWallTypeRepository.delete(entity);
    }

    @Override
    public OBPWallTypeDTO update(OBPWallTypeDTO dto) {
        OBPWallType entity = obpWallTypeRepository.retrieve(dto.getId());
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkOBPWallTypeName(entity.getWarehouseId(), dto.getName());
        }
        obpWallTypeMapper.updateEntityFromDTO(dto, entity);
        return obpWallTypeMapper.toDTO(obpWallTypeRepository.save(entity));
    }

    @Override
    public OBPWallTypeDTO retrieve(String id) {
        OBPWallType entity = obpWallTypeRepository.retrieve(id);
        OBPWallTypeDTO dto = obpWallTypeMapper.toDTO(entity);
        return dto;
    }

    @Override
    public List<OBPWallTypeDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        List<OBPWallType> entities = obpWallTypeRepository.getNotLockList(null, sort);
        return obpWallTypeMapper.toDTOList(entities);
    }

    @Override
    public List<OBPWallTypeDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<OBPWallType> entities = obpWallTypeRepository.getBySearchTerm(searchTerm, sort);
        return obpWallTypeMapper.toDTOList(entities);
    }

    @Override
    public Page<OBPWallTypeDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if(pageable.getSort()==null){
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<OBPWallType> entities = obpWallTypeRepository.getBySearchTerm(searchTerm, pageable);
        return obpWallTypeMapper.toDTOPage(pageable, entities);
    }

    private void checkOBPWallTypeName(String warehouse, String name) {
        OBPWallType obpWallType = obpWallTypeRepository.getByName(warehouse, name);
        if (obpWallType != null) {
            throw new ApiException(OutBoundException.EX_MD_OB_REBIN_WALL_TYPE_NAME_UNIQUE.toString(), name);
        }
    }
}
