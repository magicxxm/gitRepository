package com.mushiny.wms.masterdata.obbasics.crud.mapper;

import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PackingStationTypeDTO;
import com.mushiny.wms.masterdata.obbasics.domain.PackingStationType;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import org.springframework.stereotype.Component;

@Component
public class PackingStationTypeMapper implements BaseMapper<PackingStationTypeDTO, PackingStationType> {

    private final ApplicationContext applicationContext;
    private final WarehouseMapper warehouseMapper;

    public PackingStationTypeMapper(ApplicationContext applicationContext,
                                    WarehouseMapper warehouseMapper) {
        this.applicationContext = applicationContext;
        this.warehouseMapper = warehouseMapper;
    }

    @Override
    public PackingStationTypeDTO toDTO(PackingStationType entity) {
        if (entity == null) {
            return null;
        }
        PackingStationTypeDTO dto = new PackingStationTypeDTO(entity);

        dto.setName(entity.getName());
        dto.setPackStationType(entity.getPackStationType());
        dto.setDescription(entity.getDescription());
        dto.setIsScanBoxType(entity.getIsScanBoxType());
        dto.setIfScan(entity.getIfScan());
        dto.setIfPrint(entity.getIfPrint());
        dto.setIfScanInvoice(entity.getIfScanInvoice());
        dto.setIfWeight(entity.getIfWeight());

        dto.setWarehouse(entity.getWarehouseId());

        return dto;
    }

    @Override
    public PackingStationType toEntity(PackingStationTypeDTO dto) {
        if (dto == null) {
            return null;
        }
        PackingStationType entity = new PackingStationType();

        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setPackStationType(dto.getPackStationType());
        entity.setDescription(dto.getDescription());
        entity.setIsScanBoxType(dto.getIsScanBoxType());
        entity.setIfScan(dto.getIfScan());
        entity.setIfPrint(dto.getIfPrint());
        entity.setIfScanInvoice(dto.getIfScanInvoice());
        entity.setIfWeight(dto.getIfWeight());

        entity.setWarehouseId(applicationContext.getCurrentWarehouse());

        return entity;
    }

    @Override
    public void updateEntityFromDTO(PackingStationTypeDTO dto, PackingStationType entity) {

        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setPackStationType(dto.getPackStationType());
        entity.setDescription(dto.getDescription());
        entity.setIfScan(dto.getIfScan());
        entity.setIfPrint(dto.getIfPrint());
        entity.setIsScanBoxType(dto.getIsScanBoxType());
        entity.setIfScanInvoice(dto.getIfScanInvoice());
        entity.setIfWeight(dto.getIfWeight());

        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
    }
}
