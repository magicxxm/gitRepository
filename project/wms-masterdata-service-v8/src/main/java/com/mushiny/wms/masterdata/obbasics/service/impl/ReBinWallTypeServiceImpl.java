package com.mushiny.wms.masterdata.obbasics.service.impl;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.obbasics.crud.dto.ReBinWallTypeDTO;
import com.mushiny.wms.masterdata.obbasics.crud.dto.ReBinWallTypePositionDTO;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.ReBinCellMapper;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.ReBinCellTypeMapper;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.ReBinWallTypeMapper;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.ReBinWallTypePositionMapper;
import com.mushiny.wms.masterdata.obbasics.domain.ReBinCellType;
import com.mushiny.wms.masterdata.obbasics.domain.ReBinWallType;
import com.mushiny.wms.masterdata.obbasics.domain.ReBinWallTypePosition;
import com.mushiny.wms.masterdata.obbasics.exception.OutBoundException;
import com.mushiny.wms.masterdata.obbasics.repository.ReBinCellRepository;
import com.mushiny.wms.masterdata.obbasics.repository.ReBinWallTypePositionRepository;
import com.mushiny.wms.masterdata.obbasics.repository.ReBinWallTypeRepository;
import com.mushiny.wms.masterdata.obbasics.service.ReBinWallTypeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReBinWallTypeServiceImpl implements ReBinWallTypeService {

    private final ReBinWallTypeRepository reBinWallTypeRepository;
    private final ReBinWallTypeMapper reBinWallTypeMapper;
    private final ReBinWallTypePositionMapper reBinWallTypePositionMapper;
    private final ReBinWallTypePositionRepository reBinWallTypePositionRepository;
    private final ReBinCellMapper reBinCellMapper;
    private final ReBinCellTypeMapper reBinCellTypeMapper;
    private final ReBinCellRepository reBinCellRepository;

    public ReBinWallTypeServiceImpl(ReBinWallTypeRepository reBinWallTypeRepository,
                                    ReBinWallTypeMapper reBinWallTypeMapper,
                                    ReBinWallTypePositionMapper reBinWallTypePositionMapper,
                                    ReBinWallTypePositionRepository reBinWallTypePositionRepository,
                                    ReBinCellMapper reBinCellMapper,
                                    ReBinCellTypeMapper reBinCellTypeMapper,
                                    ReBinCellRepository reBinCellRepository) {
        this.reBinWallTypeRepository = reBinWallTypeRepository;
        this.reBinWallTypeMapper = reBinWallTypeMapper;
        this.reBinWallTypePositionMapper = reBinWallTypePositionMapper;
        this.reBinWallTypePositionRepository = reBinWallTypePositionRepository;
        this.reBinCellMapper = reBinCellMapper;
        this.reBinCellTypeMapper = reBinCellTypeMapper;
        this.reBinCellRepository = reBinCellRepository;
    }

    @Override
    public ReBinWallTypeDTO create(ReBinWallTypeDTO dto) {
        ReBinWallType entity = reBinWallTypeMapper.toEntity(dto);
        checkReBinWallTypeName(entity.getWarehouseId(), entity.getName());
        for (ReBinWallTypePositionDTO positionDTO : dto.getPositions()) {
            entity.addPosition(reBinWallTypePositionMapper.toEntity(positionDTO));
        }
        return reBinWallTypeMapper.toDTO(reBinWallTypeRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        ReBinWallType entity = reBinWallTypeRepository.retrieve(id);
        reBinWallTypeRepository.delete(entity);
    }

    @Override
    public ReBinWallTypeDTO update(ReBinWallTypeDTO dto) {
        ReBinWallType entity = reBinWallTypeRepository.retrieve(dto.getId());
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkReBinWallTypeName(entity.getWarehouseId(), dto.getName());
        }
        reBinWallTypeMapper.updateEntityFromDTO(dto, entity);
        entity.getPositions().clear();
        List<ReBinWallTypePosition> positions = reBinWallTypePositionMapper.toEntityList(dto.getPositions());
        for (ReBinWallTypePosition position : positions) {
            entity.addPosition(position);
        }
        return reBinWallTypeMapper.toDTO(reBinWallTypeRepository.save(entity));
    }

    @Override
    public ReBinWallTypeDTO retrieve(String id) {
        ReBinWallType entity = reBinWallTypeRepository.retrieve(id);
        ReBinWallTypeDTO dto = reBinWallTypeMapper.toDTO(entity);
        dto.setPositions(reBinWallTypePositionMapper.toDTOList(entity.getPositions()));
        return dto;
    }

    @Override
    public List<ReBinWallTypeDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        List<ReBinWallType> entities = reBinWallTypeRepository.getNotLockList(null, sort);
        return reBinWallTypeMapper.toDTOList(entities);
    }

    @Override
    public List<ReBinWallTypeDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<ReBinWallType> entities = reBinWallTypeRepository.getBySearchTerm(searchTerm, sort);
        return reBinWallTypeMapper.toDTOList(entities);
    }

    @Override
    public Page<ReBinWallTypeDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if(pageable.getSort()==null){
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<ReBinWallType> entities = reBinWallTypeRepository.getBySearchTerm(searchTerm,pageable);
        Page<ReBinWallTypeDTO> dtoPage = reBinWallTypeMapper.toDTOPage(pageable, entities);
        if (dtoPage.getContent() != null) {
            for (ReBinWallTypeDTO dto : dtoPage.getContent()) {
                List<ReBinCellType> reBinWallTypePositions = reBinWallTypePositionRepository.getByTypeId(dto.getId());
                dto.setRebinCellTypes(reBinCellTypeMapper.toDTOList(reBinWallTypePositions));
            }
        }
        return dtoPage;
    }

    private void checkReBinWallTypeName(String warehouse, String name) {
        ReBinWallType reBinWallType = reBinWallTypeRepository.getByName(warehouse, name);
        if (reBinWallType != null) {
            throw new ApiException(OutBoundException.EX_MD_OB_REBIN_WALL_TYPE_NAME_UNIQUE.toString(), name);
        }
    }
}
