package com.mushiny.wms.masterdata.mdbasics.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.ItemDataSerialNumberDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.ItemDataSerialNumberMapper;
import com.mushiny.wms.masterdata.mdbasics.domain.ItemDataSerialNumber;
import com.mushiny.wms.masterdata.mdbasics.exception.MasterDataException;
import com.mushiny.wms.masterdata.mdbasics.repository.ItemDataSerialNumberRepository;
import com.mushiny.wms.masterdata.mdbasics.service.ItemDataSerialNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ItemDataSerialNumberServiceImpl implements ItemDataSerialNumberService {

    private final ItemDataSerialNumberRepository itemDataSerialNumberRepository;
    private final ApplicationContext applicationContext;
    private final ItemDataSerialNumberMapper itemDataSerialNumberMapper;

    @Autowired
    public ItemDataSerialNumberServiceImpl(ItemDataSerialNumberRepository itemDataSerialNumberRepository,
                                           ApplicationContext applicationContext,
                                           ItemDataSerialNumberMapper itemDataSerialNumberMapper) {
        this.itemDataSerialNumberRepository = itemDataSerialNumberRepository;
        this.applicationContext = applicationContext;
        this.itemDataSerialNumberMapper = itemDataSerialNumberMapper;
    }

    @Override
    public ItemDataSerialNumberDTO create(ItemDataSerialNumberDTO dto) {
        ItemDataSerialNumber entity = itemDataSerialNumberMapper.toEntity(dto);
        checkItemNo(entity.getSerialNo());
        return itemDataSerialNumberMapper.toDTO(itemDataSerialNumberRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        ItemDataSerialNumber entity = itemDataSerialNumberRepository.retrieve(id);
        itemDataSerialNumberRepository.delete(entity);
    }

    @Override
    public ItemDataSerialNumberDTO update(ItemDataSerialNumberDTO dto) {
        ItemDataSerialNumber entity = itemDataSerialNumberRepository.retrieve(dto.getId());
        itemDataSerialNumberMapper.updateEntityFromDTO(dto, entity);
        return itemDataSerialNumberMapper.toDTO(itemDataSerialNumberRepository.save(entity));
    }

    @Override
    public ItemDataSerialNumberDTO retrieve(String id) {
        return itemDataSerialNumberMapper.toDTO(itemDataSerialNumberRepository.retrieve(id));
    }

    @Override
    public List<ItemDataSerialNumberDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<ItemDataSerialNumber> entities = itemDataSerialNumberRepository.getBySearchTerm(searchTerm, sort);
        return itemDataSerialNumberMapper.toDTOList(entities);
    }

    @Override
    public Page<ItemDataSerialNumberDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        Page<ItemDataSerialNumber> entities = itemDataSerialNumberRepository.getBySearchTerm(searchTerm, pageable);
        return itemDataSerialNumberMapper.toDTOPage(pageable, entities);
    }

    @Override
    public List<ItemDataSerialNumberDTO> getByClientId(String clientId) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "serialNo"));
        applicationContext.isCurrentClient(clientId);
        List<ItemDataSerialNumber> entities = itemDataSerialNumberRepository.getList(clientId, sort);
        return itemDataSerialNumberMapper.toDTOList(entities);
    }

    private void checkItemNo(String serialNo) {
        ItemDataSerialNumber itemData = itemDataSerialNumberRepository.getBySerialNo(serialNo);
        if (itemData != null) {
            throw new ApiException(MasterDataException.EX_MD_ITEM_DATA_NO_UNIQUE.toString(), serialNo);
        }
    }
}
