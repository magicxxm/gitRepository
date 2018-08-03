package com.mushiny.wms.masterdata.obbasics.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.obbasics.crud.dto.OBPCellDTO;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.OBPCellMapper;
import com.mushiny.wms.masterdata.obbasics.domain.OBPCell;
import com.mushiny.wms.masterdata.obbasics.exception.OutBoundException;
import com.mushiny.wms.masterdata.obbasics.repository.OBPCellRepository;
import com.mushiny.wms.masterdata.obbasics.service.OBPCellService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OBPCellServiceImpl implements OBPCellService {

    private final OBPCellRepository obpCellRepository;
    private final ApplicationContext applicationContext;
    private final OBPCellMapper obpCellMapper;

    public OBPCellServiceImpl(OBPCellRepository obpCellRepository,
                              ApplicationContext applicationContext,
                              OBPCellMapper obpCellMapper) {
        this.obpCellRepository = obpCellRepository;
        this.applicationContext = applicationContext;
        this.obpCellMapper = obpCellMapper;
    }

    @Override
    public OBPCellDTO create(OBPCellDTO dto) {
        OBPCell entity = obpCellMapper.toEntity(dto);
        checkOBPCellName(entity.getWarehouseId(), entity.getName());
        return obpCellMapper.toDTO(obpCellRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        OBPCell entity = obpCellRepository.retrieve(id);
        obpCellRepository.delete(entity);
    }

    @Override
    public OBPCellDTO update(OBPCellDTO dto) {
        OBPCell entity = obpCellRepository.retrieve(dto.getId());
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkOBPCellName(entity.getWarehouseId(), dto.getName());
        }
        obpCellMapper.updateEntityFromDTO(dto, entity);
        return obpCellMapper.toDTO(obpCellRepository.save(entity));
    }


    @Override
    public OBPCellDTO retrieve(String id) {
        return obpCellMapper.toDTO(obpCellRepository.retrieve(id));
    }

    @Override
    public List<OBPCellDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        List<OBPCell> entities = obpCellRepository.getNotLockList(null, sort);
        return obpCellMapper.toDTOList(entities);
    }

    @Override
    public List<OBPCellDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<OBPCell> entities = obpCellRepository.getBySearchTerm(searchTerm, sort);
        return obpCellMapper.toDTOList(entities);
    }

    @Override
    public Page<OBPCellDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if(pageable.getSort()==null){
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<OBPCell> entities = obpCellRepository.getBySearchTerm(searchTerm, pageable);
        return obpCellMapper.toDTOPage(pageable, entities);
    }

    private void checkOBPCellName(String warehouse, String name) {
        OBPCell obpCell = obpCellRepository.getByName(warehouse, name);
        if (obpCell != null) {
            throw new ApiException(OutBoundException.EX_MD_OB_LABEL_CONTROLLER_NAME_UNIQUE.toString(), name);
        }
    }
}
