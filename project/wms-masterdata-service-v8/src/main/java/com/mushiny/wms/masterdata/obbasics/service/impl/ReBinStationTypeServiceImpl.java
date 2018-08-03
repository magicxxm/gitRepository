package com.mushiny.wms.masterdata.obbasics.service.impl;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.obbasics.crud.dto.ReBinStationTypeDTO;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.ReBinStationMapper;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.ReBinStationTypeMapper;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.ReBinWallMapper;
import com.mushiny.wms.masterdata.obbasics.domain.ReBinStation;
import com.mushiny.wms.masterdata.obbasics.domain.ReBinStationType;
import com.mushiny.wms.masterdata.obbasics.domain.ReBinWall;
import com.mushiny.wms.masterdata.obbasics.exception.OutBoundException;
import com.mushiny.wms.masterdata.obbasics.repository.ReBinStationRepository;
import com.mushiny.wms.masterdata.obbasics.repository.ReBinStationTypeRepository;
import com.mushiny.wms.masterdata.obbasics.repository.ReBinWallRepository;
import com.mushiny.wms.masterdata.obbasics.service.ReBinStationTypeService;
import com.mushiny.wms.masterdata.general.domain.Warehouse;
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
public class ReBinStationTypeServiceImpl implements ReBinStationTypeService {

    private final ReBinStationTypeRepository reBinStationTypeRepository;
    private final ReBinStationRepository reBinStationRepository;
    private final ReBinWallRepository reBinWallRepository;
    private final ReBinStationTypeMapper reBinStationTypeMapper;
    private final ReBinStationMapper reBinStationMapper;
    private final ReBinWallMapper reBinWallMapper;

    @Autowired
    public ReBinStationTypeServiceImpl(ReBinStationTypeRepository reBinStationTypeRepository,
                                       ReBinStationTypeMapper reBinStationTypeMapper,
                                       ReBinStationRepository reBinStationRepository,
                                       ReBinWallRepository reBinWallRepository,
                                       ReBinStationMapper reBinStationMapper,
                                       ReBinWallMapper reBinWallMapper) {
        this.reBinStationTypeRepository = reBinStationTypeRepository;
        this.reBinStationTypeMapper = reBinStationTypeMapper;
        this.reBinStationRepository = reBinStationRepository;
        this.reBinWallRepository = reBinWallRepository;
        this.reBinStationMapper = reBinStationMapper;
        this.reBinWallMapper = reBinWallMapper;
    }

    @Override
    public ReBinStationTypeDTO create(ReBinStationTypeDTO dto) {
        ReBinStationType entity = reBinStationTypeMapper.toEntity(dto);
        checkReBinStationTypeName(entity.getWarehouseId(), entity.getName());
        return reBinStationTypeMapper.toDTO(reBinStationTypeRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        ReBinStationType entity = reBinStationTypeRepository.retrieve(id);
        reBinStationTypeRepository.delete(entity);
    }

    @Override
    public ReBinStationTypeDTO update(ReBinStationTypeDTO dto) {
        ReBinStationType entity = reBinStationTypeRepository.retrieve(dto.getId());
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkReBinStationTypeName(entity.getWarehouseId(), dto.getName());
        }
        reBinStationTypeMapper.updateEntityFromDTO(dto, entity);
        return reBinStationTypeMapper.toDTO(reBinStationTypeRepository.save(entity));
    }

    @Override
    public ReBinStationTypeDTO retrieve(String id) {
        return reBinStationTypeMapper.toDTO(reBinStationTypeRepository.retrieve(id));
    }

    @Override
    public List<ReBinStationTypeDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        List<ReBinStationType> entities = reBinStationTypeRepository.getNotLockList(null, sort);
        return reBinStationTypeMapper.toDTOList(entities);
    }

    @Override
    public List<ReBinStationTypeDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<ReBinStationType> entities = reBinStationTypeRepository.getBySearchTerm(searchTerm, sort);
        return reBinStationTypeMapper.toDTOList(entities);
    }

    @Override
    public Page<ReBinStationTypeDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if(pageable.getSort()==null){
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<ReBinStationType> entities = reBinStationTypeRepository.getBySearchTerm(searchTerm, pageable);
        Page<ReBinStationTypeDTO> dtoPage = reBinStationTypeMapper.toDTOPage(pageable, entities);
        if (dtoPage.getContent() != null) {
            for (ReBinStationTypeDTO dto : dtoPage.getContent()) {
                List<ReBinStation> reBinStations = reBinStationRepository.getByReBinStationTypeId(dto.getId());
                dto.setRebinStations(reBinStationMapper.toDTOList(reBinStations));
                if (dto.getRebinWallType() != null) {
                    List<ReBinWall> reBinWalls = reBinWallRepository.getByReBinWallTypeId(dto.getRebinWallType().getId());
                    dto.setRebinWalls(reBinWallMapper.toDTOList(reBinWalls));
                }
            }
        }
        return dtoPage;
    }

    private void checkReBinStationTypeName(String warehouse, String name) {
        ReBinStationType reBinStationType = reBinStationTypeRepository.getByName(warehouse, name);
        if (reBinStationType != null) {
            throw new ApiException(OutBoundException.EX_MD_OB_REBIN_STATION_TYPE_NAME_UNIQUE.toString(), name);
        }
    }
}
