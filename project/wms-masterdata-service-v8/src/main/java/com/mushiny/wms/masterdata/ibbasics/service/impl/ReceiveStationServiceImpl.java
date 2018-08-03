package com.mushiny.wms.masterdata.ibbasics.service.impl;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.ibbasics.business.ReceiveStationBusiness;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceiveStationDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.mapper.ReceiveStationMapper;
import com.mushiny.wms.masterdata.ibbasics.crud.mapper.ReceiveStationPositionMapper;
import com.mushiny.wms.masterdata.ibbasics.domain.ReceiveStation;
import com.mushiny.wms.masterdata.ibbasics.domain.ReceiveStationPosition;
import com.mushiny.wms.masterdata.ibbasics.exception.InBoundException;
import com.mushiny.wms.masterdata.ibbasics.repository.ReceiveStationRepository;
import com.mushiny.wms.masterdata.ibbasics.service.ReceiveStationService;
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
public class ReceiveStationServiceImpl implements ReceiveStationService {

    private final ReceiveStationRepository receivingStationRepository;
    private final ReceiveStationMapper receivingStationMapper;
    private final ReceiveStationPositionMapper receiveStationPositionMapper;
    private final ReceiveStationBusiness receiveStationBusiness;

    @Autowired
    public ReceiveStationServiceImpl(ReceiveStationRepository receivingStationRepository,
                                     ReceiveStationMapper receivingStationMapper,
                                     ReceiveStationPositionMapper receiveStationPositionMapper,
                                     ReceiveStationBusiness receiveStationBusiness) {
        this.receivingStationRepository = receivingStationRepository;
        this.receivingStationMapper = receivingStationMapper;
        this.receiveStationPositionMapper = receiveStationPositionMapper;
        this.receiveStationBusiness = receiveStationBusiness;
    }

    @Override
    public void createMore(ReceiveStationDTO dto) {
        checkReceivingStationName(dto.getWarehouseId(),dto.getName());
        receiveStationBusiness.createMore(dto);
    }

    @Override
    public ReceiveStationDTO create(ReceiveStationDTO dto) {
        return null;
    }

    @Override
    public void delete(String id) {
        ReceiveStation entity = receivingStationRepository.retrieve(id);
        if (entity.getOperator() != null) {
            throw new ApiException("该状态的工作站不能删除");
        }
        receivingStationRepository.delete(entity);
    }

    @Override
    public ReceiveStationDTO update(ReceiveStationDTO dto) {
        ReceiveStation entity = receivingStationRepository.retrieve(dto.getId());
        if (entity.getOperator() != null) {
            throw new ApiException("该状态的工作站不能更新");
        }
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkReceivingStationName(entity.getWarehouseId(),dto.getName());
        }
        receiveStationBusiness.updateMore(dto);
        return receivingStationMapper.toDTO(receivingStationRepository.save(entity));
    }

    @Override
    public ReceiveStationDTO retrieve(String id) {
        ReceiveStation entity = receivingStationRepository.retrieve(id);
        ReceiveStationDTO dto = receivingStationMapper.toDTO(entity);
        dto.setPositions(receiveStationPositionMapper.toDTOList(entity.getPositions()));
        return dto;
    }

    @Override
    public List<ReceiveStationDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<ReceiveStation> entities = receivingStationRepository.getBySearchTerm(searchTerm, sort);
        return receivingStationMapper.toDTOList(entities);
    }

    @Override
    public Page<ReceiveStationDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if (pageable.getSort() == null) {
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<ReceiveStation> entities = receivingStationRepository.getBySearchTerm(searchTerm, pageable);
        return receivingStationMapper.toDTOPage(pageable, entities);
    }

    @Override
    public List<ReceiveStationDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        List<ReceiveStation> entities = receivingStationRepository.getList(null, sort);
        return receivingStationMapper.toDTOList(entities);
    }

    private void checkReceivingStationName(String warehouse, String name) {
        ReceiveStation receivingStation = receivingStationRepository
                .getByName(warehouse, name);
        if (receivingStation != null) {
            throw new ApiException(InBoundException.EX_MD_IN_RECEIVE_STATION_NAME_UNIQUE.toString(), name);
        }
    }
}
