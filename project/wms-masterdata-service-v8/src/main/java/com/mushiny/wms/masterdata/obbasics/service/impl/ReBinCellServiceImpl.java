package com.mushiny.wms.masterdata.obbasics.service.impl;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.obbasics.crud.dto.ReBinCellDTO;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.ReBinCellMapper;
import com.mushiny.wms.masterdata.obbasics.domain.ReBinCell;
import com.mushiny.wms.masterdata.obbasics.exception.OutBoundException;
import com.mushiny.wms.masterdata.obbasics.repository.ReBinCellRepository;
import com.mushiny.wms.masterdata.obbasics.service.ReBinCellService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReBinCellServiceImpl implements ReBinCellService {

    private final ReBinCellRepository reBinCellRepository;
    private final ReBinCellMapper reBinCellMapper;


    public ReBinCellServiceImpl(ReBinCellRepository reBinCellRepository,
                                ReBinCellMapper reBinCellMapper) {
        this.reBinCellRepository = reBinCellRepository;
        this.reBinCellMapper = reBinCellMapper;
    }

    @Override
    public ReBinCellDTO create(ReBinCellDTO dto) {
        ReBinCell entity = reBinCellMapper.toEntity(dto);
        checkReBinCellTypeName(entity.getWarehouseId(), entity.getName());
        return reBinCellMapper.toDTO(reBinCellRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        ReBinCell entity = reBinCellRepository.retrieve(id);
        reBinCellRepository.delete(entity);
    }

    @Override
    public ReBinCellDTO update(ReBinCellDTO dto) {
        ReBinCell entity = reBinCellRepository.retrieve(dto.getId());
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkReBinCellTypeName(entity.getWarehouseId(), dto.getName());
        }
        reBinCellMapper.updateEntityFromDTO(dto, entity);
        return reBinCellMapper.toDTO(reBinCellRepository.save(entity));
    }

    @Override
    public ReBinCellDTO retrieve(String id) {
        return reBinCellMapper.toDTO(reBinCellRepository.retrieve(id));
    }

    @Override
    public List<ReBinCellDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<ReBinCell> entities = reBinCellRepository.getBySearchTerm(searchTerm, sort);
        return reBinCellMapper.toDTOList(entities);
    }

    @Override
    public Page<ReBinCellDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if(pageable.getSort()==null){
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "orderIndex"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<ReBinCell> entities = reBinCellRepository.getBySearchTerm(searchTerm, pageable);
        return reBinCellMapper.toDTOPage(pageable, entities);
    }

    private void checkReBinCellTypeName(String warehouse, String name) {
        ReBinCell reBinCellType = reBinCellRepository.getByName(warehouse, name);
        if (reBinCellType != null) {
            throw new ApiException(OutBoundException.EX_MD_OB_REBIN_CELL_NAME_UNIQUE.toString(), name);
        }
    }
}