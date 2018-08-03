package com.mushiny.wms.masterdata.obbasics.service.impl;


import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.obbasics.crud.dto.DeliveryTimeDTO;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.DeliveryTimeMapper;
import com.mushiny.wms.masterdata.obbasics.domain.Carrier;
import com.mushiny.wms.masterdata.obbasics.domain.DeliverySortCode;
import com.mushiny.wms.masterdata.obbasics.domain.DeliveryTime;
import com.mushiny.wms.masterdata.obbasics.exception.OutBoundException;
import com.mushiny.wms.masterdata.obbasics.repository.DeliveryTimeRepository;
import com.mushiny.wms.masterdata.obbasics.service.DeliveryTimeService;
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
public class DeliveryTimeServiceImpl implements DeliveryTimeService {
    private final ApplicationContext applicationContext;
    private final DeliveryTimeMapper deliveryTimeMapper;
    private final DeliveryTimeRepository deliveryTimeRepository;

    public DeliveryTimeServiceImpl(ApplicationContext applicationContext, DeliveryTimeMapper deliveryTimeMapper,
                                   DeliveryTimeRepository deliveryTimeRepository) {
        this.applicationContext = applicationContext;
        this.deliveryTimeMapper = deliveryTimeMapper;
        this.deliveryTimeRepository = deliveryTimeRepository;
    }

    @Override
    public DeliveryTimeDTO create(DeliveryTimeDTO dto) {
        DeliveryTime entity=deliveryTimeMapper.toEntity(dto);
        checkDeliveryTimeName(entity.getWarehouseId(),dto.getDeliveryTime());
        return deliveryTimeMapper.toDTO(deliveryTimeRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        DeliveryTime entity = deliveryTimeRepository.retrieve(id);
        deliveryTimeRepository.delete(entity);
    }

    @Override
    public DeliveryTimeDTO update(DeliveryTimeDTO dto) {
        DeliveryTime entity = deliveryTimeRepository.retrieve(dto.getId());
        if (!entity.getDeliveryTime().equalsIgnoreCase(dto.getDeliveryTime())) {
            checkDeliveryTimeName(entity.getWarehouseId(),dto.getDeliveryTime());
        }
        deliveryTimeMapper.updateEntityFromDTO(dto, entity);
        return deliveryTimeMapper.toDTO(deliveryTimeRepository.save(entity));
    }

    @Override
    public DeliveryTimeDTO retrieve(String id) {
        return deliveryTimeMapper.toDTO(deliveryTimeRepository.retrieve(id));
    }

    @Override
    public List<DeliveryTimeDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<DeliveryTime> entities = deliveryTimeRepository.getBySearchTerm(searchTerm, sort);
        return deliveryTimeMapper.toDTOList(entities);
    }

    @Override
    public Page<DeliveryTimeDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if(pageable.getSort()==null){
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "deliveryTime"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<DeliveryTime> entities = deliveryTimeRepository.getBySearchTerm(searchTerm, pageable);
        return deliveryTimeMapper.toDTOPage(pageable, entities);
    }

    @Override
    public List<DeliveryTimeDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "deliveryTime"));
        List<DeliveryTime> entities = deliveryTimeRepository.getNotLockList(null, sort);
        return deliveryTimeMapper.toDTOList(entities);
    }

    private void checkDeliveryTimeName(String warehouse,String time) {
        DeliveryTime boxType = deliveryTimeRepository.getByName(warehouse,time);
        if (boxType != null) {
            throw new ApiException("该仓库已经创建了该时间！");
        }
    }
}
