package com.mushiny.wms.masterdata.mdbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.general.crud.mapper.ClientMapper;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.BayDTO;
import com.mushiny.wms.masterdata.mdbasics.domain.Bay;
import com.mushiny.wms.masterdata.mdbasics.repository.PodTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BayMapper implements BaseMapper<BayDTO, Bay> {

    private final PodTypeRepository podTypeRepository;
    private final ApplicationContext applicationContext;
    ;
    private final PodTypeMapper podTypeMapper;
    private final ClientMapper clientMapper;
    private final WarehouseMapper warehouseMapper;

    @Autowired
    public BayMapper(PodTypeRepository podTypeRepository, ApplicationContext applicationContext,
                     PodTypeMapper podTypeMapper, WarehouseMapper warehouseMapper,
                     ClientMapper clientMapper
                     ) {
        this.podTypeRepository = podTypeRepository;
        this.applicationContext = applicationContext;
        this.podTypeMapper = podTypeMapper;
        this.warehouseMapper = warehouseMapper;
        this.clientMapper = clientMapper;

    }

    @Override
    public BayDTO toDTO(Bay entity) {
        if (entity == null) {
            return null;
        }

        BayDTO dto = new BayDTO(entity);

        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setAisle(entity.getAisle());
        dto.setBayIndex(entity.getBayIndex());

        dto.setPodType(podTypeMapper.toDTO(entity.getPodType()));
        dto.setClient(entity.getClientId());
        dto.setWarehouse(entity.getWarehouseId());

        return dto;
    }

    @Override
    public Bay toEntity(BayDTO dto) {
        if (dto == null) {
            return null;
        }

        Bay entity = new Bay();

        entity.setId(entity.getId());
        entity.setAdditionalContent(entity.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setAisle(dto.getAisle());
        entity.setBayIndex(dto.getBayIndex());

        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        entity.setClientId(applicationContext.getCurrentClient());
        if (dto.getPodTypeId() != null) {
            entity.setPodType(podTypeRepository.retrieve(dto.getPodTypeId()));
        }

        return entity;
    }

    @Override
    public void updateEntityFromDTO(BayDTO dto, Bay entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setDescription(dto.getDescription());
    }
}
