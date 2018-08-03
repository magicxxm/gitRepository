package com.mushiny.wms.masterdata.obbasics.service.impl;


import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.obbasics.crud.dto.DeliverySortCodeDTO;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.DeliverySortCodeMapper;
import com.mushiny.wms.masterdata.obbasics.domain.Carrier;
import com.mushiny.wms.masterdata.obbasics.domain.DeliveryPoint;
import com.mushiny.wms.masterdata.obbasics.domain.DeliverySortCode;
import com.mushiny.wms.masterdata.obbasics.exception.OutBoundException;
import com.mushiny.wms.masterdata.obbasics.repository.DeliverySortCodeRepository;
import com.mushiny.wms.masterdata.obbasics.service.DeliverySortCodeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Laptop-11 on 2017/6/8.
 */
@Service
@Transactional
public class DeliverySortCodeServiceImpl implements DeliverySortCodeService {
    private final ApplicationContext applicationContext;
    private final DeliverySortCodeMapper deliverySortCodeMapper;
    private final DeliverySortCodeRepository deliverySortCodeRepository;

    public DeliverySortCodeServiceImpl(ApplicationContext applicationContext, DeliverySortCodeMapper deliverySortCodeMapper,
                                       DeliverySortCodeRepository deliverySortCodeRepository) {
        this.applicationContext = applicationContext;
        this.deliverySortCodeMapper = deliverySortCodeMapper;
        this.deliverySortCodeRepository = deliverySortCodeRepository;
    }

    @Override
    public DeliverySortCodeDTO create(DeliverySortCodeDTO dto) {
        DeliverySortCode entity=deliverySortCodeMapper.toEntity(dto);
        checkDeliverySortCodeName(entity.getWarehouseId(), entity.getCode());
        return deliverySortCodeMapper.toDTO(deliverySortCodeRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        DeliverySortCode entity = deliverySortCodeRepository.retrieve(id);
        deliverySortCodeRepository.delete(entity);
    }

    @Override
    public DeliverySortCodeDTO update(DeliverySortCodeDTO dto) {
        DeliverySortCode entity = deliverySortCodeRepository.retrieve(dto.getId());
        if (!(entity.getCode().equalsIgnoreCase(dto.getCode()))) {
            checkDeliverySortCodeName(entity.getWarehouseId(),dto.getCode());
        }
        deliverySortCodeMapper.updateEntityFromDTO(dto, entity);
        return deliverySortCodeMapper.toDTO(deliverySortCodeRepository.save(entity));
    }

    @Override
    public DeliverySortCodeDTO retrieve(String id) {
        return deliverySortCodeMapper.toDTO(deliverySortCodeRepository.retrieve(id));
    }

    @Override
    public List<DeliverySortCodeDTO> getBySearchTerm(String searchTerm, Sort sort) {

        List<DeliverySortCode> entities = deliverySortCodeRepository.getBySearchTerm(searchTerm, sort);
        return deliverySortCodeMapper.toDTOList(entities);
    }

    @Override
    public Page<DeliverySortCodeDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if(pageable.getSort()==null) {
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "code"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<DeliverySortCode> entities = deliverySortCodeRepository.getBySearchTerm(searchTerm, pageable);
        return deliverySortCodeMapper.toDTOPage(pageable, entities);
    }
    private void checkDeliverySortCodeName(String warehouse, String code) {
        DeliverySortCode boxType = deliverySortCodeRepository.getByName(warehouse, code);
        if (boxType != null) {
            throw new ApiException("该仓库已经创建了该发货点！");
        }
    }

    @Override
    public List<DeliverySortCodeDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "code"));
        List<DeliverySortCode> entities = deliverySortCodeRepository.getNotLockList(null, sort);
        return deliverySortCodeMapper.toDTOList(entities);
    }
}
