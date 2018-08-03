package com.mushiny.wms.masterdata.ibbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceiveCategoryPositionDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.ReceiveCategoryPosition;
import com.mushiny.wms.masterdata.ibbasics.repository.ReceiveCategoryRuleRepository;
import com.mushiny.wms.masterdata.general.crud.mapper.ClientMapper;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import com.mushiny.wms.masterdata.general.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReceiveCategoryPositionMapper implements BaseMapper<ReceiveCategoryPositionDTO, ReceiveCategoryPosition> {

    private final ReceiveCategoryRuleRepository receivingCategoryRuleRepository;
    private final ApplicationContext applicationContext;
    private final ReceiveCategoryRuleMapper receivingCategoryRuleMapper;
    private final ReceiveCategoryMapper receivingCategoryMapper;
    private final ClientMapper clientMapper;
    private final WarehouseMapper warehouseMapper;
    private final ClientRepository clientRepository;

    @Autowired
    public ReceiveCategoryPositionMapper(ApplicationContext applicationContext,
                                         ClientMapper clientMapper,
                                         WarehouseMapper warehouseMapper,
                                         ReceiveCategoryRuleRepository receivingCategoryRuleRepository,
                                         ReceiveCategoryRuleMapper receivingCategoryRuleMapper,
                                         ReceiveCategoryMapper receivingCategoryMapper,
                                         ClientRepository clientRepository) {
        this.applicationContext = applicationContext;
        this.clientMapper = clientMapper;
        this.warehouseMapper = warehouseMapper;
        this.receivingCategoryRuleRepository = receivingCategoryRuleRepository;
        this.receivingCategoryRuleMapper = receivingCategoryRuleMapper;
        this.receivingCategoryMapper = receivingCategoryMapper;
        this.clientRepository = clientRepository;
    }

    @Override
    public ReceiveCategoryPositionDTO toDTO(ReceiveCategoryPosition entity) {
        if (entity == null) {
            return null;
        }
        ReceiveCategoryPositionDTO dto = new ReceiveCategoryPositionDTO(entity);

        dto.setPositionNo(entity.getPositionNo());
        dto.setOperator(entity.getOperator());
        dto.setCompKey(entity.getCompKey());

        dto.setClient(clientRepository.retrieve(entity.getClientId()).getName());
        dto.setClientId(entity.getClientId());
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setReceiveCategoryRule(receivingCategoryRuleMapper.toDTO(entity.getReceivingCategoryRule()));
        dto.setReceiveCategory(receivingCategoryMapper.toDTO(entity.getReceivingCategory()));

        return dto;
    }

    @Override
    public ReceiveCategoryPosition toEntity(ReceiveCategoryPositionDTO dto) {
        if (dto == null) {
            return null;
        }
        ReceiveCategoryPosition entity = new ReceiveCategoryPosition();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setPositionNo(dto.getPositionNo());
        entity.setOperator(dto.getOperator());
        entity.setCompKey(dto.getCompKey());

        applicationContext.isCurrentClient(dto.getClientId());
        entity.setClientId(dto.getClientId());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        entity.setReceivingCategoryRule(receivingCategoryRuleRepository.retrieve(dto.getRuleId()));

        return entity;
    }

    @Override
    public void updateEntityFromDTO(ReceiveCategoryPositionDTO dto, ReceiveCategoryPosition entity) {
    }
}
