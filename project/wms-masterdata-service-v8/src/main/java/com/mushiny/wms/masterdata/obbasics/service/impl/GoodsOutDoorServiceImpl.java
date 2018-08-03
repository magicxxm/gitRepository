package com.mushiny.wms.masterdata.obbasics.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.obbasics.crud.dto.GoodsOutDoorDTO;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.GoodsOutDoorMapper;
import com.mushiny.wms.masterdata.obbasics.domain.GoodsOutDoor;
import com.mushiny.wms.masterdata.obbasics.exception.OutBoundException;
import com.mushiny.wms.masterdata.obbasics.repository.GoodsOutDoorRepository;
import com.mushiny.wms.masterdata.obbasics.service.GoodsOutDoorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GoodsOutDoorServiceImpl implements GoodsOutDoorService {

    private final GoodsOutDoorRepository goodsOutDoorRepository;
    private final ApplicationContext applicationContext;
    private final GoodsOutDoorMapper goodsOutDoorMapper;

    public GoodsOutDoorServiceImpl(GoodsOutDoorRepository goodsOutDoorRepository,
                                   ApplicationContext applicationContext,
                                   GoodsOutDoorMapper goodsOutDoorMapper) {
        this.goodsOutDoorRepository = goodsOutDoorRepository;
        this.applicationContext = applicationContext;
        this.goodsOutDoorMapper = goodsOutDoorMapper;
    }

    @Override
    public GoodsOutDoorDTO create(GoodsOutDoorDTO dto) {
        GoodsOutDoor entity = goodsOutDoorMapper.toEntity(dto);
        checkGoodsOutDoorName(entity.getWarehouseId(), entity.getName());
        return goodsOutDoorMapper.toDTO(goodsOutDoorRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        GoodsOutDoor entity = goodsOutDoorRepository.retrieve(id);
        goodsOutDoorRepository.delete(entity);
    }

    @Override
    public GoodsOutDoorDTO update(GoodsOutDoorDTO dto) {
        GoodsOutDoor entity = goodsOutDoorRepository.retrieve(dto.getId());
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkGoodsOutDoorName(entity.getWarehouseId(), dto.getName());
        }
        goodsOutDoorMapper.updateEntityFromDTO(dto, entity);
        return goodsOutDoorMapper.toDTO(goodsOutDoorRepository.save(entity));
    }


    @Override
    public GoodsOutDoorDTO retrieve(String id) {
        return goodsOutDoorMapper.toDTO(goodsOutDoorRepository.retrieve(id));
    }

    @Override
    public List<GoodsOutDoorDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        List<GoodsOutDoor> entities = goodsOutDoorRepository.getList(null, sort);
        return goodsOutDoorMapper.toDTOList(entities);
    }

    @Override
    public List<GoodsOutDoorDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<GoodsOutDoor> entities = goodsOutDoorRepository.getBySearchTerm(searchTerm, sort);
        return goodsOutDoorMapper.toDTOList(entities);
    }

    @Override
    public Page<GoodsOutDoorDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if(pageable.getSort()==null){
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<GoodsOutDoor> entities = goodsOutDoorRepository.getBySearchTerm(searchTerm,pageable);
        return goodsOutDoorMapper.toDTOPage(pageable, entities);
    }

    private void checkGoodsOutDoorName(String warehouse, String name) {
        GoodsOutDoor goodsOutDoor = goodsOutDoorRepository.getByName(warehouse, name);
        if (goodsOutDoor != null) {
            throw new ApiException(OutBoundException.EX_MD_OB_DIGITAL_LABEL_NAME_UNIQUE.toString(), name);
        }
    }
}
