package com.mushiny.wms.masterdata.mdbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.general.repository.ClientRepository;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.AreaDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.SabcRuleDTO;
import com.mushiny.wms.masterdata.mdbasics.domain.Area;
import com.mushiny.wms.masterdata.mdbasics.domain.SabcRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SabcRuleMapper implements BaseMapper<SabcRuleDTO, SabcRule> {

    private final ApplicationContext applicationContext;

    @Autowired
    public SabcRuleMapper(ApplicationContext applicationContext,
                          ClientRepository clientRepository) {
        this.applicationContext = applicationContext;
    }

    @Override
    public SabcRuleDTO toDTO(SabcRule entity) {
        if (entity == null) {
            return null;
        }
        SabcRuleDTO dto = new SabcRuleDTO(entity);
        dto.setSkuTypeName(entity.getSkuTypeName());
        dto.setFromNo(entity.getFromNo());
        dto.setMaxDoc(entity.getMaxDoc());
        dto.setReplenDoc(entity.getReplenDoc());
        dto.setSafelyDoc(entity.getSafelyDoc());
        dto.setToNo(entity.getToNo());
        dto.setSalesPro(entity.getSalesPro());
        dto.setWarehouse(entity.getWarehouseId());
        return dto;
    }


    @Override
    public SabcRule toEntity(SabcRuleDTO dto) {
        if (dto == null) {
            return null;
        }
        SabcRule entity = new SabcRule();
        entity.setId(dto.getId());
        entity.setFromNo(dto.getFromNo());
        entity.setMaxDoc(dto.getMaxDoc());
        entity.setReplenDoc(dto.getReplenDoc());
        entity.setSafelyDoc(dto.getSafelyDoc());
        entity.setToNo(dto.getToNo());
        entity.setSkuTypeName(dto.getSkuTypeName());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        entity.setSalesPro(dto.getSalesPro());
        return entity;
    }

    @Override
    public void updateEntityFromDTO(SabcRuleDTO dto, SabcRule entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setFromNo(dto.getFromNo());
        entity.setMaxDoc(dto.getMaxDoc());
        entity.setReplenDoc(dto.getReplenDoc());
        entity.setSafelyDoc(dto.getSafelyDoc());
        entity.setToNo(dto.getToNo());
        entity.setSkuTypeName(dto.getSkuTypeName());
    }
}

