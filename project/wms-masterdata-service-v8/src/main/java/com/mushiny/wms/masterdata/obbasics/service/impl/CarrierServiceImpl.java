package com.mushiny.wms.masterdata.obbasics.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.obbasics.crud.dto.CarrierDTO;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.CarrierMapper;
import com.mushiny.wms.masterdata.obbasics.domain.Carrier;
import com.mushiny.wms.masterdata.obbasics.domain.DeliverySortCode;
import com.mushiny.wms.masterdata.obbasics.exception.OutBoundException;
import com.mushiny.wms.masterdata.obbasics.repository.CarrierRepository;
import com.mushiny.wms.masterdata.obbasics.service.CarrierService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CarrierServiceImpl implements CarrierService {

    private final CarrierRepository carrierRepository;
    private final ApplicationContext applicationContext;
    private final CarrierMapper carrierMapper;

    public CarrierServiceImpl(CarrierRepository carrierRepository,
                              ApplicationContext applicationContext,
                              CarrierMapper carrierMapper) {
        this.carrierRepository = carrierRepository;
        this.applicationContext = applicationContext;
        this.carrierMapper = carrierMapper;
    }

    @Override
    public CarrierDTO create(CarrierDTO dto) {
        Carrier entity = carrierMapper.toEntity(dto);
        checkCarrierName(entity.getWarehouseId(), entity.getName());
        return carrierMapper.toDTO(carrierRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        Carrier entity = carrierRepository.retrieve(id);
        carrierRepository.delete(entity);
    }

    @Override
    public CarrierDTO update(CarrierDTO dto) {
        Carrier entity = carrierRepository.retrieve(dto.getId());
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkCarrierName(entity.getWarehouseId(), dto.getName());
        }
        carrierMapper.updateEntityFromDTO(dto, entity);
        return carrierMapper.toDTO(carrierRepository.save(entity));
    }


    @Override
    public CarrierDTO retrieve(String id) {
        return carrierMapper.toDTO(carrierRepository.retrieve(id));
    }

    @Override
    public List<CarrierDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<Carrier> entities = carrierRepository.getBySearchTerm(searchTerm, sort);
        return carrierMapper.toDTOList(entities);
    }

    @Override
    public Page<CarrierDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if(pageable.getSort()==null) {
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<Carrier> entities = carrierRepository.getBySearchTerm(searchTerm,pageable);
        return carrierMapper.toDTOPage(pageable, entities);
    }

    private void checkCarrierName(String warehouse, String name) {
        Carrier boxType = carrierRepository.getByName(warehouse, name);
        if (boxType != null) {
            throw new ApiException(OutBoundException.EX_MD_OB_CARRIER_UNIQUE.toString(), name);
        }
    }

    @Override
    public List<CarrierDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        List<Carrier> entities = carrierRepository.getNotLockList(null, sort);
        return carrierMapper.toDTOList(entities);
    }
}
