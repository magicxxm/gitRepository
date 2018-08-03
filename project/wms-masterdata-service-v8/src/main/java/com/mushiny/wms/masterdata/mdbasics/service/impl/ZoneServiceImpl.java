package com.mushiny.wms.masterdata.mdbasics.service.impl;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.ZoneDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.ZoneMapper;
import com.mushiny.wms.masterdata.mdbasics.domain.Zone;
import com.mushiny.wms.masterdata.mdbasics.exception.MasterDataException;
import com.mushiny.wms.masterdata.mdbasics.repository.ZoneRepository;
import com.mushiny.wms.masterdata.mdbasics.service.ZoneService;
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
public class ZoneServiceImpl implements ZoneService {

    private final ZoneRepository zoneRepository;
    private final ApplicationContext applicationContext;
    private final ZoneMapper zoneMapper;

    @Autowired
    public ZoneServiceImpl(ApplicationContext applicationContext,
                           ZoneRepository zoneRepository,
                           ZoneMapper zoneMapper) {
        this.applicationContext = applicationContext;
        this.zoneRepository = zoneRepository;
        this.zoneMapper = zoneMapper;
    }

    @Override
    public ZoneDTO create(ZoneDTO dto) {
        Zone entity = zoneMapper.toEntity(dto);
        checkZoneName(entity.getWarehouseId(), entity.getClientId(), entity.getName());
        return zoneMapper.toDTO(zoneRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        Zone entity = zoneRepository.retrieve(id);
        zoneRepository.delete(entity);
    }

    @Override
    public ZoneDTO update(ZoneDTO dto) {
        Zone entity = zoneRepository.retrieve(dto.getId());
        if (!(entity.getName().equalsIgnoreCase(dto.getName())
                && entity.getClientId().equalsIgnoreCase(dto.getClientId()))) {
            String client = dto.getClientId();
            checkZoneName(entity.getWarehouseId(), client, dto.getName());
        }
        zoneMapper.updateEntityFromDTO(dto, entity);
        return zoneMapper.toDTO(zoneRepository.save(entity));
    }

    @Override
    public ZoneDTO retrieve(String id) {
        return zoneMapper.toDTO(zoneRepository.retrieve(id));
    }

    @Override
    public List<ZoneDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<Zone> entities = zoneRepository.getBySearchTerm(searchTerm, sort);
        return zoneMapper.toDTOList(entities);
    }

    @Override
    public Page<ZoneDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if (pageable.getSort() == null) {
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<Zone> entities = zoneRepository.getBySearchTerm(searchTerm, pageable);
        return zoneMapper.toDTOPage(pageable, entities);
    }

    @Override
    public List<ZoneDTO> getByClientId(String clientId) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        applicationContext.isCurrentClient(clientId);
        List<Zone> entities = zoneRepository.getList(clientId, sort);
        return zoneMapper.toDTOList(entities);
    }

    @Override
    public List<ZoneDTO> getByClientIdAndSectionId(String clientId, String sectionId) {
        applicationContext.isCurrentClient(clientId);
        List<Zone> entities = zoneRepository.getByClientIdAndSectionId(applicationContext.getCurrentWarehouse(), clientId, sectionId);
        return zoneMapper.toDTOList(entities);
    }

    private void checkZoneName(String warehouse, String client, String name) {
        Zone zone = zoneRepository.getByName(warehouse, client, name);
        if (zone != null) {
            throw new ApiException(MasterDataException.EX_MD_ZONE_NAME_UNIQUE.toString(), name);
        }
    }
}
