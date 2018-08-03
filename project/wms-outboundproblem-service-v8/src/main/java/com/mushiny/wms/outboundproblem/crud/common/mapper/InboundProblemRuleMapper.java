package com.mushiny.wms.outboundproblem.crud.common.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.outboundproblem.crud.common.dto.InboundProblemRuleDTO;
import com.mushiny.wms.outboundproblem.domain.common.InboundProblemRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class InboundProblemRuleMapper implements BaseMapper<InboundProblemRuleDTO, InboundProblemRule> {
    private final ApplicationContext applicationContext;

    @Autowired
    public InboundProblemRuleMapper(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public InboundProblemRuleDTO toDTO(InboundProblemRule entity) {
        if (entity == null) {
            return null;
        }
        InboundProblemRuleDTO dto = new InboundProblemRuleDTO(entity);
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setWarehouse(entity.getWarehouseId());
        return dto;
    }

    @Override
    public InboundProblemRule toEntity(InboundProblemRuleDTO dto) {
        if (dto == null) {
            return null;
        }
        InboundProblemRule entity = new InboundProblemRule();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        return entity;
    }

    @Override
    public void updateEntityFromDTO(InboundProblemRuleDTO dto, InboundProblemRule entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
    }
}
