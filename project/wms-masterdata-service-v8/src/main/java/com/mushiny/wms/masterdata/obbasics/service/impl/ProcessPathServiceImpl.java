package com.mushiny.wms.masterdata.obbasics.service.impl;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.obbasics.crud.dto.ProcessPathDTO;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.ProcessPathMapper;
import com.mushiny.wms.masterdata.obbasics.domain.ProcessPath;
import com.mushiny.wms.masterdata.obbasics.exception.OutBoundException;
import com.mushiny.wms.masterdata.obbasics.repository.ProcessPathRepository;
import com.mushiny.wms.masterdata.obbasics.service.ProcessPathService;
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
public class ProcessPathServiceImpl implements ProcessPathService {

    private final ProcessPathRepository processPathRepository;
    private final ProcessPathMapper processPathMapper;

    @Autowired
    public ProcessPathServiceImpl(ProcessPathRepository processPathRepository,
                                  ProcessPathMapper processPathMapper) {
        this.processPathRepository = processPathRepository;
        this.processPathMapper = processPathMapper;
    }

    @Override
    public ProcessPathDTO create(ProcessPathDTO dto) {
        ProcessPath entity = processPathMapper.toEntity(dto);
        checkProcessPathName(entity.getWarehouseId(), entity.getName());
        return processPathMapper.toDTO(processPathRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        ProcessPath entity = processPathRepository.retrieve(id);
        processPathRepository.delete(entity);
    }

    @Override
    public ProcessPathDTO update(ProcessPathDTO dto) {
        ProcessPath entity = processPathRepository.retrieve(dto.getId());
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkProcessPathName(entity.getWarehouseId(), dto.getName());
        }
        processPathMapper.updateEntityFromDTO(dto, entity);
        return processPathMapper.toDTO(processPathRepository.save(entity));
    }

    @Override
    public ProcessPathDTO retrieve(String id) {
        return processPathMapper.toDTO(processPathRepository.retrieve(id));
    }

    @Override
    public List<ProcessPathDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        List<ProcessPath> entities = processPathRepository.getNotLockList(null, sort);
        return processPathMapper.toDTOList(entities);
    }

    @Override
    public List<ProcessPathDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<ProcessPath> entities = processPathRepository.getBySearchTerm(searchTerm, sort);
        return processPathMapper.toDTOList(entities);
    }

    @Override
    public Page<ProcessPathDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if(pageable.getSort()==null){
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<ProcessPath> entities = processPathRepository.getBySearchTerm(searchTerm, pageable);
        return processPathMapper.toDTOPage(pageable, entities);
    }

    private void checkProcessPathName(String warehouse, String name) {
        ProcessPath reBatchStation = processPathRepository.getByName(warehouse, name);
        if (reBatchStation != null) {
            throw new ApiException(OutBoundException.EX_MD_OB_PROCESS_PATH_NAME_UNIQUE.toString(), name);
        }
    }
}
