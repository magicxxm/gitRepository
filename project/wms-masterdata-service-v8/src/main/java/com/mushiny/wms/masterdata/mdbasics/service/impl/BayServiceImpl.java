package com.mushiny.wms.masterdata.mdbasics.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.mdbasics.business.BayBusiness;
import com.mushiny.wms.masterdata.mdbasics.business.dto.BayStorageLocationsDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.BayDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.BayMapper;
import com.mushiny.wms.masterdata.mdbasics.domain.Bay;
import com.mushiny.wms.masterdata.mdbasics.domain.StorageLocation;
import com.mushiny.wms.masterdata.mdbasics.repository.BayRepository;
import com.mushiny.wms.masterdata.mdbasics.repository.StorageLocationRepository;
import com.mushiny.wms.masterdata.mdbasics.service.BayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BayServiceImpl implements BayService {

    private final BayRepository bayRepository;
    private final ApplicationContext applicationContext;
    private final StorageLocationRepository storageLocationRepository;
    private final BayBusiness bayBusiness;
    private final BayMapper bayMapper;

    @Autowired
    public BayServiceImpl(BayRepository bayRepository,
                          ApplicationContext applicationContext, BayMapper bayMapper,
                          StorageLocationRepository storageLocationRepository,
                          BayBusiness bayBusiness) {
        this.bayRepository = bayRepository;
        this.applicationContext = applicationContext;
        this.bayMapper = bayMapper;
        this.storageLocationRepository = storageLocationRepository;
        this.bayBusiness = bayBusiness;
    }

    @Override
    public void createMore(BayStorageLocationsDTO dto) {
        bayBusiness.createMore(dto);
    }

    @Override
    public BayDTO create(BayDTO dto) {
        Bay entity = bayMapper.toEntity(dto);
        checkBayName(entity.getWarehouseId(), entity.getName());
        return bayMapper.toDTO(bayRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        Bay entity = bayRepository.retrieve(id);
        int maxBinIndex = storageLocationRepository.getBayMaxIndex(entity);
        int minBinIndex = storageLocationRepository.getBayMinIndex(entity);
        bayRepository.updateDeleteBayIndex(entity.getBayIndex(), entity.getWarehouseId());
        int subIndex = maxBinIndex - minBinIndex + 1;
        storageLocationRepository.updateDeleteBinOrderIndex(subIndex, maxBinIndex, entity.getWarehouseId());
        // 删除
        List<StorageLocation> bins = storageLocationRepository.getByBay(entity);
        if (!bins.isEmpty()) {
            storageLocationRepository.delete(bins);
        }
        bayRepository.delete(entity);
    }

    @Override
    public BayDTO update(BayDTO dto) {
        Bay entity = bayRepository.retrieve(dto.getId());
        bayMapper.updateEntityFromDTO(dto, entity);
        return bayMapper.toDTO(bayRepository.save(entity));
    }

    @Override
    public BayDTO retrieve(String id) {
        return bayMapper.toDTO(bayRepository.retrieve(id));
    }

    @Override
    public List<BayDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<Bay> entities = bayRepository.getBySearchTerm(searchTerm, sort);
        return bayMapper.toDTOList(entities);
    }

    @Override
    public Page<BayDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        Page<Bay> entities = bayRepository.getBySearchTerm(searchTerm, pageable);
        return bayMapper.toDTOPage(pageable, entities);
    }

    @Override
    public List<BayDTO> getByClientId(String clientId) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "bayIndex"));
        List<Bay> entities = bayRepository.getList(applicationContext.getCurrentClient(), sort);
        return bayMapper.toDTOList(entities);
    }

    private void checkBayName(String warehouse, String bayName) {
        Bay bay = bayRepository.getByName(warehouse, bayName);
        if (bay != null) {
            throw new ApiException("同样名称的bay已经创建");
        }
    }
}
