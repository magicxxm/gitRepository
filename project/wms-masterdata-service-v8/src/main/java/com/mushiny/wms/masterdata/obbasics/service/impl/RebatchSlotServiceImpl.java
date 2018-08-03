package com.mushiny.wms.masterdata.obbasics.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.mdbasics.exception.MasterDataException;
import com.mushiny.wms.masterdata.obbasics.crud.dto.ReBinCellDTO;
import com.mushiny.wms.masterdata.obbasics.crud.dto.RebatchSlotDTO;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.RebatchSlotMapper;
import com.mushiny.wms.masterdata.obbasics.domain.ReBinCell;
import com.mushiny.wms.masterdata.obbasics.domain.RebatchSlot;
import com.mushiny.wms.masterdata.obbasics.exception.OutBoundException;
import com.mushiny.wms.masterdata.obbasics.repository.RebatchSlotRepository;
import com.mushiny.wms.masterdata.obbasics.service.RebatchSlotService;
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
public class RebatchSlotServiceImpl implements RebatchSlotService {

    private RebatchSlotRepository rebatchSlotRepository;
    private RebatchSlotMapper rebatchSlotMapper;
    private ApplicationContext applicationContext;

    @Autowired
    public RebatchSlotServiceImpl(RebatchSlotRepository rebatchSlotRepository,
                                  RebatchSlotMapper rebatchSlotMapper,
                                  ApplicationContext applicationContext) {
        this.rebatchSlotRepository = rebatchSlotRepository;
        this.rebatchSlotMapper = rebatchSlotMapper;
        this.applicationContext = applicationContext;
    }

    @Override
    public RebatchSlotDTO create(RebatchSlotDTO dto) {
        RebatchSlot rebatchSlot = rebatchSlotRepository.getByName(applicationContext.getCurrentWarehouse(), dto.getName());
        if (rebatchSlot != null) {
            throw new ApiException(OutBoundException.EX_MD_OB_REBATCH_SLOT_NAME_UNIQUE.toString(), dto.getName());
        }
        RebatchSlot entity = rebatchSlotMapper.toEntity(dto);
        return rebatchSlotMapper.toDTO(rebatchSlotRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        RebatchSlot entity = rebatchSlotRepository.retrieve(id);
        rebatchSlotRepository.delete(entity);
    }

    @Override
    public RebatchSlotDTO update(RebatchSlotDTO dto) {
        RebatchSlot rebatchSlot = rebatchSlotRepository.getByName(applicationContext.getCurrentWarehouse(), dto.getName());
        if (rebatchSlot != null) {
            throw new ApiException(OutBoundException.EX_MD_OB_REBATCH_SLOT_NAME_UNIQUE.toString(), dto.getName());
        }
        RebatchSlot entity = rebatchSlotRepository.retrieve(dto.getId());
        rebatchSlotMapper.updateEntityFromDTO(dto, entity);
        return rebatchSlotMapper.toDTO(rebatchSlotRepository.save(entity));
    }

    @Override
    public RebatchSlotDTO retrieve(String id) {
        return rebatchSlotMapper.toDTO(rebatchSlotRepository.retrieve(id));
    }

    @Override
    public List<RebatchSlotDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<RebatchSlot> entities = rebatchSlotRepository.getBySearchTerm(searchTerm, sort);
        return rebatchSlotMapper.toDTOList(entities);
    }

    @Override
    public Page<RebatchSlotDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if(pageable.getSort()==null){
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "orderIndex"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<RebatchSlot> entities = rebatchSlotRepository.getBySearchTerm(searchTerm, pageable);
        return rebatchSlotMapper.toDTOPage(pageable, entities);
    }
}