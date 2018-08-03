package com.mushiny.wms.masterdata.mdbasics.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.BatterConfigDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.BatterConfigMapper;
import com.mushiny.wms.masterdata.mdbasics.domain.BatterConfig;
import com.mushiny.wms.masterdata.mdbasics.exception.MasterDataException;
import com.mushiny.wms.masterdata.mdbasics.repository.BatterConfigRepository;
import com.mushiny.wms.masterdata.mdbasics.service.BatterConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BatterConfigServiceImpl implements BatterConfigService {

    private final BatterConfigRepository batterConfigRepository;
    private final ApplicationContext applicationContext;
    private final BatterConfigMapper batterConfigMapper;

    @Autowired
    public BatterConfigServiceImpl(BatterConfigRepository batterConfigRepository,
                                   ApplicationContext applicationContext,
                                   BatterConfigMapper batterConfigMapper) {
        this.batterConfigRepository = batterConfigRepository;
        this.applicationContext = applicationContext;
        this.batterConfigMapper = batterConfigMapper;
    }

    @Override
    public BatterConfigDTO create(BatterConfigDTO dto) {
        BatterConfig entity = batterConfigMapper.toEntity(dto);
        checkAreaName(entity.getName());
        return batterConfigMapper.toDTO(batterConfigRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        BatterConfig entity = batterConfigRepository.retrieve(id);
        batterConfigRepository.delete(entity);
    }

    @Override
    public BatterConfigDTO update(BatterConfigDTO dto) {
        BatterConfig entity = batterConfigRepository.retrieve(dto.getId());
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkAreaName(dto.getName());
        }
        batterConfigMapper.updateEntityFromDTO(dto, entity);
        return batterConfigMapper.toDTO(batterConfigRepository.save(entity));
    }

    @Override
    public BatterConfigDTO retrieve(String id) {
        return batterConfigMapper.toDTO(batterConfigRepository.retrieve(id));
    }

    @Override
    public List<BatterConfigDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<BatterConfig> entities = batterConfigRepository.getBySearchTerm(searchTerm, sort);
        return batterConfigMapper.toDTOList(entities);
    }

    @Override
    public Page<BatterConfigDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        Page<BatterConfig> entities = batterConfigRepository.getBySearchTerm(searchTerm, pageable);
        return batterConfigMapper.toDTOPage(pageable, entities);
    }

    private void checkAreaName(String areaName) {
        BatterConfig area = batterConfigRepository.getByName(areaName);
        if (area != null) {
            throw new ApiException(MasterDataException.EX_MD_BATTERCONFIG_NAME_UNIQUE.toString(), areaName);
        }
    }
}