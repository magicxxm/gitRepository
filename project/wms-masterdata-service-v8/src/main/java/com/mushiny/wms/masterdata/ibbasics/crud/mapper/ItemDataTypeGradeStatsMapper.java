package com.mushiny.wms.masterdata.ibbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.general.crud.mapper.ClientMapper;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.ItemDataTypeGradeStatsDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.ItemDataTypeGradeStats;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.ItemDataMapper;
import com.mushiny.wms.masterdata.mdbasics.repository.ItemDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemDataTypeGradeStatsMapper implements BaseMapper<ItemDataTypeGradeStatsDTO, ItemDataTypeGradeStats> {

    private final ItemDataRepository itemDataRepository;
    private final ApplicationContext applicationContext;
    private final ItemDataMapper itemDataMapper;
    private final ClientMapper clientMapper;
    private final WarehouseMapper warehouseMapper;

    @Autowired
    public ItemDataTypeGradeStatsMapper(
            ClientMapper clientMapper,
            WarehouseMapper warehouseMapper,
            ItemDataRepository itemDataRepository,
            ApplicationContext applicationContext, ItemDataMapper itemDataMapper) {
        this.itemDataRepository = itemDataRepository;
        this.applicationContext = applicationContext;
        this.itemDataMapper = itemDataMapper;
        this.clientMapper = clientMapper;
        this.warehouseMapper = warehouseMapper;
    }

    @Override
    public ItemDataTypeGradeStatsDTO toDTO(ItemDataTypeGradeStats entity) {
        if (entity == null) {
            return null;
        }

        ItemDataTypeGradeStatsDTO dto = new ItemDataTypeGradeStatsDTO(entity);

        dto.setShipmentDay(entity.getShipmentDay());
        dto.setUnitsShipment(entity.getUnitsShipment());
        dto.setUnitsDay(entity.getUnitsDay());
        dto.setAdjustExpireDate(entity.getAdjustExpireDate());
        dto.setWarehouse(entity.getWarehouseId());
        dto.setClient(entity.getClientId());
        dto.setItemData(itemDataMapper.toDTO(entity.getItemData()));
        dto.setSkuGrade(entity.getSkuGrade());
        dto.setAlterState(entity.getAlterState());
        dto.setSkuAdjustGrade(entity.getSkuAdjustGrade());
        return dto;
    }

    @Override
    public ItemDataTypeGradeStats toEntity(ItemDataTypeGradeStatsDTO dto) {
        if (dto == null) {
            return null;
        }
        ItemDataTypeGradeStats entity = new ItemDataTypeGradeStats();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setShipmentDay(dto.getShipmentDay());
        entity.setUnitsShipment(dto.getUnitsShipment());
        entity.setUnitsDay(dto.getUnitsDay());
        entity.setAdjustExpireDate(dto.getAdjustExpireDate());
        entity.setAlterState(dto.getAlterState());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        entity.setClientId(applicationContext.getCurrentClient());
        if (dto.getItemDataId() != null) {
            entity.setItemData(itemDataRepository.retrieve(dto.getItemDataId()));
        }

        return entity;
    }

    @Override
    public void updateEntityFromDTO(ItemDataTypeGradeStatsDTO dto, ItemDataTypeGradeStats entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setShipmentDay(dto.getShipmentDay());
        entity.setUnitsShipment(dto.getUnitsShipment());
        entity.setUnitsDay(dto.getShipmentDay().multiply(dto.getUnitsShipment()));
        entity.setAdjustExpireDate(dto.getAdjustExpireDate());
        if(!dto.getSkuAdjustGrade().equals(entity.getSkuAdjustGrade())){
            entity.setAlterState(1);
        }
        entity.setSkuAdjustGrade(dto.getSkuAdjustGrade());
        if (dto.getItemDataId() != null) {
            entity.setItemData(itemDataRepository.retrieve(dto.getItemDataId()));
        }
    }
}
