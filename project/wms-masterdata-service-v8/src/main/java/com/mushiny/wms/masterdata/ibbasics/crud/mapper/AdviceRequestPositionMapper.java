package com.mushiny.wms.masterdata.ibbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.AdviceRequestPositionDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.AdviceRequestPosition;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.ItemDataMapper;
import com.mushiny.wms.masterdata.mdbasics.repository.ItemDataRepository;
import com.mushiny.wms.masterdata.general.crud.mapper.ClientMapper;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import com.mushiny.wms.masterdata.general.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdviceRequestPositionMapper implements BaseMapper<AdviceRequestPositionDTO, AdviceRequestPosition> {

    private final ItemDataRepository itemDataRepository;
    private final ApplicationContext applicationContext;
    private final ItemDataMapper itemDataMapper;
//    private final LotMapper lotMapper;
    private final AdviceRequestMapper adviceRequestMapper;
    private final ClientMapper clientMapper;
    private final WarehouseMapper warehouseMapper;
    private final ClientRepository clientRepository;

    @Autowired
    public AdviceRequestPositionMapper(ApplicationContext applicationContext,
                                       ClientMapper clientMapper,
                                       ItemDataMapper itemDataMapper,
                                       WarehouseMapper warehouseMapper,
                                       ItemDataRepository itemDataRepository,
                                       AdviceRequestMapper adviceRequestMapper,
                                       ClientRepository clientRepository) {
        this.applicationContext = applicationContext;
        this.clientMapper = clientMapper;
        this.itemDataMapper = itemDataMapper;
        this.warehouseMapper = warehouseMapper;
        this.itemDataRepository = itemDataRepository;
        this.adviceRequestMapper = adviceRequestMapper;
        this.clientRepository = clientRepository;
    }

    @Override
    public AdviceRequestPositionDTO toDTO(AdviceRequestPosition entity) {
        if (entity == null) {
            return null;
        }
        AdviceRequestPositionDTO dto = new AdviceRequestPositionDTO(entity);

        dto.setPositionNo(entity.getPositionNo());
        dto.setNotifiedAmount(entity.getNotifiedAmount());
        dto.setReceiptAmount(entity.getReceiptAmount());
        dto.setClient(clientRepository.retrieve(entity.getClientId()).getName());
        dto.setClientId(entity.getClientId());
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setItemData(itemDataMapper.toDTO(entity.getItemData()));
//        dto.setLot(lotMapper.toDTO(entity.getLot()));
        dto.setAdviceRequest(adviceRequestMapper.toDTO(entity.getAdviceRequest()));

        return dto;
    }

    @Override
    public AdviceRequestPosition toEntity(AdviceRequestPositionDTO dto) {
        if (dto == null) {
            return null;
        }
        AdviceRequestPosition entity = new AdviceRequestPosition();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setPositionNo(dto.getPositionNo());
        entity.setNotifiedAmount(dto.getNotifiedAmount());
        entity.setReceiptAmount(dto.getReceiptAmount());

        applicationContext.isCurrentClient(dto.getClientId());
        entity.setClientId(dto.getClientId());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        if (dto.getItemDataId() != null) {
            entity.setItemData(itemDataRepository.retrieve(dto.getItemDataId()));
        }

        return entity;
    }

    @Override
    public void updateEntityFromDTO(AdviceRequestPositionDTO dto, AdviceRequestPosition entity) {
    }
}
