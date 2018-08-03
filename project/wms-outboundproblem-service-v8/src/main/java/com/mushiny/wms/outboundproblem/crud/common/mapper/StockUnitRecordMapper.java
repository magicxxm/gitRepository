package com.mushiny.wms.outboundproblem.crud.common.mapper;

import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.outboundproblem.crud.common.dto.StockUnitRecordDTO;
import com.mushiny.wms.outboundproblem.domain.common.StockUnitRecord;
import org.springframework.stereotype.Component;

@Component
public class StockUnitRecordMapper implements BaseMapper<StockUnitRecordDTO, StockUnitRecord> {


    @Override
    public StockUnitRecordDTO toDTO(StockUnitRecord entity) {
        if (entity == null) {
            return null;
        }
        StockUnitRecordDTO dto = new StockUnitRecordDTO(entity);
        dto.setAmount(entity.getAmount());
        dto.setFromStockUnit(entity.getFromStockUnit());
        dto.setFromStorageLocation(entity.getFromStorageLocation());
        dto.setItemDataItemNo(entity.getItemNo());
        dto.setItemDataSku(entity.getSku());
        dto.setLot(entity.getLot());
        dto.setOperator(entity.getOperator());
        dto.setRecordCode(entity.getRecordCode());
        dto.setRecordTool(entity.getRecordTool());
        dto.setRecordType(entity.getRecordType());
        dto.setToStockUnit(entity.getToStockUnit());
        dto.setToStorageLocation(entity.getToStorageLocation());
        dto.setToUnitLoad(entity.getToUnitLoad());
        dto.setFromState(entity.getFromState());
        dto.setToState(entity.getToState());
        return dto;
    }

    @Override
    public StockUnitRecord toEntity(StockUnitRecordDTO dto) {
        if (dto == null) {
            return null;
        }
        StockUnitRecord entity = new StockUnitRecord();
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setAmount(dto.getAmount());
        entity.setFromStockUnit(dto.getFromStockUnit());
        entity.setFromStorageLocation(dto.getFromStorageLocation());
        entity.setItemNo(dto.getItemDataItemNo());
        entity.setSku(dto.getItemDataSku());
        entity.setLot(dto.getLot());
        entity.setOperator(dto.getOperator());
        entity.setRecordCode(dto.getRecordCode());
        entity.setRecordTool(dto.getRecordTool());
        entity.setRecordType(dto.getRecordType());
        entity.setToStockUnit(dto.getToStockUnit());
        entity.setToStorageLocation(dto.getToStorageLocation());
        entity.setToUnitLoad(dto.getToUnitLoad());
        entity.setFromState(dto.getFromState());
        entity.setToState(dto.getToState());

        return entity;
    }

    @Override
    public void updateEntityFromDTO(StockUnitRecordDTO dto, StockUnitRecord entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setAmount(dto.getAmount());
        entity.setFromStockUnit(dto.getFromStockUnit());
        entity.setFromStorageLocation(dto.getFromStorageLocation());
        entity.setItemNo(dto.getItemDataItemNo());
        entity.setSku(dto.getItemDataSku());
        entity.setLot(dto.getLot());
        entity.setOperator(dto.getOperator());
        entity.setRecordCode(dto.getRecordCode());
        entity.setRecordTool(dto.getRecordTool());
        entity.setRecordType(dto.getRecordType());
        entity.setToStockUnit(dto.getToStockUnit());
        entity.setToStorageLocation(dto.getToStorageLocation());
        entity.setToUnitLoad(dto.getToUnitLoad());
        entity.setFromState(dto.getFromState());
        entity.setToState(dto.getToState());
    }
}

