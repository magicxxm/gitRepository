package com.mushiny.wms.masterdata.mdbasics.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.RobotTypeDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.RobotTypeMapper;
import com.mushiny.wms.masterdata.mdbasics.domain.RobotType;
import com.mushiny.wms.masterdata.mdbasics.exception.MasterDataException;
import com.mushiny.wms.masterdata.mdbasics.repository.RobotTypeRepository;
import com.mushiny.wms.masterdata.mdbasics.service.RobotTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RobotTypeServiceImpl implements RobotTypeService {

    private final RobotTypeRepository robotTypeRepository;
    private final ApplicationContext applicationContext;
    private final RobotTypeMapper robotTypeMapper;

    @Autowired
    public RobotTypeServiceImpl(RobotTypeRepository robotTypeRepository,
                                ApplicationContext applicationContext,
                                RobotTypeMapper robotTypeMapper) {
        this.robotTypeRepository = robotTypeRepository;
        this.applicationContext = applicationContext;
        this.robotTypeMapper = robotTypeMapper;
    }

    @Override
    public RobotTypeDTO create(RobotTypeDTO dto) {
        RobotType entity = robotTypeMapper.toEntity(dto);
        return robotTypeMapper.toDTO(robotTypeRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        RobotType entity = robotTypeRepository.retrieve(id);
        robotTypeRepository.delete(entity);
    }

    @Override
    public RobotTypeDTO update(RobotTypeDTO dto) {
        RobotType entity = robotTypeRepository.retrieve(dto.getId());
        robotTypeMapper.updateEntityFromDTO(dto, entity);
        return robotTypeMapper.toDTO(robotTypeRepository.save(entity));
    }

    @Override
    public RobotTypeDTO retrieve(String id) {
        return robotTypeMapper.toDTO(robotTypeRepository.retrieve(id));
    }

    @Override
    public List<RobotTypeDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        List<RobotType> entities = robotTypeRepository.getList(null, sort);
        return robotTypeMapper.toDTOList(entities);
    }

    @Override
    public List<RobotTypeDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<RobotType> entities = robotTypeRepository.getBySearchTerm(searchTerm, sort);
        return robotTypeMapper.toDTOList(entities);
    }

    @Override
    public Page<RobotTypeDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        Page<RobotType> entities = robotTypeRepository.getBySearchTerm(searchTerm, pageable);
        return robotTypeMapper.toDTOPage(pageable, entities);
    }
}