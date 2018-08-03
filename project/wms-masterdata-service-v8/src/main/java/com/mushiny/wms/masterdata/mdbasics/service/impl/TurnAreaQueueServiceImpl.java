package com.mushiny.wms.masterdata.mdbasics.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.TurnAreaQueueDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.TurnAreaQueueMapper;
import com.mushiny.wms.masterdata.mdbasics.domain.TurnAreaQueue;
import com.mushiny.wms.masterdata.mdbasics.repository.TurnAreaQueueRepository;
import com.mushiny.wms.masterdata.mdbasics.service.TurnAreaQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TurnAreaQueueServiceImpl implements TurnAreaQueueService {

    private final TurnAreaQueueRepository trurnAreaQueueRepository;
    private final ApplicationContext applicationContext;
    private final TurnAreaQueueMapper trurnAreaQueueMapper;

    @Autowired
    public TurnAreaQueueServiceImpl(TurnAreaQueueRepository trurnAreaQueueRepository,
                                    ApplicationContext applicationContext,
                                    TurnAreaQueueMapper trurnAreaQueueMapper) {
        this.trurnAreaQueueRepository = trurnAreaQueueRepository;
        this.applicationContext = applicationContext;
        this.trurnAreaQueueMapper = trurnAreaQueueMapper;
    }

    @Override
    public TurnAreaQueueDTO create(TurnAreaQueueDTO dto) {
        TurnAreaQueue entity = trurnAreaQueueMapper.toEntity(dto);
        return trurnAreaQueueMapper.toDTO(trurnAreaQueueRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        TurnAreaQueue entity = trurnAreaQueueRepository.retrieve(id);
        trurnAreaQueueRepository.delete(entity);
    }

    @Override
    public TurnAreaQueueDTO update(TurnAreaQueueDTO dto) {
        TurnAreaQueue entity = trurnAreaQueueRepository.retrieve(dto.getId());

        trurnAreaQueueMapper.updateEntityFromDTO(dto, entity);
        return trurnAreaQueueMapper.toDTO(trurnAreaQueueRepository.save(entity));
    }

    @Override
    public TurnAreaQueueDTO retrieve(String id) {
        return trurnAreaQueueMapper.toDTO(trurnAreaQueueRepository.retrieve(id));
    }

    @Override
    public List<TurnAreaQueueDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<TurnAreaQueue> entities = trurnAreaQueueRepository.getBySearchTerm(searchTerm, sort);
        return trurnAreaQueueMapper.toDTOList(entities);
    }

    @Override
    public Page<TurnAreaQueueDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        Page<TurnAreaQueue> entities = trurnAreaQueueRepository.getBySearchTerm(searchTerm, pageable);
        return trurnAreaQueueMapper.toDTOPage(pageable, entities);
    }

    @Override
    public List<TurnAreaQueueDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "robot"));
        List<TurnAreaQueue> entities = trurnAreaQueueRepository.getList(null, sort);
        return trurnAreaQueueMapper.toDTOList(entities);
    }

}
