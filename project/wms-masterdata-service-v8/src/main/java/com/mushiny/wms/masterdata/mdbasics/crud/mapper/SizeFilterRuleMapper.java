package com.mushiny.wms.masterdata.mdbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.SizeFilterRuleDTO;
import com.mushiny.wms.masterdata.mdbasics.domain.SizeFilterRule;
import com.mushiny.wms.masterdata.obbasics.crud.dto.DeliveryTimeDTO;
import com.mushiny.wms.masterdata.obbasics.domain.DeliveryTime;
import org.springframework.stereotype.Component;

@Component
public class SizeFilterRuleMapper implements BaseMapper<SizeFilterRuleDTO, SizeFilterRule> {
    private final ApplicationContext applicationContext;

    public SizeFilterRuleMapper(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public SizeFilterRuleDTO toDTO(SizeFilterRule entity) {
        if (entity == null) {
            return null;
        }
        SizeFilterRuleDTO dto = new SizeFilterRuleDTO(entity);
        dto.setName(entity.getName());
        dto.setMode(entity.getMode());
        dto.setPrice(entity.getPrice());
        dto.setRule(entity.getRule());
        dto.setNumber(entity.getNumber());
        return dto;
    }

    @Override
    public SizeFilterRule toEntity(SizeFilterRuleDTO dto) {
        if (dto == null) {
            return null;
        }
        SizeFilterRule entity = new SizeFilterRule();
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setRule(dto.getRule());
        entity.setPrice(dto.getPrice());
        entity.setMode(dto.getMode());
        entity.setNumber(dto.getName().equals("小") ? "1" : dto.getName().equals("中") ? "2" : dto.getName().equals("大") ? "3" : "4");
        return entity;
    }

    @Override
    public void updateEntityFromDTO(SizeFilterRuleDTO dto, SizeFilterRule entity) {

        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setMode(dto.getMode());
        entity.setPrice(dto.getPrice());
        entity.setRule(dto.getRule());
        entity.setNumber(dto.getName().equals("小") ? "1" : dto.getName().equals("中") ? "2" : dto.getName().equals("大") ? "3" : "4");
    }
}