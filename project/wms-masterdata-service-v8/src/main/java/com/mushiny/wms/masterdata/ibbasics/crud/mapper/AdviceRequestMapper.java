package com.mushiny.wms.masterdata.ibbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.AdviceRequestDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.AdviceRequest;
import com.mushiny.wms.masterdata.general.crud.mapper.ClientMapper;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import com.mushiny.wms.masterdata.general.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdviceRequestMapper implements BaseMapper<AdviceRequestDTO, AdviceRequest> {

    private final ApplicationContext applicationContext;
    private final ClientMapper clientMapper;
    private final WarehouseMapper warehouseMapper;
    private final ClientRepository clientRepository;

    @Autowired
    public AdviceRequestMapper(ApplicationContext applicationContext,
                               ClientMapper clientMapper,
                               WarehouseMapper warehouseMapper,
                               ClientRepository clientRepository) {
        this.applicationContext = applicationContext;
        this.clientMapper = clientMapper;
        this.warehouseMapper = warehouseMapper;
        this.clientRepository = clientRepository;
    }

    @Override
    public AdviceRequestDTO toDTO(AdviceRequest entity) {
        if (entity == null) {
            return null;
        }

        AdviceRequestDTO dto = new AdviceRequestDTO(entity);
        dto.setSize(entity.getSize());
        dto.setAdviceNo(entity.getAdviceNo());
        dto.setAdviceState(entity.getAdviceState());
        dto.setExpectedDelivery(entity.getExpectedDelivery());
        dto.setExpireBatch(entity.isExpireBatch());
        dto.setExternalNo(entity.getExternalNo());
        dto.setExternalId(entity.getExternalId());
        dto.setFinishDate(entity.getFinishDate());
        dto.setProcessDate(entity.getProcessDate());
        dto.setClient(clientRepository.retrieve(entity.getClientId()).getName());
        dto.setClientId(entity.getClientId());
        dto.setWarehouseId(entity.getWarehouseId());

        return dto;
    }

    @Override
    public AdviceRequest toEntity(AdviceRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        AdviceRequest entity = new AdviceRequest();

        entity.setId(dto.getId());
        entity.setSize(dto.getSize());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setAdviceNo(dto.getAdviceNo());
        entity.setAdviceState(dto.getAdviceState());
        entity.setExpectedDelivery(dto.getExpectedDelivery());
        entity.setExpireBatch(dto.isExpireBatch());
        entity.setExternalNo(dto.getExternalNo());
        entity.setExternalId(dto.getExternalId());
        entity.setFinishDate(dto.getFinishDate());
        entity.setProcessDate(dto.getProcessDate());

        applicationContext.isCurrentClient(dto.getClientId());
        entity.setClientId(dto.getClientId());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());

        return entity;
    }

    @Override
    public void updateEntityFromDTO(AdviceRequestDTO dto, AdviceRequest entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setProcessDate(dto.getProcessDate());
    }
}
