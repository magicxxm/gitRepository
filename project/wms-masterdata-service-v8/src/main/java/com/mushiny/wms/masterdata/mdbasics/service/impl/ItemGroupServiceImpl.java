package com.mushiny.wms.masterdata.mdbasics.service.impl;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.ItemGroupDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.ItemGroupMapper;
import com.mushiny.wms.masterdata.mdbasics.domain.ItemGroup;
import com.mushiny.wms.masterdata.mdbasics.exception.MasterDataException;
import com.mushiny.wms.masterdata.mdbasics.repository.ItemGroupRepository;
import com.mushiny.wms.masterdata.mdbasics.service.ItemGroupService;
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
public class ItemGroupServiceImpl implements ItemGroupService {

    private final ItemGroupRepository itemGroupRepository;
    private final ItemGroupMapper itemGroupMapper;

    @Autowired
    public ItemGroupServiceImpl(ItemGroupRepository itemGroupRepository,
                                ItemGroupMapper itemGroupMapper) {
        this.itemGroupRepository = itemGroupRepository;
        this.itemGroupMapper = itemGroupMapper;
    }

    @Override
    public ItemGroupDTO create(ItemGroupDTO dto) {
        ItemGroup entity = itemGroupMapper.toEntity(dto);
        checkItemGroupName(entity.getName());
        return itemGroupMapper.toDTO(itemGroupRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        ItemGroup entity = itemGroupRepository.retrieve(id);
        itemGroupRepository.delete(entity);
    }

    @Override
    public ItemGroupDTO update(ItemGroupDTO dto) {
        ItemGroup entity = itemGroupRepository.retrieve(dto.getId());
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkItemGroupName(dto.getName());
        }
        itemGroupMapper.updateEntityFromDTO(dto, entity);
        return itemGroupMapper.toDTO(itemGroupRepository.save(entity));
    }

    @Override
    public ItemGroupDTO retrieve(String id) {
        return itemGroupMapper.toDTO(itemGroupRepository.retrieve(id));
    }

    @Override
    public List<ItemGroupDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<ItemGroup> entities = itemGroupRepository.getBySearchTerm(searchTerm, sort);
        return itemGroupMapper.toDTOList(entities);
    }

    @Override
    public Page<ItemGroupDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if(pageable.getSort()==null){
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "orderIndex"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<ItemGroup> entities = itemGroupRepository.getBySearchTerm(searchTerm, pageable);
        return itemGroupMapper.toDTOPage(pageable, entities);
    }

    @Override
    public List<ItemGroupDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "orderIndex"));
        List<ItemGroup> entities = itemGroupRepository.getList(null, sort);
        return itemGroupMapper.toDTOList(entities);
    }

    private void checkItemGroupName(String name) {
        ItemGroup itemGroup = itemGroupRepository.getByName(name);
        if (itemGroup != null) {
            throw new ApiException(MasterDataException.EX_MD_ITEM_GROUP_NAME_UNIQUE.toString(), name);
        }
    }
}
