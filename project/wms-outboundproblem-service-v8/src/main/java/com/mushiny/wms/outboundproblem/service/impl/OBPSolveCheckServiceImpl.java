package com.mushiny.wms.outboundproblem.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.outboundproblem.crud.common.dto.StorageLocationDTO;
import com.mushiny.wms.outboundproblem.crud.common.mapper.StorageLocationMapper;
import com.mushiny.wms.outboundproblem.crud.dto.OBPSolveCheckDTO;
import com.mushiny.wms.outboundproblem.crud.mapper.OBPSolveCheckMapper;
import com.mushiny.wms.outboundproblem.domain.OBPSolveCheck;
import com.mushiny.wms.outboundproblem.domain.common.StorageLocation;
import com.mushiny.wms.outboundproblem.repository.OBPSolveCheckRepository;
import com.mushiny.wms.outboundproblem.repository.common.StorageLocationRepository;
import com.mushiny.wms.outboundproblem.service.OBPSolveCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OBPSolveCheckServiceImpl implements OBPSolveCheckService {

    private final OBPSolveCheckRepository obpSolveCheckRepository;
    private final OBPSolveCheckMapper obpSolveCheckMapper;
    private final StorageLocationRepository storageLocationRepository;
    private final ApplicationContext applicationContext;
    private final StorageLocationMapper storageLocationMapper;

    @Autowired
    public OBPSolveCheckServiceImpl(OBPSolveCheckRepository obpSolveCheckRepository,
                                    OBPSolveCheckMapper obpSolveCheckMapper,
                                    StorageLocationRepository storageLocationRepository,
                                    ApplicationContext applicationContext,
                                    StorageLocationMapper storageLocationMapper) {
        this.obpSolveCheckRepository = obpSolveCheckRepository;
        this.obpSolveCheckMapper = obpSolveCheckMapper;
        this.storageLocationRepository = storageLocationRepository;
        this.applicationContext = applicationContext;
        this.storageLocationMapper = storageLocationMapper;
    }

    @Override
    public OBPSolveCheckDTO create(OBPSolveCheckDTO dto) {
        OBPSolveCheck entity = obpSolveCheckMapper.toEntity(dto);
        return obpSolveCheckMapper.toDTO(obpSolveCheckRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        OBPSolveCheck entity = obpSolveCheckRepository.retrieve(id);
        obpSolveCheckRepository.delete(entity);
    }

    @Override
    public OBPSolveCheckDTO update(OBPSolveCheckDTO dto) {
        OBPSolveCheck entity = obpSolveCheckRepository.retrieve(dto.getId());
        obpSolveCheckMapper.updateEntityFromDTO(dto, entity);
        return obpSolveCheckMapper.toDTO(obpSolveCheckRepository.save(entity));
    }

    @Override
    public OBPSolveCheckDTO retrieve(String id) {
        return obpSolveCheckMapper.toDTO(obpSolveCheckRepository.retrieve(id));
    }

    @Override
    public List<OBPSolveCheckDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<OBPSolveCheck> entities = obpSolveCheckRepository.getBySearchTerm(searchTerm, sort);
        return obpSolveCheckMapper.toDTOList(entities);
    }

    @Override
    public Page<OBPSolveCheckDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        Page<OBPSolveCheck> entities = obpSolveCheckRepository.getBySearchTerm(searchTerm, pageable);
        return obpSolveCheckMapper.toDTOPage(pageable, entities);
    }

    @Override
    public StorageLocationDTO getStorageLocationIdByName(String storageLocationName) {
        StorageLocation storageLocation = storageLocationRepository.getAllStoragetypeByName(applicationContext.getCurrentWarehouse(),storageLocationName);
        return storageLocationMapper.toDTO(storageLocation);
    }
}
