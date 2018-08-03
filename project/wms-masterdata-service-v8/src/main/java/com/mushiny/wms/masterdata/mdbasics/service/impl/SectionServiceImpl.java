package com.mushiny.wms.masterdata.mdbasics.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.SectionDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.SectionMapper;
import com.mushiny.wms.masterdata.mdbasics.domain.Map;
import com.mushiny.wms.masterdata.mdbasics.domain.Section;
import com.mushiny.wms.masterdata.mdbasics.repository.SectionRepository;
import com.mushiny.wms.masterdata.mdbasics.service.SectionService;
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
public class SectionServiceImpl implements SectionService {

    private final SectionRepository sectionRepository;
    private final ApplicationContext applicationContext;
    private final SectionMapper sectionMapper;

    @Autowired
    public SectionServiceImpl(SectionRepository sectionRepository,
                              ApplicationContext applicationContext,
                              SectionMapper sectionMapper) {
        this.sectionRepository = sectionRepository;
        this.applicationContext = applicationContext;
        this.sectionMapper = sectionMapper;
    }

    @Override
    public SectionDTO create(SectionDTO dto) {
        Section entity = sectionMapper.toEntity(dto);
        checkMapName(dto.getName(),entity.getWarehouseId());
        return sectionMapper.toDTO(sectionRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        Section entity = sectionRepository.retrieve(id);
        sectionRepository.delete(entity);
    }

    @Override
    public SectionDTO update(SectionDTO dto) {
        Section entity = sectionRepository.retrieve(dto.getId());
        if(!entity.getName().equalsIgnoreCase(dto.getName())){
            checkMapName(dto.getName(),entity.getWarehouseId());
        }
        sectionMapper.updateEntityFromDTO(dto, entity);
        return sectionMapper.toDTO(sectionRepository.save(entity));
    }

    @Override
    public SectionDTO retrieve(String id) {
        return sectionMapper.toDTO(sectionRepository.retrieve(id));
    }

    @Override
    public List<SectionDTO> getAll() {
        String warehouse = applicationContext.getCurrentWarehouse();
        List<Section> entities = sectionRepository.getByWarehouse(warehouse);
        return sectionMapper.toDTOList(entities);
    }

    @Override
    public List<SectionDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<Section> entities = sectionRepository.getBySearchTerm(searchTerm, sort);
        return sectionMapper.toDTOList(entities);
    }

    @Override
    public Page<SectionDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if(pageable.getSort()==null){
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<Section> entities = sectionRepository.getBySearchTerm(searchTerm, pageable);
        return sectionMapper.toDTOPage(pageable, entities);
    }
    private void checkMapName(String name, String warehouse) {
        Section section = sectionRepository.getByName(name,warehouse);
        if (section != null) {
            throw new ApiException("同一仓库中区域名不能重复！");
        }
    }
}