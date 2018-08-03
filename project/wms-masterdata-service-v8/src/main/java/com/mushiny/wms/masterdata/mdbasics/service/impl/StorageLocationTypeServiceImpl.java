package com.mushiny.wms.masterdata.mdbasics.service.impl;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.StorageLocationTypeDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.StorageLocationTypeMapper;
import com.mushiny.wms.masterdata.mdbasics.domain.StorageLocationType;
import com.mushiny.wms.masterdata.mdbasics.exception.MasterDataException;
import com.mushiny.wms.masterdata.mdbasics.repository.StorageLocationTypeRepository;
import com.mushiny.wms.masterdata.mdbasics.service.StorageLocationTypeService;
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
public class StorageLocationTypeServiceImpl implements StorageLocationTypeService {

    private final StorageLocationTypeRepository storageLocationTypeRepository;
    private final StorageLocationTypeMapper storageLocationTypeMapper;

    @Autowired
    public StorageLocationTypeServiceImpl(StorageLocationTypeRepository storageLocationTypeRepository,
                                          StorageLocationTypeMapper storageLocationTypeMapper) {
        this.storageLocationTypeRepository = storageLocationTypeRepository;
        this.storageLocationTypeMapper = storageLocationTypeMapper;
    }

    @Override
    public StorageLocationTypeDTO create(StorageLocationTypeDTO dto) {
        StorageLocationType entity = storageLocationTypeMapper.toEntity(dto);
        checkStorageLocationTypeName(entity.getWarehouseId(), entity.getName());
        return storageLocationTypeMapper.toDTO(storageLocationTypeRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        StorageLocationType entity = storageLocationTypeRepository.retrieve(id);
        storageLocationTypeRepository.delete(entity);
    }

    @Override
    public StorageLocationTypeDTO update(StorageLocationTypeDTO dto) {
        StorageLocationType entity = storageLocationTypeRepository.retrieve(dto.getId());
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkStorageLocationTypeName(entity.getWarehouseId(), dto.getName());
        }
        storageLocationTypeMapper.updateEntityFromDTO(dto, entity);
        return storageLocationTypeMapper.toDTO(storageLocationTypeRepository.save(entity));
    }

    @Override
    public StorageLocationTypeDTO retrieve(String id) {
        return storageLocationTypeMapper.toDTO(storageLocationTypeRepository.retrieve(id));
    }

    @Override
    public List<StorageLocationTypeDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<StorageLocationType> entities = storageLocationTypeRepository.getBySearchTerm(searchTerm, sort);
        return storageLocationTypeMapper.toDTOList(entities);
    }

    @Override
    public Page<StorageLocationTypeDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if(pageable.getSort()==null){
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<StorageLocationType> entities = storageLocationTypeRepository.getBySearchTerm(searchTerm, pageable);
        return storageLocationTypeMapper.toDTOPage(pageable, entities);
    }

    @Override
    public List<StorageLocationTypeDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        List<StorageLocationType> entities = storageLocationTypeRepository.getList(null, sort);
        return storageLocationTypeMapper.toDTOList(entities);
    }

    private void checkStorageLocationTypeName(String warehouse, String name) {
        StorageLocationType slt = storageLocationTypeRepository.getByName(warehouse, name);
        if (slt != null) {
            throw new ApiException(MasterDataException.EX_MD_STORAGE_LOCATION_TYPE_NAME_UNIQUE.toString(), name);
        }
    }
}
