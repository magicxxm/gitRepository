package com.mushiny.wms.masterdata.obbasics.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.obbasics.crud.dto.DigitalLabelDTO;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.DigitalLabelMapper;
import com.mushiny.wms.masterdata.obbasics.domain.DigitalLabel;
import com.mushiny.wms.masterdata.obbasics.domain.PickPackCell;
import com.mushiny.wms.masterdata.obbasics.exception.OutBoundException;
import com.mushiny.wms.masterdata.obbasics.repository.DigitalLabelRepository;
import com.mushiny.wms.masterdata.obbasics.repository.PickPackCellRepository;
import com.mushiny.wms.masterdata.obbasics.service.DigitalLabelService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DigitalLabelServiceImpl implements DigitalLabelService {

    private final DigitalLabelRepository digitalLabelRepository;
    private final ApplicationContext applicationContext;
    private final DigitalLabelMapper digitalLabelMapper;

    public DigitalLabelServiceImpl(DigitalLabelRepository digitalLabelRepository,
                                   ApplicationContext applicationContext,
                                   DigitalLabelMapper digitalLabelMapper) {
        this.digitalLabelRepository = digitalLabelRepository;
        this.applicationContext = applicationContext;
        this.digitalLabelMapper = digitalLabelMapper;
    }

    @Override
    public DigitalLabelDTO create(DigitalLabelDTO dto) {
        DigitalLabel entity = digitalLabelMapper.toEntity(dto);
        checkDigitalLabelName(entity.getWarehouseId(), entity.getName());
        return digitalLabelMapper.toDTO(digitalLabelRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        DigitalLabel entity = digitalLabelRepository.retrieve(id);
        digitalLabelRepository.delete(entity);
    }

    @Override
    public DigitalLabelDTO update(DigitalLabelDTO dto) {
        DigitalLabel entity = digitalLabelRepository.retrieve(dto.getId());
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkDigitalLabelName(entity.getWarehouseId(), dto.getName());
        }
        digitalLabelMapper.updateEntityFromDTO(dto, entity);
        return digitalLabelMapper.toDTO(digitalLabelRepository.save(entity));
    }


    @Override
    public DigitalLabelDTO retrieve(String id) {
        return digitalLabelMapper.toDTO(digitalLabelRepository.retrieve(id));
    }

    @Override
    public List<DigitalLabelDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        List<DigitalLabel> entities = digitalLabelRepository.getList(null, sort);
        return digitalLabelMapper.toDTOList(entities);
    }

    @Override
    public List<DigitalLabelDTO> getByLabel(List<String> ids) {
        List<DigitalLabel> entity = digitalLabelRepository.getByLabel(ids);
        return digitalLabelMapper.toDTOList(entity);
    }

    @Override
    public List<DigitalLabelDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<DigitalLabel> entities = digitalLabelRepository.getBySearchTerm(searchTerm, sort);
        return digitalLabelMapper.toDTOList(entities);
    }

    @Override
    public Page<DigitalLabelDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if(pageable.getSort()==null){
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<DigitalLabel> entities = digitalLabelRepository.getBySearchTerm(searchTerm, pageable);
        return digitalLabelMapper.toDTOPage(pageable, entities);
    }

    private void checkDigitalLabelName(String warehouse, String name) {
        DigitalLabel digitalLabel = digitalLabelRepository.getByName(warehouse, name);
        if (digitalLabel != null) {
            throw new ApiException(OutBoundException.EX_MD_OB_DIGITAL_LABEL_NAME_UNIQUE.toString(), name);
        }
    }
}
