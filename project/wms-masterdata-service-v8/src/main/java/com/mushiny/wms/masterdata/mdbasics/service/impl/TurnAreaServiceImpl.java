package com.mushiny.wms.masterdata.mdbasics.service.impl;

import com.mushiny.wms.masterdata.mdbasics.crud.dto.TurnAreaDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.TurnAreaPositionDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.TurnAreaMapper;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.TurnAreaPositionMapper;
import com.mushiny.wms.masterdata.mdbasics.domain.TurnArea;
import com.mushiny.wms.masterdata.mdbasics.domain.TurnAreaPosition;
import com.mushiny.wms.masterdata.mdbasics.repository.TurnAreaRepository;
import com.mushiny.wms.masterdata.mdbasics.service.TurnAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TurnAreaServiceImpl implements TurnAreaService {

    private final TurnAreaRepository trurnAreaRepository;
    private final TurnAreaMapper trurnAreaMapper;
    private final TurnAreaPositionMapper trurnAreaPositionMapper;

    @Autowired
    public TurnAreaServiceImpl(TurnAreaRepository trurnAreaRepository,
                               TurnAreaMapper trurnAreaMapper,
                               TurnAreaPositionMapper trurnAreaPositionMapper) {
        this.trurnAreaRepository = trurnAreaRepository;
        this.trurnAreaMapper = trurnAreaMapper;
        this.trurnAreaPositionMapper = trurnAreaPositionMapper;
    }

    @Override
    public TurnAreaDTO create(TurnAreaDTO dto) {
        TurnArea entity = trurnAreaMapper.toEntity(dto);
        for (TurnAreaPositionDTO positionDTO : dto.getPositions()) {
            entity.addPosition(trurnAreaPositionMapper.toEntity(positionDTO));
        }
        TurnAreaDTO dtos =  trurnAreaMapper.toDTO(trurnAreaRepository.save(entity));
        TurnArea entities = trurnAreaRepository.retrieve(dtos.getId());
        TurnAreaDTO list = trurnAreaMapper.toDTO(entities);
        list.setPositions(trurnAreaPositionMapper.toDTOList(entities.getPositions()));
        return list;
    }

    @Override
    public void delete(String id) {
        TurnArea entity = trurnAreaRepository.retrieve(id);
        trurnAreaRepository.delete(entity);
    }

    @Override
    public TurnAreaDTO update(TurnAreaDTO dto) {
        TurnArea entity = trurnAreaRepository.retrieve(dto.getId());
        trurnAreaMapper.updateEntityFromDTO(dto, entity);
        entity.getPositions().clear();
        List<TurnAreaPosition> positions = trurnAreaPositionMapper.toEntityList(dto.getPositions());
        for (TurnAreaPosition position : positions) {
            entity.addPosition(position);
        }
        TurnAreaDTO dtos =  trurnAreaMapper.toDTO(trurnAreaRepository.save(entity));
        TurnArea entities = trurnAreaRepository.retrieve(dtos.getId());
        TurnAreaDTO list = trurnAreaMapper.toDTO(entities);
        list.setPositions(trurnAreaPositionMapper.toDTOList(entities.getPositions()));
        return list;
    }

    @Override
    public TurnAreaDTO retrieve(String id) {
        TurnArea entity = trurnAreaRepository.retrieve(id);
        TurnAreaDTO dto = trurnAreaMapper.toDTO(entity);
        dto.setPositions(trurnAreaPositionMapper.toDTOList(entity.getPositions()));
        return dto;
    }

    @Override
    public List<TurnAreaDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<TurnArea> entities = trurnAreaRepository.getBySearchTerm(searchTerm, sort);
        return trurnAreaMapper.toDTOList(entities);
    }

    @Override
    public Page<TurnAreaDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        Page<TurnArea> entities = trurnAreaRepository.getBySearchTerm(searchTerm, pageable);
        return trurnAreaMapper.toDTOPage(pageable, entities);
    }

    @Override
    public List<TurnAreaDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        List<TurnArea> entities = trurnAreaRepository.getList(null, sort);
        return trurnAreaMapper.toDTOList(entities);
    }
}
