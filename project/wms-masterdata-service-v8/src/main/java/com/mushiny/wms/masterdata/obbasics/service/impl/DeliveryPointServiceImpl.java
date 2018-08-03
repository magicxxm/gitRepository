package com.mushiny.wms.masterdata.obbasics.service.impl;


import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.obbasics.crud.dto.DeliveryPointDTO;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.CarrierMapper;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.DeliveryPointMapper;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.DeliverySortCodeMapper;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.DeliveryTimeMapper;
import com.mushiny.wms.masterdata.obbasics.domain.DeliveryPoint;
import com.mushiny.wms.masterdata.obbasics.domain.DeliveryTime;
import com.mushiny.wms.masterdata.obbasics.domain.LabelController;
import com.mushiny.wms.masterdata.obbasics.exception.OutBoundException;
import com.mushiny.wms.masterdata.obbasics.repository.CarrierRepository;
import com.mushiny.wms.masterdata.obbasics.repository.DeliveryPointRepository;
import com.mushiny.wms.masterdata.obbasics.repository.DeliverySortCodeRepository;
import com.mushiny.wms.masterdata.obbasics.repository.DeliveryTimeRepository;
import com.mushiny.wms.masterdata.obbasics.service.DeliveryPointService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Laptop-11 on 2017/6/8.
 */
@Service
@Transactional
public class DeliveryPointServiceImpl implements DeliveryPointService {
    private final DeliveryPointMapper deliveryPointMapper;
    private final DeliveryPointRepository deliveryPointRepository;
    private final DeliveryTimeMapper deliveryTimeMapper;
    private final DeliveryTimeRepository deliveryTimeRepository;
    private final DeliverySortCodeMapper deliverySortCodeMapper;
    private final DeliverySortCodeRepository deliverySortCodeRepository;
    private final CarrierMapper carrierMapper;
    private final CarrierRepository carrerRepository;

    public DeliveryPointServiceImpl(DeliveryPointMapper deliveryPointMapper,
                                    DeliveryPointRepository deliveryPointRepository,
                                    DeliveryTimeMapper deliveryTimeMapper,
                                    DeliveryTimeRepository deliveryTimeRepository,
                                    DeliverySortCodeMapper deliverySortCodeMapper,
                                    DeliverySortCodeRepository deliverySortCodeRepository,
                                    CarrierMapper carrierMapper,
                                    CarrierRepository carrerRepository) {

        this.deliveryPointMapper = deliveryPointMapper;
        this.deliveryPointRepository = deliveryPointRepository;
        this.deliveryTimeMapper = deliveryTimeMapper;
        this.deliveryTimeRepository = deliveryTimeRepository;
        this.deliverySortCodeMapper = deliverySortCodeMapper;
        this.deliverySortCodeRepository = deliverySortCodeRepository;
        this.carrierMapper = carrierMapper;
        this.carrerRepository = carrerRepository;

    }


    @Override
    public DeliveryPointDTO create(DeliveryPointDTO dto) {
        DeliveryPoint entity = deliveryPointMapper.toEntity(dto);
       String name= entity.getDeliverySortCode().getCode();
        checkDeliveryPointName(entity.getWarehouseId(), entity.getDeliveryTime().getId(), entity.getDeliverySortCode().getId());
        return deliveryPointMapper.toDTO(deliveryPointRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        DeliveryPoint entity = deliveryPointRepository.retrieve(id);
        deliveryPointRepository.delete(entity);
    }

    @Override
    public DeliveryPointDTO update(DeliveryPointDTO dto) {
        DeliveryPoint entity = deliveryPointRepository.retrieve(dto.getId());
        if (!(entity.getDeliveryTime().getId().equalsIgnoreCase(entity.getDeliveryTime().getId())
                &&entity.getDeliverySortCode().getId().equalsIgnoreCase(entity.getDeliverySortCode().getId()))) {
            checkDeliveryPointName(entity.getWarehouseId(),
                    dto.getDeliveryTime().getId(), dto.getDeliverySortCode().getId());
        }
        deliveryPointMapper.updateEntityFromDTO(dto, entity);
        return deliveryPointMapper.toDTO(deliveryPointRepository.save(entity));
    }

    @Override
    public DeliveryPointDTO retrieve(String id) {
        return deliveryPointMapper.toDTO(deliveryPointRepository.retrieve(id));
    }

    @Override
    public List<DeliveryPointDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<DeliveryPoint> entities = deliveryPointRepository.getBySearchTerm(searchTerm, sort);
        return deliveryPointMapper.toDTOList(entities);
    }

    @Override
    public Page<DeliveryPointDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if (pageable.getSort() == null) {
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "deliveryTime.deliveryTime"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<DeliveryPoint> entities = deliveryPointRepository.getBySearchTerm(searchTerm, pageable);
        return deliveryPointMapper.toDTOPage(pageable, entities);
    }

    private void checkDeliveryPointName(String warehouse, String time, String code) {
        DeliveryPoint boxType = deliveryPointRepository.getByName(warehouse, time, code);
        if (boxType != null) {
            throw new ApiException("该时间该目的地对应的承运商已经创建了！");
        }
    }


}
