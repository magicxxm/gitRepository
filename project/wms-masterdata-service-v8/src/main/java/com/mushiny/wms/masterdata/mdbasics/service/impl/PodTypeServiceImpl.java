package com.mushiny.wms.masterdata.mdbasics.service.impl;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.PodTypeDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.PodTypePositionDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.PodTypeMapper;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.PodTypePositionMapper;
import com.mushiny.wms.masterdata.mdbasics.domain.PodType;
import com.mushiny.wms.masterdata.mdbasics.domain.PodTypePosition;
import com.mushiny.wms.masterdata.mdbasics.exception.MasterDataException;
import com.mushiny.wms.masterdata.mdbasics.repository.PodTypeRepository;
import com.mushiny.wms.masterdata.mdbasics.service.PodTypeService;
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
public class PodTypeServiceImpl implements PodTypeService {

    private final PodTypeRepository podTypeRepository;
    private final PodTypeMapper mdPodTypeMapper;
    private final PodTypePositionMapper mdPodTypePositionMapper;

    @Autowired
    public PodTypeServiceImpl(PodTypeRepository podTypeRepository,
                              PodTypeMapper mdPodTypeMapper,
                              PodTypePositionMapper mdPodTypePositionMapper) {
        this.podTypeRepository = podTypeRepository;
        this.mdPodTypeMapper = mdPodTypeMapper;
        this.mdPodTypePositionMapper = mdPodTypePositionMapper;
    }

    @Override
    public PodTypeDTO create(PodTypeDTO dto) {
        if (dto.getNumberOfRows() != dto.getPositions().size()) {
            throw new ApiException(MasterDataException.EX_MD_POD_TYPE_ROWS_ERROR.toString(), dto.getPositions().size());
        }
        PodType entity = mdPodTypeMapper.toEntity(dto);
        checkBayTypeName(entity.getWarehouseId(), entity.getName());
        for (PodTypePositionDTO positionDTO : dto.getPositions()) {
            entity.addPosition(mdPodTypePositionMapper.toEntity(positionDTO));
        }
        return mdPodTypeMapper.toDTO(podTypeRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        PodType entity = podTypeRepository.retrieve(id);
        podTypeRepository.delete(entity);
    }

    @Override
    public PodTypeDTO update(PodTypeDTO dto) {
        if (dto.getNumberOfRows() != dto.getPositions().size()) {
            throw new ApiException(MasterDataException.EX_MD_POD_TYPE_ROWS_ERROR.toString(), dto.getPositions().size());
        }
        PodType entity = podTypeRepository.retrieve(dto.getId());
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkBayTypeName(entity.getWarehouseId(), dto.getName());
        }
        mdPodTypeMapper.updateEntityFromDTO(dto, entity);
        entity.getPositions().clear();
        List<PodTypePosition> positions = mdPodTypePositionMapper.toEntityList(dto.getPositions());
        for (PodTypePosition position : positions) {
            entity.addPosition(position);
        }
        return mdPodTypeMapper.toDTO(podTypeRepository.save(entity));
    }

    @Override
    public PodTypeDTO retrieve(String id) {
        PodType entity = podTypeRepository.retrieve(id);
        PodTypeDTO dto = mdPodTypeMapper.toDTO(entity);
        dto.setPositions(mdPodTypePositionMapper.toDTOList(entity.getPositions()));
        return dto;
    }

    @Override
    public List<PodTypeDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<PodType> entities = podTypeRepository.getBySearchTerm(searchTerm, sort);
        return mdPodTypeMapper.toDTOList(entities);
    }

    @Override
    public Page<PodTypeDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if(pageable.getSort()==null){
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<PodType> entities = podTypeRepository.getBySearchTerm(searchTerm, pageable);
        return mdPodTypeMapper.toDTOPage(pageable, entities);
    }

    @Override
    public List<PodTypeDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        List<PodType> entities = podTypeRepository.getList(null, sort);
        return mdPodTypeMapper.toDTOList(entities);
    }

    private void checkBayTypeName(String warehouse, String bayTypeName) {
        PodType podType = podTypeRepository.getByName(warehouse, bayTypeName);
        if (podType != null) {
            throw new ApiException(MasterDataException.EX_MD_POD_TYPE_NAME_UNIQUE.toString(), bayTypeName);
        }
    }
}
