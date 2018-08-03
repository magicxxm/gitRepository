package com.mushiny.wms.masterdata.mdbasics.service.impl;

import com.mushiny.wms.masterdata.mdbasics.crud.dto.ItemGroupDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.ItemGroupMapper;
import com.mushiny.wms.masterdata.mdbasics.domain.ItemGroup;
import com.mushiny.wms.masterdata.mdbasics.domain.Zone;
import com.mushiny.wms.masterdata.mdbasics.repository.ItemGroupRepository;
import com.mushiny.wms.masterdata.mdbasics.repository.ZoneRepository;
import com.mushiny.wms.masterdata.mdbasics.service.ZoneItemGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class ZoneItemGroupServiceImpl implements ZoneItemGroupService {

    private final ZoneRepository zoneRepository;
    private final ItemGroupRepository itemGroupRepository;
    private final ItemGroupMapper itemGroupMapper;

    @Autowired
    public ZoneItemGroupServiceImpl(ZoneRepository zoneRepository,
                                    ItemGroupRepository itemGroupRepository,
                                    ItemGroupMapper itemGroupMapper) {
        this.zoneRepository = zoneRepository;
        this.itemGroupRepository = itemGroupRepository;
        this.itemGroupMapper = itemGroupMapper;
    }

    @Override
    public void createZoneItemGroups(String zoneId, List<String> itemGroupIds) {
        Zone zone = zoneRepository.retrieve(zoneId);
        Set<ItemGroup> itemGroups = new HashSet<>();
        if (itemGroupIds != null && !itemGroupIds.isEmpty()) {
            for (String itemGroupId : itemGroupIds) {
                ItemGroup itemGroup = itemGroupRepository.retrieve(itemGroupId);
                itemGroups.add(itemGroup);
            }
            zone.setItemGroups(itemGroups);
        } else {
            zone.setItemGroups(null);
        }
        zoneRepository.save(zone);
    }

    @Override
    public List<ItemGroupDTO> getAssignedItemGroupByZoneId(String zoneId) {
        Zone zone = zoneRepository.retrieve(zoneId);
        List<ItemGroup> entities = new ArrayList<>();
        entities.addAll(zone.getItemGroups());
        return itemGroupMapper.toDTOList(entities);
    }

    @Override
    public List<ItemGroupDTO> getUnassignedItemGroupByZoneId(String zoneId) {
        Zone zone = zoneRepository.retrieve(zoneId);
        List<ItemGroup> entities = itemGroupRepository.getUnassignedZoneItemGroups(zone.getId());
        return itemGroupMapper.toDTOList(entities);
    }
}
