package com.mushiny.wms.masterdata.mdbasics.service.impl;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.ItemUnitDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.ItemUnitMapper;
import com.mushiny.wms.masterdata.mdbasics.domain.ItemUnit;
import com.mushiny.wms.masterdata.mdbasics.exception.MasterDataException;
import com.mushiny.wms.masterdata.mdbasics.repository.ItemUnitRepository;
import com.mushiny.wms.masterdata.mdbasics.service.ItemUnitService;
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
public class ItemUnitServiceImpl implements ItemUnitService {

    private final ItemUnitRepository itemUnitRepository;
    private final ItemUnitMapper itemUnitMapper;

    @Autowired
    public ItemUnitServiceImpl(ItemUnitRepository itemUnitRepository,
                               ItemUnitMapper itemUnitMapper) {
        this.itemUnitRepository = itemUnitRepository;
        this.itemUnitMapper = itemUnitMapper;
    }

    @Override
    public ItemUnitDTO create(ItemUnitDTO dto) {
        ItemUnit entity = itemUnitMapper.toEntity(dto);
        checkItemUnitName(entity.getName());
        return itemUnitMapper.toDTO(itemUnitRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        ItemUnit entity = itemUnitRepository.retrieve(id);
        itemUnitRepository.delete(entity);
    }

    @Override
    public ItemUnitDTO update(ItemUnitDTO dto) {
        ItemUnit entity = itemUnitRepository.retrieve(dto.getId());
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkItemUnitName(dto.getName());
        }
        itemUnitMapper.updateEntityFromDTO(dto, entity);
        return itemUnitMapper.toDTO(itemUnitRepository.save(entity));
    }

    @Override
    public ItemUnitDTO retrieve(String id) {
        return itemUnitMapper.toDTO(itemUnitRepository.retrieve(id));
    }

    @Override
    public List<ItemUnitDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<ItemUnit> entities = itemUnitRepository.getBySearchTerm(searchTerm, sort);
        return itemUnitMapper.toDTOList(entities);
    }

    @Override
    public Page<ItemUnitDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if(pageable.getSort()==null){
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<ItemUnit> entities = itemUnitRepository.getBySearchTerm(searchTerm, pageable);
        return itemUnitMapper.toDTOPage(pageable, entities);
    }

    @Override
    public List<ItemUnitDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        List<ItemUnit> entities = itemUnitRepository.getList(null, sort);
        return itemUnitMapper.toDTOList(entities);
    }

    private void checkItemUnitName(String name) {
        ItemUnit itemUnit = itemUnitRepository.getByName(name);
        if (itemUnit != null) {
            throw new ApiException(MasterDataException.EX_MD_ITEM_UNIT_NAME_UNIQUE.toString(), name);
        }
    }
}
