package com.mushiny.wms.masterdata.mdbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.general.repository.ClientRepository;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.HardWareDTO;
import com.mushiny.wms.masterdata.mdbasics.domain.HardWare;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HardWareMapper implements BaseMapper<HardWareDTO, HardWare> {

    private final ApplicationContext applicationContext;
    private final ClientRepository clientRepository;

    @Autowired
    public HardWareMapper(ApplicationContext applicationContext,
                          ClientRepository clientRepository) {
        this.applicationContext = applicationContext;
        this.clientRepository = clientRepository;
    }

    @Override
    public HardWareDTO toDTO(HardWare entity) {
        if (entity == null) {
            return null;
        }
        HardWareDTO dto = new HardWareDTO(entity);
        dto.setName(entity.getName());
        dto.setIpAddress(entity.getIpAddress());
        dto.setPortNumber(entity.getPortNumber());
        dto.setHardwareType(entity.getHardWareType());
        dto.setWarehouseId(entity.getWarehouseId());
        return dto;
    }


    @Override
    public HardWare toEntity(HardWareDTO dto) {
        if (dto == null) {
            return null;
        }
        HardWare entity = new HardWare();
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setIpAddress(dto.getIpAddress());
        entity.setPortNumber(dto.getPortNumber());
        entity.setHardWareType(dto.getHardwareType());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        return entity;
    }

    @Override
    public void updateEntityFromDTO(HardWareDTO dto, HardWare entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setIpAddress(dto.getIpAddress());
        entity.setPortNumber(dto.getPortNumber());
        entity.setHardWareType(dto.getHardwareType());
    }
}

