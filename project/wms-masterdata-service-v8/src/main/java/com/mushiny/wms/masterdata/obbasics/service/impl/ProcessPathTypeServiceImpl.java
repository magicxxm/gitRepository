package com.mushiny.wms.masterdata.obbasics.service.impl;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.obbasics.crud.dto.ProcessPathTypeDTO;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.ProcessPathTypeMapper;
import com.mushiny.wms.masterdata.obbasics.domain.ProcessPath;
import com.mushiny.wms.masterdata.obbasics.domain.ProcessPathType;
import com.mushiny.wms.masterdata.obbasics.exception.OutBoundException;
import com.mushiny.wms.masterdata.obbasics.repository.ProcessPathTypeRepository;
import com.mushiny.wms.masterdata.obbasics.service.ProcessPathTypeService;
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
public class ProcessPathTypeServiceImpl implements ProcessPathTypeService {

    private final ProcessPathTypeRepository processPathTypeRepository;
    private final ProcessPathTypeMapper processPathTypeMapper;


    @Autowired
    public ProcessPathTypeServiceImpl(ProcessPathTypeRepository processPathTypeRepository,
                                      ProcessPathTypeMapper processPathTypeMapper) {
        this.processPathTypeRepository = processPathTypeRepository;
        this.processPathTypeMapper = processPathTypeMapper;
    }

    @Override
    public ProcessPathTypeDTO create(ProcessPathTypeDTO dto) {
        ProcessPathType entity = processPathTypeMapper.toEntity(dto);
        checkProcessPathTypeName(entity.getName());
        return processPathTypeMapper.toDTO(processPathTypeRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        ProcessPathType entity = processPathTypeRepository.retrieve(id);
        processPathTypeRepository.delete(entity);
    }

    @Override
    public ProcessPathTypeDTO update(ProcessPathTypeDTO dto) {
        ProcessPathType entity = processPathTypeRepository.retrieve(dto.getId());
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkProcessPathTypeName(dto.getName());
        }
        processPathTypeMapper.updateEntityFromDTO(dto, entity);
        return processPathTypeMapper.toDTO(processPathTypeRepository.save(entity));
    }

    @Override
    public ProcessPathTypeDTO retrieve(String id) {
        return processPathTypeMapper.toDTO(processPathTypeRepository.retrieve(id));
    }

    @Override
    public List<ProcessPathTypeDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        List<ProcessPathType> entities = processPathTypeRepository.getList(null, sort);
        return processPathTypeMapper.toDTOList(entities);
    }

    @Override
    public List<ProcessPathTypeDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<ProcessPathType> entities = processPathTypeRepository.getBySearchTerm(searchTerm, sort);
        return processPathTypeMapper.toDTOList(entities);
    }

    @Override
    public Page<ProcessPathTypeDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if(pageable.getSort()==null){
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<ProcessPathType> entities = processPathTypeRepository.getBySearchTerm(searchTerm, pageable);
        return processPathTypeMapper.toDTOPage(pageable, entities);
    }

    private void checkProcessPathTypeName(String name) {
        ProcessPathType reBatchStation = processPathTypeRepository.getByName(name);
        if (reBatchStation != null) {
            throw new ApiException(OutBoundException.EX_MD_OB_PROCESS_PATH_TYPE_NAME_UNIQUE.toString(), name);
        }
    }
}
