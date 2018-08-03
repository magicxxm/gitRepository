package com.mushiny.wms.masterdata.ibbasics.service.impl;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceiveDestinationDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.mapper.ReceiveDestinationMapper;
import com.mushiny.wms.masterdata.ibbasics.domain.ReceiveDestination;
import com.mushiny.wms.masterdata.ibbasics.exception.InBoundException;
import com.mushiny.wms.masterdata.ibbasics.repository.ReceiveDestinationRepository;
import com.mushiny.wms.masterdata.ibbasics.service.ReceiveDestinationService;
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
public class ReceiveDestinationServiceImpl implements ReceiveDestinationService {

    private final ReceiveDestinationRepository receivingDestinationRepository;
    private final ReceiveDestinationMapper receivingDestinationMapper;

    @Autowired
    public ReceiveDestinationServiceImpl(ReceiveDestinationRepository receivingDestinationRepository,
                                         ReceiveDestinationMapper receivingDestinationMapper) {
        this.receivingDestinationRepository = receivingDestinationRepository;
        this.receivingDestinationMapper = receivingDestinationMapper;
    }

    @Override
    public ReceiveDestinationDTO create(ReceiveDestinationDTO dto) {
        ReceiveDestination entity = receivingDestinationMapper.toEntity(dto);
        checkReceivingDestinationName(entity.getWarehouseId(), entity.getName());
        return receivingDestinationMapper.toDTO(receivingDestinationRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        ReceiveDestination entity = receivingDestinationRepository.retrieve(id);
        receivingDestinationRepository.delete(entity);
    }

    @Override
    public ReceiveDestinationDTO update(ReceiveDestinationDTO dto) {
        ReceiveDestination entity = receivingDestinationRepository.retrieve(dto.getId());
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkReceivingDestinationName(entity.getWarehouseId(), dto.getName());
        }
        receivingDestinationMapper.updateEntityFromDTO(dto, entity);
        return receivingDestinationMapper.toDTO(receivingDestinationRepository.save(entity));
    }

    @Override
    public ReceiveDestinationDTO retrieve(String id) {
        return receivingDestinationMapper.toDTO(receivingDestinationRepository.retrieve(id));
    }

    @Override
    public List<ReceiveDestinationDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<ReceiveDestination> entities = receivingDestinationRepository.getBySearchTerm(searchTerm, sort);
        return receivingDestinationMapper.toDTOList(entities);
    }

    @Override
    public Page<ReceiveDestinationDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if(pageable.getSort()==null){
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<ReceiveDestination> entities = receivingDestinationRepository.getBySearchTerm(searchTerm, pageable);
        return receivingDestinationMapper.toDTOPage(pageable, entities);
    }

    @Override
    public List<ReceiveDestinationDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        List<ReceiveDestination> entities = receivingDestinationRepository.getList(null, sort);
        return receivingDestinationMapper.toDTOList(entities);
    }

    private void checkReceivingDestinationName(String warehouse, String name) {
        ReceiveDestination receivingDestination = receivingDestinationRepository.getByName(warehouse, name);
        if (receivingDestination != null) {
            throw new ApiException(InBoundException.EX_MD_IN_RECEIVE_DESTINATION_NAME_UNIQUE.toString(), name);
        }
    }
}
