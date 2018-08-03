package com.mushiny.wms.system.service.impl;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.system.crud.dto.SelectionDTO;
import com.mushiny.wms.system.crud.mapper.SelectionMapper;
import com.mushiny.wms.system.domain.Selection;
import com.mushiny.wms.system.exception.SystemException;
import com.mushiny.wms.system.repository.SelectionRepository;
import com.mushiny.wms.system.service.SelectionService;
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
public class SelectionServiceImpl implements SelectionService {

    private final SelectionRepository selectionRepository;
    private final SelectionMapper selectionMapper;

    @Autowired
    public SelectionServiceImpl(SelectionRepository selectionRepository,
                                SelectionMapper selectionMapper) {
        this.selectionRepository = selectionRepository;
        this.selectionMapper = selectionMapper;
    }

    @Override
    public SelectionDTO create(SelectionDTO dto) {
        Selection entity = selectionMapper.toEntity(dto);
        checkSelectionKey(entity.getSelectionKey(), entity.getSelectionValue());
        return selectionMapper.toDTO(selectionRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        Selection entity = selectionRepository.retrieve(id);
        selectionRepository.delete(entity);
    }

    @Override
    public SelectionDTO update(SelectionDTO dto) {
        Selection entity = selectionRepository.retrieve(dto.getId());
        if (!(entity.getSelectionKey().equalsIgnoreCase(dto.getSelectionKey())
                && entity.getSelectionValue().equalsIgnoreCase(dto.getSelectionValue()))) {
            checkSelectionKey(dto.getSelectionKey(), dto.getSelectionValue());
        }
        selectionMapper.updateEntityFromDTO(dto, entity);
        return selectionMapper.toDTO(selectionRepository.save(entity));
    }

    @Override
    public SelectionDTO retrieve(String id) {
        return selectionMapper.toDTO(selectionRepository.retrieve(id));
    }

    @Override
    public List<SelectionDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<Selection> entities = selectionRepository.getBySearchTerm(searchTerm, sort);
        return selectionMapper.toDTOList(entities);
    }

    @Override
    public Page<SelectionDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if (pageable.getSort() == null) {
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "orderIndex"));
            pageable = new PageRequest(pageable.getPageNumber(),pageable.getPageSize(), sort);
        }
        Page<Selection> entities = selectionRepository.getBySearchTerm(searchTerm, pageable);
        return selectionMapper.toDTOPage(pageable, entities);
    }

    @Override
    public List<SelectionDTO> getBySelectionKey(String selectionKey) {
        List<Selection> entities = selectionRepository.findBySelectionKeyOrderByOrderIndex(selectionKey);
        return selectionMapper.toDTOList(entities);
    }

    private void checkSelectionKey(String selectionKey, String selectionValue) {
        Selection selection = selectionRepository.findBySelectionKeyAndSelectionValue(
                selectionKey, selectionValue);
        if (selection != null) {
            throw new ApiException(SystemException.EX_SYS_SELECTION_KEY_UNIQUE.toString(), selectionKey);
        }
    }
}
