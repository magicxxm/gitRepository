package com.mushiny.wms.masterdata.mdbasics.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.WorkStationTypeDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.WorkStationTypeMapper;
import com.mushiny.wms.masterdata.mdbasics.domain.WorkStationType;
import com.mushiny.wms.masterdata.mdbasics.exception.MasterDataException;
import com.mushiny.wms.masterdata.mdbasics.repository.WorkStationTypeRepository;
import com.mushiny.wms.masterdata.mdbasics.service.WorkStationTypeService;
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
public class WorkStationTypeServiceImpl implements WorkStationTypeService {

    private final WorkStationTypeRepository workStationTypeRepository;
    private final ApplicationContext applicationContext;
    private final WorkStationTypeMapper workStationTypeMapper;

    @Autowired
    public WorkStationTypeServiceImpl(WorkStationTypeRepository workStationTypeRepository,
                                      ApplicationContext applicationContext,
                                      WorkStationTypeMapper workStationTypeMapper) {
        this.workStationTypeRepository = workStationTypeRepository;
        this.applicationContext = applicationContext;
        this.workStationTypeMapper = workStationTypeMapper;
    }

    @Override
    public WorkStationTypeDTO create(WorkStationTypeDTO dto) {
        WorkStationType entity = workStationTypeMapper.toEntity(dto);
        checkAreaName(entity.getWarehouseId(), entity.getName());
        return workStationTypeMapper.toDTO(workStationTypeRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        WorkStationType entity = workStationTypeRepository.retrieve(id);
        workStationTypeRepository.delete(entity);
    }

    @Override
    public WorkStationTypeDTO update(WorkStationTypeDTO dto) {
        WorkStationType entity = workStationTypeRepository.retrieve(dto.getId());
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkAreaName(entity.getWarehouseId(), dto.getName());
        }
        workStationTypeMapper.updateEntityFromDTO(dto, entity);
        return workStationTypeMapper.toDTO(workStationTypeRepository.save(entity));
    }

    @Override
    public WorkStationTypeDTO retrieve(String id) {
        return workStationTypeMapper.toDTO(workStationTypeRepository.retrieve(id));
    }

    @Override
    public List<WorkStationTypeDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        List<WorkStationType> entities = workStationTypeRepository.getList(null, sort);
        return workStationTypeMapper.toDTOList(entities);
    }

    @Override
    public List<WorkStationTypeDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<WorkStationType> entities = workStationTypeRepository.getBySearchTerm(searchTerm, sort);
        return workStationTypeMapper.toDTOList(entities);
    }

    @Override
    public Page<WorkStationTypeDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if(pageable.getSort()==null){
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<WorkStationType> entities = workStationTypeRepository.getBySearchTerm(searchTerm, pageable);
        return workStationTypeMapper.toDTOPage(pageable, entities);
    }

    private void checkAreaName(String warehouse, String areaName) {
        WorkStationType workStationType = workStationTypeRepository.getByName(warehouse, areaName);
        if (workStationType != null) {
            throw new ApiException(MasterDataException.EX_MD_WORK_STATION_TYPE_NAME_UNIQUE.toString(), areaName);
        }
    }
}