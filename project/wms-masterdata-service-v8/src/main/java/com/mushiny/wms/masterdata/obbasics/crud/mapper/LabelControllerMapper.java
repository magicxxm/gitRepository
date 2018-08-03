package com.mushiny.wms.masterdata.obbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.general.repository.ClientRepository;
import com.mushiny.wms.masterdata.obbasics.crud.dto.LabelControllerDTO;
import com.mushiny.wms.masterdata.obbasics.domain.LabelController;
import org.springframework.stereotype.Component;

@Component
public class LabelControllerMapper implements BaseMapper<LabelControllerDTO, LabelController> {

    private final ApplicationContext applicationContext;
    private final ClientRepository clientRepository;

    public LabelControllerMapper(ApplicationContext applicationContext,
                                 ClientRepository clientRepository) {
        this.applicationContext = applicationContext;
        this.clientRepository = clientRepository;
    }

    @Override
    public LabelControllerDTO toDTO(LabelController entity) {

        if (entity == null) {
            return null;
        }
        LabelControllerDTO dto = new LabelControllerDTO(entity);
        dto.setName(entity.getName());
        dto.setAddressIp(entity.getAddressIp());
        dto.setPortNumber(entity.getPortNumber());
        dto.setWarehouseId(entity.getWarehouseId());

        return dto;
    }

    @Override
    public LabelController toEntity(LabelControllerDTO dto) {
        if (dto == null) {
            return null;
        }

        LabelController entity = new LabelController();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setAddressIp(dto.getAddressIp());
        entity.setPortNumber(dto.getPortNumber());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());

        return entity;
    }

    @Override
    public void updateEntityFromDTO(LabelControllerDTO dto, LabelController entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }

        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setAddressIp(dto.getAddressIp());
        entity.setPortNumber(dto.getPortNumber());

    }
}
