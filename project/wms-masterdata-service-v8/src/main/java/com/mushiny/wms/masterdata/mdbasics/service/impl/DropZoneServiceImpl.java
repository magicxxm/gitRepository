package com.mushiny.wms.masterdata.mdbasics.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.DropZoneDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.DropZoneMapper;
import com.mushiny.wms.masterdata.mdbasics.domain.DropZone;
import com.mushiny.wms.masterdata.mdbasics.exception.MasterDataException;
import com.mushiny.wms.masterdata.mdbasics.repository.DropZoneRepository;
import com.mushiny.wms.masterdata.mdbasics.service.DropZoneService;
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
public class DropZoneServiceImpl implements DropZoneService {

    private final DropZoneRepository dropZoneRepository;
    private final ApplicationContext applicationContext;
    private final DropZoneMapper dropZoneMapper;

    @Autowired
    public DropZoneServiceImpl(DropZoneRepository dropZoneRepository,
                               ApplicationContext applicationContext, DropZoneMapper dropZoneMapper) {
        this.dropZoneRepository = dropZoneRepository;
        this.applicationContext = applicationContext;
        this.dropZoneMapper = dropZoneMapper;
    }

    @Override
    public DropZoneDTO create(DropZoneDTO dto) {
        DropZone entity = dropZoneMapper.toEntity(dto);
        checkDropZoneName(entity.getWarehouseId(), entity.getName());
        return dropZoneMapper.toDTO(dropZoneRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        DropZone entity = dropZoneRepository.retrieve(id);
        dropZoneRepository.delete(entity);
    }

    @Override
    public DropZoneDTO update(DropZoneDTO dto) {
        DropZone entity = dropZoneRepository.retrieve(dto.getId());
        if (!entity.getName().equalsIgnoreCase(dto.getName())) {
            checkDropZoneName(entity.getWarehouseId(), dto.getName());
        }
        dropZoneMapper.updateEntityFromDTO(dto, entity);
        return dropZoneMapper.toDTO(dropZoneRepository.save(entity));
    }

    @Override
    public DropZoneDTO retrieve(String id) {
        return dropZoneMapper.toDTO(dropZoneRepository.retrieve(id));
    }

    @Override
    public List<DropZoneDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<DropZone> entities = dropZoneRepository.getBySearchTerm(searchTerm, sort);
        return dropZoneMapper.toDTOList(entities);
    }

    @Override
    public Page<DropZoneDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if(pageable.getSort()==null){
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<DropZone> entities = dropZoneRepository.getBySearchTerm(searchTerm, pageable);
        return dropZoneMapper.toDTOPage(pageable, entities);
    }

    @Override
    public List<DropZoneDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        String warehouse = applicationContext.getCurrentWarehouse();
        List<DropZone> entities = dropZoneRepository.getList(warehouse,null, sort);
        return dropZoneMapper.toDTOList(entities);
    }

    private void checkDropZoneName(String warehouse, String name) {
        DropZone dropZone = dropZoneRepository.getByName(warehouse, name);
        if (dropZone != null) {
            throw new ApiException(MasterDataException.EX_MD_DROP_ZONE_NAME_UNIQUE.toString(), name);
        }
    }
}